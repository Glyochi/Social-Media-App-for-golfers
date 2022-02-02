package com.example.duongvu.Activities.User;

import static com.example.duongvu.API.ApiClientFactory.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import com.example.duongvu.API.SlimCallback;
import com.example.duongvu.Activities.General.AllGroupsActivity;
import com.example.duongvu.Activities.Group.GroupActivity;
import com.example.duongvu.Activities.Group.NewGroupActivity;
import com.example.duongvu.LocalStorage.GlobalVariables;
import com.example.duongvu.Model.GroupModel;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityAllgroupsBinding;
import com.example.duongvu.databinding.ActivityJoinedgroupsBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


public class JoinedGroupsActivity extends AppCompatActivity {

    private ActivityJoinedgroupsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityJoinedgroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<GroupModel> joinedGroups = new ArrayList<>();
        HashMap<Integer, LinearLayout> joinedGroupDisplayLayouts = new HashMap<>();

        TextView searchBar = findViewById(R.id.joinedGroups_textView_searchBar);
        Button createNewGroupButton = findViewById(R.id.joinedGroups_button_createNewGroup);


        SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
        UserModel currentUser = gson.fromJson(userJson, UserModel.class);


        //Get all the user's joined Groups and display them onto the screen
        GetUserApi().getJoinedGroups(currentUser.getId()).enqueue(new SlimCallback<List<GroupModel>>(listOfGroups -> {

            LinearLayout allGroupLinearLayout = findViewById(R.id.joinedGroups_linearLayout);
            for (int i = 0; i < listOfGroups.size(); i++) {
                GroupModel group = listOfGroups.get(i);
                LinearLayout groupDisplayLayout = createGroupDisplayLayout(group);

                allGroupLinearLayout.addView(groupDisplayLayout);
                joinedGroups.add(group);
                joinedGroupDisplayLayouts.put(group.getId(), groupDisplayLayout);
            }

        }));


        //When click create new Group button, change to createGroupActivity




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
                    for(int i = 0; i < joinedGroups.size(); i++){
                        joinedGroupDisplayLayouts.get(joinedGroups.get(i).getId()).setVisibility(View.VISIBLE);
                    }
                }else{
                    String searchMessage = s.toString().toLowerCase(Locale.ROOT);
                    LinearLayout tempLayout;
                    GroupModel tempGroup;
                    String tempString;

                    for(int i = 0; i < joinedGroups.size(); i++){
                        tempGroup = joinedGroups.get(i);
                        tempLayout = joinedGroupDisplayLayouts.get(tempGroup.getId());

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




        createNewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToNewGroupActivity();
            }
        });
    }


    static int debugC = 0;


    private LinearLayout createGroupDisplayLayout(GroupModel group) {

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


        Button leaveGroupButton = new Button(this);
        leaveGroupButton.setText("Leave Group");
        leaveGroupButton.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight, 1));
        leaveGroupButton.setGravity(Gravity.CENTER);


        //HARD PART ____________________________________________________________________________________________________________________________________________________________________
        leaveGroupButton.setOnClickListener(new View.OnClickListener() {
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
                switchToJoinedGroupsActivity();

            }
        });



        //HARD PART ____________________________________________________________________________________________________________________________________________________________________





        rightSideLayout.addView(viewGroupButton);
        rightSideLayout.addView(leaveGroupButton);


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


    private void switchToNewGroupActivity(){
        Intent newGroupActivity = new Intent(this, NewGroupActivity.class);
        startActivity(newGroupActivity);
    }

    private void switchToGroupActivity(){
        Intent groupActivity = new Intent(this, GroupActivity.class);
        startActivity(groupActivity);
    }

    private void switchToJoinedGroupsActivity(){
        Intent joinedGroupsActivity = new Intent(this, JoinedGroupsActivity.class);
        startActivity(joinedGroupsActivity);
    }


}


