package de.spinanddrain.supportchat.bungee.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.ConfigAdapter;
import de.spinanddrain.supportchat.external.Time;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class Addons {

	/*
	 * Created by SpinAndDrain on 22.12.2019
	 */

	public enum ActionBarMode {
		NONE,
		ALL,
		ACTOR;
	}
	
	private ConfigAdapter adapter;
	
	public Addons() { 
		adapter = new ConfigAdapter(new File("plugins/SupportChat/addons.yml")) {
			@Override
			public void copyDefaults(Configuration cfg) {
				addDefault("faq.enable", true);
				addDefault("faq.message", Arrays.asList("&cF","&aA","&6Q"));
				addDefault("action-bar.enable", true);
				addDefault("action-bar.message", "&b[count] &cSupporters online");
				addDefault("action-bar.empty", "&cNo supporters online!");
				addDefault("action-bar.fadeout.enable", false);
				addDefault("action-bar.fadeout.cooldown", "3s");
				addDefault("action-bar.events.on-join", "ALL");
				addDefault("action-bar.events.on-supporter-login", "ALL");
				addDefault("action-bar.events.on-supporter-logout", "ALL");
				addDefault("action-bar.events.on-loglist-view", "ACTOR");
				addDefault("action-bar.events.send-each", "0ms");
			}
		};
	}
	
	public ConfigAdapter getAdapter() {
		return adapter;
	}
	
	public List<String> getFAQ() {
		return adapter.cfg.getStringList("faq.message");
	}
	
	public boolean getActionBarEnable() {
		return adapter.cfg.getBoolean("action-bar.enable");
	}
	
	public String getActionBarMessage() {
		return adapter.cfg.getString("action-bar.message").replaceAll("&", "§");
	}
	
	public String getActionBarMessageEmpty() {
		return adapter.cfg.getString("action-bar.empty").replaceAll("&", "§");
	}
	
	public boolean getEnableFadeout() {
		return adapter.cfg.getBoolean("action-bar.fadeout.enable");
	}

	public Time getFadoutCooldown() {
		return Time.fromString(adapter.cfg.getString("action-bar.fadeout.cooldown"));
	}
	
	public ActionBarMode getEvent(String event) {
		return ActionBarMode.valueOf(adapter.cfg.getString("action-bar.events." + event));
	}
	
	public Time getSendEach() {
		return Time.fromString(adapter.cfg.getString("action-bar.events.send-each"));
	}
	
	public void sendActionBarByMode(String event, ProxiedPlayer localActor) {
		ActionBarMode mode = getEvent(event);
		switch(mode) {
		case NONE:
			break;
		case ALL:
			ProxyServer.getInstance().getPlayers().forEach(player -> BungeePlugin.provide().getActionBar().sendMessage(player));
			break;
		case ACTOR:
			BungeePlugin.provide().getActionBar().sendMessage(localActor);
			break;
		default:
			break;
		}
	}
	
}
