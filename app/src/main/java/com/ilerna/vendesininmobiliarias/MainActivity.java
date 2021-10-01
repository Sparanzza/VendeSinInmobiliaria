package com.ilerna.vendesininmobiliarias;

import static java.util.Objects.requireNonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    TextView signUpTextView;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    Button logInButton;
    SignInButton logInGoogleButton;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUpTextView = findViewById(R.id.signUpTextView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInButton = findViewById(R.id.logInButton);
        logInGoogleButton = findViewById(R.id.logInGoogleButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logInButton.setOnClickListener(view -> login());

        logInGoogleButton.setOnClickListener(view -> loginGoogle());

        signUpTextView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Log.d("email", email);
        Log.d("password", password);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "username or password are not valid" + email, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("LOGIN_GOOGLE", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("LOGIN_GOOGLE", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("FIREBASE_AUTH_GOOGLE", "signInWithCredential:success");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateUI(user);
                isUserExist(user != null ? user.getUid() : null);
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(MainActivity.this, "It was not possible to get account from Google.", Toast.LENGTH_LONG).show();
                Log.w("FIREBASE_AUTH_GOOGLE", "signInWithCredential:failure", task.getException());
                updateUI(null);
            }
        });
    }

    private boolean isUserExist(String userUid) {
        AtomicReference<Boolean> success = new AtomicReference<>();
        success.set(false);
        firestore.collection("users").document(userUid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                startActivityToHome();
            } else {
                String email = requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                String username = firebaseAuth.getCurrentUser().getEmail().split("@")[0];
                Map<String, Object> userData = new HashMap<>();
                userData.put("email", email);
                userData.put("username", username);
                firestore.collection("users").document(userUid).set(userData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivityToHome();
                    } else {
                        Toast.makeText(MainActivity.this, "It was not possible save user on database.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return success.get();
    }

    private void startActivityToHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void updateUI(FirebaseUser user) {
        // TODO
    }

    private void loginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // This method wait a action from the user, in this case, select an Google account
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}