package simulator.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import simulator.core.Program;
import simulator.core.Token;
import simulator.core.TokenType;

public class ProgramTests {
    @Test
	public void testLexer() {
		List<Token> tokens = Program.lexer("mov r4,r4");
		Set<Token> expected = new HashSet<Token>() {{
		    add(new Token(TokenType.OPERATION,"mov"));
		    add(new Token(TokenType.REGISTER,"r4"));
		    add(new Token(TokenType.COMMA,","));
		    add(new Token(TokenType.REGISTER,"r4"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));

		tokens = Program.lexer("mov r4,#4");
		expected = new HashSet<Token>() {{
		    add(new Token(TokenType.OPERATION,"mov"));
		    add(new Token(TokenType.REGISTER,"r4"));
		    add(new Token(TokenType.COMMA,","));
		    add(new Token(TokenType.HASH,"#4"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
		tokens = Program.lexer("mov r4,#+4");
		expected = new HashSet<Token>() {{
		    add(new Token(TokenType.OPERATION,"mov"));
		    add(new Token(TokenType.REGISTER,"r4"));
		    add(new Token(TokenType.COMMA,","));
		    add(new Token(TokenType.HASH,"#+4"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
				
		tokens = Program.lexer("mov r4,#-4");
		expected = new HashSet<Token>() {{
		    add(new Token(TokenType.OPERATION,"mov"));
		    add(new Token(TokenType.REGISTER,"r4"));
		    add(new Token(TokenType.COMMA,","));
		    add(new Token(TokenType.HASH,"#-4"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
		tokens = Program.lexer("b coucou");
		expected = new HashSet<Token>() {{
		    add(new Token(TokenType.OPERATION,"b"));
		    add(new Token(TokenType.IDENTIFIER,"coucou"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
		tokens = Program.lexer("coucou:b coucou");
		expected = new HashSet<Token>() {{
			add(new Token(TokenType.LABEL,"coucou:"));
		    add(new Token(TokenType.OPERATION,"b"));
		    add(new Token(TokenType.IDENTIFIER,"coucou"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
		tokens = Program.lexer("coucou:ldr r4,[r0]");
		expected = new HashSet<Token>() {{
			add(new Token(TokenType.LABEL,"coucou:"));
		    add(new Token(TokenType.OPERATION,"ldr"));
		    add(new Token(TokenType.REGISTER,"r4"));
		    add(new Token(TokenType.COMMA,","));
		    add(new Token(TokenType.OFFSET,"[r0]"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
		tokens = Program.lexer("coucou:ldrheq r4,[r0]");
		expected = new HashSet<Token>() {{
			add(new Token(TokenType.LABEL,"coucou:"));
		    add(new Token(TokenType.OPERATION,"ldr"));
		    add(new Token(TokenType.FLAG,"h"));
		    add(new Token(TokenType.CONDITIONCODE,"eq"));
		    add(new Token(TokenType.REGISTER,"r4"));
		    add(new Token(TokenType.COMMA,","));
		    add(new Token(TokenType.OFFSET,"[r0]"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
		tokens = Program.lexer("@coucou:ldr r4,[r0]");
		expected = new HashSet<Token>() {{
			add(new Token(TokenType.COMMENT,"@coucou:ldr r4,[r0]"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
		tokens = Program.lexer(".align 4");
		expected = new HashSet<Token>() {{
			add(new Token(TokenType.DIRECTIVE,".align 4"));
		}};
		assertTrue(expected.containsAll(tokens));
		assertTrue(tokens.containsAll(expected));
		
	}
}
