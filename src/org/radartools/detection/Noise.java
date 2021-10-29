package org.radartools.detection;

import org.apache.commons.math3.special.Gamma;

/**
 * Gaussian statistical model for electronic receiver noise
 */
public class Noise {

    /**
     * Should not create a Noise object
     */
    private Noise() {
    }

    /**
     * Determine detection threshold for a given probability of false alarm
     * and number of non-coherently integrated radar pulses
     * @param n number of non-coherently integrated radar pulses
     * @param pfa probability of false alarm
     * @return detection threshold
     */
    public static double thr( int n, Probability pfa ) {
        Solver solver = new Solver( (Double value) -> Gamma.regularizedGammaQ( n, value ) );
        return solver.solve(pfa.getValue(), -Math.log(pfa.getValue()) );
    }

    /**
     * Determine the probability of false alarm for a given detection threshold
     * and number of non-coherently integrated pulses
     * @param thr detection threshold
     * @param n number of non-coherently integrated radar pulses
     * @return probability of false alarm
     */
    public static Probability pfa( int n, double thr ) {
        Probability result = null;
        try {
            result = new Probability( Gamma.regularizedGammaQ(n,thr) );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }
}
