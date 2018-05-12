package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import simulator.Program;
import simulator.Preprocessor;
import simulator.Token;

public class PreprocessorTests {
    @Test
	public void testLexer() {
		List<Token> tokens = Program.lexer("mov r4,r4");
		try {
			Preprocessor.preprocess(tokens);
		}
		catch (Exception e) {
			System.out.println(e);
			fail();
		}
		tokens = Program.lexer("mov r4,r40");
		try {
			Preprocessor.preprocess(tokens);
			fail();
		}
		catch (Exception e) {
		}
		
		tokens = Program.lexer("strb r4,[r10]");
		try {
			Preprocessor.preprocess(tokens);
		}
		catch (Exception e) {
			fail();
		}
		
		tokens = Program.lexer("ldrh r4,[r10]");
		try {
			Preprocessor.preprocess(tokens);
		}
		catch (Exception e) {
			fail();
		}

		tokens = Program.lexer("ldrh r4");
		try {
			Preprocessor.preprocess(tokens);
			fail();
		}
		catch (Exception e) {
		}
		
		tokens = Program.lexer("add r4,r4,#-25");
		try {
			Preprocessor.preprocess(tokens);
		}
		catch (Exception e) {
			fail();
		}
		
		tokens = Program.lexer("b label");
		try {
			Preprocessor.preprocess(tokens);
		}
		catch (Exception e) {
			fail();
		}
		
		tokens = Program.lexer("ldrheq r4,[r10]");
		try {
			Preprocessor.preprocess(tokens);
		}
		catch (Exception e) {
			fail();
		}
		
		tokens = Program.lexer("movcc r4,#'z'");
		try {
			Preprocessor.preprocess(tokens);
		}
		catch (Exception e) {
			fail();
		}
	}
}
