package de.spinanddrain.supportchat.spigot.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.conversation.Conversation;
import de.spinanddrain.supportchat.spigot.event.SupporterLogoutEvent;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Logout extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	public Logout() {
		super("sclogout", Permissions.LOGIN, PLAYER);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(args.length == 0) {
			Supporter s = Supporter.cast(player);
			if(s != null) {
				if(s.isLoggedIn()) {
					s.setLoggedIn(false);
					Conversation conversation = SpigotPlugin.provide().getConversationOf(player);
					if(conversation != null && conversation.isRunning()) {
						if(conversation.getListeners().contains(s)) {
							player.performCommand("listen");
						} else if(conversation.getHandler() == s) {
							SpigotPlugin.provide().endConversation(conversation);
						}
					}
					player.sendMessage(Messages.SUCCESSFULLY_LOGGED_OUT.getMessage());
					for(Supporter i : SpigotPlugin.provide().getOnlineSupporters()) {
						if(i != s && i.isLoggedIn() && !s.isHidden()) {
							i.getSupporter().sendMessage(Messages.OTHER_LOGOUT.getWithPlaceholder(Placeholder.create("[player]", player.getName())));
						}
					}
					s.setHidden(false);
					Bukkit.getPluginManager().callEvent(new SupporterLogoutEvent(s));
				} else
					player.sendMessage(Messages.NOT_LOGGED_IN.getMessage());
			} else
				player.sendMessage(Messages.NO_PERMISSION.getMessage());
		} else
			player.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_LOGOUT.getWithoutPrefix())));
	}
	
}
