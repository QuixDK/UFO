package com.example.WebUFO.handler.states;

import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.handler.commands.StartCommandHandler;
import com.example.WebUFO.handler.messages.Messages;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.service.UsersService;
import com.example.WebUFO.service.UsersServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class StateStartHandler {

    private final UsersService usersService;
    private final Messages messages;
    private final StartCommandHandler startCommandHandler;

    public SendMessage handle(Update update, Users user) {
        if (update.getMessage().getText().equals("/start")) {
            String username = usersService.findUsersDataByChatId(user.getChatId()).getUserFirstName();
            usersService.updateUserState(user, UserStates.StateMenu);
            return startCommandHandler.catchStartCommand(user, username);

            // messages.sendStartMessage(user);
        } else {
            return messages.sendErrorMessage(user);
        }
    }
}
