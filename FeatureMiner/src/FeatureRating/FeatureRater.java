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
				for(String feat: features){
					if(sentence.contains(feat)) {
						summary.add(feat, pair);
					}
				}
			}
		}
		return summary;
	}

}

