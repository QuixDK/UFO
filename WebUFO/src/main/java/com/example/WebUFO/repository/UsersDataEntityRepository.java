package com.example.WebUFO.repository;

import com.example.WebUFO.model.Users;
import com.example.WebUFO.model.UsersData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersDataEntityRepository {
    void saveData(UsersData usersData);
    UsersData findUsersDataByChatId(Long id);
}
