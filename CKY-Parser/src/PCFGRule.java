/**
 * PCFGRule
 * 
 * A class which represents a rule in a probabilistic context free grammar.
 * This is used as a super class for specific kinds of rules like lexical rules,
 * unary rules, and binary rules.
 * @author Leigh Schumann
 *
 */
public class PCFGRule {

	protected Nonterminal lhs;
	protected double prob;

	public PCFGRule(Nonterminal lhs,double prob){
		this.lhs = lhs;
		this.prob = prob;
	}    
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		long temp;
		temp = Double.doubleToLongBits(prob);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		PCFGRule other = (PCFGRule) obj;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		if (Double.doubleToLongBits(prob) != Double.doubleToLongBits(other.prob))
			return false;
		return true;
	}
}
