package org.radartools.detection;

import org.junit.jupiter.api.Test;
import org.radartools.detection.Probability;

import static org.junit.jupiter.api.Assertions.*;

class ProbabilityTest {

    @Test
    void testValidProbabilities() {
        try {
            Probability valid = new Probability(1.0);
            assertEquals(valid.getValue(),1.0);
            valid = new Probability(0.0);
            assertEquals(valid.getValue(),0.0);
            valid = new Probability(0.5);
            assertEquals(valid.getValue(),0.5);
        } catch (Exception e) {
            fail("Testing a valid probability caused an exception");
            e.printStackTrace();
        }
    }

    @Test
    void testInvalidProbabilities() {
        try {
            Probability invalid = new Probability(3.0);
            fail("Testing an invalid probability did not cause an exception");
        } catch (Exception e) {
        }
    }
}