// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package me.juan.assistant;

import com.google.common.collect.Iterables;
import com.microsoft.bot.builder.Bot;
import com.microsoft.bot.integration.AdapterWithErrorHandler;
import com.microsoft.bot.integration.BotFrameworkHttpAdapter;
import com.microsoft.bot.integration.ClasspathPropertiesConfiguration;
import com.microsoft.bot.integration.Configuration;
import com.microsoft.bot.integration.spring.BotController;
import com.microsoft.bot.integration.spring.BotDependencyConfiguration;
import lombok.Getter;
import me.juan.assistant.commands.ReminderCommand;
import me.juan.assistant.commands.AliasCommand;
import me.juan.assistant.commands.CityCommand;
import me.juan.assistant.commands.MenuCommand;
import me.juan.assistant.listener.ReceptionListener;
import me.juan.assistant.manager.UserManager;
import me.juan.assistant.persistence.entity.User;
import me.juan.assistant.persistence.repository.CampusRepository;
import me.juan.assistant.persistence.repository.UserCampusRepository;
import me.juan.assistant.persistence.repository.UserRepository;
import me.juan.assistant.task.MessageTask;
import me.juan.assistant.utils.CommonUtil;
import me.juan.event.EventManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({BotController.class})
@Getter
public class Application extends BotDependencyConfiguration {

    @Getter
    private static BotFrameworkHttpAdapter botFrameworkHttpAdapter;
    @Getter
    private static Configuration configurationFile;
    @Getter
    private static Application instance;

    private final UserRepository userRepository;
    private final CampusRepository campusRepository;
    private final UserCampusRepository userCampusRepository;

    public Application(UserRepository userRepository, CampusRepository campusRepository, UserCampusRepository userCampusRepository) {
        instance = this;
        configurationFile = new ClasspathPropertiesConfiguration();
        this.userRepository = userRepository;
        this.campusRepository = campusRepository;
        this.userCampusRepository = userCampusRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void loadTasks() {
        new MessageTask(10);
    }

    public void loadCommands() {
        new MenuCommand();
        new ReminderCommand();
        new CityCommand();
        new AliasCommand();
    }

    public void loadRegisteredUsers() {
        Iterable<User> all = userRepository.findAll();
        all.forEach(user -> new UserManager(user, user.getConversationReference()));
        CommonUtil.info(Iterables.size(all) + " users were loaded from the database!");
    }

    @Bean
    public Bot getBot() {
        loadTasks();
        loadRegisteredUsers();
        loadCommands();
        ReceptionListener receptionListener = new ReceptionListener();
        EventManager.registerEvent(receptionListener);
        return receptionListener;
    }

    @Override
    public BotFrameworkHttpAdapter getBotFrameworkHttpAdaptor(Configuration configuration) {
        if (botFrameworkHttpAdapter == null) botFrameworkHttpAdapter = new AdapterWithErrorHandler(configuration);
        return botFrameworkHttpAdapter;
    }
}
