package simulator;

import java.util.ArrayList;
import java.util.List;

public class SyntaxChecker {
	
	public static List<String> listOp;
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public static void checkSyntax(List<Token> tokens)
			throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException {
		if (SyntaxChecker.listOp == null) {
			listOp = new ArrayList<String>();
			for (Operation op: Operation.values()) {
				listOp.add(op.name());
			}
		}
		try {
			String op = tokens.get(0).getValue().toUpperCase();
			if (!SyntaxChecker.listOp.contains(op)) {
				throw new InvalidOperationException();
			}
			int i = 1;
			if (tokens.get(1).getToken() == TokenType.CONDITIONCODE) {
				i++;
			}
			
			if ((op == "SWI" || op == "SVC") && (tokens.get(i).getToken() != TokenType.HASH)) {
				throw new InvalidSyntaxException();
			} else if (tokens.get(i).getToken() != TokenType.REGISTER) {
				throw new InvalidSyntaxException();
			}
			
			// TODO To finish
		} catch (IndexOutOfBoundsException e) {
				throw new InvalidSyntaxException();
		}
	}

}
