package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.models.Comment;

public class CommentsProvider {

    CollectionReference commentsCollection;

    public CommentsProvider() {
        commentsCollection = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> createComment(Comment comment) {
        return commentsCollection.document().set(comment);
    }

    public Query getCommentByPost(String postId){
        return commentsCollection.whereEqualTo("postId", postId);
    }
}
