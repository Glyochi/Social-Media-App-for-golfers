package com.example.springboot.Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String commentContent;

    @ManyToOne
    private UserModel commenter;

    @OneToMany
    private Set<CommentModel> reply = new HashSet<CommentModel>();
    private int likes;
    private int dislikes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public UserModel getCommenter() {
        return commenter;
    }

    public void setCommenter(UserModel commenter) {
        this.commenter = commenter;
    }

    //REQUIRE ATTENTION: not sure what to do with reply

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
