package com.ilerna.vendesininmobiliarias.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public interface Utils {


    int EOF = -1;
    int DEFAULT_BUFFER_SIZE = 1024 * 4;

    // Check emails
    static boolean isEmailAddressValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }

    static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
            if (inputStream != null) {
                copy(inputStream, out);
                inputStream.close();
            }

            if (out != null) out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return tempFile;
    }

    static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    @SuppressLint("Range")
    static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    // Bitmap compresor
    // https://stackoverflow.com/questions/8417034/how-to-make-bitmap-compress-without-change-the-bitmap-size
    static byte[] bitmapCompresor(File file) {
        try {
            Bitmap original = BitmapFactory.decodeStream(new FileInputStream(file));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 50, out);
            // Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            Log.e("Original   dimensions", original.getWidth() + " " + original.getHeight());
            // Log.e("Compressed dimensions", decoded.getWidth() + " " + decoded.getHeight());
            return out.toByteArray();
        } catch (Exception ex) {
            Log.d("ERROR BITMAP COMPRESSOR", "Error saving file on database " + ex.getMessage());
            return new byte[0];
        }
    }

    // https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    static @Nullable
    Bitmap unSafeurlToBitmap(String urlImage) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlImage);
            InputStream inputStream = url.openStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e("ERROR POST", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    class ImageDownloadTasK extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public ImageDownloadTasK(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.getDrawable().setTintList(null);

        }
    }

    static final int SECOND_MILLIS = 1000;
    static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    static String getTimeAgo(long time) {
        // if timestamp given in seconds, convert to millis
        if (time < 1000000000000L) time *= 1000;

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) return null;

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) return "a moment ago";
        else if (diff < 2 * MINUTE_MILLIS) return "one minute ago";
        else if (diff < 50 * MINUTE_MILLIS) return diff / MINUTE_MILLIS + " minutes ago";
        else if (diff < 90 * MINUTE_MILLIS) return "one hour ago";
        else if (diff < 24 * HOUR_MILLIS) return diff / HOUR_MILLIS + " hours ago";
        else if (diff < 48 * HOUR_MILLIS) return "Yesterday";
        else return diff / DAY_MILLIS + " days ago";

    }

    @SuppressLint("SimpleDateFormat")
    static String timeFormatAMPM(long timestamp) {
        return new SimpleDateFormat("hh:mm a").format(new Date(timestamp));
    }

}
