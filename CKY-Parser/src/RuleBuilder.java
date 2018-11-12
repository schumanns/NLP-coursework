import java.io.*;
import java.util.*;

/**
 * RuleBuilder
 * 
 * This class creates HashMaps which store the rules for our PCFG.
 * @author Leigh Schumann
 */
public class RuleBuilder {
	
	HashSet<Nonterminal> nonterminals;
	
	public RuleBuilder () {
		nonterminals = new HashSet<Nonterminal>();
	}

	/**
	 * A method that takes a file and converts it to a linked list
	 * @param file the file to be converted to a LinkedList
	 * @return a LinkedList with one node for each line
	 */
	public LinkedList<String> getLinkedList(File file) {

		LinkedList<String> ll = new LinkedList<String>();

		try {
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine())
			{
				ll.add(sc.nextLine());
			}
			sc.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Sorry, the file " + file.getName() + " was not found!");
			e.printStackTrace();
		}
		return ll;
	}

	/**
	 * A method for creating the HashMap of lexical rules.
	 * @param rawLex a linked list, with each node representing one probalistic lexical rule
	 * @return A HashMap mapping a String (the word) onto a LinkedList of all the lexical rules
	 * for that word.
	 */
	public HashMap<String, LinkedList<LexRule>> getLexRules(LinkedList<String> rawLex) {

		HashMap<String, LinkedList<LexRule>> lexR = new HashMap<String, LinkedList<LexRule>>();

		// iterate through each String-form rule, convert it to a LexRule, and map it onto the word
		// add the word if it is not already in the map, just map it in if so
		for (String s : rawLex)
		{
			//turn the String-form rule into a Array-of-Strings form
			String[] arrayRule = getArrayRule(s.split(" "));

			//declare a LexRule object and the Strings we'll need so we can create the object
			LexRule lex = null;
			Nonterminal nt = null;
			String word = null;
			double prob = 0;

			//iterate through each of the elements of rules
			// to create a LexRule
			for (int i = 0; i < arrayRule.length; i++)
			{
				switch(i)
				{
				case 0:
					String symbol = arrayRule[0];
					nt = new Nonterminal(symbol);
					nonterminals.add(nt);
					break;
				case 1:
					word = arrayRule[1];
					break;
				case 2:
					prob = Math.log(Double.parseDouble(arrayRule[2]));
					break;
				default:
					System.out.println("There is a lexical rule with a strange length:");
					System.out.println(Arrays.toString(arrayRule));
					break;
				}		
			}
			lex = new LexRule(nt, word, prob);
			//Add a new LinkedList if the word is not in the HashMap
			if (!lexR.containsKey(word))
				lexR.put(word, new LinkedList<LexRule>());

			//Add the new LexRule regardless
			lexR.get(word).add(lex);
		}
		//return the completed HashMap
		return lexR;
	}

	/**
	 * A method for creating the HashMap of unary rules.
	 * @param rawUnary a linked list, with each node representing one probalistic unary rule
	 * @return A HashMap mapping a Nonterminal onto a LinkedList of all the unary rules
	 * for that Nonterminal.
	 */
	public HashMap<Nonterminal, LinkedList<UnaryRule>> getUnaryRules(LinkedList<String> rawUnary) {
		// Iterate through each rule from the file
		// Go through each part of those rules in order to put them into the form of the UnaryRule data type
		// Map them to the words, or however you say that.

		//new HashMap to store the UnaryRules
		HashMap<Nonterminal, LinkedList<UnaryRule>> unaryRules = new HashMap<Nonterminal, LinkedList<UnaryRule>>();

		// iterate through each String-form rule, convert it to a UnaryRule, and map it onto the word
		// add the word if it is not already in the map, just map it in if so
		for (String s : rawUnary)
		{
			//turn the String-form rule into a Array-of-Strings form
			String[] arrayRule = getArrayRule(s.split(" "));

			//declare a LexRule object and the Strings we'll need so we can create the object
			UnaryRule unar = null;
			Nonterminal ntLeft = null;
			Nonterminal ntRight = null;
			double prob = 0;

			//iterate through the cleaned up arrayRule to find the appropriate stuff
			// to create a UnaryRule
			for (int i = 0; i < arrayRule.length; i++)
			{
				switch(i)
				{
				case 0:
					String symbol0 = arrayRule[0];
					ntLeft = new Nonterminal(symbol0);
					nonterminals.add(ntLeft);
					break;
				case 1:
					String symbol1 = arrayRule[1];
					ntRight = new Nonterminal(symbol1);
					nonterminals.add(ntRight);
					break;
				case 2:
					prob = Math.log(Double.parseDouble(arrayRule[2]));
					break;
				default:
					System.out.println("There is a unary rule with a strange length:");
					System.out.println(Arrays.toString(arrayRule));
					break;
				}		
			}
			unar = new UnaryRule(ntLeft, ntRight, prob);
			//Add a new LinkedList if the word is not in the HashMap
			if (!unaryRules.containsKey(ntRight))
				unaryRules.put(ntRight, new LinkedList<UnaryRule>());

			//Add the new UnaryRule regardless, since we just made sure the key has a LinkedList
			unaryRules.get(ntRight).add(unar);
		}
		return unaryRules;
	}

	/**
	 * A method for creating the HashMap of unary rules.
	 * @param rawBinary a linked list, with each node representing one probalistic binary rule
	 * @return A HashMap mapping a Nonterminal onto a LinkedList of all the binary rules
	 * for that Nonterminal.
	 */
	public HashMap<TwoNonterminals, LinkedList<BinaryRule>> getBinaryRules(LinkedList<String> rawBinary) {
		// Iterate through each rule from the file
		// Go through each part of those rules in order to put them into the form of the BinaryRule data type
		// Map them to the words, or however you say that.

		//Make a new HashMap to store the binary rules
		HashMap<TwoNonterminals, LinkedList<BinaryRule>> binaryRules = new HashMap<TwoNonterminals, LinkedList<BinaryRule>>();

		// iterate through each String-form rule, convert it to a BinaryRule, and map it onto the word
		// add the word if it is not already in the map, just map it in if so
		for (String s : rawBinary)
		{
			//turn the String-form rule into a Array-of-Strings form
			String[] arrayRule = getArrayRule(s.split(" "));

			//declare a LexRule object and the Strings we'll need so we can create the object
			BinaryRule binar = null;
			Nonterminal ntLeft = null;
			Nonterminal ntR0 = null;
			Nonterminal ntR1 = null;
			double prob = 0;

			//iterate through the cleaned up arrayRule to find the appropriate stuff
			// to create the new BinaryRule
			for (int i = 0; i < arrayRule.length; i++)
			{
				switch(i)
				{
				case 0:
					String symbol0 = arrayRule[0];
					ntLeft = new Nonterminal(symbol0);
					nonterminals.add(ntLeft);
					break;
				case 1:
					String symbol1 = arrayRule[1];
					ntR0 = new Nonterminal(symbol1);
					nonterminals.add(ntR0);
					break;
				case 2:
					String symbol2 = arrayRule[2];
					ntR1 = new Nonterminal(symbol2);
					nonterminals.add(ntR1);
					break;
				case 3:
					prob = Math.log(Double.parseDouble(arrayRule[3]));
					break;
				default:
					System.out.println("There is a binary rule with a strange length:");
					System.out.println(Arrays.toString(arrayRule));
					break;
				}		
			}
			binar = new BinaryRule(ntLeft, ntR0, ntR1, prob);
			//Add a new LinkedList if the word is not in the HashMap
			TwoNonterminals key = new TwoNonterminals(ntR0, ntR1);
			if (!binaryRules.containsKey(key))
				binaryRules.put(key, new LinkedList<BinaryRule>());

			//Add the new UnaryRule regardless, since we just made sure the key has a LinkedList
			binaryRules.get(key).add(binar);
		}
		return binaryRules;
	}

	/**
	 * A method which creates a single rule in the form of an Array
	 * @param str the raw rule in the form of an Array that came from splitting the String
	 * @return the rule in the form of an Array-of-Strings
	 */
	private String[] getArrayRule(String[] strAr)
	{
		//Make a new array to be the array rule, with a length one less than the raw array
		// because the arrow doesn't count
		String[] arrayRule = new String[strAr.length -1];
		//set the 0th element of the arrayRule equal to the 0th element of strAr, because that will be right
		arrayRule[0] = strAr[0];
		// Iterate through each of the Strings in the raw array and process them
		// into a arrayRule
		for (int i = 2; i < strAr.length; i++)
		{
			String item = strAr[i];
			//Strip it of unhelpful characters
			if (item.length() > 2 && (item.charAt(0) == "'".charAt(0) && item.charAt(item.length()-1) == "'".charAt(0)))
			{
				item = item.substring(1, item.length()-1);
			}
			if (item.charAt(item.length()-1) == '\n')
			{
				item = item.substring(0,item.length()-1);
			}
			else if (item.charAt(0) == '[' && item.charAt(strAr[i].length()-1) == ']')
			{
				item = item.substring(1, item.length()-1);
			}
			arrayRule[i-1] = item;
		}
		return arrayRule;
	}

	public HashSet<Nonterminal> getNonterminals() {
		return nonterminals;
	}

}