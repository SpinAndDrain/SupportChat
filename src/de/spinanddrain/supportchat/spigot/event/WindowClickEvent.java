package de.spinanddrain.supportchat.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.spinanddrain.supportchat.spigot.gui.InventoryWindow;
import de.spinanddrain.supportchat.spigot.gui.Item;

public class WindowClickEvent extends Event implements Cancellable {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	private static HandlerList handlers = new HandlerList();
	private boolean cancel;
	
	private InventoryWindow window;
	private Player who;
	private int slot;
	private Item clicked;
	
	public WindowClickEvent(InventoryWindow window, Player who, int slot) {
		this.cancel = false;
		this.window = window;
		this.who = who;
		this.slot = slot;
		this.clicked = window.getItemOf(slot);
	}
	
	public InventoryWindow getWindow() {
		return window;
	}
	
	public Player getPlayer() {
		return who;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public Item getClickedItem() {
		return clicked;
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
