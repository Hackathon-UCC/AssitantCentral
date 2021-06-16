package me.juan.assistant.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.bot.builder.MessageFactory;
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
    private final ArrayList<Object> body;
    private ArrayList<Action> actions;
    private ArrayList<Object> buttons;
    @JsonIgnore
    private final Double formId;


    public Form(String title, Object... fF) {
        this.body = new ArrayList<>(Collections.singletonList(new TextBlock("**"+title+"**").setSpacing("medium").setSize("medium").setHorizontalAlignment("center").setWeight("bolder").setSeparator(true)));
        this.body.addAll(Arrays.asList(fF));
        this.formId = Math.random() * 10000;
    }

    public Map<String, String> response(User user) {
        String input = user.input();
        while(!(input.contains("{") || input.contains("}") || input.contains("="))) { input = user.input(); }
        input = input.replaceAll("}", "").replaceAll("\\{", "").replaceAll(",", "?");

        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        Arrays.stream(input.split("\\?")).parallel().forEach(s -> { String[] split1 = s.split("=");objectObjectHashMap.put(split1[0].replace(" ", ""), split1[1]); });
        Object formId = objectObjectHashMap.getOrDefault("formId", null);
        if(formId == null || !formId.equals(this.formId.toString())) return response(user);
        return objectObjectHashMap;
    }

    public Map<String, String> send(User user) {
        user.sendMessage(MessageFactory.attachment(toAttachment()));
        return response(user);
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
        actions.forEach(formField -> formField.addData("formId", formId));
        Attachment attachment = new Attachment();
        attachment.setContent(this);
        attachment.setContentType("application/vnd.microsoft.card.adaptive");
        return attachment;
    }

    public class ColumnSet {

        private String type = "ColumnSet";
        private ArrayList<Action> columns;


        public Form.ColumnSet setActions(Action... fF) {
            this.columns = new ArrayList<>(Arrays.asList(fF));
            return this;
        }

        public class Column {
            private String type = "Column";

            private ArrayList<Action> items;

            public Column setActions(Action... fF) {
                this.items = new ArrayList<>(Arrays.asList(fF));
                return this;
            }
        }

    }

}
