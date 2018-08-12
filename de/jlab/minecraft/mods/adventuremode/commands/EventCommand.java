package de.jlab.minecraft.mods.adventuremode.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import de.jlab.minecraft.mods.adventuremode.AdventureMode;
import de.jlab.minecraft.mods.adventuremode.event.EventGenerator;
import de.jlab.minecraft.mods.adventuremode.event.EventHandler;

public class EventCommand implements ICommand {

	public static final String COMMAND_START 	= "start";
	public static final String COMMAND_STOP 	= "stop";
	
	private List<String> aliases;
	
	public EventCommand() {
		this.aliases = new ArrayList<String>();
		//this.aliases.add("invasion");
	}
	
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub WTF?
		return 0;
	}

	@Override
	public String getCommandName() {
		return "event";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/event <start|stop> <type> [<player>]";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length < 1) {
			throw new WrongUsageException(this.getCommandUsage(icommandsender), new Object[0]);
		} else {
			if (COMMAND_START.equalsIgnoreCase(astring[0])) {
				this.startEvent(icommandsender, astring);
			} else if (COMMAND_STOP.equalsIgnoreCase(astring[0])) {
				this.stopEvent(icommandsender, astring);
			} else {
				throw new WrongUsageException("Could not find action [" + astring[0] + "]", new Object[0]);
			}
		}
	}
	
	private void startEvent(ICommandSender icommandsender, String[] astring) {
		if (astring.length < 2) {
			throw new WrongUsageException(this.getCommandUsage(icommandsender), new Object[0]);
		} else {
			EntityPlayerMP usePlayer = null;			
			if (astring.length == 2) {
				usePlayer = (EntityPlayerMP)icommandsender;				
			} else {
				List playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
				for (int i = 0; i < playerList.size(); i++) {
					EntityPlayerMP player = (EntityPlayerMP)playerList.get(i);
					if (player.username.equalsIgnoreCase(astring[2])) {
						usePlayer = player;
					}
				}
				
				if (usePlayer == null) {
					throw new WrongUsageException("Could not find player with name [" + astring[2] + "]!", new Object[0]);
				}
			}
			
			if (AdventureMode.instance.getEventHandler().getEventGenerator(astring[1]) == null) {
				throw new WrongUsageException("Could not find event type " + astring[1], new Object[1]);
			} else {
				icommandsender.sendChatToPlayer(ChatMessageComponent.func_111066_d("Craeting event [" + astring[1] + "] at player [" + usePlayer.username + "]"));
				AdventureMode.instance.getEventHandler().startEvent(astring[1], usePlayer);
			}
		}
	}
	
	private void stopEvent(ICommandSender icommandsender, String[] astring) {
		if (astring.length < 2) {
			throw new WrongUsageException(this.getCommandUsage(icommandsender), new Object[0]);
		} else {
			if (!EventHandler.EVENTTYPE_ALL.equalsIgnoreCase(astring[1]) && AdventureMode.instance.getEventHandler().getEventGenerator(astring[1]) == null) {
				throw new WrongUsageException("Could not find event type " + astring[1], new Object[0]);
			} else {
				AdventureMode.instance.getEventHandler().stopEvents(astring[1]);
				icommandsender.sendChatToPlayer(ChatMessageComponent.func_111066_d("Stopping all events of type [" + astring[1] + "]!"));
			}
		}	
	}

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
	
}
