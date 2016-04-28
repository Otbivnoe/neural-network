package com.neuro;

import com.neuro.activate.HyperbolicTangensFunction;
import com.neuro.activate.IActivateFunction;
import com.neuro.error.HalfSquaredEuclidianDistance;
import com.neuro.error.IMetrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 14/01/16.
 */
public class NeuralNetwork {

    private Layer[] layers;
    public int trainCount;
    private final double speed = 1.0;
    public List<Double> errors = new ArrayList<Double>();
    private IMetrics error = new HalfSquaredEuclidianDistance();
    private IActivateFunction activateFunction = new HyperbolicTangensFunction();

    public double MAINERROR;

    public NeuralNetwork(int[] neuronCounts) {

        for (int i = 0; i < neuronCounts.length - 1; i++) {
            neuronCounts[i] = neuronCounts[i] + 1;
        }

        layers = new Layer[neuronCounts.length];
        layers[0] = new Layer(0, neuronCounts[0], true);

        for (int i = 1; i < neuronCounts.length; i++) {
            layers[i] = new Layer(neuronCounts[i-1], neuronCounts[i], (i < neuronCounts.length - 1));
        }

    }

    public Double valueOutput(Double valueInput) {

        layers[0].setData(new Double[]{valueInput});
        Double value = outputForCurrentWeights();
        return value;
    }

    public void trainNeuralNetwork(Double[] inputVector, Double[] outputVector) {

        for (int i = 0; i < trainCount; i++) {

            Double [] errors_ = new Double[inputVector.length];

            for (int j = 0; j < inputVector.length; j++) {

                layers[0].setData(new Double[]{inputVector[j]});

                Double out = outputForCurrentWeights();
                errors_[j] = out;

                setNewDeltaForLastLayer(outputVector[j] - out);
                trainNeuronsDelta();
                trainNewWeights();
            }

            Double _error = error.Calculate(errors_, outputVector);
            this.errors.add(_error);
        }

        Double[] neuroOutput = new Double[outputVector.length];
        for (int i = 0; i < outputVector.length; i++) {
            neuroOutput[i] = valueOutput(outputVector[i]);
        }

        this.MAINERROR = this.error.Calculate(neuroOutput, outputVector);
    }

    private Double outputForCurrentWeights() {

        for (int i = 1; i < layers.length; i++) {

            Neuron[] prevNeurons = layers[i-1].neurons;
            Neuron[] neurons = layers[i].neurons;

            int index = (i == layers.length - 1) ? 0 : 1;
            for (int j = index; j < neurons.length; j++) {

                double sum = 0;
                for (int n = 0; n < neurons[j].inputWeights.length; n++) {
                    sum += neurons[j].inputWeights[n] * prevNeurons[n].activateValue;
                }

                double activateValue = activateFunction.Compute(sum);
                layers[i].neurons[j].activateValue = activateValue;
                layers[i].neurons[j].value = sum;
            }
        }

        Layer lastLayer = layers[layers.length - 1];
        return lastLayer.neurons[0].activateValue;
    }

    private void setNewDeltaForLastLayer(Double delta) {

        Neuron lastNeuron = layers[layers.length - 1].neurons[0];
        Layer preLastLayer = layers[layers.length - 2];

        Double newDelta = delta * activateFunction.ComputeFirstDerivative(lastNeuron.value);
        layers[layers.length - 1].neurons[0].delta = newDelta;

        //set weight deltas
        for (int i = 0; i < lastNeuron.inputWeights.length; i++) {
            layers[layers.length - 1].neurons[0].deltaWeights[i] = newDelta * preLastLayer.neurons[i].activateValue * speed;
        }
    }

    private void trainNeuronsDelta() {

        for (int i = layers.length - 2; i > 0; i--) {

            Layer currentLayer = layers[i];

            for (int j = 1; j < currentLayer.neurons.length; j++) {

                double sum = 0;
                int index = (i == layers.length - 2) ? 0 : 1;
                for (int k = index; k < layers[i+1].neurons.length; k++) {
                    sum += layers[i+1].neurons[k].delta * layers[i+1].neurons[k].inputWeights[j];
                }

                double delta = sum * activateFunction.ComputeFirstDerivative(layers[i].neurons[j].value);
                layers[i].neurons[j].delta = delta;

                //set weight deltas
                for (int k = 0; k < layers[i].neurons[j].inputWeights.length; k++) {
                    layers[i].neurons[j].deltaWeights[k] = speed * layers[i].neurons[j].delta * layers[i-1].neurons[k].value;
                }
            }
        }
    }

    private void trainNewWeights() {

        for (int i = 1; i < layers.length; i++) {

            int index = (i == layers.length - 1) ? 0 : 1;
            for (int j = index; j < layers[i].neurons.length; j++) {

                for (int k = 0; k < layers[i].neurons[j].inputWeights.length; k++) {
                    layers[i].neurons[j].inputWeights[k] = layers[i].neurons[j].inputWeights[k] + layers[i].neurons[j].deltaWeights[k];
                }
            }
        }
    }
}
