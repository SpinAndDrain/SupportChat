package de.spinanddrain.supportchat.bungee.command;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Login extends Command {

	/*
	 * Created by SpinAndDrain on 20.12.2019
	 */

	public Login() {
		super("sclogin");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(Permissions.LOGIN.hasPermission(p)) {
				if(args.length == 0) {
					run(p, false);
				} else if(args.length == 1 && Permissions.LOGIN_HIDDEN.hasPermission(p) && args[0].equalsIgnoreCase("hidden")) {
					run(p, true);
				} else
					BungeePlugin.sendPluginMessage(p, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-login", false)));
			} else
				BungeePlugin.sendPluginMessage(p, "no-permission");
		} else
			BungeePlugin.sendMessage("Invalid instance!");
	}

	public static void run(ProxiedPlayer player, boolean hidden) {
		Supporter s = Supporter.cast(player);
		if(s != null) {
			if(!BungeePlugin.provide().hasRequested(player)) {
				if(!s.isLoggedIn()) {
					s.setLoggedIn(true);
					s.setHidden(hidden);
					BungeePlugin.sendPluginMessage(player, "successfully-logged-in");
					for(Supporter i : BungeePlugin.provide().getOnlineSupporters()) {
						if(i != s && i.isLoggedIn() && !hidden) {
							BungeePlugin.sendPluginMessage(i.getSupporter(), "other-login", new Placeholder("[player]", player.getName()));
						}
					}
					BungeePlugin.provide().getAddons().sendActionBarByMode("on-supporter-login", player);
				} else
					BungeePlugin.sendPluginMessage(player, "already-logged-in");
			} else
				BungeePlugin.sendPluginMessage(player, "clwr");
		} else
			BungeePlugin.sendPluginMessage(player, "no-permission");
	}
	
}
