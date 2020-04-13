package de.spinanddrain.supportchat.bungee.chat;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HoverableChatMessage implements ChatDisplayResult {

	/*
	 * Created by SpinAndDrain on 13.09.2019
	 */

	private TextComponent comp;
	
	public HoverableChatMessage(String string, String onHover) {
		this.comp = new TextComponent(string);
		comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(onHover).create()));
	}
	
	@Override
	public TextComponent getComponent() {
		return comp;
	}
	
}
