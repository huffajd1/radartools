package org.radartools.detection;

/**
 * Helper class for implementing a Marcum<sup><b>1</b></sup> (or non-fluctuating) target model
 * using a generic {@link ChiSquare} fluctuation model.
 *
 * <p>
 *     <sup><b>1</b></sup>Marcum, J. I., "A Statistical Theory of Target Detection by Pulsed Radar,"
 *     U.S. Air Force Project RAND Research Memorandum RM-754, ASTIA Document Number AD 101287,
 *     1 December 1947, Reissued 25 April 1952.
 * </p>
 */
public class Marcum {

    private final ChiSquare chiSquareModel;

    /**
     * Constructor where the signal-to-noise ratio and the detection threshold are given
     * @param snr average signal-to-noise ratio per non-coherently integrated pulse
     * @param thr detection threshold
     * @param n number of non-coherently integrated pulses
     */
    public Marcum( double snr, double thr, int n ) {
        chiSquareModel = new ChiSquare( snr, thr, n, ChiSquare.NONFLUCTUATING );
    }

    /**
     * Constructor where the desired probability of detection and the detection threshold are given
     * @param pd desired probability of detection for the non-coherently integrated pulses
     * @param thr detection threshold
     * @param n number of non-coherently integrated pulses
     */
    public Marcum( Probability pd, double thr, int n ) {
        chiSquareModel = new ChiSquare( pd, thr, n, ChiSquare.NONFLUCTUATING );
    }

    /**
     * Constructor where the signal-to-noise ratio and the desired probability of false alarm are given
     * @param snr average signal-to-noise ratio per non-coherently integrated pulse
     * @param pfa desired probability of false alarm for the non-coherently integrated pulses
     * @param n number of non-coherently integrated pulses
     */
    public Marcum( double snr, Probability pfa, int n ) {
        chiSquareModel = new ChiSquare( snr, pfa, n, ChiSquare.NONFLUCTUATING );
    }

    /**
     * Constructor where the desired probabilities of detection and false alarm are given
     * @param pd desired probability of detection for the non-coherently integrated pulses
     * @param pfa desired probability of false alarm for the non-coherently integrated pulses
     * @param n number of non-coherently integrated pulses
     */
    public Marcum( Probability pd, Probability pfa, int n ) {
        chiSquareModel = new ChiSquare( pd, pfa, n, ChiSquare.NONFLUCTUATING );
    }

    /**
     * Getter for the given or calculated average per pulse signal-to-noise ratio for this Marcum (non-fluctuating) model.
     * @return signal-to-noise ratio
     */
    public double getSnr() { return chiSquareModel.getSnr(); }

    /**
     * Getter for the given or calculated probability of detection for this Marcum (non-fluctuating) model.
     * @return probability of detection
     */
    public Probability getPd() { return chiSquareModel.getPd(); }

    /**
     * Getter for the given or calculated detection threshold for this Marcum (non-fluctuating) model.
     * @return detection threshold
     */
    public double getThr() { return chiSquareModel.getThr(); }

    /**
     * Getter for the given or calculated probability of false alarm for this Marcum (non-fluctuating) model.
     * @return probability of false alarm
     */
    public Probability getPfa() { return chiSquareModel.getPfa(); }

    /**
     * Getter for the given number of non-coherently integrated pulses for this Marcum (non-fluctuating) model.
     * @return number of non-coherently integrated pulses
     */
    public int getN() { return chiSquareModel.getN(); }

    /**
     * Build {@link String} representation for this class object
     * @return string representation of class object
     */
    public String toString() {
        String result = this.getClass().getName() + "\n";
        result += "N = " + this.getN() + "\n";
        result += "Pfa = " + this.getPfa() + "\n";
        result += "Thr = " + this.getThr() + "\n";
        result += "SNR = " + this.getSnr() + "\n";
        result += "Pd = " + this.getPd() + "\n";
        return result;
    }

}
