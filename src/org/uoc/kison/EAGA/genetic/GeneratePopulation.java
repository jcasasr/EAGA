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

public class GeneratePopulation {
	
	/** Generate population
	* -type: method for population generation: 'equal'
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
