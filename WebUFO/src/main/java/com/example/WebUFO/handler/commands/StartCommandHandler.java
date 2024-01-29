package com.example.WebUFO.handler.commands;

import com.example.WebUFO.controller.callback.Message;
import com.example.WebUFO.model.Users;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Component
public class StartCommandHandler {

    private final Message message;

    public StartCommandHandler(Message message) {
        this.message = message;
    }


    public SendMessage catchStartCommand(Users users, String firstName) {
        String answer = "<b> Привет, " + firstName + ", добро пожаловать в мультивалютную крипто-биржу UFO!</b>";
        return message.send(users, answer);
    }
}
