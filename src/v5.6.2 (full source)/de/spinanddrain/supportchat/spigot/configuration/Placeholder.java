package de.spinanddrain.supportchat.spigot.configuration;

public class Placeholder {

	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	protected String placeholder;
	protected String replacement;
	
	private Placeholder(String placeholder, String replacement) {
		this.placeholder = placeholder;
		this.replacement = replacement;
	}
	
	public static Placeholder create(String placeholder, String replacement) {
		return new Placeholder(placeholder, replacement);
	}
	
}
