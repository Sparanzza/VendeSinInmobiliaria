package com.ilerna.vendesininmobiliarias.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.adapters.PostItemAdapter;
import com.ilerna.vendesininmobiliarias.models.SlideItemPost;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;

import java.util.ArrayList;
import java.util.List;

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
    private TextView energyDetailsPostTextView;
    private TextView emissionsDetailsPostTextView;
    private FloatingActionButton commentDetailsPostFab;

    PostsProvider pp;

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
        energyDetailsPostTextView = findViewById(R.id.energyDetailsPostTextView);
        emissionsDetailsPostTextView = findViewById(R.id.emissionsDetailsPostTextView);
        commentDetailsPostFab = findViewById(R.id.commentDetailsPostFab);

        String postId = getIntent().getStringExtra("postId");
        pp = new PostsProvider();
        getPostById(postId);

    }

    private void getPostById(String id) {
        pp.getPostById(id).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Make a copy of the slides you'll be presenting.
                listItems = new ArrayList<>();
                String imageUrl;
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