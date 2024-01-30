package com.quixdk.UFOBot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users_data")
@Getter
@Setter
public class UsersData {

    @Id
    @Column(name = "users_data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usersDataId;

    @Column
    private Long chatId;
    @Column
    private String userSecondName;
    @Column
    private String userName;
    @Column
    private String userFirstName;
    @Column
    private long userBalance;
}
