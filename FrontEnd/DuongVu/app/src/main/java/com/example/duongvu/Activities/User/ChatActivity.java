package com.example.duongvu.Activities.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.example.duongvu.LocalStorage.GlobalVariables;
import com.example.duongvu.LocalStorage.URL;
import com.example.duongvu.Model.UserModel;
import com.example.duongvu.R;
import com.example.duongvu.databinding.ActivityChatBinding;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private WebSocketClient wsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        TextView chatHistoryBox = findViewById(R.id.chat_textView_chatHistoryBox);
        TextView chatInputBox = findViewById(R.id.chat_textView_chatInput);
        Button chatSendButton = findViewById(R.id.chat_button_chatSend);

        Context thisContext = this;

        //Establish a ws connection to the server
        connectWebSocket();



        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatInputBox.getText().toString();
                if(message.length() == 0){
                    //If there's nothing to send then do nothing
                    return;
                }else{
                    wsClient.send(message);
                }
                chatInputBox.setText("");
            }
        });





        //Resize chatHistoryBox when softInput is in view
        final View activityRootView = findViewById(R.id.chat_activity);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(thisContext, 200)) { // if more than 200 dp, it's probably a keyboard...
                    ViewGroup.LayoutParams layoutWithSoftInput = chatHistoryBox.getLayoutParams();
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 344, getResources().getDisplayMetrics());
                    layoutWithSoftInput.height = height;
                    chatHistoryBox.setLayoutParams(layoutWithSoftInput);
                }else{
                    ViewGroup.LayoutParams layoutWithSoftInput = chatHistoryBox.getLayoutParams();
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
                    layoutWithSoftInput.height = height;
                    chatHistoryBox.setLayoutParams(layoutWithSoftInput);
                }
            }
        });




    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    //-------------------------------------------------------------------------------------------------------------------------------
    //WebSocket function
    //-------------------------------------------------------------------------------------------------------------------------------

    private void connectWebSocket(){
        URI uri;

        SharedPreferences storedUser = getSharedPreferences(GlobalVariables.userSharedPreferenceDataKey, MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = storedUser.getString(GlobalVariables.userDataKey, "");
        UserModel currentUser = gson.fromJson(userJson, UserModel.class);

        try{
            uri = new URI(URL.webSocketBaseUrl + currentUser.getDisplayName());
//            uri = new URI("ws://echo.websocket.org");
//            System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        }catch(URISyntaxException e){
            e.printStackTrace();
            return;
        }

        wsClient = new WebSocketClient(uri){
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String message) {
                Log.i("Websocket", "Message Received");
                TextView chatHistoryBox = findViewById(R.id.chat_textView_chatHistoryBox);
                chatHistoryBox.setText(chatHistoryBox.getText() + "\n" + message.toString());
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("Websocket", "Error " + ex.getMessage());
            }
        };

        wsClient.connect();
    }



    //-------------------------------------------------------------------------------------------------------------------------------
    //Switch to different activity functions
    //-------------------------------------------------------------------------------------------------------------------------------


}


