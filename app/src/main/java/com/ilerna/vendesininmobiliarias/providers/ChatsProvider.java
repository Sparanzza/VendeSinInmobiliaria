package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.models.Comment;

public class ChatsProvider {

    CollectionReference chatsCollection;

    public ChatsProvider() {
        chatsCollection = FirebaseFirestore.getInstance().collection("Chats");
    }

    public void createChat(Chat chat) {
        chatsCollection.document(chat.getUserHome()).collection("Users")
                .document(chat.getUserAway()).set(chat);
        chatsCollection.document(chat.getUserAway()).collection("Users")
                .document(chat.getUserHome()).set(chat);
    }

    public Query getAllUsersChat(String userId){
        return chatsCollection.document(userId).collection("Users");
    }

}
