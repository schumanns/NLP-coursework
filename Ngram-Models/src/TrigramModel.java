import java.util.*;

/**
 * TrigramModel
 * 
 * This class implements a Trigram model from a given set of training
 * data.  The training data is given as a List of tokens.
 * @author Leigh Schumann
 *
 */
class TrigramModel {

	static int vocabularySize = 100000; 
	double tokenCount = 0;          // count of tokens in training set
	Map<Trigram, Integer> countMap;   // maps tokens to token counts
	WordClass[] N;                  // used for Good Turing
	int GTThreshhold = 20;          // G-T is only applied below this
	double totalUnknownProbability = 0; // probability mass assigned to 
	// unknown tokens by Good-Turing
	double unknownProbability;   // probability of a given unseen token
	BigramModel bi;


	/* 
	       WordClass is a nested class which represents a set of words, all of
	       which were seen the same number of times in the training set.  It is
	       used by the Good-Turing discounting algorithm.
	 */
	class WordClass {
		int count;
		Trigram representative;
		double adjustedCount;
		double prob;
		double adjustedProb;
		double normalizedProb;
	}

	public class Trigram {

		String[] tokens;
		final int LEN;

		public Trigram(String token0, String token1, String token2)
		{
			this.LEN = 3;
			tokens = new String[LEN];
			tokens[0] = token0;
			tokens[1] = token1;
			tokens[2] = token2;
		}

		public Trigram(String token1, String token2)
		{
			this.LEN = 2;
			tokens = new String[LEN];
			tokens[0] = token1;
			tokens[1] = token2;
		}

		public Trigram(String token2)
		{
			this.LEN = 1;
			tokens = new String[LEN];
			tokens[0] = token2;
		}

		private TrigramModel getOuterType() {
			return TrigramModel.this;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.toString(tokens).hashCode();
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
			Trigram other = (Trigram) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (!Arrays.toString(tokens).equals(Arrays.toString(other.tokens)))
				return false;
			return true;
		}

	}

	TrigramModel(List<String> tokenList){
		
		this.bi = new BigramModel(tokenList);

		int maxCount = 1;
		countMap = new HashMap<Trigram, Integer>();
		for(int i = 0; i < tokenList.size(); i++){
			Trigram trigram;
			if (i == 0)
				trigram = new Trigram(tokenList.get(0));
			else if (i == 1)
				trigram = new Trigram(tokenList.get(0), tokenList.get(1));
			else
				trigram = new Trigram(tokenList.get(i-2), tokenList.get(i-1), tokenList.get(i));
			Integer count = countMap.get(trigram);
			if(count==null){
				countMap.put(trigram, 1);

			}
			else {
				countMap.put(trigram, count+1);
				if(count+1>maxCount){
					maxCount = count+1;
				}
			}
		}

		tokenCount = tokenList.size();    

	}

	/*
	      returns the probability of the given unigram (i.e.,single token)
	 */    
	double getProbability(String token0, String token1, String token2){
		Trigram trigram = new Trigram(token0, token1, token2);
		Integer tricount = countMap.get(trigram);
		if (tricount == null)
		{
			return bi.getProbability(token1, token2);
		}
		else
		{
			BigramModel.Bigram bigram = bi.new Bigram(token0, token1);
			Integer bicount = bi.countMap.get(bigram);
			return (double)tricount/(double)bicount;
		}
	}

	/*
	      computes and returns the perplexity of the given token sequence
	 */
	double getPerplexity(List<String> tokenList){
		double logSum = 0;
		for (int i = 0; i < tokenList.size(); i++)
		{
			double prob;
			if (i == 0)
			{
				prob = bi.uni.getProbability(tokenList.get(0));
			}
			else if (i == 1)
			{
				prob = bi.getProbability(tokenList.get(0), tokenList.get(1));
			}
			else
			{
				String token0 = tokenList.get(i-2);
				String token1 = tokenList.get(i-1);
				String token2 = tokenList.get(i);
				prob = this.getProbability(token0, token1, token2);
				//System.out.println(token0 + " " + token1 + " " + token2);
			}
			logSum += Math.log(prob);
		}
		double logSumRoot = logSum / tokenList.size();
		return Math.exp(-logSumRoot);
	}

}
