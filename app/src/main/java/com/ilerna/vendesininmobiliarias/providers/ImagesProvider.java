package com.ilerna.vendesininmobiliarias.providers;

import android.content.Context;

import java.util.Date;
import java.sql.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ilerna.vendesininmobiliarias.Utils.Utils;

import java.io.File;

public class ImagesProvider {

    StorageReference storageRef;
    StorageReference storageRefCurrent;

    public ImagesProvider() {
        storageRef = FirebaseStorage.getInstance().getReference("Images");
        storageRefCurrent = FirebaseStorage.getInstance().getReference("Images");

    }

    public UploadTask save(Context context, File file) {
        byte[] imageBytes = Utils.bitmapCompresor(file);
        StorageReference storage = storageRef.child(new Timestamp(new Date().getTime()) + file.getName());
        storageRefCurrent = storage;
        return storage.putBytes(imageBytes);
    }

    public StorageReference getStorage() {
        return storageRefCurrent;
    }
}
