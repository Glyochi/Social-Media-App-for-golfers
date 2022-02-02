package com.example.springboot.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PostModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinTable(
            name = "post_by_user",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private UserModel poster;

    @ManyToOne
    @JoinTable(
            name = "post_in_group",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private GroupModel inGroup;


    private String postContent;

    @OneToMany
    private Set<CommentModel> comments = new HashSet<CommentModel>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getPoster() {
        return poster;
    }

    public void setPoster(UserModel poster) {
        this.poster = poster;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    //ATTENTION REQUIRE: not entirely sure what to do with comments

}
