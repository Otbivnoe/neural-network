package com.neuro;

/**
 * Created by nikita on 14/01/16.
 */
public class Layer {

    Neuron[] neurons;

    public Layer(int prevNeuronCount, int neuronCount, Boolean withBias) {

        neurons = new Neuron[neuronCount];

        if (withBias) {
            neurons[0] = new Neuron(0);
            neurons[0].value = 1.0;
            neurons[0].activateValue = 1.0;
        }

        int index = (withBias) ? 1 : 0;
        for (int i = index; i < neurons.length; i++) {
            neurons[i] = new Neuron(prevNeuronCount);
        }
    }

    public void setData(Double[] data) {
        for (int i = 1; i <= data.length; i++) {
            neurons[i].value = data[i-1];
            neurons[i].activateValue = data[i-1];
        }
    }
}
