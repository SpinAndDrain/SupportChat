package de.spinanddrain.supportchat;

import java.lang.reflect.InvocationTargetException;

public enum Permissions {

	NONE("null"),
	SUPPORT("sc.requests"),
	SEND_REQUEST("sc.support"),
	END("sc.end"),
	LOGIN("sc.login"),
	LOGIN_HIDDEN("sc.login.hidden"),
	LOGLIST("sc.loglist"),
	LISTEN("sc.listen"),
	LISTEN_NOTIFY("sc.listen.notify"),
	RELOAD("sc.reload");
	
	private final String permission;
	
	private Permissions(final String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public boolean hasPermission(Object sender) {
		return hasPermission(sender, this);
	}
	
	public static boolean hasPermission(Object sender, Permissions permission) {
		try {
			return (boolean) sender.getClass().getMethod("hasPermission", String.class).invoke(sender, permission.getPermission());
		} catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
