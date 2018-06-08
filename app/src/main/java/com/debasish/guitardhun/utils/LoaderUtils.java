package com.debasish.guitardhun.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.debasish.guitardhun.R;

public class LoaderUtils {

    public static ProgressDialog progressDialog;


    public static void showProgressBar(Context ctx, String message){
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public static void dismissProgress(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }


}
