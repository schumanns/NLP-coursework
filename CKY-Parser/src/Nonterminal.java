/**
 * Nonterminal
 * 
 * A class which represents a nonterminal.
 * A nonterminal is any kind of symbol that isn't a word. This can include
 * POS tags or larger constituent symbols like NP or S.
 * @author Leigh Schumann
 *
 */
public class Nonterminal {

	public String symbol;

	public Nonterminal(String symbol){
		this.symbol = symbol;
	}

	public String toString(){
		return symbol;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nonterminal other = (Nonterminal) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
}
