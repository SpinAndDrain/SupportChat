package de.spinanddrain.supportchat.spigot.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.conversation.Conversation;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Listen extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	public Listen() {
		super("listen", Permissions.LISTEN, PLAYER);
	}
	
	@Override
	public void runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(args.length == 0) {
			Supporter s = Supporter.cast(player);
			if(s != null) {
				if(s.isListening()) {
					Conversation c = SpigotPlugin.provide().getConversationOf(player);
					s.setListening(false);
					c.getListeners().remove(s);
					if(!s.isHidden()) {
						c.sendAllWithPermission(Messages.LISTEN_LEAVE.getWithPlaceholder(Placeholder.create("[player]", player.getName())),
								Permissions.LISTEN_NOTIFY);
					}
					player.sendMessage(Messages.SUCCESSFULLY_LEAVED.getMessage());
				} else
					player.sendMessage(Messages.NO_CONVERSATION.getMessage());
			} else
				player.sendMessage(Messages.NO_PERMISSION.getMessage());
		} else if(args.length == 1) {
			Supporter s = Supporter.cast(player);
			if(s != null) {
				if(!SpigotPlugin.provide().isInConversation(player)) {
					int id = asInt(args[0]);
					if(id != -1) {
						Conversation c = SpigotPlugin.provide().getConversationOf(id);
						if(c != null) {
							s.setListening(true);
							if(!s.isHidden()) {
								c.sendAllWithPermission(Messages.LISTEN_JOIN.getWithPlaceholder(Placeholder.create("[player]", player.getName())),
										Permissions.LISTEN_NOTIFY);
							}
							c.getListeners().add(s);
							player.sendMessage(Messages.SUCCESSFULLY_JOINED.getWithPlaceholder(Placeholder.create("[id]", String.valueOf(id))));
						} else
							player.sendMessage(Messages.INVALID_CONVERSATION_ID.getMessage());
					} else
						player.sendMessage(Messages.INVALID_CONVERSATION_ID.getMessage());
				} else
					player.sendMessage(Messages.ALREADY_IN_CONVERSATION.getMessage());
			} else
				player.sendMessage(Messages.NO_PERMISSION.getMessage());
		} else
			player.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_LISTEN.getWithoutPrefix())));
	}
	
	private int asInt(String number) {
		if(isInt(number)) {
			return Integer.parseInt(number); 
		}
		return -1;
	}
	
	private boolean isInt(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
}
