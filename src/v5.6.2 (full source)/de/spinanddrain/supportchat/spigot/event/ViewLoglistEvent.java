package de.spinanddrain.supportchat.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ViewLoglistEvent extends Event implements Cancellable {
	
	/*
	 * Created by SpinAndDrain on 15.10.2019
	 */
	
	private static HandlerList handlers = new HandlerList();
	private boolean cancel;
	
	private Player viewer;
	
	public ViewLoglistEvent(Player viewer) {
		this.viewer = viewer;
	}
	
	public Player getViewer() {
		return viewer;
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
