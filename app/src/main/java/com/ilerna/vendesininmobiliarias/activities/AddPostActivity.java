package com.ilerna.vendesininmobiliarias.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.storage.UploadTask;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;

import java.io.File;

public class AddPostActivity extends AppCompatActivity {

    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;
    File fileImage;
    Button createButton;
    ImagesProvider ip;
    final int GALLERY_RC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // @formatter:off
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
        // @formatter:on

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> { saveImage(); });

        ip = new ImagesProvider();
    }

    private void saveImage() {
        ip.save(this, fileImage).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "The images is uploaded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "The images was not uploaded", Toast.LENGTH_LONG).show();
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