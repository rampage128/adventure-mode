package de.jlab.minecraft.adventuremode.common.commands;

import java.util.ArrayList;
import java.util.List;

import de.jlab.minecraft.adventuremode.AdventureMode;
import de.jlab.minecraft.adventuremode.common.event.EventScheduler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;


public class EventCommand extends CommandBase {

	public static final String COMMAND_START 	= "start";
	public static final String COMMAND_STOP 	= "stop";
	
	private List<String> aliases;
	
	public EventCommand() {
		this.aliases = new ArrayList<String>();
		//this.aliases.add("invasion");
	}
	
	@Override
	public String getName() {
		return "event";
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "/event <start|stop> <type> [<player>]";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] astring) {
		if (astring.length < 1) {
			sender.sendMessage(new TextComponentString(this.getUsage(sender)));
		} else {
			if (COMMAND_START.equalsIgnoreCase(astring[0])) {
				this.startEvent(sender, astring);
			} else if (COMMAND_STOP.equalsIgnoreCase(astring[0])) {
				this.stopEvent(sender, astring);
			} else {
				sender.sendMessage(new TextComponentString("Could not find action [" + astring[0] + "]"));
			}
		}
	}
	
	private void startEvent(ICommandSender sender, String[] astring) {
		if (astring.length < 2) {
			sender.sendMessage(new TextComponentString(this.getUsage(sender)));
		} else {
			EntityPlayer usePlayer = null;			
			if (astring.length == 2) {
				usePlayer = (EntityPlayer)sender;				
			} else {
				List<EntityPlayer> playerList = sender.getEntityWorld().playerEntities;
				for (int i = 0; i < playerList.size(); i++) {
					EntityPlayer player = playerList.get(i);
					if (player.getName().equalsIgnoreCase(astring[2])) {
						usePlayer = player;
					}
				}
				
				if (usePlayer == null) {
					sender.sendMessage(new TextComponentString("Could not find player with name [" + astring[2] + "]!"));
				}
			}
			
			if (AdventureMode.instance.getEventScheduler().getEventGenerator(astring[1]) == null) {
				sender.sendMessage(new TextComponentString("Could not find event type " + astring[1]));
			} else {
				sender.sendMessage(new TextComponentString("Craeting event [" + astring[1] + "] at player [" + usePlayer.getName() + "]"));
				AdventureMode.instance.getEventScheduler().startEvent(astring[1], usePlayer);
			}
		}
	}
	
	private void stopEvent(ICommandSender sender, String[] astring) {
		if (astring.length < 2) {
			sender.sendMessage(new TextComponentString(this.getUsage(sender)));
		} else {
			if (!EventScheduler.EVENTTYPE_ALL.equalsIgnoreCase(astring[1]) && AdventureMode.instance.getEventScheduler().getEventGenerator(astring[1]) == null) {
				sender.sendMessage(new TextComponentString("Could not find event type " + astring[1]));
			} else {
				AdventureMode.instance.getEventScheduler().stopEvents(astring[1]);
				sender.sendMessage(new TextComponentString("Stopping all events of type [" + astring[1] + "]!"));
			}
		}	
	}

	/*
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return icommandsender.canCommandSenderUseCommand(4, this.getCommandName());
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}
	*/	
}
