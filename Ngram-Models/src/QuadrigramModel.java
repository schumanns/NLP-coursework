import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QuadrigramModel
 * 
 * This class implements a Quadrigram model from a given set of training
 * data.  The training data is given as a List of tokens.
 * @author Leigh Schumann
 *
 */
public class QuadrigramModel {

	static int vocabularySize = 100000; 
	double tokenCount = 0;          // count of tokens in training set
	Map<Quadrigram, Integer> countMap;   // maps tokens to token counts
	WordClass[] N;                  // used for Good Turing
	int GTThreshhold = 20;          // G-T is only applied below this
	double totalUnknownProbability = 0; // probability mass assigned to 
	// unknown tokens by Good-Turing
	double unknownProbability;   // probability of a given unseen token
	TrigramModel tri;


	/* 
	       WordClass is a nested class which represents a set of words, all of
	       which were seen the same number of times in the training set.  It is
	       used by the Good-Turing discounting algorithm.
	 */
	class WordClass {
		int count;
		Quadrigram representative;
		double adjustedCount;
		double prob;
		double adjustedProb;
		double normalizedProb;
	}

	public class Quadrigram {

		String[] tokens;
		final int LEN;

		public Quadrigram(String token0, String token1, String token2, String token3)
		{
			this.LEN = 4;
			tokens = new String[LEN];
			tokens[0] = token0;
			tokens[1] = token1;
			tokens[2] = token2;
			tokens[3] = token3;
		}

		public Quadrigram(String token0, String token1, String token2)
		{
			this.LEN = 3;
			tokens = new String[LEN];
			tokens[0] = token0;
			tokens[1] = token1;
			tokens[2] = token2;
		}

		public Quadrigram(String token1, String token2)
		{
			this.LEN = 2;
			tokens = new String[LEN];
			tokens[0] = token1;
			tokens[1] = token2;
		}

		public Quadrigram(String token2)
		{
			this.LEN = 1;
			tokens = new String[LEN];
			tokens[0] = token2;
		}

		private QuadrigramModel getOuterType() {
			return QuadrigramModel.this;
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
			Quadrigram other = (Quadrigram) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (!Arrays.toString(tokens).equals(Arrays.toString(other.tokens)))
				return false;
			return true;
		}

	}

	QuadrigramModel(List<String> tokenList){

		this.tri = new TrigramModel(tokenList);

		int maxCount = 1;
		countMap = new HashMap<Quadrigram, Integer>();
		for(int i = 0; i < tokenList.size(); i++){
			Quadrigram quadrigram;
			if (i == 0)
				quadrigram = new Quadrigram(tokenList.get(0));
			else if (i == 1)
				quadrigram = new Quadrigram(tokenList.get(0), tokenList.get(1));
			else if (i == 2)
				quadrigram = new Quadrigram(tokenList.get(0), tokenList.get(1), tokenList.get(2));
			else
				quadrigram = new Quadrigram(tokenList.get(i-3), tokenList.get(i-2), tokenList.get(i-1), tokenList.get(i));
			Integer count = countMap.get(quadrigram);
			if(count==null){
				countMap.put(quadrigram, 1);

			}
			else {
				countMap.put(quadrigram, count+1);
				if(count+1>maxCount){
					maxCount = count+1;
				}
			}
		}

		tokenCount = tokenList.size();
		
		/*
		Quadrigram quad = new Quadrigram("is", "so", "long", "since");
		System.out.println(countMap.get(quad));
		
		for (Quadrigram key : countMap.keySet())
		{
			System.out.println(countMap.get(key));
		}
		*/
	}

	/*
	      returns the probability of the given unigram (i.e.,single token)
	 */    
	double getProbability(String token0, String token1, String token2, String token3){
		Quadrigram quadrigram = new Quadrigram(token0, token1, token2, token3);
		Integer quadricount = countMap.get(quadrigram);
		if (quadricount == null)
		{
			return tri.getProbability(token1, token2, token3);
		}
		else
		{
			TrigramModel.Trigram trigram = tri.new Trigram(token0, token1, token2);
			Integer tricount = tri.countMap.get(trigram);
			return (double)quadricount/(double)tricount;
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
				prob = tri.bi.uni.getProbability(tokenList.get(0));
			}
			else if (i == 1)
			{
				prob = tri.bi.getProbability(tokenList.get(0), tokenList.get(1));
			}
			else if (i == 2)
			{
				prob = tri.getProbability(tokenList.get(0), tokenList.get(1), tokenList.get(2));
			}
			else
			{
				String token0 = tokenList.get(i-3);
				String token1 = tokenList.get(i-2);
				String token2 = tokenList.get(i-1);
				String token3 = tokenList.get(i);
				prob = this.getProbability(token0, token1, token2, token3);
				//System.out.println(token0 + " " + token1 + " " + token2);
			}
			logSum += Math.log(prob);
		}
		double logSumRoot = logSum / tokenList.size();
		return Math.exp(-logSumRoot);
	}

}

