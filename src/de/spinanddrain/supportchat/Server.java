package de.spinanddrain.supportchat;

import java.util.List;

public interface Server {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	public List<?> getRequests();
	
	public List<?> getVisibleRequests();
	
	public List<?> getOnlineSupporters();
	
	public List<?> getOnlineVisibleSupporters();
	
	public List<?> getConversations();
	
	public List<?> getRunningConversations();
	
	public void registerCommand(Object command);
	
	public String getLanguage();
	
	public String getPluginVersion();
	
}
