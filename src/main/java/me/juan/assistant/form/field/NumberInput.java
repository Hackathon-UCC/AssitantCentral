package me.juan.assistant.form.field;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FieldType;

@Setter
@Accessors(chain = true)
public class NumberInput extends TextInput {

    private Integer min, max;

    public NumberInput() {
        super(FieldType.INPUT_NUMBER);
    }
}
