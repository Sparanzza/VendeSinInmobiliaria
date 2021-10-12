package com.ilerna.vendesininmobiliarias.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

public class UserProfileActivity extends AppCompatActivity {

    ImageView photoProfileImageView;
    TextView usernameProfileTextView;
    TextView phoneProfileTextView;
    TextView emailProfileTextView;
    TextView totalPostsProfileTextView;
    ImageView arrowBackUserProfileDetail;

    ImagesProvider ip;
    UsersProvider up;
    FirebaseAuthProvider fap;
    PostsProvider pp;

    String userProfileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        usernameProfileTextView = findViewById(R.id.usernameProfileTextView);
        emailProfileTextView = findViewById(R.id.emailProfileTextView);
        phoneProfileTextView = findViewById(R.id.phoneProfileTextView);
        totalPostsProfileTextView = findViewById(R.id.totalPostsProfileTextView);
        arrowBackUserProfileDetail = findViewById(R.id.arrowBackUserProfileDetail);

        arrowBackUserProfileDetail.setOnClickListener(ev -> finish());

        userProfileId = getIntent().getStringExtra("userId");

        ip = new ImagesProvider();
        up = new UsersProvider();
        fap = new FirebaseAuthProvider();
        pp = new PostsProvider();

        getUserProfile();
        getPosts();
    }

    private void getUserProfile() {
        up.getUser(userProfileId).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("username")) {
                    String username = documentSnapshot.getString("username");
                    if (username != null && !username.isEmpty())
                        usernameProfileTextView.setText(username);
                }
                if (documentSnapshot.contains("email")) {
                    String email = documentSnapshot.getString("email");
                    if (email != null && !email.isEmpty())
                        emailProfileTextView.setText(email);
                }
                if (documentSnapshot.contains("phoneNumber")) {
                    String phoneNumber = documentSnapshot.getString("phoneNumber");
                    if (phoneNumber != null && !phoneNumber.isEmpty())
                        phoneProfileTextView.setText(phoneNumber);
                }
                if (documentSnapshot.contains("photoProfile")) {
                    String photoProfile = documentSnapshot.getString("photoProfile");
                    if (photoProfile != null && !photoProfile.isEmpty())
                        new Utils.ImageDownloadTasK((ImageView) findViewById(R.id.photoProfileImageView)).execute(photoProfile);
                }
            } else {
                Toast.makeText(this, "The user dosen't exist.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPosts() {
        pp.getAllPostByUser(userProfileId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            int totalPosts = queryDocumentSnapshots.size();
            totalPostsProfileTextView.setText(String.valueOf(totalPosts));
        });
    }
}