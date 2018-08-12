package de.jlab.minecraft.mods.adventuremode.event;

import java.util.ArrayList;

import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

public class GuiEventIndicator extends Gui {
	
	private static final ResourceLocation dialog_background = new ResourceLocation("adventuremode", "textures/gui/event_background.png");
	
	private Minecraft mc;

	public GuiEventIndicator(Minecraft mc) {
		super();
		    
		// We need this to invoke the render engine.
		this.mc = mc;
	}
	
	//
	// This event is called by GuiIngameForge during each frame by
	// GuiIngameForge.pre() and GuiIngameForce.post().
	//
	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderEventIndicator(RenderGameOverlayEvent event) {
	
		// 
	    // We draw after the ExperienceBar has drawn.  The event raised by GuiIngameForge.pre()
	    // will return true from isCancelable.  If you call event.setCanceled(true) in
	    // that case, the portion of rendering which this event represents will be canceled.
	    // We want to draw *after* the experience bar is drawn, so we make sure isCancelable() returns
	    // false and that the eventType represents the ExperienceBar event.
	    if(event.isCancelable() || event.type != ElementType.EXPERIENCE) {      
	    	return;
	    }
	        
	    // get events
	    StringBuilder textBuilder = new StringBuilder();
		ArrayList<Event> affectingEventList = AdventureMode.instance.getEventHandler().getAffectingEvents(this.mc.thePlayer);
	    for (Event adventureEvent : affectingEventList) {
    		textBuilder.append(adventureEvent.getLabelText()).append("\n");
	    }
	    //String events = AdventureMode.instance.getEventHandler().getClientAffectingEvents(this.mc.thePlayer);
	    
	    if (affectingEventList == null || affectingEventList.isEmpty()) {
	    	return;
	    } 
	    
	    ScaledResolution res = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int uiWidth 	= res.getScaledWidth();
		int uiHeight 	= res.getScaledHeight();
		//mc.entityRenderer.setupOverlayRendering();
				
		drawEventBox(uiWidth, uiHeight);
		drawAreaText(textBuilder.toString(), uiWidth - 160 + 8, 40, 146, 48, 0xFFFFFF, 0, this.mc.fontRenderer);		
	}
	
	private void drawEventBox(int screenWidth, int screenHeight) {
		this.mc.func_110434_K().func_110577_a(dialog_background);
	    drawTexturedModalRect(screenWidth - 160, 32, 0, 0, 160, 64);
	}
	
	/**
	 * Height of default font
	 */
	public static int FONT_HEIGHT = 9;
	
	private void drawAreaText(String text, int x, int y, int width, int height, int color, int lineMargin, FontRenderer fontRenderer) {
		String[] lines = text.split("\n");
		
		int lineHeight = lineMargin + FONT_HEIGHT;
		
		int linesTotal 		= (int)Math.floor(height / lineHeight);
		int linesAvailable 	= linesTotal;
		int linesNeeded 	= 0;
		int offset			= 0;
		for (int i = 0; i < lines.length; i++) {
			offset = lineHeight * (linesTotal - linesAvailable);
			String line = lines[i];
			linesAvailable -= (int)Math.ceil(fontRenderer.getStringWidth(line) / 160f);
    		if (linesAvailable < 0) {
    			break;
    		}
    		
			fontRenderer.drawSplitString(line, x, y + offset, width, color);
		}
	}
	
}

