package simulator;

public enum TokenType {
	COMMENT("@.*"),
	LABEL("[a-z]+:"),
	CONDITIONCODE("(eq|ne|cs|cc|mi|pl|vs|vc|hi|ls|ge|lt|gt|le|al)"),
	FLAG("(?<!^)(?<!:)(b|h|s)"),
	DIRECTIVE("\\.[a-z]+ ([a-z]|[0-9])+"),
	OFFSET("\\[r[0-9]+\\]"), 
	INDEXEDOFFSET("\\[r[0-9]{1,2}\\,(\\+|-)?[1-9]+\\]"), 
	COMMA("\\,"),
	SEMICOLON("\\;"),
	HASH("#(\\+|-)?([0-9]+)"),
	HASHEDASCII("#'(([0-9]|[A-z]))'"),
	REGISTER("r[0-9]+"),
	NUMBER("(?<!r)[0-9]+"),
	OPERATION("(adc|add|and|b|bic|bl|cmn|cmp|eor|ldr|mla|mov|mvn|mul|orr|sdiv|str|swi|sub|svc|swp|teq|tst|udiv)"),
	WHITESPACE(" +"),
	IDENTIFIER("[a-z]+");
	
    public final String regexp;
    
    private TokenType(String regexp) {
      this.regexp = regexp;
    }
}
