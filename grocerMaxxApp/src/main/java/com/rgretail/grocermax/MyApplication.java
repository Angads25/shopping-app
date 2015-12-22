package com.rgretail.grocermax;
import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class MyApplication extends Application {

//	// The following line should be changed to include the correct property id.
//	private static final String PROPERTY_ID = "UA-64820863-1";
//
//	//Logging TAG
//	private static final String TAG = "MyApp";
//
//	public static int GENERAL_TRACKER = 0;
//
//	public enum TrackerName {
//		APP_TRACKER, // Tracker used only in this app.
//		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
//		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
//	}
//
//	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
//
//	public MyApplication() {
//		super();
//	}

	private Tracker mTracker;


	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			mTracker = analytics.newTracker(R.xml.global_tracker);
		}
		return mTracker;
	}


//	synchronized Tracker getTracker(TrackerName trackerId) {
//		if (!mTrackers.containsKey(trackerId)) {
//
//			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
//					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(PROPERTY_ID)
//					: analytics.newTracker(R.xml.ecommerce_tracker);
//			mTrackers.put(trackerId, t);
//
//		}
//		return mTrackers.get(trackerId);
//	}
}