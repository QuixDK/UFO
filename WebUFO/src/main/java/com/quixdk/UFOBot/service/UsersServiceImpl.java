package com.quixdk.UFOBot.service;

import com.quixdk.UFOBot.model.UserStates;
import com.quixdk.UFOBot.model.Users;
import com.quixdk.UFOBot.model.UsersData;
import com.quixdk.UFOBot.repository.UsersDataEntityRepository;
import com.quixdk.UFOBot.repository.UsersEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersEntityRepository usersEntityRepository;
    private final UsersDataEntityRepository usersDataEntityRepository;

    @Override
    public void updateUserState(Users users, UserStates userState) {
        usersEntityRepository.updateUserState(users, userState);
    }

    @Override
    public Users saveUser(Long chatID, Update update) {

        Users users = new Users();
        UsersData usersData = new UsersData();

        users.setUserState(UserStates.StateStart);
        users.setChatId(chatID);
        usersEntityRepository.save(users);

        String firstName = update.getMessage().getChat().getFirstName();
        String secondName = update.getMessage().getChat().getLastName();
        String userName = update.getMessage().getChat().getUserName();

        usersData.setChatId(chatID);
        usersData.setUserName(userName);
        usersData.setUserFirstName(firstName);
        usersData.setUserSecondName(secondName);
        usersDataEntityRepository.saveData(usersData);

        return users;
    }

    @Override
    public Users findUserByChatId(Long id) {
        return usersEntityRepository.findUserByChatId(id);
    }

    @Override
    public UsersData findUsersDataByChatId(Long id) {
        return usersDataEntityRepository.findUsersDataByChatId(id);
    }

}
