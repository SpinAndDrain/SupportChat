package de.spinanddrain.supportchat.spigot.addons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.spigot.configuration.RewardsConfiguration;

public class Rewards {

	/*
	 * Created by SpinAndDrain on 02.09.2019
	 */

	/*
	 * Placeholders: [player], [player-id], [player-name]
	 */
	
	private Player player;
	
	public Rewards(Player player) {
		this.player = player;
	}
	
	public static void defaultReward(Player player, String message) {
		player.sendMessage(message.replaceAll("&", "§"));
	}
	
	public void collect() throws RewardNotAvailableException {
		YamlConfiguration cfg = RewardsConfiguration.provide().provideNativeConfiguration();
		for(String keys : cfg.getKeys(false)) {
			if(keys.equals("enable")) continue;
			try {
				collectFrom(cfg.getString(keys + ".target-method"), cfg.getStringList(keys + ".invoke"), cfg.getStringList(keys + ".params"));
			} catch (Exception e) {
				throw new RewardNotAvailableException("The reward " + keys + " is not available!");
			}
		}
	}
	
	public void collectFrom(String target, List<String> invo, List<String> para) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException  {
		String[] um = target.split("#");
		Class<?> clazz = Class.forName(um[0]);
		String[] fum = new String[um.length - 1];
		for(int i = 0; i < fum.length; i++) {
			fum[i] = um[i + 1];
		}
		
		Invokation[] invokations = new Invokation[invo.size()];
		Class<?>[] invotypes = new Class<?>[invokations.length];
		for(int i = 0; i < invokations.length; i++) {
			String current = invo.get(i);
			if(current.contains("#")) {
				String[] args = current.split("#");
				Class<?> c = Class.forName(args[0]);
				if(args[0].contains("java.lang.Byte")) {
					c = Byte.TYPE;
				} else if(args[0].contains("java.lang.Short")) {
					c = Short.TYPE;
				} else if(args[0].contains("java.lang.Integer")) {
					c = Integer.TYPE;
				} else if(args[0].contains("java.lang.Long")) {
					c = Long.TYPE;
				} else if(args[0].contains("java.lang.Boolean")) {
					c = Boolean.TYPE;
				} else if(args[0].contains("java.lang.Character")) {
					c = Character.TYPE;
				} else if(args[0].contains("java.lang.Double")) {
					c = Double.TYPE;
				} else if(args[0].contains("java.lang.Float")) {
					c = Float.TYPE;
				}
				List<Class<?>> inserts = new ArrayList<Class<?>>();
				for(String all : args[1].split(",")) {
					if(all.contains("java.lang.Byte")) {
						inserts.add(Byte.TYPE);
					} else if(all.contains("java.lang.Short")) {
						inserts.add(Short.TYPE);
					} else if(all.contains("java.lang.Integer")) {
						inserts.add(Integer.TYPE);
					} else if(all.contains("java.lang.Long")) {
						inserts.add(Long.TYPE);
					} else if(all.contains("java.lang.Boolean")) {
						inserts.add(Boolean.TYPE);
					} else if(all.contains("java.lang.Character")) {
						inserts.add(Character.TYPE);
					} else if(all.contains("java.lang.Double")) {
						inserts.add(Double.TYPE);
					} else if(all.contains("java.lang.Float")) {
						inserts.add(Float.TYPE);
					} else {
						inserts.add(Class.forName(all));
					}
				}
				invokations[i] = new Invokation(c, c.getConstructor(inserts.toArray(new Class<?>[inserts.size()])));
				invotypes[i] = c;
			} else {
				Class<?> c = Class.forName(current);
				if(current.contains("java.lang.Byte")) {
					c = Byte.TYPE;
				} else if(current.contains("java.lang.Short")) {
					c = Short.TYPE;
				} else if(current.contains("java.lang.Integer")) {
					c = Integer.TYPE;
				} else if(current.contains("java.lang.Long")) {
					c = Long.TYPE;
				} else if(current.contains("java.lang.Boolean")) {
					c = Boolean.TYPE;
				} else if(current.contains("java.lang.Character")) {
					c = Character.TYPE;
				} else if(current.contains("java.lang.Double")) {
					c = Double.TYPE;
				} else if(current.contains("java.lang.Float")) {
					c = Float.TYPE;
				}
				invokations[i] = new Invokation(c);
				invotypes[i] = c;
			}
		}
		
		
		Object[] values = new Object[para.size()];
		for(int i = 0; i < invotypes.length; i++) {
			String value = para.get(i);
			String[] args = value.split(",");
			if(args.length == 1) {
				if(value.equals("[player]")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(player);
					} else {
						values[i] = player;
					}
					continue;
				} else if(value.equals("[player-id]")) {
					value = this.player.getUniqueId().toString();
				} else if(value.equals("[player-name]")) {
					value = this.player.getName();
				}
				String val = para.get(i);
				String subclass = "";
				if(invokations[i].hasNonDefaultConstructor()) {
					subclass = invo.get(i).split("#")[1];
				} else {
					subclass = invo.get(i);
				}
				if(subclass.contains("java.lang.Byte")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(Byte.parseByte(val));
					} else {
						values[i] = Byte.parseByte(val);
					}
				} else if(subclass.contains("java.lang.Short")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(Short.parseShort(val));
					} else {
						values[i] = Short.parseShort(val);
					}
				} else if(subclass.contains("java.lang.Integer")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(Integer.parseInt(val));
					} else {
						values[i] = Integer.parseInt(val);
					}
				} else if(subclass.contains("java.lang.Long")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(Long.parseLong(val));
					} else {
						values[i] = Long.parseLong(val);
					}
				} else if(subclass.contains("java.lang.Boolean")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(Boolean.parseBoolean(val));
					} else {
						values[i] = Boolean.parseBoolean(val);
					}
				} else if(subclass.contains("java.lang.Character")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(val.toCharArray()[0]);
					} else {
						values[i] = val.toCharArray()[0];
					}
				} else if(subclass.contains("java.lang.Double")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(Double.parseDouble(val));
					} else {
						values[i] = Double.parseDouble(val);
					}
				} else if(subclass.contains("java.lang.Float")) {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(Float.parseFloat(val));
					} else {
						values[i] = Float.parseFloat(val);
					}
				} else {
					if(invokations[i].hasNonDefaultConstructor()) {
						values[i] = invokations[i].getConstructor().newInstance(val);
					} else {
						values[i] = val;
					}
				}
			} else {
				List<Object> objs = new ArrayList<>();
				
				String subclass = "";
				if(invokations[i].hasNonDefaultConstructor()) {
					subclass = invo.get(i).split("#")[1];
				} else {
					subclass = invo.get(i);
				}
				
				String[] subarr = subclass.split(",");
				
				for(int z = 0; z < args.length; z++) {
					if(args[z].equals("[player]")) {
						objs.add(this.player);
					} else if(args[z].equals("[player-id]")) {
						objs.add(this.player.getUniqueId().toString());
					} else if(args[z].equals("[player-name]")) {
						objs.add(this.player.getName());
					} else if(subarr[z].contains("java.lang.Byte")) {
						objs.add(Byte.parseByte(args[z]));
					} else if(subarr[z].contains("java.lang.Short")) {
						objs.add(Short.parseShort(args[z]));
					} else if(subarr[z].contains("java.lang.Integer")) {
						objs.add(Integer.parseInt(args[z]));
					} else if(subarr[z].contains("java.lang.Long")) {
						objs.add(Long.parseLong(args[z]));
					} else if(subarr[z].contains("java.lang.Boolean")) {
						objs.add(Boolean.parseBoolean(args[z]));
					} else if(subarr[z].contains("java.lang.Character")) {
						objs.add(args[z].toCharArray()[0]);
					} else if(subarr[z].contains("java.lang.Double")) {
						objs.add(Double.parseDouble(args[z]));
					} else if(subarr[z].contains("java.lang.Float")) {
						objs.add(Float.parseFloat(args[z]));
					} else {
						objs.add(args[z]);
					}
				}
				
				values[i] = invokations[i].getConstructor().newInstance(objs.toArray(new Object[objs.size()]));
				
			}
		}
		
		Method finalmethod = null;
		Object toinvoke = null;
		if(fum.length == 0) {
			clazz.getConstructor(invotypes).newInstance(values);
			return;
		} else if(fum.length == 1) {
			finalmethod = clazz.getMethod(fum[0], invotypes);
		} else {
			for(int i = 0; i < fum.length; i++) {
				if(finalmethod == null) {
					finalmethod = clazz.getMethod(fum[i]);
				} else if((i + 1) == fum.length) {
					toinvoke = finalmethod.invoke(finalmethod.getClass());
					finalmethod = toinvoke.getClass().getMethod(fum[i], invotypes);
				} else {
					toinvoke = finalmethod.invoke(finalmethod.getClass());
					finalmethod = toinvoke.getClass().getMethod(fum[i]);
				}
			}
		}
		
		finalmethod.invoke(toinvoke, values);
	}
	
	public class RewardNotAvailableException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RewardNotAvailableException(String message) {
			super(message);
		}
		
	}
	
}
