package de.spinanddrain.supportchat.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.Server;
import de.spinanddrain.supportchat.ServerVersion;
import de.spinanddrain.supportchat.SupportChat;
import de.spinanddrain.supportchat.external.sql.MySQL;
import de.spinanddrain.supportchat.external.sql.exception.ConnectionFailedException;
import de.spinanddrain.supportchat.external.sql.exception.QueryException;
import de.spinanddrain.supportchat.external.sql.exception.WrongDatatypeException;
import de.spinanddrain.supportchat.external.sql.overlay.DataValue;
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
import de.spinanddrain.supportchat.spigot.configuration.Addons;
import de.spinanddrain.supportchat.spigot.configuration.Config;
import de.spinanddrain.supportchat.spigot.configuration.ConfigurationHandler;
import de.spinanddrain.supportchat.spigot.configuration.Datasaver;
import de.spinanddrain.supportchat.spigot.configuration.Defaults;
import de.spinanddrain.supportchat.spigot.configuration.Messages;
import de.spinanddrain.supportchat.spigot.configuration.Reasons;
import de.spinanddrain.supportchat.spigot.configuration.RewardsConfiguration;
import de.spinanddrain.supportchat.spigot.conversation.Conversation;
import de.spinanddrain.supportchat.spigot.event.Listeners;
import de.spinanddrain.supportchat.spigot.gui.InventoryWindow;
import de.spinanddrain.supportchat.spigot.gui.WindowItem;
import de.spinanddrain.supportchat.spigot.request.Request;
import de.spinanddrain.supportchat.spigot.request.RequestState;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class SpigotPlugin extends JavaPlugin implements Server {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	private static SpigotPlugin instance;
	
	private List<Request> requests;
	private List<Supporter> supporters;
	private List<Conversation> conversations;
	private List<InventoryWindow> windows;
	
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
	private Updater u;
	private MySQL sql;
	
	private String storedLanguage;
	
	@Override
	public void onEnable() {
		instance = this;
		
		requests = new ArrayList<>();
		supporters = new ArrayList<>();
		conversations = new ArrayList<>();
		windows = new ArrayList<>();
		
		if(getServerVersion() == ServerVersion.UNSUPPORTED_TERMINAL) {
			getServer().getConsoleSender().sendMessage(Updater.prefix + "§c> The plugin does not support your server version!");
			getServer().getConsoleSender().sendMessage(Updater.prefix + "§eDisabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		prepareConfigurations();
		
		getServer().getConsoleSender().sendMessage("§7__________[§9SupportChat §52§7]_________");
		getServer().getConsoleSender().sendMessage(" ");
		getServer().getConsoleSender().sendMessage("§7   Current Version: §b"+instance.getDescription().getVersion());
		getServer().getConsoleSender().sendMessage("§7   Plugin by §cSpinAndDrain");
		getServer().getConsoleSender().sendMessage("§7   Your Serverversion: §b(Spigot) "+getServerVersion().convertFormat());
		String extm = SupportChat.readExternalMessageRaw();
		if(extm != null && !extm.equals(new String())) {
			getServer().getConsoleSender().sendMessage("   "+extm.replace("&", "§"));
		}
		getServer().getConsoleSender().sendMessage("§7__________________________________");
		
		u = new Updater(this);
		
		if(Config.CHECK_UPDATE.asBoolean()) {
			u = new Updater(this);
			u.check(false);
		}
		
		getServer().getOnlinePlayers().forEach(online -> verifyPlayer(online));
		
		if(saver.use()) {
			this.sql = new MySQL(saver.getHost(), String.valueOf(saver.getPort()), saver.getDatabase(), saver.getUser(), saver.getPassword());
			getLogger().log(Level.WARNING, "The MySQL connection is established synchronously. It could take a while until your server is ready.");
			try {
				this.sql.connect();
				this.sql.createTable(saver.getDatabaseTable());
				for(String key : this.sql.getStringifiedKeys(saver.getDatabaseTable(), "id")) {
					Player pp = getServer().getPlayer(UUID.fromString(key));
					if(pp != null) {
						requests.add(new Request(pp, this.sql.getString(saver.getDatabaseTable(), new DataValue("id", key), "reason")));
					}
				}
			} catch (QueryException | WrongDatatypeException | ConnectionFailedException e) {
				e.printStackTrace();
			}
		}
		
		mb = ActionBar.createOfConfig();
		
		registerCommand(new Support());
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
				getLogger().log(Level.WARNING, "Plugin 'Essentials' not found. The Addon 'Essentials-AFK-Hook' was not enabled.");
		}
		
		initialConstantItems();
		
		storedLanguage = getLanguage();
	}
	
	@Override
	public void onDisable() {
		try {
			mb.kill();
			getServer().getOnlinePlayers().forEach(player -> player.closeInventory());
			lcroutine();
		} catch(NullPointerException e) {}
		if(sql != null && sql.isConnected()) {
			try {
				sql.disconnect();
			} catch (ConnectionFailedException e) {
				e.printStackTrace();
			}
		}
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
	
	public MySQL getSql() {
		return sql;
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
	
	public int getRequestPages() {
		return (requests.size() / 53);
	}
	
	private void lcroutine() {
		final File m = new File("plugins/SupportChat/messages.yml");
		if(!storedLanguage.equals(getLanguage())) {
			m.delete();
			prepareConfigurations();
		}
	}
	
	public void reload() {
		lcroutine();
		config.reload();
		messages.reload();
		reasons.reload();
		Addons.provide().getConfigurationHandler().reload();
		RewardsConfiguration.provide().getConfigurationHandler().reload();
		mb.kill();
		mb = ActionBar.createOfConfig();
		initialConstantItems();
		saver.getHandler().reload();
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
		def = messages.getBuilder().preBuild("Messages " + getLanguage() + " v" + getPluginVersion());
		for(Messages i : Messages.values()) {
			def.add(i.getPath(), (getLanguage().equals("DE") ? i.solution()[0] : i.solution()[1]));
		}
		def.build();
		
		reasons = new ConfigurationHandler(new File("plugins/SupportChat/reasons.yml"));
		def = reasons.getBuilder().preBuild("Reasons v" + getPluginVersion() + " | Mode: ENABLED, DISABLED, ABSOLUTE_DISABLED");
		def.add(Reasons.MODE.getPath(), Reasons.MODE.solution().toString());
		def.add(Reasons.REASONS.getPath(), Reasons.REASONS.stringListSolution());
		def.build();
		
		Addons.initial();
		RewardsConfiguration.initial();
		saver = new Datasaver();
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
				sql.deleteAll(saver.getDatabaseTable(), new DataValue("id", c.getRequest().getRequestor().getUniqueId().toString()));
			} catch (WrongDatatypeException | QueryException e) {
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
		return Config.LANGUAGE.asString();
	}

	@Override
	public String getPluginVersion() {
		return getDescription().getVersion();
	}
	
}
