package FeatureRating;

import java.util.HashMap;

/**
 * A summary matches each feature (key) to its:
 * rating (double)
 * opinions (i.e. which phrases were about the feature, how they were rated)
 * @author Antoine Sachet
 *
 */
@SuppressWarnings("serial")
public class Summary extends HashMap<String, Opinion> {

	public Summary() {
		super();
	}

	public void add(String feature, Sentiment sen) {
		if(this.containsKey(feature)){
			this.get(feature).add(sen);
		} else {
			Opinion op = new Opinion();
			op.add(sen);
			this.put(feature, op);
		}
	}
	
	public String toString() {
		String str = "";
		for(String feat: this.keySet()) {
			Opinion op = this.get(feat);
			str += ("### "+feat+" --> "+op.getMean()+" ###")+"\n";
			for(Sentiment sen: op){
				str += "- "+sen.getSentence()+"\n";
				str += "--> "+sen.getRating()+"\n";
			}
		}
		return(str);
	}
}