package com.ilerna.vendesininmobiliarias.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.ilerna.vendesininmobiliarias.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    void start() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));

        // Avoid tapping over the screen to hide the dialog
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismiss() {
        alertDialog.dismiss();
    }
}
