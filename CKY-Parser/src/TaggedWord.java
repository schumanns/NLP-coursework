/**
  TaggedWord

  This class represents a tagged word for use with POS tagging applications.

  A tagged word consists of a word and a tag.
  @author John Donaldson and Leigh Schumann
*/
public class TaggedWord {
    private String word;
    private String tag;

    public TaggedWord(String word, String tag){
	this.word = word;
	this.tag = tag;
    }

    public String getWord(){
	return word;
    }

    public String getTag(){
	return tag;
    }

    public String toString(){
	return word + '/' + tag;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		TaggedWord other = (TaggedWord) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
    
}

