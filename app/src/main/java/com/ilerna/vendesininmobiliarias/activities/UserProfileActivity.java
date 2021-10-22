package com.ilerna.vendesininmobiliarias.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.adapters.UserPostAdapter;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    TextView existPostUserProfileTextView;
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
    RecyclerView postsProfileRecyclerView;
    UserPostAdapter userPostAdapter;
    FloatingActionButton chatFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        usernameProfileTextView = findViewById(R.id.usernameProfileTextView);
        emailProfileTextView = findViewById(R.id.emailProfileTextView);
        phoneProfileTextView = findViewById(R.id.phoneProfileTextView);
        totalPostsProfileTextView = findViewById(R.id.totalPostsProfileTextView);
        arrowBackUserProfileDetail = findViewById(R.id.arrowBackUserProfileDetail);
        existPostUserProfileTextView = findViewById(R.id.existPostUserProfileTextView);
        postsProfileRecyclerView = findViewById(R.id.postsProfileRecyclerView);
        chatFab = findViewById(R.id.chatFab);

        arrowBackUserProfileDetail.setOnClickListener(ev -> finish());

        userProfileId = getIntent().getStringExtra("userId");

        chatFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("userHome", fap.getCurrentUid());
            intent.putExtra("userAway", userProfileId);
            startActivity(intent);
        });




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        postsProfileRecyclerView.setLayoutManager(linearLayoutManager);

        ip = new ImagesProvider();
        up = new UsersProvider();
        fap = new FirebaseAuthProvider();
        pp = new PostsProvider();

        if (Objects.equals(fap.getCurrentUid(), userProfileId)) chatFab.setVisibility(View.GONE);
        getUserProfile();
        getPosts();
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = pp.getAllPostByUser(userProfileId);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class).build();
        userPostAdapter = new UserPostAdapter(options, UserProfileActivity.this);
        postsProfileRecyclerView.setAdapter(userPostAdapter);
        userPostAdapter.startListening(); // Start listening from FireStore database
    }

    @Override
    public void onStop() {
        super.onStop();
        userPostAdapter.stopListening();
    }

    private void getUserProfile() {
        pp.getAllPostByUser(userProfileId).addSnapshotListener((querySnapshot, exception) -> {
            if (querySnapshot.size() > 0) {
                existPostUserProfileTextView.setText(querySnapshot.size() + " Posts");
            } else {
                existPostUserProfileTextView.setText("No Post");
            }
        });

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