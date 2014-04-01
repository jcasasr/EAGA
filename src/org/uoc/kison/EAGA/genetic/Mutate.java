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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Params;
import org.uoc.kison.EAGA.utils.Statistics;
import org.uoc.kison.EAGA.utils.Utils;

public class Mutate {
	private Params params;
	private Statistics stats;
	private Utils utils;
	
	/****************
	* EAGA_mutation *
	****************/
	public Mutate(){
		params = Params.getInstance();
		stats = Statistics.getInstance();
		utils = new Utils();
	}
	
	public Individual[] mutatePopulation(Individual[] population, int k) {
	    // timer
        long time_ini = System.currentTimeMillis();
	    
	    // pre-alloc using the maximum number of children
        Set<Individual> children = new HashSet<Individual>(params.getPOPULATION_NUM() * params.getCHILDREN_NUM());
	    Individual candidate = null;
	    
	    // generate descendants from actual population
	    for (int i=0;i<params.getPOPULATION_NUM();i++) {
	        for (int j=0;j<params.getCHILDREN_NUM();j++) {
	            // generate candidate
	            if(params.getMUTATION_METHOD().equalsIgnoreCase("random")) {
	                candidate = mutateIndividual_random(population[i], k);
	            }
	            
	           children.add(candidate);
	        }
	    }
	    
	    Individual[] childrenVector = new Individual[children.size()];
	    int i=0;
	    for(Individual child : children){
	    	childrenVector[i] = child;
	    	i++;
	    }
	    children.clear();
	    
	    // timer
	    stats.incrementTime_mutar(System.currentTimeMillis() - time_ini);
	    stats.incrementCalls_mutar(1);
	    
	    return childrenVector;
	}

	/** random mutation
	* -individual: individual
	* @return: mutated individual
	*/
	private Individual mutateIndividual_random(Individual individual, int k) {
        Random rand = new Random();
        // copy "d" array
        int[] d = Arrays.copyOf(individual.getD(), individual.getD().length);
	    
	    // number of mutations for each individual
	    int num = 0;
	    if(params.getMUTATION_RANDOM_NUMBER().equalsIgnoreCase("fromOnetoHalf")) {
	        num = rand.nextInt(Math.round(d.length/2));
	    }
	    
	    // apply 'num' random modificacions
	    //d = randomModifications(d, num);
        d = otherModifications(d, individual.getH(), k, num);
	    
	    // new candidate
	    Individual candidate = new Individual();
	    candidate.setD(d);
	    candidate.setH(utils.getDegreeHistogramFromDegreeSequence(d));
	    candidate.setK(-1);
	    candidate.setScore(-1);
	    
	    return candidate;
	}
        
        @SuppressWarnings("unused")
		private int[] randomModifications(int[] d, int num) {
            // apply 'num' random modificacions
            Random rand = new Random();
	    int n1; 
            int n2;
            
	    for(int i=0; i<num; i++) {
	        n1 = rand.nextInt(d.length);
	        if (!(d[n1] == 0)){
                    d[n1] = d[n1]-1;
                    n2 = rand.nextInt(d.length);
                    d[n2] = d[n2]+1;
	        }	        
	    }
            
            return d;
        }
        
        private int[] otherModifications(int[] d, int[] h, int k, int num) {
            Random rand = new Random();
            double pAddNode = 0.5;
            
            // select nodes to modify
            List<Integer> candidates = new ArrayList<Integer>();
            
            for(int i=0; i<d.length; i++) {
                int degree = d[i];
                int freq = h[degree];
                
                if(freq < k) {
                    // it does not satisfy the k-anonymity
                    candidates.add(i);
                } else {
                    // it does
                    if(rand.nextDouble() > (1-pAddNode)) {
                        candidates.add(i);
                    }
                }
            }
            
            // apply 'num' random modificacions
	    for(int i=0; i<num; i++) {
	        int n1 = candidates.get(rand.nextInt(candidates.size()));
                if (!(d[n1] == 0)){
                    // number of degrees
                    int deg = rand.nextInt(d[n1]);
                    int n2 = candidates.get(rand.nextInt(candidates.size()));
                    
                    if(d[n1]-deg <= 0) {
                        deg = d[n1]-1;
                    }
                    
                    d[n1] = d[n1] - deg;
                    d[n2] = d[n2] + deg;
	        }	        
	    }
            
            return d;
        }
}
