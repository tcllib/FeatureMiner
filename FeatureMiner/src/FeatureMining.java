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

/**
 * This is the main class to perform feature mining. 
 * The simplest way to summarize a review is shown in the main() method.
 * 
 * To summarize several reviews in a row, one should set the dataPath and call
 * mineFeature(reviewPath) only with the review path. Otherwise, the feature extraction will take place every time.
 * @author Antoine Sachet
 *
 */
public class FeatureMining {

	private ArrayList<String> features; 
	private SentimentAnalyser analyser = new SentimentAnalyser();

	public FeatureMining(String dataPath) throws IOException {
		// extracting the possible features from the database
		FeatureExtractor extractor = new FeatureExtractor();
		features = extractor.extractFeatures(dataPath);
	}

	public FeatureMining(ArrayList<String> features) throws IOException {
		this.features = features;
	}

	public FeatureMining() {
		this.features=null;
	}
	
	/**
	 *  Main method for this class: summarize a review given a database of similar reviews.
	 *  
	 * @param reviewPath:  the path to a single review to summarize
	 * @param verbose: boolean indicating whether to print intermediate messages
	 * @return A summary of the review (see FeatureRating.Summary)
	 * @throws IOException
	 */
	public Summary mineFeatures(String reviewPath, Boolean verbose) throws IOException {
		
		if(features==null){
			throw new IOException("The features were not initialized");
		}
		
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
		ArrayList<Opinion> opinions = analyser.findOpinion(reviews);

		// Finding the features rating given the opinions
		FeatureRater rater = new FeatureRater(cleanFeatures);
		Summary summary = rater.summarize(opinions);

		return(summary);
	}

	/**
	 *  Summarize a review given a database of similar reviews.
	 * @param reviewPath:  the path to a single review to summarize
	 * @param dataPath: the path to a directory containing reviews of the same category
	 * @param verbose: boolean indicating whether to print intermediate messages
	 * @return A summary of the review (see FeatureRating.Summary)
	 * @throws IOException
	 */
	public Summary mineFeatures(String reviewPath, String dataPath, Boolean verbose) throws IOException {

		// extracting the possible features from the database
		FeatureExtractor extractor = new FeatureExtractor();
		ArrayList<String> features = extractor.extractFeatures(dataPath);
		this.features=features;
		return(mineFeatures(reviewPath, verbose));
	}

	private static ArrayList<String> getRawReviews(String filePath) {
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

	private static ArrayList<String> readFile(String filename, int nLines) throws IOException
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
	
	public ArrayList<String> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<String> features) {
		this.features = features;
	}

	public void setAnalyser(SentimentAnalyser analyser) {
		this.analyser = analyser;
	}
}
