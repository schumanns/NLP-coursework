/**
 * BinaryRule
 * 
 * A class which represents Binary Rules in a probabilistic context-free grammar.
 * These rules have one symbol on the left hand side and two symbols on the right
 * hand side.
 * @author Leigh Schumann
 *
 */

class BinaryRule extends PCFGRule {
	Nonterminal rhs0;
	Nonterminal rhs1;

	BinaryRule(Nonterminal lhs,Nonterminal rhs0,Nonterminal rhs1,double prob){
		super(lhs,prob);
		this.rhs0 = rhs0;
		this.rhs1 = rhs1;
	}

	public String toString(){
		return lhs+" ==> "+rhs0+" "+rhs1+" ["+prob+"]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((rhs0 == null) ? 0 : rhs0.hashCode());
		result = prime * result + ((rhs1 == null) ? 0 : rhs1.hashCode());
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
		BinaryRule other = (BinaryRule) obj;
		if (rhs0 == null) {
			if (other.rhs0 != null)
				return false;
		} else if (!rhs0.equals(other.rhs0))
			return false;
		if (rhs1 == null) {
			if (other.rhs1 != null)
				return false;
		} else if (!rhs1.equals(other.rhs1))
			return false;
		return true;
	}  
}
