package com.sakshay.grocermax;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.LoginResponse;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

/**
 * @author Pratyesh Singh
 */

public class Registration extends BaseActivity {
	String SCREEN_NAME = "Registration";
//	ForgotPasswordTask forgotPasswordTask = null;
//	RegistrationTask registrationTask = null;
	boolean clickMale = false;
	boolean clickFemale = false;
	boolean clickOther = false;
	
	EasyTracker tracker;
	TextView leftLineFirstName,middleLineFirstName,rightLineFirstName,
			 leftLineLastName,middleLineLastName,rightLineLastName,
			 leftLineMobile,middleLineMobile,rightLineMobile,
			 leftLineEmail,middleLineEmail,rightLineEmail,
			 leftLinePassword,middleLinePassword,rightLinePassword,
			 leftLineConfirmPassword,middleLineConfirmPassword,rightLineConfirmPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SCREEN_NAME = getIntent().getExtras().getString("whichScreen");

		mContext = this;
		
		if (SCREEN_NAME.equals("ForgotPassword")) {
			setContentView(R.layout.forgot_p);
			TextView tvHeaderForgot = (TextView) findViewById(R.id.tv_heading_forgot);
			tvHeaderForgot.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			addActionsInFilter(MyReceiverActions.FORGOT_PWD);
			
			displayForgotPasswordView();

		} else if (SCREEN_NAME.equals("Registration")) {
			setContentView(R.layout.registeration);
			TextView tvHeaderRegister = (TextView) findViewById(R.id.header_register);
			//TODO this ttf is not lookiap
			//tvHeaderRegister.setTypeface(CustomFonts.getInstance().getRobotoBlack(this));
			addActionsInFilter(MyReceiverActions.REGISTER_USER);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART);
			//TODO abhi, now that lots of views are gone please rewire registration screen
			//displayRegistrationView();
		}
		
	}

	private void displayForgotPasswordView() {

		final EditText email_id = (EditText) findViewById(R.id.email_id);

		TextView forgot_password = (TextView) findViewById(R.id.forgot_password);
		forgot_password.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		forgot_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String _email_id = email_id.getText().toString().trim();

				if (_email_id.equalsIgnoreCase("")) {
					UtilityMethods.customToast(ToastConstant.EMAIL_BLANCK, Registration.this);
					return;
				} else {
					// email validity check
					if (!UtilityMethods.isValidEmail(_email_id)) {
						UtilityMethods.customToast(ToastConstant.invalid_email, Registration.this);
						return;
					}
				}
				if (UtilityMethods.isInternetAvailable(mContext)) {
					showDialog();

					String url = UrlsConstants.FORGET_PASSWORD_URL +_email_id;
					myApi.reqForgetPassword(url);
				} else {
					UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
				}
			}
		});

	}
/*

	private void displayRegistrationView() {
		

		final EditText fName = (EditText) findViewById(R.id.first_name);
		final EditText lName = (EditText) findViewById(R.id.last_name);
		final EditText email_id = (EditText) findViewById(R.id.email_id);
		final EditText password = (EditText) findViewById(R.id.password);
		final EditText confirmation = (EditText) findViewById(R.id.confirmation);
		final EditText mobile_no = (EditText) findViewById(R.id.mobile_no);
		
		final ImageView iv_male = (ImageView) findViewById(R.id.img_male);
		final ImageView iv_female = (ImageView) findViewById(R.id.img_female);
		final ImageView iv_other = (ImageView) findViewById(R.id.img_other);
		
		leftLineFirstName = (TextView) findViewById(R.id.left_line_name);
		middleLineFirstName = (TextView) findViewById(R.id.middle_line_name);
		rightLineFirstName = (TextView) findViewById(R.id.right_line_name);
		
		leftLineLastName = (TextView) findViewById(R.id.left_line_last_name);
		middleLineLastName = (TextView) findViewById(R.id.middle_line_last_name);
		rightLineLastName = (TextView) findViewById(R.id.right_line_last_name);
		
		leftLineMobile = (TextView) findViewById(R.id.left_line_mobile);
		middleLineMobile = (TextView) findViewById(R.id.middle_line_mobile);
		rightLineMobile = (TextView) findViewById(R.id.right_line_mobile);
		
		leftLineEmail = (TextView) findViewById(R.id.left_line_mail);
		middleLineEmail = (TextView) findViewById(R.id.middle_line_mail);
		rightLineEmail = (TextView) findViewById(R.id.right_line_mail);
		
		leftLinePassword = (TextView) findViewById(R.id.left_line_password);
		middleLinePassword = (TextView) findViewById(R.id.middle_line_password);
		rightLinePassword = (TextView) findViewById(R.id.right_line_password);
		
		leftLineConfirmPassword = (TextView) findViewById(R.id.left_line_confirmation);
		middleLineConfirmPassword = (TextView) findViewById(R.id.middle_line_confirmation);
		rightLineConfirmPassword = (TextView) findViewById(R.id.right_line_confirmation);
		
		fName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
					middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
					rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
					
					leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
				}
			  }
			});
		
			lName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
					middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
					rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
					
					leftLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					
					leftLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					middleLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					rightLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
				}
			  }
			});

			email_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						middleLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						rightLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						
						leftLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					}
				  }
				});

			password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						middleLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						rightLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						
						leftLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					}
				  }
				});

			confirmation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						middleLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						rightLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
					}
				  }
				});

			mobile_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						leftLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineFirstName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineLastName.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						middleLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						rightLineMobile.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
						
						leftLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineEmail.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLinePassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						
						leftLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						middleLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
						rightLineConfirmPassword.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
					}
				  }
				});
		
//		final CheckBox cbMale = (CheckBox) findViewById(R.id.cb_male);
//		final CheckBox cbFemale = (CheckBox) findViewById(R.id.cb_female);
//		final CheckBox cbOther = (CheckBox) findViewById(R.id.cb_other);
//		
//		cbMale.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(cbMale.isChecked()){
//					cbFemale.setChecked(false);
//					cbOther.setChecked(false);
//				}else{
//					cbMale.setChecked(false);
//				}
//			}
//		});
		
//		final CheckBox cmMale = (CheckBox) findViewById(R.id.cb)
		
//		final Spinner gender = (Spinner) findViewById(R.id.gender);
		
//		String[] values = {"Select", "Male", "Female"};
//		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_spinner_item, values);
//		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		gender.setAdapter(spinnerArrayAdapter);
		
		password.setTransformationMethod(new PasswordTransformationMethod());
		confirmation.setTransformationMethod(new PasswordTransformationMethod());
		
		iv_male.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickFemale = false;
				clickOther = false;
				if(clickMale){
					clickMale = false;
				}else{
					clickMale = true;
				}
				
				if(clickMale){
					iv_male.setImageResource(R.drawable.checkbox_select);           //select
					iv_female.setImageResource(R.drawable.checkbox_unselect);       //unselect
					iv_other.setImageResource(R.drawable.checkbox_unselect);        //unselect
				}else{
					iv_male.setImageResource(R.drawable.checkbox_unselect);         //unselect 
				}
			}
		});
		
		iv_female.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickMale = false;
				clickOther = false;
				if(clickFemale){
					clickFemale = false;
				}else{
					clickFemale = true;
				}
				
				if(clickFemale){
					iv_female.setImageResource(R.drawable.checkbox_select);           //select
					iv_male.setImageResource(R.drawable.checkbox_unselect);       //unselect
					iv_other.setImageResource(R.drawable.checkbox_unselect);        //unselect
				}else{
					iv_female.setImageResource(R.drawable.checkbox_unselect);         //unselect 
				}
			}
		});
		
		iv_other.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickMale = false;
				clickFemale = false;
				if(clickOther){
					clickOther = false;
				}else{
					clickOther = true;
				}
				
				if(clickOther){
					iv_other.setImageResource(R.drawable.checkbox_select);           //select
					iv_male.setImageResource(R.drawable.checkbox_unselect);       //unselect
					iv_female.setImageResource(R.drawable.checkbox_unselect);        //unselect
				}else{
					iv_other.setImageResource(R.drawable.checkbox_unselect);         //unselect 
				}
				
			}
		});
		
		TextView account_create = (TextView) findViewById(R.id.account_create);
		account_create.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		account_create.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String _fname = fName.getText().toString().trim();
				String _lname = lName.getText().toString().trim();
				String _email_id = email_id.getText().toString().trim();
				String _password = password.getText().toString().trim();
				String _confirmation = confirmation.getText().toString().trim();
				String _mobile_no = mobile_no.getText().toString().trim();
//				String _gender = gender.getSelectedItem().toString();
				if (_fname.equals("")) {
//					Toast.makeText(Registration.this,ToastConstant.FNAME_BLANCK, Toast.LENGTH_LONG).show();
					UtilityMethods.customToast(ToastConstant.FNAME_BLANCK, Registration.this);
					return;
				}
				if (_fname.contains(" ")) {
//					Toast.makeText(Registration.this,ToastConstant.FNAME_SINGLE_WORD, Toast.LENGTH_LONG).show();
					UtilityMethods.customToast(ToastConstant.FNAME_SINGLE_WORD, Registration.this);
					return;
				}
				if (_lname.equals("")) {
//					Toast.makeText(Registration.this, ToastConstant.LNAME_BLANCK, Toast.LENGTH_LONG).show();
					UtilityMethods.customToast(ToastConstant.LNAME_BLANCK, Registration.this);
					return;
				}
				if (_lname.contains(" ")) {
//					Toast.makeText(Registration.this, ToastConstant.LNAME_SINGLE_WORD, Toast.LENGTH_LONG).show();
					UtilityMethods.customToast(ToastConstant.LNAME_SINGLE_WORD, Registration.this);
					return;
				}
				
				if(_mobile_no.equals("")){
					UtilityMethods.customToast(ToastConstant.MOB_BLANCK, Registration.this);
					return;
				}
				
				if(_mobile_no.length()<10 || _mobile_no.length()>10){
//					Toast.makeText(Registration.this, ToastConstant.MOB_NUMBER_DIGIT, Toast.LENGTH_LONG).show();
					UtilityMethods.customToast(ToastConstant.MOB_NUMBER_DIGIT, Registration.this);
					return;
				}
				
//				if(gender.getSelectedItem().equals("Select")){
//					Toast.makeText(Registration.this, ToastConstant.SELECT_GENDER, Toast.LENGTH_LONG).show();
//					return;
//				}
				if(!clickMale && !clickFemale && !clickOther){
//					Toast.makeText(Registration.this, ToastConstant.SELECT_GENDER, Toast.LENGTH_LONG).show();
					UtilityMethods.customToast(ToastConstant.SELECT_GENDER, Registration.this);
					return;
				}

				if (_email_id.equalsIgnoreCase("")) {
					UtilityMethods.customToast(ToastConstant.EMAIL_BLANCK, Registration.this);
					return;
				} else {
					// email validity check
					if (!UtilityMethods.isValidEmail(_email_id)) {
						UtilityMethods.customToast(ToastConstant.invalid_email, Registration.this);
						return;
					}
				}

				if (_password.equalsIgnoreCase("")) {
					UtilityMethods.customToast(ToastConstant.PWD_BLANK, Registration.this);
					return;
				}
				
				if (_password.length() < 6) {
					UtilityMethods.customToast(ToastConstant.PWD_NUMBER_DIGIT, Registration.this);
					return;
				}

				if (_confirmation.equalsIgnoreCase("")) {
					UtilityMethods.customToast(ToastConstant.CPWD_BLANK, Registration.this);
					return;
				}

				if (!_password.equals(_confirmation)) {
					UtilityMethods.customToast(ToastConstant.PWD_NOT_MATCH, Registration.this);
					return;
				}
				if (UtilityMethods.isInternetAvailable(mContext)) {
					showDialog();
					String url = UrlsConstants.REGESTRATION_URL;
					
					//String params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password;
					String params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password;
					if(MySharedPrefs.INSTANCE.getQuoteId()==null || MySharedPrefs.INSTANCE.getQuoteId().equals(""))
					params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password+"&quote_id=no";
					else
					params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
					url += params;
					myApi.reqUserRegistration(url);
					
					*/
/*HashMap<String, String> map = new HashMap<String, String>();
					
					map.put("fname", _fname);
					map.put("lname", _lname);
					map.put("uemail", _email_id);
					map.put("number", _mobile_no);
					map.put("password", _password);*//*

					
				} else {
//					Toast.makeText(mContext, ToastConstant.msgNoInternet ,Toast.LENGTH_LONG).show();
					UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
				}
			}
		});

	}
*/

	@Override
	void OnResponse(Bundle bundle) {
		if (bundle.getString("ACTION").equals(MyReceiverActions.REGISTER_USER)) {
			LoginResponse userDataBean= (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);
			if (userDataBean.getFlag().equalsIgnoreCase("1")) {
				UtilityMethods.customToast(ToastConstant.REGISTER_SUCCESSFULL, mContext);
				//finish();
				MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
				if(MySharedPrefs.INSTANCE.getFacebookEmail()==null)
				MySharedPrefs.INSTANCE.putUserEmail(((EditText)findViewById(R.id.email_id)).getText().toString().trim());
				else
				MySharedPrefs.INSTANCE.putUserEmail(MySharedPrefs.INSTANCE.getFacebookEmail());	
				MySharedPrefs.INSTANCE.putLoginStatus(true);
				
				
				ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(Registration.this, AppConstants.localCartFile);
				if(cart_products != null && cart_products.size() >= 0)
				{
					try
					{
						JSONArray products = new JSONArray();
						for(int i=0; i<cart_products.size(); i++)
						{
							JSONObject prod_obj = new JSONObject();
							prod_obj.put("productid", cart_products.get(i).getItem_id());
							prod_obj.put("quantity",cart_products.get(i).getQty());
							products.put(prod_obj);
						}
						showDialog();
						String url;
						if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
						{
							System.out.println("without quote json="+products.toString());
							url = UrlsConstants.ADD_TO_CART_URL
									+ userDataBean.getUserID() +"&products="
									+ URLEncoder.encode(products.toString(), "UTF-8");
						}
						else
						{
							System.out.println("with quote json="+products.toString());
							url = UrlsConstants.ADD_TO_CART_URL
									+ userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
									+ URLEncoder.encode(products.toString(), "UTF-8");
						}
						//String url = UrlsConstants.ADD_TO_CART_URL + userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+ "&products="+ URLEncoder.encode(products.toString(), "UTF-8");
						myApi.reqAddToCart(url);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					setResult(RESULT_OK);
					finish();
				}
			}
			else{
				UtilityMethods.customToast( userDataBean.getResult(), mContext);
			}
		}else if (bundle.getString("ACTION").equals(MyReceiverActions.FORGOT_PWD)) {
			BaseResponseBean userDataBean= (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
			if (userDataBean.getFlag().equalsIgnoreCase("1")) {
				UtilityMethods.customToast(userDataBean.getResult(), mContext);
				finish();
			}
			else{
				UtilityMethods.customToast(userDataBean.getResult(), mContext);
			}
		}else if(bundle.getString("ACTION").equals(MyReceiverActions.ADD_TO_CART))
		{
			BaseResponseBean response = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
			MySharedPrefs.INSTANCE.putQuoteId(response.getQuoteId());
			MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(response.getTotalItem()));
			if(response.getFlag().equals("1"))
			{
				/*String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId();
				myApi.reqViewCart(url);*/
				UtilityMethods.deleteLocalCart(Registration.this);
				setResult(RESULT_OK);
				finish();
			}
			else
			{
				setResult(RESULT_OK);
				finish();
			}
			
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	try{
	    	tracker.activityStart(this);
	    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
	    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){}
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	try{
	    	tracker.activityStop(this);
	    	FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
    }
	
	
	
	
	
}
