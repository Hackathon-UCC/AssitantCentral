package me.juan.assistant.commands;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.schema.*;
import me.juan.assistant.persistence.entity.User;

import java.util.*;
import java.util.stream.Collectors;

public class MenuCommand extends Command {

    public MenuCommand() {
        super(Arrays.asList("opciones", "mostrar menu", "que puedo hacer"), "menu", "Mirar el menu de action.", null, (CardAction) null);
    }

    @Override
    public void execute(User user, String command) {
        ThumbnailCard heroCard = new ThumbnailCard();
        heroCard.setTitle("•  Menu principal  •");
        heroCard.setSubtitle("Hola "+user.getAlias()+", ¿Que quieres hacer?");
        heroCard.setText(String.valueOf(new Date()));
        heroCard.setImage(new CardImage("https://i.ibb.co/q772ycZ/icons8-menu-1080px.png"));
        heroCard.setButtons(getCommands().stream().filter(cmand -> cmand.isAvailable(user)).map(Command::getCardAction).filter(Objects::nonNull).sorted(Comparator.comparing(CardAction::getTitle)).collect(Collectors.toCollection(ArrayList::new)));
        user.sendMessage(MessageFactory.attachment(heroCard.toAttachment()));
    }

    public Attachment toAttachment(Object ob) {
        Attachment attachment = new Attachment();
        attachment.setContent(ob);
        attachment.setContentType("application/vnd.microsoft.card.adaptive");
        return attachment;
    }

}
