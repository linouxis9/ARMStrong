/**
 * Copyright (c) 2018 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simulator.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interpretor converts a Tokenized' representation of a program into
 * ready-to-be executed Instruction by the Cpu.
 */
public class Program implements Iterator<List<Token>> {

	/**
	 * The compiled representation of the regular expressions used to match the ARM
	 * instructions. Instead of having to make a Pattern each time the
	 * Program.lexer(String line) method is called, We use lazy initialization to
	 * initialize our Pattern object once and then make it available to every
	 * instance of Program. It is important to note that a Pattern is
	 * computationally expensive to compile.
	 */
	private static Pattern pattern;

	/**
	 * Scanner allows us to iter over our assembly software.
	 */
	private Scanner scanner;

	/**
	 * Create an empty new program.
	 */
	public Program() {
		this("");
	}

	/**
	 * Create a new program with the specified assembly.
	 * 
	 * @param assembly
	 *            The assembly program to store into the class.
	 */
	public Program(String assembly) {
		this.setNewProgram(assembly);
	}

	/**
	 * Update the program contained in the object.
	 * 
	 * @param assembly
	 *            The assembly program to store into the class.
	 */
	public void setNewProgram(String assembly) {
		this.scanner = new Scanner(assembly);
	}

	/**
	 * Returns if we still have assembly Strings to lex.
	 * 
	 * @return If we still have assembly Strings to lex
	 */
	public boolean hasNext() {
		return scanner.hasNextLine();
	}

	/**
	 * Turns the next ARM Instruction into an easily parsable List of Token.
	 * 
	 * @return The tokenized's representation of an assembly line.
	 */
	public List<Token> next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		String line = scanner.nextLine();
		return Program.lexer(line);
	}

	/**
	 * This is our awesome small but efficient Lexical Analyzer. It turns a String
	 * containing an ARM Instruction in assembly into an easily parsable List of
	 * Token. Why is that a static method? The simulator package intends to be a
	 * library providing an ARM simulator but also some encapsulatable ARM tools
	 * easy to integrate in others java programs like for instance here, for lexing
	 * a line of ARM Assembly.
	 * 
	 * @param line
	 *            A String containing an ARM Instruction in assembly
	 * @return an easily parsable List of Token.
	 */
	public static List<Token> lexer(String line) {
		List<Token> list = new ArrayList<>();

		Matcher matcher = Program.getPattern().matcher(line);

		while (matcher.find()) {
			for (TokenType tokenType : TokenType.values()) {
				if (matcher.group(tokenType.name()) != null) {
					list.add(new Token(tokenType, matcher.group(tokenType.name())));
				}
			}

		}
		return list;
	}

	/**
	 * Instead of having to make a Pattern each time the Program.lexer(String line)
	 * method is called, We use lazy initialization to initialize our Pattern object
	 * once and then make it available to every instance of Program. It is important
	 * to note that a Pattern is computationally expensive to compile.
	 * 
	 * @return A Pattern able to match ARM instructions
	 */
	private static synchronized Pattern getPattern() {
		if (pattern == null) {
			String myFuturePattern = "";

			for (TokenType tokenType : TokenType.values()) {
				myFuturePattern = myFuturePattern + "(?<" + tokenType.name() + ">" + tokenType.regexp + ")|";
			}
			pattern = Pattern.compile(myFuturePattern);
		}
		return pattern;
	}
}
