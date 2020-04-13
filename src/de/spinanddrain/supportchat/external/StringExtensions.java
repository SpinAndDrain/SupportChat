package de.spinanddrain.supportchat.external;

import java.util.ArrayList;
import java.util.List;

public final class StringExtensions {

	/*
	 * Created by SpinAndDrain on 16.09.2019
	 */

	/**
	 * 
	 * @alphabetic -> UTF-8
	 */
	public static final char[] NUMERIC = {'0','1','2','3','4','5','6','7','8','9'},
			ALPHABETIC = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
					'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	private char[] base;
	
	public StringExtensions(String base) {
		this.base = base.toCharArray();
	}
	
	/**
	 * 
	 * @return true if the String contains an numeric value.
	 */
	public boolean hasAnyNumeric() {
		for(int i = 0; i < base.length; i++) {
			for(int j = 0; j < NUMERIC.length; j++) {
				if(base[i] == NUMERIC[j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if the String contains an alphabetic value.
	 */
	public boolean hasAnyAlphabetic() {
		for(int i = 0; i < base.length; i++) {
			for(int j = 0; j < ALPHABETIC.length; j++) {
				if(base[i] == ALPHABETIC[j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if the String contains an non alphabetic/numeric value.
	 */
	public boolean hasAnySpecial() {
		for(int i = 0; i < base.length; i++) {
			final StringExtensions t = new StringExtensions(String.valueOf(base[i]));
			if(t.isSpecial()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if all values of the String are numeric.
	 */
	public boolean isNumeric() {
		return (!hasAnyAlphabetic() && !hasAnySpecial());
	}
	
	/**
	 * 
	 * @return true if all values of the String are alphabetic.
	 */
	public boolean isAlphabetic() {
		return (!hasAnyNumeric() && !hasAnySpecial());
	}
	
	/**
	 * 
	 * @return true if all values of the String are non alphabetic/numeric.
	 */
	public boolean isSpecial() {
		return (!hasAnyAlphabetic() && !hasAnyNumeric());
	}
	
	/**
	 * 
	 * Deletes the character of the @pos position.
	 * @param pos -> position of the char to delete
	 * @return @this
	 */
	public StringExtensions deleteCharacter(int pos) {
		char[] newBase = new char[base.length - 1];
		int dif = 0;
		for(int i = 0; i < base.length; i++) {
			if(i == pos) {
				dif++;
				continue;
			}
			newBase[i - dif] = base[i];
		}
		base = newBase;
		return this;
	}
	
	/**
	 * 
	 * Deletes the first character of the String.
	 * @return @this
	 */
	public StringExtensions deleteFirst() {
		deleteCharacter(0);
		return this;
	}
	
	/**
	 * 
	 * Deletes the last character of the String.
	 * @return @this
	 */
	public StringExtensions deleteLast() {
		deleteCharacter(base.length - 1);
		return this;
	}
	
	/**
	 * 
	 * Deletes the first @amount values of the String.
	 * @param amount -> amount of chars to delete
	 * @return @this
	 */
	public StringExtensions deleteFirst(int amount) {
		for(int i = 0; i < amount; i++) {
			deleteFirst();
		}
		return this;
	}
	
	/**
	 * 
	 * Deletes the last @amount values of the String.
	 * @param amount -> amount of chars to delete
	 * @return @this
	 */
	public StringExtensions deleteLast(int amount) {
		for(int i = 0; i < amount; i++) {
			deleteLast();
		}
		return this;
	}
	
	/**
	 * 
	 * Replaces the specified placeholders.
	 * @param placeholder -> to replace
	 * @return @this
	 */
	public StringExtensions replace(Placeholder placeholder) {
		base = new String(base).replaceAll(placeholder.getName(), String.valueOf(placeholder.getValue())).toCharArray();
		return this;
	}
	
	/**
	 * 
	 * Replaces the specified values.
	 * @param oldValue -> @see Placeholder#getName()
	 * @param newValue -> @see Placeholder#getValue()
	 * @return @this
	 */
	public StringExtensions replace(String oldValue, String newValue) {
		replace(new Placeholder(oldValue, newValue));
		return this;
	}
	
	/**
	 * 
	 * Replaces all the specified placeholders.
	 * @param placeholders -> all to be replaced
	 * @return @this
	 */
	public StringExtensions replaceAll(Placeholder... placeholders) {
		for(Placeholder i : placeholders) {
			replace(i);
		}
		return this;
	}
	
	/**
	 * 
	 * Deletes the specified placeholders from the String.
	 * @param placeholder -> to delete
	 * @return @this
	 */
	public StringExtensions deleteFull(Placeholder placeholder) {
		replace(new Placeholder(placeholder.name, new String()));
		return this;
	}
	
	/**
	 * 
	 * Deletes the specified value from the String.
	 * @param del -> to delete
	 * @return @this
	 */
	public StringExtensions deleteFull(String del) {
		deleteFull(new Placeholder(del));
		return this;
	}
	
	/**
	 * 
	 * Deletes all numeric values of the base.
	 * @return @this
	 */
	public StringExtensions deleteAllNumeric() {
		for(int i = 0; i < base.length; i++) {
			final StringExtensions ext = new StringExtensions(String.valueOf(base[i]));
			if(ext.isNumeric()) {
				deleteFull(String.valueOf(base[i]));
			}
		}
		return this;
	}
	
	/**
	 * 
	 * Deletes all alphabetic values of the base.
	 * @return @this
	 */
	public StringExtensions deleteAllAlphabetic() {
		for(int i = 0; i < base.length; i++) {
			final StringExtensions ext = new StringExtensions(String.valueOf(base[i]));
			if(ext.isAlphabetic()) {
				deleteFull(String.valueOf(base[i]));
			}
		}
		return this;
	}
	
	/**
	 * 
	 * Deletes all special values of the base.
	 * @return @this
	 */
	public StringExtensions deleteAllSpecials() {
		for(int i = 0; i < base.length; i++) {
			final StringExtensions ext = new StringExtensions(String.valueOf(base[i]));
			if(ext.isSpecial()) {
				deleteFull(String.valueOf(base[i]));
			}
		}
		return this;
	}
	
	/**
	 * 
	 * Splits a String on every type change.
	 * @return String[] with the splits
	 */
	public String[] splitAtTypeChange() {
		final byte a = 0, n = 1, s = 2;
		final StringExtensions ex = (base.length > 0 ? new StringExtensions(String.valueOf(base[0])) : null);
		byte cur = (ex != null ? ex.isAlphabetic() ? a : ex.isNumeric() ? n : s : -1);
		String sto = new String();
		List<String> res = new ArrayList<>();
		for(int i = 0; i < base.length; i++) {
			final StringExtensions ext = new StringExtensions(String.valueOf(base[i]));
			if((cur == a && ext.isAlphabetic()) || (cur == n && ext.isNumeric()) || (cur == s && ext.isSpecial())) {
				sto += base[i];
			} else {
				res.add(sto);
				sto = String.valueOf(base[i]);
				cur = (ext.isAlphabetic() ? a : ext.isNumeric() ? n : s);
			}
		}
		res.add(sto);
		return res.toArray(new String[res.size()]);
	}
	
	public String[] splitTypes() {
		// TODO split Types
		return null;
	}
	
	/**
	 * 
	 * Splits the String into every character.
	 * @return String[] @note: Every value of this array got the length *1*
	 */
	public String[] splitAll() {
		String[] res = new String[base.length];
		for(int i = 0; i < res.length; i++) {
			res[i] = String.valueOf(base[i]);
		}
		return res;
	}
	
	/**
	 * 
	 * @return the (modified) String @see @this#Constructor#base
	 */
	public String getBase() {
		return new String(base);
	}
	
	/**
	 * 
	 * Connects all values of @args with the specified @link.
	 * @param args -> arguments to be linked
	 * @param link -> value between every link
	 * @return the linked @String
	 */
	public static String bind(String[] args, String link) {
		String res = new String();
		for(int i = 0; i < args.length; i++) {
			if((i + 1) == args.length) {
				res += args[i];
				continue;
			}
			res += args[i] + link;
		}
		return res;
	}
	
	/**
	 * 
	 * Connects all values of @args with a space.
	 * @param args -> @see @this#bind(String[] args, String link)
	 * @return the linked @String
	 */
	public static String bind(String[] args) {
		return bind(args, " ");
	}
	
	/**
	 * 
	 * Converts the specified @objects to a valid String[].
	 * @param objects -> objects to be converted
	 * @return the converted @String[]
	 */
	public static String[] convert(Object... objects) {
		String[] res = new String[objects.length];
		for(int i = 0; i < res.length; i++) {
			res[i] = String.valueOf(objects[i]);
		}
		return res;
	}
	
	/**
	 * 
	 * *** STOREROOM ***
	 * @author SpinAndDrain
	 */
	public static class Placeholder {
		
		private String name;
		private Object value;
		
		public Placeholder(String name) {
			this.name = name;
		}
		
		public Placeholder(String name, Object replace) {
			this.name = name;
			this.value = replace;
		}
		
		public String getName() {
			return name;
		}
		
		public Object getValue() {
			return value;
		}
		
	}
	
}
