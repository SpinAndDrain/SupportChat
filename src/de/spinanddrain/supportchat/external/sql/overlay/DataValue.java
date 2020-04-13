package de.spinanddrain.supportchat.external.sql.overlay;

public class DataValue {

	/*
	 * Created by SpinAndDrain on 15.09.2019
	 */

	/**
	 *  *** STOREROOM ***
	 */

	private String key;
	private Object value;
	
	public DataValue(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
	
}
