/*
 * Copyright 2013 Jordi Casas-Roma, Alexandre Dotor Casals
 * 
 * This file is part of UMGA. 
 * 
 * UMGA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * UMGA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with UMGA.  If not, see <http://www.gnu.org/licenses/>.
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
    private ArrayList<Integer> nodo_anadir_arista;
    private ArrayList<Integer> nodo_quitar_arista;

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
     * UMGA Step 2. Modification of graph structure in order to apply anonymized degree sequence
     */ 
    public SimpleIntGraph UMGA_recons() {
        
        logger.info("Starting graph reconstruction...");
        
        // data from original graph
        int[] d0 = utilsGraph.degree(g);

        // change's vector
        int[] vector_cambios = new int[dk.length];
        for (int i = 0; i < vector_cambios.length; i++) {
            vector_cambios[i] = dk[i] - d0[i];
        }
        logger.debug("UMGA_recons: vector_cambios: " + Arrays.toString(vector_cambios));

        int len_vector_cambios = vector_cambios.length;

        // vectors
        nodo_quitar_arista = new ArrayList<Integer>(len_vector_cambios);
        nodo_anadir_arista = new ArrayList<Integer>(len_vector_cambios);

        for (int i = 0; i < len_vector_cambios; i++) {
            if (vector_cambios[i] < 0) {
                for (int j = 0; j < Math.abs(vector_cambios[i]); j++) {
                    nodo_quitar_arista.add(i);
                }
            } else if (vector_cambios[i] > 0) {
                for (int j = 0; j < Math.abs(vector_cambios[i]); j++) {
                    nodo_anadir_arista.add(i);
                }
            }
        }

        nodo_anadir_arista.trimToSize();
        nodo_quitar_arista.trimToSize();

        logger.debug(String.format("UMGA_recons: nodo_quitar_arista (%d): %s", nodo_quitar_arista.size(), Arrays.toString(nodo_quitar_arista.toArray())));
        logger.debug(String.format("UMGA_recons: nodo_anadir_arista (%d): %s", nodo_anadir_arista.size(), Arrays.toString(nodo_anadir_arista.toArray())));

        // Reconstruct Graph from dk
        gk = reconstructGraph();

        logger.info(String.format("Original Graph  : %d nodes, %d edges and K=%d.", g.getNumNodes(), g.getNumEdges(), utilsGraph.getKAnonymityValueFromGraph(g)));
        logger.info(String.format("Anonimized Graph: %d nodes, %d edges and K=%d.", gk.getNumNodes(), gk.getNumEdges(), utilsGraph.getKAnonymityValueFromGraph(gk)));
        logger.info("Graph reconstruction done!");

        return gk;
    }

    public SimpleIntGraph reconstructGraph() {
        // Step 1
        // Reduce 'nodo_quitar_arista' if it is larger than 'nodo_anadir_arista'
        if (nodo_quitar_arista.size() > nodo_anadir_arista.size()) {
            deleteEdges();
        }

        // Step 2
        // Reduce 'nodo_anadir_arista' if it is larger than 'nodo_quitar_arista'
        if (nodo_quitar_arista.size() < nodo_anadir_arista.size()) {
            addEdges();
        }

        logger.debug(String.format("UMGA_recons: Step 3: Starting [numEdges=%d]", gk.getNumEdges()));

        // Step 3
        // Change edges to reduce/increase degree of nodes
        interchangeEdges();

        return gk;
    }

    private void deleteEdges() {

        // delete some edges
        while (nodo_quitar_arista.size() > nodo_anadir_arista.size()) {
            int node1 = nodo_quitar_arista.get(0);
            int node2 = nodo_quitar_arista.get(1);
            nodo_quitar_arista.subList(0, 2).clear();

            ArrayList<Integer> node1Neig = gk.getEdges(node1);
            ArrayList<Integer> node2Neig = gk.getEdges(node2);

            logger.debug(String.format("UMGA_recons: Step 1: deleting edges from nodes %d and %d", node1, node2));

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
                                logger.error(String.format("UMGA_recons: node [%d,%d] does not exist! It can not be deleted!", node1, a));
                            }
                            gk.deleteEdge(node1, a);
                            gk.deleteEdge(a, node1);
                            logger.debug(String.format("UMGA_recons: Step 1: deleting edge [%d,%d]", node1, a));
                            logger.debug("Num edges = " + gk.getNumEdges());

                            if (!gk.getEdges(node2).contains(b)) {
                                logger.error(String.format("UMGA_recons: node [%d,%d] does not exist! It can not be deleted!", node2, b));
                            }
                            gk.deleteEdge(node2, b);
                            gk.deleteEdge(b, node2);
                            logger.debug(String.format("UMGA_recons: Step 1: deleting edge [%d,%d]", node2, b));
                            logger.debug("Num edges = " + gk.getNumEdges());

                            gk.addEdge(a, b);
                            gk.addEdge(b, a);
                            newEdge = true;
                            logger.debug(String.format("UMGA_recons: Step 1: adding edge [%d,%d]", a, b));
                            logger.debug("Num edges = " + gk.getNumEdges());
                        }
                    }
                }
            }

            if (!newEdge) {
                // warning, no new edge has been created!
                logger.error("UMGA_recons: Step 1: NO NEW EDGE HAS BEEN CREATED!");
                break;
            }
        }
    }

    private void addEdges() {
        // add some edges        
        while (nodo_quitar_arista.size() < nodo_anadir_arista.size()) {
            boolean newEdge = false;
            int i = 0;

            while (i < nodo_anadir_arista.size() && !newEdge) {
                int j = i + 1;
                while (j < nodo_anadir_arista.size() && !newEdge) {
                    Integer source = nodo_anadir_arista.get(i);
                    Integer target = nodo_anadir_arista.get(j);

                    if ((source != target) && (!gk.getEdges(source).contains(target))) {
                        gk.addEdge(source, target);
                        gk.addEdge(target, source);
                        logger.debug(String.format("UMGA_recons: Step 2: adding edge [%d,%d]", source, target));
                        nodo_anadir_arista.remove(source);
                        nodo_anadir_arista.remove(target);

                        newEdge = true;
                    }
                    j++;
                }
                i++;
            }

            if (!newEdge) {
                // warning, no new edge has been created!
                logger.error("UMGA_recons: Step 2: NO NEW EDGE HAS BEEN CREATED!");
                break;
            }
        }
    }

    private void interchangeEdges() {

        int numEdges = gk.getNumEdges();

        while (nodo_quitar_arista.size() > 0) {
            logger.debug("UMGA_recons: nodo_quitar_arista " + nodo_quitar_arista.size());

            int nodeIni = nodo_quitar_arista.get(0);
            nodo_quitar_arista.remove(0);

            // neigbors nodes of 'nodeIni'
            ArrayList<Integer> nodeIniNeig = gk.getEdges(nodeIni);

            boolean newEdge = false;
            for (int i=0; i<nodeIniNeig.size();i++) {
            	int candidate = nodeIniNeig.get(i);
                int j = 0;
                while (j < nodo_anadir_arista.size() && !newEdge) {
                    Integer nodeEnd = nodo_anadir_arista.get(j);
                    
                    if (candidate != nodeEnd && (!gk.getEdges(nodeEnd).contains(candidate))) {
                        logger.debug(String.format("UMGA_recons: Step 3: edge change: (%d,%d) -> (%d,%d) ", nodeIni, candidate, nodeEnd, candidate));
                        gk.deleteEdge(nodeIni, candidate);
                        gk.deleteEdge(candidate, nodeIni);
                        gk.addEdge(nodeEnd, candidate);
                        gk.addEdge(candidate, nodeEnd);

                        nodo_anadir_arista.remove(nodeEnd);

                        newEdge = true;

                        // verification of number of edges
                        int numEdgesNow = gk.getNumEdges();
                        if (numEdges != numEdgesNow) {
                            logger.error(String.format("UMGA_recons: ERROR: number of edges has changed! [%d -> %d]", numEdges, numEdgesNow));
                        }
                    }
                    j = j + 1;
                }
            }
            
            if (!newEdge) {
                // warning, no new edge has been created!
                logger.error("UMGA_recons: Step 3: NO VALID EDGE SWITCH HAS BEEN FOUND!");
                break;
            }
        }
    }
}
