package de.spinanddrain.supportchat.spigot.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.event.ViewLoglistEvent;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Loglist extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	public Loglist() {
		super("loglist", Permissions.LOGLIST, PLAYER, CONSOLE, COMMAND_BLOCK);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		if(args.length == 0) {
			String result = new String();
			List<Supporter> list = SpigotPlugin.provide().getOnlineSupporters();
			boolean one = false;
			for(Supporter i : list) {
				if(i.isLoggedIn()) {
					one = true;
				}
			}
			if(!list.isEmpty() && one) {
				for(Supporter s : list) {
					if(s.isLoggedIn()) {
						if(s.isHidden()) {
							result += (Permissions.LOGIN_HIDDEN.hasPermission(sender) ? "§e" + s.getSupporter().getName() : new String())
									+ (list.indexOf(s) == (list.size() - 1) ? new String() : ", ");
						} else {
							result += "§a" + s.getSupporter().getName() + (list.indexOf(s) == (list.size() - 1) ? new String() : ", ");
						}
					}
				}
				sender.sendMessage(Messages.LOGGED_PLAYERS.getWithPlaceholder(Placeholder.create("[players]", result)));
				if(sender instanceof Player) {
					Bukkit.getPluginManager().callEvent(new ViewLoglistEvent((Player) sender));
				}
			} else
				sender.sendMessage(Messages.NOBODY_ONLINE.getMessage());
		} else
			sender.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_LOGLIST.getWithoutPrefix())));
	}
	
}
