package de.spinanddrain.supportchat.bungee.conversation;

import java.util.ArrayList;
import java.util.List;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.request.Request;
import de.spinanddrain.supportchat.bungee.supporter.Supporter;

public class Conversation {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private int id;
	private Request request;
	private Supporter handler;
	private boolean running;
	
	private List<Supporter> listeners;
	
	public Conversation(int id, Request handle, Supporter handler) {
		this.id = id;
		this.request = handle;
		this.handler = handler;
		this.running = false;
		this.listeners = new ArrayList<>();
	}
	
	public int getId() {
		return id;
	}
	
	public Request getRequest() {
		return request;
	}
	
	public Supporter getHandler() {
		return handler;
	}
	
	public List<Supporter> getListeners() {
		return listeners;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void sendAll(String message) {
		BungeePlugin.sendMessage(request.getRequestor(), message);
		BungeePlugin.sendMessage(handler.getSupporter(), message);
		for(Supporter i : listeners) {
			BungeePlugin.sendMessage(i.getSupporter(), message);
		}
	}
	
	public void sendAllWithPermission(String message, Permissions permission) {
		if(permission.hasPermission(request.getRequestor())) {
			BungeePlugin.sendMessage(request.getRequestor(), message);
		}
		if(permission.hasPermission(handler.getSupporter())) {
			BungeePlugin.sendMessage(handler.getSupporter(), message);
		}
		for(Supporter i : listeners) {
			if(permission.hasPermission(i.getSupporter())) {
				BungeePlugin.sendMessage(i.getSupporter(), message);
			}
		}
	}
	
}
