package org.radartools.detection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    @Test
    void testLinearIncreasing() {
        Solver solver = new Solver( (x)->2.*x+1 );
        double result = solver.solve( 13., 5. );
        assertTrue( TestTools.ApproxEquals( 6., result, 1e-10 ) );
    }

    @Test
    void testLinearDecreasing() {
        Solver solver = new Solver( (x)->-3.*x+100 );
        double result = solver.solve( 25., 10 );
        assertTrue( TestTools.ApproxEquals( 25., result, 1e-10 ) );
    }

    @Test
    void testLogIncreasing() {
        Solver solver = new Solver( (x)->10.*Math.log10(x) );
        double result = solver.solve( 26., 100. );
        assertTrue( TestTools.ApproxEquals( result/400., 1., .20 ) );
    }
}