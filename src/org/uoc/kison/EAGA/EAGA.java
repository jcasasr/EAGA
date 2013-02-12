/**
 * @author adotorc
 */
package org.uoc.kison.EAGA;

import org.uoc.kison.EAGA.genetic.GeneratePopulation;
import org.uoc.kison.EAGA.genetic.Score;
import org.uoc.kison.EAGA.genetic.TerminationCondition;
import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Log;
import org.uoc.kison.EAGA.utils.Params;
import org.uoc.kison.EAGA.utils.Utils;

/*******
* EAGA *
********
* Evolutionary Algorithm for Graph Anonymization
*/
public class EAGA {
	
	private TerminationCondition terminationCondition;
	private GeneratePopulation generatePopulation;
	private Utils utils;
	private Score score;
	
	public EAGA(){
		terminationCondition = new TerminationCondition();
		generatePopulation = new GeneratePopulation();
		utils = new Utils();
		score = new Score();
	}
	
	/** 
	 * Anonymize degree sequence
	 * -d0: original degree sequence
	 * -k: desired k-value
	 * @return: anonymized sequence
	 */
	public void AnonymizeDegreeSequence(int[] d0, int k) {
		Individual[] population;
	    // init
		Log.getInstance().initTimeCounters();
		terminationCondition.initIteration();
	    // timer
	    long time_ini = System.currentTimeMillis();

	    ////////////////////////////
	    // Parameters //
	    ////////////////////////////
	    Log.getInstance().showParameters(d0, k);

	    //////////////////////////////////////////////////
	    // Initialize population //
	    //////////////////////////////////////////////////
	    // crear individuo original
	    Individual original = new Individual();
	    original.setD(d0);
	    original.setH(utils.getDegreeHistogramFromDegreeSequence(original.getD()));
	    
	    // generate population
	    Params params = Params.getInstance(); 
	    population = generatePopulation.genaratePopulation(original, params.getPOPULATION_GENERATION_TYPE(), params.getPOPULATION_NUM());
	  
	    //////////////////////////////////////////////
	    // Evaluate population //
	    //////////////////////////////////////////////
	    population = score.evaluatePopulation(population, original, k);
	    
	    // add to iteration process
	    terminationCondition.addIteration(population[0]);

	    // Repeat until...
	    while(terminationCondition.doIteration(k)) {
	        ////////////////////////////////////////////////////////
	        // 1- Parents recombination //
	        ////////////////////////////////////////////////////////
	        
	        // select parents
	        
	        // recombine pairs of parents
	        
	        ////////////////////////////////////////////////
	        // 2- mutate population //
	        ////////////////////////////////////////////////
//	        children = mutatePopulation(population);
	        
	        ////////////////////////////////////////////////////
	        // 3- Evaluate population //
	        ////////////////////////////////////////////////////
//	        children = evaluatePopulation(children, original, k);
	        
	        ////////////////////////////////////////////////////////
	        // 4- Select new candidates //
	        ////////////////////////////////////////////////////////
	        // sort individuals
//	        population = sortPopulation(c(population, children));
	        
	        // seleccionar la siguiente generacion
//	        population = getNextGeneration(population);
	        
	        // show best individual
//	        loginfo("It: %d; K=%d; Sc=%f; NC: %d", getNumberOfIterations(), population[[1]]$k, population[[1]]$score, numIterNoChange);
	        
	        // add to iteration process
//	        addIteration(population[[1]]);
	    }
	    
	    // select the best candidate
//	    bestCandidate = population[[1]];
	    
	    // timer
	    /*****TODO: GLOBAL!!!!!****/
//	    time_AnonymizeDegreeSequence += (System.currentTimeMillis() - time_ini);
//	    calls_AnonymizeDegreeSequence++;
//	    showTimeAnonymizeDegreeSequence();
	    
	    // log results
//	    loginfo("Best candidate sequence (k=%d, score=%f, diffs=%d) [%s]", bestCandidate$k, bestCandidate$score, sum(abs(d0-bestCandidate$d)), bestCandidate$d);
	    
	    // return
//	    return(bestCandidate);
	}	
}
