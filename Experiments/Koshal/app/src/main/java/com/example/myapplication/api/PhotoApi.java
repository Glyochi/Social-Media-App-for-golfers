package com.example.myapplication.api;

import com.example.myapplication.model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PhotoApi {

    @GET("photos/1")
    Call<Photo> getFirstPhoto();

    @GET("photos")
    Call<List<Photo>> getAllPhoto();


    @GET("photos/{photoNum}")
    Call<Photo> getPhotoByNum(@Path("photoNum") String photoNum);

}