package de.spinanddrain.supportchat.spigot.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class SupporterLogoutEvent extends Event implements Cancellable {

	/*
	 * Created by SpinAndDrain on 15.10.2019
	 */
	
	private static HandlerList handlers = new HandlerList();
	private boolean cancel;
	
	private Supporter supporter;
	
	public SupporterLogoutEvent(Supporter supporter) {
		this.supporter = supporter;
	}
	
	public Supporter getSupporter() {
		return supporter;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
