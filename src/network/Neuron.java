package network;

import java.util.ArrayList;
import java.util.HashMap;

class Neuron {
    static int counter = 0;
    final int id;
    private Connection biasConnection;
    private double output;

    private ArrayList<Connection> InConnections = new ArrayList<>();
    private HashMap<Integer, Connection> connectionLookup = new HashMap<>();

    Neuron() {
        id = counter;
        counter++;
    }

    void calculateOutput() {
        double s = 0;
        for (Connection con : InConnections) {
            Neuron leftNeuron = con.getFromNeuron();
            double weight = con.getWeight();
            double a = leftNeuron.getOutput();

            s = s + (weight * a);
        }
        double bias = -1;
        s = s + (biasConnection.getWeight() * bias);

        output = g(s);
    }


    private double g(double x) {
        return sigmoid(x);
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + (Math.exp(-x)));
    }

    void addInConnectionsS(ArrayList<Neuron> inNeurons) {
        for (Neuron n : inNeurons) {
            Connection con = new Connection(n);
            InConnections.add(con);
            connectionLookup.put(n.id, con);
        }
    }

    Connection getConnection(int neuronIndex) {
        return connectionLookup.get(neuronIndex);
    }

    void addBiasConnection(Neuron n) {
        Connection con = new Connection(n);
        biasConnection = con;
        InConnections.add(con);
    }

    ArrayList<Connection> getAllInConnections() {
        return InConnections;
    }

    double getOutput() {
        return output;
    }

    void setOutput(double o) {
        output = o;
    }
}
