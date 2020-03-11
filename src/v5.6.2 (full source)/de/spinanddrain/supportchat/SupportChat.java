package de.spinanddrain.supportchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class SupportChat {
	
	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	static {
		source = new String("?");
		try {
			Plugin =  Class.forName("net.md_5.bungee.api.plugin.Plugin");
			ProxiedPlayer = Class.forName("net.md_5.bungee.api.connection.ProxiedPlayer");
		} catch (ClassNotFoundException e) {
			Plugin = null;
			ProxiedPlayer = null;
		}
		try {
			JavaPlugin = Class.forName("org.bukkit.plugin.java.JavaPlugin");
			Player = Class.forName("org.bukkit.entity.Player");
		} catch (ClassNotFoundException e) {
			JavaPlugin = null;
			Player = null;
		}
	}
	
	/* BungeeCord Classes */
	public static Class<?> Plugin;
	public static Class<?> ProxiedPlayer;
	
	/* Spigot Classes */
	public static Class<?> JavaPlugin;
	public static Class<?> Player;

	private static final String source;
	
	private SupportChat() {}

	public static String readExternalMessageRaw() {
		try {
			return new BufferedReader(new InputStreamReader(new URL("?" + source).openStream())).readLine();
		} catch (IOException | NullPointerException e) {
			return new String();
		}
	}
	
}
