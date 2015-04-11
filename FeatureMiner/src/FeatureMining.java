import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import FeatureExtraction.FeatureExtractor;
import FeatureRating.FeatureRater;
import FeatureRating.Opinion;
import FeatureRating.Sentiment;
import FeatureRating.SentimentAnalyser;
import FeatureRating.Summary;
import Tagger.Util;


public class FeatureMining {

	public static void main(String[] args) throws IOException {



		// directory containing our review database (json files) 
		String dataPath = "D:\\Sachou\\UCL\\FeatureMiner\\data\\small_laptop_data";

		// path to product reviews (several reviews relative to the same product, in a json file)
		String reviewPath = "D:\\Sachou\\UCL\\FeatureMiner\\data\\full_laptop_data\\B004O3BIBY.json";

		// extracting the possible features from our database
		FeatureExtractor extractor = new FeatureExtractor();
		ArrayList<String> features = extractor.extractFeatures(dataPath);

		ArrayList<String> commonNouns = readFile("frequent_nouns", 1000);
		ArrayList<String> cleanFeatures = new ArrayList<String>();
		
		
		for(String feat: features){
			Boolean toRemove = false;
			for(String noun: commonNouns){
				//System.out.println(noun);
				if(feat.equals(noun)){
					toRemove=true;
					System.out.println("Feature \""+feat+"\" was filtered out as too common.");
				}
			}
			if(!toRemove){
				cleanFeatures.add(feat);
			}
		}
		System.out.println((features.size()-cleanFeatures.size())+"/"+features.size()+" features were removed.");
		// loading up the reviews, each one in a string
		ArrayList<String> reviews = getRawReviews(reviewPath);

		// finding the opinion for each review
		// The opinion of a review is an arraylist of (sentence, sentiment)
		SentimentAnalyser analyser = new SentimentAnalyser();
		ArrayList<Opinion> opinions = analyser.findOpinion(reviews);

		// Finding the features rating given the opinions
		FeatureRater rater = new FeatureRater(cleanFeatures);
		Summary summary = rater.summarize(opinions);


		// Printing out the summary
		System.out.println(summary);

	}

	public static ArrayList<String> getRawReviews(String filePath) {
		String str = new Util().ReadFile(filePath);
		JSONObject raw = JSONObject.fromObject(str);
		ArrayList<String> reviews = new ArrayList<String>();
		JSONArray array=raw.getJSONArray("Reviews");
		int size = array.size();
		for(int  i = 0; i < size; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			String rev = jsonObject.get("Content").toString();
			reviews.add(rev);
		}
		return(reviews);
	}

	public static ArrayList<String> readFile(String filename, int nLines) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		ArrayList<String> lines = new ArrayList<String>();
		try {
			String line = br.readLine();
			int i=0;
			while (line != null && i<nLines) {
				lines.add(line.trim());
				i++;
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return(lines);
	}

}
