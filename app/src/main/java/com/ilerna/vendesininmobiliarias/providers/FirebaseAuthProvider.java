package com.ilerna.vendesininmobiliarias.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.annotation.Nullable;

public class FirebaseAuthProvider {

    FirebaseAuth firebaseAuth;

    public FirebaseAuthProvider() {
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public Task<AuthResult> loginWithMailAndPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return firebaseAuth.signInWithCredential(credential);
    }

    public Task<AuthResult> registerUser(String email, String password){
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    public @Nullable
    String getCurrentUid() {
        if (firebaseAuth.getCurrentUser() != null) return firebaseAuth.getCurrentUser().getUid();
        else return null;
    }

    public @Nullable
    String getCurrentEmail() {
        if (firebaseAuth.getCurrentUser() != null) return firebaseAuth.getCurrentUser().getEmail();
        else return null;
    }

    public @Nullable
    FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

}
