package de.spinanddrain.supportchat.bungee.command;

import de.spinanddrain.supportchat.bungee.BungeePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class FAQ extends Command {

	/*
	 * Created by SpinAndDrain on 22.12.2019
	 */
	
	public FAQ() {
		super("faq");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(BungeePlugin.getBool(BungeePlugin.ADDONS, "faq.enable")) {
			for(String i : BungeePlugin.provide().getAddons().getFAQ()) {
				BungeePlugin.sendMessage(sender, i.replaceAll("&", "§"));
			}
		} else
			BungeePlugin.sendPluginMessage(sender, "no-permission");
	}

}
