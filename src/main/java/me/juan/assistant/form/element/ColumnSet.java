package me.juan.assistant.form.element;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public class ColumnSet {

    private String type = "ColumnSet", verticalContentAlignment = "Center", horizontalAlignment = "Center";
    private ArrayList<Column> columns;


    public ColumnSet setColumns(Column... fF) {
        this.columns = new ArrayList<>(Arrays.asList(fF));
        return this;
    }

    public ColumnSet addColumn(Object... fF) {
        if(this.columns == null) this.columns = new ArrayList<>();
        this.columns.add(new Column().setItems(fF));
        return this;
    }

    public ColumnSet addColumn(String width, Object... fF) {
        if(this.columns == null) this.columns = new ArrayList<>();
        this.columns.add(new Column().setItems(fF).setWidth(width));
        return this;
    }

}