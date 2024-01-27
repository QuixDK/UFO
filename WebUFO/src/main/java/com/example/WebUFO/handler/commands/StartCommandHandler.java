package com.example.WebUFO.handler.commands;

import com.example.WebUFO.controller.callback.Message;
import com.example.WebUFO.model.Users;

public class StartCommandHandler {

    Message message = new Message();

    public void catchStartCommand(Users users, String firstName) {
        String answer = "<b> Привет, " + firstName + ", добро пожаловать в мультивалютную крипто-биржу UFO!</b>";
        message.send(users, answer);
    }
}
