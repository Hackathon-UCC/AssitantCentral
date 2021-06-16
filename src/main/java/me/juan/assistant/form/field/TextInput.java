package me.juan.assistant.form.field;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.FormField;

@Setter
@Accessors(chain = true)
public class TextInput extends FormField {

    private String style = "text";
    private Integer maxLength;
    private Boolean isRequired = null, isMultiline = null;
    private String placeholder, label, errorMessage, regex;
    private Object value;

    public TextInput() {
        super(FieldType.INPUT_TEXT);
    }

    public TextInput(FieldType inputTime) {
        super(inputTime);
    }

    public TextInput setRequired(String errorMessage) {
        this.errorMessage = errorMessage;
        this.isRequired = true;
        return this;
    }

}
