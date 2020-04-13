package de.spinanddrain.supportchat.bungee.command;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.conversation.Conversation;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Listen extends Command {

	/*
	 * Created by SpinAndDrain on 20.12.2019
	 */

	public Listen() {
		super("listen");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(Permissions.LISTEN.hasPermission(p)) {
				if(args.length == 0) {
					Supporter s = Supporter.cast(p);
					if(s != null) {
						if(s.isListening()) {
							Conversation c = BungeePlugin.provide().getConversationOf(p);
							s.setListening(false);
							c.getListeners().remove(s);
							if(!s.isHidden()) {
								c.sendAllWithPermission(BungeePlugin.getMessage("listen-leave", true).replace("[player]", p.getName()), Permissions.LISTEN_NOTIFY);
							}
							BungeePlugin.sendPluginMessage(p, "successfully-leaved");
						} else
							BungeePlugin.sendPluginMessage(p, "no-conversation");
					} else
						BungeePlugin.sendPluginMessage(p, "no-permission");
				} else if(args.length == 1) {
					Supporter s = Supporter.cast(p);
					if(s != null) {
						if(!BungeePlugin.provide().isInConversation(p)) {
							int id = asInt(args[0]);
							if(id != -1) {
								Conversation c = BungeePlugin.provide().getConversationOf(id);
								if(c != null) {
									s.setListening(true);
									if(!s.isHidden()) {
										c.sendAllWithPermission(BungeePlugin.getMessage("listen-join", true).replace("[player]", p.getName()), Permissions.LISTEN_NOTIFY);
									}
									c.getListeners().add(s);
									BungeePlugin.sendPluginMessage(p, "successfully-joined", new Placeholder("[id]", String.valueOf(id)));
								} else
									BungeePlugin.sendPluginMessage(p, "invalid-conversation-id");
							} else
								BungeePlugin.sendPluginMessage(p, "invalid-conversation-id");
						} else
							BungeePlugin.sendPluginMessage(p, "already-in-conversation");
					} else
						BungeePlugin.sendPluginMessage(p, "no-permission");
				} else
					BungeePlugin.sendPluginMessage(p, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-listen", false)));
			} else
				BungeePlugin.sendPluginMessage(p, "no-permission");
		} else
			BungeePlugin.sendMessage("Invalid instance!");
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
