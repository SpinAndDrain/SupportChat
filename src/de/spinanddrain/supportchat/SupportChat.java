package de.spinanddrain.supportchat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.spinanddrain.util.advanced.MathUtils;
import de.spinanddrain.util.arrays.ArrayUtils;
import de.spinanddrain.util.arrays.CharArray;
import de.spinanddrain.util.arrays.StringArray;
import de.spinanddrain.util.holding.Memorizer;

public final class SupportChat {
	
	 /*
	  *  CHANGES: TRUE
	  *  - fixed Updater
	  *  -> added LibsCollection
	  *  - update to LScript2
	  *  - update Updater
	  *  - removed external stuff
	  *  
	  *  classchanges: *
	  *  
	  *  (Upcomming: Update to LScript2)
	  */
	
	/*
	 * Created by SpinAndDrain on 11.10.2019
	 */

	public static final String DEPENDENCY_VERSION = "1.2";
	public static final boolean HIGHER = true;
	
	/*
	 * Old: ext.mess.201912192015.state
	 * New: ext.mess.202001162102.state
	 */
	private static final String source = new String("ext.mess.202001162102.state");
	
	private SupportChat() {}

	public static String readExternalMessageRaw() {
		try {
			return new BufferedReader(new InputStreamReader(new URL("http://spinanddrain.bplaced.net/sessions/supportchat/" + source).openStream())).readLine();
		} catch (IOException | NullPointerException e) {
			return new String();
		}
	}
	
	public static long getTime(String s) {
		Memorizer<String> contr = new Memorizer<String>(new String());
		int count = Integer.parseInt(new String(ArrayUtils.convertAndModify(s).eliminate(element -> {
			if(Character.isAlphabetic(element)) {
				contr.set(contr.get() + element);
				return true;
			}
			return false;
		}).toNativeArray()));
		switch(contr.get().toLowerCase()) {
		case "ms":
			return (long) count;
		case "s":
			return (long) count * 1000;
		case "m":
			return (long) count * 60000;
		case "h":
			return (long) count * 3600000;
		case "d":
			return (long) count * 86400000;
		case "w":
			return (long) count * 86400000 * 7;
		case "mo":
			return (long) count * 86400000 * 30;
		case "y":
			return (long) count * 86400000 * 365;
		default:
			return 0L;
		}
	}
	
	public static void moveDirectory(File directory, File destination) throws IOException {
		copyDirectory(directory, destination);
		deleteDirectory(directory);
	}
	
	public static void deleteDirectory(File directory) {
		if(directory.isDirectory()) {
			for(File files : directory.listFiles()) {
				deleteDirectory(files);
				directory.delete();
			}
		} else
			directory.delete();
	}
	
	public static void copyDirectory(File sourceFolder, File destinationFolder) throws IOException {
		if (sourceFolder.isDirectory()) {
			if (!destinationFolder.exists())
				destinationFolder.mkdir();
			String files[] = sourceFolder.list();
			for (String file : files) {
				File srcFile = new File(sourceFolder, file);
				File destFile = new File(destinationFolder, file);
				copyDirectory(srcFile, destFile);
			}
		} else
			Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public static String convertLogical(long millis) {
		if(millis < 1000) {
			return millis + "ms";
		}
		long sec = millis / 1000;
		if(sec < 60) {
			return sec + "s";
		}
		long min = sec / 60;
		if(min < 60) {
			return (min + 1) + "m";
		}
		long h = min / 60, restMin = min % 60;
		return h + "h" + (restMin == 0 ? "" : " " + restMin + "m");
	}
	
	public static String[] getTextField(String header, String... lines) {
		StringArray array = new StringArray(lines);
		Memorizer<Integer> length = new Memorizer<Integer>(0);
		array.forEach(a -> {
			if(actualLength(a) > length.get())
				length.set(actualLength(a));
		});
		array.modifyEach(e -> {
			if(actualLength(e) == length.get())
				return "| " + e + " |";
			else
				return "| " + e + multiChar(length.get() - actualLength(e), ' ') + " |";
		});
		int cu = (length.get() + 2 - actualLength(header)) / 2;
		String add = MathUtils.isOdd(length.get() + 2 - actualLength(header)) ? "=" : "";
		array.unshift("+" + multiChar(cu, '=') + header + multiChar(cu, '=') + add + "+");
		array.add("+" + multiChar(length.get() + 2, '=') + "+");
		return array.toArray();
	}
	
	private static int actualLength(String s) {
		boolean wait = false;
		int x = 0;
		for(char c : s.toCharArray()) {
			if(wait) {
				wait = false;
			} else if(c == '§') {
				wait = true;
			} else
				x++;
		}
		return x;
	}
	
	private static String multiChar(int amount, char c) {
		return new String(new CharArray().clear(amount).modifyEach(e -> c).toNativeArray());
	}
	
}
