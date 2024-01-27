package com.example.WebUFO.handler;

import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.model.Users;

public class StateStartHandler {

    public void handle(String textMessage, Users user) {
        if (textMessage.equals("/start")) {
            catchStartCommand(chatID, firstName);
            user.setUserState(UserStates.StateMenu);
            executeDbUpdate(SendMenuState, chatID);
            sendStartMessage(chatID);
        } else {
            sendErrorMessage(chatID);
        }
    }
}
