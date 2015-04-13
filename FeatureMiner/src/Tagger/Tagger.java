package Tagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Tagger {
	//static JSONObject reviews;
	private List<JSONObject> productReviews;
	private Tag tag;

	public Tagger(String filePath) throws FileNotFoundException {
		productReviews = new ArrayList<JSONObject> ();
		File dir = new File(filePath);

		if(!dir.exists()){
			throw new FileNotFoundException("Impossible to tag reviews: directory "+filePath+ " does not exist.");
		} else if (!dir.isDirectory()) {
			throw new FileNotFoundException("Impossible to tag reviews: "+filePath+" is not a directory.");
		}
		
		for (File child : dir.listFiles()) {
			if (".".equals(child.getName()) || "..".equals(child.getName())) {
				continue; // Ignore the self and parent aliases.
			}
			String str = new Util().ReadFile(child.toString());
			productReviews.add(JSONObject.fromObject(str));
		}

		this.tag = new Tag();
	}

	public ArrayList<ArrayList<Bag>> getReviews(){

		ArrayList<ArrayList<Bag>> pro=new ArrayList<ArrayList<Bag>>();
		for(JSONObject reviews : productReviews) {
			JSONArray array=reviews.getJSONArray("Reviews");
			int size = array.size();
			//System.out.println("Size: " + size);

			for(int  i = 0; i < size; i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				//System.out.println("[" + i + "]content=" + jsonObject.get("Content"));

				ArrayList<Bag> rev = new ArrayList<Bag>();
				String t1 = tag.analysis(jsonObject.get("Content").toString());
				//String t3 = jsonObject.get("Content").toString();
				String[] p1 = t1.split("\\.\\_\\.");

				for(int y=0;y<p1.length;y++){
					Bag b1=new Bag();
					b1.setID(y);
					//System.out.println(y);
					b1.setSentence(p1[y]);

					//System.out.println(p1[y]);
					b1.features=tag.collect(p1[y]);
					//System.out.println(tag.collect(p1[y]));
					rev.add(b1);
				}

				pro.add(rev);
			}
		}

		return pro;
	}
}