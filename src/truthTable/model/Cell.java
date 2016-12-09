package truthTable.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by tbyacoub on 12/6/2016.
 */
public class Cell {

    private final SimpleStringProperty value;

    public enum TYPE {INPUT, OUTPUT}

    private TYPE type;

    public Cell(TYPE type) {
        this.value = new SimpleStringProperty("0");
        this.type = type;
    }

    public Cell(TYPE type, String value) {
        this.value = new SimpleStringProperty(value);
        this.type = type;
    }

    public void toggle() {
        switch (value.getValue()) {
            case "0":
                value.setValue("1");
                break;
            case "1":
                value.setValue("0");
                break;
        }
    }

    public void setValue(int value) {
        this.value.setValue(String.valueOf(value));
    }

    public String getValue(){
        return value.getValue();
    }

    public StringProperty getValueProperty(){
        return value;
    }
    public TYPE getType() { return type; }

    public String toString(){
        return getValue();
    }

}
