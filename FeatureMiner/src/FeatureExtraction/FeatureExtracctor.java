package FeatureExtraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import AprioriAlgorithm.AlgoApriori;
import AprioriAlgorithm.FrequentItemsFinder;
import AprioriAlgorithm.Itemset;
import AprioriAlgorithm.Itemsets;

public class FeatureExtracctor {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFilePath = "testInput.txt";
		Converter converter = new Converter(inputFilePath);
		File outputFile = converter.getOutput();
		
		String input = outputFile.getAbsolutePath();
		String output = null;
		
		double minsup = 0.4; // means a minsup of 2 transaction (we used a relative support)
		
		// Applying the Apriori algorithm
		AlgoApriori apriori = new AlgoApriori();
		Itemsets result = apriori.runAlgorithm(minsup, input, output);
		//apriori.printStats();
		//result.printItemsets(apriori.getDatabaseSize());
		
		
		System.out.println("--------------");
		int patternCount = 0;
		int levelCount = 0;
		List<List<Itemset>> levels = result.getLevels();
		for (List<Itemset> level : levels) {
			// print how many items are contained in this level
			System.out.println("  L" + levelCount + " ");
			// for each itemset
			for (Itemset itemset : level) {
				Arrays.sort(itemset.getItems());
				// print the itemset
				System.out.print("  pattern " + patternCount + ":  ");
				//itemset.print();
				int[] items = itemset.getItems();
				for(int i = 0; i < items.length; i++) {
					String word = converter.lookUpDictionary(items[i]);
					System.out.print(word + " ");
				}
				// print the support of this itemset
				System.out.print("support :  "
						+ itemset.getRelativeSupportAsString(apriori.getDatabaseSize()));
				patternCount++;
				System.out.println("");
			}
			levelCount++;
		}
	}
}
