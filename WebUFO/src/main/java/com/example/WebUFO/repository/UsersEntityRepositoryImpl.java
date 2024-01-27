package com.example.WebUFO.repository;

import com.example.WebUFO.config.HibernateUtil;
import com.example.WebUFO.controller.UserStates;
import com.example.WebUFO.model.Users;
import com.example.WebUFO.model.UsersData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Component;

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
        session.evict(user);
        user.setUserState(userStates);
        Users user2 = (Users) session.merge(user);
        session.close();
    }

    @Override
    public Users findUserById(Long chatID) {
        Session session = sessionFactory.openSession();
        Users users = session.get(Users.class, chatID);
        session.close();
        return users;
    }

    @Override
    public void saveData(UsersData usersData) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(usersData);
        transaction.commit();
        session.close();
    }
}


