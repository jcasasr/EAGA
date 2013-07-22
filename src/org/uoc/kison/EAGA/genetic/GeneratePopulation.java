package org.uoc.kison.EAGA.genetic;

import org.uoc.kison.EAGA.objects.Individual;

public class GeneratePopulation {
	
	/** Generate population
	* -type: method for population genaration: 'equal'
	* -original: original 
	* -qtt: quantity
	*
	* @return: population
	*/
	public Individual[] genaratePopulation(Individual original, String type, int qtt) {
	    if(type.equalsIgnoreCase("equal")) {
	    	Individual[] population = generateEqualPopulation(original, qtt);
	    	return population;
	    }

	    return null;
	}

	// generate all individuals equal to the original one
	public Individual[] generateEqualPopulation(Individual original, int qtt) {
	    Individual[] population = new Individual[qtt];
	    
	    for (int i=0; i<qtt;i++) {
	        population[i] = original;
	    }   
	    
	    return population;
	}
}
