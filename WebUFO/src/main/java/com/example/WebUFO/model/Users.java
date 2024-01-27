package com.example.WebUFO.model;

import com.example.WebUFO.controller.UserStates;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userChatID;

    @Column
    @OneToOne
    private UserStates userState;

    @Column
    @OneToOne
    private UsersData usersData;

    @Column
    @OneToMany
    private UsersBills usersBills;
    @Override
    public String toString() {
        return "User{" + userChatID + "}";
//                "chatID=" + chatID +
//                ", userName='" + userFirstName + '\'' +
//                ", userBalance=" + userBalance +
//                '}';
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(getUserChatID());
    }


}
