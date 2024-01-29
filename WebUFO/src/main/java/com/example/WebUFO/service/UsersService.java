package com.example.WebUFO.service;

import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.model.UsersData;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UsersService {

    void updateUserState(Users users, UserStates userState);
    Users saveUser(Long chatID, Update update);
    Users findUserByChatId(Long id);
    UsersData findUsersDataByChatId(Long id);
}
