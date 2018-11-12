import java.util.*;

/**
 * BigramModel
 * 
 *  This class implements a BigramModel from a given set of training
 *  data.  The training data is given as a List of tokens.
 * @author Leigh Schumann
 *
 */
public class BigramModel {

	static int vocabularySize = 100000; 
	double tokenCount = 0;          // count of tokens in training set
	Map<Bigram, Integer> countMap;   // maps tokens to token counts
	WordClass[] N;                  // used for Good Turing
	int GTThreshhold = 20;          // G-T is only applied below this
	double totalUnknownProbability = 0; // probability mass assigned to 
	// unknown tokens by Good-Turing
	double unknownProbability;   // probability of a given unseen token
	UnigramModel uni;


	/* 
	       WordClass is a nested class which represents a set of words, all of
	       which were seen the same number of times in the training set.  It is
	       used by the Good-Turing discounting algorithm.
	 */
	class WordClass {
		int count;
		Bigram representative;
		double adjustedCount;
		double prob;
		double adjustedProb;
		double normalizedProb;
	}

	public class Bigram {
		String token0;
		String token1;

		public Bigram() {
		}

		public Bigram(String token0, String token1) {
			// TODO Auto-generated constructor stub
			this.token0 = token0;
			this.token1 = token1;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((token0 == null) ? 0 : token0.hashCode());
			result = prime * result + ((token1 == null) ? 0 : token1.hashCode());
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
			Bigram other = (Bigram) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (token0 == null) {
				if (other.token0 != null)
					return false;
			} else if (!token0.equals(other.token0))
				return false;
			if (token1 == null) {
				if (other.token1 != null)
					return false;
			} else if (!token1.equals(other.token1))
				return false;
			return true;
		}

		private BigramModel getOuterType() {
			return BigramModel.this;
		}

	}

	BigramModel(List<String> tokenList){

		List<String> unigramTokenList = new ArrayList<String>(tokenList);
		unigramTokenList.add(0, "$BEGINNING$");
		uni = new UnigramModel(unigramTokenList);


		// count unigrams
		int maxCount = 1;
		countMap = new HashMap<Bigram, Integer>();
		String prevToken = "$BEGINNING$"; //A chaser to make teh bigrom.
		for(String curToken : tokenList){
			Bigram bigram = new Bigram();
			bigram.token0 = prevToken;
			bigram.token1 = curToken;
			Integer count = countMap.get(bigram);
			if(count==null){
				countMap.put(bigram, 1);
			}
			else {
				countMap.put(bigram, count+1);
				if(count+1>maxCount){
					maxCount = count+1;
				}
			}
			prevToken = curToken;
		}

		tokenCount = tokenList.size();    
	}


	/*
	      returns the probability of the given unigram (i.e.,single token)
	 */    
	double getProbability(String token0, String token1){
		Bigram key = new Bigram(token0, token1);
		double count = 0;
		double token0Count = 0;
		if (countMap.containsKey(key))
			count = this.countMap.get(key);
		if (uni.countMap.containsKey(token0))
			token0Count = uni.countMap.get(token0);
		if ((count == 0) || (token0Count == 0))
			return uni.getProbability(token1);
		else
			return count/token0Count;
	}

	/*
	      computes and returns the perplexity of the given token sequence
	 */
	double getPerplexity(List<String> tokenList){
		double logSum = 0;
		for (int i = 0; i < tokenList.size(); i++)
		{
			double prob;
			String token0;
			if (i == 0)
			{
				prob = uni.getProbability(tokenList.get(i));
			}
			else
			{
				token0 = tokenList.get(i-1);
				String token1 = tokenList.get(i);
				prob = this.getProbability(token0, token1);
			}
			logSum += Math.log(prob);
		}
		double logSumRoot = logSum / tokenList.size();
		return Math.exp(-logSumRoot);
	}

}
