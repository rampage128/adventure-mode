package de.jlab.minecraft.adventuremode.client.event;

import java.util.ArrayList;
import java.util.List;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.common.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventIndicator extends Gui {
	
	private static final ResourceLocation DIALOG_BACKGROUND = new ResourceLocation("adventuremode", "textures/gui/event_background.png");
	
	private static final int FONT_HEIGHT = 9;
	private static final int DIALOG_WIDTH = 160;
	private static final int DIALOG_HEIGHT = 256;
	private static final int DIALOG_BORDER = 8;
		
	private static final int EVENT_DISPLAY_COUNT = 2;
	
	private Minecraft mc;

	public GuiEventIndicator(Minecraft mc) {
		super();
		    
		this.mc = mc;
	}
	
	@SubscribeEvent
	public void onRenderEventIndicator(RenderGameOverlayEvent event) {
		// Draw after experience bar
	    if(event.isCancelable() || event.getType() != ElementType.EXPERIENCE) {      
	    	return;
	    }
	        
	    // get events
	    ArrayList<Event> affectingEventList = AdventureMode.instance.getEventScheduler().getAffectingEvents(this.mc.player);
	    if (affectingEventList == null || affectingEventList.isEmpty()) {
	    	return;
	    }
	    
	    // build text lines from events
	    List<String> lines = buildTextLines(affectingEventList, DIALOG_WIDTH - DIALOG_BORDER * 2);

	    // draw notification
		int x = DIALOG_BORDER;
		int y = DIALOG_BORDER;
		drawEventBox(x, y, lines.size() * FONT_HEIGHT);
		drawAreaText(lines, x + DIALOG_BORDER, y + DIALOG_BORDER, 0xFFFFFF, FONT_HEIGHT, this.mc.fontRenderer);
		
	}
	
	private List<String> buildTextLines(List<Event> events, int width) {
		StringBuilder textBuilder = new StringBuilder();
		
	    for (int i = 0; i < events.size(); i++) {
	    	if (i+1 > EVENT_DISPLAY_COUNT) {
	    		break;
	    	}
	    	Event event = events.get(i);
	    	if (i > 0) {
	    		textBuilder.append("\n\n");
	    	}
    		textBuilder.append(event.getLabelText());
	    }
	    
	    if (events.size() > EVENT_DISPLAY_COUNT) {
	    	textBuilder.append("\n\nAnd " + (events.size() - EVENT_DISPLAY_COUNT) + " more ...");
	    }
	    
	    return this.mc.fontRenderer.listFormattedStringToWidth(textBuilder.toString(), width);
	}
	
	private void drawEventBox(int x, int y, int textHeight) {		
		this.mc.renderEngine.bindTexture(DIALOG_BACKGROUND);
		drawTexturedModalRect(x, y, 0, 0, DIALOG_WIDTH, DIALOG_BORDER);
		drawTexturedModalRect(x, y + DIALOG_BORDER, 0, DIALOG_BORDER, DIALOG_WIDTH, textHeight);
	    drawTexturedModalRect(x, y + DIALOG_BORDER + textHeight, 0, DIALOG_HEIGHT - DIALOG_BORDER, DIALOG_WIDTH, DIALOG_BORDER);
	}
		
	private void drawAreaText(List<String> lines, int x, int y, int color, int lineHeight, FontRenderer fontRenderer) {
		for (int i = 0; i < lines.size(); i++) {
			fontRenderer.drawString(lines.get(i), x, y + lineHeight * i, color);
		}
	}
	
}

