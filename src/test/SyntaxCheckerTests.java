package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import simulator.Program;
import simulator.Preprocessor;
import simulator.Token;

public class SyntaxCheckerTests {
    @Test
	public void testLexer() {
		List<Token> tokens = Program.lexer("mov r4,r4");
		try {
			Preprocessor.checkSyntax(tokens);
		}
		catch (Exception e) {
			fail();
		}
		tokens = Program.lexer("mov r4,r40");
		try {
			Preprocessor.checkSyntax(tokens);
			fail();
		}
		catch (Exception e) {
		}
		
		tokens = Program.lexer("strb r4,[r10]");
		try {
			Preprocessor.checkSyntax(tokens);
		}
		catch (Exception e) {
			fail();
		}
		
		tokens = Program.lexer("ldrh r4,[r10]");
		try {
			Preprocessor.checkSyntax(tokens);
		}
		catch (Exception e) {
			fail();
		}

		tokens = Program.lexer("ldrh r4");
		try {
			Preprocessor.checkSyntax(tokens);
			fail();
		}
		catch (Exception e) {
		}
		
		tokens = Program.lexer("add r4,r4,#-25");
		try {
			Preprocessor.checkSyntax(tokens);
		}
		catch (Exception e) {
			fail();
		}
		
		tokens = Program.lexer("b label");
		try {
			Preprocessor.checkSyntax(tokens);
		}
		catch (Exception e) {
			fail();
		}
		
		tokens = Program.lexer("ldrheq r4,[r10]");
		try {
			Preprocessor.checkSyntax(tokens);
		}
		catch (Exception e) {
			fail();
		}
	}
}
