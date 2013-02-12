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
