package de.spinanddrain.supportchat.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.spinanddrain.supportchat.spigot.gui.InventoryWindow;

public class WindowOpenEvent extends Event implements Cancellable {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */
	
	private static HandlerList handlers = new HandlerList();
	private boolean cancel;
	
	private InventoryWindow window;
	private Player who;
	
	public WindowOpenEvent(InventoryWindow window, Player who) {
		this.cancel = false;
		this.who = who;
		this.window = window;
	}
	
	public InventoryWindow getWindow() {
		return window;
	}
	
	public Player getPlayer() {
		return who;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
