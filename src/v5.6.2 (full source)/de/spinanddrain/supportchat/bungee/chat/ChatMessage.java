package de.spinanddrain.supportchat.bungee.chat;

import de.spinanddrain.supportchat.bungee.BungeePlugin;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatMessage implements ChatDisplayResult {

	/*
	 * Created by SpinAndDrain on 13.09.2019
	 */
	
	private TextComponent comp;
	
	public ChatMessage(String string, boolean color) {
		if(color) {
			this.comp = BungeePlugin.toColoredText(string);
		} else {
			this.comp = new TextComponent(string);
		}
	}
	
	@Override
	public TextComponent getComponent() {
		return comp;
	}
	
}
