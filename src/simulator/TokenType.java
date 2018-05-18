package simulator;
/**
 * This enum contains the different types of statements that can be found
 * in an ARM Assembly program alongside a regular expression allowing
 * to match and differentiate the statements from the others.
 */
public enum TokenType {
	COMMENT("@.*"),
	LABEL("[a-z]+:"),
	CONDITIONCODE("(eq|ne|cs|cc|mi|pl|vs|vc|hi|ls|ge|lt|gt|le|al)"),
	FLAG("(?<!^)(?<!:)(b|h|s)"),
	DIRECTIVE("\\.[a-z]+( )+([a-z]|[0-9])*"),
	OFFSET("\\[r[0-9]+\\]"), 
	INDEXEDOFFSET("\\[r[0-9]{1,2}\\,(\\+|-)?[1-9]+\\]"), 
	COMMA("\\,"),
	SEMICOLON("\\;"),
	SHIFTEDREGISTER("(LSL|LSR)( )*#([0-9]+)"),
	HASH("#(\\+|-)?([0-9]+)"),
	HASHEDASCII("#'(([0-9]|[A-z]))'"),
	REGISTER("(r|R)[0-9]+"),
	NUMBER("(?<!r)[0-9]+"),
	OPERATION("(adc|add|and|b|bic|bl|cmn|cmp|eor|ldr|mla|mov|mvn|mul|orr|sdiv|str|swi|sub|svc|swp|teq|tst|udiv)"),
	DATAIDENTIFIER("=[a-z]+"),
	IDENTIFIER("[a-z]+");
	
    public final String regexp;
    
    private TokenType(String regexp) {
      this.regexp = regexp;
    }
}
