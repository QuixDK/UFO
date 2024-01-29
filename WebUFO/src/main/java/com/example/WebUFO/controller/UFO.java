package com.example.WebUFO.controller;


import com.example.WebUFO.config.BotConfig;
import com.example.WebUFO.controller.callback.CallBack;
import com.example.WebUFO.handler.messages.Messages;
import com.example.WebUFO.handler.states.StateStartHandler;
import com.example.WebUFO.model.Users;

import com.example.WebUFO.service.UsersServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Configurable
@AllArgsConstructor
public class UFO extends TelegramLongPollingBot {

    private final BotConfig config;
    private final StateStartHandler stateStartHandler;
    private final Messages messages;
    private final CallBack callBack;
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
                execute(sendAnswer(user, update));
            } else if (updateHasCallBackQuery) {

                Users user = usersService.findUserByChatId(update.getCallbackQuery().getMessage().getChatId());
                if (user != null) {
                    execute(callBack.send(update));

                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private UserStates checkUserState(Users user) {
        return user.getUserState();
    }

    private SendMessage sendAnswer(Users user, Update update) {

        UserStates userState = checkUserState(user);
        String textMessage = update.getMessage().getText();
        if (userState == UserStates.StateStart) {
            return stateStartHandler.handle(update, user);
        }
        else {
            if (textMessage.equals("Меню")) {
                user.setUserState(UserStates.StateMenu);
                return messages.sendStartMessage(user);
            } else {
                return messages.sendErrorMessage(user);
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
