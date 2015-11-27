package com.rgretail.grocermax.preference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.rgretail.grocermax.utils.UtilityMethods;

/**
 * Created by Abhishek on 11/26/2015.
 */
public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        try
        {
            UtilityMethods.schedulerStart(context);
        }
        catch(Exception e){}
    }

}
