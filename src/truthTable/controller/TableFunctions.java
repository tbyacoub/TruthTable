package truthTable.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import truthTable.TruthTableApp;
import truthTable.model.Cell;
import truthTable.model.ColumnHead;


public class TableFunctions {

    protected enum TYPE {TEST, TRAIN}

    protected TYPE type;

    protected int colCount = 0;

    protected int inputCount = 0;

    protected TruthTableApp app;

    protected ObservableList<ObservableList<Cell>> data = FXCollections.observableArrayList();

    @FXML
    protected TableView<ObservableList<Cell>> table;

    /**
     * Initially create a table with the given data.
     *
     * @param inputData  ObservableList the holds input column data.
     * @param outputData ObservableList the holds output column data.
     */
    public void setTable(final ObservableList<ColumnHead> inputData, final ObservableList<ColumnHead> outputData) {
        inputCount = inputData.size();
        table.getColumns().clear();
        table.getItems().clear();
        data.clear();

        int i = 0;
        for (ColumnHead column : inputData) {
            createColumns( column, i++ );
        }

        for (ColumnHead columns : outputData) {
            createColumns( columns, i++ );
        }

        colCount = i;
    }

    /**
     * Column factory.
     *
     * @param column Column data.
     * @param colNum Column index.
     */
    private void createColumns(ColumnHead column, final int colNum) {
        TableColumn<ObservableList<Cell>, String> col = new TableColumn<>( column.getName() );
        col.setCellValueFactory( param -> param.getValue().get( colNum ).getValueProperty() );
        table.getColumns().add( col );
    }

    /**
     * Enumerates a table with half of the data set starting with 0.
     *
     * @param start starting Integer of the data set.
     */
    protected void enumerateTable(int start) {
        int rowsCount = (int) Math.pow( 2, inputCount );
        for (int i = start; i < rowsCount; i = i + 2) {
            addRow( Integer.toBinaryString( i ) );
        }
    }

    /**
     * Row factory.
     *
     * @param binary Binary String to be inserted in all cells of this row.
     */
    private void addRow(String binary) {
        ObservableList<Cell> row = FXCollections.observableArrayList();
        String fixedBinary = binary;
        while (fixedBinary.length() < inputCount) {
            fixedBinary = "0" + fixedBinary;
        }
        for (int i = 0; i < inputCount; i++) {
            row.add( new Cell( Cell.TYPE.INPUT, "" + fixedBinary.charAt( i ) ) );
        }
        for (int j = 0; j < colCount - inputCount; j++) {
            switch (type) {
                case TEST:
                    row.add( new Cell( Cell.TYPE.OUTPUT, "-" ) );
                    break;
                case TRAIN:
                    row.add( new Cell( Cell.TYPE.OUTPUT ) );
                    break;
            }
        }
        data.add( row );
    }

    public void setApp(TruthTableApp app) {
        this.app = app;
    }
}
