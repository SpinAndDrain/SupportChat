package de.spinanddrain.supportchat.bungee.chat;

import net.md_5.bungee.api.chat.TextComponent;

public class MessageHandler {

	/*
	 * Created by SpinAndDrain on 13.09.2019
	 */

	private TextComponent[] components;
 	
	public MessageHandler(ChatMessage... chatMessages) {
		components = new TextComponent[chatMessages.length];
		for(int i = 0; i < chatMessages.length; i++) {
			components[i] = chatMessages[i].getComponent();
		}
	}
	
	public MessageHandler(ClickableChatMessage... clickableChatMessages) {
		components = new TextComponent[clickableChatMessages.length];
		for(int i = 0; i < clickableChatMessages.length; i++) {
			components[i] = clickableChatMessages[i].getComponent();
		}
	}
	
	public MessageHandler(ChatDisplayResult... results) {
		components = new TextComponent[results.length];
		for(int i = 0; i < results.length; i++) {
			components[i] = results[i].getComponent();
		}
	}
	
	public TextComponent[] getFinalMessage() {
		return components;
	}
	
}
