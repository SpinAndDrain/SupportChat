package de.spinanddrain.supportchat.spigot.request;

import org.bukkit.entity.Player;

public class Request {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	private Player requestor;
	private String reason;
	private RequestState state;
	private long requestTime;
	
	public Request(Player requestor, String reason) {
		this(requestor, reason, System.currentTimeMillis());
	}
	
	public Request(Player requestor, String reason, long requestTime) {
		this.requestor = requestor;
		this.reason = reason;
		this.state = RequestState.OPEN;
		this.requestTime = requestTime;
	}
	
	public Player getRequestor() {
		return requestor;
	}
	
	public RequestState getState() {
		return state;
	}
	
	public void setState(RequestState state) {
		if(state == RequestState.OPEN)
			this.requestTime = System.currentTimeMillis();
		this.state = state;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
	
	public long getRequestTime() {
		return requestTime;
	}
	
}
