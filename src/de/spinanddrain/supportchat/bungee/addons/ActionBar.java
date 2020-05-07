package de.spinanddrain.supportchat.bungee.addons;

import java.util.concurrent.TimeUnit;

import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.configuration.Addons;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class ActionBar {

	/*
	 * Created by SpinAndDrain on 22.12.2019
	 */

	private boolean enable;
	private boolean enableFadeout;
	private String message;
	private String empty;
	private long cooldown;
	private ActionBarScheduler scheduler;
	private Plugin base;
	
	public ActionBar(boolean enable, boolean enableFadeout, String message, String empty, long cooldown, ActionBarScheduler scheduler,
			Plugin base) {
		this.enable = enable;
		this.enableFadeout = enableFadeout;
		this.message = message;
		this.empty = empty;
		this.cooldown = cooldown;
		this.base = base;
		this.scheduler = (enableFadeout ? new ActionBarScheduler(1000L, base) : scheduler);
		this.scheduler.start(this);
	}
	
	public void sendMessage(ProxiedPlayer player) {
		if(enable) {
			if(!enableFadeout) {
				ProxyServer.getInstance().getScheduler().schedule(base, new Runnable() {
					@Override
					public void run() {
						long state = System.currentTimeMillis();
						do {
							int size = BungeePlugin.provide().getOnlineLoggedInVisibleSupporters().size();
							sendActionBar((size > 0 ? message.replace("[count]", String.valueOf(size)) : empty.replace("[count]", String.valueOf(size))), player);
							long t = System.currentTimeMillis();
							while(System.currentTimeMillis() <= (t + 1000));
						} while(System.currentTimeMillis() <= (state + cooldown));
					}
				}, 1, TimeUnit.MILLISECONDS);
			} else {
				int size = BungeePlugin.provide().getOnlineLoggedInVisibleSupporters().size();
				sendActionBar((size > 0 ? message.replace("[count]", String.valueOf(size)) : empty.replace("[count]", String.valueOf(size))), player);
			}
		}
	}
	
	public void kill() {
		scheduler.stop();
	}
	
	private void sendActionBar(String message, ProxiedPlayer p) {
		p.sendMessage(ChatMessageType.ACTION_BAR, BungeePlugin.toColoredText(message));
	}
	
	public static ActionBar createOfConfig() {
		Addons a = BungeePlugin.provide().getAddons();
		return new ActionBar(a.getActionBarEnable(), a.getEnableFadeout(), a.getActionBarMessage(), a.getActionBarMessageEmpty(), a.getFadoutCooldown(),
				new ActionBarScheduler(a.getSendEach(), BungeePlugin.provide()), BungeePlugin.provide());
	}
	
}
