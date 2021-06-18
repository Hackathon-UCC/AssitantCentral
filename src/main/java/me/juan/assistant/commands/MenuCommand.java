package me.juan.assistant.commands;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.schema.Attachment;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.CardImage;
import com.microsoft.bot.schema.ThumbnailCard;
import me.juan.assistant.persistence.entity.User;
import me.juan.assistant.utils.CommonUtil;

import java.util.*;
import java.util.stream.Collectors;

public class MenuCommand extends Command {

    public MenuCommand() {
        super(Arrays.asList("opciones", "mostrar menu", "que puedo hacer"), "menu", "Mirar el menu de action.", null, (CardAction) null);
    }

    @Override
    public void execute(User user, String command) {
        ThumbnailCard heroCard = new ThumbnailCard();
        heroCard.setTitle("•  Menu principal •");
        heroCard.setSubtitle("Hola " + user.getAlias() + ", ¿Que quieres hacer hoy?");
        heroCard.setText(String.valueOf(CommonUtil.getDate()));
        heroCard.setImage(new CardImage("https://i.ibb.co/xJYhBqP/icons8-inspection-720px-1.png"));
        heroCard.setButtons(getCommands().stream().filter(cmmand -> cmmand.isAvailable(user)).map(Command::getCardAction).filter(Objects::nonNull).sorted(Comparator.comparing(CardAction::getTitle)).collect(Collectors.toCollection(ArrayList::new)));
        user.sendMessage(MessageFactory.attachment(heroCard.toAttachment()));
    }

    public Attachment toAttachment(Object ob) {
        Attachment attachment = new Attachment();
        attachment.setContent(ob);
        attachment.setContentType("application/vnd.microsoft.card.adaptive");
        return attachment;
    }

}
