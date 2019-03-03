package projetarm_v2.simulator.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import projetarm_v2.simulator.core.routines.CpuBreakpoint;

public class Preprocessor {

	private static final Pattern labelPattern = Pattern.compile("(([a-zA-Z]|[0-9])*):");

	
	public static String pass1(String assembly) {
		assembly = assembly
				.replaceAll("\r?\n", ";")
				.replaceAll(".breakpoint", "blx #" + CpuBreakpoint.ROUTINE_ADDRESS)
				.replaceAll("@.*$", "");
		Matcher matcher = labelPattern.matcher(assembly);
		
		while (matcher.find()) {
			assembly = assembly.replaceAll(matcher.group(1), escapeDigit(matcher.group(1)));
		}
		
		return assembly;
	}


	private static String escapeDigit(String group) {
		StringBuilder builder = new StringBuilder();
		
		for (char digit : group.toCharArray()) {
			switch(digit) {
				default: builder.append(digit); break;
				case '0': builder.append("Z"); break;
				case '1': builder.append("O"); break;
				case '2': builder.append("T"); break;
				case '3': builder.append("Th"); break;
				case '4': builder.append("F"); break;
				case '5': builder.append("Fi"); break;
				case '6': builder.append("S"); break;
				case '7': builder.append("Se"); break;
				case '8': builder.append("E"); break;
				case '9': builder.append("N"); break;
			}
		}
		
		return builder.toString();
	}
}
