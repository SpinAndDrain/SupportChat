package de.spinanddrain.supportchat.bungee.chat;

import net.md_5.bungee.api.chat.TextComponent;

public class EmptyLine implements ChatDisplayResult {

	/*
	 * Created by SpinAndDrain on 13.09.2019
	 */

	private boolean bool;
	
	public EmptyLine(boolean bool) {
		this.bool = bool;
	}
	
	@Override
	public TextComponent getComponent() {
		return (!bool ? new TextComponent(new TextComponent(" \n"), new TextComponent(" \n")) : new TextComponent(new TextComponent(" \n")));
	}
	
}
