package de.spinanddrain.supportchat.bungee.command;

import java.sql.SQLException;

import de.spinanddrain.sql.Value;
import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Supportleave extends Command {

	public Supportleave(String name) {
		super(name);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(Permissions.SEND_REQUEST.hasPermission(p)) {
				if(args.length == 0) {
					if(BungeePlugin.provide().hasRequested(p)) {
						BungeePlugin.provide().getRequests().remove(BungeePlugin.provide().getRequestOf(p.getName()));
						BungeePlugin.provide().applyLastRequestToNow(p.getUniqueId());
						BungeePlugin.sendPluginMessage(p, "queue-left");
						if(BungeePlugin.provide().getSaver().use() && BungeePlugin.provide().getSql().isConnected()) {
							try {
								BungeePlugin.provide().getSql().delete(new Value("id", p.getUniqueId().toString()),
										BungeePlugin.provide().getTable());
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					} else
						BungeePlugin.sendPluginMessage(p, "not-requested");
				} else
					BungeePlugin.sendPluginMessage(p, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-leave",false)));
			} else
				BungeePlugin.sendPluginMessage(p, "no-permission");
		} else
			sender.sendMessage(new TextComponent("Invalid instance"));
	}
	
}
