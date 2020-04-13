package de.spinanddrain.supportchat.external.lscript;

import java.util.ArrayList;
import java.util.List;

public class LScriptParser {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private LScriptEntry[] entries;
	
	public LScriptParser(LScriptEntry... entries) {
		this.entries = replaceVariables(entries);
	}
	
	public String getVersionType() {
		for(int i = 0; i < entries.length; i++) {
			if(entries[i].getKey().equals("TYPE")) {
				return entries[i].getValue();
			}
		}
		return null;
	}
	
	public LScriptEntry[] getEntries() {
		return entries;
	}
	
	public String getByKey(String key) {
		for(int i = 0; i < entries.length; i++) {
			if(entries[i].getKey().equals(key)) {
				return entries[i].getValue();
			}
		}
		return null;
	}
	
	private LScriptEntry[] replaceVariables(LScriptEntry[] entries) {
		List<LScriptEntry> t = new ArrayList<>();
		List<LScriptEntry> v = new ArrayList<>();
		for(int i = 0; i < entries.length; i++) {
			if(entries[i].getKey().startsWith("VAR")) {
				String varName = entries[i].getValue().split("~/")[0];
				String varValue = entries[i].getValue().split("~/")[1];
				v.add(new LScriptEntry(varName, varValue));
				continue;
			}
			if(entries[i].getValue().contains("%")) {
				String key = entries[i].getKey();
				String value = entries[i].getValue();
				for(LScriptEntry vars : v) {
					if(value.contains("%" + vars.getKey() + "%")) {
						value = value.replace("%" + vars.getKey() + "%", vars.getValue());
					}
				}
				t.add(new LScriptEntry(key, value));
			} else
				t.add(entries[i]);
		}
		return t.toArray(new LScriptEntry[t.size()]);
	}
	
}
