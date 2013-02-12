package org.uoc.kison.EAGA.utils;

import org.apache.log4j.Logger;

public class Log {
	// Singleton var
	private static Log INSTANCE = null;
	private final static Logger logger =  Logger.getLogger(Log.class);
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
            INSTANCE = new Log();
        }
    }
 
    public static Log getInstance() {
        createInstance();
        return INSTANCE;
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException(); 
    }
	
	private Log(){
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
}
