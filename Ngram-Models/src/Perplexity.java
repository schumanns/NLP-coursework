import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

/**
 * Perplexity
 * 
 * This is a driver for the calculation of perplexity in the different kinds of models.
 * @author Leigh Schumann
 *
 */
public class Perplexity {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Testing the thing

		Scanner input = new Scanner(System.in);
		System.out.println("What is the name of the training file?");
		String trainName = input.nextLine();
		System.out.println("\nThanks!! What is the name of the test file?");
		String testName = input.nextLine();
		System.out.println("Good choices!\n");
		input.close();
		
		// Get the readers for the files
		InputStreamReader trainReader;
		InputStreamReader testReader;
		// Get the file name from the command line; if not present,
		// read from System.in.
		
		trainReader = new FileReader(new File(trainName));

		// Create a StringBuffer containing the entire contents
		// of the file
		StringBuffer train = new StringBuffer(50000);
		int x = trainReader.read();
		while(x!=-1){
			train.append((char)x);
			x = trainReader.read();
		}
		trainReader.close();

		testReader = new FileReader(new File(testName));

		// Create a StringBuffer containing the entire contents
		// of the file
		StringBuffer test = new StringBuffer(50000);
		int y = testReader.read();
		while(y!=-1){
			test.append((char)y);
			y = testReader.read();
		}
		testReader.close();


		Tokenizer trainTokenizer = new Tokenizer();
		List<String> trainList = trainTokenizer.tokenize(new String(train));

		Tokenizer testTokenizer = new Tokenizer();
		List<String> testList = testTokenizer.tokenize(new String(test));
		
		// ---------------------------------

		UnigramModel uniModel = new UnigramModel(trainList);
		BigramModel biModel = new BigramModel(trainList);
		TrigramModel triModel = new TrigramModel(trainList);
		QuadrigramModel quadriModel = new QuadrigramModel(trainList);
		
		System.out.println("The training data has " + trainList.size() + " tokens.");
		System.out.println("The test data has " + testList.size() + " tokens.");
		System.out.println();

		System.out.println("Unigram model:");
		System.out.println("The training data has " + uniModel.countMap.size() + " unigram types.");
		//System.out.println(uniModel.getProbability("since"));
		System.out.println("Perplexity: " + uniModel.getPerplexity(testList));
		System.out.println();

		System.out.println("Bigram model:");
		System.out.println("The training data has " + biModel.countMap.size() + " bigram types.");
		//System.out.println(biModel.getProbability("long", "since"));
		System.out.println("Perplexity: " + biModel.getPerplexity(testList));
		System.out.println();

		System.out.println("Trigram model:");
		System.out.println("The training data has " + triModel.countMap.size() + " trigram types.");
		//System.out.println(triModel.getProbability("so", "long", "since"));
		System.out.println("Perplexity: " + triModel.getPerplexity(testList));
		System.out.println();
		
		System.out.println("Quadrigram model:");
		System.out.println("The training data has " + quadriModel.countMap.size() + " quadrigram types.");
		//System.out.println(quadriModel.getProbability("is", "so", "long", "since"));
		System.out.println("Perplexity: " + quadriModel.getPerplexity(testList));
		System.out.println();

	}

}
