package de.spinanddrain.supportchat.bungee.supporter;

import de.spinanddrain.supportchat.bungee.BungeePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Supporter {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	private ProxiedPlayer supporter;
	private boolean logged;
	private boolean hidden;
	private boolean talking;
	private boolean listening;
	
	public Supporter(ProxiedPlayer supporter) {
		this.supporter = supporter;
		this.logged = false;
		this.talking = false;
		this.listening = false;
		this.hidden = false;
	}
	
	public ProxiedPlayer getSupporter() {
		return supporter;
	}
	
	public boolean isLoggedIn() {
		return logged;
	}
	
	public boolean isTalking() {
		return talking;
	}
	
	public boolean isListening() {
		return listening;
	}
	
	public boolean isHidden() {
		return (logged && hidden);
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public void setListening(boolean listening) {
		this.listening = listening;
	}
	
	public void setLoggedIn(boolean logged) {
		this.logged = logged;
	}
	
	public void setTalking(boolean talking) {
		this.talking = talking;
	}
	
	public static Supporter cast(ProxiedPlayer player) {
		for(Supporter i : BungeePlugin.provide().getOnlineSupporters()) {
			if(i.getSupporter() == player) {
				return i;
			}
		}
		return null;
	}
	
}
