package de.spinanddrain.supportchat.spigot.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.external.sql.exception.QueryException;
import de.spinanddrain.supportchat.external.sql.exception.WrongDatatypeException;
import de.spinanddrain.supportchat.external.sql.overlay.DataValue;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.addons.AFKHook;
import de.spinanddrain.supportchat.spigot.addons.AFKHook.StringifiedListPacket;
import de.spinanddrain.supportchat.spigot.command.Login;
import de.spinanddrain.supportchat.spigot.configuration.Config;
import de.spinanddrain.supportchat.spigot.configuration.Config.Mode;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.conversation.Conversation;
import de.spinanddrain.supportchat.spigot.gui.InventoryWindow;
import de.spinanddrain.supportchat.spigot.gui.Item;
import de.spinanddrain.supportchat.spigot.request.Request;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Listeners implements Listener {

	/*
	 * Created by SpinAndDrain on 12.10.2019
	 */
	
	public static void register() {
		new Listeners();
	}
	
	private Listeners() {
		Bukkit.getPluginManager().registerEvents(this, SpigotPlugin.provide());
	}
	
	@EventHandler
	public void onToggleAfk(AFKHook event) {
		Player p = event.getTarget().getSupporter();
		if(event.isGoingAfk() && event.getTarget().isLoggedIn()) {
			AFKHook.MEMORY.add(AFKHook.compile(new StringifiedListPacket(p.getUniqueId().toString(), event.getTarget().isHidden(), true)));
			p.performCommand("sclogout");
		} else {
			StringifiedListPacket[] pack = AFKHook.parseAll();
			for(int i = 0; i < pack.length; i++) {
				if(event.getTarget() == pack[i].getSupporter()) {
					p.performCommand("sclogin" + (pack[i].wasHidden() ? " hidden" : ""));
					AFKHook.MEMORY.remove(i);
				}
			}
		}
	}
	
	@EventHandler
	public void onWindowClick(WindowClickEvent event) {
		InventoryWindow window = event.getWindow();
		Player player = event.getPlayer();
		if(window.getTitle().equals(Messages.INVENTORY_REQUESTS.getWithoutPrefix())) {
			if(event.getClickedItem() == SpigotPlugin.provide().refresh) {
				player.closeInventory();
				player.performCommand("requests");
			} else {
				String data = event.getClickedItem().getRaw().getItemMeta().getDisplayName().replaceAll("§c§l", new String()).replaceAll("§e§l", new String());
				player.closeInventory();
				window = new InventoryWindow(9, Messages.MANAGER_REQUEST.getWithoutPrefix());
				window.addConstant(SpigotPlugin.provide().accept);
				window.addConstant(SpigotPlugin.provide().back);
				window.addConstant(SpigotPlugin.provide().listen);
				window.addConstant(SpigotPlugin.provide().deny);
				window.setData(data);
				window.openWindow(player);
			}
		} else if(window.getTitle().equals(Messages.MANAGER_REQUEST.getWithoutPrefix())) {
			Item i = event.getClickedItem();
			Request r = SpigotPlugin.provide().getRequestOf(window.getData());
			player.closeInventory();
			if(r != null) {
				if(i.getRaw().getItemMeta().equals(SpigotPlugin.provide().accept.getRaw().getItemMeta())) {
					if(r.getState() == RequestState.OPEN) {
						Supporter s = Supporter.cast(player);
						if(!s.isTalking() && !s.isListening()) {
							r.setState(RequestState.HANDLE);
							s.setTalking(true);
							Conversation conversation = new Conversation(SpigotPlugin.provide().getConversations().size(), r, s);
							conversation.setRunning(true);
							SpigotPlugin.provide().getConversations().add(conversation);
							player.sendMessage(Messages.YOU_ARE_NOW_IN_A_CONVERSATION.getWithPlaceholder(Placeholder.create("[player]", r.getRequestor().getName())));
							r.getRequestor().sendMessage(Messages.YOU_ARE_NOW_IN_A_CONVERSATION.getWithPlaceholder(Placeholder.create("[player]", player.getName())));
						} else
							player.sendMessage(Messages.ALREADY_IN_CONVERSATION.getMessage());
					} else
						player.sendMessage(Messages.CONVERSATION_ALREADY_RUNNING.getMessage());
				} else if(i.getRaw().getItemMeta().equals(SpigotPlugin.provide().back.getRaw().getItemMeta())) {
					player.performCommand("requests");
				} else if(i.getRaw().getItemMeta().equals(SpigotPlugin.provide().listen.getRaw().getItemMeta())) {
					if(Permissions.LISTEN.hasPermission(player)) {
						Supporter s = Supporter.cast(player);
						if(!s.isTalking() && !s.isListening()) {
							if(r.getState() == RequestState.HANDLE) {
								player.performCommand("listen " + SpigotPlugin.provide().getConversationOf(r).getId());
							} else
								player.sendMessage(Messages.CONVERSATION_NOT_STARTED.getMessage());
						} else
							player.sendMessage(Messages.ALREADY_IN_CONVERSATION.getMessage());
					} else
						player.sendMessage(Messages.NO_PERMISSION.getMessage());
				} else if(i.getRaw().getItemMeta().equals(SpigotPlugin.provide().deny.getRaw().getItemMeta())) {
					if(r.getState() == RequestState.OPEN) {
						r.setState(RequestState.FINISHED);
						r.getRequestor().sendMessage(Messages.YOU_GOT_DENIED.getMessage());
						player.sendMessage(Messages.SUCCESSFULLY_DENIED.getMessage());
					} else
						player.sendMessage(Messages.CONVERSATION_ALREADY_RUNNING.getMessage());
				}
			} else {
				player.sendMessage(Messages.REQUEST_NO_LONGER_AVAILABE.getMessage());
			}
		}
	}
	
	@EventHandler
	public void onWindowClose(WindowCloseEvent event) {
		SpigotPlugin.provide().getWindows().remove(event.getWindow());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		SpigotPlugin.provide().verifyPlayer(player);
		if(Config.JOIN_LOGIN.asMode() != Mode.DISABLED) {
			switch(Config.JOIN_LOGIN.asMode()) {
			case FULL:
				if(Supporter.cast(player) != null) {
					Login.run(player, false);
				}
				break;
			case HIDDEN:
				if(Supporter.cast(player) != null) {
					Login.run(player, true);
				}
				break;
			case PERMISSION_RANGE:
				if(Supporter.cast(player) != null) {
					if(Permissions.LOGIN_HIDDEN.hasPermission(player)) {
						player.performCommand("sclogin hidden");
					} else if(Permissions.LOGIN.hasPermission(player)) {
						player.performCommand("sclogin");
					}
				}
				break;
			default:
				break;
			}
		}
		if(SpigotPlugin.provide().getSaver().use() && SpigotPlugin.provide().getSql().isConnected()) {
			try {
				String reason = SpigotPlugin.provide().getSql().getString(SpigotPlugin.provide().getSaver().getDatabaseTable(), 
						new DataValue("id", player.getUniqueId().toString()), "reason");
				if(reason != null) {
					SpigotPlugin.provide().getRequests().add(new Request(player, reason));
				}
			} catch (WrongDatatypeException | QueryException e) {}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		 Player player = event.getPlayer();
		 if(SpigotPlugin.provide().isInMainConversation(player)) {
			 SpigotPlugin.provide().endConversation(SpigotPlugin.provide().getConversationOf(player));
		 }
		 if(SpigotPlugin.provide().hasRequested(player)) {
			 SpigotPlugin.provide().getRequests().remove(SpigotPlugin.provide().getRequestOf(player.getName()));
		 }
		 Supporter s = Supporter.cast(player);
		 if(s != null) {
			 if(s.isLoggedIn()) {
				 player.performCommand("sclogout");
			 }
			 SpigotPlugin.provide().getOnlineSupporters().remove(s);
		 }
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(SpigotPlugin.provide().isInMainConversation(player)) {
			event.setCancelled(true);
			Conversation conversation = SpigotPlugin.provide().getConversationOf(player);
			if(conversation.getRequest().getRequestor() == player) {
				player.sendMessage(Messages.CHAT_LAYOUT_LOCAL.getWithPlaceholder(Placeholder.create("[message]", event.getMessage())));
				conversation.getHandler().getSupporter().sendMessage(Messages.CHAT_LAYOUT.getWithPlaceholder(Placeholder.create("[player]", player.getName()),
						Placeholder.create("[message]", event.getMessage())));
			} else if(conversation.getHandler().getSupporter() == player) {
				player.sendMessage(Messages.CHAT_LAYOUT_LOCAL.getWithPlaceholder(Placeholder.create("[message]", event.getMessage())));
				conversation.getRequest().getRequestor().sendMessage(Messages.CHAT_LAYOUT.getWithPlaceholder(Placeholder.create("[player]", player.getName()),
						Placeholder.create("[message]", event.getMessage())));
			}
			for(Supporter i : conversation.getListeners()) {
				i.getSupporter().sendMessage(Messages.CHAT_LAYOUT.getWithPlaceholder(Placeholder.create("[player]", player.getName()),
						Placeholder.create("[message]", event.getMessage())));
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		InventoryWindow window = null;
		for(InventoryWindow i : SpigotPlugin.provide().getWindows()) {
			if(event.getInventory() == i.getWindow()) {
				window = i;
			}
		}
		if(window != null && event.getPlayer() instanceof Player) {
			Bukkit.getPluginManager().callEvent(new WindowCloseEvent(window, (Player) event.getPlayer()));
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		InventoryWindow window = null;
		for(InventoryWindow i : SpigotPlugin.provide().getWindows()) {
			if(event.getClickedInventory() != null && event.getClickedInventory().equals(i.getWindow())) {
				window = i;
			}
		}
		if(window != null && event.getWhoClicked() instanceof Player) {
			event.setCancelled(true);
			event.setResult(Result.DENY);
			if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
				Bukkit.getPluginManager().callEvent(new WindowClickEvent(window, (Player) event.getWhoClicked(), event.getSlot()));
			}
		}
	}
	
}
