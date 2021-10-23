package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.models.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatsProvider {

    CollectionReference chatsCollection;

    public ChatsProvider() {
        chatsCollection = FirebaseFirestore.getInstance().collection("Chats");
    }

    public void createChat(Chat chat) {
        chatsCollection.document(chat.getUserHome() + chat.getUserAway()).set(chat);

    }

    public Query getAllUsersChat(String userId) {
        return chatsCollection.whereArrayContains("ids", userId);
    }

    public Query getChatByBothUsers(String userHome, String userAway) {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(userHome + userAway);
        ids.add(userAway + userHome);
        return chatsCollection.whereIn("id", ids);
    }

}
