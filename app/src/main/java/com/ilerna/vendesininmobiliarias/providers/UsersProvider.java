package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ilerna.vendesininmobiliarias.models.User;

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

    public Task<Void> create(User user) {
        return userCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(Map data, String userId){
        return userCollection.document(userId).update(data);
    }

}
