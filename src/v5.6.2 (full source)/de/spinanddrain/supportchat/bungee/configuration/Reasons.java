package de.spinanddrain.supportchat.bungee.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.spinanddrain.supportchat.bungee.ConfigAdapter;
import net.md_5.bungee.config.Configuration;

public class Reasons {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */
	
	public enum Mode {
		ENABLED,
		DISABLED,
		ABSOLUTE_DISABLED;
	}
	
	private ConfigAdapter adapter;
	
	public Reasons() {
		adapter = new ConfigAdapter(new File("plugins/SupportChat/reasons.yml")) {
			@Override
			public void copyDefaults(Configuration c) {
				addDefault("mode", "ENABLED");
				addDefault("reasons", Arrays.asList("Set","Your","Reasons","Here!"));
			}
		};
	}

	public ConfigAdapter getAdapter() {
		return adapter;
	}
	
	public Mode getMode() {
		return Mode.valueOf(adapter.cfg.getString("mode"));
	}
	
	public List<String> getReasons() {
		return adapter.cfg.getStringList("reasons");
	}
	
}
