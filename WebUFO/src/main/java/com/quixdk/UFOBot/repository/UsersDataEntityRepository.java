package com.quixdk.UFOBot.repository;

import com.quixdk.UFOBot.model.UsersData;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDataEntityRepository {
    void saveData(UsersData usersData);
    UsersData findUsersDataByChatId(Long id);
}
