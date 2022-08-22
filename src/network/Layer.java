package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Layer {
    public List<Neuron> neurons;
    public List<Connection> connections;
    public Layer previous;

    public String mode;

    //Creates a layer of the network, filled with neurons and connections
    public Layer(int neurons_count, boolean input, Layer previous, String mode) {
        Random random = new Random();

        this.mode = mode;

        neurons = new ArrayList<>();
        connections = new ArrayList<>();

        this.previous = previous;

        for(int i = 0; i < neurons_count; i ++) {
            neurons.add(new Neuron(i));
        }

        //The input layer doesn't have connections with previous layers!
        if(!input) {
            for(int i = 0; i < neurons_count; i ++) {
                for(int j = 0; j < previous.neurons.size(); j ++) {
                    connections.add(new Connection(previous.neurons.get(j), neurons.get(i), random.nextDouble()));
                }
            }
        }
    }

    //Creates a layer for the network using a list of weights
    public Layer(int neurons_count, boolean input, Layer previous, String mode, List<Double> weights) {
        this.mode = mode;

        neurons = new ArrayList<>();
        connections = new ArrayList<>();

        this.previous = previous;

        for(int i = 0; i < neurons_count; i ++) {
            neurons.add(new Neuron(i));
        }

        //We don't need to add connections for the input layer (layer 0)
        if(!input) {
            for(int i = 0; i < neurons_count; i ++) {
                for(int j = 0; j < previous.neurons.size(); j ++) {
                    //Connections are ordered A->B, A->C, A2->B, A2->C, etc.
                    connections.add(new Connection(previous.neurons.get(j), neurons.get(i), weights.get(i * previous.neurons.size() + j)));
                }
            }
        }
    }

    //"Executes" the current layer logic
    public void act() {
        //Sum in neuron b the value of a + b
        for (Connection connection : connections) {
            connection.transmit();
        }

        //Pass the sum in the neuron through the activation function
        for (Neuron neuron : neurons) {
            neuron.activation(mode);
        }
    }
}
