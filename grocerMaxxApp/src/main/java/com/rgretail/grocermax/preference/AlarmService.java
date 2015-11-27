package com.rgretail.grocermax.preference;

import android.app.Service;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

import com.facebook.internal.Utility;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

/**
 * Created by Abhishek on 11/26/2015.
 */
public class AlarmService extends Service {
    int strCount=0;
    public static AlarmService alarmService;
    public static AlarmService getInstance()
    {
        if (alarmService == null)
        {
            alarmService = new AlarmService();
        }
        return alarmService;
    }
    public AlarmService()
    {
        alarmService = this;
    }

    public Context context;

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        try
        {
            AppConstants.b2DaysUpdateDialog = true;
        }
        catch(Exception e){}
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

}

