package de.spinanddrain.supportchat.spigot.addons;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.spinanddrain.supportchat.external.Time;

public class ActionBarScheduler {

	/*
	 * Created by SpinAndDrain on 15.10.2019
	 */

	private int id;
	private Time schedule;
	private Plugin base;
	
	public ActionBarScheduler(Time schedule, Plugin base) {
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
			}, 20, (schedule.toSeconds() * 20));
		}
	}
	
	public void stop() {
		if(Bukkit.getScheduler().isCurrentlyRunning(id)) {
			Bukkit.getScheduler().cancelTask(id);
		}
	}
	
	public boolean shouldRun() {
		return (schedule != null && schedule.toMilliseconds() != 0);
	}
	
}
