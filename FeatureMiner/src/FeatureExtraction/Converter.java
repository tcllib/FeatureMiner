package FeatureExtraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;


public class Converter {
	private Hashtable<Integer, String> dictionary = new Hashtable<Integer, String> ();
	private Hashtable<String, Integer> inverseDictionary = new Hashtable<String, Integer> ();
	private FileInputStream fstream;
	private List<List<String>> inputLists;
	private List<List<Integer>> outputLists;
	
	public Converter( List<List<String>> input, String inputFilePath) throws FileNotFoundException {
		//fstream = new FileInputStream(inputFilePath);
		inputLists = input;
		outputLists = new ArrayList<List<Integer>>();
	}
	
	public Converter() {
		
	}
	
	public void ini( List<List<String>> input, String inputFilePath) throws FileNotFoundException {
		//fstream = new FileInputStream(inputFilePath);
		inputLists = input;
		outputLists = new ArrayList<List<Integer>>();
	}
	
	public List<List<Integer>> getOutputList() {
		
		int count = 1;
		for(List<String> words : inputLists) {
			List<Integer> contents = new ArrayList<Integer> ();
			
			for(int i = 0; i < words.size(); i++) {
	    		String word = words.get(i);
	    		
	    		if(dictionary.contains(word)) {
	    			//write the word with corresponding index
	    			int index = inverseDictionary.get(word);
	    			contents.add(index);
	    			continue;
	    		}
	    		
	    		dictionary.put(count, word);
	    		inverseDictionary.put(word, count);
	    		contents.add(count);
	    		
	    		//substitute the word with corresponding index
	    		count++;
	    	}
	    	
	    	Collections.sort(contents);
	    	outputLists.add(contents);
			
		}
		
		return outputLists;
	}
	
	public File getOutput() throws IOException {
		
		File outputFile = new File("testOutput.txt");
		outputFile.createNewFile();
		FileWriter writer = new FileWriter(outputFile);
		
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
		    String line;
		    
		    //create a dictionary which maps each word to a index(Integer)
		    int count = 1;
		    while ((line = br.readLine()) != null) {
		    	String[] words = line.split(",");
		    	List<Integer> contents = new ArrayList<Integer> ();
		    	//get the content of a line which is a list of ints
		    	for(int i = 0; i < words.length; i++) {
		    		String word = words[i];
		    		
		    		if(dictionary.contains(word)) {
		    			//write the word with corresponding index
		    			int index = inverseDictionary.get(word);
		    			contents.add(index);
		    			continue;
		    		}
		    		
		    		dictionary.put(count, word);
		    		inverseDictionary.put(word, count);
		    		contents.add(count);
		    		
		    		//substitute the word with corresponding index
		    		count++;
		    	}
		    	
		    	Collections.sort(contents);
		    	
		    	for(Integer num : contents) {
		    		writer.write(num + " ");
		    	}
		    	writer.write("\n");
		    	writer.flush();
		    }
		}  
		
		catch (IOException e) {
            System.out.println("File I/O error!");
        }
		
		finally {
			writer.close();
		}
		
		return outputFile;
	} 
	
	public String lookUpDictionary(int key) {
		return dictionary.get(key);
	}
	
	public int lookUpInverseDictionary(String key) {
		return inverseDictionary.get(key);
	}

}
