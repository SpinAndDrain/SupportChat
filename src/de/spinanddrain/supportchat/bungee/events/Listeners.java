package de.spinanddrain.supportchat.bungee.events;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.Placeholder;
import de.spinanddrain.supportchat.bungee.command.Login;
import de.spinanddrain.supportchat.bungee.configuration.Config.Mode;
import de.spinanddrain.supportchat.bungee.conversation.Conversation;
import de.spinanddrain.supportchat.bungee.request.Request;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import de.spinanddrain.supportchat.external.sql.exception.QueryException;
import de.spinanddrain.supportchat.external.sql.exception.WrongDatatypeException;
import de.spinanddrain.supportchat.external.sql.overlay.DataValue;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Listeners implements Listener {

	/*
	 * Created by SpinAndDrain on 21.12.2019
	 */
	
	@EventHandler
	public void onJoin(PostLoginEvent event) {
		ProxiedPlayer p = event.getPlayer();
		BungeePlugin.provide().verifyPlayer(p);
		Mode mode = Mode.valueOf(BungeePlugin.getString(BungeePlugin.CONFIG, "join-login"));
		switch(mode) {
		case DISABLED:
			break;
		case FULL:
			if(Supporter.cast(p) != null) {
				Login.run(p, false);
			}
			break;
		case HIDDEN:
			if(Supporter.cast(p) != null) {
				Login.run(p, true);
			}
			break;
		case PERMISSION_RANGE:
			if(Supporter.cast(p) != null) {
				if(Permissions.LOGIN_HIDDEN.hasPermission(p)) {
					BungeeCord.getInstance().getPluginManager().dispatchCommand(p, "sclogin hidden");
				} else if(Permissions.LOGIN.hasPermission(p)) {
					BungeeCord.getInstance().getPluginManager().dispatchCommand(p, "sclogin");
				}
			}
			break;
		default:
			break;
		}
		BungeePlugin.provide().getAddons().sendActionBarByMode("on-join", p);
		if(BungeePlugin.provide().getSaver().use() && BungeePlugin.provide().getSql().isConnected()) {
			try {
				String reason = BungeePlugin.provide().getSql().getString(BungeePlugin.provide().getSaver().getDatabaseTable(), new DataValue("id", p.getUniqueId().toString()), "reason");
				if(reason != null) {
					BungeePlugin.provide().getRequests().add(new Request(p, reason));
				}
			} catch (WrongDatatypeException | QueryException e) {}
		}
	}

	@EventHandler
	public void onQuit(PlayerDisconnectEvent event) {
		ProxiedPlayer p = event.getPlayer();
		if(BungeePlugin.provide().isInMainConversation(p)) {
			BungeePlugin.provide().endConversation(BungeePlugin.provide().getConversationOf(p));
		}
		if(BungeePlugin.provide().hasRequested(p)) {
			BungeePlugin.provide().getRequests().remove(BungeePlugin.provide().getRequestOf(p.getName()));
		}
		Supporter s = Supporter.cast(p);
		if(s != null) {
			if(s.isLoggedIn()) {
				BungeeCord.getInstance().getPluginManager().dispatchCommand(p, "sclogout");
			}
			BungeePlugin.provide().getOnlineSupporters().remove(s);
		}
	}
	
	@EventHandler
	public void onChat(ChatEvent event) {
		ProxiedPlayer p = (ProxiedPlayer) event.getSender();
		if(!event.getMessage().startsWith("/") && BungeePlugin.provide().isInMainConversation(p)) {
			event.setCancelled(true);
			Conversation c = BungeePlugin.provide().getConversationOf(p);
			if(c.getRequest().getRequestor() == p) {
				BungeePlugin.sendPluginMessage(p, "chat-layout-local", new Placeholder("[message]", event.getMessage()));
				BungeePlugin.sendPluginMessage(c.getHandler().getSupporter(), "chat-layout", new Placeholder("[message]", event.getMessage()),
						new Placeholder("[player]", p.getName()));
			} else if(c.getHandler().getSupporter() == p) {
				BungeePlugin.sendPluginMessage(p, "chat-layout-local", new Placeholder("[message]", event.getMessage()));
				BungeePlugin.sendPluginMessage(c.getRequest().getRequestor(), "chat-layout", new Placeholder("[message]", event.getMessage()),
						new Placeholder("[player]", p.getName()));
			}
			for(Supporter i : c.getListeners()) {
				BungeePlugin.sendPluginMessage(i.getSupporter(), "chat-layout", new Placeholder("[message]", event.getMessage()),
						new Placeholder("[player]", p.getName()));
			}
		}
	}
	
}
