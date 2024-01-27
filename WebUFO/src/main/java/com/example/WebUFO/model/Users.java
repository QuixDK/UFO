package com.example.WebUFO.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column
    private int userState;


    @Override
    public String toString() {
        return "User{" + userID + "}";
//                "chatID=" + chatID +
//                ", userName='" + userFirstName + '\'' +
//                ", userBalance=" + userBalance +
//                '}';
    }

    @Override
    public int hashCode() {
        return getUserID();
    }


}
