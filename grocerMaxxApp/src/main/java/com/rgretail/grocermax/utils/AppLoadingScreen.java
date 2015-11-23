package com.rgretail.grocermax.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class AppLoadingScreen {
	Context mContext;
	ProgressDialog mProgressDialog;
	public static AppLoadingScreen instance;
	public AppLoadingScreen(Context context){
		this.mContext = context;
	}
	public static AppLoadingScreen getInstance(Context context){
		if(instance == null){
			instance = new AppLoadingScreen(context);
		}
		return instance;
	}
	
	public void showDialog() {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.show();
		mProgressDialog.setCancelable(false);
	}

	public void dismissDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
	
}
