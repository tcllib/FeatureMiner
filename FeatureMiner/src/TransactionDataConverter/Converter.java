package TransactionDataConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Hashtable;


public class Converter {
	private static Hashtable<Integer, String> dictionary = new Hashtable<Integer, String> ();
	private static Hashtable<String, Integer> inverseDictionary = new Hashtable<String, Integer> ();
	
	public static void main(String [] args) throws IOException {
		FileInputStream fstream = new FileInputStream("testInput.txt");
		File outputFile = new File("testOutput.txt");
		outputFile.createNewFile();
		FileWriter writer = new FileWriter(outputFile);
		
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
		    String line;
		    
		    //create a dictionary which maps each word to a index(Integer)
		    int count = 1;
		    while ((line = br.readLine()) != null) {
		    	String[] words = line.split(",");
		    	
		    	for(int i = 0; i < words.length; i++) {
		    		String word = words[i];
		    		
		    		if(dictionary.contains(word)) {
		    			//write the word with corresponding index
		    			int index = inverseDictionary.get(word);
		    			writer.write(index + " "); 
		    			writer.flush();
		    			continue;
		    		}
		    		
		    		dictionary.put(count, word);
		    		inverseDictionary.put(word, count);
		    		
		    		//substitute the word with corresponding index
		    		writer.write(count + " ");
		    		count++;
		    	}
		    
		    	writer.write("\n");
		    }
		}  
		
		catch (IOException e) {
            System.out.println("File I/O error!");
        }
		
		finally {
			writer.close();
		}
	} 

}
