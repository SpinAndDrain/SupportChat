package de.spinanddrain.supportchat.bungee.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.spinanddrain.lscript.exception.FileNotSupportedException;
import de.spinanddrain.lscript.exception.ScriptException;
import de.spinanddrain.lscript.resources.Variable;
import de.spinanddrain.lscript.tools.LParser;
import de.spinanddrain.lscript.tools.LReader;
import de.spinanddrain.lscript.tools.LReader.ScriptType;
import de.spinanddrain.lscript.tools.LWriter;
import de.spinanddrain.lscript.tools.LWriter.OverridingMethod;
import de.spinanddrain.supportchat.bungee.BungeePlugin;
import de.spinanddrain.supportchat.bungee.ConfigAdapter;
import net.md_5.bungee.config.Configuration;

public class Messages {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */
	
	private ConfigAdapter adapter;
	private LParser parser;
	
	public Messages() {
		File parent = new File("plugins/SupportChat/messages");
		if(!parent.exists()) {
			parent.mkdirs();
		}
		adapter = new ConfigAdapter(new File("plugins/SupportChat/messages/messages.yml")) {
			@Override
			public void copyDefaults(Configuration cfg) {
				addDefault("language-file", "en.lang");
			}
		};
		createFiles("en.lang", new File("plugins/SupportChat/messages/en.lang"), ScriptType.TRANSLATION);
		createFiles("de.lang", new File("plugins/SupportChat/messages/de.lang"), ScriptType.TRANSLATION);
		reInitParser();
	}
	
	public void reInitParser() {
		try {
			parser = new LReader(new File("plugins/SupportChat/messages/" + adapter.cfg.getString("language-file")))
					.readAndParse(ScriptType.TRANSLATION);
		} catch (IOException | FileNotSupportedException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigAdapter getAdapter() {
		return adapter;
	}

	public LParser getParser() {
		return parser;
	}
	
	private void createFiles(String resource, File out, ScriptType type) {
		try {
			if(!out.getParentFile().exists())
				out.getParentFile().mkdirs();
			if(!out.exists())
				out.createNewFile();
			InputStream is = BungeePlugin.provide().getResourceAsStream("resources/bungee/" + resource);
			File f = new File(resource);
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			fos.write(buffer);
			fos.close();
			LParser p = new LReader(f).readAndParse(type);
			LWriter w = new LWriter(out, p.getVersionType(), p.getPattern());
			for(Variable v : p.getVariables())
				w.addVariable(v.getName(), v.getValue());
			w.addTranslation(p.getContent());
			w.write(type, OverridingMethod.UNEXISTING, true);
			f.delete();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
