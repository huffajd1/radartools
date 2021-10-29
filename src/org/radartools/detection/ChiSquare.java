package org.radartools.detection;

/**
 * A chi-square distribution model suitable for calculation of radar probability of detection (PD) for a given
 * signal-to-noise ratio (SNR) and a given {@link Noise} model.
 */
public class ChiSquare {

    // parameters associated with the chi-square distribution model
    private final double thr;  // detection threshold for the associated probability of false alarm and given Noise model
    private final Probability pfa;  // probability of false alarm associated with the detection threshold and given Noise model
    private final double snr;  // signal-to-noise ratio yielding a given probability of detection for a chi-square distribution
    private final Probability pd;   // probability of detection corresponding to a given signal-to-noise ratio for a chi-square distribution
    private final int n;  // the number of non-coherently integrated pulses in the radar dwell
    private final int K;  // the number of chi-square degrees of freedom

    static final int NONFLUCTUATING = Integer.MAX_VALUE;

    /**
     * Constructor given signal-to-noise ratio and detection threshold
     * @param snr signal-to-noise ratio
     * @param thr detection threshold
     * @param n number of non-coherently integrated pulses
     * @param K chi-square degrees of freedom
     */
    public ChiSquare(double snr, double thr, int n, int K) {
        this.n = n;
        this.K = K;
        this.thr = thr;
        this.pfa = Noise.pfa(n,thr);
        this.snr = snr;
        this.pd = pd(snr);
    }

    /**
     * Constructor given signal-to-noise ratio and probability of false alarm
     * @param snr signal-to-noise ratio
     * @param pfa detection threshold
     * @param n number of non-coherently integrated pulses
     * @param K chi-square degrees of freedom
     */
    public ChiSquare(double snr, Probability pfa, int n, int K) {
        this.n = n;
        this.K = K;
        this.thr = Noise.thr(this.n, pfa);
        this.pfa = pfa;
        this.snr = snr;
        this.pd = pd(snr);
    }

    /**
     * Constructor given probability of detection and detection threshold
     * @param pd signal-to-noise ratio
     * @param thr detection threshold
     * @param n number of non-coherently integrated pulses
     * @param K chi-square degrees of freedom
     */
    public ChiSquare(Probability pd, double thr, int n, int K) {
        this.n = n;
        this.K = K;
        this.thr = thr;
        this.pfa = Noise.pfa(this.n,thr);
        this.snr = snr(pd);
        this.pd = pd;
    }

    /**
     * Constructor given probability of detection and probability of false alarm
     * @param pd signal-to-noise ratio
     * @param pfa detection threshold
     * @param n number of non-coherently integrated pulses
     * @param K chi-square degrees of freedom
     */
    public ChiSquare(Probability pd, Probability pfa, int n, int K) {
        this.n = n;
        this.K = K;
        this.thr = Noise.thr(this.n,pfa);
        this.pfa = pfa;
        this.snr = snr(pd);
        this.pd = pd;
    }

    /**
     * Calculates the signal-to-noise ratio required to yield the input probability of detection for
     * this chi-square distribution.
     * @param pd The desired probability of detection.  Must be between 0 and 1 inclusive.
     * @return  Signal-to-noise ratio
     */
    public double snr(Probability pd) {
        Solver solver = new Solver( this::mitchell_walker );
        return solver.solve( pd.getValue(), this.thr / (double) this.n );
    }

    /**
     * Calculates the probability of detection given the input signal-to-noise ratio
     * for this chi-square distribution.
     * @param snr The desired signal-to-noise ratio.
     * @return Probability of detection
     */
    public Probability pd(double snr) {
        Probability result = null;
        try {
            result = new Probability( mitchell_walker(snr) );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }

    /**
     * Calculate the probability of detection for a given signal-to-noise ratio and chi-square distribution
     * @param snr signal-to-noise ratio
     * @return probability of detection
     */
    private double mitchell_walker(double snr) {

        /* The approach here (and notation) follows that of:
           Mitchell, R. L. and J. F. Walker,
           "Recursive Methods for Computing Detection Probabilities,"
           IEEE Transactions on Aerospace and Electronic Systems,
           Vol AES-7, No. 4, July 1971, pp. 671-676.
         */

        double x_bar = snr * this.n;
        double err = 1e-16;

        // find g(N,t)
        double e = Math.exp(-this.thr);
        double g = e;
        double h = e;
        for(int i=1;i<=n-1;i++) {
            h *= this.thr/(double)i;
            g += h;
        }

        // a(0,x_bar)
        double a;
        if( this.K == NONFLUCTUATING ) a = Math.exp(-x_bar);
        else a = Math.pow(1+x_bar/this.K,-this.K);

        // j=0 result a(0,x_bar)*g(N,t)
        double result = a*g;

        // iterate the result for j > 0
        double l = Double.MAX_VALUE;
        int j = 0;
        while(Math.abs(l/result)>err) {
            h *= this.thr/(n+j);
            g += h;
            if( this.K == NONFLUCTUATING ) a *= x_bar / (double)(1+j);
            else a *= x_bar*(1+(double)j/(double)this.K) / (1+x_bar/(double)this.K) / (double)(1+j);
            l = a*g;
            result += l;
            j++;
        }
        return result;
    }

    /**
     * Build {@link String} representation for this class object
     * @return string representation of class object
     */
    public String toString() {
        String result = this.getClass().getName() + "\n";
        result += "N = " + this.getN() + "\n";
        result += "DoF = " + this.getK() + "\n";
        result += "Pfa = " + this.getPfa() + "\n";
        result += "Thr = " + this.getThr() + "\n";
        result += "SNR = " + this.getSnr() + "\n";
        result += "Pd = " + this.getPd() + "\n";
        return result;
    }

    /**
     * Getter for the detection threshold associated with this chi-square distribution model.
     * @return detection threshold
     */
    public double getThr() {
        return thr;
    }

    /**
     * Getter for the probability of false alarm associated with this chi-square distribution model.
     * @return probability of false alarm
     */
    public Probability getPfa() {
        return pfa;
    }

    /**
     * Getter for the signal-to-noise ratio associated with this chi-square distribution model.
     * @return signal-to-noise ratio
     */
    public double getSnr() {
        return snr;
    }

    /**
     * Getter for the probability of detection associated with this chi-square distribution model.
     * @return probability of detection
     */
    public Probability getPd() {
        return pd;
    }

    /**
     * Getter for the number of non-coherently integrated radar pulses associated with this chi-square distribution model.
     * @return number of non-coherently integrated pulses
     */
    public int getN() {
        return n;
    }

    /**
     * Getter for the degrees of freedom associated with this chi-square distribution model.
     * @return degrees of freedom
     */
    public int getK() {
        return K;
    }


}
