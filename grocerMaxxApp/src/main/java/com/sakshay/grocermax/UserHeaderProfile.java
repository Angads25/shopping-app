package com.sakshay.grocermax;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.AddressList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

/**
 * Created by Abhishek on 8/25/2015.
 */
public class UserHeaderProfile extends BaseActivity implements View.OnClickListener{
    TextView tvUserName,tvUserEmail,tvUserMobileNo;
    RelativeLayout rlLogin,rlOrderHistory,rlMyAddresses,rlViewProfile,rlInviteFriends,rlCallToUs,rlWriteToUs,rlSignOut;
    TextView tvLogin,tvOrderHistory,tvMyAddresses,tvViewProfile,tvInviteFriends,tvCallToUs,tvWriteToUs,tvSignOut;
    EasyTracker tracker;
//    tv_login_signup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        View viewSignOut = (View) findViewById(R.id.view_sign_out);
        ImageView ivLoginCarat = (ImageView) findViewById(R.id.iv_login_carrat);

        tvUserName = (TextView) findViewById(R.id.tv_userprofile_fullname);
        tvUserEmail = (TextView) findViewById(R.id.tv_userprofile_email);
        tvUserMobileNo = (TextView) findViewById(R.id.tv_userprofile_phoneno);

        rlLogin = (RelativeLayout) findViewById(R.id.rl_login_signup);
        rlOrderHistory = (RelativeLayout) findViewById(R.id.rl_orderhistory);
        rlMyAddresses = (RelativeLayout) findViewById(R.id.rl_myaddresses);
        rlViewProfile = (RelativeLayout) findViewById(R.id.rl_viewprofile);
        rlInviteFriends = (RelativeLayout) findViewById(R.id.rl_invitefriends);
        rlCallToUs = (RelativeLayout) findViewById(R.id.rl_callus);
        rlWriteToUs = (RelativeLayout) findViewById(R.id.rl_writetous);
        rlSignOut = (RelativeLayout) findViewById(R.id.rl_signout);

        tvLogin = (TextView) findViewById(R.id.tv_login_signup);
        tvOrderHistory = (TextView) findViewById(R.id.tv_orderhistory);
        tvMyAddresses = (TextView) findViewById(R.id.tv_myaddresses);
        tvViewProfile = (TextView) findViewById(R.id.tv_viewprofile);
        tvInviteFriends = (TextView) findViewById(R.id.tv_invitefriends);
        tvCallToUs = (TextView) findViewById(R.id.tv_callus);
        tvWriteToUs = (TextView) findViewById(R.id.tv_writetous);
        tvSignOut = (TextView) findViewById(R.id.tv_signout);

        rlLogin.setOnClickListener(this);
        rlOrderHistory.setOnClickListener(this);
        rlMyAddresses.setOnClickListener(this);
        rlViewProfile.setOnClickListener(this);
        rlInviteFriends.setOnClickListener(this);
        rlCallToUs.setOnClickListener(this);
        rlWriteToUs.setOnClickListener(this);
        rlSignOut.setOnClickListener(this);

        if (MySharedPrefs.INSTANCE.getLoginStatus()) {
            tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName()+" "+MySharedPrefs.INSTANCE.getLastName());
            tvUserEmail.setText(MySharedPrefs.INSTANCE.getUserEmail());
            tvUserMobileNo.setText(MySharedPrefs.INSTANCE.getMobileNo());
//            tvUserName.setTextColor(Color.WHITE);
            tvLogin.setText(MySharedPrefs.INSTANCE.getUserEmail());
            tvLogin.setTextColor(Color.WHITE);
            rlLogin.setBackgroundColor(getResources().getColor(
                    R.color.app_header));
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
        initFooter(findViewById(R.id.footer), 4, 3);
    }

    @Override
    public void onClick(View v) {
        String userId = MySharedPrefs.INSTANCE.getUserId();
        switch (v.getId()) {
            case R.id.rl_login_signup:
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
                break;
            case R.id.rl_orderhistory:
                if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".OrderHistory")) {
                    if (userId != null && userId.trim().length() > 0) {
                        openOrderHistory();
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                    }
                }
                break;
            case R.id.rl_myaddresses:
                if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".AddressDetail")) {
                    if (userId != null && userId.trim().length() > 0) {
                        showDialog();
                        String url = UrlsConstants.ADDRESS_BOOK + userId;
                        myApi.reqAddressBook(url, MyReceiverActions.ADDRESS_BOOK);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                    }
                }
                break;
            case R.id.rl_viewprofile:
                if (!UtilityMethods.getCurrentClassName(UserHeaderProfile.this).equals(getApplicationContext().getPackageName() + ".UserProfile")) {
                    if (userId != null && userId.trim().length() > 0) {
                        showDialog();
                        String url = UrlsConstants.USER_DETAIL_URL + userId;
                        myApi.reqUserDetails(url);
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
                    }
                }
                break;
            case R.id.rl_invitefriends:
                UtilityMethods.shareApp(mContext);
                break;
            case R.id.rl_callus:
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(AppConstants.customer_care));
                startActivity(callIntent);

                break;
            case R.id.rl_writetous:
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
                break;
            case R.id.rl_signout:
                MySharedPrefs.INSTANCE.clearUserInfo();
                MySharedPrefs.INSTANCE.putTotalItem("0");
                cart_count_txt.setText("0");
                BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon_logout);
                UtilityMethods.deleteCloneCart(UserHeaderProfile.this);

                ////Fb logout/////////
                if (MySharedPrefs.INSTANCE.getFacebookId() != null) {
                    Session session = LoginActivity.getInstance().getSession();
                    if (!session.isClosed()) {
                        MySharedPrefs.INSTANCE.clearAllData();
                        session.closeAndClearTokenInformation();
                    }
                }
                if (MySharedPrefs.INSTANCE.getGoogleId() != null) {
//					LoginActivity loginActivity = new LoginActivity();
//					loginActivity.googlePlusLogoutLocally();
                    LoginActivity.googlePlusLogout();
                    Registration.googlePlusLogoutReg();
//					loginActivity.googlePlusLogout();
                    MySharedPrefs.INSTANCE.clearAllData();
                }
                UtilityMethods.customToast(AppConstants.ToastConstant.LOGOUT_SUCCESS, mContext);
                Intent intent = new Intent(mContext, HomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    void OnResponse(Bundle bundle) {

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            initHeader(findViewById(R.id.app_bar_header), true, "My Profile");
        }catch(Exception e){
            new GrocermaxBaseException("CreateNewAddress","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            tracker.activityStart(this);
            FlurryAgent.onStartSession(this, getResources().getString(R.string.flurry_api_key));
            FlurryAgent.onPageView();         //Use onPageView to report page view count.
        }catch(Exception e){
            new GrocermaxBaseException("CreateNewAddress","onStart",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try{
            tracker.activityStop(this);
            FlurryAgent.onEndSession(this);
        }catch(Exception e){
            new GrocermaxBaseException("CreateNewAddress","onStop",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }





}
