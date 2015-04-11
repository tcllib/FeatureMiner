package FeatureRating;

import java.util.ArrayList;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyser {
	 
	 Properties props;
	 StanfordCoreNLP pipeline;
     
	public SentimentAnalyser() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
	}
	
	/**
	 * Takes a review and returns the sentiment for each of its sentences (=opinion)
	 * @param review
	 * @return
	 */
    public Opinion findOpinion(String review) {
    	Opinion op = new Opinion();
        if (review != null && review.length() > 0) {
            Annotation annotation = pipeline.process(review);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                //System.out.println(partText+" : "+sentiment);
                op.add(partText, sentiment);
            }
        }
        return op;
    }
    
    /**
     * A simple wrapper to find multiple opinions at once
     * @param reviews
     * @return
     */
    public ArrayList<Opinion> findOpinion(ArrayList<String> reviews) {
    	ArrayList<Opinion> ops = new ArrayList<Opinion>();
		for(String rev: reviews){
			ops.add(findOpinion(rev));
		}
		return(ops);
    }
}