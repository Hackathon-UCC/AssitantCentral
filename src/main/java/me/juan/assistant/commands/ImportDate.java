package me.juan.assistant.commands;

import me.juan.assistant.data.ScheduledEvent;
import me.juan.assistant.form.Form;
import me.juan.assistant.form.FormResponse;
import me.juan.assistant.form.field.Action;
import me.juan.assistant.form.field.TextInput;
import me.juan.assistant.persistence.entity.Role;
import me.juan.assistant.persistence.entity.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ImportDate extends Command {

    public ImportDate() {
        super(Collections.emptyList(), "Ingresar fecha", null, Collections.singletonList(Role.ADMINISTRATOR), "Ingresar fecha");
    }

    @Override
    public void execute(User user, String command) {
        FormResponse send = new Form("Ingresar fechas", new TextInput().setPlaceholder("Url del repositorio.").setId("url")).setActions(new Action("Enviar").addStyle(), new Action("Cancelar").setCancel()).send(user);
        String urlString = send.get("url");
        if (send.isCanceled() || urlString == null) {
            user.sendMessage("Operación cancelada.");
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlString).openConnection().getInputStream()));
            String line;
            ArrayList<String> specifications = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.equals("")) {
                    specifications = new ArrayList<>();
                    continue;
                }
                line = line.trim().replaceAll(" {2}", " ");
                String[] spaceSplit = line.split(" ");
                ScheduledEvent scheduledEvent = deserializeEvent(line, specifications, spaceSplit);
                if(scheduledEvent == null) {
                    while (Character.isDigit(line.charAt(0)) || line.charAt(0) == '.' || line.startsWith(" ")) { line = line.substring(1); }
                    if (specifications.size() >= 2) specifications.remove(specifications.size() - 1);
                    specifications.add(line);
                    continue;
                }
                scheduledEvent.print();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ScheduledEvent deserializeEvent(String line, ArrayList<String> specifications, String[] spaceSplit) {
        ScheduledEvent scheduledEvent = null;
        if (line.contains("\"") && line.contains("/")) {
            boolean isLastStringDate = isDate(spaceSplit[spaceSplit.length - 1]);
            String dateComentado = line.split("\"")[1].trim(), startAt = isLastStringDate ? dateComentado : Arrays.stream(spaceSplit).filter(this::isDate).collect(Collectors.joining()), endsAt = isLastStringDate ? spaceSplit[spaceSplit.length - 1] : dateComentado, details = line.replace(startAt, "").replace(endsAt, "").replaceAll("\"", "").trim();
            scheduledEvent = new ScheduledEvent().setSpecifications(specifications).setDetails(details).setStartsAt(startAt).setEndsAt(endsAt);
        } else if (line.contains("\"")) {
            String description = line.split("\"")[1].trim(), details = line.replaceAll("\"", "").replace(description, "").trim();
            scheduledEvent = new ScheduledEvent().setSpecifications(specifications).setDetails(details).setDescription(description);
        } else if (line.contains("/")) {
            ArrayList<String> collect = Arrays.stream(spaceSplit).filter(this::isDate).collect(Collectors.toCollection(ArrayList::new));
            for (String s : collect) { line = line.replace(s, ""); }
            scheduledEvent = new ScheduledEvent().setSpecifications(specifications).setDetails(line.trim()).setStartsAt(collect.get(0)).setEndsAt(collect.size() == 2 ? collect.get(1) : "No definido.");
        }
        return scheduledEvent;
    }

    public boolean isDate(String spliced) {
        return spliced.split("/").length == 3;
    }

}
