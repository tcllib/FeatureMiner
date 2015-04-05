package FeatureExtraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import AprioriAlgorithm.AlgoApriori;
import AprioriAlgorithm.FrequentItemsFinder;
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
		apriori.printStats();
		result.printItemsets(apriori.getDatabaseSize());
	}
}
