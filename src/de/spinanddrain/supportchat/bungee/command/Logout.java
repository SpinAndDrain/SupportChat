package de.spinanddrain.supportchat.bungee.command;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.conversation.Conversation;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Logout extends Command {

	/*
	 * Created by SpinAndDrain on 20.12.2019
	 */

	public Logout() {
		super("sclogout");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(Permissions.LOGIN.hasPermission(p)) {
				if(args.length == 0) {
					Supporter s = Supporter.cast(p);
					if(s != null) {
						if(s.isLoggedIn()) {
							s.setLoggedIn(false);
							Conversation c = BungeePlugin.provide().getConversationOf(p);
							if(c != null && c.isRunning()) {
								if(c.getListeners().contains(s)) {
									BungeeCord.getInstance().getPluginManager().dispatchCommand(p, "listen");
								} else if(c.getHandler() == s) {
									BungeePlugin.provide().endConversation(c);
								}
							}
							BungeePlugin.sendPluginMessage(p, "successfully-logged-out");
							for(Supporter i : BungeePlugin.provide().getOnlineSupporters()) {
								if(i != s && i.isLoggedIn() && !s.isHidden()) {
									BungeePlugin.sendPluginMessage(i.getSupporter(), "other-logout", new Placeholder("[player]", p.getName()));
								}
							}
							s.setHidden(false);
							BungeePlugin.provide().getAddons().sendActionBarByMode("on-supporter-logout", p);
						} else
							BungeePlugin.sendPluginMessage(p, "not-logged-in");
					} else
						BungeePlugin.sendPluginMessage(p, "no-permission");
				} else
					BungeePlugin.sendPluginMessage(p, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-logout", false)));
			} else
				BungeePlugin.sendPluginMessage(p, "no-permission");
		} else
			BungeePlugin.sendMessage("Invalid instance!");
	}
	
}