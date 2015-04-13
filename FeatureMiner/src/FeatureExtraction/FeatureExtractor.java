package FeatureExtraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import AprioriAlgorithm.AlgoApriori;
import AprioriAlgorithm.Itemset;
import AprioriAlgorithm.Itemsets;
import Tagger.Bag;
import Tagger.Tagger;

public class FeatureExtractor {

	private Converter converter;
	private List<List<Itemset>> levels;
	private List<ArrayList<Bag>> reviews; 
	
	public AlgoApriori getApriori() {
		return apriori;
	}

	public Converter getConverter() {
		return converter;
	}

	AlgoApriori apriori;
	
	public FeatureExtractor() {
		this.converter = new Converter();
		this.apriori = new AlgoApriori();
	}

	public static void main(String[] args) throws IOException {
		String inputPath2 = "C:\\Users\\Jason\\Desktop\\grad 2\\data mining\\group\\data\\test";
		
		FeatureExtractor extractor = new FeatureExtractor();
		Itemsets result = extractor.extract(inputPath2);
		
		System.out.println("--------------");
		int patternCount = 0;
		int levelCount = 0;
		List<List<Itemset>> levels = result.getLevels();
		//reOrder();
		//print the result
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
					String word = extractor.getConverter().lookUpDictionary(items[i]);
					System.out.print(word + " ");
				}
				// print the support of this itemset
				System.out.print("\t\tsupport :  "
						+ itemset.getRelativeSupportAsString(extractor.getApriori().getDatabaseSize()));
				patternCount++;
				System.out.println(" ");
				
			}
			levelCount++;
		}
	}
	
	public Itemsets extract(String inputFile) throws IOException {
		//extract potential features
				Tagger myTagger = new Tagger(inputFile);
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
				
				converter.ini(input, inputFile);
				List<List<Integer>> outputList = converter.getOutputList();
				String output = null;
				
				double minsup = 0.01; // means a minsup of 2 transaction (we used a relative support)
				
				// Applying the Apriori algorithm
				Itemsets result = apriori.runAlgorithm(minsup, outputList, output, true);
			
				//apriori.printStats();
				//result.printItemsets(apriori.getDatabaseSize());
				return(result);

	}
	
	public ArrayList<String> extractFeatures(String inputPath) throws IOException{

		ArrayList<String> features = new ArrayList<String>();
		Itemsets result = this.extract(inputPath);
		levels = result.getLevels();
		//reoder the itemsets so that they appear in the right order
		reOrder();
		
		for (List<Itemset> level : levels) {
			// print how many items are contained in this level
			// System.out.println("  L" + levelCount + " ");
			// for each itemset
			for (Itemset itemset : level) {
				// print the itemset
				// System.out.print("  pattern " + patternCount + ":  ");
				//itemset.print();
				int[] items = itemset.getItems();
				String word = "";
				for(int i = 0; i < items.length; i++) {
					if(i==0){
						word=converter.lookUpDictionary(items[i]);
					} else {
						word += " "+converter.lookUpDictionary(items[i]);						
					}
					//System.out.print(word + " ");
				}
				features.add(word);
				// print the support of this itemset
				//System.out.print("\t\tsupport :  "+ itemset.getRelativeSupportAsString(apriori.getDatabaseSize()));				
			}
		}
		return(features);
	}
	
	private void reOrder() {
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
