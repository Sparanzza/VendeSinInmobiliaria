package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.models.Message;

import java.util.HashMap;
import java.util.Map;

public class MessagesProvider {

    CollectionReference messagesCollection;

    public MessagesProvider() {
        messagesCollection = FirebaseFirestore.getInstance().collection("Messages");
    }

    public Task<Void> createMessage(Message message) {
        DocumentReference doc = messagesCollection.document();
        message.setId(doc.getId());
        return doc.set(message);
    }

    public Query getMsgsFromChat(String chatId) {
        return messagesCollection.whereEqualTo("chatId", chatId).orderBy("timestamp", Query.Direction.ASCENDING);
    }


    public Query getMsgsFromChat(String chatId, String senderId) {
        return messagesCollection.whereEqualTo("chatId", chatId).whereEqualTo("senderId", senderId).whereEqualTo("checked", false);
    }

    public Query getLastMsg(String chatId) {
        return messagesCollection.whereEqualTo("chatId", chatId).orderBy("timestamp", Query.Direction.DESCENDING).limit(1);
    }

    public Task<Void> updateChecked(String documentId, boolean isChecked) {
        Map<String, Object> map = new HashMap<>();
        map.put("checked", isChecked);
        return messagesCollection.document(documentId).update(map);
    }



}
