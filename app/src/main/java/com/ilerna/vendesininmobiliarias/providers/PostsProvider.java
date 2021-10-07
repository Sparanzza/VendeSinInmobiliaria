package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.models.Post;

public class PostsProvider {

    CollectionReference postCollection;

    public PostsProvider() {
        postCollection = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<Void> createPost(Post post) {
        return postCollection.document().set(post);
    }

    public Query getAllPosts(){
        return postCollection.orderBy("title", Query.Direction.DESCENDING);
    }

}
