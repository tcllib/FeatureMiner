package FeatureExtraction;

public class Pair<T, K> {
	private T left;
	private K right;
	
	public Pair() {
		
	}
	
	public Pair(T left, K right) {
		this.left = left;
		this.right = right;
	}
	
	public T getLeft() {
		return this.left;
	}
	
	public K getRight() {
		return this.right;
	}
	
	public void setLeft(T l) {
		this.left = l;
	}
	
	public void setRight(K r) {
		this.right = r;
	}

}
