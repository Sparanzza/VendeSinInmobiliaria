package com.ilerna.vendesininmobiliarias.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    ImageView arrowBack;

    ImageView imageView0, imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;
    ImageView imageViewHomes, imageViewOffices, imageViewFactories, imageViewFlats, imageViewStorages, imageViewFields, imageViewGarages, imageViewCommercials;
    List<ImageView> imageViews;
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
    final int GALLERY_RC_OFFSET = 10;

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
    AlertDialog.Builder selectorImageSrc;
    CharSequence[] options;

    String photoCameraPath;
    String photoCameraAbsolutePath;
    File photoCameraFile;

    // features
    AutoCompleteTextView statusEditText;
    AutoCompleteTextView parkingEditText;
    AutoCompleteTextView emissionsEditText;
    AutoCompleteTextView orientationEditText;
    AutoCompleteTextView energyConsumptionEditText;
    AutoCompleteTextView furnishedEditText;
    AutoCompleteTextView heatingEditText;
    AutoCompleteTextView acEditText;
    AutoCompleteTextView elevatorEditText;
    TextInputEditText bedroomEditText;
    TextInputEditText bathroomEditText;
    TextInputEditText sqmEditText;
    TextInputEditText floorEditText;
    TextInputEditText antiquityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // @formatter:off
        //Strings Arrays
        String[] yesNoStrings = getResources().getStringArray(R.array.yes_no);
        String[] parkingStrings = getResources().getStringArray(R.array.parking);
        String[] emissionStrings = getResources().getStringArray(R.array.emission);
        String[] orientationStrings = getResources().getStringArray(R.array.orientation);
        String[] statusStrings = getResources().getStringArray(R.array.status);

        // Adapter Arrays
        ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, R.id.itemTextViewDropDown, yesNoStrings);
        ArrayAdapter<String> parkingAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, R.id.itemTextViewDropDown, parkingStrings);
        ArrayAdapter<String> emissionAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, R.id.itemTextViewDropDown, emissionStrings);
        ArrayAdapter<String> orientationAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, R.id.itemTextViewDropDown, orientationStrings);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, R.id.itemTextViewDropDown, statusStrings);

        bedroomEditText = findViewById(R.id.bedroomEditText);
        bathroomEditText = findViewById(R.id.bathroomEditText);
        sqmEditText = findViewById(R.id.sqmEditText);
        floorEditText = findViewById(R.id.floorEditText);
        antiquityEditText = findViewById(R.id.antiquityEditText);

        // Set adapter to features
        statusEditText = findViewById(R.id.statusEditText);
        statusEditText.setText(statusAdapter.getItem(0).toString(), false);
        statusEditText.setAdapter(statusAdapter);

        acEditText = findViewById(R.id.acEditText);
        acEditText.setText(yesNoAdapter.getItem(0).toString(), false);
        acEditText.setAdapter(yesNoAdapter);

        heatingEditText = findViewById(R.id.heatingEditText);
        heatingEditText.setText(yesNoAdapter.getItem(0).toString(), false);
        heatingEditText.setAdapter(yesNoAdapter);

        furnishedEditText = findViewById(R.id.furnishedEditText);
        furnishedEditText.setText(yesNoAdapter.getItem(0).toString(), false);
        furnishedEditText.setAdapter(yesNoAdapter);

        energyConsumptionEditText = findViewById(R.id.energyConsumptionEditText);
        energyConsumptionEditText.setText(emissionAdapter.getItem(0).toString(), false);
        energyConsumptionEditText.setAdapter(emissionAdapter);

        emissionsEditText = findViewById(R.id.emissionsEditText);
        emissionsEditText.setText(emissionAdapter.getItem(0).toString(), false);
        emissionsEditText.setAdapter(emissionAdapter);

        orientationEditText = findViewById(R.id.orientationEditText);
        orientationEditText.setText(orientationAdapter.getItem(0).toString(), false);
        orientationEditText.setAdapter(orientationAdapter);

        parkingEditText = findViewById(R.id.parkingEditText);
        parkingEditText.setText(parkingAdapter.getItem(0).toString(), false);
        parkingEditText.setAdapter(parkingAdapter);

        elevatorEditText = findViewById(R.id.elevatorEditText);
        elevatorEditText.setText(yesNoAdapter.getItem(0).toString(), false);
        elevatorEditText.setAdapter(yesNoAdapter);

        // Pictures
        imageView0 = findViewById(R.id.imageView0);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);

        imageView0.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_0); });
        imageView1.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_1); });
        imageView2.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_2); });
        imageView3.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_3); });
        imageView4.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_4); });
        imageView5.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_5); });
        imageView6.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_6); });
        imageView7.setOnClickListener(view -> { selectImageSrc(GALLERY_RC_7); });

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
        selectorImageSrc = new AlertDialog.Builder(this);
        selectorImageSrc.setTitle("Select source");
        options = new CharSequence[]{"Image from Gallery", "Photo from camera"};

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

            // Images
            post.setImage0(urlsImagesUploaded[0]);
            post.setImage1(urlsImagesUploaded[1]);
            post.setImage2(urlsImagesUploaded[2]);
            post.setImage3(urlsImagesUploaded[3]);
            post.setImage4(urlsImagesUploaded[4]);
            post.setImage5(urlsImagesUploaded[5]);
            post.setImage6(urlsImagesUploaded[6]);
            post.setImage7(urlsImagesUploaded[7]);

            // Id User
            post.setUserUid(fap.getCurrentUid());

            // Features
            post.setBedroom(bedroomEditText.getText().toString());
            post.setBathroom(bathroomEditText.getText().toString());
            post.setSqm(sqmEditText.getText().toString());
            post.setFloor(floorEditText.getText().toString());
            post.setAntiquity(antiquityEditText.getText().toString());
            post.setParking(parkingEditText.getText().toString());
            post.setStatus(statusEditText.getText().toString());
            post.setElevator(elevatorEditText.getText().toString());
            post.setHeating(heatingEditText.getText().toString());
            post.setAc(acEditText.getText().toString());
            post.setOrientation(orientationEditText.getText().toString());
            post.setFurnished(furnishedEditText.getText().toString());
            post.setEmissions(emissionsEditText.getText().toString());
            post.setConsumption(energyConsumptionEditText.getText().toString());

            post.setTimestamp(new Date().getTime());

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
            loadingDialog.dismiss();
            Toast.makeText(this, "There are empty fields!", Toast.LENGTH_LONG).show();
        }
    }

    private void selectImageSrc(int requestCode) {
        selectorImageSrc.setItems(options, (dialogInterface, i) -> {
            if (i == 0) openGallery(requestCode);
            if (i == 1) openCamera(requestCode);
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
                    startActivityForResult(photoCameraIntent, requestCode + GALLERY_RC_OFFSET);
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
            if (RC >= GALLERY_RC_OFFSET) {
                uri = Uri.fromFile(photoCameraFile);
                RC = RC - GALLERY_RC_OFFSET;
            } else {
                uri = data.getData();
            }
            fileImages[RC] = Utils.from(this, uri);
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