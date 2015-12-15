package com.rgretail.grocermax;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class MyApplication extends Application {
    
	private static MyApplication mApplication;
	Context context;
//	private static String PROPERTY_ID;


	/**
	 * The Analytics singleton. The field is set in onCreate method override when the application
	 * class is initially created.
	 */
	private static GoogleAnalytics analytics;

	/**
	 * The default app tracker. The field is from onCreate callback when the application is
	 * initially created.
	 */
	private static Tracker tracker;

	/**
	 * Access to the global Analytics singleton. If this method returns null you forgot to either
	 * set android:name="&lt;this.class.name&gt;" attribute on your application element in
	 * AndroidManifest.xml or you are not setting this.analytics field in onCreate method override.
	 */
	public static GoogleAnalytics analytics() {
		return analytics;
	}

	/**
	 * The default app tracker. If this method returns null you forgot to either set
	 * android:name="&lt;this.class.name&gt;" attribute on your application element in
	 * AndroidManifest.xml or you are not setting this.tracker field in onCreate method override.
	 */
	public static Tracker tracker() {
		return tracker;
	}


	@SuppressLint("NewApi") @Override
	public void onCreate() {
		
		super.onCreate();
		try {
			mApplication = this;
			// configure Flurry
			FlurryAgent.setLogEnabled(false);
			// init Flurry
			FlurryAgent.init(this, getResources().getString(R.string.flurry_api_key));
		}catch (Exception e){}

		analytics = GoogleAnalytics.getInstance(this);

		String analytics_id = getResources().getString(R.string.ga_trackingId);

		// TODO: Replace the tracker-id with your app one from https://www.google.com/analytics/web/
//        tracker = analytics.newTracker("UA-54478999-1");
		tracker = analytics.newTracker(analytics_id);

		// Provide unhandled exceptions reports. Do that first after creating the tracker
//		tracker.enableExceptionReporting(true);

		// Enable Remarketing, Demographics & Interests reports
		// https://developers.google.com/analytics/devguides/collection/android/display-features
//		tracker.enableAdvertisingIdCollection(true);

		// Enable automatic activity tracking for your app
		tracker.enableAutoActivityTracking(true);


	}





	public static MyApplication getInstance() {
		return mApplication;
	}

//	public enum TrackerName {
//		APP_TRACKER, // Tracker used only in this app.
//		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
//		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
//	}

//	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

//	synchronized public Tracker getTracker(TrackerName trackerId) {
//		PROPERTY_ID = getResources().getString(R.string.ga_trackingId);
////    synchronized public Tracker getTracker() {
//		if (!mTrackers.containsKey(trackerId)) {
////        if (!mTrackers.containsKey(PROPERTY_ID)) {
//
//			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
////            Tracker t = analytics.newTracker(PROPERTY_ID);
//
//
//			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
//					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
//					: analytics.newTracker(R.xml.ecommerce_tracker);
//			mTrackers.put(trackerId, t);
//
////            mTrackers.put(PROPERTY_ID, t);
//		}
//		return mTrackers.get(trackerId);
//	}
	
	

}
