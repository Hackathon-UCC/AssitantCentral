package me.juan.assistant.persistence.entity;

import com.google.gson.Gson;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationReference;
import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.juan.assistant.manager.MessageManager;
import me.juan.assistant.manager.UserManager;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Getter
    private static final HashMap<Integer, ConversationReference> conversationReferenceCache = new HashMap<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String alias;
    private String name;
    private String email;
    private boolean active;

    @Column(columnDefinition = "ENUM('STUDENT', 'TEACHER', 'ADMINISTRATOR')")
    @Enumerated(EnumType.STRING)
    private Role role;

    private Timestamp registered_at;
    private Timestamp updated_at;
    private String id_teams;

    @Column(name = "conversation_reference")
    private String conversationReference;

    @OneToMany(mappedBy = "user")
    private List<UserCampus> userCampuses;

    @SneakyThrows
    public User(TeamsChannelAccount teamsChannelAccount, TurnContext turnContext) {
        this.name = teamsChannelAccount.getName();
        this.alias = this.name;
        this.email = teamsChannelAccount.getEmail();
        this.id_teams = teamsChannelAccount.getId();
        this.role = Role.STUDENT;
        this.registered_at = new Timestamp(System.currentTimeMillis());
        this.updated_at = this.registered_at;
        this.conversationReference = new Gson().toJson(turnContext.getActivity().getConversationReference());
    }

    private Campus getCampus() {
        return userCampuses != null && !userCampuses.isEmpty() ? userCampuses.get(0).getCampus() : null;
    }

    public ConversationReference getConversationReference() {
        ConversationReference conversationReferenceCache = User.conversationReferenceCache.getOrDefault(id, null);
        if (conversationReferenceCache == null) {
            conversationReferenceCache = new Gson().fromJson(conversationReference, ConversationReference.class);
            User.conversationReferenceCache.put(id, conversationReferenceCache);
        }
        return conversationReferenceCache;
    }

    public void setConversationReference(TurnContext turnContext) {
        ConversationReference conversationReference = turnContext.getActivity().getConversationReference();
        conversationReferenceCache.put(id, conversationReference);
        this.conversationReference = new Gson().toJson(conversationReference);
    }

    public UserManager getManager() {
        return UserManager.createOrFind(this, this.getConversationReference());
    }

    public void sendMessage(String msg) {
        getManager().getMessageManager().sendMessage(msg);
    }

    public void sendMessage(String... a) {
        getManager().getMessageManager().sendMessage(a);
    }

    public void sendMessage(Activity a) { getManager().getMessageManager().sendMessage(a); }

    public void sendMessage(Activity... a) {
        getManager().getMessageManager().sendMessage(a);
    }

    @JsonIgnore
    public String getDomain() {
        String[] domainSplit = email.split("@");
        return domainSplit.length == 2 ? domainSplit[1] : email;
    }

}
