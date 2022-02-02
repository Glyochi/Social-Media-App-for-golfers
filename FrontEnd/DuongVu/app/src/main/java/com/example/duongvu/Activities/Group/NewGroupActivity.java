package com.example.duongvu.Activities.Group;

import static com.example.duongvu.API.ApiClientFactory.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import com.example.duongvu.API.SlimCallback;
import com.example.duongvu.Activities.User.UserProfileActivity;
import com.example.duongvu.LocalStorage.GlobalVariables;
import com.example.duongvu.Model.GroupModel;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityNewgroupBinding;
import com.example.duongvu.databinding.ActivityNewuserBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;


public class NewGroupActivity extends AppCompatActivity {

    private ActivityNewgroupBinding binding;
    private final int groupNameIndex = 0;
    private final int groupDescriptionIndex = 1;
    private boolean[] newGroupInfoSatisfied = {false, false};
    private final int darkGreen = Color.rgb(0, 100, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewgroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
        UserModel currentUser = gson.fromJson(userJson, UserModel.class);


        TextView createNewGroupView = findViewById(R.id.newGroup_textView_createNewGroup);
        Button createNewGroupButton = findViewById(R.id.newGroup_button_createNewGroup);


        TextView groupNameView = findViewById(R.id.newGroup_textView_groupName);
        TextView groupNameBox = findViewById(R.id.newGroup_textBox_groupName);
        TextView groupDescriptionView = findViewById(R.id.newGroup_textView_groupDescription);
        TextView groupDescriptionBox = findViewById(R.id.newGroup_textBox_groupDescription);


        //--------------------------------------------------------------------------------------------
        //Check if valid groupName right after the name was changed
        //--------------------------------------------------------------------------------------------
        groupNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //avoid triggering event when text is empty
                newGroupInfoSatisfied[groupNameIndex] = false;
                if (editable.length() > 0) {
                    boolean groupNameStatus = checkGroupNameRequirements(editable.toString());
                    if (groupNameStatus)
                        GetGroupApi().checkIfGroupNameExisted(groupNameBox.getText().toString()).enqueue(
                                new SlimCallback<Boolean>(userNameAlreadyExsited -> {
                                    if (userNameAlreadyExsited) {
                                        groupNameView.setTextColor(Color.RED);
                                        groupNameView.setText("Group's name has already been taken!");
                                    } else {
                                        groupNameView.setTextColor(darkGreen);
                                        groupNameView.setText("Group's name (VALID)");

                                        newGroupInfoSatisfied[groupNameIndex] = true;
                                        return;
                                    }
                                })
                        );

                } else {
                    groupNameView.setTextColor(Color.GRAY);
                    groupNameView.setText("Group's name");
                }


            }

        });

        //--------------------------------------------------------------------------------------------
        //Check if valid groupDescription right after the name was changed
        //--------------------------------------------------------------------------------------------
        groupDescriptionBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //avoid triggering event when text is empty
                newGroupInfoSatisfied[groupDescriptionIndex] = false;
                if (editable.length() > 0) {
                    boolean groupNameStatus = checkGroupDescriptionRequirements(editable.toString());
                    if (groupNameStatus) {
                        groupDescriptionView.setTextColor(darkGreen);
                        groupDescriptionView.setText("Group's description (VALID)");
                        newGroupInfoSatisfied[groupDescriptionIndex] = true;
                    } else {
                        groupDescriptionView.setTextColor(Color.RED);
                        groupDescriptionView.setText("Group's description must have at least 8 characters!");
                    }

                } else {
                    groupDescriptionView.setTextColor(Color.GRAY);
                    groupDescriptionView.setText("Group's description");
                }

            }

        });


        //--------------------------------------------------------------------------------------------
        //On click, create a new group if all conditions are met
        //--------------------------------------------------------------------------------------------
        createNewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newGroupInfoSatisfied[groupNameIndex] && newGroupInfoSatisfied[groupDescriptionIndex]
                ) {
                    //If all conditions are met, send request to create new user
                    createNewGroupView.setTextColor(Color.GRAY);
                    createNewGroupView.setText("");

//                    createNewGroupView.setText(groupNameBox.getText().toString() + "\n" + groupDescriptionBox.getText().toString() + "\n" + currentUser.getId());
                    GetGroupApi().createNewGroup(groupNameBox.getText().toString(),
                            groupDescriptionBox.getText().toString(),
                            currentUser.getId()).enqueue(
                                    new SlimCallback<GroupModel>(newGroup -> {

//                                Switch to that group's layout
                                switchToGroupActivity(newGroup);
                            })
                    );

                } else {
                    //If conditions weren't met, display error
                    String message = "Invalid group's information, please change the ";
                    if (!newGroupInfoSatisfied[groupNameIndex]) {
                        message += "group's name, ";
                    }
                    if (!newGroupInfoSatisfied[groupDescriptionIndex]) {
                        message += "group's description, ";
                    }


                    createNewGroupView.setTextColor(Color.RED);
                    createNewGroupView.setText(message.substring(0, message.length() - 2) + "!");
                }
            }
        });


    }

    static final int minCharacter = 8;

    private boolean checkGroupNameRequirements(String groupName) {
        if (groupName.length() < minCharacter) {
            return false;
        }
        return true;
    }

    private boolean checkGroupDescriptionRequirements(String groupName) {
        if (groupName.length() < minCharacter) {
            return false;
        }
        return true;
    }


    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------


    private void switchToGroupActivity(GroupModel group) {
        //Saving the returned group as json object in sharedPreference
        SharedPreferences storedGroup = getSharedPreferences(GlobalVariables.groupSharedPreferenceDataKey, MODE_PRIVATE);
        SharedPreferences.Editor editor = storedGroup.edit();

        Gson gson = new Gson();
        String groupJson = gson.toJson(group);
        editor.putString(GlobalVariables.groupDataKey, groupJson);
        editor.commit();


        Intent groupActivity = new Intent(this, GroupActivity.class);
        startActivity(groupActivity);
    }
}


