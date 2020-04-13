package de.spinanddrain.supportchat.bungee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater {

	private final String resource = "60569";
	public static final String prefix = "§7[§6SupportChat§7] §r";
	
	public Updater() {}
	
	@Deprecated
	public boolean checkUpdate() {
		BungeePlugin.sendMessage(prefix+"§eChecking §efor §eupdates...");
		try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource="+resource+"key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4").openConnection();
//            con.setDoOutput(true);
//            con.setRequestMethod("POST");
//            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="+resource).getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if(version.equalsIgnoreCase(BungeePlugin.provide().getDescription().getVersion())) {
            	BungeePlugin.sendMessage(prefix+"§eNo §eupdates §efound.");
            	return false;
            } else {
            	BungeePlugin.sendMessage(prefix+"§eA §enewer §eversion §eis §eavailable: §b"+version);
            	BungeePlugin.sendMessage(prefix+"§eDownload: §bhttps://bit.ly/2oEqA4t");
            	return true;
            }
        }catch(IOException e) {
        	BungeePlugin.sendMessage(prefix+"§cAn §cerror §coccurred §cwhile §csearching §cfor §cupdates!");
        	return false;
        }
	}
	
	public void check(boolean debug) {
		BungeePlugin.sendMessage(prefix+"§eChecking §efor §eupdates...");
		for(int i = 0; i < 2; i++) {
			try {
				HttpURLConnection con = (HttpURLConnection) (i == 0 ? new URL("https://api.spigotmc.org/legacy/update.php?resource=").openConnection() : new URL("https://api.spigotmc.org/legacy/update.php?resource="+resource+"key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4").openConnection());
				if(i == 0) {
					con.setDoOutput(true);
					con.setRequestMethod("POST");
					con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="+resource).getBytes("UTF-8"));
				}
				String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
				if(version.equalsIgnoreCase(BungeePlugin.provide().getDescription().getVersion())) {
	            	BungeePlugin.sendMessage(prefix+"§eNo §eupdates §efound.");
	            	return;
	            } else {
	            	BungeePlugin.sendMessage(prefix+"§eA §enewer §eversion §eis §eavailable: §b"+version);
	            	BungeePlugin.sendMessage(prefix+"§eDownload: §bhttps://bit.ly/2oEqA4t");
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
		BungeePlugin.sendMessage(prefix+"§cAn §cerror §coccurred §cwhile §csearching §cfor §cupdates!");
	}
	
}
