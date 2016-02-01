package com.neuro.activate;

/**
 * Created by nikita on 23/01/16.
 */
public class HyperbolicTangensFunction implements IActivateFunction {

    private double alpha = 0.1;

    //cos = 0.1

    public double Compute(double x) {
        return (Math.tanh(alpha * x));
    }

    public double ComputeFirstDerivative(double x) {
        double t = Compute(x);
        return alpha*(1 - t*t);
    }
}
