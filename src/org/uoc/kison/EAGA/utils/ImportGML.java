package org.uoc.kison.EAGA.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * @author adotorc
 * Parse ONLY basic data from a GML file, no labels/weight support
 */
public class ImportGML {
	UndirectedGraph<String, DefaultEdge> graph;
	String parsedFile;

	public UndirectedGraph<String, DefaultEdge> parseGML(String filename){
		parsedFile = readTextFile(filename);

		graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		
		parseNodes();
		parseEdges();
		
		return graph;
	}

	private void parseNodes(){
		Pattern genericNodePattern = Pattern.compile("node\\s*\\[((?!\\snode\\s*\\[|\\sedge\\s*\\[).)*\\s*\\]",Pattern.DOTALL);
		Matcher matcher = genericNodePattern.matcher(parsedFile);

		Pattern nodeIdPattern = Pattern.compile("id\\s+(\\w+)");
		Matcher nodeIdMatcher = nodeIdPattern.matcher("");
		while (matcher.find()) {
			nodeIdMatcher.reset(matcher.group());
			if (nodeIdMatcher.find()) {
				graph.addVertex(nodeIdMatcher.group(1));
			}
		}
	}

	private void parseEdges(){
		Pattern genericEdgePattern = Pattern.compile("edge\\s*\\[((?!\\snode\\s*\\[|\\sedge\\s*\\[).)*\\s*\\]",Pattern.DOTALL);
		Matcher matcher = genericEdgePattern.matcher(parsedFile);

		Pattern edgeSourcePattern = Pattern.compile("source\\s+(\\w+)");
		Matcher edgeSourceMatcher = edgeSourcePattern.matcher("");
		
		Pattern edgeTargetPattern = Pattern.compile("target\\s+(\\w+)");
		Matcher edgeTargetMatcher = edgeTargetPattern.matcher("");
		
		while (matcher.find()) {
			edgeSourceMatcher.reset(matcher.group());
			edgeTargetMatcher.reset(matcher.group());
			if(edgeSourceMatcher.find() && edgeTargetMatcher.find()){
				graph.addEdge(edgeSourceMatcher.group(1), edgeTargetMatcher.group(1));
			}
		}
	}
	
	private String readTextFile(String filename) {
		try {
			File file = new File(filename);
			FileInputStream fis = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			char[] text = new char[(int)file.length()];
			while(in.ready()==false) {}
			in.read(text);
			in.close();
			return new String(text);
		}catch (FileNotFoundException e) {
			return ""; 
		}catch (IOException e) {
			return ""; 
		}
	}

}
