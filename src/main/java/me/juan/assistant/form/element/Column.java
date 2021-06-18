package me.juan.assistant.form.element;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
@Accessors(chain = true)
public class Column {
    private String type = "Column", verticalContentAlignment = "Center", horizontalAlignment = "Center", width;

    private ArrayList<Object> items;
    public Column setItems(Object... fF) {
        this.items = new ArrayList<>(Arrays.asList(fF));
        return this;
    }


}