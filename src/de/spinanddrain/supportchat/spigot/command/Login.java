package de.spinanddrain.supportchat.spigot.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.addons.AFKHook;
import de.spinanddrain.supportchat.spigot.configuration.Addons;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.event.SupporterLoginEvent;
import de.spinanddrain.supportchat.spigot.event.SupporterLoginEvent.LoginAction;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Login extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	public Login() {
		super("sclogin", Permissions.LOGIN, PLAYER);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(args.length == 0) {
			run(player, false);
		} else if(args.length == 1 && Permissions.LOGIN_HIDDEN.hasPermission(player) && args[0].equalsIgnoreCase("hidden")) {
			run(player, true);
		} else
			player.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_LOGIN.getWithoutPrefix())));
	}
	
	public static void run(Player player, boolean hidden) {
		Supporter s = Supporter.cast(player);
		if(s != null) {
			if(!SpigotPlugin.provide().hasRequested(player)) {
				if(!s.isLoggedIn()) {
					s.setLoggedIn(true);
					s.setHidden(hidden);
					if(AFKHook.contains(s)) {
						player.sendMessage(Messages.PREFIX.getWithoutPrefix() + " " + Addons.provide().getAFKHookLoginMessage());
						for(Supporter i : SpigotPlugin.provide().getOnlineSupporters()) {
							if(i != s && i.isLoggedIn() && !hidden) {
								i.getSupporter().sendMessage(Messages.PREFIX.getWithoutPrefix() + " " + Addons.provide().getAFKHookLoginNotification(s));
							}
						}
					} else {
						player.sendMessage(Messages.SUCCESSFULLY_LOGGED_IN.getMessage());
						for(Supporter i : SpigotPlugin.provide().getOnlineSupporters()) {
							if(i != s && i.isLoggedIn() && !hidden) {
								i.getSupporter().sendMessage(Messages.OTHER_LOGIN.getWithPlaceholder(Placeholder.create("[player]", player.getName())));
							}
						}
					}
					Bukkit.getPluginManager().callEvent(new SupporterLoginEvent(s, (hidden ? LoginAction.HIDDEN : LoginAction.VISIBLE)));
				} else
					player.sendMessage(Messages.ALREADY_LOGGED_IN.getMessage());
			} else
				player.sendMessage(Messages.CLWR.getMessage());
		} else
			player.sendMessage(Messages.NO_PERMISSION.getMessage());
	}
	
}
