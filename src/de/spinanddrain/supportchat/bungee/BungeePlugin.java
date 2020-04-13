package de.spinanddrain.supportchat.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.Server;
import de.spinanddrain.supportchat.ServerVersion;
import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.supportchat.bungee.addons.ActionBar;
import de.spinanddrain.supportchat.bungee.command.End;
import de.spinanddrain.supportchat.bungee.command.FAQ;
import de.spinanddrain.supportchat.bungee.command.Listen;
import de.spinanddrain.supportchat.bungee.command.Login;
import de.spinanddrain.supportchat.bungee.command.Loglist;
import de.spinanddrain.supportchat.bungee.command.Logout;
import de.spinanddrain.supportchat.bungee.command.Reload;
import de.spinanddrain.supportchat.bungee.command.Req;
import de.spinanddrain.supportchat.bungee.command.Requests;
import de.spinanddrain.supportchat.bungee.command.SCB;
import de.spinanddrain.supportchat.bungee.command.Support;
import de.spinanddrain.supportchat.bungee.configuration.Addons;
import de.spinanddrain.supportchat.bungee.configuration.Config;
import de.spinanddrain.supportchat.bungee.configuration.Datasaver;
import de.spinanddrain.supportchat.bungee.configuration.Messages;
import de.spinanddrain.supportchat.bungee.configuration.Reasons;
import de.spinanddrain.supportchat.bungee.conversation.Conversation;
import de.spinanddrain.supportchat.bungee.events.Listeners;
import de.spinanddrain.supportchat.bungee.request.Request;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import de.spinanddrain.supportchat.external.sql.MySQL;
import de.spinanddrain.supportchat.external.sql.exception.ConnectionFailedException;
import de.spinanddrain.supportchat.external.sql.exception.QueryException;
import de.spinanddrain.supportchat.external.sql.exception.WrongDatatypeException;
import de.spinanddrain.supportchat.external.sql.overlay.DataValue;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeePlugin extends Plugin implements Server {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */
	
	private static BungeePlugin provider;
	public static final int CONFIG = 0, MESSAGES = 1, REASONS = 2, ADDONS = 3;
	
	private Updater u;
	private ActionBar mb;
	
	private Config config;
	private Messages messages;
	private Reasons reasons;
	private Addons addons;
	private Datasaver saver;
	
	private List<Supporter> supporters;
	private List<Request> requests;
	private List<Conversation> conversations;
	
	private MySQL sql;
	
	@Override
	public void onEnable() {
		provider = this;
		
		supporters = new ArrayList<>();
		requests = new ArrayList<>();
		conversations = new ArrayList<>();
		
		if(getServerVersion() == ServerVersion.UNSUPPORTED_TERMINAL) {
			sendMessage(Updater.prefix + "§c> §cThe plugin does not support your server version!");
			sendMessage(Updater.prefix + "§eStopping...");
			BungeeCord.getInstance().stop("SupportChat: Unsupported Terminal");
			return;
		}
		
		prepareConfigurations();
		
		sendMessage("§7__________[§9SupportChat §52§7]_________");
		sendMessage(" ");
		sendMessage("§7   Current Version: §b"+provider.getDescription().getVersion());
		sendMessage("§7   Plugin by §cSpinAndDrain");
		sendMessage("§7   Your Serverversion: §b(BungeeCord) "+getServerVersion().convertFormat());
		String extm = SupportChat.readExternalMessageRaw();
		if(extm != null && !extm.equals(new String())) {
			sendMessage("   "+extm.replace("&", "§"));
		}
		sendMessage("§7__________________________________");
		
		u = new Updater();
		
		if(getBool(CONFIG, "check-update")) {
			u.check(false);
		}
		
		getProxy().getPlayers().forEach(player -> verifyPlayer(player));
		
		if(saver.use()) {
			this.sql = new MySQL(saver.getHost(), String.valueOf(saver.getPort()), saver.getDatabase(), saver.getUser(), saver.getPassword());
			getLogger().log(Level.WARNING, "The MySQL connection is established synchronously. It could take a while until your server is ready.");
			try {
				this.sql.connect();
				this.sql.createTable(saver.getDatabaseTable());
				for(String key : this.sql.getStringifiedKeys(saver.getDatabaseTable(), "id")) {
					ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(UUID.fromString(key));
					if(pp != null) {
						requests.add(new Request(pp, this.sql.getString(saver.getDatabaseTable(), new DataValue("id", key), "reason")));
					}
				}
			} catch (QueryException | WrongDatatypeException | ConnectionFailedException e) {
				e.printStackTrace();
			}
		}
		
		mb = ActionBar.createOfConfig();
		
		PluginManager pm = getProxy().getPluginManager();
		pm.registerCommand(this, new Support());
		pm.registerCommand(this, new End());
		pm.registerCommand(this, new Login());
		pm.registerCommand(this, new Logout());
		pm.registerCommand(this, new Listen());
		pm.registerCommand(this, new Loglist());
		pm.registerCommand(this, new Reload());
		pm.registerCommand(this, new Requests("requests"));
		pm.registerCommand(this, new Req());
		pm.registerCommand(this, new SCB());
		pm.registerCommand(this, new FAQ());
		pm.registerListener(this, new Listeners());
	}
	
	@Override
	public void onDisable() {
		try {
			mb.kill();
		} catch(Exception e) {}
		if(this.sql != null && this.sql.isConnected()) {
			try {
				this.sql.disconnect();
			} catch (ConnectionFailedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static BungeePlugin provide() {
		return provider;
	}
	
	public MySQL getSql() {
		return sql;
	}
	
	public Datasaver getSaver() {
		return saver;
	}
	
	public Messages getMessages() {
		return messages;
	}
	
	public Config getConfig() {
		return config;
	}
	
	public Reasons getReasons() {
		return reasons;
	}
	
	public Updater getUpdater() {
		return u;
	}
	
	public ActionBar getActionBar() {
		return mb;
	}
	
	public Addons getAddons() {
		return addons;
	}
	
	public void reload() {
		config.getAdapter().reload();
		messages.getAdapter().reload();
		messages.reInitParser();
		reasons.getAdapter().reload();
		addons.getAdapter().reload();
		mb.kill();
		mb = ActionBar.createOfConfig();
	}
	
	public static void sendMessage(String message) {
		ProxyServer.getInstance().getConsole().sendMessage(toColoredText(message));
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(toColoredText(message));
	}
	
	public static void sendPluginMessage(CommandSender sender, String path, Placeholder... ph) {
		String message = getMessage(path, true);
		for(Placeholder phx : ph) {
			message = message.replace(phx.holder, phx.placer);
		}
		sendMessage(sender, message);
	}
	
	public static void sendMessage(ProxiedPlayer pp, String message) {
		pp.sendMessage(toColoredText(message));
	}
	
	public static void sendPluginMessage(ProxiedPlayer p, String path, Placeholder... ph) {
		String message = getMessage(path, true);
		for(Placeholder phx : ph) {
			message = message.replace(phx.holder, phx.placer);
		}
		sendMessage(p, message);
	}
	
	public void verifyPlayer(ProxiedPlayer player) {
		if(Permissions.SUPPORT.hasPermission(player) || Permissions.LOGIN.hasPermission(player)) {
			supporters.add(new Supporter(player));
		}
	}
	
	public void endConversation(Conversation c) {
		c.setRunning(false);
		c.getRequest().setState(RequestState.FINISHED);
		c.getHandler().setTalking(false);
		c.getListeners().forEach(listener -> listener.setListening(false));
		c.sendAll(getMessage("conversation-ended", true));
		conversations.remove(c);
		if(saver.use() && sql.isConnected()) {
			try {
				sql.deleteAll(saver.getDatabaseTable(), new DataValue("id", c.getRequest().getRequestor().getUniqueId().toString()));
			} catch (WrongDatatypeException | QueryException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isInConversation(ProxiedPlayer player) {
		return (getConversationOf(player) != null);
	}
	
	public static Object get(int i, String path) {
		switch (i) {
		case 0:
			return BungeePlugin.provider.config.getAdapter().cfg.get(path);
		case 1:
			return BungeePlugin.provider.messages.getParser().getByKey(path);
		case 2:
			return BungeePlugin.provider.reasons.getAdapter().cfg.get(path);
		case 3:
			return BungeePlugin.provider.addons.getAdapter().cfg.get(path);
		default:
			return null;
		}
	}
	
	public static String getString(int i, String path) {
		return ((String) get(i, path)).replaceAll("&", "§");
	}
	
	public static String getMessage(String path, boolean prefix) {
		return (prefix ? getString(1, "prefix") + " " : new String()) + getString(1, path);
	}
	
	public static boolean getBool(int i, String path) {
		return (boolean) get(i, path);
	}
	
	public static ServerVersion getServerVersion() {
		String v = provider.getProxy().getVersion().split(":")[2];
		if (v.startsWith("1.8")) {
			return ServerVersion.v1_8;
		} else if (v.startsWith("1.9")) {
			return ServerVersion.v1_9;
		} else if (v.startsWith("1.10")) {
			return ServerVersion.v1_10;
		} else if (v.startsWith("1.11")) {
			return ServerVersion.v1_11;
		} else if (v.startsWith("1.12")) {
			return ServerVersion.v1_12;
		} else if (v.startsWith("1.13")) {
			return ServerVersion.v1_13;
		} else if (v.startsWith("1.14")) {
			return ServerVersion.v1_14;
		} else if (v.startsWith("1.15")) { 
			return ServerVersion.v1_15;
		} else {
			return ServerVersion.UNSUPPORTED_TERMINAL;
		}
	}
	
	private void prepareConfigurations() {
		File parent = new File("plugins/SupportChat");
		if(!parent.exists()) {
			parent.mkdirs();
		}
		config = new Config();
		messages = new Messages();
		reasons = new Reasons();
		addons = new Addons();
		saver = new Datasaver();
	}
	
	@Override
	public List<Request> getRequests() {
		return requests;
	}

	public Request getRequestOf(String name) {
		for(Request i : requests) {
			if(i.getRequestor().getName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	public boolean hasRequested(ProxiedPlayer player) {
		for(Request i : requests) {
			if(i.getRequestor() == player && i.getState() != RequestState.FINISHED) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<Request> getVisibleRequests() {
		List<Request> open = new ArrayList<>();
		for(Request i : requests) {
			if(i.getState() != RequestState.FINISHED) {
				open.add(i);
			}
		}
		return open;
	}

	@Override
	public List<Supporter> getOnlineSupporters() {
		return supporters;
	}

	@Override
	public List<Supporter> getOnlineVisibleSupporters() {
		List<Supporter> online = new ArrayList<>();
		for(Supporter s : supporters) {
			if(!s.isHidden()) {
				online.add(s);
			}
		}
		return online;
	}

	public List<Supporter> getOnlineLoggedInVisibleSupporters() {
		List<Supporter> logged = new ArrayList<>();
		for(Supporter i : getOnlineVisibleSupporters()) {
			if(i.isLoggedIn()) {
				logged.add(i);
			}
		}
		return logged;
	}
	
	public Conversation getConversationOf(Request base) {
		for(Conversation i : conversations) {
			if(i.getRequest() == base) {
				return i;
			}
		}
		return null;
	}
	
	public Conversation getConversationOf(ProxiedPlayer player) {
		for(Conversation i : conversations) {
			if(i.getRequest().getRequestor() == player || i.getHandler().getSupporter() == player || i.getListeners().contains(Supporter.cast(player))) {
				return i;
			}
		}
		return null;
	}
	
	public Conversation getConversationOf(int id) {
		for(Conversation i : conversations) {
			if(i.getId() == id) {
				return i;
			}
		}
		return null;
	}
	
	public boolean isInMainConversation(ProxiedPlayer player) {
		Conversation conversation = getConversationOf(player);
		return (conversation != null && (conversation.getRequest().getRequestor() == player || conversation.getHandler().getSupporter() == player));
	}
	
	@Override
	public List<Conversation> getConversations() {
		return conversations;
	}

	@Override
	public List<Conversation> getRunningConversations() {
		List<Conversation> running = new ArrayList<>();
		for(Conversation i : conversations) {
			if(i.isRunning()) {
				running.add(i);
			}
		}
		return running;
	}

	@Override
	public void registerCommand(Object command) {}

	@Override
	public String getLanguage() {
		return "EN";
	}

	@Override
	public String getPluginVersion() {
		return provider.getDescription().getVersion();
	}

	public static TextComponent toColoredText(String text) {
		String[] a = text.split(" ");
		String build = "";
		String color = "";
		for(int i = 0; i < a.length; i++) {
			String cc;
			if(a[i].toCharArray().length > 1) {
				cc = a[i].toCharArray()[0] + "" + a[i].toCharArray()[1];
			} else {
				cc = "";
			}
			if(a[i].startsWith("§") && !cc.equals(color)) {
				color = cc;
				String tmp = a[i];
				if((i + 1) == a.length) {
					build += tmp;
				} else {
					build += tmp + " ";
				}
			} else {
				String tmp = a[i];
				if((i + 1) == a.length) {
					build += color + tmp;
				} else {
					build += color + tmp + " ";
				}
			}
		}
		return new TextComponent(build);
	}
	
}
