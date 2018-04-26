package simulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program implements Iterator<List<Token>> {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private Scanner scanner;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Program(String assembly) {
		this.scanner = new Scanner(assembly);
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public boolean hasNext() {
		return scanner.hasNextLine();
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public List<Token> next() {
		if (!this.hasNext()) {
			return null;
		}
		String line = scanner.nextLine();
		return Program.lexer(line);
	}

	public static List<Token> lexer(String line) {
		ArrayList<Token> list = new ArrayList<Token>();

		String myFuturePattern = "";

		for (TokenType tokenType : TokenType.values()) {
			myFuturePattern = myFuturePattern + "(?<" + tokenType.name() + ">" + tokenType.regexp + ")|";
		}

		Pattern patterns = Pattern.compile(myFuturePattern);

		Matcher matcher = patterns.matcher(line);

		while (matcher.find()) {
			for (TokenType tokenType : TokenType.values()) {
				if (matcher.group(TokenType.WHITESPACE.name()) != null) {
					continue;
				}
				if (matcher.group(tokenType.name()) != null) {
					list.add(new Token(tokenType, matcher.group(tokenType.name())));
				}
			}

		}
		return list;
	}
}
