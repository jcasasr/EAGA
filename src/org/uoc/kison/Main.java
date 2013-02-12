/**
 * @author adotorc
 */
package org.uoc.kison;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uoc.kison.EAGA.utils.Utils;

public class Main {
	
	private static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		//ImportGML gml = new ImportGML();
		//UndirectedGraph<String, DefaultEdge> graph = gml.parseGML("/home/adotorc/football.gml");
		
		//Params.getInstance();
		
		int[] test = {2,3,5,8,9,0,4,1,2};
		Utils utils = new Utils();
		int[] degreeHist = utils.getDegreeHistogramFromDegreeSequence(test);
		System.out.println("K: "+utils.getKAnonymityValue(degreeHist));
		
	}

}
