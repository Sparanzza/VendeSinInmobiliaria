package com.ilerna.vendesininmobiliarias.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.providers.ChatsProvider;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String userHome;
    String userAway;

    ChatsProvider cp;
    View actionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        showToolbar(R.layout.chat_toolbar);

        userHome = getIntent().getStringExtra("userHome");
        userAway = getIntent().getStringExtra("userAway");

        cp = new ChatsProvider();

        existChat();
    }

    private void existChat(){
        cp.getChatByBothUsers(userHome, userAway).get().addOnSuccessListener(querySnapshot -> {
           if(querySnapshot.size() == 0) createChat();
           else Toast.makeText(this, "Chat Already exists", Toast.LENGTH_SHORT).show();
        });
    }

    private void showToolbar(int chat_toolbar) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setSubtitle("");
        toolbar.setNavigationIcon(null);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        actionBarView = inflater.inflate(chat_toolbar, null);
        actionBar.setCustomView(actionBarView);
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setUserHome(userHome);
        chat.setUserAway(userAway);
        chat.setTyping(false);
        chat.setTimestamp(new Date().getTime());
        chat.setId(userHome + userAway);
        ArrayList<String> ids = new ArrayList<>();
        ids.add(userHome);
        ids.add(userAway);
        chat.setIds(ids);
        cp.createChat(chat);

    }


}