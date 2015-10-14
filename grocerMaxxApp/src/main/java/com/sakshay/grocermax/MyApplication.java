package com.sakshay.grocermax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.flurry.android.FlurryAgent;

public class MyApplication extends Application {
    
	private static MyApplication mApplication;
	Context context;
	
	@SuppressLint("NewApi") @Override
	public void onCreate() {
		
		super.onCreate();
		mApplication = this;
		// configure Flurry
		FlurryAgent.setLogEnabled(false);
		// init Flurry
		FlurryAgent.init(this, getResources().getString(R.string.flurry_api_key));
	}
	
	public static MyApplication getInstance() {
		return mApplication;
	}
	
	

}
