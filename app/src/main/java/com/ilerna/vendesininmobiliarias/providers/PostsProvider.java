package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.models.User;

public class PostsProvider {

    CollectionReference userCollection;

    public PostsProvider() {
        userCollection = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<Void> createPost(Post post) {
        return userCollection.document().set(post);
    }

}
