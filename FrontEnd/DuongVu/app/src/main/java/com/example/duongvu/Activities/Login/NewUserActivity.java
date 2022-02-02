package com.example.duongvu.Activities.Login;

import static com.example.duongvu.API.ApiClientFactory.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import com.example.duongvu.API.SlimCallback;
import com.example.duongvu.Activities.User.UserProfileActivity;
import com.example.duongvu.LocalStorage.GlobalVariables;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityNewuserBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;


public class NewUserActivity extends AppCompatActivity {

    private ActivityNewuserBinding binding;
    private final int displayNameIndex = 0;
    private final int usernameIndex = 1;
    private final int passwordIndex = 2;
    private final int retypePasswordIndex = 3;
    private Boolean[] newAccountInfoSatisified = {false, false, false, false};
    private final int darkGreen = Color.rgb(0, 100, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewuserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView createNewUserView = findViewById(R.id.newUser_textView_createNewUser);
        Button createNewUserButton = findViewById(R.id.newUser_button_createNewUser);

        TextView displayNameView = findViewById(R.id.newUser_textView_displayName);
        TextView displayNameBox = findViewById(R.id.newUser_textBox_displayName);
        TextView usernameView = findViewById(R.id.newUser_textView_username);
        TextView usernameBox = findViewById(R.id.newUser_textBox_username);
        TextView passwordView = findViewById(R.id.newUser_textView_password);
        TextView passwordBox = findViewById(R.id.newUser_textBox_password);
        TextView retypePasswordView = findViewById(R.id.newUser_textView_retypePassword);
        TextView retypePasswordBox = findViewById(R.id.newUser_textBox_retypePassword);

        //--------------------------------------------------------------------------------------------
        //Check if valid username right after the name was changed
        //--------------------------------------------------------------------------------------------
        displayNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() > 0){
                    Boolean[] displayNameStatus = checkDisplayNameRequireMents(editable.toString());
                    if(displayNameStatus[0]){
                        if(displayNameStatus[1]) {
                            //If satisfy all conditions
                            displayNameView.setTextColor(darkGreen);
                            displayNameView.setText("Display name (VALID)");

                            //Setting this to true if requirement met
                            newAccountInfoSatisified[displayNameIndex] = true;
                            return;
                        }else{
                            //If display name contains spaces
                            displayNameView.setTextColor(Color.RED);
                            displayNameView.setText("Display name cannot contain spaces!");
                        }
                    }else{
                        displayNameView.setTextColor(Color.RED);
                        displayNameView.setText("Display name has to have at least 8 characters!");
                    }
                }else{
                    displayNameView.setTextColor(Color.GRAY);
                    displayNameView.setText("Display name");
                }

                //Setting this to false if requirement not met
                newAccountInfoSatisified[displayNameIndex] = false;
            }
        });


        //--------------------------------------------------------------------------------------------
        //Check if valid username right after the name was changed
        //--------------------------------------------------------------------------------------------
        usernameBox.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //avoid triggering event when text is empty
                if (editable.length() > 0) {
                    Boolean[] userNameStatus = checkUserNameRequireMents(editable.toString());

                    Log.i("rEee", userNameStatus[0] + "");
                    if(userNameStatus[0]){
                        if(userNameStatus[1]){
                            //If satisfy all conditions
                            GetUserApi().checkIfUserNameExisted(usernameBox.getText().toString()).enqueue(
                                    new SlimCallback<Boolean>(userNameAlreadyExsited ->{
                                        if(userNameAlreadyExsited){
                                            usernameView.setTextColor(Color.RED);
                                            usernameView.setText("Username has already been taken!");
                                        }else{
                                            usernameView.setTextColor(darkGreen);
                                            usernameView.setText("Username (VALID)");

                                            newAccountInfoSatisified[usernameIndex] = true;
                                            return;
                                        }
                                    })
                            );
                        }else{
                            //If username contains spaces
                            usernameView.setTextColor(Color.RED);
                            usernameView.setText("Username cannot contain spaces!");
                        }
                    }
                    else{
                        usernameView.setTextColor(Color.RED);
                        usernameView.setText("Username has to have at least 8 characters!");
                    }
                } else {
                    usernameView.setTextColor(Color.GRAY);
                    usernameView.setText("Username");
                }

                newAccountInfoSatisified[usernameIndex] = false;
            }

        });



        //--------------------------------------------------------------------------------------------
        //Check if the retype password is the same as password
        //--------------------------------------------------------------------------------------------

        retypePasswordBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                checkRetypePassword(passwordBox.getText().toString(), editable.toString(), R.id.newUser_textView_retypePassword);

            }
        });

        //--------------------------------------------------------------------------------------------
        //Check if the password satisfies the requirements
        //--------------------------------------------------------------------------------------------
        passwordBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0){
                    //If there's nothing in the password box
                    passwordView.setTextColor(Color.GRAY);
                    passwordView.setText("Password");
                    return;
                }

                Boolean[] passwordStatus = checkPasswordRequirements(editable.toString());
                String message = "Password must contain ";
                if(!passwordStatus[0]){
                    message = "Password must has at least 8 characters!";
                }else{
                    if(!passwordStatus[1]){
                        message += "at least one normal character, ";
                    }
                    if(!passwordStatus[2]){
                        message += "at least one numeric character, ";
                    }
                    if(!passwordStatus[3]){
                        message += "at least one special character, ";
                    }
                }
                if(passwordStatus[0] && passwordStatus[1] && passwordStatus[2] && passwordStatus[3]){
                    //edit the message passwordView
                    passwordView.setTextColor(darkGreen);
                    passwordView.setText("Password (VALID)");


                    newAccountInfoSatisified[passwordIndex] = true;

                    //If password satisfies all condition, check the retype password
                    checkRetypePassword(retypePasswordBox.getText().toString(), editable.toString(), R.id.newUser_textView_retypePassword);

                    //Return so that we dont set the newAccountInfoSatisified[passwordIndex] to false at the end
                    return;
                }else{
                    //if doesnt satisify all the condition, show the why not
                    passwordView.setTextColor(Color.RED);
                    passwordView.setText(message.substring(0, message.length() - 2) + "!");
                }

                newAccountInfoSatisified[passwordIndex] = false;
            }
        });


        createNewUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(newAccountInfoSatisified[displayNameIndex] &&
                        newAccountInfoSatisified[usernameIndex] &&
                        newAccountInfoSatisified[passwordIndex] &&
                        newAccountInfoSatisified[retypePasswordIndex]
                ){
                    //If all conditions are met, send request to create new user
                    createNewUserView.setTextColor(Color.GRAY);
                    createNewUserView.setText("");
                    GetUserApi().createUser(displayNameBox.getText().toString(),
                            0,
                            usernameBox.getText().toString(),
                            passwordBox.getText().toString()
                    ).enqueue(new SlimCallback<UserModel>(returnedUser ->{

                        //Switch to userProfileActivity
                        switchToUserProfileActivity(returnedUser);
                    })
                );

                }
                else{
                    //If conditions weren't met, display error
                    String message = "Invalid user's information, please change the ";
                    if(!newAccountInfoSatisified[displayNameIndex]){
                        message += "display name, ";
                    }
                    if(!newAccountInfoSatisified[usernameIndex]){
                        message += "username, ";
                    }
                    if(!newAccountInfoSatisified[passwordIndex]){
                        message += "password, ";
                    }
                    if(!newAccountInfoSatisified[retypePasswordIndex]){
                        message += "retype password, ";
                    }

                    createNewUserView.setTextColor(Color.RED);
                    createNewUserView.setText(message.substring(0, message.length() - 2) + "!");
                }
            }
        });



    }

    static final int minCharacter = 8;
    private Boolean[] checkDisplayNameRequireMents(String displayName){
        if(displayName.length() < minCharacter){
            Boolean[] condition = {false, false};
            return condition;
        }
        boolean doesntContainSpace = false;
        if(!displayName.contains(" ")){
            doesntContainSpace = true;
        }
        Boolean[] condition = {true, doesntContainSpace};
        return condition;
    }

    private Boolean[] checkUserNameRequireMents(String userName){
        if(userName.length() < minCharacter){
            Boolean[] condition = {false, false};
            return condition;
        }
        boolean doesntContainSpace = false;
        if(!userName.contains(" ")){
            doesntContainSpace = true;
        }
        Boolean[] condition = {true, doesntContainSpace};
        return condition;
    }

    private Boolean[] checkPasswordRequirements(String password){
        //Must have a special character
        if(password.length() < minCharacter) {
            Boolean[] condition = {false, false, false, false};
            return condition;
        }

        boolean containNormalChar = false;
        boolean containSpecialChar = false;
        boolean containNumChar = false;
        for(int i = 0; i < password.length(); i++){
            Character c = password.charAt(i);
            if((c > ' ' && c < '0')
                    || (c > '9' && c < 'A')
                    || (c > 'Z' && c < 'a')
                    || (c > 'z')){
                containSpecialChar = true;
            }
            else if(c >= '0' && c <= '9'){
                containNumChar = true;
            }else{
                containNormalChar = true;
            }
            if(containSpecialChar && containNumChar && containNormalChar){
                break;
            }
        }
        Boolean[] condition = {true, containNormalChar, containNumChar, containSpecialChar};
        return condition;
    }

    private void checkRetypePassword(String password, String retypePassword, int textViewID){
        TextView retypePasswordView = findViewById(textViewID);
        if(retypePassword.length() > 0){
            if(password.equals(retypePassword)){
                //If retyped password matches password
                retypePasswordView.setTextColor(darkGreen);
                retypePasswordView.setText("Retype password (Matched)");

                newAccountInfoSatisified[retypePasswordIndex] = true;
                return;
            }else{
                retypePasswordView.setTextColor(Color.RED);
                retypePasswordView.setText("Retype password doesn't match the password above");
            }
        }else{
            //If there's nothing in the box set the retypePasswordView back to normal
            retypePasswordView.setTextColor(Color.GRAY);
            retypePasswordView.setText("Retype password");
        }


        newAccountInfoSatisified[retypePasswordIndex] = false;
    }


    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------


    private void switchToUserProfileActivity(UserModel currentUser){
        //Saving the returned user as json object in sharedPreference
        SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
        SharedPreferences.Editor editor = storedUser.edit();

        Gson gson = new Gson();
        String userJson = gson.toJson(currentUser);
        editor.putString(GlobalVariables.userDataKey, userJson);
        editor.commit();



        Intent userProfileActivity = new Intent(this, UserProfileActivity.class);
        startActivity(userProfileActivity);
    }
}


