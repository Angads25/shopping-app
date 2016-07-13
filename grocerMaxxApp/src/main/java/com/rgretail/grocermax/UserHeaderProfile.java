package com.rgretail.grocermax;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.flurry.android.FlurryAgent;
import com.invitereferrals.invitereferrals.InviteReferralsApi;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

import java.util.Random;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Abhishek on 8/25/2015.
 */
public class UserHeaderProfile extends BaseActivity implements View.OnClickListener{
    public static TextView tvUserName,tvUserEmail,tvUserMobileNo;
    RelativeLayout rlLogin,rlOrderHistory,rl_wallet,rl_rewardPoint,rlMyAddresses,rlEditProfile,rlInviteFriends,rlCallToUs,rlWriteToUs,rlSignOut;
//    rlViewProfile
    TextView tvLogin,tvOrderHistory,tvMyAddresses,tvEditProfile,tvInviteFriends,tvCallToUs,tvWriteToUs,tvSignOut;

    String[] colorArray={"#808000","#008000","#800000","#A52A2A","#2B547E","#306754","#CD7F32","#806517","#7F462C","#8C001A"};



//    tvViewProfile
//    EasyTracker tracker;
//    private static int LOGIN_SIGNUP = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);



        View viewSignOut = (View) findViewById(R.id.view_sign_out);
        ImageView ivLoginCarat = (ImageView) findViewById(R.id.iv_login_carrat);

        Random ran = new Random();
        int x = ran.nextInt(10);
        CircleView circle_view=(CircleView)findViewById(R.id.img_user_icon);
        circle_view.setTitleText(String.valueOf(MySharedPrefs.INSTANCE.getFirstName().charAt(0)).toUpperCase());
        circle_view.setFillColor(Color.parseColor(colorArray[x]));

        tvUserName = (TextView) findViewById(R.id.tv_userprofile_fullname);
        tvUserEmail = (TextView) findViewById(R.id.tv_userprofile_email);
        tvUserMobileNo = (TextView) findViewById(R.id.tv_userprofile_phoneno);

        rlLogin = (RelativeLayout) findViewById(R.id.rl_login_signup);
        rlOrderHistory = (RelativeLayout) findViewById(R.id.rl_orderhistory);
        rl_wallet = (RelativeLayout) findViewById(R.id.rl_wallet);
        rl_rewardPoint = (RelativeLayout) findViewById(R.id.rl_reward_point);
        rlMyAddresses = (RelativeLayout) findViewById(R.id.rl_myaddresses);
//        rlViewProfile = (RelativeLayout) findViewById(R.id.rl_viewprofile);
        rlEditProfile = (RelativeLayout) findViewById(R.id.rl_editprofile);
        rlInviteFriends = (RelativeLayout) findViewById(R.id.rl_invitefriends);
        rlCallToUs = (RelativeLayout) findViewById(R.id.rl_callus);
        rlWriteToUs = (RelativeLayout) findViewById(R.id.rl_writetous);
        rlSignOut = (RelativeLayout) findViewById(R.id.rl_signout);

        tvLogin = (TextView) findViewById(R.id.tv_login_signup);
        tvOrderHistory = (TextView) findViewById(R.id.tv_orderhistory);
        tvMyAddresses = (TextView) findViewById(R.id.tv_myaddresses);
//        tvViewProfile = (TextView) findViewById(R.id.tv_viewprofile);
        tvEditProfile = (TextView) findViewById(R.id.tv_editprofile);
        tvInviteFriends = (TextView) findViewById(R.id.tv_invitefriends);
        tvCallToUs = (TextView) findViewById(R.id.tv_callus);
        tvWriteToUs = (TextView) findViewById(R.id.tv_writetous);
        tvSignOut = (TextView) findViewById(R.id.tv_signout);

        rlLogin.setOnClickListener(this);
        rlOrderHistory.setOnClickListener(this);
        rl_wallet.setOnClickListener(this);
        rl_rewardPoint.setOnClickListener(this);
        rlMyAddresses.setOnClickListener(this);
//        rlViewProfile.setOnClickListener(this);
        rlEditProfile.setOnClickListener(this);
        rlInviteFriends.setOnClickListener(this);
        rlCallToUs.setOnClickListener(this);
        rlWriteToUs.setOnClickListener(this);
        rlSignOut.setOnClickListener(this);

        if (MySharedPrefs.INSTANCE.getLoginStatus()){
//        if(MySharedPrefs.INSTANCE.getFacebookName() != null){
//            if(Registration.facebookName != null) {



//                if(Registration.facebookName != null) {
//                    tvUserName.setText(Registration.facebookName);
//                    if (MySharedPrefs.INSTANCE.getFacebookName() == null) {
//                        MySharedPrefs.INSTANCE.putFacebookName(Registration.facebookName);
//                    }
//                }else if(MySharedPrefs.INSTANCE.getFacebookName() != null){
//                    tvUserName.setText(MySharedPrefs.INSTANCE.getFacebookName());
//                    if (MySharedPrefs.INSTANCE.getFacebookName() == null) {
//                        MySharedPrefs.INSTANCE.putFacebookName(Registration.facebookName);
//                    }
//                }else if(Registration.googleName != null) {
//                    tvUserName.setText(Registration.googleName);
//                    if (MySharedPrefs.INSTANCE.getGoogleName() == null) {
//                        MySharedPrefs.INSTANCE.putGoogleName(Registration.googleName);
//                    }
//                }else if(MySharedPrefs.INSTANCE.getGoogleName() != null) {
//                    tvUserName.setText(MySharedPrefs.INSTANCE.getGoogleName());
//                    if (MySharedPrefs.INSTANCE.getGoogleName() == null) {
//                        MySharedPrefs.INSTANCE.putGoogleName(Registration.googleName);
//                    }
//                }else


                if (MySharedPrefs.INSTANCE.getFirstName() != null && MySharedPrefs.INSTANCE.getLastName() != null) {
                    tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName() + " " + MySharedPrefs.INSTANCE.getLastName().replace("Google","").replace("social",""));
                }else if(MySharedPrefs.INSTANCE.getFirstName() != null){
                    tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName());
                }

//        }else if(MySharedPrefs.INSTANCE.getGoogleName() != null){
//            }else if(Registration.googleName != null){
//                    tvUserName.setText(Registration.googleName);
//                if (MySharedPrefs.INSTANCE.getGoogleName() == null) {
//                    MySharedPrefs.INSTANCE.putGoogleName(Registration.googleName);
//                }
//        }else {
//                if (MySharedPrefs.INSTANCE.getFirstName() != null && MySharedPrefs.INSTANCE.getLastName() != null) {
//                    tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName() + " " + MySharedPrefs.INSTANCE.getLastName());
//                }else if(MySharedPrefs.INSTANCE.getFirstName() != null){
//                    tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName());
//                }
//            }

//        if (MySharedPrefs.INSTANCE.getLoginStatus()) {
//            if(MySharedPrefs.INSTANCE.getFirstName() != null) {
//                tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName() + " " + MySharedPrefs.INSTANCE.getLastName());
//            }

            tvUserEmail.setText(MySharedPrefs.INSTANCE.getUserEmail());

            tvUserMobileNo.setText(MySharedPrefs.INSTANCE.getMobileNo());
//            tvLogin.setText(MySharedPrefs.INSTANCE.getUserEmail());
            rlLogin.setVisibility(View.GONE);
//            tvLogin.setTextColor(Color.WHITE);
//            rlLogin.setBackgroundColor(getResources().getColor(
//                    R.color.app_header));
            ivLoginCarat.setVisibility(View.GONE);

        } else {
            tvLogin.setText(getString(R.string.Login));
            rlSignOut.setVisibility(View.GONE);
            rlLogin.setBackgroundColor(Color.WHITE);
            tvUserName.setTextAppearance(this, R.style.normal_textsize);
            rlSignOut.setVisibility(View.GONE);
            viewSignOut.setVisibility(View.GONE);
        }

            initHeader(findViewById(R.id.app_bar_header), true, "My Profile");
//        initFooter(findViewById(R.id.footer), 4, 3);
    }

    @Override
    public void onClick(View v) {
        String userId = MySharedPrefs.INSTANCE.getUserId();
        switch (v.getId()) {
            case R.id.rl_login_signup:
                try{
                RelativeLayout rl = (RelativeLayout)v.findViewById(R.id.rl_login_signup);
                TextView tv = (TextView) rl.findViewById(R.id.tv_login_signup);
                if (tv.getText().toString().equalsIgnoreCase(getString(R.string.Login))) {
                    if (UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".CartProductList")) {  //return back to CartProductList class
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
//					//startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                        startActivityForResult(intent, 555);
                    }
                }
                }catch(Exception e){}
                break;
            case R.id.rl_orderhistory:
                try{
                if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".OrderHistory")) {
                    if (userId != null && userId.trim().length() > 0) {
                        /* tracking GA events for Order History*/
                        try{
                            UtilityMethods.clickCapture(activity,"Profile Activity","","Order History","",MySharedPrefs.INSTANCE.getSelectedCity());
                            RocqAnalytics.trackEvent("Profile Activity", new ActionProperties("Category", "Profile Activity", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", "Order History"));
                       /*QGraph event*/
                            JSONObject json=new JSONObject();
                            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                                json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                            UtilityMethods.setQGraphevent("Android Profile Activity - View Order History",json);
                   /*--------------*/

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        /*--------------*/

                        openOrderHistory();
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                    }
                }
                }catch(Exception e){}
                break;
            case R.id.rl_wallet:
                try{
                    if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".WalletActivity")) {
                        if (userId != null && userId.trim().length() > 0) {

                            /*QGraph event*/
                            JSONObject json=new JSONObject();
                            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                                json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                            UtilityMethods.setQGraphevent("Android Profile Activity - View Refund Balance",json);
                                /*--------------*/

                            Intent intent = new Intent(mContext, WalletActivity.class);
                            intent.putExtra("coming_from","wallet");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                        }
                    }
                }catch(Exception e){}
                break;
            case R.id.rl_reward_point:
                try{
                    if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".WalletActivity")) {
                        if (userId != null && userId.trim().length() > 0) {

                             /*QGraph event*/
                            JSONObject json=new JSONObject();
                            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                                json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                            UtilityMethods.setQGraphevent("Android Profile Activity - View Max Coins",json);
                                /*--------------*/


                            Intent intent = new Intent(mContext, RedeemHistory.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                        }
                    }
                }catch(Exception e){}
                break;
            case R.id.rl_myaddresses:
                try{
                if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".AddressDetail")) {
                    if (userId != null && userId.trim().length() > 0) {

                         /*QGraph event*/
                        JSONObject json=new JSONObject();
                        if(MySharedPrefs.INSTANCE.getUserId()!=null)
                            json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                        UtilityMethods.setQGraphevent("Android Profile Activity - View Address",json);
                                /*--------------*/

                        showDialog();
                        String url = UrlsConstants.ADDRESS_BOOK + userId;
                        myApi.reqAddressBook(url, MyReceiverActions.ADDRESS_BOOK);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                    }
                }
                }catch(Exception e){}
                break;

//            case R.id.rl_viewprofile:
//                if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".UserProfile")) {
//                    if (userId != null && userId.trim().length() > 0) {
//                        showDialog();
//                        String url = UrlsConstants.USER_DETAIL_URL + userId;
//                        myApi.reqUserDetails(url);
//                    } else {
//                        Intent intent = new Intent(mContext, LoginActivity.class);
//                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
//                    }
//                }
//                break;
            case R.id.rl_editprofile:
                try{
                if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".EditProfile")) {
                    if (userId != null && userId.trim().length() > 0) {
                        Intent intent = new Intent(mContext, EditProfile.class);
                        startActivity(intent);
//                        startActivityForResult(intent, 1001);
//                        finish();                                                     //added 25_9
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                    }
                }
                }catch(Exception e){}
                break;
            case R.id.rl_invitefriends:
                try{

                    InviteReferralsApi.getInstance(UserHeaderProfile.this).inline_btn(Integer.parseInt(MySharedPrefs.INSTANCE.getInviteReferralId()));

                     /*QGraph event*/
                    JSONObject json=new JSONObject();
                    if(MySharedPrefs.INSTANCE.getUserId()!=null)
                        json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                    UtilityMethods.setQGraphevent("Android Profile Activity - Share",json);
                                /*--------------*/

                //UtilityMethods.shareApp(mContext);
                }catch(Exception e){}
                break;
            case R.id.rl_callus:
                try{
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(AppConstants.customer_care));
                startActivity(callIntent);
                }catch(Exception e){}
                break;
            case R.id.rl_writetous:
                try{
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(
                            getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (pInfo != null) {
                    String subject = AppConstants.subject;
                    shareToGMail(AppConstants.email, subject);
                }
                }catch(Exception e){}
                break;
            case R.id.rl_signout:
                UtilityMethods.logout(UserHeaderProfile.this);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 555){
            if(resultCode==RESULT_OK)
                finish();
            }
//        else if(requestCode == 1001){
//            if(resultCode==RESULT_OK){
//
//            }
//        }
        else {
//        if (resultCode == LOGIN_SIGNUP) {

            Intent intent = new Intent(mContext, HomeScreen.class);
            startActivity(intent);
//            Intent intent = new Intent(this, HomeScreen.class);
//            startActivity(intent);
        }
//        }

    }

    @Override
    public void OnResponse(Bundle bundle) {

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            initHeader(findViewById(R.id.app_bar_header), false, "My Profile");
            ((ImageView)findViewById(R.id.icon_header_cart)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.nom_producte)).setVisibility(View.INVISIBLE);
        }catch(Exception e){
            new GrocermaxBaseException("CreateNewAddress","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
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
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
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





}
