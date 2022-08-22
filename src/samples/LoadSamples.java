package samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadSamples {
    //Loads n images from data/samples and converts them into samples
    //Returns a list of the samples that can be used to evaluate a network
    public static List<Sample> load(int count) throws FileNotFoundException {
        List<Sample> samples = new ArrayList<>();

        for(int i = 0; i < count; i ++) {
            File file = new File("data/samples/" + (i + 1) + ".txt");

            Scanner scanner = new Scanner(file);

            String inputLine = scanner.nextLine();
            String expectedLine = scanner.nextLine();

            //Converting the data in the sample into tokens
            String[] inputTokens = inputLine.split(" ");
            String[] expectedTokens = expectedLine.split(" ");

            double[] input = new double[inputTokens.length];
            double[] expected = new double[expectedTokens.length];

            //Filling the sample with parsed inputs
            for(int j = 0; j < inputTokens.length; j ++) {
                input[j] = Double.parseDouble(inputTokens[j]);
            }

            //Filling the sample with parsed expected outputs
            for(int j = 0; j < expectedTokens.length; j ++) {
                expected[j] = Double.parseDouble(expectedTokens[j]);
            }

            //Making the sample and adding it to the list
            Sample sample = new Sample(input, expected);

            samples.add(sample);
        }

        return samples;
    }
}
