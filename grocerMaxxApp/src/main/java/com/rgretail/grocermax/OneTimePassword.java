package com.rgretail.grocermax;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsflyer.AppsFlyerLib;
import com.flurry.android.FlurryAgent;
//import com.google.analytics.tracking.android.EasyTracker;
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


public class OneTimePassword extends BaseActivity {
    OTPResponse otpDataBean;
    String params = "";
    String strEmail;
    String registraion_method;
    private JSONObject jsonObjectParams;
    String phone_number;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        try{
            AppsFlyerLib.setCurrencyCode("INR");
            AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
            AppsFlyerLib.sendTracking(getApplicationContext());
        }catch(Exception e){}
        try{
                Bundle bundle = getIntent().getExtras();
                addActionsInFilter(MyReceiverActions.REGISTER_USER);
                addActionsInFilter(MyReceiverActions.LOGIN);
                addActionsInFilter(MyReceiverActions.OTP);

                if (bundle != null) {
                     otpDataBean = (OTPResponse)bundle.getSerializable("Otp");
                     params = bundle.getString("USER_REGISTER_DATA");
                     strEmail = bundle.getString("USER_EMAIL");
                     registraion_method = bundle.getString("REGISTRATION_METHOD");

                     JSONObject paramObject=new JSONObject(params);
                     phone_number=paramObject.getString("number");

                }

                final EditText etOTP = (EditText)findViewById(R.id.et_otp);
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
                Button btnResend = (Button)findViewById(R.id.btn_resendOtp);
            btnResend.setVisibility(View.VISIBLE);
                btnResend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                                    jsonObject.put("number", phone_number);
                                    System.out.println("==jsonobject==" + jsonObject);
                                }else{
                                    url = UrlsConstants.FB_LOGIN_URL;
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



            initHeader(findViewById(R.id.header), true, null);
        }catch(Exception e){
            new GrocermaxBaseException("OneTimePassword","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
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
                MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
                if (MySharedPrefs.INSTANCE.getFacebookEmail() == null) {
//                    MySharedPrefs.INSTANCE.putUserEmail(((EditText) findViewById(R.id.et_register_email)).getText().toString().trim());
                    MySharedPrefs.INSTANCE.putUserEmail(strEmail);
                }
                else {
                    MySharedPrefs.INSTANCE.putUserEmail(MySharedPrefs.INSTANCE.getFacebookEmail());
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
                    MySharedPrefs.INSTANCE.putUserEmail(MySharedPrefs.INSTANCE.getUserEmail());
                    MySharedPrefs.INSTANCE.putLoginStatus(true);
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
}
