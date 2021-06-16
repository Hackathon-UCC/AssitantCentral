package me.juan.assistant.form;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FieldType {
    TEXT("TextBlock"),
    INPUT_TEXT("Input.Text"),
    INPUT_NUMBER("Input.Number"),
    INPUT_DATE("Input.Date"),
    INPUT_TIME("Input.Time"),
    INPUT_CHOICE("Input.ChoiceSet"),
    INPUT_TOGGLE("Input.Toggle"),
    ACTION_SHOWCARD("Action.ShowCard"),
    ACTION_SUBMIT("Action.Submit");

    private final String typeName;

}
