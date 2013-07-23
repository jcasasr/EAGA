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
package org.uoc.kison.EAGA.utils;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Statistics {
	// Singleton variable
	private static Statistics INSTANCE = null;
	private final static Logger logger =  Logger.getLogger(Statistics.class);
	private Utils utils;
		
	private long time_AnonymizeDegreeSequence;
	private long time_mutar;
	private long time_score;
	private int calls_AnonymizeDegreeSequence;
	private int calls_mutar;
	private int calls_score;
	
	/** Begin Singleton pattern */
    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new Statistics();
        }
    }
 
    public static Statistics getInstance() {
        createInstance();
        return INSTANCE;
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException(); 
    }
	
	private Statistics(){
		utils = new Utils();
	}
	/** End singleton pattern */
	
	// vars
	public void initTimeCounters() {
	    time_AnonymizeDegreeSequence = 0;
	    calls_AnonymizeDegreeSequence = 0;
	    time_mutar = 0;
	    calls_mutar = 0;
	    time_score = 0;
	    calls_score = 0;
	}
	
	public int incrementCalls_AnonymizeDegreeSequence(int increment){
		return calls_AnonymizeDegreeSequence += increment;
	}
	
	public int incrementCalls_mutar(int increment){
		return calls_mutar += increment;
	}
	
	public int incrementCalls_score(int increment){
		return calls_score += increment;
	}
	
	public long incrementTime_AnonymizeDegreeSequence(long increment){
		return time_AnonymizeDegreeSequence += increment;
	}
	
	public long incrementTime_mutar(long increment){
		return time_mutar += increment;
	}
	
	public long incrementTime_score(long increment){
		return time_score += increment;
	}
	
	public void showParameters(int[] d0, int k) {
		Params params = Params.getInstance();
	    logger.info("Original sequence k-anonymity value: "+ utils.getKAnonymityValue(utils.getDegreeHistogramFromDegreeSequence(d0)));
	    logger.info("Desired k-anonymity value:"+ k);
	    logger.info("Population method: "+ params.getPOPULATION_GENERATION_TYPE());
	    logger.info("Population number: "+ params.getPOPULATION_NUM());
	    logger.info("Children's number: "+ params.getCHILDREN_NUM());
	    if(params.getEVALUATION_FUNCTION() == 1) {
	        logger.info("Evaluation function (fitness): "+params.getEVALUATION_FUNCTION()+" [P1="+params.getEVALFUNC1_P1()+", P2="+
	        		params.getEVALFUNC1_P2()+", P3="+params.getEVALFUNC1_P3()+", P4="+params.getEVALFUNC1_P4()+"]");
	    } else {
	        logger.info("Evaluation function (fitness): "+ params.getEVALUATION_FUNCTION());
	    }
	    if(params.getMUTATION_METHOD().equalsIgnoreCase("random")) {
	        logger.info("Mutation method: "+params.getMUTATION_METHOD()+" [num. mutations="+params.getMUTATION_RANDOM_NUMBER()+"]");
	    } else {
	        logger.info("Mutation method: "+ params.getMUTATION_METHOD());
	    }
	    if(params.getSURVIVOR_METHOD().equalsIgnoreCase("steadyState")) {
	        logger.info("Survivor selection method: "+params.getSURVIVOR_METHOD()+" [random="+params.getSURVIVOR_STEADYSTATE_RANDOM()+"]");
	    } else {
	        logger.info("Survivor selection method: "+ params.getSURVIVOR_METHOD());
	    }
	    logger.info("Maximum number of iterations with solution: "+ params.getSOLUTION_MAX_ITER_NO_CHANGE());
	    logger.info("Maximum number of iterations with no solution: "+ params.getNO_SOLUTION_MAX_ITER_NO_CHANGE());
	}
	
	public void showTimeAnonymizeDegreeSequence() {
	    // time
	    printTimeFuncion(time_AnonymizeDegreeSequence, calls_AnonymizeDegreeSequence, "AnonymizeDegreeSequence", 1);
	    printTimeFuncion(time_mutar, calls_mutar, "mutatePopulation", 2);
	    printTimeFuncion(time_score, calls_score, "evaluatePopulation", 2);  
	}
	
	
	private void printTimeFuncion(long fTime, int calls, String functionName, int level) {		
	    
	    String sTime = getDurationBreakdown(fTime);
	    
	    String sCadena = "";
	    for (int i=0;i<level;i++){
	        if (i < level){
	            sCadena += " ";
	        }else{
	            sCadena +="+";  
	        }      
	    }
	    
	    logger.info(String.format("%s- %s: (%d calls) - %s",sCadena, functionName, calls, sTime));
	}
	
	private String getDurationBreakdown(long millis){
        if(millis < 0){
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return(sb.toString());
    }
}
