package de.spinanddrain.supportchat.bungee.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClickableChatMessage implements ChatDisplayResult {

	/*
	 * Created by SpinAndDrain on 13.09.2019
	 */

	private TextComponent comp;
	
	public ClickableChatMessage(String string, String onHover, String onAction, Action action) {
		this.comp = new TextComponent(string);
		comp.setClickEvent(new ClickEvent(action, onAction));
		comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(onHover).create()));
	}
	
	@Override
	public TextComponent getComponent() {
		return comp;
	}
	
}
