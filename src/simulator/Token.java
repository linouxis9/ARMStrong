package simulator;

import java.util.List;

/**
 * Each Token contains a group of characters logically bound together, easily
 * parsable and understandable by a parser.
 */
public class Token {
	private final TokenType type;
	private final String value;

	public Token(TokenType token, String value) {
		this.type = token;
		this.value = value;
	}

	public TokenType getTokenType() {
		return type;
	}

	public String toString() {
		return "[" + type.name() + ":" + this.value + "]";
	}
	
	public int getRawOffset() {
		if (this.type != TokenType.INDEXEDOFFSET && this.type != TokenType.OFFSET) {
			new RuntimeException();
		}
		return Integer.parseInt(this.value.substring(2, this.value.length() - 1));
	}

	public int getRawImmediateValue() {
		if (this.type != TokenType.HASH) {
			new RuntimeException();
		}
		return Integer.parseInt(this.value.substring(1));
	}
	
	public int getRawAsciiValue() {
		if (this.type != TokenType.HASHEDASCII) {
			new RuntimeException();
		}
		return this.value.substring(2, this.value.length() - 1).getBytes()[0];
	}

	public int getRawRegister() {
		if (this.type != TokenType.REGISTER) {
			new RuntimeException();
		}
		return Integer.parseInt(this.value.substring(1));
	}
	
	public String getRawLabel() {
		if (this.type != TokenType.LABEL) {
			new RuntimeException();
		}		
		return this.value.substring(0, this.value.length() - 1);
	}
	
	public String getRawDirective() {
		if (this.type != TokenType.DIRECTIVE) {
			new RuntimeException();
		}		
		return this.value.substring(1);
	}

	public String getRawOperation() {
		if (this.type != TokenType.OPERATION) {
			new RuntimeException();
		}		
		return this.value;
	}
	
	public String getRawIdentifier() {
		if (this.type != TokenType.IDENTIFIER) {
			new RuntimeException();
		}		
		return this.value;
	}

	public char getRawFlag() {
		if (this.type != TokenType.FLAG) {
			new RuntimeException();
		}		
		return this.value.toCharArray()[0];
	}

	public String getRawConditionCode() {
		if (this.type != TokenType.CONDITIONCODE) {
			new RuntimeException();
		}		
		return this.value;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}


}
