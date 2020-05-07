package de.spinanddrain.supportchat.bungee.configuration;

import java.io.File;

import de.spinanddrain.supportchat.bungee.ConfigAdapter;
import net.md_5.bungee.config.Configuration;

public class Config {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	public enum Mode {
		FULL,
		HIDDEN,
		PERMISSION_RANGE,
		DISABLED;
	}
	
	private ConfigAdapter adapter;
	
	public Config() {
		adapter = new ConfigAdapter(new File("plugins/SupportChat/config.yml")) {
			@Override
			public void copyDefaults(Configuration cfg) {
				addDefault("join-login", "DISABLED");
				addDefault("updater.check-on-startup", true);
				addDefault("updater.auto-download", true);
				addDefault("auto-notification", "2m");
				addDefault("request-delay", "10m");
				addDefault("request-auto-delete-after", "1d");
			}
		};
	}
	
	public ConfigAdapter getAdapter() {
		return adapter;
	}
	
}
