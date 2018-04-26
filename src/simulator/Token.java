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
	
}
