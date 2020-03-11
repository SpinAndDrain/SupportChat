package de.spinanddrain.supportchat.spigot.command;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.conversation.Conversation;
import de.spinanddrain.supportchat.spigot.gui.InventoryWindow;
import de.spinanddrain.supportchat.spigot.gui.MoveableItem;
import de.spinanddrain.supportchat.spigot.request.Request;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Requests extends SpigotCommand {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	public Requests() {
		super("requests", Permissions.SUPPORT, PLAYER);
	}

	@Override
	public void runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(args.length == 0) {
			Supporter s = Supporter.cast(player);
			if(s != null && s.isLoggedIn()) {
				InventoryWindow window = new InventoryWindow(6 * 9, Messages.INVENTORY_REQUESTS.getWithoutPrefix());
				window.addConstant(SpigotPlugin.provide().refresh);
				List<Request> req = SpigotPlugin.provide().getVisibleRequests();
				for(Request i : req) {
					if(req.indexOf(i) > 53) {
						break;
					}
					RequestState state = i.getState();
					Material type = (state == RequestState.OPEN ? Material.PAPER : Material.MAP);
					String color = (state == RequestState.OPEN ? "§c§l" : "§e§l");
					Conversation c = SpigotPlugin.provide().getConversationOf(i);
					String firstLore = (state == RequestState.OPEN ? Messages.INVENTORY_REASON.getWithoutPrefixWithPlaceholder(
							Placeholder.create("[reason]", i.getReason())) : Messages.INVENTORY_INFO.getWithoutPrefixWithPlaceholder(
									Placeholder.create("[supporter]", c.getHandler().getSupporter().getName())));
					String secondLore = (state == RequestState.OPEN ? Messages.INVENTORY_CLICK_TO_ACCEPT.getWithoutPrefix() : Messages.INVENTORY_ID
							.getWithoutPrefixWithPlaceholder(Placeholder.create("[id]", String.valueOf(c.getId()))));
					window.addItem(new MoveableItem(type, color + i.getRequestor().getName(), new String(), firstLore, secondLore));
				}
				window.openWindow(player);
			} else
				player.sendMessage(Messages.NOT_LOGGED_IN.getMessage());
		} else
			player.sendMessage(Messages.SYNTAX.getWithPlaceholder(Placeholder.create("[command]", Messages.SYNTAX_REQUESTS.getWithoutPrefix())));
	}
	
}
