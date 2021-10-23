package com.ilerna.vendesininmobiliarias.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.models.Message;
import com.ilerna.vendesininmobiliarias.providers.ChatsProvider;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.MessagesProvider;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String userHome;
    String userAway;
    String chatId;

    ChatsProvider cp;
    MessagesProvider mp;
    FirebaseAuthProvider fap;
    View actionBarView;

    EditText messageEditText;
    ImageView sendMessageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        showToolbar(R.layout.chat_toolbar);

        messageEditText = findViewById(R.id.messageEditText);
        sendMessageImageView = findViewById(R.id.sendMessageImageView);

        userHome = getIntent().getStringExtra("userHome");
        userAway = getIntent().getStringExtra("userAway");
        chatId = getIntent().getStringExtra("chatId");

        cp = new ChatsProvider();
        mp = new MessagesProvider();
        fap = new FirebaseAuthProvider();

        sendMessageImageView.setOnClickListener(view -> {
            sendMessage();
        });
        existChat();
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString();
        if (messageText.isEmpty()) return;

        Message message = new Message();
        message.setChatId(chatId);
        message.setChecked(false);
        message.setText(messageText);
        message.setTimestamp(new Date().getTime());

        if (fap.getCurrentUid().equals(userHome)) {
            message.setSenderId(userHome);
            message.setRecieverId(userAway);
        } else {
            message.setSenderId(userAway);
            message.setRecieverId(userHome);
        }

        mp.createMessage(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageEditText.setText("");
                Toast.makeText(this, "sended Ok!", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show();
        });
    }

    private void existChat() {
        cp.getChatByBothUsers(userHome, userAway).get().addOnSuccessListener(querySnapshot -> {
            if (querySnapshot.size() == 0) createChat();
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