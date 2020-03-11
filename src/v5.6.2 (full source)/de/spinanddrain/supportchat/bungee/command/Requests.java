package de.spinanddrain.supportchat.bungee.command;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Requests extends Command {

	/*
	 * Created by SpinAndDrain on 20.12.2019
	 */

	public Requests(String name) {
		super(name);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(Permissions.SUPPORT.hasPermission(p)) {
				if(args.length == 0) {
					Supporter s = Supporter.cast(p);
					if(s != null && s.isLoggedIn()) {
						BungeeCord.getInstance().getPluginManager().dispatchCommand(p, "scb display 1");
					} else
						BungeePlugin.sendPluginMessage(p, "not-logged-in");
				} else
					BungeePlugin.sendPluginMessage(p, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-requests", false)));
			} else
				BungeePlugin.sendPluginMessage(p, "no-permission");
		} else
			BungeePlugin.sendMessage("Invalid instance!");
	}
	
}
