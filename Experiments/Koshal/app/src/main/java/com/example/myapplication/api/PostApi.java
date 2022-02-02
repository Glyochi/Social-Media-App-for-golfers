package com.example.myapplication.api;

import retrofit2.Call;
import retrofit2.http.GET;
import com.example.myapplication.model.Post;

public interface PostApi{

    @GET("posts/1")
    Call<Post> getFirstPost();
}