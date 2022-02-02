package com.example.springboot.Models;

import org.apache.catalina.User;

import javax.persistence.*;

@Entity
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Message;
    @ManyToOne
    private UserModel sender;
    @ManyToOne
    private UserModel receiver;
    private String groupName;
    private String numOfUsers;

    public MessageModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public void setReceiver(UserModel newReceiver) {
        this.receiver = newReceiver;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel newSender) {
        this.sender = newSender;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(String numOfUsers) {
        this.numOfUsers = numOfUsers;
    }


}
