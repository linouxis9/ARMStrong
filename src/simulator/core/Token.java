/**
 * Copyright (c) 2018 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simulator.core;

import simulator.core.exceptions.Bug;

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
			throw new Bug("Token: invalid getter used");
		}
		return Integer.parseInt(this.value.substring(2, this.value.length() - 1));
	}

	public int getRawImmediateValue() {
		if (this.type != TokenType.HASH) {
			throw new Bug("Token: invalid getter used");
		}
		return Integer.parseInt(this.value.substring(1));
	}
	
	public int getRawAsciiValue() {
		if (this.type != TokenType.HASHEDASCII) {
			throw new Bug("Token: invalid getter used");
		}
		return this.value.substring(2, this.value.length() - 1).getBytes()[0];
	}

	public int getRawRegister() {
		if (this.type != TokenType.REGISTER) {
			throw new Bug("Token: invalid getter used");
		}
		return Integer.parseInt(this.value.substring(1));
	}
	
	public String getRawLabel() {
		if (this.type != TokenType.LABEL) {
			throw new Bug("Token: invalid getter used");
		}		
		return this.value.substring(0, this.value.length() - 1);
	}

	public String getRawDirective() {
		if (this.type != TokenType.DIRECTIVE) {
			throw new Bug("Token: invalid getter used");
		}
		if (this.value.indexOf(' ') == -1) {
			return this.value.substring(1);
		} else {
			return this.value.substring(1,this.value.indexOf(' '));
		}
	}
	
	public String getRawDirectiveData() {
		if (this.type != TokenType.DIRECTIVE) {
			throw new Bug("Token: invalid getter used");
		}
		if (this.value.indexOf(' ') == -1) {
			return "";
		} else {
			return this.value.substring(this.value.indexOf(' ')+1,this.value.length());
		}
	}
	
	public String getRawOperation() {
		if (this.type != TokenType.OPERATION) {
			throw new Bug("Token: invalid getter used");
		}		
		return this.value.replaceAll("\\s", "");
	}
	
	public String getRawIdentifier() {
		if (this.type != TokenType.IDENTIFIER) {
			throw new Bug("Token: invalid getter used");
		}		
		return this.value;
	}

	public String getRawDataIdentifier() {
		if (this.type != TokenType.DATAIDENTIFIER) {
			throw new Bug("Token: invalid getter used");
		}		
		return this.value.substring(1);
	}
	
	public char getRawFlag() {
		if (this.type != TokenType.FLAG) {
			throw new Bug("Token: invalid getter used");
		}		
		return this.value.toCharArray()[0];
	}

	public String getRawConditionCode() {
		if (this.type != TokenType.CONDITIONCODE) {
			throw new Bug("Token: invalid getter used");
		}		
		return this.value;
	}
	
	public String getSyntaxError() {
		if (this.type != TokenType.CATCHSYNTAXERROR) {
			throw new Bug("Token: invalid getter used");
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
