import java.util.*;
/**
 * UnigramModel
 * 
 *  This class implements a UnigramModel from a given set of training
 *  data.  The training data is given as a List of tokens.
 *  @author Leigh Schumann
 */
class UnigramModel {
	static int vocabularySize = 100000; 
	double tokenCount = 0;          // count of tokens in training set
	Map<String,Integer> countMap;   // maps tokens to token counts
	HashMap<String,Double> probMap; // maps tokens to probabilities
	WordClass[] N;                  // used for Good Turing
	int GTThreshhold = 20;          // G-T is only applied below this
	double totalUnknownProbability = 0; // probability mass assigned to 
	// unknown tokens by Good-Turing
	double unknownProbability;      // probability of a given unseen token

	/**
       WordClass is a nested class which represents a set of words, all of
       which were seen the same number of times in the training set.  It is
       used by the Good-Turing discounting algorithm.
	 */
	class WordClass {
		int count;
		String representative;
		double adjustedCount;
		double prob;
		double adjustedProb;
		double normalizedProb;
	}

	UnigramModel(List<String> tokenList){

		probMap = new HashMap<String,Double>();

		// count unigrams
		int maxCount = 1;
		countMap = new HashMap<String, Integer>();
		for(String token : tokenList){
			Integer count = countMap.get(token);
			if(count==null){
				countMap.put(token, 1);
			}
			else {
				countMap.put(token, count+1);
				if(count+1>maxCount){
					maxCount = count+1;
				}
			}
		}

		tokenCount = tokenList.size();    

		// assign MLE probabilities
		for(String key : countMap.keySet()){
			int count = countMap.get(key);
			probMap.put(key,(count/tokenCount));
		}

		/*
           This section of code applies Good-Turing
           discounting to the probabilities we've
           computed so far.
		 */

		// create the WordClass array
		N = new WordClass[maxCount+10];

		// initialize each entry
		for(int i=0; i<N.length; i++){
			N[i] = new WordClass();
			N[i].prob = i/tokenCount;
		}

		// count the number of tokens in each WordClass
		for(String key : countMap.keySet()){
			int count = countMap.get(key);
			N[count].count++;
			if(N[count].representative == null)
				N[count].representative = key;
		}

		// Good-Turing estimate of the total probability of all unseen tokens
		totalUnknownProbability = N[1].count/tokenCount;

		// this probability is divided among all unseen words in our vocabulary
		unknownProbability = totalUnknownProbability/(vocabularySize-countMap.keySet().size());

		// In the following loop, discounting is applied to only the tokens
		// which have appeared 1-19 times in the training set
		double totalAdjustedProbability = 0;
		double totalRawProbability = 0;

		for(int i=0; i<GTThreshhold; i++){
			WordClass wclass = N[i];
			if(wclass.count==0 || N[i+1].count==0 || N[i+1].count>wclass.count)
				wclass.adjustedCount = i;
			else
				wclass.adjustedCount = 1.0*(i+1)*N[i+1].count/wclass.count;
			wclass.adjustedProb = wclass.adjustedCount / tokenCount;
			totalRawProbability += wclass.prob * wclass.count;
			totalAdjustedProbability += wclass.adjustedProb*wclass.count;
		}

		// Probabilites must be normalized so they add up to 1	
		double normalizationFactor = (totalRawProbability-totalUnknownProbability)/totalAdjustedProbability;

		// This loop applies the normalization factor
		for(int i=N.length-1; i>=0; i--){
			WordClass wclass = N[i];
			if(wclass.count!=0 && i<GTThreshhold){
				wclass.normalizedProb = wclass.adjustedProb*normalizationFactor;
			}
			else {
				wclass.normalizedProb = wclass.prob;
			}
		}

		for(String token : countMap.keySet()){
			int count = countMap.get(token);
			WordClass wc = N[count];
			probMap.put(token,wc.normalizedProb);
		}

	}

	/*
      returns the probability of the given unigram (i.e.,single token)
	 */    
	double getProbability(String token){
		Double prob = probMap.get(token);
		if(prob == null)
			return unknownProbability;
		else
			return prob;
	}

	/*
      computes and returns the perplexity of the given token sequence
	 */
	double getPerplexity(List<String> tokenList){
		double logSum = 0;
		for(String token : tokenList){
			logSum += Math.log(getProbability(token));
		}
		double logSumRoot = logSum / tokenList.size();
		return Math.exp(-logSumRoot);
	}

}
