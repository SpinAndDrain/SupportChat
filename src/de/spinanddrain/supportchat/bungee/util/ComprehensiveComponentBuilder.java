package de.spinanddrain.supportchat.bungee.util;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComprehensiveComponentBuilder {

	private final List<BaseComponent> parts;
	
	public ComprehensiveComponentBuilder() {
		this.parts = new ArrayList<BaseComponent>();
	}
	
	public void append(BaseComponent part) {
		parts.add(part);
	}
	
	public void append(String text) {
		parts.add(new TextComponent(text));
	}
	
	public BaseComponent[] create() {
		return parts.toArray(new BaseComponent[parts.size()]);
	}

}
