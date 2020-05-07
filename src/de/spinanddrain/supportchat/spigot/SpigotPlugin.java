package de.spinanddrain.supportchat.spigot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.spinanddrain.logging.Log;
import de.spinanddrain.logging.LogType;
import de.spinanddrain.lscript.exception.FileNotSupportedException;
import de.spinanddrain.lscript.exception.ScriptException;
import de.spinanddrain.lscript.resources.Variable;
import de.spinanddrain.lscript.tools.LParser;
import de.spinanddrain.lscript.tools.LReader;
import de.spinanddrain.lscript.tools.LReader.ScriptType;
import de.spinanddrain.lscript.tools.LWriter;
import de.spinanddrain.lscript.tools.LWriter.OverridingMethod;
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
import de.spinanddrain.supportchat.spigot.DownloadSession.TFile;
import de.spinanddrain.supportchat.spigot.addons.AFKHook;
import de.spinanddrain.supportchat.spigot.addons.ActionBar;
import de.spinanddrain.supportchat.spigot.addons.AfkListener;
import de.spinanddrain.supportchat.spigot.addons.Rewards;
import de.spinanddrain.supportchat.spigot.addons.Rewards.RewardNotAvailableException;
import de.spinanddrain.supportchat.spigot.command.End;
import de.spinanddrain.supportchat.spigot.command.FAQ;
import de.spinanddrain.supportchat.spigot.command.Listen;
import de.spinanddrain.supportchat.spigot.command.Login;
import de.spinanddrain.supportchat.spigot.command.Loglist;
import de.spinanddrain.supportchat.spigot.command.Logout;
import de.spinanddrain.supportchat.spigot.command.Reload;
import de.spinanddrain.supportchat.spigot.command.Requests;
import de.spinanddrain.supportchat.spigot.command.SpigotCommand;
import de.spinanddrain.supportchat.spigot.command.Support;
import de.spinanddrain.supportchat.spigot.command.Supportleave;
import de.spinanddrain.supportchat.spigot.configuration.Addons;
import de.spinanddrain.supportchat.spigot.configuration.Config;
import de.spinanddrain.supportchat.spigot.configuration.ConfigurationHandler;
import de.spinanddrain.supportchat.spigot.configuration.Datasaver;
import de.spinanddrain.supportchat.spigot.configuration.Defaults;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Placeholder;
import de.spinanddrain.supportchat.spigot.configuration.Reasons;
import de.spinanddrain.supportchat.spigot.configuration.RewardsConfiguration;
import de.spinanddrain.supportchat.spigot.conversation.Conversation;
import de.spinanddrain.supportchat.spigot.event.Listeners;
import de.spinanddrain.supportchat.spigot.gui.InventoryWindow;
import de.spinanddrain.supportchat.spigot.gui.WindowItem;
import de.spinanddrain.supportchat.spigot.request.Request;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;
import de.spinanddrain.updater.Updater;
import de.spinanddrain.updater.VersionPattern;

public class SpigotPlugin extends JavaPlugin implements Server {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	private static SpigotPlugin instance;
	
	private Log logger;
	private Updater u;
	private final String consolePrefix = "§7[§6SupportChat§7]§r";
	private final File t = new File(".sccd1");
	
	private List<Request> requests;
	private List<Supporter> supporters;
	private List<Conversation> conversations;
	private List<InventoryWindow> windows;
	
	private Map<UUID, Long> lastRequest;
	
	private ConfigurationHandler config;
	private ConfigurationHandler messages;
	private ConfigurationHandler reasons;
	private Datasaver saver;
	
	public WindowItem refresh;
	public WindowItem accept;
	public WindowItem back;
	public WindowItem listen;
	public WindowItem deny;
	
	private ActionBar mb;
	private Connection sql;
	private Table table;
	
	private LParser message;
	
	private int notificator, expire;
	
	@Override
	public void onEnable() {
		instance = this;
		
		logger = new Log(getServer().getConsoleSender(), getServer().getLogger(), LogType.RAW);
		
		VersionPattern pattern = new VersionPattern(SupportChat.DEPENDENCY_VERSION);
		String dependencyVersion = getServer().getPluginManager().getPlugin("LibsCollection").getDescription().getVersion();
		if(!pattern.isEqual(dependencyVersion)) {
			if(!(SupportChat.HIGHER && pattern.isOlderThan(dependencyVersion))) {
				logger.log(consolePrefix, "§cThe dependency §eLibsCollection §chas an invalid version (§e" + dependencyVersion + "§c)."
						+ " Version §e" + SupportChat.DEPENDENCY_VERSION + "§c " + (SupportChat.HIGHER ? "(or higher) " : "") 
						+ "is required!");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		
		u = new Updater(ResourceIdParser.defaultPrid()
				.getResourceIdByKey("de.spinanddrain.supportchat"), getPluginVersion());
		
		requests = new ArrayList<>();
		supporters = new ArrayList<>();
		conversations = new ArrayList<>();
		windows = new ArrayList<>();
		
		lastRequest = new HashMap<UUID, Long>();
		
		if(getServerVersion() == ServerVersion.UNSUPPORTED_TERMINAL) {
			logger.log(consolePrefix, "§c> The plugin does not support your server version! Please update your server"
					+ " to continue using SupportChat.");
			logger.log(consolePrefix, "§eDisabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		logger.log(consolePrefix, "§ePreparing files...");
		
		if(t.exists()) {
			try {
				TFile.fetch(t.getName()).performAction();
				t.delete();
			} catch (IOException e) {
				logger.log(consolePrefix, "§cFailed to remove old SupportChat data. This may cause errors, please check"
						+ " your plugins folder for multiple SupportChat Jar files.");
				getServer().getPluginManager().disablePlugin(this);
				t.delete();
				return;
			}
		}
		
		prepareConfigurations();
		
		for(String i : SupportChat.getTextField("[§9SupportChat §43§7]", "§7Current Version: §b"+getPluginVersion()+"§r",
				"§7Plugin by §cSpinAndDrain§r", "§7Your Serverversion: §b(Spigot) "+getServerVersion().convertFormat()+"§r")) {
			logger.log(i);
		}
		
		String extm = SupportChat.readExternalMessageRaw();
		if(extm != null && !extm.equals(new String())) {
			logger.log("§7[§eSupportChat: INFO§7]§r", extm.replace("&", "§"));
		}
		
		if(Config.UPDATER$CHECK_ON_STARTUP.asBoolean()) {
			logger.log(consolePrefix, "§eSearching for updates...");
			try {
				if(u.isAvailable()) {
					logger.log(consolePrefix, "§eA newer version is available: §b" + u.getLatestVersion());
					if(Config.UPDATER$AUTO_DOWNLOAD.asBoolean()) {
						try {
							logger.log(consolePrefix, "§eDownloading...");
							u.installLatestVersion(new DownloadSession(this));
							return;
						} catch(Exception e) {
							logger.log(consolePrefix, "§cAn error occurred while downloading the update.");
							e.printStackTrace();
						}
					}
				} else
					logger.log(consolePrefix, "§eNo updates found. You are running the latest version of SupportChat.");
			} catch(Exception e) {
				logger.log(consolePrefix, "§cAn error occurred while searching for updates.");
				e.printStackTrace();
			}
		}
		
		getServer().getOnlinePlayers().forEach(online -> verifyPlayer(online));
		
		if(saver.use()) {
			HashMap<String, Object> p = new HashMap<>();
			p.put("autoReconnect", true);
			p.put("useSSL", saver.useSSL());
			sql = new Connection(saver.getHost(), saver.getPort(), saver.getUser(), saver.getPassword(), saver.getDatabase(), p);
			logger.logTemporary("[SupportChat]", "The MySQL connection is established synchronously. "
					+ "It could take a while until your server is ready.", LogType.WARN);
			try {
				sql.connect();
				table = sql.createTable("supportchat", new Parameter("id", DataType.VARCHAR, 100, true, true, false, null),
						new Parameter("reason", DataType.VARCHAR, 100, false, false, false, null),
						new Parameter("requesttime", DataType.BIGINT, -1, false, true, false, 0L));
				for(String key : sql.getKeys("id", table)) {
					Player pp = getServer().getPlayer(UUID.fromString(key));
					if(pp != null) {
						requests.add(new Request(pp, (String) sql.get(new Value("id", key), "reason", table),
								(long) sql.get(new Value("id", key), "requesttime", table)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		verifyNotificator();
		verifyExpire();
		
		mb = ActionBar.createOfConfig();
		
		registerCommand(new Support());
		registerCommand(new Supportleave());
		registerCommand(new Requests());
		registerCommand(new End());
		registerCommand(new Login());
		registerCommand(new Logout());
		registerCommand(new Loglist());
		registerCommand(new Listen());
		registerCommand(new Reload());
		registerCommand(new FAQ());
		
		Listeners.register();
		
		if(Addons.provide().isAFKHookEnabled()) {
			if(Bukkit.getPluginManager().getPlugin("Essentials") != null) {
				Bukkit.getPluginManager().registerEvents(new AfkListener(), this);
			} else
				logger.logTemporary("[SupportChat]", "Plugin 'Essentials' not found. The Addon 'Essentials-AFK-Hook' was not enabled.",
						LogType.WARN);
		}
		
		initialConstantItems();
	}
	
	@Override
	public void onDisable() {
		try {
			if(mb != null)
				mb.kill();
			getServer().getOnlinePlayers().forEach(player -> player.closeInventory());
		} catch(NullPointerException e) {}
		if(sql != null && sql.isConnected()) {
			try {
				sql.disconnect();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
		closeNotificatorQuietly();
		closeExpireQuietly();
	}
	
	public static SpigotPlugin provide() {
		return instance;
	}
	
	public void callAFKEvent(AFKHook event) {
		getServer().getPluginManager().callEvent(event);
	}
	
	public Datasaver getSaver() {
		return saver;
	}
	
	public Connection getSql() {
		return sql;
	}
	
	public Table getTable() {
		return table;
	}
	
	public Map<UUID, Long> getLastRequested() {
		return lastRequest;
	}
	
	public void applyLastRequestToNow(UUID id) {
		if(lastRequest.containsKey(id))
			lastRequest.remove(id);
		lastRequest.put(id, System.currentTimeMillis());
	}
	
	public ConfigurationHandler getNativeConfig() {
		return config;
	}
	
	public ConfigurationHandler getMessages() {
		return messages;
	}
	
	public ConfigurationHandler getReasons() {
		return reasons;
	}
	
	public ActionBar getMainBar() {
		return mb;
	}
	
	public LParser getMessager() {
		return message;
	}
	
	public int getRequestPages() {
		return (requests.size() / 53);
	}
	
	private void verifyNotificator() {
		long duration = Config.AUTO_NOTIFICATION.asTime();
		if(duration > 0) {
			notificator = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
				for(Supporter all : supporters) {
					if(this.anyRequestOpen() && all.isLoggedIn()) {
						int b = 0;
						for(Request r : requests) {
							if(r.getState() == RequestState.OPEN) {
								b++;
							}
						}
						all.getSupporter().sendMessage(Messages.REQUESTS_AVAILABLE.getWithPlaceholder(Placeholder.create("[count]", String.valueOf(b))));
					}
				}
			}, 20, 20 * (duration / 1000));
		}
	}
	
	private void closeNotificatorQuietly() {
		if(getServer().getScheduler().isCurrentlyRunning(notificator)) {
			getServer().getScheduler().cancelTask(notificator);
		}
	}
	
	private void verifyExpire() {
		long duration = Config.REQUEST_AUTO_DELETE_AFTER.asTime();
		if(duration > 0) {
			expire = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
				for(Iterator<Request> it = requests.iterator(); it.hasNext();) {
					Request r = it.next();
					if(r.getState() == RequestState.OPEN && System.currentTimeMillis() > r.getRequestTime() + duration) {
						Player t = r.getRequestor();
						if(sql != null && sql.isConnected()) {
							try {
								sql.delete(new Value("id", t.getUniqueId().toString()), table);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						it.remove();
						t.sendMessage(Messages.REQUEST_EXPIRED.getMessage());
					}
				}
			}, 20, 20);
		}
	}
	
	private void closeExpireQuietly() {
		if(getServer().getScheduler().isCurrentlyRunning(expire)) {
			getServer().getScheduler().cancelTask(expire);
		}
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
		closeNotificatorQuietly();
		closeExpireQuietly();
		config.reload();
		messages.reload();
		prepareLangBasics();
		Messages.refresh();
		reasons.reload();
		Addons.provide().getConfigurationHandler().reload();
		RewardsConfiguration.provide().getConfigurationHandler().reload();
		mb.kill();
		mb = ActionBar.createOfConfig();
		initialConstantItems();
		saver.getHandler().reload();
		verifyNotificator();
		verifyExpire();
	}
	
	private void prepareConfigurations() {
		Defaults def = null;
		
		config = new ConfigurationHandler(new File("plugins/SupportChat/config.yml"));
		def = config.getBuilder().preBuild("Config v" + getPluginVersion() + " | Join-Login: FULL, HIDDEN, PERMISSION_RANGE, DISABLED");
		for(Config i : Config.values()) {
			def.add(i.getPath(), i.solution());
		}
		def.build();
		
		messages = new ConfigurationHandler(new File("plugins/SupportChat/messages.yml"));
		def = messages.getBuilder().preBuild("Messages v" + getPluginVersion());
		def.add("language-file", "en.lang");
		def.build();
		
		prepareLangBasics();
		
		reasons = new ConfigurationHandler(new File("plugins/SupportChat/reasons.yml"));
		def = reasons.getBuilder().preBuild("Reasons v" + getPluginVersion() + " | Mode: ENABLED, DISABLED, ABSOLUTE_DISABLED");
		def.add(Reasons.MODE.getPath(), Reasons.MODE.solution().toString());
		def.add(Reasons.REASONS.getPath(), Reasons.REASONS.stringListSolution());
		def.build();
		
		Addons.initial();
		RewardsConfiguration.initial();
		saver = new Datasaver();
	}
	
	private void prepareLangBasics() {
		createFiles("s_en.lang", new File("plugins/SupportChat/messages/en.lang"), ScriptType.TRANSLATION);
		createFiles("s_de.lang", new File("plugins/SupportChat/messages/de.lang"), ScriptType.TRANSLATION);
		try {
			message = new LReader(new File("plugins/SupportChat/messages/" + messages.reload().getString("language-file")))
					.readAndParse(ScriptType.TRANSLATION);
		} catch (IOException | ScriptException | FileNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	private void createFiles(String resource, File out, ScriptType type) {
		try {
			if(!out.getParentFile().exists())
				out.getParentFile().mkdirs();
			if(!out.exists())
				out.createNewFile();
			InputStream is = this.getResource("resources/spigot/" + resource);
			File f = new File(resource);
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			fos.write(buffer);
			fos.close();
			LParser p = new LReader(f).readAndParse(type);
			LWriter w = new LWriter(out, p.getVersionType(), p.getPattern());
			for(Variable v : p.getVariables())
				w.addVariable(v.getName(), v.getValue());
			w.addTranslation(p.getContent());
			w.write(type, OverridingMethod.UNEXISTING, true);
			f.delete();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initialConstantItems() {
		refresh = new WindowItem(Material.COMPASS, 53, Messages.INVENTORY_REFRESH.getWithoutPrefix());
		accept = new WindowItem(Material.ANVIL, 1, Messages.INVENTORY_ACCEPT.getWithoutPrefix());
		back = new WindowItem(Material.BARRIER, 3, Messages.INVENTORY_BACK.getWithoutPrefix());
		listen = new WindowItem(Material.MAP, 5, Messages.INVENTORY_LISTEN.getWithoutPrefix());
		deny = new WindowItem(Material.PAPER, 7, Messages.INVENTORY_DENY.getWithoutPrefix());
	}
	
	public static ServerVersion getServerVersion() {
		String ver = instance.getServer().getBukkitVersion();
		if(ver.startsWith("1.8")) {
			return ServerVersion.v1_8;
		} else if(ver.startsWith("1.9")) {
			return ServerVersion.v1_9;
		} else if(ver.startsWith("1.10")) {
			return ServerVersion.v1_10;
		} else if(ver.startsWith("1.11")) {
			return ServerVersion.v1_11;
		} else if(ver.startsWith("1.12")) {
			return ServerVersion.v1_12;
		} else if(ver.startsWith("1.13")) {
			return ServerVersion.v1_13;
		} else if(ver.startsWith("1.14")) {
			return ServerVersion.v1_14;
		} else if(ver.startsWith("1.15")) {
			return ServerVersion.v1_15;
		} else {
			return ServerVersion.UNSUPPORTED_TERMINAL;
		}
	}
	
	public void verifyPlayer(Player player) {
		if(Permissions.SUPPORT.hasPermission(player) || Permissions.LOGIN.hasPermission(player)) {
			supporters.add(new Supporter(player));
		}
	}
	
	public void endConversation(Conversation c) {
		if(RewardsConfiguration.provide().isEnabled() && c.getHandler().getSupporter() != null) {
			try {
				new Rewards(c.getHandler().getSupporter()).collect();
			} catch (RewardNotAvailableException e) {
				e.printStackTrace();
			}
		}
		c.setRunning(false);
		c.getRequest().setState(RequestState.FINISHED);
		c.getHandler().setTalking(false);
		c.getListeners().forEach(listener -> listener.setListening(false));
		c.sendAll(Messages.CONVERSATION_ENDED.getMessage());
		conversations.remove(c);
		if(saver.use() && sql.isConnected()) {
			try {
				sql.delete(new Value("id", c.getRequest().getRequestor().getUniqueId().toString()), table);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Conversation getConversationOf(Request base) {
		for(Conversation i : conversations) {
			if(i.getRequest() == base) {
				return i;
			}
		}
		return null;
	}
	
	public Conversation getConversationOf(Player player) {
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
	
	public boolean isInMainConversation(Player player) {
		Conversation conversation = getConversationOf(player);
		return (conversation != null && (conversation.getRequest().getRequestor() == player || conversation.getHandler().getSupporter() == player));
	}
	
	public boolean isInConversation(Player player) {
		return (getConversationOf(player) != null);
	}
	
	@Override
	public List<Request> getRequests() {
		return requests;
	}

	public List<InventoryWindow> getWindows() {
		return windows;
	}
	
	public Request getRequestOf(String name) {
		for(Request i : requests) {
			if(i.getRequestor().getName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	public boolean hasRequested(Player player) {
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
		List<Supporter> visible = new ArrayList<>();
		for(Supporter i : supporters) {
			if(!i.isHidden()) {
				visible.add(i);
			}
		}
		return visible;
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
	public void registerCommand(Object command) {
		if(command instanceof SpigotCommand) {
			SpigotCommand cmd = (SpigotCommand) command;
			getCommand(cmd.getName()).setExecutor(cmd);
		}
	}

	@Override
	public String getLanguage() {
		return "undefined";
	}

	@Override
	public String getPluginVersion() {
		return getDescription().getVersion();
	}
	
}
