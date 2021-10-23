package com.ilerna.vendesininmobiliarias.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.models.Message;
import com.ilerna.vendesininmobiliarias.providers.ChatsProvider;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.MessagesProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String userHome;
    String userAway;
    String chatId;

    ChatsProvider cp;
    MessagesProvider mp;
    FirebaseAuthProvider fap;
    UsersProvider up;
    View actionBarView;

    EditText messageEditText;
    ImageView sendMessageImageView;

    TextView textViewUsernameChat;
    TextView timeChatTextView;
    ImageView photoProfileChatImageView;
    ImageView backChatImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        cp = new ChatsProvider();
        mp = new MessagesProvider();
        up = new UsersProvider();
        fap = new FirebaseAuthProvider();

        messageEditText = findViewById(R.id.messageEditText);
        sendMessageImageView = findViewById(R.id.sendMessageImageView);

        userHome = getIntent().getStringExtra("userHome");
        userAway = getIntent().getStringExtra("userAway");
        chatId = getIntent().getStringExtra("chatId");
        showToolbar(R.layout.chat_toolbar);

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
            else chatId = querySnapshot.getDocuments().get(0).getId();

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


        textViewUsernameChat = actionBarView.findViewById(R.id.textViewUsernameChat);
        timeChatTextView = actionBarView.findViewById(R.id.timeChatTextView);
        photoProfileChatImageView = actionBarView.findViewById(R.id.photoProfileChatImageView);
        backChatImageView = actionBarView.findViewById(R.id.backChatImageView);

        backChatImageView.setOnClickListener(view -> finish());

        String userIdToolbar = "";
        if (fap.getCurrentUid().equals(userHome)) userIdToolbar = userAway;
        else userIdToolbar = userHome;

        up.getUser(userIdToolbar).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("username"))
                    textViewUsernameChat.setText(documentSnapshot.getString("username"));
                if (documentSnapshot.contains("photoProfile")) {
                    String photoProfile = documentSnapshot.getString("photoProfile");
                    if (photoProfile != null && !photoProfile.isEmpty())
                        new Utils.ImageDownloadTasK(photoProfileChatImageView).execute(photoProfile);
                }

            }
        });
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setUserHome(userHome);
        chat.setUserAway(userAway);
        chat.setTyping(false);
        chat.setTimestamp(new Date().getTime());
        chat.setId(userHome + userAway);
        chatId = userHome + userAway;
        ArrayList<String> ids = new ArrayList<>();
        ids.add(userHome);
        ids.add(userAway);
        chat.setIds(ids);
        cp.createChat(chat).addOnSuccessListener(unused -> {

        });
    }
}