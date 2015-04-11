package FeatureExtraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import Tagger.Bag;
import Tagger.Tagger;
import AprioriAlgorithm.AlgoApriori;
import AprioriAlgorithm.Itemset;
import AprioriAlgorithm.Itemsets;

public class FeatureExtracctor {

	private static List<List<Itemset>> levels;
	private static List<ArrayList<Bag>>  reviews;
	private static Converter converter;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//extract potential features
		String inputFile = "C:\\Users\\Jason\\Desktop\\grad 2\\data mining\\group\\data\\test";
		Tagger myTagger = new Tagger("C:\\Users\\Jason\\Desktop\\grad 2\\data mining\\group\\data\\test");
		reviews = myTagger.getReviews();
		List<List<String>> input = new ArrayList<List<String>> ();
		for(ArrayList<Bag> bags : reviews) {
			for(Bag bag : bags) {
				if (bag.features.size() != 0) {
					input.add(bag.features);
					//System.out.println(bag.features);
				}
			}
		}	
		
		/*
		//String inputFilePath = "testOutput.txt";
		
		
		
		//old implementation
		//String input = outputFile.getAbsolutePath();
		//create test input
		List<List<String>> input = new ArrayList<List<String>> ();
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line;
		// for each line (transactions) until the end of the file
		while (((line = reader.readLine()) != null)) { 
			// if the line is  a comment, is  empty or is a
			// kind of metadata
			if (line.isEmpty() == true ||
					line.charAt(0) == '#' || line.charAt(0) == '%'
							|| line.charAt(0) == '@') {
				continue;
			}
			// split the line according to spaces
			String[] lineSplited = line.split(",");
			
			List<String> temp = new ArrayList<String>();
			for (int i=0; i< lineSplited.length; i++) { 
				String item = lineSplited[i];
				if(item != null)
					temp.add(item);
			}
			input.add(temp);
		}
		
		//for (List<Integer> ii : input) {
		//	for (Integer iii : ii) {
		//		System.out.println(iii);
		//	}
		//	System.out.println("---------------");
		//}
		
		reader.close();
		*/
		
		converter = new Converter(input, inputFile);
		List<List<Integer>> outputList = converter.getOutputList();
		String output = null;
		
		double minsup = 0.01; // means a minsup of 2 transaction (we used a relative support)
		
		// Applying the Apriori algorithm
		AlgoApriori apriori = new AlgoApriori();
		Itemsets result = apriori.runAlgorithm(minsup, outputList, output, true);
		//apriori.printStats();
		//result.printItemsets(apriori.getDatabaseSize());
		
		System.out.println("--------------");
		int patternCount = 0;
		int levelCount = 0;
		levels = result.getLevels();
		
		//get the right order
		String targetSentence = "";
		
		//reoder the itemsets so that they appear in the right order
		reOrder();

		//print the result
		for (List<Itemset> level : levels) {
			// print how many items are contained in this level
			System.out.println("  L" + levelCount + " ");
			// for each itemset
			for (Itemset itemset : level) {
				//Arrays.sort(itemset.getItems());
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
				System.out.println(" ");
				
			}
			levelCount++;
		}
	}
	
	private static void reOrder() {
		int levelSize = levels.size();
		//System.out.println("------------------size is " + levelSize);
		for (int i = 2; i <= levelSize - 1; i++) {
			String sentence = "";
			List<Itemset> list = levels.get(i);
			for (int c = 0; c < list.size(); c++) {
			//for (Itemset itemset : levels.get(i)) {
				Itemset itemset = list.get(c);
				List<Pair<Integer, String>> words = new ArrayList<Pair<Integer, String>> ();
				
				searchloop:
				for(ArrayList<Bag> bags : reviews) {
					for(Bag bag : bags) {
						sentence = bag.getSentence().toLowerCase();
						words.clear();
						//System.out.println(sentence);
						for (int j = 0; j < itemset.size(); j++) {
							String word = converter.lookUpDictionary(itemset.get(j));
							int index = sentence.indexOf(word);
							Pair<Integer, String> pair = new Pair<Integer, String> (index, word);
							words.add(pair);
						}

						boolean containsAll = true;
						//Enumeration<Integer> enumKey = wordsTable.keys();
						//while(enumKey.hasMoreElements()) {
						//	Integer key = enumKey.nextElement();
						//	if (key >= 0) {
						//		containsAll = false;
						//		break;
						//	}
						//}
						for(Pair<Integer, String> pair : words) {
							if(pair.getLeft() == -1) {
								containsAll = false;
								break;
							}
						}
						
						if(containsAll) {
							List<Integer> indexes = new ArrayList<Integer> ();
							
							for (Pair<Integer, String> pair : words) {
								indexes.add(pair.getLeft());
							}
							
							Hashtable<Integer, String> wordsTable = new Hashtable<Integer, String> ();
							for(Pair<Integer, String> pair : words) {
								wordsTable.put(pair.getLeft(), pair.getRight());
							}
							
							Collections.sort(indexes);
							//itemset.clear();
							Itemset newItemset = new Itemset();
							int support = itemset.support;
							List<Integer> newItems = new ArrayList<Integer>();
							for(Integer ind : indexes) {
								int IntItem = converter.lookUpInverseDictionary(wordsTable.get(ind));
								newItems.add(IntItem);
							}
							
							itemset = new Itemset(newItems, support);
							
							//for(Integer index : indexes) {
							//	System.out.println("index is " + index);
							//	int IntItem = converter.lookUpInverseDictionary(wordsTable.get(index));
							//	newItemset.addItem(IntItem);
							//	
							//	itemset = new Itemset(newItemset, support);
							//}
							//itemset = newItemset;
							break searchloop;
						}
						
					}	
				}
				list.set(c, itemset);
			}
			levels.set(i, list);
		}
	}
}
