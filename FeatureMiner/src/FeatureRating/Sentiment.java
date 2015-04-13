package FeatureRating;

import FeatureExtraction.Pair;

public class Sentiment extends Pair<String, Integer> {

	public Sentiment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Sentiment(String left, Integer right) {
		super(left, right);
	}
	
	public String getSentence() {
		return(getLeft());
	}
	
	public int getRating() {
		return(getRight());
	}

	public String getStringRating() {
		return getRating()>2?"positive":"negative";
	}

}
