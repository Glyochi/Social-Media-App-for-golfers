package com.example.springboot.Models;

import org.apache.catalina.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

public class ConversationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    private Set<UserModel> participants = new HashSet<UserModel>();

    @ManyToOne
    private Set<MessageModel> messageHistory = new HashSet<MessageModel>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //Adding new participant. If already in the conversation then return false. Else add that person and return true.
    public boolean addParticipant(UserModel newParticipant) {
        if(this.participants.contains(newParticipant))
            return false;
        this.participants.add(newParticipant);
        return true;
    }

    //Removing a participant. If that person is not in the conversation return false, else remove that person and return true.
    public boolean removeParticipant(UserModel removingParticipant) {
        if(this.participants.contains(removingParticipant)) {
            this.participants.remove(removingParticipant);
            return true;
        }
        return false;
    }

    //REQUIRE ATTENTION: Need more functions
}
