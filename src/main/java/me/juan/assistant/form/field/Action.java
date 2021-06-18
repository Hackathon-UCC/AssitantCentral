package me.juan.assistant.form.field;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.FormField;

import java.util.HashMap;

@Setter
@Accessors(chain = true)
public class Action extends FormField {

    private final String title;
    private String fallback, associatedInputs, style;
    private HashMap<String, Object> data;

    public Action(FieldType type, String title) {
        super(type);
        this.title = title;
    }

    public Action(String title) {
        super(FieldType.ACTION_SUBMIT);
        this.title = title;
    }

    public Action addData(String k, Object v) {
        if (data == null) data = new HashMap<>();
        data.put(k, v);
        return this;
    }

    public Action setCancel() {
        addData("cancel", true);
        return setAssociatedInputs("none");
    }

    public Action addStyle() {
        setStyle("positive");
        return this;
    }

}
