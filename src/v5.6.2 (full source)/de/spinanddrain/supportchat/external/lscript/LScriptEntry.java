package de.spinanddrain.supportchat.external.lscript;

public class LScriptEntry {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private String key;
	private String value;
	
	protected LScriptEntry(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
}
