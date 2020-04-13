package de.spinanddrain.supportchat.spigot.conversation;

import java.util.ArrayList;
import java.util.List;

import de.spinanddrain.supportchat.Permissions;
import de.spinanddrain.supportchat.spigot.request.Request;
import de.spinanddrain.supportchat.spigot.supporter.Supporter;

public class Conversation {

	/*
	 * Created by SpinAndDrain on 11.10.2019
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
		request.getRequestor().sendMessage(message);
		handler.getSupporter().sendMessage(message);
		for(Supporter i : listeners) {
			i.getSupporter().sendMessage(message);
		}
	}
	
	public void sendAllWithPermission(String message, Permissions permission) {
		if(permission.hasPermission(request.getRequestor())) {
			request.getRequestor().sendMessage(message);
		}
		if(permission.hasPermission(handler.getSupporter())) {
			handler.getSupporter().sendMessage(message);
		}
		for(Supporter i : listeners) {
			if(permission.hasPermission(i.getSupporter())) {
				i.getSupporter().sendMessage(message);
			}
		}
	}
	
}
