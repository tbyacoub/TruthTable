package truthTable.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import truthTable.TruthTableApp;
import truthTable.model.ColumnHead;

public class SetupController {

    private TruthTableApp truthTableApp;

    private enum SPINNER {INPUT, OUTPUT}

    private final ObservableList<ColumnHead> inputData;

    private final ObservableList<ColumnHead> outputData;

    @FXML
    private HBox outputHBox;

    @FXML
    private HBox inputHBox;

    @FXML
    private TableView<ColumnHead> inputTable;

    @FXML
    private TableView<ColumnHead> outputTable;

    @FXML
    private TableColumn<ColumnHead, Integer> inputIdCol;

    @FXML
    private TableColumn<ColumnHead, String> inputNameCol;

    @FXML
    private TableColumn<ColumnHead, Integer> outputIdCol;

    @FXML
    private TableColumn<ColumnHead, String> outputNameCol;

    @FXML
    private void initialize() {
        Spinner<Integer> inputSpinner = new Spinner<>( 1, 6, 4 );
        inputSpinner.setMaxWidth( 60 );
        inputSpinner.valueProperty().addListener( new SpinnerChangeListener( SPINNER.INPUT ) );
        Spinner<Integer> outputSpinner = new Spinner<>( 1, 4, 1 );
        outputSpinner.valueProperty().addListener( new SpinnerChangeListener( SPINNER.OUTPUT ) );
        outputSpinner.setMaxWidth( 60 );

        inputHBox.getChildren().add( inputSpinner );
        outputHBox.getChildren().add( outputSpinner );

        inputIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        inputNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        inputTable.setItems( inputData );

        outputIdCol.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        outputNameCol.setCellValueFactory( new PropertyValueFactory<>( "name" ) );
        outputTable.setItems( outputData );
    }

    @FXML
    private void handleOkOnMouseClick() {
        truthTableApp.setupNetwork( inputData, outputData );
    }

    /**
     * Initializes all default column names.
     */
    public SetupController() {
        inputData = FXCollections.observableArrayList(
                new ColumnHead( 1, "A" ),
                new ColumnHead( 2, "B" ),
                new ColumnHead( 3, "C" ),
                new ColumnHead( 4, "D" )
        );

        outputData = FXCollections.observableArrayList(
                new ColumnHead( 1, "F1" )
        );
    }

    public void setApp(TruthTableApp truthTableApp) {
        this.truthTableApp = truthTableApp;
    }

    private class SpinnerChangeListener implements ChangeListener<Integer> {

        private SPINNER spinner;

        private SpinnerChangeListener(SPINNER spinner) {
            this.spinner = spinner;
        }

        @Override
        public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
            if (oldValue > newValue)
                removeRow( newValue );
            else
                addRow( newValue );
        }

        private void addRow(int index) {
            switch (spinner) {
                case INPUT:
                    inputData.add( new ColumnHead( index ) );
                    break;
                case OUTPUT:
                    outputData.add( new ColumnHead( index, "F" + index ) );
                    break;
            }
        }

        private void removeRow(int index) {
            switch (spinner) {
                case INPUT:
                    inputData.remove( index );
                    break;
                case OUTPUT:
                    outputData.remove( index );
                    break;
            }
        }
    }
}
