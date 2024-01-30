package com.quixdk.UFOBot.repository;

import com.quixdk.UFOBot.config.HibernateUtil;
import com.quixdk.UFOBot.model.UserStates;
import com.quixdk.UFOBot.model.Users;
import com.quixdk.UFOBot.model.UsersData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersEntityRepositoryImpl implements UsersEntityRepository, UsersDataEntityRepository {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void save(Users users) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(users);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateUserState(Users user, UserStates userStates) {
        Session session = sessionFactory.openSession();
        user.setUserState(userStates);
        session.update(user);
        Transaction transaction = session.beginTransaction();
        transaction.commit();
        session.close();
    }

    @Override
    public Users findUserByChatId(Long chatId) {
        Session session = sessionFactory.openSession();
        String query = "FROM Users where chatId = " + chatId;
        List<Users> users = session.createQuery(query, Users.class).list();
        session.close();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public void saveData(UsersData usersData) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(usersData);
        transaction.commit();
        session.close();
    }

    @Override
    public UsersData findUsersDataByChatId(Long chatId) {
        Session session = sessionFactory.openSession();
        String query = "FROM UsersData where chatId = " + chatId;
        List<UsersData> usersData = session.createQuery(query, UsersData.class).list();
        session.close();
        return usersData.isEmpty() ? null : usersData.get(0);
    }
}


