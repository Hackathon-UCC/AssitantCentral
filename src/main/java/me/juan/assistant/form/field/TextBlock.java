package me.juan.assistant.form.field;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.FormField;

@Setter
@Accessors(chain = true)
public class TextBlock extends FormField {

    private String size, border, text, horizontalAlignment, weight, fontType, color, spacing;
    private boolean wrap = true;
    private boolean isSubtle;

    public TextBlock(String text) {
        super(FieldType.TEXT);
        this.text = text;
    }
}
