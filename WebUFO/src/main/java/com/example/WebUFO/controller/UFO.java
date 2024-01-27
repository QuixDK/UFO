package com.example.WebUFO.controller;


import com.example.WebUFO.config.BotConfig;
import com.example.WebUFO.controller.callback.CallBack;
import com.example.WebUFO.handler.messages.Messages;
import com.example.WebUFO.handler.states.StateStartHandler;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.repository.UsersEntityRepository;

import com.example.WebUFO.service.UsersServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

@Slf4j
@Component
public class UFO extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UsersEntityRepository usersEntityRepository;
    private Messages messages = new Messages();
    private final UsersServiceImpl usersService;


    public UFO(BotConfig config, UsersEntityRepository usersEntityRepository, UsersServiceImpl usersService) {
        this.config = config;
        this.usersEntityRepository = usersEntityRepository;
        this.usersService = usersService;

    }

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
        if (updateHasMessage) {
            Users user = checkUser(update);
            sendAnswer(user, update);
        } else if (updateHasCallBackQuery) {
            CallBack callBack = new CallBack();
            Users user = usersEntityRepository.findUserById(update.getCallbackQuery().getMessage().getChatId());
            if (user != null) {
                callBack.send(update);
            }
        }
    }

    private UserStates checkUserState(Users user) {
        return user.getUserState();
    }

    private void sendAnswer(Users user, Update update) {

        UserStates userState = checkUserState(user);
        String textMessage = update.getMessage().getText();
        if (userState == UserStates.StateStart) {
            StateStartHandler stateStartHandler = new StateStartHandler();
            stateStartHandler.handle(update, user);
        } else if (userState == UserStates.StatePayment) {
            if (isNumber(textMessage)) {
                //createBill(textMessage,chatID);
            } else {
                if (textMessage.equals("Меню")) {
                    usersService.updateUserState(user, UserStates.StateMenu);
                    messages.sendStartMessage(user);
                }
                // else
                //sendMessage(chatID, "<b>Введите число!</b>");
            }
        } else {
            if (textMessage.equals("Меню")) {
                user.setUserState(UserStates.StateMenu);

                messages.sendStartMessage(user);
            } else {
                messages.sendErrorMessage(user);
            }
        }

    }

    private boolean isNumber(String textMessage) {
        try {
            Integer.parseInt(textMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Users checkUser(Update update) {
        Long chatID = update.getMessage().getChatId();
        //If exist return Users
        if (usersEntityRepository.findUserById(chatID) != null) {
            return usersEntityRepository.findUserById(chatID);
        }
        //Else create new Users
        Users user = usersService.saveUser(chatID, update);
        log.info("New user with chat ID: " + chatID + " saved!");
        return user;
    }

}
