package org.radartools.detection;

/**
 * Helper class for implementing the four Swerling<sup><b>1</b></sup> fluctuation models
 * using a generic {@link ChiSquare} fluctuation model.
 * <p>
 *     The Swerling fluctuation models are summarized as follows, where <i>N</i> is the number
 *     of non-coherently integrated pulses.
 *     <ul>
 *         <li>
 *             Swerling 1
 *             <ul>
 *                 <li><i>1</i> Chi-Square degrees of freedom</li>
 *                 <li>Target has a large number of independent scatterers</li>
 *                 <li>Radar pulses are fully correlated</li>
 *             </ul>
 *         </li>
 *         <li>
 *             Swerling 2
 *             <ul>
 *                 <li><i>N</i> Chi-Square degrees of freedom</li>
 *                 <li>Target has a large number of independent scatterers</li>
 *                 <li>Radar pulses are fully de-correlated</li>
 *             </ul>
 *         </li>
 *         <li>
 *             Swerling 3
 *             <ul>
 *                 <li><i>2</i> Chi-Square degrees of freedom</li>
 *                 <li>Target has a single dominant scatterer plus smaller independent scatterers</li>
 *                 <li>Radar pulses are fully correlated</li>
 *             </ul>
 *         </li>
 *         <li>
 *             Swerling 4
 *             <ul>
 *                 <li><i>2*N</i> Chi-Square degrees of freedom</li>
 *                 <li>Target has a single dominant scatterer plus smaller independent scatterers</li>
 *                 <li>Radar pulses are fully de-correlated</li>
 *             </ul>
 *         </li>
 *     </ul>
 * <p>
 *     <sup><b>1</b></sup>Swerling, P., "Probability of Detection for Fluctuating Targets,"
 *     U.S. Air Force Project RAND Research Memorandum RM-1217, ASTIA Document Number AD 80638,
 *     17 March 1954.
 * </p>
 */
public class Swerling {

    private final ChiSquare chiSquareModel;
    private final Case sw_case;

    /**
     * Enumeration of the four Swerling fluctuation cases.
     */
    public enum Case {
        /**
         * Swerling Case 1
         */
        Swerling_1,
        /**
         * Swerling Case 2
         */
        Swerling_2,
        /**
         * Swerling Case 3
         */
        Swerling_3,
        /**
         * Swerling Case 4
         */
        Swerling_4
    }

    /**
     * Constructor where the signal-to-noise ratio and the detection threshold are given
     * @param snr average signal-to-noise ratio per non-coherently integrated pulse
     * @param thr detection threshold
     * @param n number of non-coherently integrated pulses
     * @param sw_case Swerling fluctuation case
     */
    public Swerling( double snr, double thr, int n, Case sw_case ) {
        this.sw_case = sw_case;
        chiSquareModel = new ChiSquare( snr, thr, n, getK(n,sw_case) );
    }

    /**
     * Constructor where the probability of detection and the detection threshold are given
     * @param pd desired probability of detection for the non-coherently integrated pulses
     * @param thr detection threshold
     * @param n number of non-coherently integrated pulses
     * @param sw_case Swerling fluctuation case
     */
    public Swerling( Probability pd, double thr, int n, Case sw_case ) {
        this.sw_case = sw_case;
        chiSquareModel = new ChiSquare( pd, thr, n, getK(n,sw_case) );
    }

    /**
     * Constructor where the signal-to-noise ratio and the probability of false alarm are given
     * @param snr average signal-to-noise ratio per non-coherently integrated pulse
     * @param pfa desired probability of false alarm
     * @param n number of non-coherently integrated pulses
     * @param sw_case Swerling fluctuation case
     */
    public Swerling( double snr, Probability pfa, int n, Case sw_case ) {
        this.sw_case = sw_case;
        chiSquareModel = new ChiSquare( snr, pfa, n, getK(n,sw_case) );
    }

    /**
     * Constructor where the probability of detection and the probability of false alarm are given
     * @param pd desired probability of detection for the non-coherently integrated pulses
     * @param pfa desired probability of false alarm
     * @param n number of non-coherently integrated pulses
     * @param sw_case Swerling fluctuation case
     */
    public Swerling( Probability pd, Probability pfa, int n, Case sw_case ) {
        this.sw_case = sw_case;
        chiSquareModel = new ChiSquare( pd, pfa, n, getK(n,sw_case) );
    }

    /**
     * Set the Chi Square degrees of freedom for the give Swerling case.
     * @param n number of non-coherently integrated pulses
     * @param sw_case Swerling case
     * @return corresponding chi-square degrees of freedom
     */
    private int getK( int n, Case sw_case ) {
        int result = 0;
        switch ( sw_case ) {
            case Swerling_1 -> result = 1;
            case Swerling_2 -> result = n;
            case Swerling_3 -> result = 2;
            case Swerling_4 -> result = 2*n;
        }
        return result;
    }

    /**
     * Getter for given or calculated signal-to-noise ratio for this Swerling case model.
     * @return signal-to-noise ratio
     */
    public double getSnr() { return chiSquareModel.getSnr(); }

    /**
     * Getter for given or calculated probability of detection for this Swerling case model.
     * @return probability of detection
     */
    public Probability getPd() { return chiSquareModel.getPd(); }

    /**
     * Getter for given or calculated detection threshold for this Swerling case model.
     * @return detection threshold
     */
    public double getThr() { return chiSquareModel.getThr(); }

    /**
     * Getter for given or calculated probability of false alarm for this Swerling model.
     * @return probability of false alarm
     */
    public Probability getPfa() { return chiSquareModel.getPfa(); }

    /**
     * Getter for the number of non-coherently integrated pulses given for this Swerling model.
     * @return number of non-coherently integrated pulses
     */
    public int getN() { return chiSquareModel.getN(); }

    /**
     * Getter for the given Swerling case associated with this fluctuation model.
     * @return Swerling case
     */
    public Case getSwerlingCase() { return this.sw_case; }

    /**
     * Build {@link String} representation for this class object
     * @return string representation of class object
     */
    public String toString() {
        String result = this.getClass().getName() + "\n";
        result += "N = " + this.getN() + "\n";
        result += "Swerling Case = " + this.getSwerlingCase() + "\n";
        result += "Pfa = " + this.getPfa() + "\n";
        result += "Thr = " + this.getThr() + "\n";
        result += "SNR = " + this.getSnr() + "\n";
        result += "Pd = " + this.getPd() + "\n";
        return result;
    }

}
