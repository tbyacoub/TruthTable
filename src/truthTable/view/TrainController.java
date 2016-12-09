package truthTable.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import truthTable.controller.TableFunctions;
import truthTable.model.Cell;
import truthTable.model.ColumnHead;

public class TrainController extends TableFunctions {

    @FXML
    private Button train;

    @FXML
    private TextField epochs;

    @FXML
    private void initialize() {
        type = TYPE.TRAIN;
        table.setItems(data);
        table.setRowFactory(param -> {
            TableRow<ObservableList<Cell>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    ObservableList clickedRow = row.getItem();
                    int colNum = row.getTableView().getEditingCell().getColumn();
                    if (colNum >= inputCount) {
                        Cell clickedCell = (Cell) clickedRow.get(row.getTableView().getEditingCell().getColumn());
                        clickedCell.toggle();
                    }
                }
            });
            return row;
        });
    }

    @FXML
    private void handleTrainMouseClick() {
        if (epochs.getText().isEmpty() || epochs.getText().trim().length() < 1) {
            return;
        }

        int numEpochs;
        try {
            numEpochs = Integer.parseInt(epochs.getText());
        } catch (NumberFormatException e) {
            return;
        }

        int inputRowCount = 0;
        int inputColCount;
        int outputRowCount = 0;
        int outputColCount;
        double[][] input = new double[data.size()][inputCount];
        double[][] output = new double[data.size()][colCount - inputCount];
        for (ObservableList<Cell> curRow : data) {
            inputColCount = 0;
            outputColCount = 0;
            for (Cell curCell : curRow) {
                switch (curCell.getType()) {
                    case INPUT:
                        input[inputRowCount][inputColCount++] = Integer.parseInt(curCell.getValue());
                        break;
                    case OUTPUT:
                        output[outputRowCount][outputColCount++] = Integer.parseInt(curCell.getValue());
                        break;
                }
            }
            inputRowCount++;
            outputRowCount++;
        }
        app.train(input, output, numEpochs);
    }

    public void running(boolean disable) {
        train.setDisable( disable );
    }

    public void setTable(ObservableList<ColumnHead> inputData, ObservableList<ColumnHead> outputData) {
        super.setTable(inputData, outputData);
        enumerateTable(0);
    }
}
