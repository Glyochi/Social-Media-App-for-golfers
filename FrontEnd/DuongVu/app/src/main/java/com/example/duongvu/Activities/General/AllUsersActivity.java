package com.example.duongvu.Activities.General;

import static com.example.duongvu.API.ApiClientFactory.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import com.example.duongvu.API.SlimCallback;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityAllusersBinding;

import androidx.appcompat.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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


public class AllUsersActivity extends AppCompatActivity {

    private ActivityAllusersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityAllusersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<UserModel> allUsers = new ArrayList<>();
        HashMap<Integer, LinearLayout> allUserDisplayLayouts = new HashMap<>();
        TextView searchBar = findViewById(R.id.allUsers_textView_searchBar);



        GetUserApi().getAllUser().enqueue(new SlimCallback<List<UserModel>>(listOfUsers -> {

            LinearLayout allUserLinearLayout = findViewById(R.id.allUsers_linearLayout);
            for (int i = 0; i < listOfUsers.size(); i++) {
                UserModel user = listOfUsers.get(i);
                LinearLayout userDisplayLayout = createUserDisplayLayout(user);
                allUserLinearLayout.addView(userDisplayLayout);
                allUsers.add(user);
                allUserDisplayLayouts.put(user.getId(), userDisplayLayout);
            }

        }));


        //When search bar is typed, update the list of all users accordingly
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
                    for(int i = 0; i < allUsers.size(); i++){
                        allUserDisplayLayouts.get(allUsers.get(i).getId()).setVisibility(View.VISIBLE);
                    }
                }else{
                    String searchMessage = s.toString().toLowerCase(Locale.ROOT);
                    LinearLayout tempLayout;
                    UserModel tempUser;
                    String tempString;

                    for(int i = 0; i < allUsers.size(); i++){
                        tempUser = allUsers.get(i);
                        tempLayout = allUserDisplayLayouts.get(tempUser.getId());

                        //Check if the displayName contains the search message
                        tempString = tempUser.getDisplayName().toLowerCase(Locale.ROOT);
                        if(tempString.contains(searchMessage)){
                            tempLayout.setVisibility(View.VISIBLE);
                            continue;
                        }

                        //Check if the userName contains the search message
                        tempString = tempUser.getUserName();
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


    private LinearLayout createUserDisplayLayout(UserModel user) {

        int userDisplayHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        int buttonHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        int buttonWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());

        //Constructing the left half of the userDisplayLayout_____________________________________________________________________________________
        LinearLayout leftSideLayout = new LinearLayout(this);
        leftSideLayout.setOrientation(LinearLayout.VERTICAL);

        //Setting leftSideLayout layout_weight
        LinearLayout.LayoutParams leftParam = new LinearLayout.LayoutParams(0,
                userDisplayHeight,
                1.0f);
        leftSideLayout.setLayoutParams(leftParam);


        TextView userDisplayName = new TextView(this);
        userDisplayName.setText(user.getDisplayName().toString());
        //This is for setting the weights of the textView
        userDisplayName.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));

        TextView userUsername = new TextView(this);
        userUsername.setText("@" + user.getUserName().toString());
        //This is for setting the weights of the textView
        userUsername.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));

        TextView userScore = new TextView(this);
        userScore.setText("Score: " + user.getScore());
        //This is for setting the weights of the textView
        userScore.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));


        leftSideLayout.addView(userDisplayName);
        leftSideLayout.addView(userUsername);
        leftSideLayout.addView(userScore);

        //DEBUG
        leftSideLayout.setBackgroundColor(Color.GRAY);


        //Constructing the right half of the userDisplayLayout_____________________________________________________________________________________________________
        LinearLayout rightSideLayout = new LinearLayout(this);
        rightSideLayout.setOrientation(LinearLayout.VERTICAL);

        //Setting rightSideLayout layout_weight
        LinearLayout.LayoutParams rightParam = new LinearLayout.LayoutParams(0,
                userDisplayHeight,
                1.0f);
        rightSideLayout.setLayoutParams(rightParam);


        Button addFriendButton = new Button(this);
        addFriendButton.setText("Add Friend");
        addFriendButton.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight, 1));

        Button goToProfileButton = new Button(this);
        goToProfileButton.setText("View Profile");
        goToProfileButton.setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight, 1));

        rightSideLayout.addView(addFriendButton);
        rightSideLayout.addView(goToProfileButton);

        rightSideLayout.setGravity(Gravity.RIGHT);


        //Constructing the final userDisplayLayout_________________________________________________________________________________________________
        LinearLayout userDisplayLayout = new LinearLayout(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 10, 5, 10);
        userDisplayLayout.setLayoutParams(params);
        userDisplayLayout.setOrientation(LinearLayout.HORIZONTAL);

        userDisplayLayout.addView(leftSideLayout);
        userDisplayLayout.addView(rightSideLayout);


        //DEBUG
        if (debugC % 2 == 0) {
            userDisplayLayout.setBackgroundColor(Color.GREEN);
        } else {
            userDisplayLayout.setBackgroundColor(Color.YELLOW);
        }
        debugC++;


        return userDisplayLayout;
    }


    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------


}


