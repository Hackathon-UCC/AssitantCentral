package me.juan.assistant.form.field;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FieldType;

@Setter
@Accessors(chain = true)
public class TimeInput extends TextInput {

    private String max, min;

    public TimeInput() {
        super(FieldType.INPUT_TIME);
    }

}
