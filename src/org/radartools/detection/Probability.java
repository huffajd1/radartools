package org.radartools.detection;

/**
 * Helper class that does a couple of things
 *    - ensures that any probability entered (for the inverse functions) is a valid probability (between 0 and 1 inclusive)
 *    - allows for multiple @ChiSquare constructors different depending on whether the parameters
 *      are doubles (e.g. signal to noise ratio and detection threshold)
 *      or probabilities (e.g. probability of false alarm and probability of detection)
 */
public class Probability {

    private final double value;

    /**
     * Constructor for a probability value
     * @param value a probability value between zero and one inclusive
     * @throws Exception if the input probability value is not between zero and one inclusive
     */
    public Probability( double value ) throws Exception {
        if( value < 0.0 || value > 1.0 ) {
            throw new Exception("Probability must be between 0 and 1 (inclusive).  Value is " + value );
        }
        else {
            this.value = value;
        }
    }

    /**
     * Getter for the underlying value of the probability
     * @return the value of the probability
     */
    public double getValue() {
        return value;
    }

    /**
     * Provide a String representation of probability
     * @return the value of probability as a String
     */
    public String toString() { return Double.toString( this.getValue() ); }
}
