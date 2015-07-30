package com.sakshay.grocermax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    
	private static MyApplication mApplication;
	Context context;
	
	@SuppressLint("NewApi") @Override
	public void onCreate() {
		
		super.onCreate();
		mApplication = this;

	}
	
	public static MyApplication getInstance() {
		return mApplication;
	}
	
	

}
