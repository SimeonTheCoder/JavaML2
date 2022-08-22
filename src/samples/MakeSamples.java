package samples;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MakeSamples {
    public static void make(int count) throws IOException {
        //Reads the labels file where each image is given a label
        File labelsFile = new File("data/labels.txt");

        List<List<Double>> labels = new ArrayList<>();

        Scanner labelScanner = new Scanner(labelsFile);

        int lineIndex = 0;

        while(labelScanner.hasNextLine()) {
            labels.add(new ArrayList<>());

            String line = labelScanner.nextLine();

            for (String token : line.split(" ")) {
                labels.get(lineIndex).add(Double.parseDouble(token));
            }

            lineIndex ++;
        }

        //Executed for every image
        for(int i = 0; i < count; i ++) {
            BufferedImage image = ImageIO.read(new File("data/training_data/" + (i + 1) + ".jpg"));

            FileWriter writer = new FileWriter(new File("data/samples/" + (i + 1) + ".txt"));

            double[] data = new double[image.getHeight() * image.getWidth()];

            //Reading and writing pixel data to sample input
            for(int y = 0; y < image.getHeight() / 4; y ++) {
                for(int x = 0; x < image.getWidth() / 4; x ++) {
                    data[y * image.getHeight() + x] = new Color(image.getRGB(x, y)).getRed() / 25.;
                }
            }

            //Writing expected result to sample
            double[] label = new double[labels.get(i).size()];

            for(int j = 0; j < labels.get(i).size(); j ++) {
                label[j] = labels.get(i).get(j);
            }

            //Writing sample to file
            for (double datum : data) {
                writer.write(String.valueOf(datum));
                writer.write(" ");
            }

            writer.write(System.lineSeparator());

            for (double v : label) {
                writer.write(String.valueOf(v));
                writer.write(" ");
            }

            writer.close();
        }
    }
}
