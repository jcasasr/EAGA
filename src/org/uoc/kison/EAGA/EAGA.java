/**
 * @author adotorc
 */
package org.uoc.kison.EAGA;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.uoc.kison.EAGA.genetic.GeneratePopulation;
import org.uoc.kison.EAGA.genetic.Mutate;
import org.uoc.kison.EAGA.genetic.NextGeneration;
import org.uoc.kison.EAGA.genetic.Score;
import org.uoc.kison.EAGA.genetic.TerminationCondition;
import org.uoc.kison.EAGA.objects.Individual;
import org.uoc.kison.EAGA.utils.Statistics;
import org.uoc.kison.EAGA.utils.Params;
import org.uoc.kison.EAGA.utils.Utils;

/*******
 * EAGA *
 ********
 * Evolutionary Algorithm for Graph Anonymization
 */
public class EAGA {

    private static final Logger logger = Logger.getLogger(EAGA.class);
    private TerminationCondition terminationCondition;
    private GeneratePopulation generatePopulation;
    private Utils utils;
    private Score score;
    private Statistics stats;
    private Mutate mutate;
    private NextGeneration nextGen;

    public EAGA() {
        terminationCondition = new TerminationCondition();
        generatePopulation = new GeneratePopulation();
        utils = new Utils();
        score = new Score();
        stats = Statistics.getInstance();
        mutate = new Mutate();
        nextGen = new NextGeneration();
    }

    /** 
     * Anonymize degree sequence
     * -d0: original degree sequence
     * -k: desired k-value
     * @return: anonymized sequence
     */
    public Individual AnonymizeDegreeSequence(int[] d0, int k) {
        Individual[] population;
        // init
        stats.initTimeCounters();
        terminationCondition.initIteration();
        // timer
        long time_ini = System.currentTimeMillis();

        ////////////////////////////
        // Parameters //
        ////////////////////////////
        stats.showParameters(d0, k);

        //////////////////////////////////////////////////
        // Initialize population //
        //////////////////////////////////////////////////
        // crear individuo original
        Individual original = new Individual();
        original.setD(d0);
        original.setH(utils.getDegreeHistogramFromDegreeSequence(original.getD()));

        // generate population
        Params params = Params.getInstance();
        population = generatePopulation.genaratePopulation(original, params.getPOPULATION_GENERATION_TYPE(), params.getPOPULATION_NUM());

        //////////////////////////////////////////////
        // Evaluate population //
        //////////////////////////////////////////////
        population = score.evaluatePopulation(population, original, k);

        // add to iteration process
        terminationCondition.addIteration(population[0]);

        // Repeat until...
        Individual[] children;
        while (terminationCondition.doIteration(k)) {
            ////////////////////////////////////////////////////////
            // 1- Parents recombination //
            ////////////////////////////////////////////////////////

            // select parents

            // recombine pairs of parents

            ////////////////////////////////////////////////
            // 2- mutate population //
            ////////////////////////////////////////////////
            children = mutate.mutatePopulation(population);

            ////////////////////////////////////////////////////
            // 3- Evaluate population //
            ////////////////////////////////////////////////////
            children = score.evaluatePopulation(children, original, k);

            ////////////////////////////////////////////////////////
            // 4- Select new candidates //
            ////////////////////////////////////////////////////////
            // sort individuals
            Individual[] sortPopulation = Arrays.copyOf(population, population.length + children.length);
            System.arraycopy(children, 0, sortPopulation, population.length, children.length);
            population = nextGen.sortPopulation(sortPopulation);

            // seleccionar la siguiente generacion
            population = nextGen.getNextGeneration(population);

            // show best individual
            logger.info("It: " + terminationCondition.getNumberOfIterations() + "; K=" + population[0].getK() + "; Sc=" + population[0].getScore() + "; NC:" + terminationCondition.getNumIterNoChange());

            // add to iteration process
            terminationCondition.addIteration(population[0]);
        }

        // select the best candidate
        Individual bestCandidate = population[0];

        // timer

        stats.incrementTime_AnonymizeDegreeSequence(System.currentTimeMillis() - time_ini);
        stats.incrementCalls_AnonymizeDegreeSequence(1);
        stats.showTimeAnonymizeDegreeSequence();

        // log results
        int sumD = 0;
        int[] bestCandidateD = bestCandidate.getD();
        for (int i = 0; i < bestCandidateD.length; i++) {
            if (d0.length <= i) {
                sumD += Math.abs(bestCandidateD[i]);
            } else {
                sumD += Math.abs(d0[i] - bestCandidateD[i]);
            }
        }
        logger.info(String.format("Best candidate sequence (k=%d, score=%f, diffs=%d) [%s]", bestCandidate.getK(), bestCandidate.getScore(), sumD, Arrays.toString(bestCandidate.getD())));

        return bestCandidate;
    }
}
