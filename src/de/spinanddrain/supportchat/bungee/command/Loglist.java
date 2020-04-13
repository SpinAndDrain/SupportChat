package de.spinanddrain.supportchat.bungee.command;

import java.util.List;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Loglist extends Command {

	/*
	 * Created by SpinAndDrain on 20.12.2019
	 */

	public Loglist() {
		super("loglist");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(Permissions.LOGLIST.hasPermission(sender)) {
			if(args.length == 0) {
				String result = new String();
				List<Supporter> list = BungeePlugin.provide().getOnlineSupporters();
				boolean one = false;
				for(Supporter i : list) {
					if(i.isLoggedIn()) {
						one = true;
					}
				}
				if(!list.isEmpty() && one) {
					for(Supporter s : list) {
						if(s.isLoggedIn()) {
							if(s.isHidden()) {
								result += (Permissions.LOGIN_HIDDEN.hasPermission(sender) ? "§e" + s.getSupporter().getName() : new String())
										+ (list.indexOf(s) == (list.size() - 1) ? new String() : ", ");
							} else {
								result += "§a" + s.getSupporter().getName() + (list.indexOf(s) == (list.size() - 1) ? new String() : ", ");
							}
						}
					}
					BungeePlugin.sendPluginMessage(sender, "logged-players", new Placeholder("[players]", result));
					if(sender instanceof ProxiedPlayer) {
						BungeePlugin.provide().getAddons().sendActionBarByMode("on-loglist-view", (ProxiedPlayer) sender);
					}
				} else
					BungeePlugin.sendPluginMessage(sender, "nobody-online");
			} else
				BungeePlugin.sendPluginMessage(sender, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-loglist", false)));
		} else
			BungeePlugin.sendPluginMessage(sender, "no-permission");
	}
	
}
