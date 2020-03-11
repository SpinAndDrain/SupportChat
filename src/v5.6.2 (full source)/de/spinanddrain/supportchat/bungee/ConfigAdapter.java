package de.spinanddrain.supportchat.bungee;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public abstract class ConfigAdapter {

	/*
	 * Created by SpinAndDrain on 04.08.2019
	 */

	private File file;
	public Configuration cfg;
	
	public ConfigAdapter(File file) {
		this.file = file;
		mkdirs();
		this.cfg = load(file);
		copyDefaults(cfg);
	}
	
	public abstract void copyDefaults(Configuration arg0);
	
	public void mkdirs() {
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addDefault(String path, Object obj, Configuration cfg) {
		if(cfg.get(path) == null) {
			cfg.set(path, obj);
		}
	}
	
	public void addDefault(String path, Object obj) {
		if(cfg.get(path) == null) {
			cfg.set(path, obj);
			save();
		}
	}
	
	public Configuration reload() {
		this.cfg = load(file);
		return this.cfg;
	}
	
	public void save() {
		save(file, this.cfg);
	}
	
	public static Configuration load(File config) {
		try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void save(File config, Configuration cfg) {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
