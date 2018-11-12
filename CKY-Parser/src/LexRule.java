/**
 * LexRule
 * 
 * A class which represents lexical rules.
 * Lexical rules are rules about the probability of certain tags for a word.
 * @author Leigh Schumann
 *
 */
public class LexRule extends PCFGRule {

	private String rhs;

	public LexRule(Nonterminal lhs,String rhs,double prob){
		super(lhs,prob);
		this.rhs = rhs;
	} 

	public String toString(){
		return lhs+" ==> '"+rhs+"' ["+prob+"]";
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
		LexRule other = (LexRule) obj;
		if (rhs == null) {
			if (other.rhs != null)
				return false;
		} else if (!rhs.equals(other.rhs))
			return false;
		return true;
	}
}
