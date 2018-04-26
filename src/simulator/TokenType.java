package simulator;

public enum TokenType {
	PLUS("\\+"), MINUS("\\-"), LEFTBRACKET("\\["), RIGHTBRACKET("\\]"), COMMA("\\,"), SEMICOLON("\\;"), HASH("#([1-9]*)"), REGISTER("r[1-9]{1,2}"),NUMBER("(?<!r)[0-9]+"), INSTRUCTION("[a-z]{1,4}"), WHITESPACE("\\ *");
	
    public final String regexp;
    
    private TokenType(String regexp) {
      this.regexp = regexp;
    }
}
