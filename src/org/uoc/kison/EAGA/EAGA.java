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
package org.uoc.kison.EAGA;

import org.apache.log4j.Logger;
import org.uoc.kison.EAGA.utils.ElapsedTime;
import org.uoc.kison.EAGA.utils.UtilsGraph;
import org.uoc.kison.objects.SimpleIntGraph;

///////////////////////////////////////////////////////
// Evolutionary Algorithms for Graph Anonymization  //
/////////////////////////////////////////////////////
public class EAGA {

    private static final Logger logger = Logger.getLogger(EAGA.class);
    UtilsGraph utilsGraph;

    public EAGA() {
        utilsGraph = new UtilsGraph();
    }

    /**
     * EAGA Algorithm
     */ 
    public SimpleIntGraph eaga(SimpleIntGraph g, int k) {

        ElapsedTime et = new ElapsedTime();
        
        logger.info(String.format("Original Graph: %d nodes, %d edges and K=%d.", g.getNumNodes(), g.getNumEdges(), utilsGraph.getKAnonymityValueFromGraph(g)));
        
        // degree sequence of G
        int[] d = utilsGraph.degree(g);

        /**
         * Step 1. anonymize the degree sequence
         */
        DegreeAnonimization da = new DegreeAnonimization();
        int[] dk = da.AnonymizeDegreeSequence(d, k);

        /**
         * Step 2. modify original graph to anonymize it
         */ 
        GraphReconstruction gr = new GraphReconstruction(g, dk);
        SimpleIntGraph gk = gr.EAGA_recons();

        logger.info("Anonimization process has finished!");
        logger.info("************************************************");
        // show results
        utilsGraph.edgeIntersection(g, gk);
        logger.info(String.format("Modified graph K=%d", utilsGraph.getKAnonymityValueFromDegreeSequence(utilsGraph.degree(gk))));

        et.stop();
        logger.info(String.format("Total running time: %s", et.getElapsedTime()));
        logger.info("************************************************");
        
        return gk;
    }
}
