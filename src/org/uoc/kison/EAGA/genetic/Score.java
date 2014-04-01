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

import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Params;
import org.uoc.kison.EAGA.utils.Statistics;
import org.uoc.kison.EAGA.utils.Utils;
import org.uoc.kison.EAGA.utils.UtilsGraph;

/*************
* EAGA_score *
**************
* Functions related to individual score
*/
public class Score {
	private Utils utils;
        private UtilsGraph utilsGraph;
	private Params params;
	private Statistics stats;
	
	public Score(){
		utils = new Utils();
                utilsGraph = new UtilsGraph();
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
	        score = getScore(individual.getD(), individual.getH(), k, original.getD());
	    }
	    
	    return score;
	}
	
        // get number of nodes with k-anoninymity value betweenn 1,k-1
        private double getNoKAnonymity(int[] d, int[] h, int k) {
            int numNodes = d.length;
            // compute the number of nodes which k < desired k
            int numNodesNK = getNumberNodesNoKAnonymity(h, k);
            // compute the normalized value
            double numNodesNK_norm = (1 - (numNodesNK / numNodes));
            
            return numNodesNK_norm;
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

        /** Get individual's score
	* - d: degree sequence
	* - h: degree histogram
	* - k: desired k-value
	* - d0: original degree sequence
	* @return: float [ak, ak+1) where 'ak' is the k-anonymity value of 'd'
	*/ 
	private double getScore(int[] d, int[] h, int k, int[] d0) {
            double c1=0, c2=0, c3=0, c4=0;
            
	    // 1- k-anonymity value
	    int kActual = utils.getKAnonymityValue(h);
            
            if(kActual < k) {
                /**
                 * k < desired k
                 */
                // 2- number of nodes which are [1,k-1]-anonymity
                double numNodesNK_norm = getNoKAnonymity(d, h, k);
                c2 = params.getEVALFUNC1_P2() * numNodesNK_norm;

                // 4- grouping function
                //double dispersion = getDispersionScore(d);
                double dispersion = getDispersionScore(d, h, k);
                c4 = params.getEVALFUNC1_P4() * dispersion;
            } else {
                /**
                 * k >= desired k
                 * Only the distance to the original sequence is relevant
                 */
                // 1- k-anonymity value
                c1 = params.getEVALFUNC1_P1();
                
                // 3- distance to original sequence
                double distance = getDistanceFromOriginalSequence(d0, d);
                c3 = params.getEVALFUNC1_P3() * distance;
            }
            
	    // total
	    double total = c1 + c2 + c3 + c4;
	    
	    // return
	    return total;
	}
        
        private double getDistanceFromOriginalSequence(int[] d0, int[] d) {
            int sumd = 0;
            int distance = 0;
            
            for(int i=0; i<d0.length; i++) {
                distance += Math.abs(d0[i] - d[i]);
                sumd += d[i];
            }
            
            int maxDistance = sumd * 2;
            double distance_norm = 1 - ((double) distance / (double) maxDistance);
            
            return distance_norm;
        }
        
        /** Get a score based on dispersion
	* @param d: degree sequence
	* @return: (0,1] score
	*/
	private double getDispersionScore(int[] d, int[] h, int k) {
            // select the mean
            double mean = utilsGraph.getAverageDegree(d);

            // compute deviation from the mean
            double deviation = 0;
            for(int i=0; i<h.length; i++) {
                if(h[i] < k) {
                    // we only consider nodes of 0<k<desired k
                    int value = i;
                    int qtt = h[i];
                    deviation += Math.abs(mean - value) * qtt;
                }
            }
            
            // compute deviation^(-0.5)
            double punctuation = Math.pow(deviation, -0.5);

	    return(punctuation);
	}
}
