package me.juan.assistant.persistence.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationReference;
import com.microsoft.bot.schema.teams.TeamsChannelAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import me.juan.assistant.manager.MessageManager;
import me.juan.assistant.manager.UserManager;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class User {

    @Getter
    private static final HashMap<Integer, ConversationReference> conversationReferenceCache = new HashMap<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String alias;
    private String name;
    private String email;
    private Boolean active;

    @Column(columnDefinition = "ENUM('STUDENT', 'TEACHER', 'ADMINISTRATOR')")
    @Enumerated(EnumType.STRING)
    private Role role;

    private Timestamp registered_at;
    private Timestamp updated_at;
    private String id_teams;
    private String city;

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
        this.active = true;
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
        getMessageManager().sendMessage(msg);
    }

    public void sendMessage(String... a) {
        getMessageManager().sendMessage(a);
    }

    public void sendMessage(Activity a) {
        getMessageManager().sendMessage(a);
    }

    public void sendMessage(Activity... a) {
        getMessageManager().sendMessage(a);
    }

    public MessageManager getMessageManager() {
        return getManager().getMessageManager();
    }

    public String input() {
        return getMessageManager().input();
    }

    public boolean isRegistering() {
        return UserManager.getUsersRegistering().containsKey(email);
    }

    @JsonIgnore
    public String getDomain() {
        String[] domainSplit = email.split("@");
        return domainSplit.length == 2 ? domainSplit[1] : email;
    }

    @Override
    public boolean equals(Object user) {
        return ((User) user).getId().equals(getId());
    }

    public void sendMessage(ArrayList<Activity> activities) {
        getMessageManager().sendMessage(activities);
    }
}
