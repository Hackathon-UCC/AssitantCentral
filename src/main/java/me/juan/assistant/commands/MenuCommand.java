package me.juan.assistant.commands;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.schema.Attachment;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.HeroCard;
import me.juan.assistant.persistence.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuCommand extends Command {

    public MenuCommand() {
        super(Arrays.asList("opciones", "mostrar menu", "que puedo hacer"), "menu", "Mirar el menu de action.", null, (CardAction) null);
    }

    @Override
    public void execute(User user, String command) {

        /*String content = null;
        try {
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "card.json");
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        HeroCard heroCard = new HeroCard();
        heroCard.setText("texto");
        heroCard.setTitle("•  Menu principal  •");
        heroCard.setSubtitle(String.valueOf(new Date()));
        heroCard.setButtons(getCommands().stream().map(Command::getCardAction).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new)));
        user.sendMessage(MessageFactory.attachment(heroCard.toAttachment()));
    }

    public Attachment toAttachment(Object ob) {
        Attachment attachment = new Attachment();
        attachment.setContent(ob);
        attachment.setContentType("application/vnd.microsoft.card.adaptive");
        return attachment;
    }

}
