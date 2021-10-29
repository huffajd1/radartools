package org.radartools.detection;

import java.util.Objects;
import java.util.function.Function;

/**
 * Solves for the inverse functions related to probability of false alarm (Pfa) and probability of detection
 * (Pd) calculations.  Currently, this solver requires that any forward direction function the solver is
 * applied to is monotonic.  The forward function can be monotonically increasing or decreasing.
 */
final class Solver {
    private final Function<Double, Double> calculate;


    /**
     * Constructor for the solver.
     *
     * @param calculate the forward function for which to solve for the inverse.
     */
    Solver(Function<Double, Double> calculate) {
        this.calculate = calculate;
    }

    /**
     * Solve for the parameter input to the forward function that provides the desired result.  Given the
     * condition that the forward function is monotonic, the behavior of the solver is not particularly
     * dependent on an initial starting point that's close to the final result, though there are minor
     * performance gains with a more accurate starting point.
     *
     * @param desired_result the desired result of the forward function
     * @param initial_guess  an initial starting point for the iterative solver
     * @return the input to the forward function for which it returns the desired result.
     */
    public double solve(double desired_result, double initial_guess) {

        /*
         * The approach taken here is to:
         * <ol>
         *     <li>determine two initial solution bounds for the desired value, one below what the
         *      calculated result should be and the other above</li>
         *     <li>improve the solution estimate by determining a value between these bounds closer
         *     to what the calculated result should be.</li>
         *     <li>calculate the result from this updated estimate</li>
         *     <li>update either the upper solution bound or lower solution bound depending on whether
         *      the corresponding result is respectively above or below the desired result.</li>
         *     <li>repeat steps 2-4 until the solution is within the specified error.</li>
         * </ol>
         *
         * One could use something like a Newton-Raphson method, but for this particular application, the
         * derivative of the forward function tends to zero, requiring many iterations in key cases.
         *
         */

        /* <h3>Maximum error for the SNR estimate</h3>
         * <p>
          This is currently left in the code rather than user input to avoid the potential for
          the user to select a maximum error to small for this iterative process to converge given
          the double precision accuracy of all terms.
         </p>

          <p>If a faster convergence is necessary, the maximum error can be adjusted to a larger value.</p>

          <p>A future alternative may be to allow a user to input a desired maximum error with the code
          enforcing a lower bound of say 1e-14.</p>

        */
        double err = 1e-12;

        // determine initial solution bounds
        double value = calculate.apply(initial_guess);

        /* this just helps expedite the identification of initial upper and lower bounds for the solution
         * if the forward function has a very small derivative.  We will iterate the upper and lower bounds
         * by multiplying and dividing the initial guess repeatedly by increasing value of i, so we approach
         * the bounds in a factorial manner.
         */
        int i = 1;

        /* since the forward function can be monotonically increasing or decreasing, we don't specify a
         * which bound is the upper or lower.  We just ensure that one is above the desired solution
         * and the other is below.
         */
        double bound_a = initial_guess;
        double bound_b = initial_guess;
        double result_a = value;
        double result_b = value;
        boolean bounds_not_found = true;
        while (bounds_not_found) {
            i++;
            bound_a /= i;
            bound_b *= i;
            result_a = calculate.apply(bound_a);
            result_b = calculate.apply(bound_b);
            bounds_not_found =
                    Math.max(result_a, result_b) < desired_result ||
                            Math.min(result_a, result_b) > desired_result;
        }

        // determine if it's monotonically increasing
        boolean increasing = result_a < result_b;

        // check to see if our solution is within error bound
        while (Math.abs(bound_a - bound_b) > err) {

            // if not, pick a new estimate as the arithmetic mean between the current upper and lower bounds
            double updated_guess = 0.5 * (bound_a + bound_b);

            // determine the corresponding probability of false alarm
            double updated_result = calculate.apply(updated_guess);

            // adjust bounds
            if (increasing) {
                if (updated_result > desired_result) bound_b = updated_guess;
                else bound_a = updated_guess;
            } else {
                if (updated_result < desired_result) bound_b = updated_guess;
                else bound_a = updated_guess;
            }

        }

        // return final result
        return 0.5 * (bound_a + bound_b);
    }

    public Function<Double, Double> calculate() {
        return calculate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Solver) obj;
        return Objects.equals(this.calculate, that.calculate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calculate);
    }

    @Override
    public String toString() {
        return "Solver[" +
                "calculate=" + calculate + ']';
    }

}
