package com.example.WebUFO.handler.states;

import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.handler.commands.StartCommandHandler;
import com.example.WebUFO.handler.messages.Messages;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.service.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StateStartHandler {

    @Autowired
    UsersServiceImpl usersService;
    Messages messages = new Messages();
    StartCommandHandler startCommandHandler = new StartCommandHandler();

    public void handle(Update update, Users user) {
        if (update.getMessage().getText().equals("/start")) {
            startCommandHandler.catchStartCommand(user, user.getUsersData().getUserFirstName());
            usersService.updateUserState(user, UserStates.StateMenu);
            messages.sendStartMessage(user);
        } else {
            messages.sendErrorMessage(user);
        }
    }
}
