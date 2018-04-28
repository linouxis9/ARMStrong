package simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class SyntaxChecker {

	private static List<String> listOp;

	private static final Set<String> RROP2 = new HashSet<String>(
			Arrays.asList(new String[] { "ADC", "ADD", "AND", "BIC", "EOR", "SUB", "MUL", "ORR", "SDIV", "UDIV" }));

	private static final Set<String> OOP2 = new HashSet<String>(
			Arrays.asList(new String[] { "B", "BL", "SWI", "SVC" }));

	private static final Set<String> ROP2 = new HashSet<String>(
			Arrays.asList(new String[] { "CMP", "CMN", "TST", "TEQ", "MOV", "MVN" }));

	private static final Set<String> SPO2 = new HashSet<String>(Arrays.asList(new String[] { "LDR", "STR" }));

	// TODO write javadoc comment
	/**
	 * 
	 */
	public static void checkSyntax(List<Token> tokens, int line)
			throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {
		if (SyntaxChecker.listOp == null) {
			listOp = new ArrayList<String>();
			for (Operation op : Operation.values()) {
				listOp.add(op.name());
			}
		}
		try {
			String op = tokens.get(0).getValue().toUpperCase();

			if (!SyntaxChecker.listOp.contains(op)) {
				throw new InvalidOperationException(line, op);
			}
			int i = 1;
			if (tokens.get(i).getToken() == TokenType.FLAG) {
				i++;
			}
			if (tokens.get(i).getToken() == TokenType.CONDITIONCODE) {
				i++;
			}
			
			for (Token token : tokens) {
				if (token.getToken() == TokenType.REGISTER) {
					int register = Integer.parseInt(token.toInner());
					if (SyntaxChecker.checkRegister(register)) {
						throw new InvalidRegisterException(line, register);
					}

				}
			}
			boolean error = false;

			if (SyntaxChecker.RROP2.contains(op)) {
				error = SyntaxChecker.checkRROP2(tokens, op, i);
			} else if (SyntaxChecker.OOP2.contains(op)) {
				error = SyntaxChecker.checkOOP2(tokens, op, i);
			} else if (SyntaxChecker.ROP2.contains(op)) {
				error = SyntaxChecker.checkROP2(tokens, op, i);
			} else if (SyntaxChecker.SPO2.contains(op)) {
				error = SyntaxChecker.checkSPO2(tokens, op, i);
			}

			if (error) {
				throw new InvalidSyntaxException(line);
			}

		} catch (IndexOutOfBoundsException e) {
			throw new InvalidSyntaxException(line);
		}
	}

	private static boolean checkRegister(int register) {
		if (register > 15 || register < 0) {
			return true;
		}
		return false;
	}

	private static boolean checkRROP2(List<Token> tokens, String op, int i) {
		if ((tokens.get(i).getToken() != TokenType.REGISTER) || (tokens.get(i + 1).getToken() != TokenType.COMMA)
				|| (tokens.get(i + 2).getToken() != TokenType.REGISTER)
				|| (tokens.get(i + 3).getToken() != TokenType.COMMA)
				|| (tokens.get(i + 4).getToken() != TokenType.REGISTER
						&& tokens.get(i + 4).getToken() != TokenType.HASH)) {
			return true;
		}
		return false;
	}

	private static boolean checkOOP2(List<Token> tokens, String op, int i) {
		if (tokens.get(i).getToken() != TokenType.REGISTER && tokens.get(i).getToken() != TokenType.HASH) {
			return true;
		}
		return false;
	}

	private static boolean checkROP2(List<Token> tokens, String op, int i) {
		if ((tokens.get(i).getToken() != TokenType.REGISTER) || (tokens.get(i + 1).getToken() != TokenType.COMMA)
				|| (tokens.get(i + 2).getToken() != TokenType.REGISTER
						&& tokens.get(i + 2).getToken() != TokenType.HASH)) {
			return true;
		}
		return false;
	}

	private static boolean checkSPO2(List<Token> tokens, String op, int i) {
		if (tokens.get(i).getToken() == TokenType.REGISTER && tokens.get(i + 1).getToken() == TokenType.COMMA) {
			;
			Token token = tokens.get(i + 2);
			switch (token.getToken()) {
			case OFFSET:
				return SyntaxChecker.checkRegister(Integer.parseInt(token.toInner()));
			case INDEXEDOFFSET:
				String offset = token.toInner();
				return SyntaxChecker.checkRegister(Integer.parseInt(offset.substring(0, offset.indexOf(","))));
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
}
