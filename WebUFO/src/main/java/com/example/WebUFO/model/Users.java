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
    private Long userId;

    @Column
    private Long chatId;

    @Column
    private UserStates userState;

    @Column
    private Long usersDataId;

    @Column
    private Long usersBillsId;
    @Override
    public String toString() {
        return "User{" + chatId + "}";
//                "chatID=" + chatID +
//                ", userName='" + userFirstName + '\'' +
//                ", userBalance=" + userBalance +
//                '}';
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(getChatId());
    }


}
