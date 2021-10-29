package org.radartools.detection;

/**
 * A set of static tools to aid in testing the org.radartools.detection package
 */
public class TestTools {

    /**
     * Checks for two doubles that are close enough in value to pass as equal within the given margin of error.
     * @param x the first value in the comparison
     * @param y the second value in the comparision
     * @param err the margin of error given for tthe comparison
     * @return true if the two values given are within the specified margin of error
     */
    public static boolean ApproxEquals( double x, double y, double err ) {
        return Math.abs(y-x) <= err;
    }

}
