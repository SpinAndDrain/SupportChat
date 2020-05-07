package de.spinanddrain.supportchat.spigot.addons;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class AFKHook extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	public static final List<String[]> MEMORY = new ArrayList<String[]>();
	
	private Supporter target;
	private boolean goingAfk;
	
	public AFKHook(Supporter target, boolean isGoingAfk) {
		this.target = target;
		this.goingAfk = isGoingAfk;
	}
	
	public Supporter getTarget() {
		return target;
	}
	
	public boolean isGoingAfk() {
		return goingAfk;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public static String[] compile(StringifiedListPacket packet) {
		return new String[] {
				"target:" + packet.id,
				"hidden:" + packet.hidden,
				"left:" + packet.left
		};
	}
	
	public static StringifiedListPacket parse(String[] arguments) {
		String id = new String();
		boolean hidden = false, left = false;
		for(String arg : arguments) {
			String[] split = arg.split(":");
			switch(split[0]) {
			case "target":
				id = split[1];
				break;
			case "hidden":
				hidden = Boolean.parseBoolean(split[1]);
				break;
			case "left":
				left = Boolean.parseBoolean(split[1]);
				break;
			}
		}
		return new StringifiedListPacket(id, hidden, left);
	}
	
	public static boolean contains(Supporter s) {
		for(StringifiedListPacket i : parseAll()) {
			if(i.getSupporter() == s) {
				return true;
			}
		}
		return false;
	}
	
	public static StringifiedListPacket[] parseAll() {
		StringifiedListPacket[] packets = new StringifiedListPacket[MEMORY.size()];
		for(int i = 0; i < packets.length; i++) {
			packets[i] = parse(MEMORY.get(i));
		}
		return packets;
	}
	
	public static final class RawEvent {
		
		private Class<? extends Event> eventClass;
		private Method afk;
		private String targetMethodPath;
		
		public RawEvent(Class<? extends Event> clazz, Method afk, String targetMethodPath) {
			this.eventClass = clazz;
			this.afk = afk;
			this.targetMethodPath = targetMethodPath;
		}
		
		public Class<? extends Event> getEventClass() {
			return eventClass;
		}
		
		public Method getStateMethod() {
			return afk;
		}
		
		public String getTargetMethodPath() {
			return targetMethodPath;
		}
		
		/* Format: url.path.xxx.clazz@methodname */
		
		public static String compile(RawEvent event) {
			String compiled = event.getEventClass().getSimpleName();
			if(event.getStateMethod() != null) {
				compiled += "@" + event.getStateMethod().getName();
			}
			compiled += "@" + event.getTargetMethodPath();
			return compiled;
		}
		
		@SuppressWarnings("unchecked")
		public static RawEvent parse(String raw) {
			try {
				String[] split = raw.split("@");
				Class<?> clazz = Class.forName(split[0]);
				Method method = null;
				String p = null;
				if(split.length > 2) {
					method = clazz.getMethod(split[1]);
					p = split[2];
				} else
					p = split[1];
				return new RawEvent((Class<? extends Event>) clazz, method, p);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public static final class StringifiedListPacket {
		
		private String id;
		private boolean hidden;
		private boolean left;
		
		public StringifiedListPacket(String id, boolean hidden, boolean left) {
			this.id = id;
			this.hidden = hidden;
			this.left = left;
		}
		
		public Supporter getSupporter() {
			return Supporter.cast(Bukkit.getPlayer(UUID.fromString(id)));
		}
		
		public boolean wasHidden() {
			return hidden;
		}
		
		public boolean isGoingAFK() {
			return left;
		}
		
	}
	
}
