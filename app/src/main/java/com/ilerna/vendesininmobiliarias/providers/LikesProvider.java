package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.models.Like;
import com.ilerna.vendesininmobiliarias.models.Post;

public class LikesProvider {

    CollectionReference likesCollection;

    public LikesProvider() {
        likesCollection = FirebaseFirestore.getInstance().collection("Likes");
    }

    public Task<Void> createLike(Like like) {
        DocumentReference doc = likesCollection.document();
        like.setId(doc.getId());
        return doc.set(like);
    }

    public Query getLikeByPostAndUser(String postId, String userId) {
        return likesCollection.whereEqualTo("postId", postId).whereEqualTo("userId", userId);
    }

    public Task<Void> removeLike(String id){
        return likesCollection.document(id).delete();
    }

    public Query getLikeByPost(String postId) {
        return likesCollection.whereEqualTo("postId", postId);
    }

}
