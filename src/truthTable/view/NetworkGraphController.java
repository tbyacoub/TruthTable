package truthTable.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import truthTable.model.ColumnHead;

import java.util.ArrayList;

public class NetworkGraphController {

    private static final int NODE_RADIUS = 30;

    private ObservableList<ColumnHead> inputData;

    private ObservableList<ColumnHead> outputData;

    private ArrayList<Circle> inputNodeList;

    private ArrayList<Circle> hiddenNodeList;

    private ArrayList<Circle> outputNodeList;

    @FXML
    private Group group;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void initialize() {
        anchorPane.setStyle( "-fx-background-color: gray" );
    }

    private void clear() {
        inputNodeList = new ArrayList<>();
        hiddenNodeList = new ArrayList<>();
        outputNodeList = new ArrayList<>();
        group.getChildren().clear();
    }

    public void initializeData(ObservableList<ColumnHead> inputData, ObservableList<ColumnHead> outputData) {
        this.inputData = inputData;
        this.outputData = outputData;
        clear();
    }

    public void render(ArrayList<ArrayList<Double>> allWeights) {
        clear();
        double layerHeight = anchorPane.getHeight() / (3);
        double inputLayerWidth = anchorPane.getWidth() / (inputData.size() + 1);
        double hiddenLayerWidth = anchorPane.getWidth() / (inputData.size() + 2);
        double outputLayerWidth = anchorPane.getWidth() / (outputData.size() + 1);

        double curHeight = layerHeight;

        createInputLayerNodes( curHeight, inputLayerWidth, inputData );
        curHeight += layerHeight;
        createHiddenLayerNodes( curHeight, hiddenLayerWidth, inputData.size() + 1 );
        curHeight += layerHeight;
        createOutputLayerNodes( curHeight, outputLayerWidth, outputData );
        connectNodes( allWeights );
    }

    private void connectNodes(ArrayList<ArrayList<Double>> allWeights) {
        for (int destinationNode = 0; destinationNode < allWeights.size(); destinationNode++) {
            if (destinationNode < hiddenNodeList.size())
                inputToHidden( destinationNode, allWeights );
            else
                hiddenToOutput( destinationNode - hiddenNodeList.size(), allWeights );
        }
    }

    private void hiddenToOutput(int destinationNode, ArrayList<ArrayList<Double>> allWeights) {
        Circle curDestinationNode = outputNodeList.get( destinationNode );
        ArrayList<Double> curOutputNodeWeights = allWeights.get( destinationNode );
        for (int targetNode = 0; targetNode < curOutputNodeWeights.size(); targetNode++) {
            createEdge( targetNode, curOutputNodeWeights.get( targetNode ), curDestinationNode.getCenterX(),
                    curDestinationNode.getCenterY(), hiddenNodeList );
        }
    }

    private void inputToHidden(int destinationNode, ArrayList<ArrayList<Double>> allWeights) {
        Circle curDestinationNode = hiddenNodeList.get( destinationNode );
        ArrayList<Double> curHiddenNodeWeights = allWeights.get( destinationNode );
        for (int targetNode = 0; targetNode < curHiddenNodeWeights.size() - 1; targetNode++) {
            createEdge( targetNode, curHiddenNodeWeights.get( targetNode ), curDestinationNode.getCenterX(),
                    curDestinationNode.getCenterY(), inputNodeList );
        }
    }

    private void createEdge(int targetNode, double edgeWeight, double destinationX, double destinationY,
                            ArrayList<Circle> targetList) {
        Circle curTargetNode = targetList.get( targetNode );
        double scaledWeight = 1 + edgeWeight;
        Line edge = new Line( curTargetNode.getCenterX(), curTargetNode.getCenterY(),
                destinationX, destinationY );
        edge.setStrokeWidth( scaledWeight );
        group.getChildren().add( edge );
    }

    private void createOutputLayerNodes(double curHeight, double outputLayerWidth, ObservableList<ColumnHead> outputData) {
        double curWidth = outputLayerWidth;
        for (ColumnHead output : outputData) {
            outputNodeList.add( createNode( curHeight, curWidth, output.getName(), Color.GREEN ) );
            curWidth += outputLayerWidth;
        }
    }

    private void createHiddenLayerNodes(double curHeight, double hiddenLayerWidth, int hiddenSize) {
        double curWidth = hiddenLayerWidth;
        for (int i = 0; i < hiddenSize; i++) {
            hiddenNodeList.add( createNode( curHeight, curWidth, "H" + i, Color.ALICEBLUE ) );
            curWidth += hiddenLayerWidth;
        }
    }

    private void createInputLayerNodes(double curHeight, double inputLayerWidth, ObservableList<ColumnHead> inputData) {
        double curWidth = inputLayerWidth;
        for (ColumnHead input : inputData) {
            inputNodeList.add( createNode( curHeight, curWidth, input.getName(), Color.RED ) );
            curWidth += inputLayerWidth;
        }
    }

    private Circle createNode(double curHeight, double curWidth, String name, Color color) {
        Text label = new Text( curWidth, curHeight, name );
        Circle inputNode = new Circle( curWidth, curHeight, NODE_RADIUS, color );
        group.getChildren().addAll( inputNode, label );
        return inputNode;
    }
}
