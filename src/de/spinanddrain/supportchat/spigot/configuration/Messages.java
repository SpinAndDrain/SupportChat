package de.spinanddrain.supportchat.spigot.configuration;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;

public enum Messages {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */
	
	PREFIX("&7[&6SupportChat&7]", "&7[&6SupportChat&7]"),
	NO_PERMISSION("&cKeine Rechte!", "&cNo permission!"),
	CANT_EXECUTE_COMMAND("&cDu kannst diesen Befehl nicht ausführen!", "&cYou can't execute this command!"),
	NEW_REQUEST("&9Eine neue Supportanfrage ist verfübar! &7(/requests)", "&9A new request is available! &7(/requests)"),
	QUEUE_JOINED("&aDu befindest dich nun in der Warteschlange! Bitte habe etwas Geduld!", "&aYou are in the queue now. Please have some patience."),
	ALREADY_IN_QUEUE("&cDu befindest dich bereits in der Warteschlange!", "&cYou are already in the queue!"),
	CRWL("&cDu kannst keinen Support anfordern, wenn du selbst ein Supporter bist!", "&cYou can't request support while you are logged in."),
	CLWR("&cDu kannst dich nicht einloggen, wenn du Support angefordert hast!", "&cYou can't log in if you have requested support!"),
	CONVERSATION_ALREADY_RUNNING("&cDie Konversation läuft bereits!", "&cThe conversation is already running!"),
	CONVERSATION_NOT_STARTED("&cDie Konversation wurde noch nicht gestartet!", "&cThe conversation was not started yet!"),
	REQUEST_NO_LONGER_AVAILABE("&cDie Anfrage ist nicht mehr verfügbar!", "&cThe request is no longer available!"),
	REQUESTS_AVAILABLE("&b[count] &9requests are still available", "&b[count] &9Anfragen sind noch unbearbeitet"),
	YOU_ARE_NOW_IN_A_CONVERSATION("&6Du befindest dich nun in einer Konversation mit &b[player]&6!", "&6You are now in a conversation with &b[player]&6!"),
	SUCCESSFULLY_DENIED("&eErfolgreich abgelehnt.", "&eSuccessfully denied."),
	YOU_GOT_DENIED("&cDeine Anfrage wurde abgelehnt!", "&cYour request was denied!"),
	NO_CONVERSATION("&cDu befindest dich in keiner Konversation!", "&cYou are not in a conversation!"),
	ALREADY_IN_CONVERSATION("&cDu befindest dich bereits in einer Konversation!", "&cYou are already in a conversation!"),
	ONLY_LEADERS("&cNur der Konversationsleiter darf das Gespräch beenden!", "&cOnly the conversation leader is allowed to end a conversation!"),
	CONVERSATION_ENDED("&eDie Konversation wurde beendet!", "&eThe conversation was ended!"),
	ALREADY_LOGGED_IN("&cDu bist bereits eingeloggt!", "&cYou are already logged in!"),
	NOT_LOGGED_IN("&cDu bist nicht eingeloggt!", "&cYou are not logged in!"),
	SUCCESSFULLY_LOGGED_IN("&eDu hast dich erfolgreich eingeloggt!", "&eYou logged in successfully!"),
	SUCCESSFULLY_LOGGED_OUT("&eDu hast dich erfolgreich ausgeloggt!", "&eYou logged out successfully!"),
	OTHER_LOGIN("&e[player] &ahat sich eingeloggt!", "&e[player] &ahas logged in!"),
	OTHER_LOGOUT("&e[player] &chat sich ausgeloggt!", "&e[player] &chas logged out!"),
	LOGGED_PLAYERS("&fEingeloggte Spieler: [players]", "&fLogged players: [players]"),
	NOBODY_ONLINE("&eDerzeit ist niemand online.", "&eCurrently nobody online."),
	LISTEN_JOIN("&7[&a+&7] &7[player]", "&7[&a+&7] &7[player]"),
	LISTEN_LEAVE("&7[&c-&7] &7[player]", "&7[&c-&7] &7[player]"),
	INVALID_CONVERSATION_ID("&cUngültige ID!", "&cInvalid ID!"),
	SUCCESSFULLY_JOINED("&eErfolgreich der Konversation §7([id]) §ebeigetreten!", "&eSuccessfully joined the conversation &7([id])&e!"),
	SUCCESSFULLY_LEAVED("&cDu hast die Konversation verlassen.", "&cConversation leaved."),
	RELOADING("&eReloading...", "&eReloading..."),
	SUCCESSFULLY_RELOADED("&aReloaded!", "&aReloaded!"),
	RELOADING_FAILED("&cReloading fehlgeschlagen!", "&eReloading failed!"),
	CHAT_LAYOUT("&6[player]: &b[message]", "&6[player]: &b[message]"),
	CHAT_LAYOUT_LOCAL("&6Du: &b[message]", "&6You: &b[message]"),
	
	INVENTORY_REQUESTS("&9&lSupportanfragen", "&9&lRequests"),
	INVENTORY_REFRESH("&cAktualisieren", "&cRefresh"),
	INVENTORY_REASON("&9Grund: &c[reason]", "&9Reason: &c[reason]"),
	INVENTORY_INFO("&9Info: &cWird bearbeitet von &e[supporter]", "&9Info: &cProcessing by &e[supporter]"),
	INVENTORY_CLICK_TO_ACCEPT("&bKlicke zum Annehmen!", "&bClick to accept!"),
	INVENTORY_ID("&eConversation-ID: [id]", "&eConversation-ID: [id]"),
	MANAGER_REQUEST("&9&lAnfrag verwalten", "&9&lManage request"),
	INVENTORY_ACCEPT("&aAnnehmen", "&aAccept"),
	INVENTORY_BACK("&cZurück", "&cBack"),
	INVENTORY_LISTEN("&eZuhören", "&eListen"),
	INVENTORY_DENY("&cAblehnen", "&cDeny"),
	
	SYNTAX("&cSyntax: &e[command]", "&cSyntax: &e[command]"),
	SYNTAX_SUPPORT("/support [<Grund>]", "/support [<reason>]"),
	SYNTAX_REASONS("&fVerfügbare Gründe: &a[reasons]", "&fAvailable reasons: &a[reasons]"),
	SYNTAX_REQUESTS("/requests oder /req", "/requests or /req"),
	SYNTAX_END("/scend", "/scend"),
	SYNTAX_LOGIN("/sclogin [<hidden>]", "/sclogin [<hidden>]"),
	SYNTAX_LOGOUT("/sclogout", "/sclogout"),
	SYNTAX_LOGLIST("/loglist", "/loglist"),
	SYNTAX_LISTEN("/listen [<Conversation-ID>]", "/listen [<Conversation-ID>]"),
	SYNTAX_RELOAD("/screload", "/screload");
	
	private final String path;
	private final String german;
	private final String english;
	
	private final ConfigurationHandler handler;
	
	private Messages(final String german, final String english) {
		this.path = this.toString().toLowerCase().replaceAll("_", "-");
		this.german = german;
		this.english = english;
		this.handler = SpigotPlugin.provide().getMessages();
	}
	
	public String getPath() {
		return path;
	}
	
	public String[] solution() {
		return new String[]{german, english};
	}
	
	public String getWithoutPrefix() {
		return handler.configuration.getString(path).replaceAll("&", "§");
	}
	
	public String getMessage() {
		return (PREFIX.getWithoutPrefix()) + " " + getWithoutPrefix();
	}
	
	public String getWithoutPrefixWithPlaceholder(Placeholder placeholder) {
		return getWithoutPrefixWithPlaceholder(new Placeholder[]{placeholder});
	}
	
	public String getWithoutPrefixWithPlaceholder(Placeholder... placeholders) {
		String message = handler.configuration.getString(path).replaceAll("&", "§");
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
	
}
