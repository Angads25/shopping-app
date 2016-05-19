package com.rgretail.grocermax;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.LoginResponse;
import com.rgretail.grocermax.bean.OTPResponse;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

//import com.google.analytics.tracking.android.EasyTracker;


public class OneTimePassword extends BaseActivity {
    OTPResponse otpDataBean;
    String params = "";
    String strEmail;
    String registraion_method;
    private JSONObject jsonObjectParams;
    String phone_number;
    TextView tv_msg;
    ImageView icon_header_back;
    public static EditText etOTP;
    public static  Button btnResend;

    /*for progress bar*/
    public static TextView tv_time,tv_pBarMsg,tv;
    public static ProgressBar pBar;
    Thread t;
    int pStatus = 0;
    private Handler handler = new Handler();
    ProgressTask pTask;


    @Override
    protected void onStart() {
        super.onStart();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
        try {
//            EasyTracker.getInstance(this).activityStart(this);
            FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
            FlurryAgent.onPageView();         //Use onPageView to report page view count.
        }catch(Exception e){}
        /*screen tracking using rocq*/
        try {
            RocqAnalytics.initialize(this);
            RocqAnalytics.startScreen(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /*------------------------------*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        pStatus = 0;
        try {
            AppsFlyerLib.setCurrencyCode("INR");
            AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
            AppsFlyerLib.sendTracking(getApplicationContext());
        }catch(Exception e){}
        try{
                Bundle bundle = getIntent().getExtras();
                addActionsInFilter(MyReceiverActions.REGISTER_USER);
                addActionsInFilter(MyReceiverActions.LOGIN);
                addActionsInFilter(MyReceiverActions.OTP);

            MySharedPrefs.INSTANCE.putOTPScreenName("OneTimePassword");

                if (bundle != null) {
                     otpDataBean = (OTPResponse)bundle.getSerializable("Otp");
                     params = bundle.getString("USER_REGISTER_DATA");
                     strEmail = bundle.getString("USER_EMAIL");
                     registraion_method = bundle.getString("REGISTRATION_METHOD");

                     JSONObject paramObject=new JSONObject(params);
                     phone_number=paramObject.getString("number");

                }
                tv_msg=(TextView)findViewById(R.id.tv_msg);
                tv=(TextView)findViewById(R.id.tv);
                tv.setText("Enter OTP");
                tv_msg.setText(Html.fromHtml("Please enter the one time password (OTP) sent to <b>+91"+MySharedPrefs.INSTANCE.getMobileNo()+"</b>"));
                icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
                icon_header_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       onBackPressed();
                    }
                });

                etOTP = (EditText)findViewById(R.id.et_otp);
                Button btnOTP = (Button)findViewById(R.id.btn_submit);
                btnOTP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etOTP.getText().toString().equals("")) {
                            UtilityMethods.customToast("Please enter valid otp", OneTimePassword.this);
                            return;
                        }
                        if (otpDataBean.getOTP().equals(etOTP.getText().toString())) {
                            if (UtilityMethods.isInternetAvailable(mContext)) {
                                showDialog();
                                if (!params.equals("")) {
                                    String url;
                                    if(registraion_method.equals("regular"))  // for regular registration
                                     url= UrlsConstants.REGESTRATION_URL;
                                    else                                      // for social registration
                                     url= UrlsConstants.FB_LOGIN_URL;//here GOOGLE_LOGIN_URL can also be used both are same for social login or register
                                    try {
                                        if (!params.equals("")) {
                                            JSONObject json = new JSONObject(params);
                                            if(registraion_method.equals("regular")) // for regular registration
                                              myApi.reqUserRegistration(url, json);
                                            else                                    // for social registration
                                              myApi.reqLogin(url,json);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        } else {
                            UtilityMethods.customToast("Please enter valid otp", OneTimePassword.this);
                        }
                    }
                });

                /*this is click event for resending otp */
                btnResend = (Button)findViewById(R.id.btn_resendOtp);
                btnResend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            if (UtilityMethods.isInternetAvailable(mContext)) {
                                showDialog();
                                String url;
                                if(MySharedPrefs.INSTANCE.getLoginMethod().equalsIgnoreCase("Social")){
                                    url = UrlsConstants.FB_LOGIN_URL;
                                }else{
                                    url = UrlsConstants.REGESTRATION_URL;
                                }
                                JSONObject jsonObject = new JSONObject();
                                if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
                                {
                                   // url = UrlsConstants.REGESTRATION_URL;
                                    jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
                                    jsonObject.put("quote_id","no");
                                    jsonObject.put("fname",MySharedPrefs.INSTANCE.getFirstName());
                                    jsonObject.put("lname", MySharedPrefs.INSTANCE.getLastName());
                                    jsonObject.put("number", phone_number);
                                    System.out.println("==jsonobject==" + jsonObject);
                                }else{
                                   // url = UrlsConstants.REGESTRATION_URL;
                                    jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
                                    jsonObject.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
                                    jsonObject.put("fname",MySharedPrefs.INSTANCE.getFirstName());
                                    jsonObject.put("lname", MySharedPrefs.INSTANCE.getLastName());
                                    jsonObject.put("number", phone_number);
                                }
                                jsonObject.put("otp", "0");
                                jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                                jsonObject.put("device_id",UtilityMethods.getDeviceId(OneTimePassword.this));
                                myApi.reqUserRegistrationOTP(url,jsonObject);
                            } else {
                                UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
                            }
                        }catch (Exception e){}
                    }
                });



             /*show progress bar for a perticular time*/
            tv_time = (TextView) findViewById(R.id.tv_time);
            tv_pBarMsg = (TextView) findViewById(R.id.tv_waitMsg);
            pBar = (ProgressBar) findViewById(R.id.progressBar1);
            tv_time.setVisibility(View.VISIBLE);
            tv_pBarMsg.setVisibility(View.VISIBLE);
            pBar.setVisibility(View.VISIBLE);
            btnOTP.setText("VERIFY ME");
            initHeader(findViewById(R.id.header), true, null);

           /* t=new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (pStatus <= 60) {

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                pBar.setProgress(pStatus);
                                pBar.setSecondaryProgress(pStatus + 0);
                                tv_time.setText(""+(60-pStatus));
                                if(pStatus==60){
                                    tv_time.setVisibility(View.GONE);
                                    pBar.setVisibility(View.GONE);
                                    tv_pBarMsg.setVisibility(View.GONE);
                                    btnResend.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        try {
                            // Sleep for 200 milliseconds.
                            // Just to display the progress slowly
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pStatus ++;
                    }
                }
            });
            t.start();
*/

            pTask=new ProgressTask();
            pTask.execute();
            /*--------------------------------------------------------------*/


        }catch (Exception e){
            new GrocermaxBaseException("OneTimePassword","onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
        }
    }

    @Override
    public void onBackPressed() {

        setResult(1221);
        finish();
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy otp");
        try {
            pTask.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  t.stop();

        super.onDestroy();
       // t.abort = true;

    }

    public void recivedSms(String message)
    {
        try
        {
            etOTP.setText(message);
            tv_time.setVisibility(View.GONE);
            pBar.setVisibility(View.GONE);
            tv_pBarMsg.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            System.out.println("OneTimePassword.recivedSms"+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_one_time_password, menu);
        return true;
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
        try {
            RocqAnalytics.stopScreen(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnResponse(Bundle bundle) {
        try{
        if (bundle.getString("ACTION").equals(MyReceiverActions.REGISTER_USER)) {
            dismissDialog();
            LoginResponse userDataBean = (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);

            if (userDataBean.getFlag().equalsIgnoreCase("1")) {
               // try{UtilityMethods.clickCapture(mContext,"","","","",AppConstants.GA_EVENT_REGISTER_EMAIL);}catch(Exception e){}
                UtilityMethods.customToast(AppConstants.ToastConstant.REGISTER_SUCCESSFULL, mContext);
                //finish();
                MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
                MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
                MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
                if (MySharedPrefs.INSTANCE.getFacebookEmail() == null) {
//                    MySharedPrefs.INSTANCE.putUserEmail(((EditText) findViewById(R.id.et_register_email)).getText().toString().trim());
                    MySharedPrefs.INSTANCE.putUserEmail(strEmail.trim());
                }
                else {
                    MySharedPrefs.INSTANCE.putUserEmail(MySharedPrefs.INSTANCE.getFacebookEmail().trim());
                }

                /*save gcm token to our server*/
                try {
                    saveGcmTokenTOServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                MySharedPrefs.INSTANCE.putLoginStatus(true);

                ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(OneTimePassword.this, AppConstants.localCartFile);
                if (cart_products != null && cart_products.size() > 0) {
                    try {
                        JSONArray products = new JSONArray();
                        for (int i = 0; i < cart_products.size(); i++) {
                            JSONObject prod_obj = new JSONObject();
                            prod_obj.put("productid", cart_products.get(i).getItem_id());
                            prod_obj.put("quantity", cart_products.get(i).getQty());
                            products.put(prod_obj);
                        }
                        showDialog();
                        String url;
                        if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
                            System.out.println("without quote json=" + products.toString());
                            url = UrlsConstants.ADD_TO_CART_URL
                                    + userDataBean.getUserID() + "&products="
                                    + URLEncoder.encode(products.toString(), "UTF-8");
                        } else {
                            System.out.println("with quote json=" + products.toString());
                            url = UrlsConstants.ADD_TO_CART_URL
                                    + userDataBean.getUserID() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&products="
                                    + URLEncoder.encode(products.toString(), "UTF-8");
                        }
                        //String url = UrlsConstants.ADD_TO_CART_URL + userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+ "&products="+ URLEncoder.encode(products.toString(), "UTF-8");
                        myApi.reqAddToCart(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                UtilityMethods.customToast(userDataBean.getResult(), mContext);
            }
        }else if (bundle.getString("ACTION").equals(MyReceiverActions.LOGIN)) {
                dismissDialog();
                LoginResponse userDataBean = (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);
                if (userDataBean.getFlag().equalsIgnoreCase("1")) {
                    MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
                    if(MySharedPrefs.INSTANCE.getFirstName() == null){//if(MySharedPrefs.INSTANCE.getFirstName() != null){    //changed 17/9/15
                        MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
                    }
                    if(MySharedPrefs.INSTANCE.getLastName() == null){//if(MySharedPrefs.INSTANCE.getLastName() != null){      //changed 17/9/15
                        MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
                    }

                    MySharedPrefs.INSTANCE.putMobileNo(userDataBean.getMobile());
                    MySharedPrefs.INSTANCE.putUserEmail(MySharedPrefs.INSTANCE.getUserEmail().trim());
                    MySharedPrefs.INSTANCE.putLoginStatus(true);

                    /*save gcm token to our server*/
                    try {
                        saveGcmTokenTOServer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    if(userDataBean.getQuoteId() != null && !userDataBean.getQuoteId().equals("")) {
                        MySharedPrefs.INSTANCE.clearQuote();
                        MySharedPrefs.INSTANCE.putQuoteId(userDataBean.getQuoteId());/////////last change
                    }
                    MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(userDataBean.getTotalItem()));
                    setResult(RESULT_OK);
                    finish();
                }
            }else if(bundle.getString("ACTION").equals(MyReceiverActions.OTP)){
                 dismissDialog();
                 OTPResponse otpDataBean = (OTPResponse) bundle.getSerializable(ConnectionService.RESPONSE);
                 if(otpDataBean.getFlag().equals("1")) {
                     MySharedPrefs.INSTANCE.putOTPScreenName("OneTimePassword");
                     this.otpDataBean=otpDataBean;
                     UtilityMethods.customToast("OTP has been sent to your mobile number", mContext);
                 }else{
                     UtilityMethods.customToast(otpDataBean.getResult(), mContext);
                 }
             }
        }catch(Exception e){
            new GrocermaxBaseException("LoginActivity","OnResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   public class ProgressTask extends AsyncTask<String, String, String> {

       @Override
       protected String doInBackground(String... f_url) {
           try {
                    while(pStatus <= 60) {
                        if(isCancelled()){
                            break;
                        }
                        publishProgress(""+pStatus);
                           if(pStatus==60){
                             return null;
                           }
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   pStatus ++;
               }
          } catch (Exception e) {
               Log.e("Error: ", e.getMessage());
           }
          return null;
        }
       protected void onProgressUpdate(String... progress) {
           // setting progress percentage
           pBar.setProgress(Integer.parseInt(progress[0]));
           pBar.setSecondaryProgress(Integer.parseInt(progress[0]) + 0);
           tv_time.setText(""+(60-Integer.parseInt(progress[0])));
       }
       @Override
       protected void onPostExecute(String file_url) {
           tv_time.setVisibility(View.GONE);
           pBar.setVisibility(View.GONE);
           tv_pBarMsg.setVisibility(View.GONE);
           btnResend.setVisibility(View.VISIBLE);
       }

   }




}
