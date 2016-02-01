package com.neuro.error;

/**
 * Created by nikita on 14/01/16.
 */

public class HalfSquaredEuclidianDistance implements IMetrics<Double> {

    public double Calculate(Double[] v1, Double[] v2) {
        double sum = 0;
        for (int i = 0; i < v1.length; i++) {
            sum += (v1[i] - v2[i]) * (v1[i] - v2[i]);
        }
        return 0.5 * sum;
    }

    public Double CalculatePartialDerivaitveByV2Index(Double[] v1, Double[] v2, int v2Index) {
        return v2[v2Index] - v1[v2Index];
    }
}
