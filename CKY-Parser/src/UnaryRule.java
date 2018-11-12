/**
 * Unary Rule
 * 
 * A class which implements unary rules for a probabilistic context-free grammar.
 * Unary rules consist of a left hand side, ONE element on the right hand side,
 * and the probability of the rule.
 * @author Leigh Schumann
 *
 */

public class UnaryRule extends PCFGRule {
	private Nonterminal rhs;

	public UnaryRule(Nonterminal lhs,Nonterminal rhs,double prob){
		super(lhs,prob);
		this.rhs = rhs;
	}

	public String toString(){
		return lhs+" ==> "+rhs+" ["+prob+"]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
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
		UnaryRule other = (UnaryRule) obj;
		if (rhs == null) {
			if (other.rhs != null)
				return false;
		} else if (!rhs.equals(other.rhs))
			return false;
		return true;
	}

}
