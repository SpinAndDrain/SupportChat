package de.spinanddrain.supportchat.spigot.configuration;

import java.io.File;
import java.util.Arrays;

import org.bukkit.configuration.file.YamlConfiguration;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;

public class RewardsConfiguration {

	/*
	 * Created by SpinAndDrain on 15.10.2019
	 */

	private static RewardsConfiguration instance;
	
	private ConfigurationHandler handler;
	private boolean appear;
	
	private RewardsConfiguration(final String version) {
		instance = this;
		this.handler = new ConfigurationHandler(new File("plugins/SupportChat/addons/rewards.yml"), false);
		this.appear = handler.mkdirs();
		handler.reload();
		this.handler.getBuilder().preBuild("Rewards v" + SpigotPlugin.provide().getPluginVersion() + " - Placeholders: [player], [player-name], [player-id]"
				+ " \\n Examples here: https://github.com/SpinAndDrain/SupportChat/blob/master/examples/rewards.yml").add("enable", true).build();
		setAppears();
	}
	
	public ConfigurationHandler getConfigurationHandler() {
		return handler;
	}
	
	public static void initial() {
		new RewardsConfiguration(SpigotPlugin.provide().getPluginVersion());
	}
	
	public static RewardsConfiguration provide() {
		return instance;
	}
	
	public YamlConfiguration provideNativeConfiguration() {
		return handler.configuration;
	}
	
	public boolean isEnabled() {
		return handler.configuration.getBoolean("enable");
	}
	
	private void setAppears() {
		if(appear) {
			handler.configuration.set("default-reward.target-method", "de.spinanddrain.supportchat.spigot.addons.Rewards#defaultReward");
			handler.configuration.set("default-reward.invoke", Arrays.asList("org.bukkit.entity.Player","java.lang.String"));
			handler.configuration.set("default-reward.params", Arrays.asList("[player]","&cThis is just a test. Add your own rewards!"));
			handler.save();
		}
	}
	
}
