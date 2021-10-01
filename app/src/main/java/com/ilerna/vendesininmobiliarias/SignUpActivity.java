package com.ilerna.vendesininmobiliarias;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ilerna.vendesininmobiliarias.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ImageView arrowBack;
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText confirmPasswordEditText;
    Button registerButton;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        arrowBack = findViewById(R.id.arrowBack);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        arrowBack.setOnClickListener(view -> finish());
        registerButton.setOnClickListener(view -> {
            signUp();
        });
    }

    private void signUp() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        Log.d("username", username);
        Log.d("email", email);
        Log.d("password", password);
        Log.d("password", confirmPassword);

        if (!isFilledFieldsValid(username, email, password, confirmPassword)) return;
        createUser(username, email, password);
    }

    private boolean isFilledFieldsValid(String username, String email, String password, String confirmPassword) {

        // @formatter:off
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
            if (!Utils.isEmailAddressValid(email)) { Toast.makeText(this, "The email is not valid", Toast.LENGTH_LONG).show(); return false; }
            if(!password.equals(confirmPassword)) { Toast.makeText(this, "the password fields are not the same", Toast.LENGTH_SHORT).show(); return false; }
            if (password.length() < 8 ) { Toast.makeText(this, "the password must be more than 7 characters", Toast.LENGTH_SHORT).show(); return false; }
            return true;
        }

        Toast.makeText(this, "There are fields empties", Toast.LENGTH_LONG).show();
        // @formatter:on
        return false;
    }

    private void createUser(String username, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "It was not possible to register the user: " + email, Toast.LENGTH_SHORT).show();
                return;
            }

            String userUID = firebaseAuth.getCurrentUser().getUid();
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("email", email);
            firestore.collection("users").document(userUID).set(userData).addOnCompleteListener(task1 -> Toast.makeText(SignUpActivity.this, "The user : " + email + " was created successfully!", Toast.LENGTH_SHORT).show());

        });
    }

}