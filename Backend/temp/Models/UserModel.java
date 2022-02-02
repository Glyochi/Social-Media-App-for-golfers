package com.example.backend.Model;

import org.apache.catalina.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int score;
//    @ManyToMany
//    private List<UserModel> friends;
//    @ManyToMany
//    private List<GroupModel> groups;
//    @ManyToMany
//    private List<ConversationModel> conversations;


    public UserModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

//    public List<UserModel> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(List<UserModel> friends) {
//        this.friends = friends;
//    }
//
//    public void addFriend(UserModel friend){
//        this.friends.add(friend);
//    }
//
//    public void removeFriend(UserModel friend){
//        this.friends.remove(friend);
//    }
//
//    public List<GroupModel> getGroups() {
//        return groups;
//    }
//
//    public void setGroups(List<GroupModel> groups) {
//        this.groups = groups;
//    }
//
//    public void addGroup(GroupModel group){
//        this.groups.add(group);
//    }
//
//    public void removeGroup(GroupModel group){
//        this.groups.remove(group);
//    }
}
