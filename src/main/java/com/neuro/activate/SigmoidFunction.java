package com.neuro.activate;

/**
 * Created by nikita on 14/01/16.
 */
public class SigmoidFunction implements IActivateFunction {

    private double alpha = 1;

    public double Compute(double x) {
        return (1 / (1 + Math.exp(-1 * alpha * x)));
    }

    public double ComputeFirstDerivative(double x) {
        return alpha * this.Compute(x) * (1 - this.Compute(x));
    }
}
