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
package com.rgretail.grocermax.GCM;

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

import com.google.android.gms.gcm.GcmListenerService;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.hotoffers.HotOffersActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    String strLinkurl,strName,strImageUrl;
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
            strImageUrl = data.getString("imageurl");

            String collapse_key = data.getString("collapse_key");
            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Message: " + message);
            sendNotification(message,data);
        }catch(Exception e){}

    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message,Bundle data) {
        Intent intent = new Intent(this, HotOffersActivity.class);
        data.putBoolean("IS_FROM_NOTIFICATION",true);
        intent.putExtras(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle(strName);
        notiStyle.setSummaryText(message);
        Bitmap remote_picture =  null;
        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(strImageUrl).getContent());
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