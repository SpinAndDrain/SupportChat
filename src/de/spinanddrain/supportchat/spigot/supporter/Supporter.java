package de.spinanddrain.supportchat.spigot.supporter;

import org.bukkit.entity.Player;

import de.spinanddrain.supportchat.spigot.SpigotPlugin;

public class Supporter {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	private Player supporter;
	private boolean logged;
	private boolean hidden;
	private boolean talking;
	private boolean listening;
	
	public Supporter(Player supporter) {
		this.supporter = supporter;
		this.logged = false;
		this.talking = false;
		this.listening = false;
		this.hidden = false;
	}
	
	public Player getSupporter() {
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
	
	public static Supporter cast(Player player) {
		for(Supporter i : SpigotPlugin.provide().getOnlineSupporters()) {
			if(i.getSupporter() == player) {
				return i;
			}
		}
		return null;
	}
	
}
