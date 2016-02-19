package com.rgretail.grocermax;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.dq.rocq.RocqAnalytics;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class MyApplication extends Application {



	private static MyApplication mApplication;
	Context context;
    public static boolean isFromDrawer=false;
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

           /*initialization of rocq analytics*/
            RocqAnalytics.initialize(getApplicationContext());

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
