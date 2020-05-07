package de.spinanddrain.supportchat.spigot.configuration;

import de.spinanddrain.lscript.tools.LParser;
import de.spinanddrain.supportchat.spigot.SpigotPlugin;

public enum Messages {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	PREFIX,
	NO_PERMISSION,
	CANT_EXECUTE_COMMAND,
	NEW_REQUEST,
	REQUEST_EXPIRED,
	QUEUE_JOINED,
	QUEUE_LEFT,
	ALREADY_IN_QUEUE,
	REQUESTING_TOO_FAST,
	NOT_REQUESTED,
	CRWL,
	CLWR,
	CONVERSATION_ALREADY_RUNNING,
	CONVERSATION_NOT_STARTED,
	REQUEST_NO_LONGER_AVAILABE,
	REQUESTS_AVAILABLE,
	YOU_ARE_NOW_IN_A_CONVERSATION,
	SUCCESSFULLY_DENIED,
	YOU_GOT_DENIED,
	NO_CONVERSATION,
	ALREADY_IN_CONVERSATION,
	ONLY_LEADERS,
	CONVERSATION_ENDED,
	ALREADY_LOGGED_IN,
	NOT_LOGGED_IN,
	SUCCESSFULLY_LOGGED_IN,
	SUCCESSFULLY_LOGGED_OUT,
	OTHER_LOGIN,
	OTHER_LOGOUT,
	LOGGED_PLAYERS,
	NOBODY_ONLINE,
	LISTEN_JOIN,
	LISTEN_LEAVE,
	INVALID_CONVERSATION_ID,
	SUCCESSFULLY_JOINED,
	SUCCESSFULLY_LEAVED,
	RELOADING,
	SUCCESSFULLY_RELOADED,
	RELOADING_FAILED,
	CHAT_LAYOUT,
	CHAT_LAYOUT_LOCAL,
	
	INVENTORY_REQUESTS,
	INVENTORY_REFRESH,
	INVENTORY_REASON,
	INVENTORY_INFO,
	INVENTORY_CLICK_TO_ACCEPT,
	INVENTORY_ID,
	MANAGER_REQUEST,
	INVENTORY_ACCEPT,
	INVENTORY_BACK,
	INVENTORY_LISTEN,
	INVENTORY_DENY,
	
	SYNTAX,
	SYNTAX_SUPPORT,
	SYNTAX_LEAVE,
	SYNTAX_REASONS,
	SYNTAX_REQUESTS,
	SYNTAX_END,
	SYNTAX_LOGIN,
	SYNTAX_LOGOUT,
	SYNTAX_LOGLIST,
	SYNTAX_LISTEN,
	SYNTAX_RELOAD;
	
	private final String path;
	private LParser parser;
	
	private Messages() {
		this.path = this.toString().toLowerCase().replaceAll("_", "-");
		this.parser = SpigotPlugin.provide().getMessager();
	}
	
	public String getPath() {
		return path;
	}
	
	public String getWithoutPrefix() {
		return parser.getByKey(path).replaceAll("&", "§");
	}
	
	public String getMessage() {
		return (PREFIX.getWithoutPrefix()) + " " + getWithoutPrefix();
	}
	
	public String getWithoutPrefixWithPlaceholder(Placeholder placeholder) {
		return getWithoutPrefixWithPlaceholder(new Placeholder[]{placeholder});
	}
	
	public String getWithoutPrefixWithPlaceholder(Placeholder... placeholders) {
		String message = parser.getByKey(path).replaceAll("&", "§");
		for(Placeholder i : placeholders) {
			message = message.replace(i.placeholder, i.replacement);
		}
		return message;
	}
	
	public String getWithPlaceholder(Placeholder placeholder) {
		return getMessage().replace(placeholder.placeholder, placeholder.replacement);
	}
	
	public String getWithPlaceholder(Placeholder... placeholders) {
		String message = getMessage();
		for(Placeholder i : placeholders) {
			message = message.replace(i.placeholder, i.replacement);
		}
		return message;
	}
	
	public static void refresh() {
		for(Messages v : values())
			v.parser = SpigotPlugin.provide().getMessager();
	}
	
}
