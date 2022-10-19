package com.example.WebUFO.model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity(name = "usersDataTable")
public class User {

    @Id
    private long chatID;

    private String userSecondName;
    private String userName;
    private String userFirstName;
    private long userBalance;
    private int userState;
    private String payUrl;
    private String billID;
    private String payStatus = "NONE";

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayUlr() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public User() {
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }


    public int getUserState() {
        return userState;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSecondName() {
        return userSecondName;
    }

    public long getChatID() {
        return chatID;
    }

    public long getUserBalance() {
        return userBalance;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    @Override
    public String toString() {
        return "User{" +
                "chatID=" + chatID +
                ", userName='" + userFirstName + '\'' +
                ", userBalance=" + userBalance +
                '}';
    }

    public void setUserBalance(long balance) {
        this.userBalance = balance;
    }
}
