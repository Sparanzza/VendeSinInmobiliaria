package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ilerna.vendesininmobiliarias.models.Message;

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

}
