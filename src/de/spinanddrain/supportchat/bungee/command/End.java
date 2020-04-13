package de.spinanddrain.supportchat.bungee.command;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.conversation.Conversation;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class End extends Command {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	public End() {
		super("scend");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(Permissions.END.hasPermission(p)) {
				if(args.length == 0) {
					Conversation c = BungeePlugin.provide().getConversationOf(p);
					if(c != null) {
						Supporter s = Supporter.cast(p);
						if(s != null && c.getHandler() == s) {
							BungeePlugin.provide().endConversation(c);
						} else
							BungeePlugin.sendPluginMessage(p, "only-leaders");
					} else
						BungeePlugin.sendPluginMessage(p, "no-conversation");
				} else
					BungeePlugin.sendPluginMessage(p, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-end", false)));
			} else
				BungeePlugin.sendPluginMessage(p, "no-permission");
		} else
			sender.sendMessage(new TextComponent("Invalid instance"));
	}
	
}
