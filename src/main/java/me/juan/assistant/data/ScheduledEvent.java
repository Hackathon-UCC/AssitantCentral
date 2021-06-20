package me.juan.assistant.data;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Setter
public class ScheduledEvent {

    private ArrayList<String> specifications;
    private String details, description, startsAt, endsAt;


    public void print() {
        System.out.println(new Gson().toJson(this));
    }

}
