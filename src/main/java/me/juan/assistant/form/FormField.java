package me.juan.assistant.form;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class FormField {

    private final String type;
    private String id;
    private boolean separator;
    private Object card;

    public FormField(FieldType type) {
        this.type = type.getTypeName();
    }

    public FormField(String type) {
        this.type = type;
    }

    public FormField setCard(Object card) {
        this.card = card;
        return this;
    }

    public FormField setId(String id) {
        this.id = id;
        return this;
    }
}
