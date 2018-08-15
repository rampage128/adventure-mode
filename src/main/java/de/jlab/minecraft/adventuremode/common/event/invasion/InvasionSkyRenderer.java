package de.jlab.minecraft.adventuremode.common.event.invasion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InvasionSkyRenderer extends IRenderHandler {

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		/*
	    ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int uiWidth 	= res.getScaledWidth();
		int uiHeight 	= res.getScaledHeight();
		
		Gui.drawRect(0, 0, uiWidth, uiHeight, 0xFFFFFF);
		*/
	}
	
    /**
     *  Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color 
     */
	/*
    public static void drawRect(int par0, int par1, int par2, int par3, int par4) {
        int j1;

        if (par0 < par2)
        {
            j1 = par0;
            par0 = par2;
            par2 = j1;
        }

        if (par1 < par3)
        {
            j1 = par1;
            par1 = par3;
            par3 = j1;
        }

        float f = (float)(par4 >> 24 & 255) / 255.0F;
        float f1 = (float)(par4 >> 16 & 255) / 255.0F;
        float f2 = (float)(par4 >> 8 & 255) / 255.0F;
        float f3 = (float)(par4 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)par0, (double)par3, 0.0D);
        tessellator.addVertex((double)par2, (double)par3, 0.0D);
        tessellator.addVertex((double)par2, (double)par1, 0.0D);
        tessellator.addVertex((double)par0, (double)par1, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    */
}
