package com.example.duongvu.Activities.General;

import static com.example.duongvu.API.ApiClientFactory.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import com.example.duongvu.API.SlimCallback;
import com.example.duongvu.Activities.Group.GroupActivity;
import com.example.duongvu.LocalStorage.GlobalVariables;
import com.example.duongvu.Model.GroupModel;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityAllgroupsBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


public class AllGroupsActivity extends AppCompatActivity {

    private ActivityAllgroupsBinding binding;
//    static List<GroupModel> listOfJoinedGroups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityAllgroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<GroupModel> allGroups = new ArrayList<>();
        HashMap<Integer, LinearLayout> allGroupDisplayLayouts = new HashMap<>();
        TextView searchBar = findViewById(R.id.allGroups_textView_searchBar);

        //Getting the currentUser
        SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
        UserModel currentUser = gson.fromJson(userJson, UserModel.class);


        //Getting the list of joined Groups
//        List<GroupModel> listOfJoinedGroups;
        //Getting the list of joined Groups from the server and store them in sharedPreference
//        GetUserApi().getJoinedGroups(currentUser.getId()).enqueue(new SlimCallback<List<GroupModel>>(
//                returnedListOfJoinedGroups -> {
//                    listOfJoinedGroups = returnedListOfJoinedGroups;
//                    //Saving the returned list of joined groups as json object in sharedPreference
//                    SharedPreferences storedListOfJoinedGroups = getSharedPreferences(GlobalVariables.listOfJoinedGroupsSharedPreferenceDataKey, MODE_PRIVATE);
//                    SharedPreferences.Editor editor = storedListOfJoinedGroups.edit();
//
//                    Gson gsonTemp = new Gson();
//                    String listOfJoinedGroupsJson = gsonTemp.toJson(currentUser);
//                    editor.putString(GlobalVariables.listOfJoinedGroupsDataKey, listOfJoinedGroupsJson);
//                    editor.commit();
//                }
//        ));
//        //Retrieving the list of joined groups from sharedPreference
//        SharedPreferences storedListOfJoinedGroups = getSharedPreferences(GlobalVariables.listOfJoinedGroupsSharedPreferenceDataKey, MODE_PRIVATE);
//        String listOfJoinedGroupsJson = storedListOfJoinedGroups.getString(GlobalVariables.listOfJoinedGroupsDataKey, "");
//
//        //Create a new type that represents list of groupModels
//        Type listOfGroupModelsType = new TypeToken<List<GroupModel>>(){}.getType();
//        listOfJoinedGroups = gson.fromJson(listOfJoinedGroupsJson, listOfGroupModelsType);



        GetGroupApi().getAllGroup().enqueue(new SlimCallback<List<GroupModel>>(listOfGroups -> {

            LinearLayout allGroupLinearLayout = findViewById(R.id.allGroups_linearLayout);



            for (int i = 0; i < listOfGroups.size(); i++) {
                GroupModel group = listOfGroups.get(i);


                LinearLayout groupDisplayLayout = createGroupDisplayLayout(group, true);


                allGroupLinearLayout.addView(groupDisplayLayout);
                allGroups.add(group);
                allGroupDisplayLayouts.put(group.getId(), groupDisplayLayout);
            }

        }));


        //When search bar is typed, update the list of all groups accordingly
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() == 0){
                    for(int i = 0; i < allGroups.size(); i++){
                        allGroupDisplayLayouts.get(allGroups.get(i).getId()).setVisibility(View.VISIBLE);
                    }
                }else{
                    String searchMessage = s.toString().toLowerCase(Locale.ROOT);
                    LinearLayout tempLayout;
                    GroupModel tempGroup;
                    String tempString;

                    for(int i = 0; i < allGroups.size(); i++){
                        tempGroup = allGroups.get(i);
                        tempLayout = allGroupDisplayLayouts.get(tempGroup.getId());

                        //Check if the displayName contains the search message
                        tempString = tempGroup.getName().toLowerCase(Locale.ROOT);
                        if(tempString.contains(searchMessage)){
                            tempLayout.setVisibility(View.VISIBLE);
                            continue;
                        }


                        //If the search message matches neither, then dont show it
                        tempLayout.setVisibility(View.GONE);
                    }
                }
            }
        });



    }


    static int debugC = 0;


    private LinearLayout createGroupDisplayLayout(GroupModel group, boolean joined) {

        int groupDisplayHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        int buttonHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        int buttonWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());

        //Constructing the left half of the groupDisplayLayout_____________________________________________________________________________________
        LinearLayout leftSideLayout = new LinearLayout(this);
        leftSideLayout.setOrientation(LinearLayout.VERTICAL);

        //Setting leftSideLayout layout_weight
        LinearLayout.LayoutParams leftParam = new LinearLayout.LayoutParams(0,
                groupDisplayHeight,
                1.0f);
        leftSideLayout.setLayoutParams(leftParam);


        TextView groupDisplayNameView = new TextView(this);
        groupDisplayNameView.setText(group.getName().toString());
        //This is for setting the weights of the textView
        groupDisplayNameView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
        groupDisplayNameView.setGravity(Gravity.LEFT);
        groupDisplayNameView.setGravity(Gravity.CENTER_VERTICAL);


        TextView groupMemberCountView = new TextView(this);
        GetGroupApi().getGroupMemberCount(group.getId()).enqueue(new SlimCallback<>(memberCount -> {
            groupMemberCountView.setText(memberCount + " members");
        }));

        //This is for setting the weights of the textView
        groupMemberCountView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
        groupMemberCountView.setGravity(Gravity.LEFT);
        groupMemberCountView.setGravity(Gravity.CENTER_VERTICAL);


        leftSideLayout.addView(groupDisplayNameView);
        leftSideLayout.addView(groupMemberCountView);

        //DEBUG
        leftSideLayout.setBackgroundColor(Color.GRAY);


        //Constructing the right half of the groupDisplayLayout_____________________________________________________________________________________________________
        LinearLayout rightSideLayout = new LinearLayout(this);
        rightSideLayout.setOrientation(LinearLayout.VERTICAL);

        //Setting rightSideLayout layout_weight
        LinearLayout.LayoutParams rightParam = new LinearLayout.LayoutParams(0,
                groupDisplayHeight,
                1.0f);
        rightSideLayout.setLayoutParams(rightParam);


        Button viewGroupButton = new Button(this);
        viewGroupButton.setText("View Group");
        viewGroupButton.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight, 1));
        viewGroupButton.setGravity(Gravity.CENTER);

        //HARD PART ____________________________________________________________________________________________________________________________________________________________________
        viewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Saving the given group as json object in sharedPreference before switching to groupActivity
                SharedPreferences storedGroup = getSharedPreferences(GlobalVariables.groupSharedPreferenceDataKey, MODE_PRIVATE);
                SharedPreferences.Editor editor = storedGroup.edit();

                Gson gson = new Gson();
                String groupJson = gson.toJson(group);
                editor.putString(GlobalVariables.groupDataKey, groupJson);
                editor.commit();


                //Switch to group activity
                switchToGroupActivity();
            }
        });



        //HARD PART ____________________________________________________________________________________________________________________________________________________________________


        Button leaveOrJoinButton = new Button(this);

        leaveOrJoinButton.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight, 1));
        leaveOrJoinButton.setGravity(Gravity.CENTER);



        //HARD PART ____________________________________________________________________________________________________________________________________________________________________



        if(joined) {
            leaveOrJoinButton.setText("Leave Group");
            leaveOrJoinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
                    Gson gson = new Gson();
                    String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
                    UserModel currentUser = gson.fromJson(userJson, UserModel.class);


                    GetUserApi().leaveGroup(currentUser.getId(), group.getId()).enqueue(new SlimCallback<Boolean>(
                            b -> {
                                Log.i("Leave group in JoinedGroupsActivity", b.toString());
                            }
                    ));


                    //Switch to joinedGroupsActivity (basically refresh the ui)
                    refreshActivity();

                }
            });
        }else{
            leaveOrJoinButton.setText("Join Group");
            leaveOrJoinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
                    Gson gson = new Gson();
                    String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
                    UserModel currentUser = gson.fromJson(userJson, UserModel.class);


                    GetUserApi().joinGroup(currentUser.getId(), group.getId()).enqueue(new SlimCallback<Boolean>(
                            b -> {
                                Log.i("Leave group in JoinedGroupsActivity", b.toString());
                            }
                    ));


                    //Switch to AllGroupsActivity (basically refresh the ui)
                    refreshActivity();

                }
            });
        }


        //HARD PART ____________________________________________________________________________________________________________________________________________________________________





        rightSideLayout.addView(viewGroupButton);
        rightSideLayout.addView(leaveOrJoinButton);


        rightSideLayout.setGravity(Gravity.RIGHT);


        //Constructing the final groupDisplayLayout_________________________________________________________________________________________________
        LinearLayout groupDisplayLayout = new LinearLayout(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 10, 5, 10);
        groupDisplayLayout.setLayoutParams(params);
        groupDisplayLayout.setOrientation(LinearLayout.HORIZONTAL);

        groupDisplayLayout.addView(leftSideLayout);
        groupDisplayLayout.addView(rightSideLayout);


        //DEBUG
        if (debugC % 2 == 0) {
            groupDisplayLayout.setBackgroundColor(Color.GREEN);
        } else {
            groupDisplayLayout.setBackgroundColor(Color.YELLOW);
        }
        debugC++;


        return groupDisplayLayout;
    }



    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------
    private void switchToAllGroupsActivity(){
        Intent allGroupsActivity = new Intent(this, AllGroupsActivity.class);
        startActivity(allGroupsActivity);
    }

    private void switchToGroupActivity(){
        Intent groupActivity = new Intent(this, GroupActivity.class);
        startActivity(groupActivity);
    }

    private void refreshActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}


