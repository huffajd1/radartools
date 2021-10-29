package org.radartools.detection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MarcumTest {

    class TestData {
        int n;
        double xbar;
        double expected;

        public TestData(int n, double xbar, double expected) {
            this.n = n;
            this.xbar = xbar;
            this.expected = expected;
        }
    }

    @Test
    void testMarcum() {
        TestData[] testData = new TestData[] {
                new TestData( 1, 3.162278, .0045853516 ),
                new TestData( 1, 10., .24804931 ),
                new TestData( 1, 31.62278, .9972254 ),
                new TestData( 3, 3.162278, .088813157 ),
                new TestData( 3, 10., .97272573 ),
                new TestData( 10, 3.162278, .85331678 ),
                new TestData( 30, 3.162278, .99999943 ),
        };
        try {
            Probability pfa = new Probability(1.e-6);
            for(int i = 0; i < testData.length; i++) {
                Marcum model = new Marcum( testData[i].xbar, pfa, testData[i].n );
                double result = model.getPd().getValue();
                assertTrue( TestTools.ApproxEquals( testData[i].expected, result, .001 ) );
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Testing a valid probability caused an exception");
        }
    }

}