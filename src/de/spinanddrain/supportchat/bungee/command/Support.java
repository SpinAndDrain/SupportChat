package de.spinanddrain.supportchat.bungee.command;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.spinanddrain.sql.Value;
import de.spinanddrain.sql.exception.ConnectionException;
import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.configuration.Reasons.Mode;
import de.spinanddrain.supportchat.bungee.request.Request;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import de.spinanddrain.util.advanced.AdvancedString;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Support extends Command {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	public Support() {
		super("support");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(Permissions.SEND_REQUEST.hasPermission(p)) {
				if(args.length == 0 && BungeePlugin.provide().getReasons().getMode() == Mode.ABSOLUTE_DISABLED) {
					run(p, "-");
				} else if(args.length == 1) {
					String reason = args[0];
					if(BungeePlugin.provide().getReasons().getMode() == Mode.ENABLED) {
						List<String> reasons = BungeePlugin.provide().getReasons().getReasons();
						if(!reasons.contains(reason)) {
							BungeePlugin.sendPluginMessage(p, "syntax-reasons", new Placeholder("[reasons]", 
									AdvancedString.bind(", ", reasons.toArray(new String[reasons.size()]))));
							return;
						}
					}
					run(p, reason);
				} else
					BungeePlugin.sendPluginMessage(p, "syntax", new Placeholder("[command]", BungeePlugin.getMessage("syntax-support",false)));
			} else
				BungeePlugin.sendPluginMessage(p, "no-permission");
		} else
			sender.sendMessage(new TextComponent("Invalid instance"));
	}
	
	private void run(ProxiedPlayer p, String reason) {
		Supporter su = Supporter.cast(p);
		if(su == null || !su.isLoggedIn()) {
			if(!BungeePlugin.provide().hasRequested(p)) {
				long delay = SupportChat.getTime(BungeePlugin.getString(BungeePlugin.CONFIG, "request-delay"));
				Map<UUID, Long> m = BungeePlugin.provide().getLastRequest();
				UUID id = p.getUniqueId();
				if(delay > 0 && (!m.containsKey(id) || System.currentTimeMillis() > m.get(id) + delay)) {
					if(BungeePlugin.provide().getRequestOf(p.getName()) != null) {
						Request r = BungeePlugin.provide().getRequestOf(p.getName());
						r.setState(RequestState.OPEN);
						r.setReason(reason);
					} else
						BungeePlugin.provide().getRequests().add(new Request(p, reason));
					for(Supporter s : BungeePlugin.provide().getOnlineSupporters()) {
						if(s.isLoggedIn()) {
							BungeePlugin.sendPluginMessage(s.getSupporter(), "new-request");
						}
					}
					BungeePlugin.sendPluginMessage(p, "queue-joined");
					if(BungeePlugin.provide().getSaver().use() && BungeePlugin.provide().getSql().isConnected()) {
						try {
							BungeePlugin.provide().getSql().insert(BungeePlugin.provide().getTable(),
									new Value("id", p.getUniqueId().toString()), new Value("reason", reason),
									new Value("requesttime", System.currentTimeMillis()));
						} catch (ConnectionException e) {
							e.printStackTrace();
						}
					}
				} else
					BungeePlugin.sendPluginMessage(p, "requesting-too-fast", new Placeholder("[remaining]",
							SupportChat.convertLogical(m.get(id) + delay - System.currentTimeMillis())));
			} else
				BungeePlugin.sendPluginMessage(p, "already-in-queue");
		} else
			BungeePlugin.sendPluginMessage(p, "crwl");
	}
	
}
