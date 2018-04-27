package simulator;

public class Token {
	private final TokenType token;
	private final String value;
	
	public Token(TokenType token, String value) {
		this.token = token;
		this.value = value;
	}
	
	public TokenType getToken() {
		return token;
	}
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return "[" + token.name() + ":" + this.value + "]";
	}
	
	// TODO Technical debt, isn't it?
	public String toInner() {
		switch(this.token) {
			case INDEXEDOFFSET:
			case OFFSET:
				return this.value.substring(2, this.value.length()-1);
			case HASH:
				return this.value.substring(1);
			case REGISTER:
				return this.value.substring(1);
			default:
				return this.value;
		}
	}
	
}
