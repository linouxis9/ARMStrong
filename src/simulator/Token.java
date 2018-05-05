package simulator;

/**
 * Each Token contains a group of characters logically bound together, easily
 * parsable and understandable by a parser.
 */
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

	public String toString() {
		return "[" + token.name() + ":" + this.value + "]";
	}

	public String getValue() {
		switch (this.token) {
		case INDEXEDOFFSET:
		case OFFSET:
			return this.value.substring(2, this.value.length() - 1);
		case HASH:
			return this.value.substring(1);
		case REGISTER:
			return this.value.substring(1);
		case LABEL:
			return this.value.substring(0, this.value.length() - 1);
		case HASHEDASCII:
			return Integer.toString(this.value.substring(2, this.value.length() - 1).getBytes()[0]);
		default:
			return this.value;
		}
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (token != other.token)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
