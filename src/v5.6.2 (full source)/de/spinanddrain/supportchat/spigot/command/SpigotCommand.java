package de.spinanddrain.supportchat.spigot.command;

import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.configuration.Messages;

public abstract class SpigotCommand implements CommandExecutor {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	public static final byte PLAYER = 0, COMMAND_BLOCK = 1, CONSOLE = 2;
	
	private String name;
	private Permissions permission;
	private byte[] execFor;
	
	public SpigotCommand(String name, Permissions permission, byte... execFor) {
		this.name = name;
		this.permission = permission;
		this.execFor = execFor;
	}
	
	public abstract void runCommand(CommandSender sender, String[] args);
	
	public String getName() {
		return name;
	}
	
	public Permissions getPermission() {
		return permission;
	}
	
	public boolean canExecute(CommandSender sender) {
		for(int i = 0; i < execFor.length; i++) {
			switch(execFor[i]) {
			case PLAYER:
				if(sender instanceof Player) {
					return true;
				}
				break;
			case COMMAND_BLOCK:
				if(sender instanceof CommandBlock) {
					return true;
				}
				break;
			case CONSOLE:
				if(sender instanceof ConsoleCommandSender) {
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase(name)) {
			if(canExecute(sender)) {
				if(permission == Permissions.NONE) {
					runCommand(sender, args);
					return false;
				}
				if(permission.hasPermission(sender)) {
					runCommand(sender, args);
				} else
					sender.sendMessage(Messages.NO_PERMISSION.getMessage());
			} else
				sender.sendMessage(Messages.CANT_EXECUTE_COMMAND.getMessage());
		}
		return false;
	}
	
}
