package de.spinanddrain.supportchat.spigot.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MoveableItem implements Item {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */

	private ItemStack raw;
	
	public MoveableItem(Material type, String display, String... lore) {
		raw = new ItemStack(type);
		ItemMeta meta = raw.getItemMeta();
		meta.setDisplayName(display);
		meta.setLore(Arrays.asList(lore));
		raw.setItemMeta(meta);
	}
	
	@Override
	public ItemStack getRaw() {
		return raw;
	}
	
}
