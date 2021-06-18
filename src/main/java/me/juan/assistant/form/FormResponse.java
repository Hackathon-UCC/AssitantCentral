package me.juan.assistant.form;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class FormResponse {

    private final Map<String, String> response;

    public boolean isCanceled() {
        return response.containsKey("cancel");
    }

    public String get(String id) {
        return response.getOrDefault(id, null);
    }

}
