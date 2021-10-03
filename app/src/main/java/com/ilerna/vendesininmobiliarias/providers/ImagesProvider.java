package com.ilerna.vendesininmobiliarias.providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.ilerna.vendesininmobiliarias.Utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.annotation.Nullable;

public class ImagesProvider {

    StorageReference storageRef;

    public ImagesProvider() {
        storageRef = FirebaseStorage.getInstance().getReference("Images");
    }

    public @Nullable
    UploadTask save(Context context, File file) {
        byte[] imageBytes = Utils.bitmapCompresor(file);
        StorageReference storage = storageRef.child(file.getName());
        return storage.putBytes(imageBytes);
    }
}
