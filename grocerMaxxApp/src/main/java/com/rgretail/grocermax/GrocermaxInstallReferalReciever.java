package com.rgretail.grocermax;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appsflyer.MultipleInstallBroadcastReceiver;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.RocqCampaignReceiver;

/**
 * Created by anchit-pc on 19-Feb-16.
 */
public class GrocermaxInstallReferalReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*For AppsFlyer*/
        try {
            new MultipleInstallBroadcastReceiver().onReceive(context,intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*For Rocq Analytics*/
        try {
            RocqAnalytics.initialize(context);
            RocqCampaignReceiver.trackCampaign(intent,context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*InviteReferrals  Sdk*/
       /* try {
            new InviteReferrerBroadcastReceiver().onReceive(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }
}
