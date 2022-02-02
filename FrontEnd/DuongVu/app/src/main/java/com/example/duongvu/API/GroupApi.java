package com.example.duongvu.API;

import com.example.duongvu.Model.GroupModel;
import com.example.duongvu.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GroupApi {


    @GET("group/getAllGroup")
    Call<List<GroupModel>> getAllGroup();

    @GET("group/getGroup/{groupID}")
    Call<GroupModel> getGroup(@Path("groupID") int groupID);

    @GET("group/getGroupAdmins/{groupID}")
    Call<List<UserModel>> getGroupAdmins(@Path("groupID") int groupID);

    @GET("group/getGroupBannedUser/{groupID}")
    Call<List<UserModel>> getGroupBannedUser(@Path("groupID") int groupID);

    @GET("group/getGroupJoinRequestQueue/{groupID}")
    Call<List<UserModel>> getGroupJoinRequestQueue(@Path("groupID") int groupID);

    @GET("group/getGroupMembers/{groupID}")
    Call<List<UserModel>> getGroupMember(@Path("groupID") int groupID);

    @GET("group/getGroupMods/{groupID}")
    Call<List<UserModel>> getGroupMods(@Path("groupID") int groupID);

    @GET("group/getGroupMemberCount/{groupID}")
    Call<Integer> getGroupMemberCount(@Path("groupID") int groupID);

    @GET("group/checkIfGroupNameExisted/{groupName}")
    Call<Boolean> checkIfGroupNameExisted(@Path("groupName") String groupName);


    @POST("group/createNewGroup/{groupName}/{groupDescription}/{creatorUserID}")
    Call<GroupModel> createNewGroup(@Path("groupName") String groupName, @Path("groupDescription") String groupDescription, @Path("creatorUserID") int creatorUserID);
}
