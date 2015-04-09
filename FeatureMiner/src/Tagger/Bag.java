package Tagger;

import java.util.ArrayList;


public class Bag {
	private int ID;
	public ArrayList<String> features;
	private String sentence;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	

}
