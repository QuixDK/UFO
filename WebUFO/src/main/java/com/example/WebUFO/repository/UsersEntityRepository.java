package com.example.WebUFO.repository;

import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


public interface UsersEntityRepository {
    void updateUserState(Users users, UserStates userStates);
    Users findUserById(Long chatID);
    void save(Users users);
}
