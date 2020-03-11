package de.spinanddrain.supportchat.bungee.command;

import java.util.logging.Level;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class Reload extends Command {

	/*
	 * Created by SpinAndDrain on 20.12.2019
	 */

	public Reload() {
		super("screload");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(Permissions.RELOAD.hasPermission(sender)) {
			if(args.length == 0) {
				BungeePlugin.sendPluginMessage(sender, "reloading");
				try {
					BungeePlugin.provide().reload();
					BungeePlugin.sendPluginMessage(sender, "successfully-reloaded");
				} catch(Exception e) {
					BungeePlugin.sendPluginMessage(sender, "reloading-failed");
					ProxyServer.getInstance().getLogger().log(Level.WARNING, "[SupportChat] Reloading failed: " + e.getMessage());
				}
			} else
				BungeePlugin.sendPluginMessage(sender, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-reload", false)));
		} else
			BungeePlugin.sendPluginMessage(sender, "no-permission");
	}
	
}
