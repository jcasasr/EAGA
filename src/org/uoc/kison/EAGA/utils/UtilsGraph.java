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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.uoc.kison.objects.SimpleIntGraph;

public class UtilsGraph {

    private static final Logger logger = Logger.getLogger(UtilsGraph.class);

    public int[] degree(SimpleIntGraph graph) {
        int numNodes = graph.getNumNodes();
        int[] degrees = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            degrees[i] = graph.getEdges(i).size();
        }

        return degrees;
    }
    
    public int[] orderPermutation(final int[] d) {
        List<Integer> indices = new ArrayList<Integer>(d.length);
        for (int i = 0; i < d.length; i++) {
            indices.add(i);
        }
        Comparator<Integer> comparator = new Comparator<Integer>() {

            @Override
            public int compare(Integer i, Integer j) {
                Integer di = d[i];
                return di.compareTo(d[j]);
            }
        };
        Collections.sort(indices, comparator);

        int[] result = new int[indices.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = indices.get(i);
        }

        return result;
    }

    public int[] getDegreeHistogramFromDegreeSequence(int[] d) {
        int max = d[0];

        // find the max degree of all nodes
        for (int i = 1; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        // create an empty sequence with max+1 values [0..max]
        int[] h = new int[max + 1];
        for (int i = 0; i < d.length; i++) {
            h[d[i]]++;
        }

        return h;
    }

    /** Get k-anonymity value
     * -h: degree histogram
     * @return: k-anonymity value
     */
    public int getKAnonymityValueFromHistogram(int[] h) {
        int min = Integer.MAX_VALUE;
        int i;

        for (i = 0; i < h.length; i++) {
            if (h[i] > 0 && h[i] < min) {
                min = h[i];
            }
        }

        return min;
    }

    public int getKAnonymityValueFromDegreeSequence(int[] d) {
        return (getKAnonymityValueFromHistogram(getDegreeHistogramFromDegreeSequence(d)));
    }

    public int getKAnonymityValueFromGraph(SimpleIntGraph graph) {
        return (getKAnonymityValueFromDegreeSequence(degree(graph)));
    }

    public int edgeIntersection(SimpleIntGraph g1, SimpleIntGraph g2) {
        logger.debug("Starting edge intersection...");
        int numNodesG1 = g1.getNumNodes();
        int numNodesG2 = g2.getNumNodes();

        if (numNodesG1 != numNodesG2) {
            logger.error(String.format("edgeIntersection: ERROR: Different number of nodes G1=%d and G2=%d", numNodesG1, numNodesG2));

            return -1;
        } else {
            int numEdgesG1 = g1.getNumEdges();
            int numEdgesG2 = g2.getNumEdges();
            int total = Math.max(numEdgesG1, numEdgesG2);
            int inter = 0;

            for (int source = 0; source < numNodesG1; source++) {
                for (int target = 0; target < g1.getEdges(source).size(); target++) {
                    if (g2.getEdges(source).contains(g1.getEdges(source).get(target))) {
                        inter++;
                    }
                }
            }

            logger.info(String.format("Number of edges: G1=%d and G2=%d [%d edges]", numEdgesG1, numEdgesG2, (numEdgesG2 - numEdgesG1)));
            logger.info(String.format("Edge intersection(G1,G2) = %d/%d [%.2f %%]", inter, total, ((double) inter / total) * 100));

            logger.debug("Edge intersection done!");

            return inter;
        }
    }

    public SimpleIntGraph copyGraph(SimpleIntGraph g) {
        SimpleIntGraph clone = new SimpleIntGraph(g.getNumNodes());

        for (int source = 0; source < g.getNumNodes(); source++) {
            for (int j = 0; j < g.getEdges(source).size(); j++) {
                clone.addEdge(source, g.getEdges(source).get(j));
            }
        }

        return clone;
    }
    
    public double getAverageDegree(int[] d) {
        double v = 0;
        // sum all elements
        for(int i=0; i<d.length; i++) {
            v += d[i];
        }
        // divide by length
        v = v / d.length;
        
        return(v);
    }
}
