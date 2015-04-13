package FeatureRating;

import java.util.ArrayList;

public class FeatureRater {

	ArrayList<String> features;

	public FeatureRater(ArrayList<String> features) {
		super();
		this.features = features;
	}

	/**
	 * Matches features with sentences in opinions.
	 * Returns a summary, i.e. a HashMap matching features to opinion
	 * @param ops
	 * @return
	 */
	public Summary summarize(ArrayList<Opinion> ops) {
		String sentence;
		Summary summary = new Summary();
		for(Opinion op: ops){
			for(Sentiment pair: op) {
				sentence = pair.getLeft();
				Boolean hasFeature = false;
				for(String feat: features){
					// looking for feature "feat" as a whole word in the sentence
					if(sentence.matches(".*\\b"+feat+"\\b.*")) {
						summary.add(feat, pair);
						hasFeature=true;
					}
				}
				if(!hasFeature){
					summary.add("miscalleneous", pair);
				}
			}
		}
		return summary;
	}

}

