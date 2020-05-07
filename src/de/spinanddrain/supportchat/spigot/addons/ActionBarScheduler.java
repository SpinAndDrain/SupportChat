package de.spinanddrain.supportchat.spigot.addons;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ActionBarScheduler {

	/*
	 * Created by SpinAndDrain on 15.10.2019
	 */

	private int id;
	private long schedule;
	private Plugin base;
	
	public ActionBarScheduler(long schedule, Plugin base) {
		this.schedule = schedule;
		this.base = base;
	}
	
	public void start(ActionBar actionbar) {
		if(shouldRun()) {
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(base, new Runnable() {
				@Override
				public void run() {
					if(shouldRun()) {
						Bukkit.getOnlinePlayers().forEach(player -> actionbar.sendMessage(player));
					} else {
						Bukkit.getScheduler().cancelTask(id);
					}
				}
			}, 20, (schedule / 1000) * 20);
		}
	}
	
	public void stop() {
		if(Bukkit.getScheduler().isCurrentlyRunning(id)) {
			Bukkit.getScheduler().cancelTask(id);
		}
	}
	
	public boolean shouldRun() {
		return (schedule != 0);
	}
	
}
