package simulator;

public enum TokenType {
	CONDITIONCODE("(eq|ne|cs|cc|mi|pl|vs|vc|hi|ls|ge|lt|gt|le|al)"),PLUS("\\+"), MINUS("\\-"), LEFTBRACKET("\\["), RIGHTBRACKET("\\]"), COMMA("\\,"), SEMICOLON("\\;"), HASH("#([1-9]*)"), REGISTER("r[1-9]{1,2}"),NUMBER("(?<!r)[0-9]+"), INSTRUCTION("(adc|add|and|b|bic|bl|cmn|cmp|eor|ldr|mla|mov|mvn|mul|orr|sdiv|str|swi|sub|svc|swp|teq|tst|udiv)"), WHITESPACE("\\ *");
	
    public final String regexp;
    
    private TokenType(String regexp) {
      this.regexp = regexp;
    }
}
