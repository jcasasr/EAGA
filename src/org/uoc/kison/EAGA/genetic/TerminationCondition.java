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

import org.apache.log4j.Logger;
import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Params;

public class TerminationCondition {
	private static final Logger logger = Logger.getLogger(TerminationCondition.class);
	private Params params;
	private ArrayList<Double> iterationHistory;
	private int numIterNoChange;
	
	public void initIteration(){
		 iterationHistory = new ArrayList<Double>();
		 numIterNoChange = 0;
		 params = Params.getInstance();
	}
	
	public int getNumIterNoChange(){
		return numIterNoChange;
	}
	
	public void addIteration(Individual bestCandidate) {
	    iterationHistory.add(bestCandidate.getScore());
	}
	
	public int getNumberOfIterations() {
	    return(iterationHistory.size());
	}
	
	public boolean doIteration(int k) {
	    if(iterationHistory.size() > 0) {
	        // compute the number of iterations with the same best candidate
	        numIterNoChange = -1;
	        for(int i=0;i<iterationHistory.size();i++){
	        	if (iterationHistory.get(i).doubleValue() == iterationHistory.get(iterationHistory.size()-1).doubleValue()){
	        		numIterNoChange++;
	        	}
	        }
	        
	        if (iterationHistory.get(iterationHistory.size()-1) >= 1) { // K enlloc de 1
	            /**
	            * THERE IS A VALID SOLUTION
	            * define the max iterations without change in the best individual
	            **/
	            int solutionMaxIterNoChange = params.getSOLUTION_MAX_ITER_NO_CHANGE();

	            if(numIterNoChange >= solutionMaxIterNoChange) {
	                /**
	                * STOP THE PROCESS
	                * last 'solutionMaxIterNoChange' values are equal
	                **/
	                return false;
	            } else {
	                return true;
	            }
	        } else {
	           /**
	            * THERE IS NOT A SOLUTION
	            * define the max iterations without change in the best individual
	            **/
	            int noSolutionMaxIterNoChange = params.getNO_SOLUTION_MAX_ITER_NO_CHANGE();
	            
	            if(numIterNoChange >= noSolutionMaxIterNoChange) {
	                /**
	                * STOP THE PROCESS
	                * last 'noSolutionMaxIterNoChange' values are equal
	                **/
	                logger.warn("Process stopped without valid solution!");
	                
	                return false;
	            } else {
	                return true;
	            }
	        }
	    } else {
	        return true;
	    }
	}
	
}
