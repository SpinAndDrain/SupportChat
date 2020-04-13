package de.spinanddrain.supportchat.spigot.configuration;

public class Defaults {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	private ConfigurationHandler handler;
	
	protected Defaults(ConfigurationHandler handler) {
		this.handler = handler;
	}
	
	public Defaults preBuild(String header) {
		handler.configuration.options().header(header);
		handler.configuration.options().copyDefaults(true);
		return this;
	}
	
	public Defaults add(String path, Object value) {
		handler.configuration.addDefault(path, value);
		return this;
	}
	
	public void build() {
		handler.save();
	}
	
}
