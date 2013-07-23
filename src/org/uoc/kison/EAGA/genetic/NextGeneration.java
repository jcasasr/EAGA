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
	    
	    // random % (maintain diversity)
	    Random rand = new Random();
	    while (count < populationNum) {
	        nextGeneration[count] = population[rand.nextInt(population.length)];
	        count++;
	    }    
	    
	    return nextGeneration;
	}
}
