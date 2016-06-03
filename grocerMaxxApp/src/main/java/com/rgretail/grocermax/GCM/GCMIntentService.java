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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.dq.rocq.push.RocqGcmIntentService;
import com.google.android.gms.gcm.GcmListenerService;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.hotoffers.HomeScreen;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    String strLinkurl,strName,strImageUrl,strSubText,feedback,orderid,cus_id;
    Context context = this;

    public static int count,i;

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

            if(!new RocqGcmIntentService().handleRocqMessage(data, getApplicationContext()))
            {
                System.out.println("notification recieved");

                String message = data.getString("message");
                strName = data.getString("name");
                strLinkurl = data.getString("linkurl");
                strImageUrl = data.getString("imageurl");
                strSubText = data.getString("subtext");
                feedback = data.getString("feedback");
                //orderid = data.getString("orderid");
                //cus_id = data.getString("cus_id");

                String collapse_key = data.getString("collapse_key");
                Log.d(TAG, "From: " + from);
                Log.d(TAG, "Message: " + message);

                if(feedback.equals("0"))
                sendNotification(message,data);
                else
                sendFeedbackNotification(message,data);
            }

        }catch(Exception e){}

    }
    //public static RemoteViews expandedView;
    //public static Notification notification;
    //public static NotificationManager mNotificationManager;
    public void sendFeedbackNotification(String message,Bundle data){

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = new Notification();
        noti = setCustomViewNotification(message,data);
        noti.defaults |= Notification.DEFAULT_LIGHTS;
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        noti.defaults |= Notification.DEFAULT_SOUND;

        noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

        mNotificationManager.notify(5, noti);
    }
    private Notification setCustomViewNotification(String message,Bundle data) {
        PendingIntent pending_one_star = null;
        PendingIntent pending_two_star = null;
        PendingIntent pending_three_star = null;
        PendingIntent pending_four_star = null;
        PendingIntent pending_five_star = null;

            Intent one_star = new Intent("ONE_STAR");
            one_star.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pending_one_star = PendingIntent.getBroadcast(this, 0, one_star, 0);

            Intent two_star = new Intent("TWO_STAR");
            two_star.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pending_two_star = PendingIntent.getBroadcast(this, 0, two_star, 0);

            Intent three_star = new Intent("THREE_STAR");
            three_star.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pending_three_star = PendingIntent.getBroadcast(this, 0, three_star, 0);

            Intent four_star = new Intent("FOUR_STAR");
            four_star.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pending_four_star = PendingIntent.getBroadcast(this, 0, four_star, 0);

            Intent five_star = new Intent("FIVE_STAR");
            five_star.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pending_five_star = PendingIntent.getBroadcast(this, 0, five_star, 0);



        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(this, HomeScreen.class);

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)

        stackBuilder.addParentStack(HomeScreen.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.widget_update_notification);

            expandedView.setOnClickPendingIntent(R.id.image1, pending_one_star);
            expandedView.setOnClickPendingIntent(R.id.image2, pending_two_star);
            expandedView.setOnClickPendingIntent(R.id.image3, pending_three_star);
            expandedView.setOnClickPendingIntent(R.id.image4, pending_four_star);
            expandedView.setOnClickPendingIntent(R.id.image5, pending_five_star);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.grocemax_cloud)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.app_icon))
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentTitle(strName).build();

        notification.bigContentView = expandedView;
        return notification;
    }




    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message,Bundle data) {
        Intent intent = new Intent(this, HomeScreen.class);
        data.putBoolean("IS_FROM_NOTIFICATION",true);
        data.putString("rating_count","0");
        intent.putExtras(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        //notiStyle.setBigContentTitle(strName);
           //notiStyle.setSummaryText(message);
            Bitmap remote_picture = null;
            try {
                remote_picture = BitmapFactory.decodeStream((InputStream) new URL(strImageUrl).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notiStyle.bigPicture(remote_picture);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.grocemax_cloud)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.app_icon))
                .setContentTitle(strName)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(!strSubText.equals("")){
            notificationBuilder.setSubText(strSubText);
        }

        if(!strImageUrl.equals("")) {
            if(strSubText.equals(""))
              notiStyle.setSummaryText(message);
           notificationBuilder.setStyle(notiStyle);
        }else{
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        /*if(count==1){
            count ++;
        }
        else{
            i++;

        }*/
       // notificationBuilder.build().number +=i;
        count++;


        notificationManager.notify( count /* ID of notification */, notificationBuilder.build());
    }



    public static class DownloadCancelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case "ONE_STAR":
                    startApp(context,"1");
                    break;
                case "TWO_STAR":
                    startApp(context,"2");
                    break;
                case "THREE_STAR":
                    startApp(context,"3");
                    break;
                case "FOUR_STAR":
                    startApp(context,"4");
                    break;
                case "FIVE_STAR":
                    startApp(context,"5");
                    break;
            }
        }

        public void  startApp(Context con,String count){
            NotificationManager mNM = (NotificationManager)con.getSystemService(NOTIFICATION_SERVICE);
            mNM.cancel(5);
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            con.sendBroadcast(it);
            Intent intent = new Intent(con, HomeScreen.class);
            Bundle data=new Bundle();
            data.putBoolean("IS_FROM_NOTIFICATION",true);
            data.putString("rating_count",count);
            intent.putExtras(data);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            con.startActivity(intent);
        }


    }



}