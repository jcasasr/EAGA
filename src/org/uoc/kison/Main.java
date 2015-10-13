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
package org.uoc.kison;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uoc.kison.EAGA.EAGA;
import org.uoc.kison.EAGA.utils.UtilsGraph;
import org.uoc.kison.exporters.GmlExporter;
import org.uoc.kison.objects.SimpleIntGraph;
import org.uoc.kison.parsers.GmlParser;
import org.uoc.kison.parsers.TxtParser;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    private static final String version = "1.0";

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

        String inputFileName = null;
        String fullPath = null;
        String baseName = null;
        String extension = null;
        String outputFileName = null;
        int k = 1;

        if (args.length >= 2) {
            inputFileName = args[0];
            k = Integer.parseInt(args[1]);

            fullPath = FilenameUtils.getFullPath(inputFileName);
            baseName = FilenameUtils.getBaseName(inputFileName);
            extension = FilenameUtils.getExtension(inputFileName);
            

            logger.info("*************************************************************");
            logger.info("* EAGA - Evolutionary Algorithm for Graph Anonymization     *");
            logger.info("*                                                           *");
            logger.info("* Jordi Casas-Roma (jcasasr@uoc.edu)                        *");
            logger.info("* Alexandre Dotor-Casals (adotorc@uoc.edu)                  *");
            logger.info("* Universitat Oberta de Catalunya (www.uoc.edu)             *");
            logger.info("*************************************************************");
            logger.info("");
            logger.info(String.format("Version %s", version));
            logger.info(String.format("Input filname   : %s", inputFileName));
            logger.info(String.format("k value         : %d", k));
            logger.info("");
            logger.info("---------------------------------------------------------------");

        } else {
            System.out.println("EAGA Version " + version);
            System.out.println("Usage: java EAGA <input filename> <k value>");
            System.out.println("   <input filename>: GML, TXT");
            System.out.println("   <k value>: k-degree anonymity value");
            System.exit(-1);
        }

        // import GML 
        SimpleIntGraph graph = null;

        if (extension.compareToIgnoreCase("GML") == 0) {
            GmlParser gmlParser = new GmlParser();
            graph = gmlParser.parseFile(inputFileName);

        } else if (extension.compareToIgnoreCase("TXT") == 0) {
            TxtParser txtParser = new TxtParser();
            graph = txtParser.parseFile(inputFileName);

        } else {
            logger.error(String.format("Unknown filetype (extension %s)!", extension));
            System.exit(0);
        }

        // apply EAGA algorithm
        EAGA eaga = new EAGA();
        SimpleIntGraph gk = eaga.eaga(graph, k);
        
        UtilsGraph utilsGraph = new UtilsGraph();
        int real_k = utilsGraph.getKAnonymityValueFromGraph(gk);
        int ei = Math.round(utilsGraph.edgeIntersection(graph, gk) * 100 / graph.getNumEdges());
        
        if(real_k < k) {
            logger.error(String.format("ERROR: Obtained K value is smaller than desired! %s < %s", real_k, k));
            logger.error(String.format("Output file won't be written!"));

        } else {
            // export result to GML
            outputFileName = fullPath + baseName + "-k=" + real_k + "-EI="+ ei +"." + extension;
            logger.info(String.format("Saving anonymized graph to: %s", outputFileName));
            GmlExporter gmlExporter = new GmlExporter();
            gmlExporter.exportToFile(gk, outputFileName);
        }
    }
}
