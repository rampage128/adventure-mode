package de.jlab.minecraft.adventuremode.client.event;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.common.event.Event;
import de.jlab.minecraft.adventuremode.common.event.PositionalEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DebugEventIndicator {

	@SubscribeEvent
	public void renderWorld(RenderWorldLastEvent event) {
		if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			return;
		}
		
		Event[] events = AdventureMode.instance.getEventScheduler().getActiveEvents();
		for (Event adventureEvent : events) {
	 		if (adventureEvent instanceof PositionalEvent) {
				this.renderSphere(((PositionalEvent)adventureEvent).getPosition(), ((PositionalEvent)adventureEvent).getRadius(), Color.YELLOW, event);
				this.renderSphere(((PositionalEvent)adventureEvent).getPosition(), 1, Color.RED, event);
			}
		}
	}
	
	private void renderSphere(BlockPos center, double radius, Color color, RenderWorldLastEvent evt) {
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glPointSize(2f);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		//Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)evt.getPartialTicks();
		double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)evt.getPartialTicks();
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)evt.getPartialTicks();
		worldRenderer.setTranslation(-d0, -d1, -d2);
		        
        worldRenderer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        for (Vec3d point : buildPoints(new Vec3d(center), radius)) {
            worldRenderer.pos(point.x, point.y, point.z)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), 255)
                    .endVertex();
        }
        tessellator.draw();
        worldRenderer.setTranslation(0, 0, 0);
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
	
	private Set<Vec3d> buildPoints(Vec3d center, double radius) {
        Set<Vec3d> points = new HashSet<>(1200);
        
        double tau = 6.283185307179586D;
        double pi = tau / 2D;
        double segment = tau / 48D;

        for (double t = 0.0D; t < tau; t += segment)
            for (double theta = 0.0D; theta < pi; theta += segment) {
                double dx = radius * Math.sin(t) * Math.cos(theta);
                double dz = radius * Math.sin(t) * Math.sin(theta);
                double dy = radius * Math.cos(t);

                points.add(center.addVector(dx, dy, dz));
            }
        return points;
    }
	
}
