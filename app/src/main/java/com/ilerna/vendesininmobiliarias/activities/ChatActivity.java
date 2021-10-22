package com.ilerna.vendesininmobiliarias.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.providers.ChatsProvider;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String userHome;
    String userAway;

    ChatsProvider cp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userHome = getIntent().getStringExtra("userHome");
        userAway = getIntent().getStringExtra("userAway");

        cp = new ChatsProvider();

        createChat();
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setUserHome(userHome);
        chat.setUserAway(userAway);
        chat.setTyping(false);
        chat.setTimestamp(new Date().getTime());

        cp.createChat(chat);

    }


}