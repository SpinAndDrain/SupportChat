package de.spinanddrain.supportchat.bungee;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;

import com.google.common.io.Files;

import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.updater.requests.provider.ExecutionProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class DownloadSession implements ExecutionProvider {

	private Plugin plugin;
	private File file;
	
	public DownloadSession() {
		plugin = BungeePlugin.provide();
		try {
			file = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String preparation() {
		PluginManager pm = ProxyServer.getInstance().getPluginManager();
		pm.unregisterCommands(plugin);
		pm.unregisterListeners(plugin);
		URLClassLoader loader = (URLClassLoader) plugin.getClass().getClassLoader();
		try {
			loader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File dir = new File("plugins/SupportChat_old/" + plugin.getDescription().getVersion()),
				pluginDir = new File("plugins/SupportChat");
		if(!dir.exists())
			dir.mkdirs();
		try {
			SupportChat.moveDirectory(pluginDir, dir);
			Files.move(file, new File(dir.getPath() + "/" + file.getName()));
		} catch (IOException e) {
			BungeePlugin.sendMessage("§cSomething went wrong at cleaning data. Please remove the old SupportChat Jar file and "
					+ "directory manually. Otherwise errors may occur in later versions.");
			e.printStackTrace();
		}
		return plugin.getDescription().getName();
	}
	
	@Override
	public void postProcessing(File arg0) {
		BungeePlugin.sendMessage("§7[§6SupportChat§7] §eDownload finished. Please restart your proxy to complete the update.");
		ProxyServer.getInstance().stop("please finish SupportChat setup");
	}
	
}
