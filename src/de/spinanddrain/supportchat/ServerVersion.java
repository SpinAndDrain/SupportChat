package de.spinanddrain.supportchat;

public enum ServerVersion {

	v1_8("1.8"),
	v1_9("1.9"),
	v1_10("1.10"),
	v1_11("1.11"),
	v1_12("1.12"),
	v1_13("1.13"),
	v1_14("1.14"),
	v1_15("1.15"),
	UNSUPPORTED_TERMINAL("Unsupported Terminal");
	
	String s;
	
	ServerVersion(String s) {
		this.s = s;
	}
	
	public String convertFormat() {
		return s;
	}
	
}
