package de.spinanddrain.supportchat.spigot.addons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;
import de.spinanddrain.supportchat.spigot.configuration.Addons;
import de.spinanddrain.supportchat.spigot.event.SupporterLoginEvent;
import de.spinanddrain.supportchat.spigot.event.SupporterLogoutEvent;
import de.spinanddrain.supportchat.spigot.event.ViewLoglistEvent;

public class ActionBar {

	/*
	 * Created by SpinAndDrain on 14.10.2019
	 */

	private boolean enable;
	private boolean enableFadeout;
	private String message;
	private String empty;
	private long cooldown;
	private Listener callActionbar;
	private ActionBarScheduler scheduler;
	private Plugin base;
	
	public ActionBar(boolean enable, boolean enableFadeout, String message, String empty, long cooldown, Listener callActionbar, ActionBarScheduler scheduler,
			Plugin base) {
		this.enable = enable;
		this.enableFadeout = enableFadeout;
		this.message = message;
		this.empty = empty;
		this.cooldown = cooldown;
		this.callActionbar = callActionbar;
		this.base = base;
		Bukkit.getPluginManager().registerEvents(callActionbar, base);
		this.scheduler = (enableFadeout ? new ActionBarScheduler(1000, base) : scheduler);
		this.scheduler.start(this);
	}
	
	public void sendMessage(Player player) {
		if(enable) {
			if(!enableFadeout) {
				Bukkit.getScheduler().runTaskAsynchronously(base, new Runnable() {
					@Override
					public void run() {
						long state = System.currentTimeMillis();
						do {
							int size = SpigotPlugin.provide().getOnlineLoggedInVisibleSupporters().size();
							sendActionBar((size > 0 ? message.replace("[count]", String.valueOf(size)) : empty.replace("[count]", String.valueOf(size))), player);
							long t = System.currentTimeMillis();
							while(System.currentTimeMillis() <= (t + 1000));
						} while(System.currentTimeMillis() <= (state + cooldown));
					}
				});
			} else {
				int size = SpigotPlugin.provide().getOnlineLoggedInVisibleSupporters().size();
				sendActionBar((size > 0 ? message.replace("[count]", String.valueOf(size)) : empty.replace("[count]", String.valueOf(size))), player);
			}
		}
	}
	
	public void kill() {
		scheduler.stop();
		HandlerList.unregisterAll(callActionbar);
	}
	
	private void sendActionBar(String message, Player player) {
		
		boolean old = false;
		
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);

        if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.startsWith("v1_7_")) {
            old = true;
        }
		
		try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object packet;
            Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> packetClass = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if (old) {
                Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
                Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}"));
                packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> chatComponentTextClass = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                try {
                    Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
                    Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                    Object chatMessageType = null;
                    for (Object obj : chatMessageTypes) {
                        if (obj.toString().equals("GAME_INFO")) {
                            chatMessageType = obj;
                        }
                    }
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                    packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, chatMessageTypeClass}).newInstance(chatCompontentText, chatMessageType);
                } catch (ClassNotFoundException cnfe) {
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                    packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(chatCompontentText, (byte) 2);
                }
            }
            Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
            Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
            Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
            Object playerConnection = playerConnectionField.get(craftPlayerHandle);
            Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static ActionBar createOfConfig() {
		boolean enable = Addons.provide().isActionBarEnabled();
		boolean enableFadeout = Addons.provide().isActionBarFadeoutEnabled();
		String message = Addons.provide().getActionBarMessage().replaceAll("&", "§");
		String empty = Addons.provide().getActionBarEmptyMessage().replaceAll("&", "§");
		long cooldown = Addons.provide().getActionBarFadeoutCooldown();
		Listener callActionbar = new Listener() {
			
			@EventHandler
			public void onJoin(PlayerJoinEvent event) {
				switch(Addons.provide().getOnJoinMode()) {
				case ALL:
					Bukkit.getOnlinePlayers().forEach(player -> SpigotPlugin.provide().getMainBar().sendMessage(player));
					break;
				case ACTOR:
					SpigotPlugin.provide().getMainBar().sendMessage(event.getPlayer());
					break;
				default:
					break;	
				}
			}
			
			@EventHandler
			public void onMove(PlayerMoveEvent event) {
				switch(Addons.provide().getOnMoveMode()) {
				case ALL:
					Bukkit.getOnlinePlayers().forEach(player -> SpigotPlugin.provide().getMainBar().sendMessage(player));
					break;
				case ACTOR:
					SpigotPlugin.provide().getMainBar().sendMessage(event.getPlayer());
					break;
				default:
					break;	
				}
			}
			
			@EventHandler
			public void onLogin(SupporterLoginEvent event) {
				switch(Addons.provide().getOnSupporterLoginMode()) {
				case ALL:
					Bukkit.getOnlinePlayers().forEach(player -> SpigotPlugin.provide().getMainBar().sendMessage(player));
					break;
				case ACTOR:
					SpigotPlugin.provide().getMainBar().sendMessage(event.getSupporter().getSupporter());
					break;
				default:
					break;	
				}
			}
			
			@EventHandler
			public void onLogout(SupporterLogoutEvent event) {
				switch(Addons.provide().getOnSupporterLogoutMode()) {
				case ALL:
					Bukkit.getOnlinePlayers().forEach(player -> SpigotPlugin.provide().getMainBar().sendMessage(player));
					break;
				case ACTOR:
					SpigotPlugin.provide().getMainBar().sendMessage(event.getSupporter().getSupporter());
					break;
				default:
					break;	
				}
			}
			
			@EventHandler
			public void onLogin(ViewLoglistEvent event) {
				switch(Addons.provide().getOnLoglistViewMode()) {
				case ALL:
					Bukkit.getOnlinePlayers().forEach(player -> SpigotPlugin.provide().getMainBar().sendMessage(player));
					break;
				case ACTOR:
					SpigotPlugin.provide().getMainBar().sendMessage(event.getViewer());
					break;
				default:
					break;	
				}
			}
			
		};
		return new ActionBar(enable, enableFadeout, message, empty, cooldown, callActionbar, 
				new ActionBarScheduler(Addons.provide().getScheduledDelay(), SpigotPlugin.provide()), SpigotPlugin.provide());
	}
	
}
