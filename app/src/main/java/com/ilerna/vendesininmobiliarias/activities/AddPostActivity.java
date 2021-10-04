package com.ilerna.vendesininmobiliarias.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    ImageView arrowBack;

    ImageView imageView0, imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;
    ImageView imageViewHomes, imageViewOffices, imageViewFactories, imageViewFlats, imageViewStorages, imageViewFields, imageViewGarages, imageViewCommercials;
    List<ImageView> imageViews;
    File fileImage1, fileImage2, fileImage3, fileImage4, fileImage5, fileImage6, fileImage7, fileImage8;
    File fileImages[];
    Button createButton;

    final int GALLERY_RC_0 = 0;
    final int GALLERY_RC_1 = 1;
    final int GALLERY_RC_2 = 2;
    final int GALLERY_RC_3 = 3;
    final int GALLERY_RC_4 = 4;
    final int GALLERY_RC_5 = 5;
    final int GALLERY_RC_6 = 6;
    final int GALLERY_RC_7 = 7;

    TextInputEditText titleEditText;
    TextInputEditText descriptionEditText;
    TextInputEditText priceEditText;

    String category = "";
    String[] urlsImagesUploaded;

    // Providers
    ImagesProvider ip;
    PostsProvider pp;
    FirebaseAuthProvider fap;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // @formatter:off
        // Pictures
        imageView0 = findViewById(R.id.imageView0);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);

        imageView0.setOnClickListener(view -> { openGallery(GALLERY_RC_0); });
        imageView1.setOnClickListener(view -> { openGallery(GALLERY_RC_1); });
        imageView2.setOnClickListener(view -> { openGallery(GALLERY_RC_2); });
        imageView3.setOnClickListener(view -> { openGallery(GALLERY_RC_3); });
        imageView4.setOnClickListener(view -> { openGallery(GALLERY_RC_4); });
        imageView5.setOnClickListener(view -> { openGallery(GALLERY_RC_5); });
        imageView6.setOnClickListener(view -> { openGallery(GALLERY_RC_6); });
        imageView7.setOnClickListener(view -> { openGallery(GALLERY_RC_7); });

        imageViews = Arrays.asList(imageView0,imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7);

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

        fileImages = new File[8];
        urlsImagesUploaded = new String[8];
        loadingDialog = new LoadingDialog(AddPostActivity.this);

        // Back to home activity
        arrowBack = findViewById(R.id.arrowBack);
        arrowBack.setOnClickListener(view -> finish());
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
        loadingDialog.start();

        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String price = priceEditText.getText().toString();

        if (category.isEmpty()) {
            Toast.makeText(this, "Please, select a category.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!title.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
            Post post = new Post();
            post.setTitle(title);
            post.setDescription(description);
            post.setPrice(price);
            post.setCategory(category);

            post.setImage0(urlsImagesUploaded[0]);
            post.setImage1(urlsImagesUploaded[1]);
            post.setImage2(urlsImagesUploaded[2]);
            post.setImage3(urlsImagesUploaded[3]);
            post.setImage4(urlsImagesUploaded[4]);
            post.setImage5(urlsImagesUploaded[5]);
            post.setImage6(urlsImagesUploaded[6]);
            post.setImage7(urlsImagesUploaded[7]);

            post.setUserUid(fap.getCurrentUid());
            pp.createPost(post).addOnCompleteListener(taskCreatePost -> {
                if (taskCreatePost.isSuccessful()) {
                    Toast.makeText(this, "The Post was uploaded successfully.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "There was an error to upload the post.", Toast.LENGTH_LONG).show();
                }
                loadingDialog.dismiss();
            });

        } else {
            Toast.makeText(this, "There are empty fields!", Toast.LENGTH_LONG).show();
        }
    }


    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadingDialog.start();
        setImageFromFile(requestCode, data);
    }

    private void setImageFromFile(int RC, Intent data) {
        try {
            fileImages[RC] = Utils.from(this, data.getData());
            imageViews.get(RC).setImageBitmap(BitmapFactory.decodeFile(fileImages[RC].getAbsolutePath()));
            imageViews.get(RC).clearColorFilter();
            imageViews.get(RC).setBackgroundTintList(null);
            imageViews.get(RC).getDrawable().setTintList(null);
            uploadImageToFirebase(fileImages[RC], RC);
        } catch (Exception ex) {
            loadingDialog.dismiss();
            Log.d("ERROR", "Error loading file from gallery");
            Toast.makeText(this, "Error trying to opening gallery " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageToFirebase(File fileImage, int RC) {
        ip.save(this, fileImage).addOnSuccessListener(task -> {
            ip.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                loadingDialog.dismiss();
                String url = uri.toString();
                urlsImagesUploaded[RC] = url;
                Toast.makeText(this, "The images is uploaded.", Toast.LENGTH_LONG).show();
            });
        });
    }
}