package de.spinanddrain.supportchat.spigot.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class SupporterLoginEvent extends Event implements Cancellable {

	/*
	 * Created by SpinAndDrain on 15.10.2019
	 */
	
	public enum LoginAction {
		VISIBLE,
		HIDDEN,
		AUTOMATIC;
	}
	
	private static HandlerList handlers = new HandlerList();
	private boolean cancel;
	
	private Supporter supporter;
	private LoginAction action;
	
	public SupporterLoginEvent(Supporter supporter, LoginAction action) {
		this.supporter = supporter;
		this.action = action;
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

	public LoginAction getAction() {
		return action;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
