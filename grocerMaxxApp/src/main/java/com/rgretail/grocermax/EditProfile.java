

package com.rgretail.grocermax;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.bean.UserDetailBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

//import com.google.analytics.tracking.android.EasyTracker;
//import android.widget.Toast;

public class EditProfile extends BaseActivity{
	
	EditText old_pwd,new_pwd,conf_pwd;
	EditText fname,lname,contact;
	ImageView ivCBChangePwd;
	TextView tvCBChangePwd;
	private boolean bChangePwd = false;
	CheckBox chb;
	LinearLayout ll_pwd;
	TextView change;
//	EasyTracker tracker;
	private TextView tvHeader;
	
//	private TextView leftLineFirstName,middleLineFirstName,rightLineFirstName,
//				     leftLineLastName,middleLineLastName,rightLineLastName,
//				     leftLineMobileName,middleLineMobileName,rightLineMobileName,
//				     leftLineoldPwd,middleLineOldPwd,rightLineOldPwd,
//				     leftLineNewPwd,middleLineNewPwd,rightLineNewPwd,
//				     leftLineConfirmPwd,middleLineConfirmPwd,rightLineConfirmPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

		try{
			AppsFlyerLib.setCurrencyCode("INR");
			AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
			AppsFlyerLib.sendTracking(getApplicationContext());
		}catch(Exception e){}

        try {
			addActionsInFilter(MyReceiverActions.EDIT_PROFILE);
			addActionsInFilter(MyReceiverActions.USER_DETAILS1);

			old_pwd = (EditText) findViewById(R.id.old_password);
			new_pwd = (EditText) findViewById(R.id.new_password);
			conf_pwd = (EditText) findViewById(R.id.conf_password);

			fname = (EditText) findViewById(R.id.fname);
			lname = (EditText) findViewById(R.id.lname);
			contact = (EditText) findViewById(R.id.contact);
			chb = (CheckBox) findViewById(R.id.chbx);
			ll_pwd = (LinearLayout) findViewById(R.id.ll_pwd);

			fname.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			lname.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			contact.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			old_pwd.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			new_pwd.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			conf_pwd.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			tvHeader = (TextView) findViewById(R.id.msg);
			tvHeader.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			ivCBChangePwd = (ImageView) findViewById(R.id.cb_iv_change_pwd);
			tvCBChangePwd = (TextView) findViewById(R.id.cb_tv_change_pwd);

//			leftLineFirstName = (TextView) findViewById(R.id.left_line_first_name);
//			middleLineFirstName = (TextView) findViewById(R.id.middle_line_first_name);
//			rightLineFirstName = (TextView) findViewById(R.id.right_line_first_name);

			//leftLineLastName = (TextView) findViewById(R.id.left_line_last_name);
			//middleLineLastName = (TextView) findViewById(R.id.middle_line_last_name);
			//rightLineLastName = (TextView) findViewById(R.id.right_line_last_name);

			//leftLineMobileName = (TextView) findViewById(R.id.left_line_mobile_name);
			//middleLineMobileName = (TextView) findViewById(R.id.middle_line_mobile_name);
			//rightLineMobileName = (TextView) findViewById(R.id.right_line_mobile_name);

//			leftLineoldPwd = (TextView) findViewById(R.id.left_line_old_pwd);
//			middleLineOldPwd = (TextView) findViewById(R.id.middle_line_old_pwd);
//			rightLineOldPwd = (TextView) findViewById(R.id.right_line_old_pwd);

//			leftLineNewPwd = (TextView) findViewById(R.id.left_line_new_pwd);
//			middleLineNewPwd = (TextView) findViewById(R.id.middle_line_new_pwd);
//			rightLineNewPwd = (TextView) findViewById(R.id.right_line_new_pwd);

//			leftLineConfirmPwd = (TextView) findViewById(R.id.left_line_confirm_pwd);
//			middleLineConfirmPwd = (TextView) findViewById(R.id.middle_line_confirm_pwd);
//			rightLineConfirmPwd = (TextView) findViewById(R.id.right_line_confirm_pwd);


//			fname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineoldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			lname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						leftLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineoldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						middleLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						rightLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						leftLineoldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			old_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineoldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						middleLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						rightLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						leftLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			new_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineoldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						middleLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						rightLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						leftLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			conf_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineMobileName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineoldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineOldPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						middleLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						rightLineNewPwd.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						leftLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						middleLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						rightLineConfirmPwd.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//					}
//				}
//			});


			ivCBChangePwd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(EditProfile.this);
					if (bChangePwd) {
						bChangePwd = false;
					} else {
						bChangePwd = true;
					}

					if (bChangePwd) {
						ll_pwd.setVisibility(View.VISIBLE);
						ivCBChangePwd.setImageResource(R.drawable.chkbox_selected);
					} else {
						ll_pwd.setVisibility(View.GONE);
						ivCBChangePwd.setImageResource(R.drawable.chkbox_unselected);
					}

				}
			});

			tvCBChangePwd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(EditProfile.this);
					if (bChangePwd) {
						bChangePwd = false;
					} else {
						bChangePwd = true;
					}

					if (bChangePwd) {
						ll_pwd.setVisibility(View.VISIBLE);
						ivCBChangePwd.setImageResource(R.drawable.chkbox_selected);
					} else {
						ll_pwd.setVisibility(View.GONE);
						ivCBChangePwd.setImageResource(R.drawable.chkbox_unselected);
					}

				}
			});

			chb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked)
						ll_pwd.setVisibility(View.VISIBLE);
					else
						ll_pwd.setVisibility(View.GONE);
				}
			});

			change = (TextView) findViewById(R.id.change);
			change.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			change.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String old_p = old_pwd.getText().toString().trim();
					String new_p = new_pwd.getText().toString().trim();
					String conf_p = conf_pwd.getText().toString().trim();
					String fname_n = fname.getText().toString().trim();
					String lname_n = lname.getText().toString().trim();
					String contact_n = contact.getText().toString().trim();

					if (fname_n.equals("")) {
						UtilityMethods.customToast(AppConstants.ToastConstant.FNAME_BLANCK, EditProfile.this);
						return;
					}
					if (lname_n.equals("")) {
						UtilityMethods.customToast(AppConstants.ToastConstant.LNAME_BLANCK, EditProfile.this);
						return;
					}
					if (contact_n.equals("")) {
						UtilityMethods.customToast(AppConstants.ToastConstant.MOB_BLANCK, EditProfile.this);
						return;
					}
					if (contact_n.length() < 10 || contact_n.length() > 10) {
						UtilityMethods.customToast(AppConstants.ToastConstant.MOB_NUMBER_DIGIT, EditProfile.this);
						return;
					}

//				if(chb.isChecked())
					if (bChangePwd) {
						if (old_p.equals("")) {
							UtilityMethods.customToast(AppConstants.ToastConstant.OPWD_BLANK, EditProfile.this);
							return;
						}
						if (new_p.equals(" ")) {
							UtilityMethods.customToast(AppConstants.ToastConstant.NPWD_BLANK, EditProfile.this);
							return;
						}
						if (new_p.length() < 6) {
							UtilityMethods.customToast(AppConstants.ToastConstant.PWD_NUMBER_DIGIT, EditProfile.this);
							return;
						}
						if (conf_p.equalsIgnoreCase("")) {
							UtilityMethods.customToast(AppConstants.ToastConstant.CPWD_BLANK, EditProfile.this);
							return;
						}
						if (!new_p.equals(conf_p)) {
							UtilityMethods.customToast(AppConstants.ToastConstant.PWD_NOT_MATCH, EditProfile.this);
							return;
						}
					}
					if (UtilityMethods.isInternetAvailable(mContext)) {
						showDialog();
						String url = UrlsConstants.EDIT_PROFILE_URL;

						//String params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password;
						String params;
						JSONObject jsonObject = new JSONObject();
//					if(chb.isChecked())
						try {
							if (bChangePwd) {
//								params = "userid=" + MySharedPrefs.INSTANCE.getUserId() + "uemail=" + MySharedPrefs.INSTANCE.getUserEmail() + "&password=" + new_p +
//										"&old_password=" + old_p + "&fname=" + fname_n + "&lname=" + lname_n + "&number=" + contact_n;

								jsonObject.put("userid", MySharedPrefs.INSTANCE.getUserId());
								jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
								jsonObject.put("password",new_p);
								jsonObject.put("old_password",old_p);
								jsonObject.put("fname",fname_n);
								jsonObject.put("lname",lname_n);
								jsonObject.put("number",contact_n);
//								jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);

                                /*tracking GA event for edit profile and change password both*/
                                try{
                                    UtilityMethods.clickCapture(activity,"Profile Activity","","Edit Information","",MySharedPrefs.INSTANCE.getSelectedCity());
                                    UtilityMethods.clickCapture(activity,"Profile Activity","","Change Password","",MySharedPrefs.INSTANCE.getSelectedCity());
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                /*--------------------------------*/

							} else {
//								params = "userid=" + MySharedPrefs.INSTANCE.getUserId() + "uemail=" + MySharedPrefs.INSTANCE.getUserEmail() +
//										"&fname=" + fname_n + "&lname=" + lname_n + "&number=" + contact_n;
								jsonObject.put("userid", MySharedPrefs.INSTANCE.getUserId());
								jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
								jsonObject.put("fname",fname_n);
								jsonObject.put("lname",lname_n);
								jsonObject.put("number",contact_n);
//								jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);

                                /*tracking GA event only for Edit information*/
                                try{
                                    UtilityMethods.clickCapture(activity,"Profile Activity","","Edit Information","",MySharedPrefs.INSTANCE.getSelectedCity());
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                /*--------------------------------*/


							}
//							url += params;
//							myApi.reqEditProfile(url);
							myApi.reqEditProfile(url,jsonObject);
						}catch(Exception e){}

					} else {
						UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, mContext);
					}
				}
			});
			initHeader(findViewById(R.id.header), true, "Edit Profile");
			showDialog();
			String url = UrlsConstants.USER_DETAIL_URL + MySharedPrefs.INSTANCE.getUserId();
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put();

			myApi.reqUserDetails1(url);
		}catch(Exception e){
			new GrocermaxBaseException("EditProfile","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	
	
	@Override
	public void OnResponse(Bundle bundle) {
		try {
			if (bundle.getString("ACTION").equals(MyReceiverActions.EDIT_PROFILE)) {
				BaseResponseBean userDataBean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (userDataBean.getFlag().equalsIgnoreCase("1")) {
					UtilityMethods.customToast(AppConstants.ToastConstant.PROFILE_UPDATED, mContext);
					if (UserProfile.textName != null) {
						UserProfile.textName.setText(fname.getText() + " " + lname.getText());
					}
					if (UserProfile.textPhoneNo != null) {
						UserProfile.textPhoneNo.setText(contact.getText());
					}

//					UserHeaderProfile.tvUserName = "";
//					MySharedPrefs.INSTANCE.getMobileNo()
					Registration.facebookName = fname.getText() + " " + lname.getText();    //just for displaying update on MyProfile screen.
					if(fname.getText().toString().length() > 0 && fname != null) {
						MySharedPrefs.INSTANCE.putFirstName(fname.getText().toString());
						MySharedPrefs.INSTANCE.putLastName(lname.getText().toString());
					}
					if(contact.getText().toString().length() > 0 && contact != null) {
						MySharedPrefs.INSTANCE.putMobileNo(contact.getText().toString());
					}

					if(UserHeaderProfile.tvUserName != null) {
						UserHeaderProfile.tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName()+" "+MySharedPrefs.INSTANCE.getLastName());
					}
					if(UserHeaderProfile.tvUserEmail != null) {
						UserHeaderProfile.tvUserEmail.setText(MySharedPrefs.INSTANCE.getUserEmail());
					}
					if(UserHeaderProfile.tvUserMobileNo != null) {
						UserHeaderProfile.tvUserMobileNo.setText(MySharedPrefs.INSTANCE.getMobileNo());
					}

//					setResult(RESULT_OK);
					finish();
				} else {
					UtilityMethods.customToast(userDataBean.getResult(), mContext);
				}
			}
			if (bundle.getString("ACTION").equals(MyReceiverActions.USER_DETAILS1)) {
				UserDetailBean userDataBean = (UserDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
				fname.setText(userDataBean.getPersonalInfo().getFirstname());
				lname.setText(userDataBean.getPersonalInfo().getLastname());
				contact.setText(userDataBean.getPersonalInfo().getMobile());

			}
		}catch(Exception e){
			new GrocermaxBaseException("EditProfile","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}
		try{
			initHeader(findViewById(R.id.header), true, "Edit Profile");
		}catch(Exception e){
			new GrocermaxBaseException("EditProfile","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
	}

	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}
    	try{
//			EasyTracker.getInstance(this).activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
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
//			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
    }
	
}
