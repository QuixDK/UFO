package com.quixdk.UFOBot.controller;


import com.quixdk.UFOBot.config.BotConfig;
import com.quixdk.UFOBot.controller.callback.CallBackController;
import com.quixdk.UFOBot.handler.commands.StartCommandHandler;
import com.quixdk.UFOBot.handler.messages.Messages;
import com.quixdk.UFOBot.model.UserStates;
import com.quixdk.UFOBot.model.Users;

import com.quixdk.UFOBot.service.UsersServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Configurable
@AllArgsConstructor
public class MainBotController extends TelegramLongPollingBot {

    private final BotConfig config;
    private final StartCommandHandler startCommandHandler;
    private final Messages messages;
    private final CallBackController callBackController;
    private final UsersServiceImpl usersService;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        boolean updateHasMessage = update.hasMessage() && update.getMessage().hasText();
        boolean updateHasCallBackQuery = update.hasCallbackQuery();
        try {
            if (updateHasMessage) {
                Users user = checkUser(update);
                sendAnswer(user, update);
            } else if (updateHasCallBackQuery) {
                Users user = usersService.findUserByChatId(update.getCallbackQuery().getMessage().getChatId());
                if (user != null) {
                    execute(callBackController.handle(update));

                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private UserStates checkUserState(Users user) {
        return user.getUserState();
    }

    private void sendAnswer(Users user, Update update) throws TelegramApiException {

        UserStates userState = checkUserState(user);
        String textMessage = update.getMessage().getText();
        if (userState == UserStates.StateStart) {
            for (var message: startCommandHandler.handle(update, user)) {
                execute(message);
            }

        }
        else {
            if (textMessage.equals("Меню")) {
                usersService.updateUserState(user, UserStates.StateMenu);
                execute(messages.sendStartMessage(user));
            } else {
                execute(messages.sendErrorMessage(user));
            }
        }

    }

    private Users checkUser(Update update) {
        Long chatID = update.getMessage().getChatId();
        Users users = usersService.findUserByChatId(chatID);
        //If exist return Users
        if (users != null) {
            return users;
        }
        //Else create new Users
        users = usersService.saveUser(chatID, update);
        log.info("New user with chat ID: " + chatID + " saved!");
        return users;
    }

}
