package Tagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
 
public class Tag {
	public String analysis(String content){
 
		// Initialize the tagger
		MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
		// The sample string
		String sample =content;
		// The tagged string
		String tagged = tagger.tagString(sample);
		//output the tagged sample string onto your console

		return tagged;
	}
	
	public ArrayList<String> collect(String res){
		ArrayList<String> noun=new ArrayList();
		String[] sen=res.split(" ");
	
		for(int i=0;i<sen.length;i++){
			//		System.out.println("part:"+sen[i]);
			if(sen[i].endsWith("_NN")){
			noun.add(sen[i].substring(0,sen[i].length()-3).toLowerCase());
		}
	}
	
	return noun;
	}
}