package simulator;

public enum TokenType {
	COMMENT("@.*"),
	LABEL("[a-z]+:"),
	FLAG("(?<!^)(b|h|s)"),
	DIRECTIVE("\\.[a-z]+ [a-z]+"),
	CONDITIONCODE("(eq|ne|cs|cc|mi|pl|vs|vc|hi|ls|ge|lt|gt|le|al)"),
	PLUS("\\+"),
	MINUS("\\-"),
	OFFSET("\\[r[1-9]{1,2}\\]"), 
	INDEXEDOFFSET("\\[r[0-9]{1,2}\\,(\\+|-)?[1-9]+\\]"), 
	COMMA("\\,"),
	SEMICOLON("\\;"),
	HASH("#(\\+|-)?([0-9]+)"),
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
