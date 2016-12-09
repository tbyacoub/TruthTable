package network;

class Connection {
    private double weight = 0;
    private double prevDeltaWeight = 0;
    private double deltaWeight = 0;

    final Neuron leftNeuron;
    final int id;
    static int counter = 0;

    Connection(Neuron fromN) {
        leftNeuron = fromN;
        id = counter;
        counter++;
    }

    double getWeight() {
        return weight;
    }

    void setWeight(double w) {
        weight = w;
    }

    void setDeltaWeight(double w) {
        prevDeltaWeight = deltaWeight;
        deltaWeight = w;
    }

    double getPrevDeltaWeight() {
        return prevDeltaWeight;
    }

    Neuron getFromNeuron() {
        return leftNeuron;
    }
}