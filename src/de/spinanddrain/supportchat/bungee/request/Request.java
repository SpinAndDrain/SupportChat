package de.spinanddrain.supportchat.bungee.request;

import de.spinanddrain.supportchat.spigot.request.RequestState;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Request {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private ProxiedPlayer requestor;
	private String reason;
	private RequestState state;
	
	public Request(ProxiedPlayer requestor, String reason) {
		this.requestor = requestor;
		this.reason = reason;
		this.state = RequestState.OPEN;
	}
	
	public ProxiedPlayer getRequestor() {
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
