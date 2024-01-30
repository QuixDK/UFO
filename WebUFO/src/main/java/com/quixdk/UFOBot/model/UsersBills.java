package com.quixdk.UFOBot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users_bills")
public class UsersBills {

    @Id
    @Column(name = "bill_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billID;

    @Column
    private String payUrl;

    @Column
    private String payStatus = "NONE";
}
