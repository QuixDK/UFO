package com.quixdk.UFOBot.repository;

import com.quixdk.UFOBot.model.UserStates;
import com.quixdk.UFOBot.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersEntityRepository  {
    void updateUserState(Users users, UserStates userStates);
    Users findUserByChatId(Long chatID);
    void save(Users users);
}
