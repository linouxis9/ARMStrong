/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import projetarm_v2.simulator.core.routines.CpuBreakpoint;
import projetarm_v2.simulator.core.routines.CpuConsoleClear;

public class Preprocessor {

	private static final String startOfLine = "(?m)^\\s*";
	public static final Pattern labelPattern = Pattern.compile(startOfLine + "(([a-zA-Z]|[0-9])*):");
	private static final Pattern equPattern = Pattern.compile(startOfLine + "\\.equ +(.*),(.*)");
	
	public static String pass1(String assembly) {
		Matcher matcher = equPattern.matcher(assembly);
		
		while (matcher.find()) {
			assembly = assembly.replaceAll("#"+ matcher.group(1), "#"+matcher.group(2));
			assembly = assembly.replaceAll("="+ matcher.group(1), "="+matcher.group(2));
		}

		matcher = labelPattern.matcher(assembly);

		while (matcher.find()) {
			assembly = assembly.replaceAll(startOfLine + matcher.group(1) + ":", escapeDigit(matcher.group(1)) + ":\n");
			assembly = assembly.replaceAll("=" + matcher.group(1), "=" + escapeDigit(matcher.group(1)));
		}

		assembly = assembly
				.replaceAll(startOfLine + "\\.breakpoint", "blx #" + CpuBreakpoint.ROUTINE_ADDRESS)
				.replaceAll(startOfLine + "\\.clear", "blx #" + CpuConsoleClear.ROUTINE_ADDRESS)
				.replaceAll(startOfLine + "\\.stop", ".word 0")
				.replaceAll(startOfLine + "\\.end", ".word 0")
				.replaceAll(startOfLine+"\\.equ +.*", "")
				.replaceAll("@.*", "");

		return assembly.replaceAll("\\R",";");
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
