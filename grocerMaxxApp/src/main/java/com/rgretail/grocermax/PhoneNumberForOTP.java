package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.OTPResponse;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

/**
 * Created by anchit-pc on 07-Jan-16.
 */
public class PhoneNumberForOTP extends BaseActivity {

    TextView tv_msg,tv_suggesion;
    EditText edt_phoneNumber;
    Button btnSendOtp;
    private JSONObject jsonObjectParams;
    public static TextView tv;
    ImageView icon_header_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);

        addActionsInFilter(MyReceiverActions.OTP);
        try {
            AppsFlyerLib.setCurrencyCode("INR");
            AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
            AppsFlyerLib.sendTracking(getApplicationContext());
        } catch (Exception e) {}

        tv_msg=(TextView)findViewById(R.id.tv_msg);
        tv_suggesion=(TextView)findViewById(R.id.tv_suggesion);
        edt_phoneNumber=(EditText)findViewById(R.id.et_otp);
        btnSendOtp=(Button)findViewById(R.id.btn_submit);
        tv=(TextView)findViewById(R.id.tv);
        tv.setText("Enter Mobile Number");
        btnSendOtp.setText("SUBMIT");
        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_msg.setText("Enter your 10 digit mobile number.");
        tv_suggesion.setVisibility(View.VISIBLE);
        edt_phoneNumber.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        edt_phoneNumber.setHint("Enter number");

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_phoneNumber.getText().toString().trim().length()==0){
                    UtilityMethods.customToast("Please enter your phone number.",PhoneNumberForOTP.this);
                }else if(edt_phoneNumber.getText().toString().trim().length()>10 || edt_phoneNumber.getText().toString().trim().length()<10){
                    UtilityMethods.customToast("Please enter correct phone number",PhoneNumberForOTP.this);
                }else{
                       try{
                           if (UtilityMethods.isInternetAvailable(mContext)) {
                               showDialog();
                               String url;
                               JSONObject jsonObject = new JSONObject();
                               if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
                               {
                                   url = UrlsConstants.FB_LOGIN_URL;
                                   jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
                                   jsonObject.put("quote_id","no");
                                   jsonObject.put("fname",MySharedPrefs.INSTANCE.getFirstName());
                                   jsonObject.put("lname", MySharedPrefs.INSTANCE.getLastName());
                                   jsonObject.put("number", edt_phoneNumber.getText().toString());
                                   System.out.println("==jsonobject==" + jsonObject);
                               }else{
                                   url = UrlsConstants.FB_LOGIN_URL;
                                   jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
                                   jsonObject.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
                                   jsonObject.put("fname",MySharedPrefs.INSTANCE.getFirstName());
                                   jsonObject.put("lname", MySharedPrefs.INSTANCE.getLastName());
                                   jsonObject.put("number", edt_phoneNumber.getText().toString());
                                }
                               jsonObject.put("otp", "0");
                               jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                               jsonObject.put("device_id",UtilityMethods.getDeviceId(PhoneNumberForOTP.this));
                               myApi.reqUserRegistrationOTP(url,jsonObject);
                           } else {
                               UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
                           }
                       }catch (Exception e){}
                }
            }
        });

        initHeader(findViewById(R.id.header), true, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            //uses when coming from OneTimePassword screen after to register successfully this will finish and go back to loginactivity and loginactivity also uses same funct
            if(requestCode==123)
            {
                if(resultCode==RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }catch(Exception e){
            new GrocermaxBaseException("Registeration","requestCode123",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
        try {
//            EasyTracker.getInstance(this).activityStart(this);
            FlurryAgent.onStartSession(this, getResources().getString(R.string.flurry_api_key));
            FlurryAgent.onPageView();         //Use onPageView to report page view count.
        }catch(Exception e){}
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try{
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
        try{
//            EasyTracker.getInstance(this).activityStop(this);
            FlurryAgent.onEndSession(this);
        }catch(Exception e){}
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            initHeader(findViewById(R.id.header), true, null);
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
    }

    @Override
    public void OnResponse(Bundle bundle) {

        if (bundle.getString("ACTION").equals(MyReceiverActions.OTP)) {
            dismissDialog();
            OTPResponse otpDataBean = (OTPResponse) bundle.getSerializable(ConnectionService.RESPONSE);
            if(otpDataBean.getFlag().equals("1")) {
                try {
                    jsonObjectParams = new JSONObject();
                    jsonObjectParams.put("fname", MySharedPrefs.INSTANCE.getFirstName());
                    jsonObjectParams.put("lname", MySharedPrefs.INSTANCE.getLastName());
                    jsonObjectParams.put("uemail", MySharedPrefs.INSTANCE.getUserEmail());
                    jsonObjectParams.put("number", edt_phoneNumber.getText().toString().trim());
                    jsonObjectParams.put("otp", "1");
                    jsonObjectParams.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                    jsonObjectParams.put("device_id",UtilityMethods.getDeviceId(PhoneNumberForOTP.this));
                    if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
                    jsonObjectParams.put("quote_id","no");
                    else
                    jsonObjectParams.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
                } catch (Exception e) {
                }
                MySharedPrefs.INSTANCE.putMobileNo(edt_phoneNumber.getText().toString().trim());
                Intent intent = new Intent(this, OneTimePassword.class);
                Bundle call_bundle = new Bundle();
                call_bundle.putSerializable("Otp", otpDataBean);
                call_bundle.putSerializable("USER_REGISTER_DATA", String.valueOf(jsonObjectParams));
                call_bundle.putString("USER_EMAIL", MySharedPrefs.INSTANCE.getUserEmail());
                call_bundle.putString("REGISTRATION_METHOD", "social");
                intent.putExtras(call_bundle);
                startActivityForResult(intent, 123);
            }else{
                UtilityMethods.customToast(otpDataBean.getResult(), mContext);
            }
        }
    }
}
