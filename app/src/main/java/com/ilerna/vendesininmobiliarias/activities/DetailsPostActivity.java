package com.ilerna.vendesininmobiliarias.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.print.PageRange;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.CategoriesEnum;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.adapters.CommentsAdapter;
import com.ilerna.vendesininmobiliarias.adapters.PostItemAdapter;
import com.ilerna.vendesininmobiliarias.adapters.PostsAdapter;
import com.ilerna.vendesininmobiliarias.models.Comment;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.models.SlideItemPost;
import com.ilerna.vendesininmobiliarias.providers.CommentsProvider;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailsPostActivity extends AppCompatActivity {

    private List<SlideItemPost> listItems;
    private ViewPager page;
    private TabLayout tabLayout;

    private ImageView photoProfileDetailsPostImageView;
    private TextView usernameDetailsPostTextView;
    private TextView phoneNumberDetialsPostTextView;
    private TextView goToProfileDetailsPostButton;
    private TextView titleDetailsPostTextView;
    private ImageView categoryDetailsPostImageView;
    private TextView categoryDetailsPostTextView;
    private TextView priceDetailsPostTextView;
    private TextView descriptionDetailsPostTextView;
    private TextView bedroomsDetailsPostTextView;
    private TextView bathroomDetailsPostTextView;
    private TextView floorDetailsPostTextView;
    private TextView antiquityDetailsPostTextView;
    private TextView sqmDetailsPostTextView;
    private TextView parkingDetailsPostTextView;
    private TextView statusDetailsPostTextView;
    private TextView elevatorDetailsPostTextView;
    private TextView orientationDetailsPostTextView;
    private TextView furnishedDetailsPostTextView;
    private TextView heatingDetailsPostTextView;
    private TextView acDetailsPostTextView;
    private TextView consumptionDetailsPostTextView;
    private TextView emissionsDetailsPostTextView;
    private FloatingActionButton commentDetailsPostFab;
    private ImageView arrowBackPostDetail;
    private RecyclerView commentsDetailsPostRecyclerView;

    PostsProvider pp;
    UsersProvider up;
    CommentsProvider cp;
    FirebaseAuthProvider fap;

    CommentsAdapter commentsAdapter;

    String userUid;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_post);
        page = findViewById(R.id.my_pager);
        tabLayout = findViewById(R.id.my_tablayout);

        photoProfileDetailsPostImageView = findViewById(R.id.photoProfileDetailsPostImageView);
        usernameDetailsPostTextView = findViewById(R.id.usernameDetailsPostTextView);
        phoneNumberDetialsPostTextView = findViewById(R.id.phoneNumberDetialsPostTextView);
        goToProfileDetailsPostButton = findViewById(R.id.goToProfileDetailsPostButton);
        titleDetailsPostTextView = findViewById(R.id.titleDetailsPostTextView);
        categoryDetailsPostImageView = findViewById(R.id.categoryDetailsPostImageView);
        categoryDetailsPostTextView = findViewById(R.id.categoryDetailsPostTextView);
        priceDetailsPostTextView = findViewById(R.id.priceDetailsPostTextView);
        descriptionDetailsPostTextView = findViewById(R.id.descriptionDetailsPostTextView);
        bedroomsDetailsPostTextView = findViewById(R.id.bedroomsDetailsPostTextView);
        bathroomDetailsPostTextView = findViewById(R.id.bathroomDetailsPostTextView);
        floorDetailsPostTextView = findViewById(R.id.floorDetailsPostTextView);
        antiquityDetailsPostTextView = findViewById(R.id.antiquityDetailsPostTextView);
        sqmDetailsPostTextView = findViewById(R.id.sqmDetailsPostTextView);
        parkingDetailsPostTextView = findViewById(R.id.parkingDetailsPostTextView);
        statusDetailsPostTextView = findViewById(R.id.statusDetailsPostTextView);
        elevatorDetailsPostTextView = findViewById(R.id.elevatorDetailsPostTextView);
        orientationDetailsPostTextView = findViewById(R.id.orientationDetailsPostTextView);
        furnishedDetailsPostTextView = findViewById(R.id.furnishedDetailsPostTextView);
        heatingDetailsPostTextView = findViewById(R.id.heatingDetailsPostTextView);
        acDetailsPostTextView = findViewById(R.id.acDetailsPostTextView);
        consumptionDetailsPostTextView = findViewById(R.id.consumptionDetailsPostTextView);
        emissionsDetailsPostTextView = findViewById(R.id.emissionsDetailsPostTextView);
        commentDetailsPostFab = findViewById(R.id.commentDetailsPostFab);
        arrowBackPostDetail = findViewById(R.id.arrowBackPostDetail);
        commentsDetailsPostRecyclerView = findViewById(R.id.commentsDetailsPostRecyclerView);

        arrowBackPostDetail.setOnClickListener(view -> finish());

        goToProfileDetailsPostButton.setOnClickListener(view -> goToProfile());

        commentDetailsPostFab.setOnClickListener(view -> {
            showDialogInsertComment();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailsPostActivity.this);
        commentsDetailsPostRecyclerView.setLayoutManager(linearLayoutManager);

        postId = getIntent().getStringExtra("postId");
        pp = new PostsProvider();
        up = new UsersProvider();
        cp = new CommentsProvider();
        fap = new FirebaseAuthProvider();
        getPostById(postId);

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = cp.getCommentByPost(postId);
        FirestoreRecyclerOptions<Comment> options =
                new FirestoreRecyclerOptions.Builder<Comment>()
                        .setQuery(query, Comment.class).build();
        commentsAdapter = new CommentsAdapter(options, DetailsPostActivity.this);
        commentsDetailsPostRecyclerView.setAdapter(commentsAdapter);
        commentsAdapter.startListening(); // Start listening from FireStore database
    }

    @Override
    public void onStop() {
        super.onStop();
        commentsAdapter.stopListening();
    }


    private void showDialogInsertComment() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailsPostActivity.this);
        dialog.setTitle("Comment");
        dialog.setMessage("Insert your comment");

        EditText commentEditText = new EditText(DetailsPostActivity.this);
        commentEditText.setHint("Your text ...");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(40, 40, 40, 40);
        commentEditText.setLayoutParams(params);

        RelativeLayout container = new RelativeLayout(DetailsPostActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        container.setLayoutParams(relativeParams);
        container.addView(commentEditText);
        dialog.setView(container);

        dialog.setPositiveButton("Send", (dialog1, which) -> {
            String commentText = commentEditText.getText().toString();
            if (commentText.isEmpty()) {
                Toast.makeText(this, "Please, Write the comment for the user", Toast.LENGTH_SHORT).show();
                return;
            }

            Comment comment = new Comment();
            comment.setComment(commentText);
            comment.setPostId(postId);
            comment.setUserId(fap.getCurrentUid());
            comment.setTimestamp(new Date().getTime());
            cp.createComment(comment).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Created comment successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error creating comment", Toast.LENGTH_SHORT).show();
                }
            });

        });

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
        });

        dialog.show();
    }

    private void goToProfile() {
        if (userUid.equals("")) return;
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("userId", userUid);
        startActivity(intent);
    }

    private void getPostById(String id) {
        pp.getPostById(id).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Make a copy of the slides you'll be presenting.
                listItems = new ArrayList<>();
                String imageUrl, dataText;

                if (documentSnapshot.contains("image0")) {
                    imageUrl = documentSnapshot.getString("image0");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("image1")) {
                    imageUrl = documentSnapshot.getString("image1");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("image2")) {
                    imageUrl = documentSnapshot.getString("image2");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("image3")) {
                    imageUrl = documentSnapshot.getString("image3");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("image4")) {
                    imageUrl = documentSnapshot.getString("image4");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("image5")) {
                    imageUrl = documentSnapshot.getString("image5");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("image6")) {
                    imageUrl = documentSnapshot.getString("image6");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("image7")) {
                    imageUrl = documentSnapshot.getString("image7");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        listItems.add(new SlideItemPost(imageUrl, ""));
                }

                if (documentSnapshot.contains("title")) {
                    dataText = documentSnapshot.getString("title");
                    if (dataText != null && !dataText.isEmpty())
                        titleDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("title")) {
                    dataText = documentSnapshot.getString("title");
                    if (dataText != null && !dataText.isEmpty())
                        titleDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("category")) {
                    dataText = documentSnapshot.getString("category");
                    if (dataText != null && !dataText.isEmpty()) {
                        categoryDetailsPostTextView.setText(dataText);
                        categoryDetailsPostImageView.setImageResource(CategoriesEnum.valueOf(dataText).getResource());
                    }
                }

                if (documentSnapshot.contains("description")) {
                    dataText = documentSnapshot.getString("description");
                    if (dataText != null && !dataText.isEmpty())
                        descriptionDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("ac")) {
                    dataText = documentSnapshot.getString("ac");
                    if (dataText != null && !dataText.isEmpty())
                        acDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("antiquity")) {
                    dataText = documentSnapshot.getString("antiquity");
                    if (dataText != null && !dataText.isEmpty())
                        antiquityDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("bedroom")) {
                    dataText = documentSnapshot.getString("bedroom");
                    if (dataText != null && !dataText.isEmpty())
                        bedroomsDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("bathroom")) {
                    dataText = documentSnapshot.getString("bathroom");
                    if (dataText != null && !dataText.isEmpty())
                        bathroomDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("consumption")) {
                    dataText = documentSnapshot.getString("consumption");
                    if (dataText != null && !dataText.isEmpty())
                        consumptionDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("emissions")) {
                    dataText = documentSnapshot.getString("emissions");
                    if (dataText != null && !dataText.isEmpty())
                        emissionsDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("elevator")) {
                    dataText = documentSnapshot.getString("elevator");
                    if (dataText != null && !dataText.isEmpty())
                        elevatorDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("elevator")) {
                    dataText = documentSnapshot.getString("elevator");
                    if (dataText != null && !dataText.isEmpty())
                        elevatorDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("floor")) {
                    dataText = documentSnapshot.getString("floor");
                    if (dataText != null && !dataText.isEmpty())
                        floorDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("furnished")) {
                    dataText = documentSnapshot.getString("furnished");
                    if (dataText != null && !dataText.isEmpty())
                        furnishedDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("heating")) {
                    dataText = documentSnapshot.getString("heating");
                    if (dataText != null && !dataText.isEmpty())
                        heatingDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("orientation")) {
                    dataText = documentSnapshot.getString("orientation");
                    if (dataText != null && !dataText.isEmpty())
                        orientationDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("parking")) {
                    dataText = documentSnapshot.getString("parking");
                    if (dataText != null && !dataText.isEmpty())
                        parkingDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("price")) {
                    dataText = documentSnapshot.getString("price");
                    if (dataText != null && !dataText.isEmpty())
                        priceDetailsPostTextView.setText((NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(dataText))) + " $");
                }

                if (documentSnapshot.contains("sqm")) {
                    dataText = documentSnapshot.getString("sqm");
                    if (dataText != null && !dataText.isEmpty())
                        sqmDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("status")) {
                    dataText = documentSnapshot.getString("status");
                    if (dataText != null && !dataText.isEmpty())
                        statusDetailsPostTextView.setText(dataText);
                }

                if (documentSnapshot.contains("userUid")) {
                    userUid = documentSnapshot.getString("userUid");
                    if (userUid != null && !userUid.isEmpty())
                        up.getUser(userUid).addOnSuccessListener(userSnapshot -> {
                            if (userSnapshot.exists()) {
                                if (userSnapshot.contains("username")) {
                                    String username = userSnapshot.getString("username");
                                    if (username != null && !username.isEmpty())
                                        usernameDetailsPostTextView.setText(username);
                                }

                                if (userSnapshot.contains("phoneNumber")) {
                                    String phoneNumber = userSnapshot.getString("phoneNumber");
                                    if (phoneNumber != null && !phoneNumber.isEmpty())
                                        phoneNumberDetialsPostTextView.setText(phoneNumber);
                                }


                                if (userSnapshot.contains("photoProfile")) {
                                    String photoProfileUrl = userSnapshot.getString("photoProfile");
                                    if (photoProfileUrl != null && !photoProfileUrl.isEmpty())
                                        new Utils.ImageDownloadTasK((ImageView) findViewById(R.id.photoProfileDetailsPostImageView)).execute(photoProfileUrl);
                                }


                            } else {

                            }
                        });

                }


                PostItemAdapter itemsPager_adapter = new PostItemAdapter(this, listItems);
                page.setAdapter(itemsPager_adapter);
                tabLayout.setupWithViewPager(page, true);
                Toast.makeText(this, "Loaded Post", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error on loading post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}