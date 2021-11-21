package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ilerna.vendesininmobiliarias.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UsersProvider {

    CollectionReference userCollection;

    public UsersProvider() {
        userCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String userUid) {
        return userCollection.document(userUid).get();
    }

    public DocumentReference getUserDocumentRef(String userUid) {
        return userCollection.document(userUid);
    }

    public Task<Void> create(User user) {
        return userCollection.document(user.getId()).set(user);
    }

    public Task<Void> updateUser(Map data, String userId) {
        return userCollection.document(userId).update(data);
    }

    public Task<Void> updateChatOnline(boolean isOnline, String userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("isOnline", isOnline);
        data.put("lastConnection", new Date().getTime());
        return userCollection.document(userId).update(data);
    }

}
