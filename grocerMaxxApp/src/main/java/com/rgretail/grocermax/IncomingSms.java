package com.rgretail.grocermax;

/**
 * Created by anchit-pc on 17-Feb-16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.rgretail.grocermax.preference.MySharedPrefs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomingSms extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    try
                    {
                        if (senderNum .contains("GROCER"))
                        {
                            if(MySharedPrefs.INSTANCE.getOTPScreenName().equals("OneTimePassword")){
                            OneTimePassword Sms = new OneTimePassword();
                            Sms.recivedSms(extractDigits(message));
                            }else  if(MySharedPrefs.INSTANCE.getOTPScreenName().equals("EditProfile")){
                            EditProfile edt=new EditProfile();
                            edt.recivedSms(extractDigits(message));
                            }

                        }
                    }
                    catch(Exception e){}

                }
            }

        } catch (Exception e)
        {

        }
    }

    public static String extractDigits(final String in) {
        final Pattern p = Pattern.compile( "(\\d{6})" );
        final Matcher m = p.matcher( in );
        if ( m.find() ) {
            return m.group( 0 );
        }
        return "";
    }

}
