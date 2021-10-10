package com.ilerna.vendesininmobiliarias.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    ImageView arrowBackEditProfileBtn;
    ImageView photoEditProfileImageView;
    TextInputEditText usernameEditProfileTextView;
    TextInputEditText phoneEditProfileTextView;

    private final int GALLERY_RC = 0;
    private final int CAMERA_RC = 1;

    LoadingDialog loadingDialog;
    AlertDialog.Builder selectorImageSrc;
    CharSequence[] options;

    String photoCameraPath;
    String photoCameraAbsolutePath;
    File photoCameraFile;
    File fileImage;

    Button updateProfileButton;
    String urlImageUploaded;

    ImagesProvider ip;
    UsersProvider up;
    FirebaseAuthProvider fap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        arrowBackEditProfileBtn = findViewById(R.id.arrowBackEditProfileBtn);
        usernameEditProfileTextView = findViewById(R.id.usernameEditProfileTextView);
        phoneEditProfileTextView = findViewById(R.id.phoneEditProfileTextView);
        photoEditProfileImageView = findViewById(R.id.photoEditProfileImageView);
        updateProfileButton = findViewById(R.id.updateProfileButton);

        arrowBackEditProfileBtn.setOnClickListener(view -> finish());
        updateProfileButton.setOnClickListener(view -> updateProfile());
        photoEditProfileImageView.setOnClickListener(view -> selectImageSrc());

        urlImageUploaded = "";

        loadingDialog = new LoadingDialog(EditProfileActivity.this);
        selectorImageSrc = new AlertDialog.Builder(this);
        selectorImageSrc.setTitle("Select source");
        options = new CharSequence[]{"Image from Gallery", "Photo from camera"};

        ip = new ImagesProvider();
        up = new UsersProvider();
        fap = new FirebaseAuthProvider();

        getUserProfile();
    }

    private void updateProfile() {
        String username = usernameEditProfileTextView.getText().toString();
        String phoneNumber = phoneEditProfileTextView.getText().toString();
        if (username.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Fill username and phone number", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("phoneNumber", phoneNumber);
            data.put("timestamp", new Date().getTime());
            data.put("photoProfile", urlImageUploaded);
            up.update(data, fap.getCurrentUid()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "An error occurred while updating the user", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void selectImageSrc() {
        selectorImageSrc.setItems(options, (dialogInterface, i) -> {
            if (i == GALLERY_RC) openGallery(GALLERY_RC);
            if (i == CAMERA_RC) openCamera(CAMERA_RC);
        });
        selectorImageSrc.show();
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void openCamera(int requestCode) {
        Intent photoCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File file = takePhotoFromCamera();
                if (file != null) {
                    Uri uri = FileProvider.getUriForFile(this, "com.ilerna.vendesininmobiliarias", file);
                    photoCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(photoCameraIntent, requestCode);
                }
            } catch (Exception ex) {
                loadingDialog.dismiss();
                Toast.makeText(this, "An error ocurred on open the camera.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private File takePhotoFromCamera() throws IOException {
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photo = File.createTempFile(new Timestamp(new Date().getTime()) + "_photo", ".jpg", directory);
        photoCameraPath = "file:" + photo.getAbsolutePath();
        photoCameraAbsolutePath = photo.getAbsolutePath();
        photoCameraFile = photo;
        return photo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadingDialog.start();
        setImageFromFile(requestCode, data);
    }

    private void setImageFromFile(int RC, Intent data) {
        try {
            Uri uri;
            if (RC == CAMERA_RC) uri = Uri.fromFile(photoCameraFile);
            else uri = data.getData();

            fileImage = Utils.from(this, uri);
            photoEditProfileImageView.setImageBitmap(BitmapFactory.decodeFile(fileImage.getAbsolutePath()));
            photoEditProfileImageView.clearColorFilter();
            photoEditProfileImageView.setBackgroundTintList(null);
            photoEditProfileImageView.getDrawable().setTintList(null);
            uploadImageToFirebase(fileImage);
        } catch (Exception ex) {
            loadingDialog.dismiss();
            Log.d("ERROR", "Error loading file from gallery");
            Toast.makeText(this, "Error trying to opening gallery " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageToFirebase(File fileImage) {
        ip.save(this, fileImage).addOnSuccessListener(task -> {
            ip.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                loadingDialog.dismiss();
                String url = uri.toString();
                urlImageUploaded = url;
                Toast.makeText(this, "The images is uploaded.", Toast.LENGTH_LONG).show();
            });
        });
    }

    private void getUserProfile() {
        up.getUser(fap.getCurrentUid()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("username")) {
                    String username = documentSnapshot.getString("username");
                    if (username != null && !username.isEmpty())
                        usernameEditProfileTextView.setText(username);
                }
                if (documentSnapshot.contains("phoneNumber")) {
                    String phoneNumber = documentSnapshot.getString("phoneNumber");
                    if (phoneNumber != null && !phoneNumber.isEmpty())
                        phoneEditProfileTextView.setText(phoneNumber);
                }
                if (documentSnapshot.contains("photoProfile")) {
                    String photoProfile = documentSnapshot.getString("photoProfile");
                    urlImageUploaded = photoProfile;
                    if (photoProfile != null && !photoProfile.isEmpty())
                        new Utils.ImageDownloadTasK((ImageView) findViewById(R.id.photoEditProfileImageView)).execute(photoProfile);
                }
            } else {
                Toast.makeText(this, "The user dosen't exist.", Toast.LENGTH_LONG).show();
            }
        });
    }
}