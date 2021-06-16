package me.juan.assistant.form.field;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FieldType;

@Setter
@Accessors(chain = true)
public class DateInput extends TextInput {

    private String max, min;

    public DateInput() {
        super(FieldType.INPUT_DATE);
    }


}

