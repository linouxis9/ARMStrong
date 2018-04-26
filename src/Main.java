import java.util.List;

import simulator.*;

public class Main {
	public static void main(String[] args) {
		ArmSimulator jpp = new ArmSimulator();
		if (args.length == 0) {
			System.out.println("You need to pass ARM assembly to java, ex: java Main \"mov r2,r3\" for syntax checking.");
			return;
		}
		List<Token> tokens = Program.lexer(args[0]);
		System.out.println(tokens);
		try {
			SyntaxChecker.checkSyntax(tokens);
		} catch (InvalidSyntaxException | InvalidOperationException | InvalidRegisterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		System.out.println("ALLOWED SYNTAX");
	}
}
