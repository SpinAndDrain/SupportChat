package de.spinanddrain.supportchat.external;

import java.util.ArrayList;
import java.util.List;

public final class Time {

	/*
	 * Created by SpinAndDrain on 19.09.2019
	 */

	public enum TimeFormat {
		
		MILLISECONDS("ms",0),
		SECONDS("s",1),
		MINUTES("m",2),
		HOURS("h",3),
		DAYS("d",4),
		WEEKS("w",5),
		MONTHS("mo",6),
		YEARS("y",7);
		
		String string;
		int integer;
		
		TimeFormat(String string, int integer) {
			this.string = string;
			this.integer = integer;
		}
		
		public String getContraction() {
			return string;
		}
		
		public static TimeFormat fromString(String string) {
			try {
				return TimeFormat.valueOf(string);
			} catch(Exception e) {
				return null;
			}
		}
		
		public static TimeFormat fromRoutine(int routine) {
			for(TimeFormat i : values()) {
				if(i.integer == routine) {
					return i;
				}
			}
			return null;
		}
		
		public static TimeFormat fromContraction(String string) {
			for(TimeFormat i : values()) {
				if(i.getContraction().equals(string)) {
					return i;
				}
			}
			return null;
		}
		
	}
	
	private TimeFormat f;
	private int a;
	
	public Time(TimeFormat format, int amount) {
		this.f = format;
		this.a = amount;
	}
	
	public TimeFormat getFormat() {
		return f;
	}
	
	public int getAmount() {
		return a;
	}
	
	public void changeFormat(TimeFormat newFormat, int newAmount) {
		this.f = newFormat;
		this.a = newAmount;
	}
	
	public int routine() {
		return f.integer;
	}
	
	public static int routineOf(TimeFormat format) {
		return format.integer;
	}
	
	public boolean higherRoutine(TimeFormat format) {
		return (f.integer > format.integer);
	}
	
	public boolean lowerRoutine(TimeFormat format) {
		return (f.integer < format.integer);
	}
	
	public static Time fromString(String string) {
		char[] base = string.toCharArray();
		String res = new String(), cont = new String();
		for (int i = 0; i < base.length; i++) {
			final StringExtensions ext = new StringExtensions(String.valueOf(base[i]));
			if(ext.isAlphabetic()) {
				cont += base[i];
			} else if(ext.isNumeric()) {
				res += base[i];
			}
		}
		return new Time(TimeFormat.fromContraction(cont), Integer.parseInt(res));
	}
	
	public static Time[] allFromString(String string) {
		final StringExtensions ext = new StringExtensions(string);
		if(!ext.hasAnySpecial()) {
			List<Time> times = new ArrayList<>();
			String[] args = ext.splitAtTypeChange();
			for(int i = 0, c = 0; i < args.length; i++) {
				if(c == 1) {
					times.add(new Time(TimeFormat.fromContraction(args[i]), Integer.parseInt(args[i-1])));
					c = 0;
				} else {
					c++;
				}
			}
			return times.toArray(new Time[times.size()]);
		}
		return null;
	}
	
	public long toMilliseconds() {
		switch(this.f) {
		case MILLISECONDS:
			return (long) a;
		case SECONDS:
			return (long) a * 1000;
		case MINUTES:
			return (long) a * 1000 * 60;
		case HOURS:
			return (long) a * 1000 * 60 * 60;
		case DAYS:
			return (long) a * 1000 * 60 * 60 * 24;
		case WEEKS:
			return (long) a * 1000 * 60 * 60 * 24 * 7;
		case MONTHS:
			return (long) a * 1000 * 60 * 60 * 24 * 7 * 4;
		case YEARS:
			return (long) a * 1000 * 60 * 60 * 24 * 7 * 4 * 12;
		default:
			return 0;
		}
	}
	
	public long toSeconds() {
		return (long) toMilliseconds() / 1000;
	}
	
	public long toMinutes() {
		return (long) toMilliseconds() / 1000 / 60;
	}
	
	public long toHours() {
		return (long) toMilliseconds() / 1000 / 60 / 60;
	}
	
	public long toDays() {
		return (long) toMilliseconds() / 1000 / 60 / 60 / 24;
	}
	
	public long toWeeks() {
		return (long) toMilliseconds() / 1000 / 60 / 60 / 24 / 7;
	}

	public long toMonths() {
		return (long) toMilliseconds() / 1000 / 60 / 60 / 24 / 7 / 4;
	}
	
	public long toYears() {
		return (long) toMilliseconds() / 1000 / 60 / 60 / 24 / 7 / 4 / 12;
	}
	
	public double toExactSeconds() {
		return (double) toMilliseconds() / 1000;
	}
	
	public double toExactMinutes() {
		return (double) toMilliseconds() / 1000 / 60;
	}
	
	public double toExactHours() {
		return (double) toMilliseconds() / 1000 / 60 / 60;
	}
	
	public double toExactDays() {
		return (double) toMilliseconds() / 1000 / 60 / 60 / 24;
	}
	
	public double toExactWeeks() {
		return (double) toMilliseconds() / 1000 / 60 / 60 / 24 / 7;
	}

	public double toExactMonths() {
		return (double) toMilliseconds() / 1000 / 60 / 60 / 24 / 7 / 4;
	}
	
	public double toExactYears() {
		return (double) toMilliseconds() / 1000 / 60 / 60 / 24 / 7 / 4 / 12;
	}
	
}
