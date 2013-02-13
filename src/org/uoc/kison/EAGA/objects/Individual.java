package org.uoc.kison.EAGA.objects;

public class Individual implements Comparable<Individual>{
	private int[] d;
	private int[] h;
	private int k;
	private double score;	
	
	public int[] getD() {
		return d;
	}
	public void setD(int[] d) {
		this.d = d;
	}
	public int[] getH() {
		return h;
	}
	public void setH(int[] h) {
		this.h = h;
	}
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	@Override
	public int compareTo(Individual object) {
		if (this.score > object.score) return -1;
		else if (this.score < object.score) return 1;
		else return 0;
	}
	
}
