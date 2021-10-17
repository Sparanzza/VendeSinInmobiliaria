package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public Query getAllPosts() {
        return postCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getAllPostByUser(String userUid) {
        return postCollection.whereEqualTo("userUid", userUid);
    }

    public Query getAllPostsByCategory(String category) {
        return postCollection.whereEqualTo("category", category).orderBy("timestamp", Query.Direction.DESCENDING);
    }


    public Query getAllPostsByTitle(String title) {
        return postCollection.whereGreaterThanOrEqualTo("title", title).whereLessThanOrEqualTo("title", title + '\uf8ff').orderBy("title", Query.Direction.DESCENDING);
    }

    public Task<DocumentSnapshot> getPostById(String id) {
        return postCollection.document(id).get();
    }

    public Task<Void> deletePost(String id) {
        return postCollection.document(id).delete();
    }

}
