package com.quixdk.UFOBot.service;

import com.quixdk.UFOBot.model.UserStates;
import com.quixdk.UFOBot.model.Users;
import com.quixdk.UFOBot.model.UsersData;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UsersService {

    void updateUserState(Users users, UserStates userState);
    Users saveUser(Long chatID, Update update);
    Users findUserByChatId(Long id);
    UsersData findUsersDataByChatId(Long id);
}
