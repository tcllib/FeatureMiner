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
		ArrayList<String> noun=new ArrayList<String> ();
		String[] sen=res.split(" ");
	
		for(int i=0;i<sen.length;i++){
//			System.out.println("part:"+sen[i]);
			if(sen[i].endsWith("_NN")){
				noun.add(sen[i].substring(0,sen[i].length()-3));
			}
		}
	
		return noun;
	}

	public String trans(String result){
		String[] rec=result.split(" ");
		String fin="";
		String mid="";
		for(int i=0;i<rec.length;i++){
			if(rec[i].contains("_")){
				int index=rec[i].indexOf("_");

				mid=rec[i].substring(0,index);
				rec[i]=mid;
				rec[i]+=" ";
				if(rec[i].startsWith(",")){
					rec[i-1]=rec[i-1].substring(0, rec[i-1].length()-1);
				}
				if(rec[i].startsWith("n't")){
					rec[i-1]=rec[i-1].substring(0, rec[i-1].length()-1);
				}
				if(rec[i].startsWith("'s")){
					rec[i-1]=rec[i-1].substring(0, rec[i-1].length()-1);
				}
				if(rec[i].startsWith("'ve")){
					rec[i-1]=rec[i-1].substring(0, rec[i-1].length()-1);
				}
				if(rec[i].startsWith("'m")){
					rec[i-1]=rec[i-1].substring(0, rec[i-1].length()-1);
				}
				if(rec[i].startsWith("'ll")){
					rec[i-1]=rec[i-1].substring(0, rec[i-1].length()-1);
				}
		
			}
		}
		for(int y=0;y<rec.length;y++){
			fin+=rec[y];
		}
		if(!fin.isEmpty()){
			fin=fin.substring(0, fin.length()-1);
			fin+=".";
		}
		
	return fin;
	}
}