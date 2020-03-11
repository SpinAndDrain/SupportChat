package de.spinanddrain.supportchat.spigot.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WindowItem implements Item {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	private ItemStack raw;
	private int slot;
	
	public WindowItem(Material type, int slot, String display) {
		this.slot = slot;
		raw = new ItemStack(type);
		ItemMeta meta = raw.getItemMeta();
		meta.setDisplayName(display);
		raw.setItemMeta(meta);
	}
	
	public int getSlot() {
		return slot;
	}
	
	@Override
	public ItemStack getRaw() {
		return raw;
	}
	
}
