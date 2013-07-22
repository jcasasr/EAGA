package org.uoc.kison.EAGA.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

public class Params {
	// Singleton var
	private static Params INSTANCE = null;
	
	private final String propertiesFileName = "config.properties";
	private Properties prop;

	// POPULATION
	private String POPULATION_GENERATION_TYPE;
	private int POPULATION_NUM;

	// PARENT SELECTION MECHANISM

	// VARIATION OPERATORS (MUTATION)
	private int CHILDREN_NUM; // children's number for each invidual
	private String MUTATION_METHOD;
	private String MUTATION_RANDOM_NUMBER;

	// EVALUATION FUNCTION (FITNESS)
	private double EVALUATION_FUNCTION;
	private double EVALFUNC1_P1; // k value
	private double EVALFUNC1_P2; // nodes NK
	private double EVALFUNC1_P3; // distance to original sequence
	private double EVALFUNC1_P4; // distance to nearest neighbors

	// SURVIVOR SELECTION MECHANISM
	private String SURVIVOR_METHOD;
	private double SURVIVOR_STEADYSTATE_RANDOM;

	// TERMINATION CONDITION
	private double SOLUTION_MAX_ITER_NO_CHANGE; // number of iterations' %
	private double NO_SOLUTION_MAX_ITER_NO_CHANGE; // number of iterations' %
	
	
	/** Begin Singleton pattern */
	private synchronized static void createInstance() {
		if (INSTANCE == null) { 
			INSTANCE = new Params();
		}
	}

	public static Params getInstance() {
		createInstance();
		return INSTANCE;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}

	private Params(){
		prop = new Properties();
		try {
			//load a properties file
			prop.load(new FileInputStream(propertiesFileName));
			
			POPULATION_GENERATION_TYPE = prop.getProperty("POPULATION_GENERATION_TYPE");
			POPULATION_NUM = Integer.parseInt(prop.getProperty("POPULATION_NUM"));
			CHILDREN_NUM = Integer.parseInt(prop.getProperty("CHILDREN_NUM")); 
			MUTATION_METHOD = prop.getProperty("MUTATION_METHOD");
			MUTATION_RANDOM_NUMBER = prop.getProperty("MUTATION_RANDOM_NUMBER");
			EVALUATION_FUNCTION = Double.parseDouble(prop.getProperty("EVALUATION_FUNCTION"));
			EVALFUNC1_P1 = Double.parseDouble(prop.getProperty("EVALFUNC1_P1"));
			EVALFUNC1_P2 = Double.parseDouble(prop.getProperty("EVALFUNC1_P2"));
			EVALFUNC1_P3 = Double.parseDouble(prop.getProperty("EVALFUNC1_P3"));
			EVALFUNC1_P4 = Double.parseDouble(prop.getProperty("EVALFUNC1_P4"));
			SURVIVOR_METHOD = prop.getProperty("SURVIVOR_METHOD");
			SURVIVOR_STEADYSTATE_RANDOM = Double.parseDouble(prop.getProperty("SURVIVOR_STEADYSTATE_RANDOM"));
			SOLUTION_MAX_ITER_NO_CHANGE = Double.parseDouble(prop.getProperty("SOLUTION_MAX_ITER_NO_CHANGE"));
			NO_SOLUTION_MAX_ITER_NO_CHANGE = Double.parseDouble(prop.getProperty("NO_SOLUTION_MAX_ITER_NO_CHANGE"));

		} catch (FileNotFoundException ex) {
			loadDefaultValues();
			try{
				prop.store(new FileOutputStream(propertiesFileName), null);
			}catch (Exception e){
				e.printStackTrace();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/** End singleton pattern */
	
	private void loadDefaultValues(){
		POPULATION_GENERATION_TYPE = "equal";
		POPULATION_NUM = 500;
		CHILDREN_NUM = 10; 
		MUTATION_METHOD = "random";
		MUTATION_RANDOM_NUMBER = "fromOnetoHalf";
		EVALUATION_FUNCTION = 1;
		EVALFUNC1_P1 = 1;
		EVALFUNC1_P2 = 0.8;
		EVALFUNC1_P3 = 0.05;
		EVALFUNC1_P4 = 0.15;
		SURVIVOR_METHOD = "steadyState";
		SURVIVOR_STEADYSTATE_RANDOM = 0.7;
		SOLUTION_MAX_ITER_NO_CHANGE = 0.1;
		NO_SOLUTION_MAX_ITER_NO_CHANGE = 0.5;
		
		prop.setProperty("POPULATION_GENERATION_TYPE", POPULATION_GENERATION_TYPE);
		prop.setProperty("POPULATION_NUM", Integer.toString(POPULATION_NUM));
		prop.setProperty("CHILDREN_NUM", Integer.toString(CHILDREN_NUM));
		prop.setProperty("MUTATION_METHOD", MUTATION_METHOD);
		prop.setProperty("MUTATION_RANDOM_NUMBER", MUTATION_RANDOM_NUMBER);
		prop.setProperty("EVALUATION_FUNCTION", Double.toString(EVALUATION_FUNCTION));
		prop.setProperty("EVALFUNC1_P1", Double.toString(EVALFUNC1_P1));
		prop.setProperty("EVALFUNC1_P2", Double.toString(EVALFUNC1_P2));
		prop.setProperty("EVALFUNC1_P3", Double.toString(EVALFUNC1_P3));
		prop.setProperty("EVALFUNC1_P4", Double.toString(EVALFUNC1_P4));
		prop.setProperty("SURVIVOR_METHOD", SURVIVOR_METHOD);
		prop.setProperty("SURVIVOR_STEADYSTATE_RANDOM", Double.toString(SURVIVOR_STEADYSTATE_RANDOM));
		prop.setProperty("SOLUTION_MAX_ITER_NO_CHANGE", Double.toString(SOLUTION_MAX_ITER_NO_CHANGE));
		prop.setProperty("NO_SOLUTION_MAX_ITER_NO_CHANGE", Double.toString(NO_SOLUTION_MAX_ITER_NO_CHANGE));
		
	}

	public String getPOPULATION_GENERATION_TYPE() {
		return POPULATION_GENERATION_TYPE;
	}

	public void setPOPULATION_GENERATION_TYPE(
			String pOPULATION_GENERATION_TYPE) {
		POPULATION_GENERATION_TYPE = pOPULATION_GENERATION_TYPE;
	}

	public int getPOPULATION_NUM() {
		return POPULATION_NUM;
	}

	public void setPOPULATION_NUM(int pOPULATION_NUM) {
		POPULATION_NUM = pOPULATION_NUM;
	}

	public int getCHILDREN_NUM() {
		return CHILDREN_NUM;
	}

	public void setCHILDREN_NUM(int cHILDREN_NUM) {
		CHILDREN_NUM = cHILDREN_NUM;
	}

	public String getMUTATION_METHOD() {
		return MUTATION_METHOD;
	}

	public void setMUTATION_METHOD(String mUTATION_METHOD) {
		MUTATION_METHOD = mUTATION_METHOD;
	}

	public String getMUTATION_RANDOM_NUMBER() {
		return MUTATION_RANDOM_NUMBER;
	}

	public void setMUTATION_RANDOM_NUMBER(String mUTATION_RANDOM_NUMBER) {
		MUTATION_RANDOM_NUMBER = mUTATION_RANDOM_NUMBER;
	}

	public double getEVALUATION_FUNCTION() {
		return EVALUATION_FUNCTION;
	}

	public void setEVALUATION_FUNCTION(double eVALUATION_FUNCTION) {
		EVALUATION_FUNCTION = eVALUATION_FUNCTION;
	}

	public double getEVALFUNC1_P1() {
		return EVALFUNC1_P1;
	}

	public void setEVALFUNC1_P1(double eVALFUNC1_P1) {
		EVALFUNC1_P1 = eVALFUNC1_P1;
	}

	public double getEVALFUNC1_P2() {
		return EVALFUNC1_P2;
	}

	public void setEVALFUNC1_P2(double eVALFUNC1_P2) {
		EVALFUNC1_P2 = eVALFUNC1_P2;
	}

	public double getEVALFUNC1_P3() {
		return EVALFUNC1_P3;
	}

	public void setEVALFUNC1_P3(double eVALFUNC1_P3) {
		EVALFUNC1_P3 = eVALFUNC1_P3;
	}

	public double getEVALFUNC1_P4() {
		return EVALFUNC1_P4;
	}

	public void setEVALFUNC1_P4(double eVALFUNC1_P4) {
		EVALFUNC1_P4 = eVALFUNC1_P4;
	}

	public String getSURVIVOR_METHOD() {
		return SURVIVOR_METHOD;
	}

	public void setSURVIVOR_METHOD(String sURVIVOR_METHOD) {
		SURVIVOR_METHOD = sURVIVOR_METHOD;
	}

	public double getSURVIVOR_STEADYSTATE_RANDOM() {
		return SURVIVOR_STEADYSTATE_RANDOM;
	}

	public void setSURVIVOR_STEADYSTATE_RANDOM(
			double sURVIVOR_STEADYSTATE_RANDOM) {
		SURVIVOR_STEADYSTATE_RANDOM = sURVIVOR_STEADYSTATE_RANDOM;
	}

	public double getSOLUTION_MAX_ITER_NO_CHANGE() {
		return SOLUTION_MAX_ITER_NO_CHANGE;
	}

	public void setSOLUTION_MAX_ITER_NO_CHANGE(
			double sOLUTION_MAX_ITER_NO_CHANGE) {
		SOLUTION_MAX_ITER_NO_CHANGE = sOLUTION_MAX_ITER_NO_CHANGE;
	}

	public double getNO_SOLUTION_MAX_ITER_NO_CHANGE() {
		return NO_SOLUTION_MAX_ITER_NO_CHANGE;
	}

	public void setNO_SOLUTION_MAX_ITER_NO_CHANGE(
			double nO_SOLUTION_MAX_ITER_NO_CHANGE) {
		NO_SOLUTION_MAX_ITER_NO_CHANGE = nO_SOLUTION_MAX_ITER_NO_CHANGE;
	}
	
	
}
