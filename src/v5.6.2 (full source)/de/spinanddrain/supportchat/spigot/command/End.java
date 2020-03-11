package de.spinanddrain.supportchat.spigot.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.conversation.Conversation;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class End extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	public End() {
		super("scend", Permissions.END, PLAYER);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(args.length == 0) {
			Conversation c = SpigotPlugin.provide().getConversationOf(player);
			if(c != null) {
				Supporter s = Supporter.cast(player);
				if(s != null && c.getHandler() == s) {
					SpigotPlugin.provide().endConversation(c);
				} else
					player.sendMessage(Messages.ONLY_LEADERS.getMessage());
			} else
				player.sendMessage(Messages.NO_CONVERSATION.getMessage());
		} else
			player.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_END.getWithoutPrefix())));
	}
	
}
