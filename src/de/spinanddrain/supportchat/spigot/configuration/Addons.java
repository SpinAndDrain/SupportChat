package de.spinanddrain.supportchat.spigot.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Addons {

	/*
	 * Created by SpinAndDrain on 14.10.2019
	 */

	public enum EventMode {
		
		ALL,
		ACTOR,
		NONE;
		
	}
	
	private static Addons instance;
	
	private ConfigurationHandler handler;
	
	private Addons(final String version) {
		instance = this;
		this.handler = new ConfigurationHandler(new File("plugins/SupportChat/addons/addons.yml"));
		handler.getBuilder().preBuild("Addons v" + version + " | Event-Modes: ALL, ACTOR, NONE | Timevalues: ms, s, m, h, d, w, mo, y")
			.add("faq.enable", true)
			.add("faq.message", Arrays.asList("&cF","&aA","&6Q"))
			.add("action-bar.enable", true)
			.add("action-bar.message", "&b[count] &cSupporters online")
			.add("action-bar.empty", "&cNo supporters online!")
			.add("action-bar.fadeout.enable", false)
			.add("action-bar.fadeout.cooldown", "3s")
			.add("action-bar.events.on-join", EventMode.ALL.toString())
			.add("action-bar.events.on-move", EventMode.NONE.toString())
			.add("action-bar.events.on-supporter-login", EventMode.ALL.toString())
			.add("action-bar.events.on-supporter-logout", EventMode.ALL.toString())
			.add("action-bar.events.on-loglist-view", EventMode.ACTOR.toString())
			.add("action-bar.events.send-each", "0ms")
			.add("essentials-afk-hook.enable", false)
			.add("essentials-afk-hook.login", "&7~ You are logged in again")
			.add("essentials-afk-hook.logout", "&7~ You got logged out")
			.add("essentials-afk-hook.login-notification", "&b[player] &egot logged in automatically.")
			.add("essentials-afk-hook.logout-notification", "&b[player] &egot logged out automatically.")
			.build();
	}
	
	public static void initial() {
		new Addons(SpigotPlugin.provide().getPluginVersion());
	}
	
	public ConfigurationHandler getConfigurationHandler() {
		return handler;
	}
	
	public static Addons provide() {
		return instance;
	}
	
	public EventMode getOnJoinMode() {
		return EventMode.valueOf(handler.configuration.getString("action-bar.events.on-join"));
	}
	
	public EventMode getOnMoveMode() {
		return EventMode.valueOf(handler.configuration.getString("action-bar.events.on-move"));
	}
	
	public EventMode getOnSupporterLoginMode() {
		return EventMode.valueOf(handler.configuration.getString("action-bar.events.on-supporter-login"));
	}
	
	public EventMode getOnSupporterLogoutMode() {
		return EventMode.valueOf(handler.configuration.getString("action-bar.events.on-supporter-logout"));
	}
	
	public EventMode getOnLoglistViewMode() {
		return EventMode.valueOf(handler.configuration.getString("action-bar.events.on-loglist-view"));
	}
	
	public long getScheduledDelay() {
		return SupportChat.getTime(handler.configuration.getString("action-bar.events.send-each"));
	}
	
	public boolean isFaqEnabled() {
		return handler.configuration.getBoolean("faq.enable");
	}
	
	public List<String> getFaqMessage() {
		return handler.configuration.getStringList("faq.message");
	}
	
	public boolean isActionBarEnabled() {
		return handler.configuration.getBoolean("action-bar.enable");
	}
	
	public String getActionBarMessage() {
		return handler.configuration.getString("action-bar.message");
	}
	
	public String getActionBarEmptyMessage() {
		return handler.configuration.getString("action-bar.empty");
	}
	
	public boolean isActionBarFadeoutEnabled() {
		return handler.configuration.getBoolean("action-bar.fadeout.enable");
	}
	
	public long getActionBarFadeoutCooldown() {
		return SupportChat.getTime(handler.configuration.getString("action-bar.fadeout.cooldown"));
	}
	
	public boolean isAFKHookEnabled() {
		return handler.configuration.getBoolean("essentials-afk-hook.enable");
	}
	
	public String getAFKHookLoginMessage() {
		return handler.configuration.getString("essentials-afk-hook.login").replace("&", "§");
	}
	
	public String getAFKHookLogoutMessage() {
		return handler.configuration.getString("essentials-afk-hook.logout").replace("&", "§");
	}
	
	public String getAFKHookLoginNotification(Supporter s) {
		return handler.configuration.getString("essentials-afk-hook.login-notification").replace("&", "§").replace("[player]", s.getSupporter().getName());
	}
	
	public String getAFKHookLogoutNotification(Supporter s) {
		return handler.configuration.getString("essentials-afk-hook.logout-notification").replace("&", "§").replace("[player]", s.getSupporter().getName());
	}
	
}
