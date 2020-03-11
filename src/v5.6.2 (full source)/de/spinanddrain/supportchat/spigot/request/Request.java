package de.spinanddrain.supportchat.spigot.request;

import org.bukkit.entity.Player;

public class Request {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	private Player requestor;
	private String reason;
	private RequestState state;
	
	public Request(Player requestor, String reason) {
		this.requestor = requestor;
		this.reason = reason;
		this.state = RequestState.OPEN;
	}
	
	public Player getRequestor() {
		return requestor;
	}
	
	public RequestState getState() {
		return state;
	}
	
	public void setState(RequestState state) {
		this.state = state;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
	
}
