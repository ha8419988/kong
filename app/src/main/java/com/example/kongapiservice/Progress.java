package com.example.kongapiservice;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress {
    static ProgressDialog progress;


    static public void showDialog(Context context) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(true);
        if (progress != null && !progress.isShowing())
            progress.show();
    }





}
