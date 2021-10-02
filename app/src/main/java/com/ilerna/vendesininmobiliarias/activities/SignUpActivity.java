package com.ilerna.vendesininmobiliarias.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.models.User;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ImageView arrowBack;
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText confirmPasswordEditText;
    Button registerButton;

    FirebaseAuthProvider fap;
    UsersProvider up;

    LoadingDialog loadingDialog;


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

        fap = new FirebaseAuthProvider();
        up = new UsersProvider();

        loadingDialog = new LoadingDialog(SignUpActivity.this);

        arrowBack.setOnClickListener(view -> finish());
        registerButton.setOnClickListener(view -> {
            loadingDialog.start();
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
        loadingDialog.dismiss();
        Toast.makeText(this, "There are fields empties", Toast.LENGTH_LONG).show();
        return false;
        // @formatter:on
    }

    private void createUser(String username, String email, String password) {
        fap.registerUser(email, password).addOnCompleteListener(task -> {
            loadingDialog.dismiss();
            if (!task.isSuccessful()) {
                Toast.makeText(this, "It was not possible to register the user: " + email, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                return;
            }

            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setId(fap.getCurrentUid());
            up.create(user).addOnCompleteListener(task1 -> {
                loadingDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "The user : " + email + " was created successfully!", Toast.LENGTH_SHORT).show();
            });
        });
    }

}