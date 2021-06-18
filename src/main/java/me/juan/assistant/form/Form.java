package me.juan.assistant.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.Attachment;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.TextBlock;
import me.juan.assistant.persistence.entity.User;

import java.util.*;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Accessors(chain = true)
public class Form {

    private final String
            $schema = "http://adaptivecards.io/schemas/adaptive-card.json",
            type = "AdaptiveCard",
            version = "1.3";
    private String speak;
    private final ArrayList<Object> body;
    @JsonIgnore
    private final Double formId;
    private ArrayList<Action> actions;
    private ArrayList<Object> buttons;

    @JsonIgnore
    private FormResponse formResponse;


    public Form(String title, Object... fF) {
        if(title != null) {
            this.speak = title;
            this.body = new ArrayList<>(Collections.singletonList(new TextBlock("**" + title + "**").setSpacing("medium").setSize("medium").setHorizontalAlignment("center").setWeight("bolder").setSeparator(true)));
        }
        else this.body = new ArrayList<>();
        this.body.addAll(Arrays.asList(fF));
        this.formId = Math.random() * 10000;
    }

    public void sendAsMessage() {

    }

    public Form(Object... fF) {
        this(null,fF);
    }

    public Activity get() {
        return MessageFactory.attachment(toAttachment());
    }

    public Map<String, String> response(User user) {
        String input = user.input();
        while (!(input.contains("{") || input.contains("}") || input.contains("="))) {
            input = user.input();
        }
        input = input.replaceAll("}", "").replaceAll("\\{", "").replaceAll(",", "?");
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        Arrays.stream(input.split("\\?")).parallel().forEach(s -> {
            String[] split1 = s.split("=");
            objectObjectHashMap.put(split1[0].replace(" ", ""), split1.length <2 ? null : split1[1]);
        });
        Object formId = objectObjectHashMap.getOrDefault("formId", null);
        if (formId == null || !formId.equals(this.formId.toString())) return response(user);
        return objectObjectHashMap;
    }

    public FormResponse send(User user, String... prevMessages) {
        ArrayList<Activity> activities = new ArrayList<>();
        for (String s : prevMessages) { activities.add(MessageFactory.text(s)); }
        activities.add(get());
        user.sendMessage(activities);
        formResponse = new FormResponse(response(user));
        return formResponse;
    }

    public FormResponse send(User user) {
        user.sendMessage(get());
        formResponse = new FormResponse(response(user));
        return formResponse;
    }

    public Form setActions(Action... fF) {
        this.actions = new ArrayList<>(Arrays.asList(fF));
        return this;
    }

    public Form setButtons(Object... obs) {
        this.buttons = new ArrayList<>(Arrays.asList(obs));
        return this;
    }

    public Attachment toAttachment() {
        if(actions != null) actions.forEach(formField -> formField.addData("formId", formId));
        Attachment attachment = new Attachment();
        attachment.setContent(this);
        attachment.setContentType("application/vnd.microsoft.card.adaptive");
        return attachment;
    }
}
