package de.spinanddrain.supportchat.spigot.addons;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;
import net.ess3.api.events.AfkStatusChangeEvent;

public class AfkListener implements Listener {

	@EventHandler
	public void onAfk(AfkStatusChangeEvent event) {
		Supporter s = Supporter.cast(event.getAffected().getBase());
		if(s != null) {
			Bukkit.getScheduler().runTask(SpigotPlugin.provide(), () -> {
				SpigotPlugin.provide().callAFKEvent(new AFKHook(s, event.getValue()));
			});
		}
	}
	
}
