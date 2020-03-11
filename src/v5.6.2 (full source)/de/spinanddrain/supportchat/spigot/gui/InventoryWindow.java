package de.spinanddrain.supportchat.spigot.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.event.WindowOpenEvent;

public class InventoryWindow {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	private Inventory window;
	private String data;
	private String title;
	
	private List<Item> items;
	private List<WindowItem> constants;
	
	public InventoryWindow(int size, String title) {
		this.window = Bukkit.createInventory(null, size, title);
		this.items = new ArrayList<>();
		this.constants = new ArrayList<>();
		this.title = title;
	}
	
	public InventoryWindow addItem(Item item) {
		window.addItem(item.getRaw());
		items.add(item);
		return this;
	}
	
	public InventoryWindow addConstant(WindowItem item) {
		window.setItem(item.getSlot(), item.getRaw());
		constants.add(item);
		return this;
	}
	
	public Inventory getWindow() {
		return window;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public String getData() {
		return data;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public List<WindowItem> getConstants() {
		return constants;
	}
	
	public Item getItemOf(int slot) {
		ItemStack stack = window.getItem(slot);
		for(Item i : items) {
			if(stack.getItemMeta().equals(i.getRaw().getItemMeta())) {
				return i;
			}
		}
		for(Item i : constants) {
			if(stack.getItemMeta().equals(i.getRaw().getItemMeta())) {
				return i;
			}
		}
		return null;
	}
	
	public void openWindow(Player player) {
		player.openInventory(window);
		Bukkit.getPluginManager().callEvent(new WindowOpenEvent(this, player));
		SpigotPlugin.provide().getWindows().add(this);
	}
	
}
