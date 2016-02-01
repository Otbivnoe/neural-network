package com.neuro;

import java.util.Random;

/**
 * Created by nikita on 14/01/16.
 */
public class Neuron {

    public Double value;
    public Double activateValue;
    public Double delta;
    public Double[] inputWeights;
    public Double[] deltaWeights;

    public Neuron(int inputWeightsCount) {

        inputWeights = new Double[inputWeightsCount];
        deltaWeights = new Double[inputWeightsCount];

        for (int i = 0; i < inputWeightsCount; i++) {
            Random r = new Random();
            double rangeMin = -0.5;
            double rangeMax = 0.5;
            double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            inputWeights[i] = randomValue;
        }
    }
}
