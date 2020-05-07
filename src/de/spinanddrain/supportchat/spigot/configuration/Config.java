package de.spinanddrain.supportchat.spigot.configuration;

import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;

public enum Config {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	JOIN_LOGIN(Mode.DISABLED.toString()),
	UPDATER$CHECK_ON_STARTUP(true),
	UPDATER$AUTO_DOWNLOAD(true),
	AUTO_NOTIFICATION("2m"),
	REQUEST_DELAY("10m"),
	REQUEST_AUTO_DELETE_AFTER("1d");
	
	private final String path;
	private final Object def;
	
	private final ConfigurationHandler handler;
	
	private Config(Object def) {
		this.path = this.toString().toLowerCase().replace("_", "-").replace("$", ".");
		this.def = def;
		this.handler = SpigotPlugin.provide().getNativeConfig();
	}
	
	public String getPath() {
		return path;
	}
	
	public Object solution() {
		return def;
	}
	
	public String asString() {
		return handler.configuration.getString(path);
	}
	
	public boolean asBoolean() {
		return handler.configuration.getBoolean(path);
	}

	public long asLong() {
		return handler.configuration.getLong(path);
	}
	
	public long asTime() {
		return SupportChat.getTime(handler.configuration.getString(path));
	}
	
	public Mode asMode() {
		return Mode.valueOf(asString().toUpperCase());
	}
	
	public enum Mode {
		FULL,
		HIDDEN,
		PERMISSION_RANGE,
		DISABLED;
	}
	
}
