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
import com.example.duongvu.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicReference;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
               
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Button loginButton = findViewById(R.id.login_button_login);
        Button newUserButton = findViewById(R.id.login_button_newUser);

        TextView usernameBox = findViewById(R.id.login_textBox_username);
        TextView passwordBox = findViewById(R.id.login_textBox_password);
        TextView usernameView = findViewById(R.id.login_textView_username);
        TextView passwordView = findViewById(R.id.login_textView_password);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtomicReference<Boolean> getResponse = new AtomicReference<>(false);
                GetUserApi().getUserByCredentials(usernameBox.getText().toString(), passwordBox.getText().toString()).enqueue(
                        new SlimCallback<UserModel>(returnedUser ->{
                            getResponse.set(true);





                            //Switch activity to the user profile activity
                            switchToUserProfileActivity(returnedUser);
                            return;
                        })
                );


                if(!getResponse.get()){
                    //If didn't get any response
                    usernameView.setTextColor(Color.RED);
                    usernameView.setText("Wrong credentials, please try again");
                }

            }
        });

        newUserButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToNewUserActivity();
            }
        }));

    }



    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------


    private void switchToNewUserActivity(){
        Intent newUserActivity = new Intent(this, NewUserActivity.class);
        startActivity(newUserActivity);
    }

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


