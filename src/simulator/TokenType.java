package simulator;
/**
 * This enum contains the different types of statements that can be found
 * in an ARM Assembly program alongside a regular expression allowing
 * to match and differentiate the statements from the others.
 */
public enum TokenType {
	COMMENT("@.*"),
	CONDITIONCODE("(eq|ne|cs|cc|mi|pl|vs|vc|hi|ls|ge|lt|gt|le|al)"),
	FLAG("(?<!^)(?<!\\s)(?<!:)(b|h|s)"),
	DIRECTIVE("\\.[a-z]+( )*([a-z]|[0-9])*"),
	OFFSET("\\[r[0-9]+\\]"), 
	INDEXEDOFFSET("\\[r[0-9]{1,2}\\,(\\+|-)?[1-9]+\\]"), 
	COMMA("\\,"),
	SEMICOLON("\\;"),
	SHIFTEDREGISTER("(LSL|LSR)( )*#([0-9]+)"),
	HASH("#(\\+|-)?([0-9]+)"),
	HASHEDASCII("#'.'"),
	REGISTER("(r|R)[0-9]+"),
	NUMBER("(?<!r)[0-9]+"),
	OPERATION("(?<![a-z])(adc|add|and|bic|bl|b|cmn|cmp|eor|ldr|mla|mov|mvn|mul|orr|sdiv|str|swi|sub|svc|swp|teq|tst|udiv)((?=(b|h|f))|(?=((b|h|f)(eq|ne|cs|cc|mi|pl|vs|vc|hi|ls|ge|lt|gt|le|al))|(?=(eq|ne|cs|cc|mi|pl|vs|vc|hi|ls|ge|lt|gt|le|al)| )))"),
	LABEL("[a-z]+:"),
	IDENTIFIER("[a-z]+"),
	DATAIDENTIFIER("=[a-z]+"),
	CATCHSYNTAXERROR("^([^\\s])");
	
    public final String regexp;
    
    private TokenType(String regexp) {
      this.regexp = regexp;
    }
}
