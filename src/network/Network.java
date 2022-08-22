package network;

import samples.Sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Network {
    public List<Layer> layers;

    public int[] layer_sizes;
    public String mode;

    //Creates a network using an array of layer sizes
    //Also takes a string value, corresponding to the desired activation function of the network
    public Network(int[] layer_sizes, String mode) {
        this.layer_sizes = layer_sizes;
        this.mode = mode;

        layers = new ArrayList<>();

        for(int i = 0; i < layer_sizes.length; i ++) {
            Layer layer = null;

            //Checks if the layer is an input layer, if so, flag it as one
            if(i == 0) {
                layer = new Layer(layer_sizes[i], true, null, mode);
            }else{
                layer = new Layer(layer_sizes[i], false, layers.get(layers.size() - 1), mode);
            }

            layers.add(layer);
        }
    }

    //Fills the first layer's values with a double array
    public void input(double[] values) {
        for(int i = 0; i < layer_sizes[0]; i ++) {
            layers.get(0).neurons.get(i).value = values[i];
        }
    }

    //Runs the network and returns its prediction
    public double[] run() {
        //"Executes" each layer
        for (Layer layer : layers) {
            layer.act();
        }

        //Collect the results and return them
        double[] results = new double[layer_sizes[layer_sizes.length - 1]];
        int index = 0;

        for (Neuron neuron : layers.get(layers.size() - 1).neurons) {
            results[index] = neuron.value;

            index ++;
        }

        return results;
    }

    //Goes through a list of samples and executes a cost function on them
    public double eval(List<Sample> samples) {
        double sum = 0;

        for (Sample sample : samples) {
            //Makes the sample input a network input
            input(sample.input);

            //Runs the network
            for (Layer layer : layers) {
                layer.act();
            }

            //Calculating the difference between result and expected
            double diff = 0;
            int index = 0;

            for (Neuron neuron : layers.get(layers.size() - 1).neurons) {
                diff += sample.expected[index] - neuron.value;

                index++;
            }

            sum += diff;
        }

        //Averages the cost of the network
        return sum / (samples.size() + .0);
    }

    //Saves the network in data/network.txt
    public void saveNetwork(String filename) throws IOException {
        File file = new File(filename);

        FileWriter writer = new FileWriter(file);

        for(int i = 0; i < layers.size(); i ++) {
            for(int j = 0; j < layers.get(i).connections.size(); j ++) {
                //File format: layer, weight

                writer.write(i + " " + layers.get(i).connections.get(j).weight);

                writer.write(System.lineSeparator());
            }
        }

        writer.close();
    }

    //Loads the saved network from data/network.txt
    public void loadNetwork(String filename) throws FileNotFoundException {
        File file = new File(filename);

        Scanner scanner = new Scanner(file);

        List<List<Double>> weightsPerLayer = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            //Splits the current line into tokens and converts them into weights
            String[] tokens = line.split(" ");

            if(Integer.parseInt(tokens[0]) >= weightsPerLayer.size()) {
                weightsPerLayer.add(new ArrayList<>());
            }

            weightsPerLayer.get(Integer.parseInt(tokens[0]) - 1).add(Double.parseDouble(tokens[1]));
        }

        //Setting the correct weight for each connection
        for (int i = 1; i < layer_sizes.length; i ++) {
            layers.set(i, new Layer(layer_sizes[i], false, layers.get(i-1), mode, weightsPerLayer.get(i - 1)));
        }
    }

    public void train(double step, List<Sample> samples) {
        for (Layer layer : layers) {
            for (Connection connection : layer.connections) {
                double connection_weight = connection.weight;

                double cost1 = eval(samples);

                connection.weight += step;

                double cost2 = eval(samples);

                connection.weight -= step * 2;

                double cost3 = eval(samples);

                double min = Math.min(cost1, Math.min(cost2, cost3));

                if(min == cost1) {
                    connection.weight = connection_weight;
                }

                if(min == cost2) {
                    connection.weight = connection_weight + step;
                }

                if(min == cost3) {
                    connection.weight = connection_weight - step;
                }
            }
        }
    }
}
