package truthTable.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import truthTable.model.Cell;
import truthTable.model.ColumnHead;
import truthTable.controller.TableFunctions;

public class TestController extends TableFunctions{

    @FXML
    private Button test;

    @FXML
    private void initialize() {
        type = TYPE.TEST;
        table.setItems(data);
    }

    @FXML
    private void handleTestMouseClick() {

        int inputRowCount = 0;
        int inputColCount;
        int outputRowCount = 0;
        int outputColCount;
        double[][] input = new double[data.size()][inputCount];
        int[][] output = new int[data.size()][colCount - inputCount];
        for (ObservableList<Cell> curRow : data) {
            inputColCount = 0;
            for (Cell curCell : curRow) {
                switch (curCell.getType()) {
                    case INPUT:
                        input[inputRowCount][inputColCount++] = Integer.parseInt(curCell.getValue());
                        break;
                }
            }
            inputRowCount++;
        }

        output = app.test(input);

        for (ObservableList<Cell> curRow : data) {
            outputColCount = 0;
            for (Cell curCell : curRow) {
                switch (curCell.getType()) {
                    case OUTPUT:
                        curCell.setValue(output[outputRowCount][outputColCount++]);
                        break;
                }
            }
            outputRowCount++;
        }
    }

    public void setTable(ObservableList<ColumnHead> inputData, ObservableList<ColumnHead> outputData) {
        super.setTable(inputData, outputData);
        enumerateTable(1);
    }
}
