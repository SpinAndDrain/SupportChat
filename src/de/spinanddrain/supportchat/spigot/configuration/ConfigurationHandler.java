package de.spinanddrain.supportchat.spigot.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationHandler {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	private final File base;
	protected YamlConfiguration configuration;
	
	public ConfigurationHandler(final File base) {
		this.base = base;
		mkdirs();
		reload();
	}

	public ConfigurationHandler(final File base, boolean prebuild) {
		this.base = base;
		if(prebuild) {
			mkdirs();
			reload();
		}
	}
	
	public boolean mkdirs() {
		File parent = base.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		if(!base.exists()) {
			try {
				base.createNewFile();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public YamlConfiguration reload() {
		configuration = YamlConfiguration.loadConfiguration(base);
		return configuration;
	}
	
	public void save() {
		try {
			configuration.save(base);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Defaults getBuilder() {
		return new Defaults(this);
	}
	
}
