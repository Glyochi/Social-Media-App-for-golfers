package com.example.duongvu.API;

import com.example.duongvu.Model.GroupModel;
import com.example.duongvu.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {


    @GET("user/getAllUser")
    Call<List<UserModel>> getAllUser();

    @GET("user/getUserByCredentials/{userName}/{password}")
    Call<UserModel> getUserByCredentials(@Path("userName") String userName,
                                         @Path("password") String password);

    @GET("user/getUserByID/{userID}")
    Call<UserModel> getUserByID(@Path("userID") int userID);

    @GET("user/checkIfUserNameExisted/{userName}")
    Call<Boolean> checkIfUserNameExisted(@Path("userName") String userName);

    @GET("user/getJoinedGroups/{userID}")
    Call<List<GroupModel>> getJoinedGroups(@Path("userID") int userID);

    @GET("user/getJoinedGroups/{userID}/{groupID}")
    Call<Boolean> isJoinedGroup(@Path("userID") int userID, @Path("groupID") int groupID);



    @POST("user/createNewUser/{displayName}/{userScore}/{userName}/{password}")
    Call<UserModel> createUser(@Path("displayName") String displayName,
                               @Path("userScore") int userScore,
                               @Path("userName") String userName,
                               @Path("password") String password);

    @POST("user/changeUserPassword/{userID}/{oldPassword}/{newPassword}")
    Call<Boolean> changeUserPassword(@Path("userID") int userID,
                               @Path("oldPassword") String oldPassword,
                               @Path("newPassword") String newPassword);

    @DELETE("user/deleteUser/{callerID}")
    Call<Boolean> deleteUser(@Path("callerID") int callerID);


    @PUT("user/joinGroup/{callerID}/{groupID}")
    Call<Boolean> joinGroup(@Path("callerID") int callerID, @Path("groupID") int groupID);

    @PUT("user/leaveGroup/{callerID}/{groupID}")
    Call<Boolean> leaveGroup(@Path("callerID") int callerID, @Path("groupID") int groupID);


}
