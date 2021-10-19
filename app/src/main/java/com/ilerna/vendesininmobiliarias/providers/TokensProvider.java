package com.ilerna.vendesininmobiliarias.providers;

import static android.provider.Settings.System.getString;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.models.Like;
import com.ilerna.vendesininmobiliarias.models.Token;

public class TokensProvider {

    CollectionReference tokensCollection;

    public TokensProvider() {
        tokensCollection = FirebaseFirestore.getInstance().collection("Tokens");
    }

    public void createToken(String userId) {
        if (userId == null) return;
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    Token token = new Token(task.getResult());
                    tokensCollection.document(userId).set(token);
                });
    }

    public Task<DocumentSnapshot> getToken(String userId) {
        return tokensCollection.document(userId).get();
    }

}
