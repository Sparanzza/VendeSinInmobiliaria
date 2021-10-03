package com.ilerna.vendesininmobiliarias.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.io.File;

public class AddPostActivity extends AppCompatActivity {

    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;
    ImageView imageViewHomes, imageViewOffices, imageViewFactories, imageViewFlats, imageViewStorages, imageViewFields, imageViewGarages, imageViewCommercials;

    File fileImage;
    Button createButton;
    final int GALLERY_RC = 1;

    TextInputEditText titleEditText;
    TextInputEditText descriptionEditText;
    TextInputEditText priceEditText;

    String category = "";

    // Providers
    ImagesProvider ip;
    PostsProvider pp;
    FirebaseAuthProvider fap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // @formatter:off
        // Pictures
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);

        imageView1.setOnClickListener(view -> { openGallery(); });
        imageView2.setOnClickListener(view -> { openGallery(); });
        imageView3.setOnClickListener(view -> { openGallery(); });
        imageView4.setOnClickListener(view -> { openGallery(); });
        imageView5.setOnClickListener(view -> { openGallery(); });
        imageView6.setOnClickListener(view -> { openGallery(); });
        imageView7.setOnClickListener(view -> { openGallery(); });
        imageView8.setOnClickListener(view -> { openGallery(); });

        //Categories
        imageViewHomes = findViewById(R.id.imageViewHomes);
        imageViewOffices = findViewById(R.id.imageViewOffices);
        imageViewFactories = findViewById(R.id.imageViewFactories);
        imageViewFlats = findViewById(R.id.imageViewFlats);
        imageViewStorages = findViewById(R.id.imageViewStorages);
        imageViewFields = findViewById(R.id.imageViewFields);
        imageViewGarages = findViewById(R.id.imageViewGarages);
        imageViewCommercials = findViewById(R.id.imageViewCommercials);

        imageViewHomes.setOnClickListener(view -> { setCategoryColor(imageViewHomes); category = "HOMES"; });
        imageViewOffices.setOnClickListener(view -> { setCategoryColor(imageViewOffices); category = "OFFICES"; });
        imageViewFactories.setOnClickListener(view -> { setCategoryColor(imageViewFactories); category = "FACTORIES"; });
        imageViewFlats.setOnClickListener(view -> { setCategoryColor(imageViewFlats); category = "FLATS"; });
        imageViewStorages.setOnClickListener(view -> { setCategoryColor(imageViewStorages); category = "STORAGES"; });
        imageViewFields.setOnClickListener(view -> { setCategoryColor(imageViewFields); category = "FIELDS"; });
        imageViewGarages.setOnClickListener(view -> { setCategoryColor(imageViewGarages); category = "GARAGES"; });
        imageViewCommercials.setOnClickListener(view -> { setCategoryColor(imageViewCommercials); category = "COMMERCIALS"; });

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> { createPost(); });
        // @formatter:on

        ip = new ImagesProvider();
        pp = new PostsProvider();
        fap = new FirebaseAuthProvider();
    }

    private void setCategoryColor(ImageView iview) {
        setDefaultColorAllCategories();
        int color = getResources().getColor(R.color.secondary);
        ((CardView) iview.getParent()).setCardBackgroundColor(color);
    }

    private void setDefaultColorAllCategories() {
        int color = getResources().getColor(R.color.primary);
        ((CardView) imageViewHomes.getParent()).setCardBackgroundColor(color);
        ((CardView) imageViewOffices.getParent()).setCardBackgroundColor(color);
        ((CardView) imageViewFactories.getParent()).setCardBackgroundColor(color);
        ((CardView) imageViewFlats.getParent()).setCardBackgroundColor(color);
        ((CardView) imageViewStorages.getParent()).setCardBackgroundColor(color);
        ((CardView) imageViewFields.getParent()).setCardBackgroundColor(color);
        ((CardView) imageViewGarages.getParent()).setCardBackgroundColor(color);
        ((CardView) imageViewCommercials.getParent()).setCardBackgroundColor(color);
    }

    private void createPost() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String price = priceEditText.getText().toString();

        if (category.isEmpty()) {
            Toast.makeText(this, "Please, select a category.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!title.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
            if (fileImage != null) {
                saveImage(title, description, price);
            } else {
                Toast.makeText(this, "Please, select at least one image.", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "There are empty fields!", Toast.LENGTH_LONG).show();
        }

    }

    private void saveImage(String title, String description, String price) {
        ip.save(this, fileImage).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ip.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    Post post = new Post();
                    post.setTitle(title);
                    post.setDescription(description);
                    post.setPrice(price);
                    post.setCategory(category);
                    post.setImage1(url);
                    post.setUserUid(fap.getCurrentUid());
                    pp.createPost(post).addOnCompleteListener(taskCreatePost -> {
                        if (taskCreatePost.isSuccessful()) {
                            Toast.makeText(this, "The Post was uploaded successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "There was an error to upload the post.", Toast.LENGTH_LONG).show();
                        }
                    });
                });
                Toast.makeText(this, "The images is uploaded.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "The images was not uploaded.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_RC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_RC) {
            try {
                fileImage = Utils.from(this, data.getData());
                imageView1.setImageBitmap(BitmapFactory.decodeFile(fileImage.getAbsolutePath()));
                imageView1.clearColorFilter();
                imageView1.setBackgroundTintList(null);
                imageView1.getDrawable().setTintList(null);
            } catch (Exception ex) {
                Log.d("ERROR", "Error loading file from gallery");
                Toast.makeText(this, "Error trying to opening gallery " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }
}