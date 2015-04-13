import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import FeatureExtraction.FeatureExtractor;
import FeatureRating.FeatureRater;
import FeatureRating.Opinion;
import FeatureRating.SentimentAnalyser;
import FeatureRating.Summary;
import Tagger.Util;


public class FeatureMining {

	public static void main(String[] args) throws IOException {



		// directory containing our review database (json files) 
		String dataPath = "D:\\Sachou\\UCL\\FeatureMiner\\data\\small_laptop_data";

		// path to product reviews (several reviews relative to the same product, in a json file)
		String reviewPath = "D:\\Sachou\\UCL\\FeatureMiner\\data\\full_laptop_data\\B004EWEZKQ.json";

		// to print intermediate messages
		Boolean verbose = true;
		
		Summary summary = mineFeatures(reviewPath, dataPath, verbose);
		
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

	public static Summary mineFeature(String reviewPath, String dataPath) throws IOException {
		return(mineFeatures(reviewPath, dataPath, false));
	}
	public static Summary mineFeatures(String reviewPath, String dataPath, Boolean verbose) throws IOException {

		// extracting the possible features from the database
		FeatureExtractor extractor = new FeatureExtractor();
		ArrayList<String> features = extractor.extractFeatures(dataPath);
		//print features
		if(verbose)
			for (String feature : features) {
				System.out.println(feature);
			}

		ArrayList<String> commonNouns = readFile("frequent_nouns", 500);
		ArrayList<String> cleanFeatures = new ArrayList<String>();


		for(String feat: features){
			Boolean toRemove = false;
			for(String noun: commonNouns){
				//System.out.println(noun);
				if(feat.equals(noun)){
					toRemove=true;
					if(verbose)
						System.out.println("Feature \""+feat+"\" was filtered out as too common.");
				} else if (feat.endsWith("s") && feat.length()>1 && feat.substring(0, feat.length()-1).equals(noun)) {
					// testing to see if it was a plural of a noun to remove
					toRemove=true;
					if(verbose)
						System.out.println("Feature \""+feat+"\" was filtered out as too common.");
				}
			}
			if(!toRemove){
				cleanFeatures.add(feat);
			}
		}
		if(verbose)
			System.out.println((features.size()-cleanFeatures.size())+"/"+features.size()+" features were removed.");
		// loading up the reviews, each one in a string
		ArrayList<String> reviews = getRawReviews(reviewPath);

		if(verbose)
			for(String r: reviews){
				System.out.println(r+"\n\n");
			}
		// finding the opinion for each review
		// The opinion of a review is an arraylist of (sentence, sentiment)
		SentimentAnalyser analyser = new SentimentAnalyser();
		ArrayList<Opinion> opinions = analyser.findOpinion(reviews);

		// Finding the features rating given the opinions
		FeatureRater rater = new FeatureRater(cleanFeatures);
		Summary summary = rater.summarize(opinions);

		return(summary);


	}
}
