package com.example.WebUFO.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users_data")
@Getter
@Setter
public class UsersData {

    @Id
    @Column(name = "user_data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDataID;

    @Column
    private String userSecondName;
    @Column
    private String userName;
    @Column
    private String userFirstName;
    @Column
    private long userBalance;
}
