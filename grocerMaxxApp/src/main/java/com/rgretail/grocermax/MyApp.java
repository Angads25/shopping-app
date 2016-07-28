package com.rgretail.grocermax;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static MyApp mApplication;
    Context context;

    @SuppressLint("NewApi") @Override
    public void onCreate() {

        super.onCreate();
        mApplication = this;



    }



    	public static MyApp getInstance() {
		return mApplication;
	}



}
