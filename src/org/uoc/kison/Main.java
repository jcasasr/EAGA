/**
 * @author adotorc
 */
package org.uoc.kison;

import java.util.Arrays;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.uoc.kison.EAGA.EAGA;
import org.uoc.kison.EAGA.utils.ImportGML;
import org.uoc.kison.EAGA.utils.Params;

public class Main {
	
	private static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		ImportGML gml = new ImportGML();
		UndirectedGraph<String, DefaultEdge> graph = gml.parseGML("/home/adotorc/football.gml");
		
		Params.getInstance();
		
		Set<String> vertex = graph.vertexSet();
		int[] degrees = new int[vertex.size()];
		int i=0;
		for(String v : vertex){
			degrees[i] = graph.degreeOf(v);
			i++;
		}
		
		EAGA eaga = new EAGA();
		eaga.AnonymizeDegreeSequence(degrees, 2);
		
	}

}
