package de.spinanddrain.supportchat.bungee;

import java.io.File;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import de.spinanddrain.prid.ResourceIdParser;
import de.spinanddrain.sql.Connection;
import de.spinanddrain.sql.DataType;
import de.spinanddrain.sql.Parameter;
import de.spinanddrain.sql.Table;
import de.spinanddrain.sql.Value;
import de.spinanddrain.sql.exception.ConnectionException;
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
import de.spinanddrain.supportchat.bungee.command.Scl;
import de.spinanddrain.supportchat.bungee.command.Support;
import de.spinanddrain.supportchat.bungee.command.Supportleave;
import de.spinanddrain.supportchat.bungee.configuration.Addons;
import de.spinanddrain.supportchat.bungee.configuration.Config;
import de.spinanddrain.supportchat.bungee.configuration.Datasaver;
import de.spinanddrain.supportchat.bungee.configuration.Messages;
import de.spinanddrain.supportchat.bungee.configuration.Reasons;
import de.spinanddrain.supportchat.bungee.conversation.Conversation;
import de.spinanddrain.supportchat.bungee.events.Listeners;
import de.spinanddrain.supportchat.bungee.request.Request;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import de.spinanddrain.updater.Updater;
import de.spinanddrain.updater.VersionPattern;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeePlugin extends Plugin implements Server {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */
	
	private static BungeePlugin provider;
	public static final int CONFIG = 0, MESSAGES = 1, REASONS = 2, ADDONS = 3;
	
	private Updater u;
	private final String consolePrefix = "§7[§6SupportChat§7] §r";
	private ActionBar mb;
	
	private Config config;
	private Messages messages;
	private Reasons reasons;
	private Addons addons;
	private Datasaver saver;
	
	private List<Supporter> supporters;
	private List<Request> requests;
	private List<Conversation> conversations;
	
	private Map<UUID, Long> lastRequest;
	
	private Connection sql;
	private Table table;
	private ScheduledTask notificator, expire;
	
	@Override
	public void onEnable() {
		provider = this;
		
		VersionPattern pattern = new VersionPattern(SupportChat.DEPENDENCY_VERSION);
		String dependencyVersion = getProxy().getPluginManager().getPlugin("LibsCollection").getDescription().getVersion();
		if(!pattern.isEqual(dependencyVersion)) {
			if(!(SupportChat.HIGHER && pattern.isOlderThan(dependencyVersion))) {
				sendMessage(consolePrefix+"§cThe dependency §eLibsCollection §chas an invalid version (§e" + dependencyVersion + "§c)."
						+ " Version §e" + SupportChat.DEPENDENCY_VERSION + "§c " + (SupportChat.HIGHER ? "(or higher) " : "") 
						+ "is required!");
				PluginManager pm = getProxy().getPluginManager();
				pm.unregisterCommands(this);
				pm.unregisterListeners(this);
				try { ((URLClassLoader) getClass().getClassLoader()).close(); } catch(Exception e) {};
				return;
			}
		}
		
		supporters = new ArrayList<>();
		requests = new ArrayList<>();
		conversations = new ArrayList<>();
		
		lastRequest = new HashMap<UUID, Long>();
		
		if(getServerVersion() == ServerVersion.UNSUPPORTED_TERMINAL) {
			sendMessage(consolePrefix + "§c> §cThe plugin does not support your server version!");
			sendMessage(consolePrefix + "§eStopping...");
			getProxy().stop("SupportChat: Unsupported Terminal");
			return;
		}
		
		prepareConfigurations();
		
		for(String m : SupportChat.getTextField("[§9SupportChat §43§7]", "§7Current Version: §b"
				+getDescription().getVersion()+"§r", "§7Plugin by §cSpinAndDrain§r",
				"§7Your Serverversion: §b(BungeeCord) "+getServerVersion().convertFormat()+"§r")) {
			sendMessage(m);
		}
		
		String extm = SupportChat.readExternalMessageRaw();
		if(extm != null && !extm.equals(new String())) {
			sendMessage("§7[§cSupportChat: INFO§7] §r" + extm.replace("&", "§"));
		}
		
		u = new Updater(ResourceIdParser.defaultPrid().getResourceIdByKey("de.spinanddrain.supportchat"),
				getDescription().getVersion());
		
		if(getBool(CONFIG, "updater.check-on-startup")) {
			sendMessage(consolePrefix + "§eChecking for updates...");
			try {
				if(u.isAvailable()) {
					sendMessage(consolePrefix + "§eA newer version is available: §b" + u.getLatestVersion());
					if(getBool(CONFIG, "updater.auto-download")) {
						sendMessage(consolePrefix + "§eDownloading...");
						u.installLatestVersion(new DownloadSession());
						return;
					}
				} else
					sendMessage(consolePrefix + "§eNo updates found. You are running the latest version of SupportChat.");
			} catch(Exception e) {
				sendMessage(consolePrefix + "§cAn error occurred while searching for updates.");
			}
		}
		
		getProxy().getPlayers().forEach(player -> verifyPlayer(player));
		
		if(saver.use()) {
			HashMap<String, Object> p = new HashMap<>();
			p.put("autoReconnect", true);
			p.put("useSSL", saver.useSSL());
			sql = new Connection(saver.getHost(), saver.getPort(), saver.getUser(), saver.getPassword(), saver.getDatabase(), p);
			getLogger().log(Level.WARNING, "The MySQL connection is established synchronously. It could take a while until your server is ready.");
			try {
				sql.connect();
				table = sql.createTable("supportchat", new Parameter("id", DataType.VARCHAR, 100, true, true, false, null),
						new Parameter("reason", DataType.VARCHAR, 100, false, false, false, null),
						new Parameter("requesttime", DataType.BIGINT, -1, false, true, false, 0L));
				for(String key : sql.getKeys("id", table)) {
					ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(UUID.fromString(key));
					if(pp != null) {
						requests.add(new Request(pp, (String) sql.get(new Value("id", key), "reason", table)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		verifyNotificator();
		verifyExpireTask();
		
		mb = ActionBar.createOfConfig();
		
		PluginManager pm = getProxy().getPluginManager();
		pm.registerCommand(this, new Support());
		pm.registerCommand(this, new Supportleave("supportleave"));
		pm.registerCommand(this, new Scl());
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
			if(mb != null)
				mb.kill();
		} catch(Exception e) {}
		if(this.sql != null && this.sql.isConnected()) {
			try {
				sql.disconnect();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
		closeTasks();
	}
	
	public static BungeePlugin provide() {
		return provider;
	}
	
	public Connection getSql() {
		return sql;
	}
	
	public Table getTable() {
		return table;
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
	
	public Map<UUID, Long> getLastRequest() {
		return lastRequest;
	}
	
	public ActionBar getActionBar() {
		return mb;
	}
	
	public Addons getAddons() {
		return addons;
	}
	
	private void verifyNotificator() {
		long time = SupportChat.getTime(getString(CONFIG, "auto-notification"));
		if(time > 0) {
			notificator = getProxy().getScheduler().schedule(this, () -> {
				for(Supporter all : supporters) {
					if(this.anyRequestOpen() && all.isLoggedIn()) {
						int b = 0;
						for(Request r : requests) {
							if(r.getState() == RequestState.OPEN) {
								b++;
							}
						}
						sendPluginMessage(all.getSupporter(), "requests-available", new Placeholder("[count]", String.valueOf(b)));
					}
				}
			}, 1000, time, TimeUnit.MILLISECONDS);
		}
	}
	
	private void verifyExpireTask() {
		long time = SupportChat.getTime(getString(CONFIG, "request-auto-delete-after"));
		if(time > 0) {
			expire = getProxy().getScheduler().schedule(this, () -> {
				for(Iterator<Request> it = requests.iterator(); it.hasNext();) {
					Request r = it.next();
					if(r.getState() == RequestState.OPEN && System.currentTimeMillis() > r.getRequestTime() + time) {
						ProxiedPlayer t = r.getRequestor();
						if(sql != null && sql.isConnected()) {
							try {
								sql.delete(new Value("id", t.getUniqueId().toString()), table);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						it.remove();
						sendPluginMessage(t, "request-expired");
					}
				}
			}, 1, 1, TimeUnit.SECONDS);
		}
	}
	
	private void closeTasks() {
		if(notificator != null)
			notificator.cancel();
		if(expire != null)
			expire.cancel();
	}
	
	public boolean anyRequestOpen() {
		for(Request r : requests) {
			if(r.getState() == RequestState.OPEN) {
				return true;
			}
		}
		return false;
	}
	
	public void reload() {
		closeTasks();
		config.getAdapter().reload();
		messages.getAdapter().reload();
		messages.reInitParser();
		reasons.getAdapter().reload();
		addons.getAdapter().reload();
		mb.kill();
		mb = ActionBar.createOfConfig();
		verifyNotificator();
		verifyExpireTask();
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
				sql.delete(new Value("id", c.getRequest().getRequestor().getUniqueId().toString()), table);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void applyLastRequestToNow(UUID id) {
		if(lastRequest.containsKey(id))
			lastRequest.remove(id);
		lastRequest.put(id, System.currentTimeMillis());
	}
	
	public boolean isInConversation(ProxiedPlayer player) {
		return (getConversationOf(player) != null);
	}
	
	public static Object get(int i, String path) {
		switch (i) {
		case 0:
			return provider.config.getAdapter().cfg.get(path);
		case 1:
			return provider.messages.getParser().getByKey(path);
		case 2:
			return provider.reasons.getAdapter().cfg.get(path);
		case 3:
			return provider.addons.getAdapter().cfg.get(path);
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
		String m = new String(), code = new String();
		boolean waitForCode = false, waitForFormat = false;
		char[] ch = text.toCharArray();
		for(int i = 0; i < ch.length; i++) {
			if(waitForFormat) {
				if(ch[i] != '§') {
					if(ch[i+1] != '§')
						waitForFormat = false;
					code += "§" + ch[i];
				}
			} else if(ch[i] == '§') {
				waitForCode = true;
				code = new String();
			} else if(waitForCode) {
				waitForCode = false;
				code = "§" + ch[i];
				if(ch[i+1] == '§') {
					switch(ch[i+2]) {
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'o':
						waitForFormat = true;
					}
				}
			} else
				m += code + ch[i];
		}
		return new TextComponent(m);
	}
	
}
