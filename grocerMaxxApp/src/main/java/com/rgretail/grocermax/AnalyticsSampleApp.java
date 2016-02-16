package com.rgretail.grocermax;
import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;

/**
 * Created by Abhishek on 12/9/2015.
 */

/**
 * An extension to Application class to provide tracker for analytics purposes. Having the tracker
 * instances here allows all the activities to access the same tracker instances. The trackers can
 * be initialised on startup or when they are required based on performance requirements.
 */
public class AnalyticsSampleApp extends Application {

    // The following line should be changed to include the correct property id.
//    private static final String PROPERTY_ID = "UA-XXXXX-Y";
    private static String PROPERTY_ID;

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.

    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public AnalyticsSampleApp() {
        super();
        PROPERTY_ID = getResources().getString(R.string.ga_trackingId);
    }

    synchronized public Tracker getTracker(TrackerName trackerId) {
//    synchronized public Tracker getTracker() {
        if (!mTrackers.containsKey(trackerId)) {
//        if (!mTrackers.containsKey(PROPERTY_ID)) {


            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(PROPERTY_ID);
//            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
//                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.gl)
//                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);
//            mTrackers.put(PROPERTY_ID, t);

        }
        return mTrackers.get(trackerId);
    }
}