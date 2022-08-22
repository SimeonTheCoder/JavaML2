import network.Network;
import samples.LoadSamples;
import samples.MakeSamples;
import samples.Sample;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Network network = new Network(new int[]{10 * 10, 40, 20, 2}, "ReLu");

        MakeSamples.make(10);

        List<Sample> sampleList = LoadSamples.load(10);

        System.out.println(network.eval(sampleList));

        for(int i = 0; i < 1000; i++) {
            network.train(.2, sampleList);
            System.out.println(i + " -> " + network.eval(sampleList));
        }

        System.out.println(network.eval(sampleList));

        network.input(sampleList.get(0).input);
        double[] output = network.run();

        for (double v : output) {
            System.out.println(v);
        }

        network.saveNetwork("data/network.txt");
    }
}
