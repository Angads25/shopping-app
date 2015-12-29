package com.rgretail.grocermax;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.flurry.android.FlurryAgent;

public class MyApp extends Application {

    private static MyApp mApplication;
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



    	public static MyApp getInstance() {
		return mApplication;
	}



}
