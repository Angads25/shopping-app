package com.sakshay.grocermax;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    EasyTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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

        TextView tv = (TextView) findViewById(R.id.tv_login_signup);

        rlLogin.setOnClickListener(this);
        rlOrderHistory.setOnClickListener(this);
        rlMyAddresses.setOnClickListener(this);
        rlViewProfile.setOnClickListener(this);
        rlInviteFriends.setOnClickListener(this);
        rlCallToUs.setOnClickListener(this);
        rlWriteToUs.setOnClickListener(this);
        rlSignOut.setOnClickListener(this);

        if (MySharedPrefs.INSTANCE.getLoginStatus()) {
            String name = MySharedPrefs.INSTANCE.getUserEmail();
            tvUserName.setText(name);
            tvUserName.setTextColor(Color.WHITE);
            rlLogin.setBackgroundColor(getResources().getColor(
                    R.color.app_header));

        } else {
            tvUserName.setText(getString(R.string.Login));
//            tvSignout.setVisibility(View.GONE);
            rlSignOut.setVisibility(View.GONE);
            rlLogin.setBackgroundColor(Color.WHITE);

            tvUserName.setTextAppearance(this, R.style.normal_textsize);

            // my_fav_lay.setVisibility(View.GONE);
        }

        initHeader(findViewById(R.id.app_bar_header), true, "User Profile");
        initFooter(findViewById(R.id.footer), 4, 3);
    }

    @Override
    public void onClick(View v) {
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
//                intent = new Intent(mContext, BrowseActivity.class);
//                startActivity(intent);
                break;
            case R.id.rl_myaddresses:
//                intent = new Intent(mContext, BrowseActivity.class);
//                startActivity(intent);
                break;
            case R.id.rl_viewprofile:
//                intent = new Intent(mContext, BrowseActivity.class);
//                startActivity(intent);
                break;
            case R.id.rl_invitefriends:
//                intent = new Intent(mContext, BrowseActivity.class);
//                startActivity(intent);
                break;
            case R.id.rl_callus:
//                intent = new Intent(mContext, BrowseActivity.class);
//                startActivity(intent);
                break;
            case R.id.rl_writetous:
//                intent = new Intent(mContext, BrowseActivity.class);
//                startActivity(intent);
                break;
            case R.id.rl_signout:
//                intent = new Intent(mContext, BrowseActivity.class);
//                startActivity(intent);
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
            initHeader(findViewById(R.id.app_bar_header), true, "User Profile");
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
