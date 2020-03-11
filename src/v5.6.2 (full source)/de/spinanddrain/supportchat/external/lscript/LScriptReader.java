package de.spinanddrain.supportchat.external.lscript;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.spinanddrain.supportchat.external.lscript.exception.FileNotSupportedException;
import de.spinanddrain.supportchat.external.lscript.exception.ScriptSyntaxException;

public class LScriptReader {

	/*
	 * Created by SpinAndDrain on 19.12.2019
	 */

	private final File base;
	
	public LScriptReader(final File base) {
		this.base = base;
	}
	
	public LScriptEntry[] read() throws FileNotSupportedException, FileNotFoundException, ScriptSyntaxException {
		if(!base.getName().endsWith(".lang")) {
			throw new FileNotSupportedException("File has invalid extension");
		}
		if(!base.canRead()) {
			throw new FileNotSupportedException("File is not readable");
		}
		List<LScriptEntry> t = new ArrayList<>();
		Scanner s = new Scanner(base);
		boolean scriptStarted = false;
		int line = 0;
		int var = 0;
		while(s.hasNextLine()) {
			line++;
			String next = s.nextLine();
			if(line == 1) {
				if(next.startsWith("type=")) {
					t.add(new LScriptEntry("TYPE", next.split("=")[1]));
				} else {
					s.close();
					throw new ScriptSyntaxException("[Line:1] Script always starts with 'type='!");
				}
			}
			if(isComment(next)) continue;
			if(checkFor(next, "{")) {
				if(scriptStarted) {
					s.close();
					throw new ScriptSyntaxException("[Line:" + line + "] Script already initialized!");
				}
				scriptStarted = true;
				continue;
			}
			if(checkFor(next, "}")) {
				if(scriptStarted)
					break;
				else {
					s.close();
					throw new ScriptSyntaxException("[Line:" + line + "] Script not initialized!");
				}
			}
			if(next.contains("=")) {
				if(next.split("=").length > 2) {
					s.close();
					throw new ScriptSyntaxException("[Line:" + line + "] Character '=' not allowed!");
				}
				if(!scriptStarted && !next.contains("~/")) {
					String varName = next.split("=")[0];
					String varValue = next.split("=")[1];
					t.add(new LScriptEntry("VAR" + var, varName + "~/" + varValue));
				} else {
					t.add(new LScriptEntry(next.split("=")[0], next.split("=")[1]));
				}
			} else {
				s.close();
				throw new ScriptSyntaxException("[Line:" + line + "] Invalid Syntax '" + next + "'");
			}
		}
		s.close();
		return t.toArray(new LScriptEntry[t.size()]);
	}
	
	private boolean checkFor(String s, String z) {
		return(s.equals(z) || containsComment(s, z));
	}
	
	private boolean containsComment(String s, String z) {
		return (!isComment(s) && (s.startsWith(z) && s.contains("#")));
	}
	
	private boolean isComment(String s) {
		return (s.startsWith("#"));
	}
	
}
