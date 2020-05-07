package de.spinanddrain.supportchat.spigot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

import com.google.common.io.Files;

import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.updater.requests.provider.ExecutionProvider;

public class DownloadSession implements ExecutionProvider {

	private Plugin plugin;
	private String location;

	public DownloadSession(Plugin plugin) throws URISyntaxException {
		this.plugin = plugin;
		location = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	}

	@Override
	public String preparation() {
		Bukkit.getPluginManager().disablePlugin(plugin);
		try {
			TFile.create(".sccd1", location, plugin.getDescription().getVersion());
		} catch (IOException e) {
			throw new RuntimeException("Temp file creation failed");
		}
		try {
			((URLClassLoader) plugin.getClass().getClassLoader()).close();
		} catch (IOException e) {
			throw new RuntimeException("Classloader could not be closed");
		}
		return "SupportChat";
	}

	@Override
	public void postProcessing(File f) {
		try {
			PluginManager pm = Bukkit.getPluginManager();
			pm.enablePlugin(pm.loadPlugin(f));
		} catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final class TFile extends File {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6981901649319950476L;

		private TFile(String path, String softLoc, String v, boolean create) throws IOException {
			this(path);
			if (create) {
				if (!exists())
					createNewFile();
				FileWriter w = new FileWriter(this);
				w.write("spigot@" + v + "@" + softLoc);
				w.close();
			}
		}

		private TFile(String pathname) {
			super(pathname);
		}

		public void performAction() throws IOException {
			BufferedReader r = new BufferedReader(new FileReader(this));
			String c = r.readLine();
			r.close();
			File soft = new File(c.split("@")[2]);
			File dest = new File("plugins/SupportChat_old/" + c.split("@")[0] + "/" + c.split("@")[1] + "/");
			if (!dest.exists())
				dest.mkdirs();
			Files.move(soft, new File(dest.getPath() + "/" + soft.getName()));
			File pluginFolder = new File("plugins/SupportChat");
			if (pluginFolder.exists())
				SupportChat.moveDirectory(pluginFolder, dest);
			delete();
		}

		public static TFile fetch(String path) throws IOException {
			return new TFile(path, null, null, false);
		}

		public static TFile create(String path, String currentSoftLocation, String version) throws IOException {
			return new TFile(path, currentSoftLocation, version, true);
		}

		public static File randomDir(String dir) {
			File cd = null;
			while (cd != null && cd.exists())
				cd = new File(dir + "/" + new Random().nextInt());
			cd.mkdirs();
			return cd;
		}

	}

}
