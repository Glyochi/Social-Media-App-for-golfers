package com.example.duongvu.API;

import com.example.duongvu.LocalStorage.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientFactory {

    static Retrofit apiClientSeed = null;

    static Retrofit GetApiClientSeed() {

        if (apiClientSeed == null) {
            apiClientSeed = new Retrofit.Builder()
                    .baseUrl(URL.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return apiClientSeed;
    }


    public static UserApi GetUserApi(){
        return GetApiClientSeed().create(UserApi.class);
    }

    public static GroupApi GetGroupApi(){
        return GetApiClientSeed().create(GroupApi.class);
    }


}