package me.juan.assistant.form.field;

import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.FieldType;
import me.juan.assistant.form.FormField;

import java.util.ArrayList;

@Setter
@Accessors(chain = true)
public class SelectionInput extends FormField {

    private String value, placeholder, errorMessage, style;
    private Boolean wrap, isMultiSelect, isRequired, separator;
    private ArrayList<Choice> choices;

    public SelectionInput() {
        super(FieldType.INPUT_CHOICE);
    }

    public SelectionInput setChoices(String... cs) {
        ArrayList<Choice> objects = new ArrayList<>();
        for (String c : cs) {
            objects.add(new Choice(c));
        }
        this.choices = objects;
        return this;
    }

    public SelectionInput setChoices(ArrayList<String> choices) {
        ArrayList<Choice> objects = new ArrayList<>();
        for (String choice : choices) {
            objects.add(new Choice(choice));
        }
        this.choices = objects;
        return this;
    }

    public SelectionInput setRequired(String errorMessage) {
        this.errorMessage = errorMessage;
        this.isRequired = errorMessage == null ? null : true;
        return this;
    }


    public static class Choice {
        private final String title, value;

        public Choice(String title) {
            this.title = title;
            this.value = title;
        }
    }

}
