/*
 * Copyright 2013 Jordi Casas-Roma, Alexandre Dotor Casals
 * 
 * This file is part of EAGA. 
 * 
 * EAGA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EAGA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EAGA.  If not, see <http://www.gnu.org/licenses/>.
*/
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
