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
package org.uoc.kison.EAGA.utils;

public class Utils {
	
	public int[] getDegreeHistogramFromDegreeSequence(int[] d) {
	    int max = d[0];
	    for(int i=1;i<d.length;i++) if (d[i] > max) max = d[i];
	    
	    int[] h = new int[max+1];	    
	    for (int i=0; i<d.length;i++) {
	    	h[d[i]]++;
	    }

	    return h;
	}
	
	/** Get k-anonymity value
	* -h: degree histogram
	* @return: k-anonymity value
	*/
	public int getKAnonymityValue(int[] h) {
		int min = -1;
		int i;
		for (i=0; i<h.length;i++){
			if (h[i] > 0){
				min = h[i];
				break;
			}
		}
		
		for (; i<h.length;i++) if (h[i]>0 && h[i] < min) min = h[i];
		
		return min;
	}
}
