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
package org.uoc.kison.EAGA.genetic;

import java.util.ArrayList;

import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Statistics;
import org.uoc.kison.EAGA.utils.Params;
import org.uoc.kison.EAGA.utils.Utils;

/*************
* EAGA_score *
**************
* Functions related to individual score
*/
public class Score {
	private Utils utils;
	private Params params;
	private Statistics stats;
	
	public Score(){
		utils = new Utils();
		params = Params.getInstance();
		stats = Statistics.getInstance();
	}

	//evaluate entire population
	public Individual[] evaluatePopulation(Individual[] population, Individual original, int k) {
		// timer
	    long time_ini = System.currentTimeMillis();
	    
	    // evaluate all population
	    for(int i=0;i<population.length;i++) {
	        population[i].setK(utils.getKAnonymityValue(population[i].getH()));
	        population[i].setScore(evaluateIndividual(population[i], original, k));
	    }
	    
	    // timer
	    stats.incrementTime_score(System.currentTimeMillis() - time_ini);
	    stats.incrementCalls_score(1);
	    
	    return population;
	}
	
	// evaluate individual
	private double evaluateIndividual(Individual individual, Individual original, int k) {
		double score = -1;
		// get puntuation
	    if(params.getEVALUATION_FUNCTION() == 1) {
	        // Evaluation function #1
	        score = getScore1(individual.getD(), individual.getH(), k, original.getD());
	    }
	    
	    return score;
	}
	
	/** Get individual's score
	* - d: degree sequence
	* - h: degree histogram
	* - k: desired k-value
	* - d0: original degree sequence
	* @return: float [ak, ak+1) where 'ak' is the k-anonymity value of 'd'
	*/ 
	private double getScore1(int[] d, int[] h, int k, int[] d0) {
	    // 1- k-anonymity value
	    int kActual = utils.getKAnonymityValue(h);
	    double c1 = params.getEVALFUNC1_P1() * kActual;
	    
	    // 2- number of nodes which are [1,k-1]-anonymity
	    int numNodes = 0;
	    for(int i=0;i<h.length;i++) numNodes += h[i];
	    
	    int numNodesNK = getNumberNodesNoKAnonymity(h, k);
	    double numNodesNK_norm = (1 - (numNodesNK / numNodes));
	    double c2 = params.getEVALFUNC1_P2() * numNodesNK_norm;
	    
	    // 3- distance to original sequence
	    int sumd = 0;
	    for(int i=0;i<d.length;i++) sumd += d[i];
	    int maxDistance = sumd * 2;
	    int distance = 0;
	    for(int i=0;i<d0.length;i++) distance += Math.abs(d0[i] - d[i]);
	    double distance_norm = (1 - (distance / maxDistance));
	    double c3 = params.getEVALFUNC1_P3() * distance_norm;
	    
	    // 4- distance to nearest neighborhoods
	    double neigh = getNearestNeighborhoodPuntuation(h, k);
	    double c4 = params.getEVALFUNC1_P4() * neigh;
	    
	    // total
	    double total = c1 + c2 + c3 + c4;
	    
	    // return
	    return total;
	}
	
	/** Get number of nodes which don't agree k-anonymity
	* -h: degree histogram
	* -k: desired k-anonymity value
	* @return: integer [0, +Inf)
	*/ 
	private int getNumberNodesNoKAnonymity(int[] h, int k) {
		int num = 0;
	    for(int i=0;i<h.length;i++){
	    	if (h[i] > 0 && h[i] < k) num += h[i];
	    }
	    return num;
	}

	/** Get a score based on the isolation of NK nodes
	* Applies:
	* punctuation = SUM((1/di)/i) where i are the (1,k-1)-anonymity nodes
	* ** only counts NK nodes.
	* -h: degree histogram
	* -k: desired k-anonymity value
	* @return: (0,1] score
	*/
	private double getNearestNeighborhoodPuntuation(int[] h, int k) {
	    ArrayList<Integer> nodesNK = new ArrayList<Integer>();
		for (int i=0;i<h.length;i++) if (h[i]>0 && h[i]<k) nodesNK.add(i);
	    
		double punctuation = 0;
	    double distance;
	    for (int i=0;i<nodesNK.size();i++) {
	        distance = getDistanceToNearestNeigborhood(h, nodesNK.get(i));
	        punctuation += (1/distance);
	    }
	    
	    // normalize value (0, 1]
	    if(nodesNK.size() > 0) {
	        punctuation = punctuation / nodesNK.size();
	    }

	    return punctuation;
	}

	// Get a score based on the isolation of 1 node
	private double getDistanceToNearestNeigborhood(int[] h, int index) {
	    int distance = -1;
	    int i = 1;
	    int iMin = 0;
	    int iMax = h.length;
	    
	    while(distance < 0) {
	        if(index-i >= iMin) {
	            if(h[index-i]>0) {
	                distance = i;
	            }
	        } 
	        if(index+i < iMax) {
	            if(h[index+i]>0) {
	                distance = i;
	            }
	        }
	        i++;
	    }

	    return distance;
	}
}
