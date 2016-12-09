package truthTable;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.Network;
import truthTable.model.ColumnHead;
import truthTable.view.NetworkGraphController;
import truthTable.view.SetupController;
import truthTable.view.TestController;
import truthTable.view.TrainController;

import java.io.IOException;

/*
  A truth table, that uses neural network to solve logic. The Program is set to solve up to 6 inputs and 4 outputs.
  At start up the user will be greeted with a setup page, where the user can select the name, and the number of both
  inputs and outputs. After the setup process the other three tabs (train, test, visualize network) will be enabled.
  Half of the inputs will be enumerated for train, and test, to prove that the neural network can solve a given logic,
  without ever encountering that logic before*. In the visualization tab, a graph representation of the neural network
  will be shown. Through the representation of the graph the user can see which of the inputs had biggest factor in
  deciding the output.
 */
public class TruthTableApp extends Application {

    private Tab testTab;

    private Tab trainTab;

    private Tab setupTab;

    private Network network;

    private Tab visualizeTab;

    private TabPane tabPane;

    private TestController testController;

    private TrainController trainController;

    private NetworkGraphController visualizeController;

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.tabPane = FXMLLoader.load( getClass().getResource( "view/Main.fxml" ) );
        primaryStage.setTitle( "Truth Table NeuralN" );
        primaryStage.setScene( new Scene( tabPane, 1024, 680 ) );
        primaryStage.show();

        initTabs();
    }

    /**
     * Initializes all four tabs right after application startup.
     * All tabs except the setup tab are initially disabled.
     *
     * @throws IOException If *.fxml was not found.
     */
    private void initTabs() throws IOException {
        FXMLLoader setupLoader = new FXMLLoader();
        setupLoader.setLocation( getClass().getResource( "view/Setup.fxml" ) );
        AnchorPane setup = setupLoader.load();
        SetupController setupController = setupLoader.getController();
        setupController.setApp( this );
        setupTab = new Tab( "Setup", setup );

        FXMLLoader trainLoader = new FXMLLoader();
        trainLoader.setLocation( getClass().getResource( "view/Train.fxml" ) );
        AnchorPane train = trainLoader.load();
        trainController = trainLoader.getController();
        trainController.setApp( this );
        trainTab = new Tab( "Train", train );
        trainTab.setDisable( true );

        FXMLLoader testLoader = new FXMLLoader();
        testLoader.setLocation( getClass().getResource( "view/Test.fxml" ) );
        AnchorPane test = testLoader.load();
        testController = testLoader.getController();
        testController.setApp( this );
        testTab = new Tab( "Test Network", test );
        testTab.setDisable( true );

        FXMLLoader networkGraphLoader = new FXMLLoader();
        networkGraphLoader.setLocation( getClass().getResource( "view/NetworkGraph.fxml" ) );
        AnchorPane visualize = networkGraphLoader.load();
        visualizeController = networkGraphLoader.getController();
        visualizeTab = new Tab( "Visualize Network", visualize );
        visualizeTab.setDisable( true );

        tabPane.getTabs().addAll( setupTab, trainTab, testTab, visualizeTab );
    }

    /**
     * Initializes the neural network. Moreover train, test tables are enumerated, and initial network graph is
     * rendered.
     *
     * @param inputData  List of input column names.
     * @param outputData List of output column names.
     */
    public void setupNetwork(ObservableList<ColumnHead> inputData, ObservableList<ColumnHead> outputData) {
        network = new Network( inputData.size(), inputData.size() + 1, outputData.size(), false );

        trainController.setTable( inputData, outputData );
        trainTab.setDisable( false );

        testController.setTable( inputData, outputData );
        testTab.setDisable( false );

        visualizeController.initializeData( inputData, outputData );
        visualizeController.render( network.getAllWeights() );
        visualizeTab.setDisable( false );
    }

    /**
     * Calls the training thread that in turn calls the train function of the network.
     *
     * @param input     2d-array of input data. index 0 -> row, index 1 -> col.
     * @param output    2d-array of output data. index 0 -> row, index 1 -> col.
     * @param numEpochs the number of times the network will validate the expected outputs with actual outputs.
     */
    public void train(double[][] input, double[][] output, int numEpochs) {
        Thread loadingThread = new Thread( () -> {
            setupTab.setDisable( true );
            trainController.running( true );
            testTab.setDisable( true );
            visualizeTab.setDisable( true );

            network.setTrainingInputs( input );
            network.setTrainingOutputs( output );
            network.train( numEpochs );

            setupTab.setDisable( false );
            trainController.running( false );
            testTab.setDisable( false );
            visualizeTab.setDisable( false );
        } );
        loadingThread.setDaemon( true );
        loadingThread.start();
        visualizeController.render( network.getAllWeights() );
    }

    /**
     * Gets the actual calculation of data.
     *
     * @param inputs 2d-array of input data. index 0 -> row, index 1 -> col.
     * @return The actual calculation results of the 2d-data array.
     */
    public int[][] test(double[][] inputs) {
        return network.calculateOutput( inputs );
    }

}
