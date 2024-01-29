package com.example.WebUFO.repository;

import com.example.WebUFO.config.HibernateUtil;
import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.model.UsersData;
import org.apache.catalina.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Component;

import javax.management.Query;
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
        String query = "UPDATE Users SET userState = " + userStates + " where chatId = " + user.getChatId();
        session.createQuery(query);
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


