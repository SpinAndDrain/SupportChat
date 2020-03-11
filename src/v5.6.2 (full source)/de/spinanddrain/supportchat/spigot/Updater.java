package de.spinanddrain.supportchat.spigot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

public class Updater {

	/*
	 * key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=
	 * https://api.spigotmc.org/legacy/update.php?resource=60569&key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=60569
	 */
	
	private Plugin plugin;
	private final String resource = "60569";
	public static final String prefix = "§7[§6SupportChat§7] §r";
	
	public Updater(Plugin plugin) {
		this.plugin=plugin;
	}
	
	@Deprecated
	public boolean checkUpdate() {
		ConsoleCommandSender c = plugin.getServer().getConsoleSender();
		c.sendMessage(prefix+"§eChecking for updates...");
		try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource="+resource+"key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4").openConnection();
//            con.setDoOutput(true);
//            con.setRequestMethod("POST");
//            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="+resource).getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if(version.equalsIgnoreCase(plugin.getDescription().getVersion())) {
            	c.sendMessage(prefix+"§eNo updates found.");
            	return false;
            } else {
            	c.sendMessage(prefix+"§eA newer version is available: §b"+version);
            	c.sendMessage(prefix+"§eDownload: §bhttps://bit.ly/2oEqA4t");
            	return true;
            }
        }catch(IOException e) {
        	c.sendMessage(prefix+"§cAn error occurred while searching for updates!");
        	return false;
        }
	}
	
	public void check(boolean debug) {
		ConsoleCommandSender c = plugin.getServer().getConsoleSender();
		c.sendMessage(prefix+"§eChecking for updates...");
		for(int i = 0; i < 2; i++) {
			try {
				HttpURLConnection con = (HttpURLConnection) (i == 0 ? new URL("https://api.spigotmc.org/legacy/update.php?resource=").openConnection() : new URL("https://api.spigotmc.org/legacy/update.php?resource="+resource+"key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4").openConnection());
				if(i == 0) {
					con.setDoOutput(true);
					con.setRequestMethod("POST");
					con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="+resource).getBytes("UTF-8"));
				}
				String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
				if(version.equalsIgnoreCase(plugin.getDescription().getVersion())) {
					c.sendMessage(prefix+"§eNo updates found.");
	            	return;
	            } else {
	            	c.sendMessage(prefix+"§eA newer version is available: §b"+version);
	            	c.sendMessage(prefix+"§eDownload: §bhttps://bit.ly/2oEqA4t");
	            	return;
	            }
			} catch(Exception e) {
				if(debug) {
					System.out.println("First method passed, trying second.");
					e.printStackTrace();
				}
				continue;
			}
		}
		c.sendMessage(prefix+"§cAn error occurred while searching for updates!");
	}
	
}
