package de.spinanddrain.supportchat.bungee.addons;

import java.util.concurrent.TimeUnit;

import de.spinanddrain.supportchat.external.Time;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class ActionBarScheduler {

	/*
	 * Created by SpinAndDrain on 22.12.2019
	 */

	private ScheduledTask id;
	private Time schedule;
	private Plugin base;
	
	public ActionBarScheduler(Time schedule, Plugin base) {
		this.schedule = schedule;
		this.base = base;
	}
	
	public void start(ActionBar actionbar) {
		if(shouldRun()) {
			id = ProxyServer.getInstance().getScheduler().schedule(base, new Runnable() {
				@Override
				public void run() {
					if(shouldRun()) {
						ProxyServer.getInstance().getPlayers().forEach(player -> actionbar.sendMessage(player));
					} else {
						ProxyServer.getInstance().getScheduler().cancel(id);
					}
				}
			}, 1, schedule.toSeconds(), TimeUnit.SECONDS);
		}
	}
	
	public void stop() {
		if(id != null) {
			ProxyServer.getInstance().getScheduler().cancel(id);
		}
	}
	
	public boolean shouldRun() {
		return (schedule != null && schedule.toMilliseconds() != 0);
	}
	
}
