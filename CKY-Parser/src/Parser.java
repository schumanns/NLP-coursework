import java.util.*;
import java.io.*;

/**
 * Parser
 * 
 *  Class for a parser which implements the CKY algorithm.
 * It has a method which parses raw words 
 * and a method which parses words that are already tagged.
 * @author Leigh Schumann
 */
public class Parser {
	HashMap<String, LinkedList<LexRule>> lexRules;
	HashMap<Nonterminal, LinkedList<UnaryRule>> unaryRules;
	HashMap<TwoNonterminals, LinkedList<BinaryRule>> binaryRules;
	HashSet<Nonterminal> nonterminals;

	/**
	 * A constructor which builds HashMaps for the rules of the Probabalistic Context Free Grammar.
	 */
	public Parser() {

		RuleBuilder builder = new RuleBuilder();

		File lex = new File("lexicon.dat");
		LinkedList<String> rawLex = builder.getLinkedList(lex);
		this.lexRules = builder.getLexRules(rawLex);

		File unary = new File("unary.dat");
		LinkedList<String> rawUnary = builder.getLinkedList(unary);
		this.unaryRules = builder.getUnaryRules(rawUnary);

		File binary = new File("binary.dat");
		LinkedList<String> rawBinary = builder.getLinkedList(binary);
		this.binaryRules = builder.getBinaryRules(rawBinary);

		this.nonterminals = builder.getNonterminals();
	}

	/**
	 * An implementation of the CKY algorithm which parses untagged sentences.
	 * @param sentence a list of untagged words
	 * @return the most likely parse tree in the form of a BinaryTree
	 * @throws Exception if there are words in the sentence that are not in the lexicon
	 */
	public Tree parseRaw(List<String> sentence) throws Exception
	{
		//make a constant which stores the length of the sentence in words
		final int LEN = sentence.size();
		//create the CKY Table
		HashMap<String, Tree>[][] cykTable = new HashMap[LEN][LEN];

		//Iterate through and label all the words at the lowest level with lexical and unary rules
		for (int i = 0; i < LEN; i++)
		{
			//Get the word at this index for later use
			String word = sentence.get(i);

			//create a HashMap that will contain all the possibilities for the single word
			HashMap<String, Tree> parses = new HashMap<String, Tree>();

			//get the lexical rules for this word
			if (lexRules.containsKey(word)) //checking to make sure there's something for it!
			{
				for (LexRule lR : lexRules.get(word)) // go through all the LexRules to add the parses to the cell
				{
					//get the symbol the LexRule produces
					String tag = lR.lhs.symbol; 
					//make a node for the word
					Tree wordChild = new Tree(word, Math.log(1)); 
					//Make a Tree from the symbol and the prob
					Tree parse = new Tree(tag, lR.prob, wordChild); 

					parses.put(tag, parse); //add it to the cell
				}
			}
			else
				throw new Exception("Sorry, the raw parser does not handle unrecognized words!"
						+ " The unrecognized word is: " + word);

			//now lets get the UnaryRules
			findUnaryRules(parses);

			//And thus we have all the parses for the lowest level
			cykTable[i][i] = parses;
		}
		//Go on to the upper levels to fill in the higher levels of the table
		goThrough(LEN, cykTable);

		//Return the Binary tree that comes from the CKY Table
		return evaluateTable(LEN, cykTable);
	}

	/**
	 * An implementation of the CKY algorithm which parses tagged sentences.
	 * @param sentence a list of untagged words
	 * @return the most likely parse tree in the form of a BinaryTree
	 * @throws Exception 
	 */
	public Tree parseTagged(List<TaggedWord> sentence)
	{
		//make a constant which stores the length of the sentence in words
		final int LEN = sentence.size();
		//create the CYK Table
		HashMap<String, Tree>[][] cykTable = new HashMap[LEN][LEN];

		//Iterate through and label all the words at the lowest level with unary rules
		for (int i = 0; i < LEN; i++)
		{
			//create a new hashmap for this square
			HashMap<String, Tree> parses = new HashMap<String, Tree>();

			//get the word and its tag
			String word = sentence.get(i).getWord();
			String tag = sentence.get(i).getTag();
			//create a node for the word
			Tree wordChild = new Tree(word, Math.log(1));
			//and create a node for the tag
			Tree tagged = new Tree(tag, Math.log(1), wordChild);
			//add this completed little mini parse tree to the HashMap
			parses.put(tag, tagged);

			//now let's see if we can find UnaryRules for the these tagged puppies
			findUnaryRules(parses);

			//and put these parses into the table
			cykTable[i][i] = parses;
		}
		//Go on to the upper levels to fill in the higher levels of the table
		goThrough(LEN, cykTable);

		//Return the Binary tree that comes from the CKY Table
		return evaluateTable(LEN, cykTable);
	}

	/**
	 * A method which fills the table after the diagonal has already been filled
	 * @param LEN the length of the sentence
	 * @param cykTable the cyk table to filling, with the diagonal edge already filled
	 */
	private void goThrough(int LEN, HashMap<String, Tree>[][] cykTable) {
		// if the sentence is only one word long, this we don't need to be here.
		if (LEN == 1)
			return;

		//go through the columns (c should represent the columns)
		for (int c = 1; c < LEN; c++)
		{
			//go through the rows (r should represent the rows, the left index)
			for (int r = c-1; r >= 0; r--)
			{
				//square to fill: = cykTable[r][c]
				//fill the square with binary rules
				cykTable[r][c] = fillSquare(r,c, cykTable);
				//find the unary rules for that square
				findUnaryRules(cykTable[r][c]);
			}
		}

	}

	private HashMap<String, Tree> fillSquare(int r, int c, HashMap<String, Tree>[][] cykTable) {

		HashMap<String, Tree> parses = new HashMap<String, Tree>();

		//System.out.println("rows: " + r + " columns: "+ c); // print statement for testing

		//Get the BinaryRules!!
		//iterate through the row it's on
		for (int j = 0; j < c; j++)
		{
			//one of the squares we want to look at
			HashMap<String, Tree> rhs0Map = cykTable[r][j];
			//check to see if its not null
			if (rhs0Map != null && !rhs0Map.isEmpty())
			{
				//the appropriate corresponding square at the apropriate split
				HashMap<String, Tree> rhs1Map = cykTable[j+1][c];
				//check to see if that's not null
				if (rhs1Map != null && !rhs1Map.isEmpty())
				{
					//iterate through each of the possible rhs0s
					for (String rhs0 : rhs0Map.keySet())
					{
						// for each of the possible rhs1s
						for (String rhs1 : rhs1Map.keySet())
						{
							//create a new TwoNonterminals for the two of those rules
							TwoNonterminals rhs = new TwoNonterminals(rhs0, rhs1);
							//check to see if there is a binary rule for these two nonterminals
							if (binaryRules.containsKey(rhs))
							{
								//get all the Binary rules for this rhs
								LinkedList<BinaryRule> binRules = binaryRules.get(rhs);
								//iterate through them all
								for (BinaryRule bR : binRules)
								{
									//get the information we need to enter to create
									//a new parse tree for this unary rule
									//Create a string for the left hand side of the BinaryRule
									String LHSSym = bR.lhs.symbol;
									//get the parses of the constituents in the two squares
									Tree rhs0Tree = rhs0Map.get(rhs0);
									Tree rhs1Tree = rhs1Map.get(rhs1);
									//get the new probability of the new, higher level parse
									double newProb = bR.prob + rhs0Tree.probability + rhs1Tree.probability;
									//make a new tree with the two constituents as children
									Tree newTree = new Tree(LHSSym, newProb, rhs0Tree, rhs1Tree);

									//check if we already have this nonterminal in this square
									if (parses.containsKey(LHSSym))
									{
										//get the probability of the way of getting that nonterminal
										double oldProb = parses.get(LHSSym).probability;
										//check to see if its more or less than the old probability
										if (newProb > oldProb)
										{
											//if its more, then replace the old tree
											//with the new tree
											parses.put(LHSSym, newTree);
										}
									}
									else
									{
										// if not, just put it in the map
										parses.put(LHSSym, newTree);
									}
								}
							}
						}
					}
				}
			}
		}
		return parses;
	}

	/**
	 * A method which finds all the unary rules for a given square for which lexical or binary rules
	 * have already been found
	 * @param parses all the current parses for the square
	 */
	private void findUnaryRules(HashMap<String, Tree> parses) {

		boolean added = true; //this boolean determines whether we need to do another check
		while (added)
		{
			//make it false and then remember to make it true if we change it!
			added = false;

			//make an array out of the keys so that it doesn't get modified
			Object[] keysArray = parses.keySet().toArray();
			//take a look at all the nonterminals we've come up with
			for (Object key : keysArray)
			{
				//create a cast to make the key a String-type
				String constituentSym = (String) key;
				//make a nonterminal out of the String-form symbol
				Nonterminal constituent = new Nonterminal(constituentSym);
				//take a look to see if there are any UnaryRule-s for the nonterminal
				if (unaryRules.containsKey(constituent))
				{
					//look through all those UnaryRule-s for that constituent nonterminal
					for (UnaryRule unR : unaryRules.get(constituent))
					{
						//get the information we need to enter to create
						//a new parse tree for this unary rule
						//Create a string for the left hand side of the UnaryRule
						String LHSSym = unR.lhs.symbol;
						//get the parse of the constituent that these rules produce
						Tree constituentParse = parses.get(constituentSym);
						//get the probability of the constituent parse
						double newProb = unR.prob + constituentParse.probability;

						//if we've already come up with this rule in the parses
						if (parses.containsKey(LHSSym))
						{
							//if so, we'll have to see if this is more or less likely than the
							//previous parse tree that formed this LHS
							//get the details of the old way of getting this LHS.
							Tree oldTree = parses.get(LHSSym);
							double oldProb = oldTree.probability;
							//see if the probability of the new way is greater than the old way
							if (newProb > oldProb)
							{
								//if it is greater, replace the old way of getting that LHS
								//with the new way
								Tree newTree = new Tree(LHSSym, newProb, constituentParse);
								parses.put(LHSSym, newTree);
								added = true; // we added something!
							}
						}
						else
						{
							//create the new pase tree
							Tree parse = new Tree(LHSSym, newProb, constituentParse);
							//add it to the parses hashmap in the cell
							parses.put(LHSSym, parse);

							added = true; // we added something!!
						}
					}
				}
			}
		}
	}

	/**
	 * A method which returns the most likely parse tree of an evaluated parse Tree
	 * @param LEN the number of words in the parsed sentence
	 * @param cykTable the completely filled CYK table
	 * @return the most likely parse tree
	 */
	private Tree evaluateTable(int LEN, HashMap<String, Tree>[][] cykTable) {
		//let's find the most probable parse!
		//get the hashmap of the final square
		HashMap<String, Tree> fullParses = cykTable[0][LEN-1];

		double big = Double.NEGATIVE_INFINITY;
		Tree mostLikely = null;
		//iterate through all the possible parses
		for (String endSym : fullParses.keySet())
		{
			//make a local variable for the possible parse tree
			Tree possibleParse = fullParses.get(endSym);
			// let's see if we have a sentance
			if (possibleParse.symbol.equals("S"))
			{
				// compare that possible parse tree with the biggest we've seen so far
				if (possibleParse.probability > big)
				{
					//if so, make it the most likely and the biggest probability so far
					big = possibleParse.probability;
					mostLikely = possibleParse;
				}
			}
		}
		//we'll need to do some work if it still hasn't found a grammatical parse
		if (mostLikely == null)
		{
			String message = "Sorry, no grammatical parse found!";
			mostLikely = new Tree(message, 0);
		}

		return mostLikely;
	}

}
