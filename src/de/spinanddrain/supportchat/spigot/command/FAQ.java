package de.spinanddrain.supportchat.spigot.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.configuration.Addons;
import de.spinanddrain.supportchat.spigot.configuration.Messages;

public class FAQ extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 14.10.2019
	 */

	public FAQ() {
		super("faq", Permissions.NONE, PLAYER, CONSOLE, COMMAND_BLOCK);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		if(Addons.provide().isFaqEnabled()) {
			List<String> message = Addons.provide().getFaqMessage();
			for(String i : message) {
				sender.sendMessage(i.replaceAll("&", "§"));
			}
		} else
			sender.sendMessage(Messages.NO_PERMISSION.getMessage());
	}
	
}
