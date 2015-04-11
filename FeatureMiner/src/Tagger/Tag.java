package Tagger;

import java.util.ArrayList;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
 
public class Tag {

	MaxentTagger tagger;

	public String analysis(String content){
 
		// The sample string
		String sample =content;
		// The tagged string
		String tagged = tagger.tagString(sample);
		//output the tagged sample string onto your console

		return tagged;
	}
	
	public ArrayList<String> collect(String res){
		ArrayList<String> noun=new ArrayList<String>();
		String[] sen=res.split(" ");
	
		for(int i=0;i<sen.length;i++){
			//		System.out.println("part:"+sen[i]);
			if(sen[i].endsWith("_NN")){ //singular noun
				noun.add(sen[i].substring(0,sen[i].length()-3).toLowerCase());
			} else if (sen[i].endsWith("_NNS")) { //plural noun
				noun.add(sen[i].substring(0,sen[i].length()-4).toLowerCase());
			}
	}
	return noun;
	}
	
	public Tag() {
		// Initialize the tagger
		this.tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");

	}
}