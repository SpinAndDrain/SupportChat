package de.spinanddrain.supportchat.spigot.configuration;

import java.util.Arrays;
import java.util.List;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;

public enum Reasons {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	MODE(Mode.ENABLED),
	REASONS(new String[]{"Set","Your","Reasons","Here"});

	private final String path;
	private final Object def;
	
	private final ConfigurationHandler handler;
	
	private Reasons(Object def) {
		this.path = this.toString().toLowerCase().replaceAll("_", "-");
		this.def = def;
		this.handler = SpigotPlugin.provide().getReasons();
	}
	
	public String getPath() {
		return path;
	}
	
	public Object solution() {
		return def;
	}
	
	public List<String> stringListSolution() {
		return Arrays.asList((String[]) def);
	}
	
	public Mode asMode() {
		return Mode.valueOf(handler.configuration.getString(path));
	}
	
	public List<String> asList() {
		return handler.configuration.getStringList(path);
	}
	
	public enum Mode {
		ENABLED,
		DISABLED,
		ABSOLUTE_DISABLED;
	}
	
}
