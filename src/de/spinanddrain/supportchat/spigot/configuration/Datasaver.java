package de.spinanddrain.supportchat.spigot.configuration;

import java.io.File;

import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.supportchat.external.sql.overlay.Table;

public class Datasaver {

	/*
	 * Created by SpinAndDrain on 02.04.2020
	 */

	private ConfigurationHandler handler;
	
	public Datasaver() {
		handler = new ConfigurationHandler(new File("plugins/SupportChat/mysql.yml"));
		handler.getBuilder().preBuild("MySQL option to store requests").add("use", false).add("host", "localhost").add("port", 3306).add("database", "supportchat").add("user", "root").add("password", "pw123").build();
		handler.save();
	}
	
	public Table getDatabaseTable() {
		return SupportChat.SQL_TABLE;
	}
	
	public ConfigurationHandler getHandler() {
		return handler;
	}
	
	public boolean use() {
		return handler.configuration.getBoolean("use");
	}
	
	public String getHost() {
		return handler.configuration.getString("host");
	}
	
	public int getPort() {
		return handler.configuration.getInt("port");
	}
	
	public String getDatabase() {
		return handler.configuration.getString("database");
	}
	
	public String getUser() {
		return handler.configuration.getString("user");
	}
	
	public String getPassword() {
		return handler.configuration.getString("password");
	}
	
}
