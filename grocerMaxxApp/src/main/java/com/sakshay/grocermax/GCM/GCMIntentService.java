/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sakshay.grocermax.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.*;
import com.google.android.gms.gcm.GcmListenerService;
import com.sakshay.grocermax.CartProductList;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.SplashScreen;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.utils.UrlsConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    String strLinkurl,strName;
    Context context = this;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        try{
            String message = data.getString("message");
            strName = data.getString("name");
            strLinkurl = data.getString("linkurl");
            String collapse_key = data.getString("collapse_key");
            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Message: " + message);
            sendNotification(message);
        }catch(Exception e){}

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }


//        String str = data.getString();



        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
//        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {

//        String linkurl = "";
//        int index = 0;
//        String strType = "";
//        if (linkurl.contains("?")) {
//            index = linkurl.indexOf("?");
//            if (linkurl.length() >= index) {
//                strType = linkurl.substring(0, index);
//                System.out.println("====result is====" + strType);
//            }
//        } else {
//            strType = linkurl;
//        }

        ((HotOffersActivity) context).addActionsInFilter(MyReceiverActions.ALL_PRODUCTS_CATEGORY);
        ((HotOffersActivity) context).showDialog();
        String strUrl = UrlsConstants.NEW_BASE_URL;
        ((HotOffersActivity) context).myApi.reqAllProductsCategory(strUrl + strLinkurl);
        System.out.println("===complete url====" + strUrl + strLinkurl);

//        Intent intent = new Intent(this, CartProductList.class);
        Intent intent = new Intent(this, CartProductList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle("Big Picture Expanded");
        notiStyle.setSummaryText("Nice big picture.");
        Bitmap remote_picture =  null;
        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL("http://independent.pepsimax.com.au/assets/logo-54ec3ba94fc69acc15a536331952d774.png").getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notiStyle.bigPicture(remote_picture);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("GrocerMax-Online Grocery")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(notiStyle)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}