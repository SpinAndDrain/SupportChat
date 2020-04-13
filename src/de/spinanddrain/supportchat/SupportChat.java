package de.spinanddrain.supportchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import de.spinanddrain.supportchat.external.sql.overlay.Datatype;
import de.spinanddrain.supportchat.external.sql.overlay.Table;
import de.spinanddrain.supportchat.external.sql.overlay.Datatype.Type;

public final class SupportChat {

	 /*
	  *  CHANGES: FALSE
	  */
	
	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	static {
		/*
		 * Old: ext.mess.201912192015.state
		 * New: ext.mess.202001162102.state
		 */
		source = new String("ext.mess.202001162102.state");
		SQL_TABLE = new Table("supportchat", new Datatype[]{new Datatype(Type.VARCHAR, "id", 100), new Datatype(Type.VARCHAR, "reason", 100)});
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
	
	public static final Table SQL_TABLE;
	
	private SupportChat() {}

	public static String readExternalMessageRaw() {
		try {
			return new BufferedReader(new InputStreamReader(new URL("http://spinanddrain.bplaced.net/sessions/supportchat/" + source).openStream())).readLine();
		} catch (IOException | NullPointerException e) {
			return new String();
		}
	}
	
}
