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
	
	public void addIteration(Individual bestCandidate) {
	    iterationHistory.add(bestCandidate.getScore());
	}
	
	private int getNumberOfIterations() {
	    return(iterationHistory.size());
	}
	
	public boolean doIteration(int k) {
	    if(iterationHistory.size() > 0) {
	        // number of iterations we did
	        int numberOfIterations = getNumberOfIterations();
	        // compute the number of iterations with the same best candidate
	        numIterNoChange = -1;
	        for(int i=0;i<iterationHistory.size();i++){
	        	if (iterationHistory.get(i) == iterationHistory.get(iterationHistory.size()-1)){
	        		numIterNoChange++;
	        	}
	        }
	        
	        if (iterationHistory.get(iterationHistory.size()) >= k) {
	            /***************************
	            * THERE IS A VALID SOLUTION
	            * define the max iterations without change in the best individual
	            * */
	            int solutionMaxIterNoChange = Math.max((int)Math.ceil(numberOfIterations * params.getSOLUTION_MAX_ITER_NO_CHANGE()), 2);

	            if(numIterNoChange >= solutionMaxIterNoChange) {
	                /******************
	                * STOP THE PROCESS
	                * last 'solutionMaxIterNoChange' values are equal
	                */
	                return false;
	            } else {
	                return true;
	            }
	        } else {
	           /***************************
	            * THERE IS NOT A SOLUTION
	            * define the max iterations without change in the best individual
	            */
	            int noSolutionMaxIterNoChange = Math.max((int)Math.ceil(numberOfIterations * params.getNO_SOLUTION_MAX_ITER_NO_CHANGE()), 2);
	            
	            if(numIterNoChange >= noSolutionMaxIterNoChange) {
	                /************************
	                * STOP THE PROCESS
	                * last 'noSolutionMaxIterNoChange' values are equal
	                */
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
