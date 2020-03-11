package de.spinanddrain.supportchat.external.lscript;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.spinanddrain.supportchat.external.lscript.exception.FileNotSupportedException;

public class LScriptWriter {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private String type;
	private File file;
	private List<LScriptEntry> vars;
	private List<LScriptEntry> trans;
	private List<String> comments;
	
	public LScriptWriter(File file, String type) {
		this.type = type;
		this.file = file;
		this.vars = new ArrayList<>();
		this.trans = new ArrayList<>();
		this.comments = new ArrayList<>();
	}
	
	public LScriptWriter addVariable(String name, String value) {
		vars.add(new LScriptEntry(name, value));
		return this;
	}
	
	public LScriptWriter addTranslation(String key, String value) {
		trans.add(new LScriptEntry(key, value));
		return this;
	}
	
	public LScriptWriter addComment(String comment) {
		comments.add(comment);
		return this;
	}
	
	public void write() throws FileNotSupportedException, IOException {
		if(!file.getName().endsWith(".lang")) {
			throw new FileNotSupportedException("File has invalid extension");
		}
		if(!file.canWrite()) {
			throw new FileNotSupportedException("File is not writeable");
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("type="+type+nl());
		for(String c : comments) {
			bw.write("# " + c+nl());
		}
		for(LScriptEntry v : vars) {
			bw.write(v.getKey()+"="+v.getValue()+nl());
		}
		bw.write("{"+nl());
		for(LScriptEntry t : trans) {
			bw.write(t.getKey()+"="+t.getValue()+nl());
		}
		bw.write("}");
		bw.close();
	}
	
	private String nl() {
		return System.getProperty("line.separator");
	}
	
}
