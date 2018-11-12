/**
 * A driver for parsing things with our CKY parser!
 * You can write the sentences you wish to parse in this file.
 * @author Leigh Schumann
 */

import java.util.*;

public class ParserApp {

	public static void main(String[] args) throws Exception {

		Parser p = new Parser();

		//Raw parsing
		//First sentence: "The man is living in Paris"
		//Second sentence: "I am an author"
		//third sentence: "We know where he is"
		//fourth sentence: "She flies in airplanes"

		String sSentence1 = "I like the successful business";
		List<String> sentence1 = Arrays.asList(sSentence1.split(" "));


		System.out.println("Final parse of the raw sentence:\n");
		System.out.println(p.parseRaw(sentence1));
		System.out.println("\n\n");

		//Tagged parsing
		//First sentence: "The/DT action/NN came/VBD in/IN response/NN to/TO a/DT petition/NN filed/VBN by/IN Timex/NNP Inc./NNP"
		//Second sentence: "Both/DT Newsweek/NNP and/CC U.S./NNP News/NNP have/VBP been/VBN gaining/VBG circulation/NN in/IN recent/JJ years/NNS"

		String sSentence2 = "Both/DT Newsweek/NNP and/CC U.S./NNP News/NNP have/VBP been/VBN gaining/VBG circulation/NN in/IN recent/JJ years/NNS";

		String[] wordsAndTags = sSentence2.split(" ");
		ArrayList<TaggedWord> sentence2 = new ArrayList<TaggedWord>(sSentence2.length());

		for (String wordTag : wordsAndTags)
		{
			String[] word_tag = wordTag.split("/");
			String word = word_tag[0];
			String tag = word_tag[1];

			TaggedWord tw = new TaggedWord(word, tag);
			sentence2.add(tw);
		}

		System.out.println("Final parse of the tagged sentence:\n");
		System.out.println(p.parseTagged(sentence2));
	}

}
