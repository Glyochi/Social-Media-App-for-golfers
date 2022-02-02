package com.example.duongvu.Activities.Group;

import static com.example.duongvu.API.ApiClientFactory.GetUserApi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.example.duongvu.API.SlimCallback;
import com.example.duongvu.Activities.General.AllGroupsActivity;
import com.example.duongvu.Activities.General.AllUsersActivity;
import com.example.duongvu.LocalStorage.GlobalVariables;
import com.example.duongvu.Model.GroupModel;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityGroupBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class GroupActivity extends AppCompatActivity {

    private ActivityGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
        UserModel currentUser = gson.fromJson(userJson, UserModel.class);

        SharedPreferences storedGroup = getSharedPreferences(GlobalVariables.groupSharedPreferenceDataKey, MODE_PRIVATE);
        gson = new Gson();
        String groupJson = storedGroup.getString(GlobalVariables.groupDataKey, "");
        GroupModel currentGroup = gson.fromJson(groupJson, GroupModel.class);


        TextView groupNameView = findViewById(R.id.group_textView_groupName);
        TextView groupDescriptionView = findViewById(R.id.group_textView_groupDescription);
        ImageView wallpaperView = findViewById(R.id.group_imageView_wallpaper);
//
//        //Set the display view and score view to the appropriate values

        groupNameView.setText("Group's name: " + currentGroup.getName());
        groupDescriptionView.setText("Description: \n" + currentGroup.getDescription());
        wallpaperView.setImageResource(R.drawable.default_wallpaper);



        Button leaveOrJoinGroupButton = findViewById(R.id.group_button_leaveOrJoin);
        GetUserApi().getJoinedGroups(currentUser.getId()).enqueue(new SlimCallback<List<GroupModel>>(
            listOfGroups -> {
                if(listOfGroups.contains(currentGroup)){
                    leaveOrJoinGroupButton.setText("Leave Group");
                }else{
                    leaveOrJoinGroupButton.setText("Join Group");
                }
            }
        ));

//        leaveGroupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
//                Gson gson = new Gson();
//                String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
//                UserModel currentUser = gson.fromJson(userJson, UserModel.class);
//
//
//                GetUserApi().leaveGroup(currentUser.getId(), group.getId()).enqueue(new SlimCallback<Boolean>(
//                        b -> {
//                            Log.i("Leave group in JoinedGroupsActivity", b.toString());
//                        }
//                ));
//
//
//                //Switch to joinedGroupsActivity (basically refresh the ui)
//                switchToJoinedGroupsActivity();
//
//            }
//        });





    }





    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------


}


