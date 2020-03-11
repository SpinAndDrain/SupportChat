package de.spinanddrain.supportchat.spigot.command;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;

public class Reload extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	public Reload() {
		super("screload", Permissions.RELOAD, PLAYER, CONSOLE, COMMAND_BLOCK);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(Messages.RELOADING.getMessage());
			try {
				SpigotPlugin.provide().reload();
				sender.sendMessage(Messages.SUCCESSFULLY_RELOADED.getMessage());
			} catch(Exception e) {
				sender.sendMessage(Messages.RELOADING_FAILED.getMessage());
				Bukkit.getLogger().log(Level.WARNING, "[SupportChat] Reloading failed: " + e.getMessage());
//				e.printStackTrace();
			}
		} else
			sender.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_RELOAD.getWithoutPrefix())));
	}
	
}
