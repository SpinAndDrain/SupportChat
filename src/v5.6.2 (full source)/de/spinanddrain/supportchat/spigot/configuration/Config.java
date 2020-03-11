package de.spinanddrain.supportchat.spigot.configuration;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;

public enum Config {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	LANGUAGE("DE"),
	JOIN_LOGIN(Mode.DISABLED.toString()),
	CHECK_UPDATE(true);
	
	private final String path;
	private final Object def;
	
	private final ConfigurationHandler handler;
	
	private Config(Object def) {
		this.path = this.toString().toLowerCase().replaceAll("_", "-");
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
