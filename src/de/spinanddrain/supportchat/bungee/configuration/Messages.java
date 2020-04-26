package de.spinanddrain.supportchat.bungee.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.spinanddrain.supportchat.bungee.ConfigAdapter;
import de.spinanddrain.supportchat.external.lscript.LScriptParser;
import de.spinanddrain.supportchat.external.lscript.LScriptReader;
import de.spinanddrain.supportchat.external.lscript.LScriptWriter;
import de.spinanddrain.supportchat.external.lscript.exception.FileNotSupportedException;
import de.spinanddrain.supportchat.external.lscript.exception.ScriptSyntaxException;
import net.md_5.bungee.config.Configuration;

public class Messages {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */
	
	public static final int length = 56 + 1;
	
	private ConfigAdapter adapter;
	private LScriptParser parser;
	
	public Messages() {
		File parent = new File("plugins/SupportChat/messages");
		if(!parent.exists()) {
			parent.mkdirs();
		}
		adapter = new ConfigAdapter(new File("plugins/SupportChat/messages/messages.yml")) {
			@Override
			public void copyDefaults(Configuration cfg) {
				addDefault("language-file", "en.lang");
			}
		};
		File en = new File("plugins/SupportChat/messages/en.lang");
		if(!en.exists()) {
			try {
				en.createNewFile();
				new LScriptWriter(en, "EN-1.0").addComment("Language file for ENGLISH V1.0")
				.addTranslation("prefix", "&7[&6SupportChat&7]")
				.addTranslation("no-permission", "&cNo permission!")
				.addTranslation("new-request", "&9A new request is available! &7(/requests)")
				.addTranslation("queue-joined", "&aYou are in the queue now. Please have some patience.")
				.addTranslation("already-in-queue", "&cYou are already in the queue!")
				.addTranslation("crwl", "&cYou can''t request support while you are logged in.")
				.addTranslation("clwr", "&cYou can''t log in if you have requested support!")
				.addTranslation("conversation-already-running", "&cThe conversation is already running!")
				.addTranslation("conversation-not-started", "&cThe conversation was not started yet!")
				.addTranslation("request-no-longer-availabe", "&cThe request is no longer available!")
				.addTranslation("requests-available", "&b[count] &9requests are still available")
				.addTranslation("you-are-now-in-a-conversation", "&6You are now in a conversation with &b[player]&6!")
				.addTranslation("successfully-denied", "&eSuccessfully denied.")
				.addTranslation("you-got-denied", "&cYour request was denied!")
				.addTranslation("no-conversation", "&cYou are not in a conversation!")
				.addTranslation("already-in-conversation", "&cYou are already in a conversation!")
				.addTranslation("only-leaders", "&cOnly the conversation leader is allowed to end a conversation!")
				.addTranslation("conversation-ended", "&eThe conversation was ended!")
				.addTranslation("already-logged-in", "&cYou are already logged in!")
				.addTranslation("not-logged-in", "&cYou are not logged in!")
				.addTranslation("successfully-logged-in", "&eYou logged in successfully!")
				.addTranslation("successfully-logged-out", "&eYou logged out successfully!")
				.addTranslation("other-login", "&e[player] &ahas logged in!")
				.addTranslation("other-logout", "&e[player] &chas logged out!")
				.addTranslation("logged-players", "&fLogged players: [players]")
				.addTranslation("nobody-online", "&eCurrently nobody online.")
				.addTranslation("listen-join", "&7[&a+&7] &7[player]")
				.addTranslation("listen-leave", "&7[&c-&7] &7[player]")
				.addTranslation("invalid-conversation-id", "&cInvalid ID!")
				.addTranslation("successfully-joined", "&eSuccessfully joined the conversation &7([id])&e!")
				.addTranslation("successfully-leaved", "&cConversation leaved.")
				.addTranslation("reloading", "&eReloading...")
				.addTranslation("successfully-reloaded", "&aReloaded!")
				.addTranslation("reloading-failed", "&eReloading failed!")
				.addTranslation("chat-layout", "&6[player]: &b[message]")
				.addTranslation("chat-layout-local", "&6You: &b[message]")
				.addTranslation("no-requests", "&cNo requests available!")
				.addTranslation("gui-update", "&cRefresh")
				.addTranslation("gui-page", "&aPage: [page]")
				.addTranslation("gui-reason", "&9Reason: &a[reason]")
				.addTranslation("gui-header", "&9&l&nRequests")
				.addTranslation("gui-accept", "&a[Accept]")
				.addTranslation("gui-back", "&c[Back]")
				.addTranslation("gui-listen", "&e[Listen]")
				.addTranslation("gui-deny", "&c[Deny]")
				.addTranslation("gui-edit", "&eEdited by: &b[supporter] &7([id])")
				.addTranslation("syntax", "&cSyntax: &e[command]")
				.addTranslation("syntax-support", "/support [<reason>]")
				.addTranslation("syntax-reasons", "&fAvailable reasons: &a[reasons]")
				.addTranslation("syntax-requests", "/requests or /req")
				.addTranslation("syntax-end", "/scend")
				.addTranslation("syntax-login", "/sclogin [<hidden>]")
				.addTranslation("syntax-logout", "/sclogout")
				.addTranslation("syntax-loglist", "/loglist")
				.addTranslation("syntax-listen", "/listen [<Conversation-ID>]")
				.addTranslation("syntax-reload", "/screload")
					.write();
			} catch (IOException | FileNotSupportedException e) {
				e.printStackTrace();
			}
		}
		File de = new File("plugins/SupportChat/messages/de.lang");
		if(!de.exists()) {
			try {
				de.createNewFile();
				new LScriptWriter(de, "DE-1.0").addComment("Language file for DEUTSCH V1.0")
					.addTranslation("prefix", "&7[&6SupportChat&7]")
					.addTranslation("no-permission", "&cKeine Rechte!")
					.addTranslation("new-request", "&9Eine neue Supportanfrage ist verfübar! &7(/requests)")
					.addTranslation("queue-joined", "&aDu befindest dich nun in der Warteschlange! Bitte habe etwas Geduld!")
					.addTranslation("already-in-queue", "&cDu befindest dich bereits in der Warteschlange!")
					.addTranslation("crwl", "&cDu kannst keinen Support anfordern, wenn du selbst ein Supporter bist!")
					.addTranslation("clwr", "&cDu kannst dich nicht einloggen, wenn du Support angefordert hast!")
					.addTranslation("conversation-already-running", "&cDie Konversation läuft bereits!")
					.addTranslation("conversation-not-started", "&cDie Konversation wurde noch nicht gestartet!")
					.addTranslation("request-no-longer-availabe", "&cDie Anfrage ist nicht mehr verfügbar!")
					.addTranslation("requests-available", "&b[count] &9Anfragen sind noch unbearbeitet")
					.addTranslation("you-are-now-in-a-conversation", "&6Du befindest dich nun in einer Konversation mit &b[player]&6!")
					.addTranslation("successfully-denied", "&eErfolgreich abgelehnt.")
					.addTranslation("you-got-denied", "&cDeine Anfrage wurde abgelehnt!")
					.addTranslation("no-conversation", "&cDu befindest dich in keiner Konversation!")
					.addTranslation("already-in-conversation", "&cDu befindest dich bereits in einer Konversation!")
					.addTranslation("only-leaders", "&cNur der Konversationsleiter darf das Gespräch beenden!")
					.addTranslation("conversation-ended", "&eDie Konversation wurde beendet!")
					.addTranslation("already-logged-in", "&cDu bist bereits eingeloggt!")
					.addTranslation("not-logged-in", "&cDu bist nicht eingeloggt!")
					.addTranslation("successfully-logged-in", "&eDu hast dich erfolgreich eingeloggt!")
					.addTranslation("successfully-logged-out", "&eDu hast dich erfolgreich ausgeloggt!")
					.addTranslation("other-login", "&e[player] &ahat sich eingeloggt!")
					.addTranslation("other-logout", "&e[player] &chat sich ausgeloggt!")
					.addTranslation("logged-players", "&fEingeloggte Spieler: [players]")
					.addTranslation("nobody-online", "&eDerzeit ist niemand online.")
					.addTranslation("listen-join", "&7[&a+&7] &7[player]")
					.addTranslation("listen-leave", "&7[&c-&7] &7[player]")
					.addTranslation("invalid-conversation-id", "&cUngültige ID!")
					.addTranslation("successfully-joined", "&eErfolgreich der Konversation §7([id]) §ebeigetreten!")
					.addTranslation("successfully-leaved", "&cDu hast die Konversation verlassen.")
					.addTranslation("reloading", "&eReloading...")
					.addTranslation("successfully-reloaded", "&aReloaded!")
					.addTranslation("reloading-failed", "&cReloading fehlgeschlagen!")
					.addTranslation("chat-layout", "&6[player]: &b[message]")
					.addTranslation("chat-layout-local", "&6Du: &b[message]")
					.addTranslation("no-requests", "&cDerzeit sind keine Anfragen verfügbar!")
					.addTranslation("gui-update", "&cAktualisieren")
					.addTranslation("gui-page", "&aSeite: [page]")
					.addTranslation("gui-reason", "&9Grund: &a[reason]")
					.addTranslation("gui-header", "&9&l&nSupportanfragen")
					.addTranslation("gui-accept", "&a[Annehmen]")
					.addTranslation("gui-back", "&c[Zurück]")
					.addTranslation("gui-listen", "&e[Zuhören]")
					.addTranslation("gui-deny", "&c[Ablehnen]")
					.addTranslation("gui-edit", "&eBearbeitet: &b[supporter] &7([id])")
					.addTranslation("syntax", "&cSyntax: &e[command]")
					.addTranslation("syntax-support", "/support [<Grund>]")
					.addTranslation("syntax-reasons", "&fVerfügbare Gründe: &a[reasons]")
					.addTranslation("syntax-requests", "/requests oder /req")
					.addTranslation("syntax-end", "/scend")
					.addTranslation("syntax-login", "/sclogin [<hidden>]")
					.addTranslation("syntax-logout", "/sclogout")
					.addTranslation("syntax-loglist", "/loglist")
					.addTranslation("syntax-listen", "/listen [<Conversation-ID>]")
					.addTranslation("syntax-reload", "/screload")
					.write();
			} catch (IOException | FileNotSupportedException e) {
				e.printStackTrace();
			}
		}
		reInitParser();
	}
	
	public void reInitParser() {
		try {
			parser = new LScriptParser(new LScriptReader(new File("plugins/SupportChat/messages/"+adapter.cfg.getString("language-file"))).read());
		} catch (FileNotFoundException | FileNotSupportedException | ScriptSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigAdapter getAdapter() {
		return adapter;
	}

	public LScriptParser getParser() {
		return parser;
	}
	
}
