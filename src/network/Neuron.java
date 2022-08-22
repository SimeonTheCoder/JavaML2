package network;

public class Neuron {
    public double value;
    public int index;

    public Neuron(int index) {
        this.value = 0.;

        this.index = index;
    }

    //Passes its value through an activation function, specified in "mode"
    public void activation(String mode) {
        switch (mode) {
            case "ReLu":
                this.value = Math.max(0, this.value);

                break;
        }
    }
}
