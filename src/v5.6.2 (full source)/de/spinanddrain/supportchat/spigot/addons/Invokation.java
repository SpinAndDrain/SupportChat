package de.spinanddrain.supportchat.spigot.addons;

import java.lang.reflect.Constructor;

public class Invokation {

	/*
	 * Created by SpinAndDrain on 02.09.2019
	 */

	private Class<?> clazz;
	private Constructor<?> constructor;
	
	public Invokation(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Invokation(Class<?> clazz, Constructor<?> constructor) {
		this.clazz = clazz;
		this.constructor = constructor;
	}
	
	public boolean hasNonDefaultConstructor() {
		if(this.constructor != null) {
			return true;
		}
		return false;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public Constructor<?> getConstructor() {
		return constructor;
	}
	
}
