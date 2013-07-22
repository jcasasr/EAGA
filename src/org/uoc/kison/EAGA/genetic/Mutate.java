package org.uoc.kison.EAGA.genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Statistics;
import org.uoc.kison.EAGA.utils.Params;
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
	
	public Individual[] mutatePopulation(Individual[] population) {
	    // timer
		long time_ini = System.currentTimeMillis();
	    
	    // pre-alloc amb el nombre maxim de descendents
	    ArrayList<Individual> children = new ArrayList<Individual>(params.getPOPULATION_NUM() * params.getCHILDREN_NUM());
	    Individual candidate = null;
	    
	    // generar descendientes a partir de la poblacion actual
	    for (int i=0;i<params.getPOPULATION_NUM();i++) {
	        for (int j=0;j<params.getCHILDREN_NUM();j++) {
	            // generar candidato
	            if(params.getMUTATION_METHOD().equalsIgnoreCase("random")) {
	                candidate = mutateIndividual_random(population[i]);
	            }
	            
	            if(!children.contains(candidate)) children.add(candidate);
	        }
	    }
	    
	    // trim size and convert to array (faster to operate with)
	    children.trimToSize();
	    Individual[] childrenVector = new Individual[children.size()];
	    for(int i=0;i<children.size();i++) childrenVector[i] = children.get(i);
	    
	    // timer
	    stats.incrementTime_mutar(System.currentTimeMillis() - time_ini);
	    stats.incrementCalls_mutar(1);
	    
	    return childrenVector;
	}

	/** random mutation
	* -individual: individual
	* @return: mutated individual
	*/
	private Individual mutateIndividual_random(Individual individual) {	    
		int[] d = Arrays.copyOf(individual.getD(),individual.getD().length);
	    Random rand = new Random();
	    
	    // number of mutations for each individual
	    int num = 0;
	    if(params.getMUTATION_RANDOM_NUMBER().equalsIgnoreCase("fromOnetoHalf")) {
	        num = rand.nextInt(Math.round(d.length/2)); //sample(1:round(length(individual)/2), 1);
	    }
	    
	    // apply 'num' random modificacions
	    int n1; int n2;
	    for(int i=0;i<num;i++) {
	        n1 = rand.nextInt(d.length);
	        if (!(d[n1] == 0)){
	        	d[n1] = d[n1]-1;
	        	n2 = rand.nextInt(d.length);
		        d[n2] = d[n2]+1;
	        }	        
	    }
	    
	    // new candidate
	    Individual candidate = new Individual();
	    candidate.setD(d);
	    candidate.setH(utils.getDegreeHistogramFromDegreeSequence(d));
	    candidate.setK(-1);
	    candidate.setScore(-1);
	    
	    return candidate;
	}
}
