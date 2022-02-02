package com.example.duongvu.Activities.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.example.duongvu.Activities.General.AllGroupsActivity;
import com.example.duongvu.Activities.General.AllUsersActivity;
import com.example.duongvu.LocalStorage.GlobalVariables;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityUserprofileBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserprofileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityUserprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
        UserModel currentUser = gson.fromJson(userJson, UserModel.class);


        TextView displayNameView = findViewById(R.id.group_textView_groupName);
        TextView userScoreView = findViewById(R.id.group_textView_groupDescription);
        ImageView wallpaperView = findViewById(R.id.group_imageView_wallpaper);
        ImageView profilePicView = findViewById(R.id.userProfile_imageView_profilePicture);
        ImageView profilePicBorderView = findViewById(R.id.userProfile_imageView_profilePictureBorder);
//
//        //Set the display view and score view to the appropriate values
        String temp = currentUser.getDisplayName();
        displayNameView.setText("Display name: " + currentUser.getDisplayName());
        userScoreView.setText("Score: " + currentUser.getScore() + " points");
        wallpaperView.setImageResource(R.drawable.default_wallpaper);
        profilePicView.setImageResource(R.drawable.default_pfp);
        profilePicBorderView.setImageResource(R.drawable.border);




        //Switch to test sockets activity when button clicked
        Button testSocketButton = findViewById(R.id.userProfile_button_testSocket);
        testSocketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToTestSocketActivity();
            }
        });

        //Switch to all users activity when button clicked
        Button allUsersButton = findViewById(R.id.userProfile_button_allUsers);
        allUsersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switchToAllUsersActivity();
            }
        });


        //Switch to all users activity when button clicked
        Button allGroupsButton = findViewById(R.id.userProfile_button_allGroups);
        allGroupsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switchToAllGroupsActivity();
            }
        });


        //Switch to all joinedGroupsActivity when button clicked
        Button groupsButton = findViewById(R.id.userProfile_button_groups);
        groupsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switchToJoinedGroupsActivity();
            }
        });




    }





    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------

    private void switchToTestSocketActivity(){
        Intent newChatActivity = new Intent(this, ChatActivity.class);
        startActivity(newChatActivity);
    }

    private void switchToAllGroupsActivity(){
        Intent allGroupsActivity = new Intent(this, AllGroupsActivity.class);
        startActivity(allGroupsActivity);
    }

    private void switchToJoinedGroupsActivity(){
        Intent joinedGroupsActivity = new Intent(this, JoinedGroupsActivity.class);
        startActivity(joinedGroupsActivity);
    }

    private void switchToFriendsActivity(){
        //TODO
    }

    private void switchToSettingsActivity(){
        //TODO
    }

    private void switchToAllUsersActivity(){
        Intent allUsersActivity = new Intent(this, AllUsersActivity.class);
        startActivity(allUsersActivity);
    }

}


