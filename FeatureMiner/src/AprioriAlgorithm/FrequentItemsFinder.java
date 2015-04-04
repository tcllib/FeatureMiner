package AprioriAlgorithm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;


/**
 * Example of how to use APRIORI algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class FrequentItemsFinder {

	public static void main(String [] arg) throws IOException{

		String input = fileToPath("testInput.txt");
		String output = ".//output.txt";  // the path for saving the frequent itemsets found
		
		double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)
		
		// Applying the Apriori algorithm
		AlgoApriori apriori = new AlgoApriori();
		apriori.runAlgorithm(minsup, input, output);
		apriori.printStats();
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = FrequentItemsFinder.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}