package de.spinanddrain.supportchat.bungee.chat;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Chat {

	/*
	 * Created by SpinAndDrain on 13.09.2019
	 */

	private ProxiedPlayer pp;
	
	private Chat(ProxiedPlayer pp) {
		this.pp = pp;
	}
	
	public static Chat of(ProxiedPlayer player) {
		return new Chat(player);
	}
	
	public void sendPluginMessage(MessageHandler handler) {
		ComponentBuilder builder = new ComponentBuilder(new TextComponent());
		TextComponent[] f = handler.getFinalMessage();
		for(int i = 0; i < f.length; i++) {
			builder.append(f[i]);
			if((i + 1) != f.length) {
				builder.append(" ");
			}
		}
		pp.sendMessage(builder.create());
	}
	
	public void sendRelatedPluginMessage(MessageHandler handler) {
		ComponentBuilder tc = new ComponentBuilder(new TextComponent());
		TextComponent[] fin = handler.getFinalMessage();
		for(int i = 0; i < fin.length; i++) {
			tc.append(fin[i]);
			if((i + 1) != fin.length) {
				tc.append(" §7* ");
			}
		}
		pp.sendMessage(tc.create());
	}
	
	public void clean() {
		for(int i = 0; i < 105; i++) {
			pp.sendMessage(new EmptyLine(false).getComponent());
		}
	}
	
}
