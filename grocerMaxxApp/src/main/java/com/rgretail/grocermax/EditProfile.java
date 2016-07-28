

package com.rgretail.grocermax;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import org.json.JSONException;
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
    String otp="";
    JSONObject jsonObject;
    String url = UrlsConstants.EDIT_PROFILE_URL;
    public static EditText edt_otp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);



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
                    editProfile();
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


   public void editProfile(){
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


           String params;
           jsonObject = new JSONObject();
           try {
               if (bChangePwd) {
                   jsonObject.put("userid", MySharedPrefs.INSTANCE.getUserId());
                   jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
                   jsonObject.put("password",new_p);
                   jsonObject.put("old_password",old_p);
                   jsonObject.put("fname",fname_n);
                   jsonObject.put("lname",lname_n);
                   jsonObject.put("number",contact_n);
                   jsonObject.put("otp","0");

                                /*tracking GA event for edit profile and change password both*/
                   try{
                       UtilityMethods.clickCapture(activity,"Profile Activity","","Edit Information","",MySharedPrefs.INSTANCE.getSelectedCity());
                       UtilityMethods.clickCapture(activity, "Profile Activity", "", "Change Password", "", MySharedPrefs.INSTANCE.getSelectedCity());
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                                /*--------------------------------*/

               } else {
                   jsonObject.put("userid", MySharedPrefs.INSTANCE.getUserId());
                   jsonObject.put("uemail",MySharedPrefs.INSTANCE.getUserEmail());
                   jsonObject.put("fname",fname_n);
                   jsonObject.put("lname",lname_n);
                   jsonObject.put("number",contact_n);
                   jsonObject.put("otp","0");

                                /*tracking GA event only for Edit information*/
                   try{
                       UtilityMethods.clickCapture(activity,"Profile Activity","","Edit Information","",MySharedPrefs.INSTANCE.getSelectedCity());
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                                /*--------------------------------*/


               }
               myApi.reqEditProfile(url,jsonObject);
           }catch(Exception e){}

       } else {
           UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, mContext);
       }

   }

    public void acceptOTP(final Context context){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
        Typeface typeface1=Typeface.createFromAsset(context.getAssets(),"Roboto-Light.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        TextView tv_msg=(TextView)dialogView.findViewById(R.id.tv_msg);
        tv_msg.setTypeface(typeface1);
        TextView tv_skip=(TextView)dialogView.findViewById(R.id.tv_skip);
        tv_skip.setTypeface(typeface);
        TextView tv_update=(TextView)dialogView.findViewById(R.id.tv_update);
        tv_update.setTypeface(typeface);
        TextView tv_resendOtp=(TextView)dialogView.findViewById(R.id.tv_resendOtp);
        tv_resendOtp.setTypeface(typeface1);
        tv_resendOtp.setVisibility(View.VISIBLE);

        edt_otp=(EditText)dialogView.findViewById(R.id.edt_otp);
        edt_otp.setTypeface(typeface);
        edt_otp.setVisibility(View.VISIBLE);

        tv_msg.setText("Enter your one time password(OTP)");
        tv_skip.setText("CANCEL");
        tv_update.setText("SUBMIT");

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String user_entered_otp=edt_otp.getText().toString().trim();
                if(user_entered_otp.equals("")){
                  UtilityMethods.customToast("Please enter OTP sent to your mobile number.",EditProfile.this);
                }else if(!user_entered_otp.equals(otp)){
                    UtilityMethods.customToast("Please enter correct OTP.",EditProfile.this);
                }else{
                    try {
                        alert.dismiss();
                        showDialog();
                        jsonObject.put("otp","1");
                        myApi.reqEditProfile(url,jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        new GrocermaxBaseException("EditProfile","editing profile after validating otp",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
                    }
                }

            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        tv_resendOtp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                editProfile();
            }
        });


        alert.show();
    }
    public void recivedSms(String message)
    {
        try
        {
            edt_otp.setText(message);
        }
        catch (Exception e)
        {
            System.out.println("OneTimePassword.recivedSms"+e.getMessage());
            e.printStackTrace();
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
						UserProfile.textName.setText(fname.getText() + " " + lname.getText().toString().replace("Google","").replace("social",""));
					}
					if (UserProfile.textPhoneNo != null) {
						UserProfile.textPhoneNo.setText(contact.getText());
					}
					Registration.facebookName = fname.getText() + " " + lname.getText();    //just for displaying update on MyProfile screen.
					if(fname.getText().toString().length() > 0 && fname != null) {
						MySharedPrefs.INSTANCE.putFirstName(fname.getText().toString());
						MySharedPrefs.INSTANCE.putLastName(lname.getText().toString());
					}
					if(contact.getText().toString().length() > 0 && contact != null) {
						MySharedPrefs.INSTANCE.putMobileNo(contact.getText().toString());
					}

					if(UserHeaderProfile.tvUserName != null) {
						UserHeaderProfile.tvUserName.setText(MySharedPrefs.INSTANCE.getFirstName()+" "+MySharedPrefs.INSTANCE.getLastName().replace("Google","").replace("social",""));
					}
					if(UserHeaderProfile.tvUserEmail != null) {
						UserHeaderProfile.tvUserEmail.setText(MySharedPrefs.INSTANCE.getUserEmail());
					}
					if(UserHeaderProfile.tvUserMobileNo != null) {
						UserHeaderProfile.tvUserMobileNo.setText(MySharedPrefs.INSTANCE.getMobileNo());
					}
					finish();
				}else if(userDataBean.getFlag().equalsIgnoreCase("2")){
                    MySharedPrefs.INSTANCE.putOTPScreenName("EditProfile");
                    otp=userDataBean.getOtp();
                    acceptOTP(EditProfile.this);

                }else {
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
			initHeader(findViewById(R.id.header), true, "Edit Profile");
		}catch(Exception e){
			new GrocermaxBaseException("EditProfile","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();

       /*------------------------------*/
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();

    }
	
}
