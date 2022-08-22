package network;

public class Connection {
    public Neuron a;
    public Neuron b;

    public double weight;

    public Connection(Neuron a, Neuron b, double weight) {
        this.a = a;
        this.b = b;

        this.weight = weight;
    }

    //Increments neuron b's value (the end of the connection) with neuron a's value
    public void transmit() {
        b.value += a.value * this.weight;
    }
}
