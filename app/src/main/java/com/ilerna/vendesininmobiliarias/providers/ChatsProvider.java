package com.ilerna.vendesininmobiliarias.providers;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.models.Chat;

import java.util.ArrayList;

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
