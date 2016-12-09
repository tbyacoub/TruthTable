package truthTable.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by tbyacoub on 12/6/2016.
 */
public class ColumnHead {

    private final SimpleIntegerProperty id;

    private final SimpleStringProperty name;

    public ColumnHead(int id){
        this(id, Character.toString((char) (64 + id)));
    }

    public ColumnHead(int id, String name){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public String getName(){
        return name.get();
    }

    public int getId(){
        return id.get();
    }

}
