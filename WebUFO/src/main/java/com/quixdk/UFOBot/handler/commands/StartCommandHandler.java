package com.quixdk.UFOBot.handler.commands;

import com.quixdk.UFOBot.handler.buttons.MessageButtons;
import com.quixdk.UFOBot.handler.messages.Messages;
import com.quixdk.UFOBot.model.UserStates;
import com.quixdk.UFOBot.model.Users;
import com.quixdk.UFOBot.service.UsersService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor

public class StartCommandHandler {

    private final UsersService usersService;
    private final Messages messages;

    public SendMessage catchStartCommand(Users users, String firstName) {

        String answer = "<b> Привет, " + firstName + ", добро пожаловать в мультивалютную крипто-биржу UFO!</b>";
        SendMessage message = new SendMessage(users.getChatId().toString() ,answer);
        message.setParseMode("HTML");
        return message;
    }
    public List<SendMessage> handle(Update update, Users user) {
        if (update.getMessage().getText().equals("/start")) {
            String username = usersService.findUsersDataByChatId(user.getChatId()).getUserFirstName();
            usersService.updateUserState(user, UserStates.StateMenu);
            return List.of(catchStartCommand(user, username), messages.sendStartMessage(user));
        } else {
            return List.of(messages.sendErrorMessage(user));
        }
    }
}
