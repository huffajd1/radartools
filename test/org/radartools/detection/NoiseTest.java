package org.radartools.detection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class NoiseTest {



    @Test
    void testNoise() {
        try {
            int[] n = new int[]{1, 3, 10, 30, 100 };
            double[] results = new double[]{ 13.81551055, 19.12916818, 32.71034051, 63.54818012, 154.9190459};
            Probability pfa = new Probability(1.e-6);
            for(int i = 0; i < n.length; i++) {
                double thr = Noise.thr(n[i], pfa );
                assertTrue( TestTools.ApproxEquals( results[i], thr, .001 ) );
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Testing a valid probability caused an exception");
        }
    }

}