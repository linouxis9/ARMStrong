package simulator;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

//TODO JAVADOOOC

public class Preprocessor {

	/**
	 * Small Set containing the instructions that takes as input two Registers and
	 * an Operand2.
	 */
	private static final Set<String> RROP2 = new HashSet<>(
			Arrays.asList("adc", "add", "and", "bic", "eor", "sub", "mul", "orr", "sdiv", "udiv" ));

	/**
	 * Small Set containing the instructions that takes as input only one Operand2.
	 */
	private static final Set<String> OOP2 = new HashSet<>(Arrays.asList("swi", "svc"));
	/**
	 * Small Set containing the instructions that takes as input one Register and
	 * one Operand2.
	 */
	private static final Set<String> ROP2 = new HashSet<>(
			Arrays.asList("cmp", "cmn", "tst", "teq", "mov", "mvn"));

	/**
	 * Small Set containing the special instructions that takes as input one
	 * Operand2.
	 */
	private static final Set<String> LSOP2 = new HashSet<>(Arrays.asList("ldr", "str"));

	/**
	 * Small Set containing the branching instructions that takes as input one
	 * Operand2.
	 */
	private static final Set<String> BOP2 = new HashSet<>(Arrays.asList("b", "bl"));

	/**
	 * This static method converts Syntactic Sugar into their correct counterpart.
	 * It then calls Preprocessor.checkInstruction(tokens,0);
	 * Why is that a static method? The simulator package intends to be a library providing an ARM
	 * simulator but also some encapsulatable ARM tools easy to integrate in others java programs
	 * like for instance here, for manipulating a List of lexed Tokens.
	 * The preprocessing is explicitly done before checking the syntax so we can also check the syntax of the expanded form of the Syntactic Sugar.
	 *
	 * @param tokens The Tokenized's representation of an instruction to handle.
	 * @throws InvalidSyntaxException
	 * @throws InvalidOperationException
	 * @throws InvalidRegisterException
	 */
	public static void preprocess(List<Token> tokens) throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {
		Preprocessor.preprocess(tokens, 0);
	}
	
	/**
	 * This static method converts Syntactic Sugar into their correct counterpart.
	 * It then calls Preprocessor.checkInstruction(tokens line);
	 * Why is that a static method? The simulator package intends to be a library providing an ARM
	 * simulator but also some encapsulatable ARM tools easy to integrate in others java programs
	 * like for instance here, for manipulating a List of lexed Tokens.
	 * The preprocessing is explicitly done before checking the syntax so we can also check the syntax of the expanded form of the Syntactic Sugar.
	 *
	 * @param tokens The Tokenized's representation of an instruction to handle.
	 * @param line The line to show in the thrown errors.
	 * @throws InvalidSyntaxException
	 * @throws InvalidOperationException
	 * @throws InvalidRegisterException
	 */
	public static void preprocess(List<Token> tokens, int line)
			throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {

		for (Token token : tokens) {
			if (token.getTokenType() == TokenType.HASHEDASCII) {
				tokens.set(tokens.indexOf(token), new Token(TokenType.HASH, "#" + token.getRawAsciiValue()));
			}
		}

		if (tokens.get(0).getTokenType() == TokenType.DIRECTIVE) {
			switch(tokens.get(0).getRawDirective()) {
			case "breakpoint":
				tokens.clear();
				tokens.add(new Token(TokenType.OPERATION, "swi"));
				tokens.add(new Token(TokenType.HASH, "#80"));
			}
		} else {
			Preprocessor.checkInstruction(tokens, line);
		}
	}


	/**
	 * This static method checks the Instruction's Tokenized representation. 
	 * Why is that a static method? The simulator package intends to be a library providing an ARM
	 * simulator but also some ARM tools easy to integrate in others java programs
	 * like for instance here, for checking the syntax of a List of lexed Tokens.
	 * 
	 * @param tokens The Tokenized's representation of an instruction to handle.
	 * @param line The line to show in the thrown errors.
	 * @throws InvalidSyntaxException
	 * @throws InvalidOperationException
	 * @throws InvalidRegisterException
	 */
	public static void checkInstruction(List<Token> tokens, int line)
			throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {

		try {
			int i = 0;
			if (tokens.get(i).getTokenType() == TokenType.LABEL) {
				i++;
			}
			String op = tokens.get(i).getRawOperation();
			if (tokens.get(i).getTokenType() != TokenType.OPERATION || !(Preprocessor.RROP2.contains(op)
					|| Preprocessor.OOP2.contains(op) || Preprocessor.ROP2.contains(op)
					|| Preprocessor.LSOP2.contains(op) || Preprocessor.BOP2.contains(op))) {
				throw new InvalidOperationException(line, op);
			}
			i++;
			while (tokens.get(i).getTokenType() == TokenType.FLAG) {
				i++;
			}

			if (tokens.get(i).getTokenType() == TokenType.CONDITIONCODE) {
				i++;
			}
			
			for (Token token : tokens) {
				if (token.getTokenType() == TokenType.REGISTER) {
					int registerId = token.getRawRegister();
					if (Preprocessor.checkRegister(registerId)) {
						throw new InvalidRegisterException(line, registerId);
					}

				}
			}

			boolean error = false;

			if (Preprocessor.RROP2.contains(op)) {
				error = Preprocessor.checkRROP2(tokens, i);
			} else if (Preprocessor.OOP2.contains(op)) {
				error = Preprocessor.checkOOP2(tokens, i);
			} else if (Preprocessor.ROP2.contains(op)) {
				error = Preprocessor.checkROP2(tokens, i);
			} else if (Preprocessor.LSOP2.contains(op)) {
				error = Preprocessor.checkLSOP2(tokens, i);
			} else if (Preprocessor.BOP2.contains(op)) {
				error = Preprocessor.checkBOP2(tokens, i);
			}

			if (error) {
				throw new InvalidSyntaxException(line);
			}

		} catch (IndexOutOfBoundsException e) {
			throw new InvalidSyntaxException(line);
		}
	}

	/**
	 * Ensure that a register is comprised between [0;15]
	 * 
	 * @param registerId
	 *            The Register id to test
	 * @return True if the register is invalid, else otherwise.
	 */
	private static boolean checkRegister(int registerId) {
		return registerId > 15 || registerId < 0;
	}

	/**
	 * Ensure the syntax correctness of a RROP2 (Two Registers, One Operand2)
	 * instruction.
	 * 
	 * @param tokens
	 *            The instruction's parsable tokens
	 * @param i
	 *            The index of the first element of the right-hand expression
	 * @return True if the instruction is invalid, else otherwise.
	 */
	private static boolean checkRROP2(List<Token> tokens, int i) {
		return (tokens.get(i).getTokenType() != TokenType.REGISTER) || (tokens.get(i + 1).getTokenType() != TokenType.COMMA)
				|| (tokens.get(i + 2).getTokenType() != TokenType.REGISTER)
				|| (tokens.get(i + 3).getTokenType() != TokenType.COMMA)
				|| (tokens.get(i + 4).getTokenType() != TokenType.REGISTER
						&& tokens.get(i + 4).getTokenType() != TokenType.HASH);
	}

	/**
	 * Ensure the syntax correctness of a OOP2 (Only One Operand2) instruction.
	 * 
	 * @param tokens
	 *            The instruction's parsable tokens
	 * @param i
	 *            The index of the first element of the right-hand expression
	 * @return True if the instruction is invalid, else otherwise.
	 */
	private static boolean checkOOP2(List<Token> tokens, int i) {
		return tokens.get(i).getTokenType() != TokenType.REGISTER && tokens.get(i).getTokenType() != TokenType.HASH;
	}

	/**
	 * Ensure the syntax correctness of a ROP2 (One Register, One Operand2)
	 * instruction.
	 * 
	 * @param tokens
	 *            The instruction's parsable tokens
	 * @param i
	 *            The index of the first element of the right-hand expression
	 * @return True if the instruction is invalid, else otherwise.
	 */
	private static boolean checkROP2(List<Token> tokens, int i) {
		return ((tokens.get(i).getTokenType() != TokenType.REGISTER) || (tokens.get(i + 1).getTokenType() != TokenType.COMMA)
				|| (tokens.get(i + 2).getTokenType() != TokenType.REGISTER
						&& tokens.get(i + 2).getTokenType() != TokenType.HASH));
	}

	/**
	 * Ensure the syntax correctness of a LSOP2 (Special Instruction with One
	 * Operand2) instruction.
	 * 
	 * @param tokens
	 *            The instruction's parsable tokens
	 * @param i
	 *            The index of the first element of the right-hand expression
	 * @return True if the instruction is invalid, else otherwise.
	 */
	private static boolean checkLSOP2(List<Token> tokens, int i) {
		if (tokens.get(i).getTokenType() == TokenType.REGISTER && tokens.get(i + 1).getTokenType() == TokenType.COMMA) {
			Token token = tokens.get(i + 2);
			switch (token.getTokenType()) {
			case OFFSET:
				return Preprocessor.checkRegister(token.getRawOffset());
			default:
				return true;
			case HASH:
			case REGISTER:
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Ensure the syntax correctness of a BOP2 (Branching Instruction with One
	 * Operand2) instruction.
	 * 
	 * @param tokens
	 *            The instruction's parsable tokens
	 * @param i
	 *            The index of the first element of the right-hand expression
	 * @return True if the instruction is invalid, else otherwise.
	 */
	private static boolean checkBOP2(List<Token> tokens, int i) {
		return (tokens.get(i).getTokenType() != TokenType.IDENTIFIER);
	}
}
