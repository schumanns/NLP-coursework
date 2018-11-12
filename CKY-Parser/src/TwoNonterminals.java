/**
 * TwoNonterminals
 * 
 * A class which represents two nonterminals.
 * This is useful for making HashMaps which store all the binary rules that
 * are associated with two given nonterminals on the right hand side.
 * @author Leigh Schumann
 *
 */

public class TwoNonterminals extends Nonterminal {
	public String symbol2;

	public TwoNonterminals(String symbol1, String symbol2) {
		super(symbol1);
		this.symbol2 = symbol2;
	}

	public TwoNonterminals(Nonterminal ntR0, Nonterminal ntR1) {
		super(ntR0.symbol);
		this.symbol2 = ntR1.symbol;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.symbol + ", " + this.symbol2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((symbol2 == null) ? 0 : symbol2.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwoNonterminals other = (TwoNonterminals) obj;
		if (symbol2 == null) {
			if (other.symbol2 != null)
				return false;
		} else if (!symbol2.equals(other.symbol2))
			return false;
		return true;
	}

}
