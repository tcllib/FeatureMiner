import java.io.IOException;

import FeatureRating.Summary;


public class Example {

	public static void main(String[] args) throws IOException {

		/* directory containing our review database (json files) 
		   we provide a small dataset of laptop reviews for this example
		   The features shown in the report were computed using the full available dataset
		*/
		String dataPath = "data/small_laptop_data";

		// path to product reviews (several reviews relative to the same product, in a json file)
		String reviewPath = "data/B00BC84HEE.json";

		// to print intermediate messages
		Boolean verbose = true;

		// the features are computed during initialization
		FeatureMining miner = new FeatureMining(dataPath);
		// one could also directly provide the arrayList of features

		// Computing the summary
		Summary summary1 = miner.mineFeatures(reviewPath, verbose);
		System.out.println(summary1);

		// computing another summary 
		// (the feature extraction has already been done)
		reviewPath = "D:\\Sachou\\UCL\\FeatureMiner\\data\\full_laptop_data\\B009AEPI90.json";
		Summary summary2 = miner.mineFeatures(reviewPath, !verbose);
		System.out.println(summary2);

	}
}

