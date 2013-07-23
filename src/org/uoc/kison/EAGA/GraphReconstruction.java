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

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.uoc.kison.EAGA.utils.UtilsGraph;
import org.uoc.kison.objects.SimpleIntGraph;

public class GraphReconstruction {

    private static final Logger logger = Logger.getLogger(GraphReconstruction.class);
    UtilsGraph utilsGraph;
    private SimpleIntGraph g; // original graph
    private SimpleIntGraph gk; // anonymized graph
    private int[] dk;
    private ArrayList<Integer> addEdgeNodes;
    private ArrayList<Integer> removeEdgeNodes;

    /**
     * 
     * @param g: original graph
     * @param dk: k-degree anonymous sequence
     */
    public GraphReconstruction(SimpleIntGraph g, int[] dk) {
        this.utilsGraph = new UtilsGraph();
        
        this.g = utilsGraph.copyGraph(g);
        this.gk = utilsGraph.copyGraph(g);
        this.dk = dk;
    }
    
    /**
     * EAGA Step 2. Modification of graph structure in order to apply anonymized degree sequence
     */ 
    public SimpleIntGraph EAGA_recons() {
        
        logger.info("Starting graph reconstruction...");
        
        // data from original graph
        int[] d0 = utilsGraph.degree(g);

        // change's vector
        int[] changesVector = new int[dk.length];
        for (int i = 0; i < changesVector.length; i++) {
            changesVector[i] = dk[i] - d0[i];
        }
        logger.debug("EAGA_recons: changesVector: " + Arrays.toString(changesVector));

        int len_changesVector = changesVector.length;

        // vectors
        removeEdgeNodes = new ArrayList<Integer>(len_changesVector);
        addEdgeNodes = new ArrayList<Integer>(len_changesVector);

        for (int i = 0; i < len_changesVector; i++) {
            if (changesVector[i] < 0) {
                for (int j = 0; j < Math.abs(changesVector[i]); j++) {
                    removeEdgeNodes.add(i);
                }
            } else if (changesVector[i] > 0) {
                for (int j = 0; j < Math.abs(changesVector[i]); j++) {
                    addEdgeNodes.add(i);
                }
            }
        }

        addEdgeNodes.trimToSize();
        removeEdgeNodes.trimToSize();

        logger.debug(String.format("EAGA_recons: addEdgeNodes (%d): %s", removeEdgeNodes.size(), Arrays.toString(removeEdgeNodes.toArray())));
        logger.debug(String.format("EAGA_recons: removeEdgeNodes (%d): %s", addEdgeNodes.size(), Arrays.toString(addEdgeNodes.toArray())));

        // Reconstruct Graph from dk
        gk = reconstructGraph();

        logger.info(String.format("Original Graph  : %d nodes, %d edges and K=%d.", g.getNumNodes(), g.getNumEdges(), utilsGraph.getKAnonymityValueFromGraph(g)));
        logger.info(String.format("Anonimized Graph: %d nodes, %d edges and K=%d.", gk.getNumNodes(), gk.getNumEdges(), utilsGraph.getKAnonymityValueFromGraph(gk)));
        logger.info("Graph reconstruction done!");

        return gk;
    }

    public SimpleIntGraph reconstructGraph() {
        // Step 1
        // Reduce 'removeEdgeNodes' if it is larger than 'addEdgeNodes'
        if (removeEdgeNodes.size() > addEdgeNodes.size()) {
            deleteEdges();
        }

        // Step 2
        // Reduce 'addEdgeNodes' if it is larger than 'removeEdgeNodes'
        if (removeEdgeNodes.size() < addEdgeNodes.size()) {
            addEdges();
        }

        logger.debug(String.format("EAGA_recons: Step 3: Starting [numEdges=%d]", gk.getNumEdges()));

        // Step 3
        // Change edges to reduce/increase degree of nodes
        interchangeEdges();

        return gk;
    }

    private void deleteEdges() {

        // delete some edges
        while (removeEdgeNodes.size() > addEdgeNodes.size()) {
            int node1 = removeEdgeNodes.get(0);
            int node2 = removeEdgeNodes.get(1);
            removeEdgeNodes.subList(0, 2).clear();

            ArrayList<Integer> node1Neig = gk.getEdges(node1);
            ArrayList<Integer> node2Neig = gk.getEdges(node2);

            logger.debug(String.format("EAGA_recons: Step 1: deleting edges from nodes %d and %d", node1, node2));

            boolean newEdge = false;
            for (int i=0; i<node1Neig.size(); i++) {
            	int a = node1Neig.get(i);
                if (newEdge) {
                    break;
                }

                for (int j=0; j<node2Neig.size(); j++) {
                	int b = node2Neig.get(j);
                    if (newEdge) {
                        break;
                    }

                    if (a != b) {
                    	
                        if (!gk.getEdges(a).contains(b)) {
                            logger.debug("Num edges = " + gk.getNumEdges());
                            if (!gk.getEdges(node1).contains(a)) {
                                logger.error(String.format("EAGA_recons: node [%d,%d] does not exist! It can not be deleted!", node1, a));
                            }
                            gk.deleteEdge(node1, a);
                            gk.deleteEdge(a, node1);
                            logger.debug(String.format("EAGA_recons: Step 1: deleting edge [%d,%d]", node1, a));
                            logger.debug("Num edges = " + gk.getNumEdges());

                            if (!gk.getEdges(node2).contains(b)) {
                                logger.error(String.format("EAGA_recons: node [%d,%d] does not exist! It can not be deleted!", node2, b));
                            }
                            gk.deleteEdge(node2, b);
                            gk.deleteEdge(b, node2);
                            logger.debug(String.format("EAGA_recons: Step 1: deleting edge [%d,%d]", node2, b));
                            logger.debug("Num edges = " + gk.getNumEdges());

                            gk.addEdge(a, b);
                            gk.addEdge(b, a);
                            newEdge = true;
                            logger.debug(String.format("EAGA_recons: Step 1: adding edge [%d,%d]", a, b));
                            logger.debug("Num edges = " + gk.getNumEdges());
                        }
                    }
                }
            }

            if (!newEdge) {
                // warning, no new edge has been created!
                logger.error("EAGA_recons: Step 1: NO NEW EDGE HAS BEEN CREATED!");
                break;
            }
        }
    }

    private void addEdges() {
        // add some edges        
        while (removeEdgeNodes.size() < addEdgeNodes.size()) {
            boolean newEdge = false;
            int i = 0;

            while (i < addEdgeNodes.size() && !newEdge) {
                int j = i + 1;
                while (j < addEdgeNodes.size() && !newEdge) {
                    Integer source = addEdgeNodes.get(i);
                    Integer target = addEdgeNodes.get(j);

                    if ((source != target) && (!gk.getEdges(source).contains(target))) {
                        gk.addEdge(source, target);
                        gk.addEdge(target, source);
                        logger.debug(String.format("EAGA_recons: Step 2: adding edge [%d,%d]", source, target));
                        addEdgeNodes.remove(source);
                        addEdgeNodes.remove(target);

                        newEdge = true;
                    }
                    j++;
                }
                i++;
            }

            if (!newEdge) {
                // warning, no new edge has been created!
                logger.error("EAGA_recons: Step 2: NO NEW EDGE HAS BEEN CREATED!");
                break;
            }
        }
    }

    private void interchangeEdges() {

        int numEdges = gk.getNumEdges();

        while (removeEdgeNodes.size() > 0) {
            logger.debug("EAGA_recons: nodo_quitar_arista " + removeEdgeNodes.size());

            int nodeIni = removeEdgeNodes.get(0);
            removeEdgeNodes.remove(0);

            // neigbors nodes of 'nodeIni'
            ArrayList<Integer> nodeIniNeig = gk.getEdges(nodeIni);

            boolean newEdge = false;
            for (int i=0; i<nodeIniNeig.size();i++) {
            	int candidate = nodeIniNeig.get(i);
                int j = 0;
                while (j < addEdgeNodes.size() && !newEdge) {
                    Integer nodeEnd = addEdgeNodes.get(j);
                    
                    if (candidate != nodeEnd && (!gk.getEdges(nodeEnd).contains(candidate))) {
                        logger.debug(String.format("EAGA_recons: Step 3: edge change: (%d,%d) -> (%d,%d) ", nodeIni, candidate, nodeEnd, candidate));
                        gk.deleteEdge(nodeIni, candidate);
                        gk.deleteEdge(candidate, nodeIni);
                        gk.addEdge(nodeEnd, candidate);
                        gk.addEdge(candidate, nodeEnd);

                        addEdgeNodes.remove(nodeEnd);

                        newEdge = true;

                        // verification of number of edges
                        int numEdgesNow = gk.getNumEdges();
                        if (numEdges != numEdgesNow) {
                            logger.error(String.format("EAGA_recons: ERROR: number of edges has changed! [%d -> %d]", numEdges, numEdgesNow));
                        }
                    }
                    j = j + 1;
                }
            }
            
            if (!newEdge) {
                // warning, no new edge has been created!
                logger.error("EAGA_recons: Step 3: NO VALID EDGE SWITCH HAS BEEN FOUND!");
                break;
            }
        }
    }
}
