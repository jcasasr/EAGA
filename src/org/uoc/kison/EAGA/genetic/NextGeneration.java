package org.uoc.kison.EAGA.genetic;

import java.util.Arrays;
import java.util.Random;

import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Params;

public class NextGeneration {
	private Params params;
	
	public NextGeneration(){
		params = Params.getInstance();
	}
	
	public Individual[] sortPopulation(Individual[] population){
		Arrays.sort(population);
	    
	    return population;
	}
	 
	public Individual[] getNextGeneration(Individual[] population) {
	    
		Individual[] nextGeneration = null;
	    if(params.getSURVIVOR_METHOD().equalsIgnoreCase("steadyState")) {
	        nextGeneration = getSteadyStateModel(population);
	    }
	    
	    return nextGeneration;
	}

	// Steady-State Model for next generation selection
	private Individual[] getSteadyStateModel(Individual[] population) {
	    // pre-allocate space
		int populationNum = params.getPOPULATION_NUM();
	    Individual[] nextGeneration = new Individual[populationNum];
	    
	    // best candidates %
	    int count = (int) Math.floor(populationNum * (1 - params.getSURVIVOR_STEADYSTATE_RANDOM()));
	    for(int i=0;i<count;i++) nextGeneration[i] = population[i];
	    
	    // random % (mantain diversity)
	    Random rand = new Random();
	    while (count < populationNum) {
	        nextGeneration[count] = population[rand.nextInt(population.length)];
	        count++;
	    }    
	    
	    return nextGeneration;
	}
}
