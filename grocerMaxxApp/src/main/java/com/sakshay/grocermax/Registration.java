package com.sakshay.grocermax;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.CartDetailBean;
import com.sakshay.grocermax.bean.LoginResponse;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

/**
 * @author Pratyesh Singh
 */

public class Registration extends BaseActivity implements OnClickListener {
	String SCREEN_NAME = "Registration";
//	ForgotPasswordTask forgotPasswordTask = null;
//	RegistrationTask registrationTask = null;
	boolean clickMale = false;
	boolean clickFemale = false;
	boolean clickOther = false;
	String USER_EMAIL = "";
	String QUOTE_ID_AFTER_FB = "";
	ImageView ivFacebook;
	TextView tvFacebook;
	private static final int RC_SIGN_IN = 0;          //
	private static final int FB_SIGN_IN = 64206;
	EasyTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
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

			addActionsInFilter(MyReceiverActions.LOGIN);
			addActionsInFilter(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN);
			//TODO abhi, now that lots of views are gone please rewire registration screen
			displayRegistrationView();
		}
		}catch(Exception e){
			new GrocermaxBaseException("Registeration","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
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

					String url = UrlsConstants.FORGET_PASSWORD_URL + _email_id;
					myApi.reqForgetPassword(url);
				} else {
					UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
				}
			}
		});

	}

	private void displayRegistrationView() {
		try {
			final EditText fName = (EditText) findViewById(R.id.et_register_first_name);
			final EditText lName = (EditText) findViewById(R.id.et_register_last_name);
			final EditText email_id = (EditText) findViewById(R.id.et_register_email);
			final EditText password = (EditText) findViewById(R.id.et_register_password);
			final EditText confirmation = (EditText) findViewById(R.id.et_register_confirm_pwd);
			final EditText mobile_no = (EditText) findViewById(R.id.et_register_mobileno);

			final ImageView iv_male = (ImageView) findViewById(R.id.img_male);
			final ImageView iv_female = (ImageView) findViewById(R.id.img_female);
			final ImageView iv_other = (ImageView) findViewById(R.id.img_other);

			final CheckBox cbMale = (CheckBox) findViewById(R.id.cb_male);
			final CheckBox cbFemale = (CheckBox) findViewById(R.id.cb_female);
			final CheckBox cbOther = (CheckBox) findViewById(R.id.cb_other);

			ivFacebook = (ImageView)findViewById(R.id.facebook_icon);
			tvFacebook = (TextView)findViewById(R.id.button_facebook);
			ImageView ivGoogle = (ImageView)findViewById(R.id.google_plus_left_icon);
			TextView tvGoogle = (TextView)findViewById(R.id.button_google);

			tvFacebook.setOnClickListener(fb_signin_listener);
			ivFacebook.setOnClickListener(fb_signin_listener);

//			tvFacebook.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					UtilityMethods.customToast("FB tv",mContext);
//				}
//			});
//			ivFacebook.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					UtilityMethods.customToast("FB iv",mContext);
//				}
//			});
			tvGoogle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UtilityMethods.customToast("Google Plus tv",mContext);
				}
			});
			ivGoogle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UtilityMethods.customToast("Google Plus iv",mContext);
				}
			});

//			ivFB.setClickable(this);
//			tvFB.setClickable(this);
//			ivGoogle.setClickable(true);
//			tvGoogle.setClickable(true);
//			rlbtnGoogle.setClickable(true);
//			rlbtnFB.setClickable(true);

			cbMale.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (cbMale.isChecked()) {
						cbFemale.setChecked(false);
						cbOther.setChecked(false);
					} else {
						cbMale.setChecked(false);
					}
				}
			});

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
					if (clickMale) {
						clickMale = false;
					} else {
						clickMale = true;
					}

					if (clickMale) {
						iv_male.setImageResource(R.drawable.checkbox_select);           //select
						iv_female.setImageResource(R.drawable.checkbox_unselect);       //unselect
						iv_other.setImageResource(R.drawable.checkbox_unselect);        //unselect
					} else {
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
					if (clickFemale) {
						clickFemale = false;
					} else {
						clickFemale = true;
					}

					if (clickFemale) {
						iv_female.setImageResource(R.drawable.checkbox_select);           //select
						iv_male.setImageResource(R.drawable.checkbox_unselect);       //unselect
						iv_other.setImageResource(R.drawable.checkbox_unselect);        //unselect
					} else {
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
					if (clickOther) {
						clickOther = false;
					} else {
						clickOther = true;
					}

					if (clickOther) {
						iv_other.setImageResource(R.drawable.checkbox_select);           //select
						iv_male.setImageResource(R.drawable.checkbox_unselect);       //unselect
						iv_female.setImageResource(R.drawable.checkbox_unselect);        //unselect
					} else {
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

					if (_mobile_no.equals("")) {
						UtilityMethods.customToast(ToastConstant.MOB_BLANCK, Registration.this);
						return;
					}

					if (_mobile_no.length() < 10 || _mobile_no.length() > 10) {
//					Toast.makeText(Registration.this, ToastConstant.MOB_NUMBER_DIGIT, Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(ToastConstant.MOB_NUMBER_DIGIT, Registration.this);
						return;
					}

//				if(gender.getSelectedItem().equals("Select")){
//					Toast.makeText(Registration.this, ToastConstant.SELECT_GENDER, Toast.LENGTH_LONG).show();
//					return;
//				}
					if (!clickMale && !clickFemale && !clickOther) {
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
						if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals(""))
							params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password + "&quote_id=no";
						else
							params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
						url += params;
						myApi.reqUserRegistration(url);
					

/*HashMap<String, String> map = new HashMap<String, String>();
					
					map.put("fname", _fname);
					map.put("lname", _lname);
					map.put("uemail", _email_id);
					map.put("number", _mobile_no);
					map.put("password", _password);*/


					} else {
//					Toast.makeText(mContext, ToastConstant.msgNoInternet ,Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
					}
				}
			});
		}catch(NullPointerException e){
			new GrocermaxBaseException("Registeration", "displayRegistrationView", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("Registeration", "displayRegistrationView", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	void OnResponse(Bundle bundle) {
		try{
			if (bundle.getString("ACTION").equals(MyReceiverActions.LOGIN)) {
				LoginResponse userDataBean = (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);

				if (userDataBean.getFlag().equalsIgnoreCase("1")) {          //successful login
					MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
					MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
					MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
					MySharedPrefs.INSTANCE.putMobileNo(userDataBean.getMobile());

					BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon);  //login icon
//					if (USER_EMAIL.equals("")) {
//						MySharedPrefs.INSTANCE.putUserEmail(username.getText().toString());
//						MySharedPrefs.INSTANCE.putQuoteId(userDataBean.getQuoteId());
//					} else {
					MySharedPrefs.INSTANCE.putUserEmail(USER_EMAIL);
					MySharedPrefs.INSTANCE.putQuoteId(QUOTE_ID_AFTER_FB);
//					}

					MySharedPrefs.INSTANCE.putLoginStatus(true);
					MySharedPrefs.INSTANCE.putQuoteId(userDataBean.getQuoteId());/////////last change
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(userDataBean.getTotalItem()));
					ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(Registration.this, AppConstants.localCartFile);  //send to server if local cart has products for adding.
					if (cart_products != null && cart_products.size() > 0)            //it will call when user has come from HOME | PRODUCT LISTING | DESCRIPTION only.
					{
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
					} else          //SIMPLE LOGIN AND FACEBOOK CASE : user can come here by [direct login] OR [from product listing] and [home screen] OR [from view cart].
					{             //quote id will not return from server in SIMPLE LOGIN case BUT quote id will return in case of FACEBOOK login.
						//if calling from CartProductList then in BaseActivity startActivityForResult will call other wise simply finish will work .
						int str = userDataBean.getTotalItem();
						cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
//					if(userDataBean.getTotalItem() > 0){            //means user has >= than 1 product
//						showDialog();
//						if(MySharedPrefs.INSTANCE.getQuoteId() != null && !MySharedPrefs.INSTANCE.getQuoteId().equals("")){    //not call in simple login but call in facebook as i am getting quote id in fb case
//							String url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
////							myApi.reqViewCart(url);
//							myApi.reqViewCartGoHomeScreen(url);
//						}else if(userDataBean.getQuoteId() != null && !userDataBean.getQuoteId().equals("")){   //not call in simple login but call in facebook as i am getting quote id in fb case
//							String url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId()+"&quote_id="+userDataBean.getQuoteId();
////							myApi.reqViewCart(url);
//							myApi.reqViewCartGoHomeScreen(url);
//						}
//					}else{                                   //if user has no total item that mean he will redirect to home screen or product listing or product description screen.
						setResult(RESULT_OK);
						finish();
//					}
					}

				} else {
					UtilityMethods.customToast(ToastConstant.LOGIN_FAIL, mContext);
				}
			}
		else if (bundle.getString("ACTION").equals(MyReceiverActions.REGISTER_USER)) {
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
//		else if (bundle.getString("ACTION").equals(MyReceiverActions.ADD_TO_CART))  //it will call when user has come from HOME | PRODUCT LISTING | DESCRIPTION only.
//			{
//				BaseResponseBean response = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
//				if (response.getFlag().equals("1")) {
////				String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId();
////				myApi.reqViewCart(url);  MySharedPrefs.INSTANCE.getUserId().equals("")
//					showDialog();
//					if (MySharedPrefs.INSTANCE.getQuoteId() != null && !MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
//						System.out.println("==some value sharedprefs==" + MySharedPrefs.INSTANCE.getQuoteId() + ">>");
//						String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
////					myApi.reqViewCart(url);
//						myApi.reqViewCartGoHomeScreen(url);
//					} else if (response.getQuoteId() != null && !response.getQuoteId().equals("")) {
//						System.out.println("==some value simple==" + response.getQuoteId() + "||||");
//						String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + response.getQuoteId();
////					myApi.reqViewCart(url);
//						myApi.reqViewCartGoHomeScreen(url);
//					} else {
//
//					}
//					UtilityMethods.deleteLocalCart(Registration.this);
////				finish();
//				} else {
//					setResult(RESULT_OK);
//					finish();
//				}
//				MySharedPrefs.INSTANCE.putQuoteId(response.getQuoteId());
//				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(response.getTotalItem()));
//			}

		else if (bundle.getString("ACTION").equals(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN)) {
				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
//			if(cartBean.getFlag().equals("1"))
//			{
				if (cartBean.getItems().size() > 0) {
					UtilityMethods.deleteCloneCart(Registration.this);
					for (int i = 0; i < cartBean.getItems().size(); i++) {
						UtilityMethods.writeCloneCart(Registration.this, Constants.localCloneFile, cartBean.getItems().get(i));
					}
				}
				if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int) Float.parseFloat(cartBean.getItems_qty())));
					cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
				}
//				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(cartBean.getItems_qty()));
//				cart_count_txt.setText(String.valueOf(cartBean.getItems_qty()));
//			}
				setResult(RESULT_OK);
				finish();
			}






















		}catch(Exception e){
			new GrocermaxBaseException("Registeration","OnResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
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
    	}catch(Exception e){
			new GrocermaxBaseException("Registeration","onStart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
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
			new GrocermaxBaseException("Registeration","onStop",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }


	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {

				case R.id.rl_button_facebook:
					UtilityMethods.customToast("Facebook",this);
					break;

				case R.id.facebook_icon:
					UtilityMethods.customToast("FB",this);
					break;

				case R.id.button_google:
					UtilityMethods.customToast("Google",this);
					break;

				case R.id.google_plus_left_icon:
					UtilityMethods.customToast("G+",this);
					break;
			}
		}catch(Exception e){
			new GrocermaxBaseException("Registeration", "onClick", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		64206 requestCode

		switch (requestCode) {
//			case RC_SIGN_IN:
//				try{
//					if (resultCode == RESULT_OK) {
//						signedInUser = false;
//					}
//					mIntentInProgress = false;
//					if (!mGoogleApiClient.isConnecting()) {
//						mGoogleApiClient.connect();
//					}
//				}catch(Exception e){
//					new GrocermaxBaseException("LoginAactivity","OnResponseRcSignIn",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//				}
//
//				break;

			case FB_SIGN_IN:
				try{
					Session.getActiveSession().onActivityResult(Registration.this, requestCode,resultCode, data);
				}catch(Exception e){
					new GrocermaxBaseException("Registeration","onActivityResult",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
				}
		}
		if(requestCode==111)
		{
			if(resultCode==RESULT_OK)
				finish();
		}

//		if(requestCode == 64206){
//			Session.getActiveSession().onActivityResult(LoginActivity.this, requestCode,resultCode, data);
//		}else{
//
//		}

		super.onActivityResult(requestCode, resultCode, data);

	}


	/**
	 * facebook sign in listener
	 * */
	OnClickListener fb_signin_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{
				if (UtilityMethods.isInternetAvailable(mContext)) {
					facebookLoginWithEmailPermission();
				} else
					UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
			}catch(Exception e){
				new GrocermaxBaseException("Registeration","fb_signin_listener",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
		}
	};

	private void facebookLoginWithEmailPermission() {
		try {

			final List<String> newPermissionsRequest = new ArrayList<String>(Arrays.asList("email", "user_location"));
			openActiveSession(Registration.this, true, new Session.StatusCallback() {
				@Override
				public void call(final Session session, SessionState state, Exception exception) {
					if (session.isOpened()) {
						showDialog();
						Request.newMeRequest(session, new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user, Response response) {
								dismissDialog();
								if (user != null) {
									saveUserData(user);
									tvFacebook.setText("Sign out Facebook");
									tvFacebook.setOnClickListener(fb_sign_out_listener);
									ivFacebook.setOnClickListener(fb_sign_out_listener);
									//TODO: Mohit Raheja
									//								Intent intent = new Intent(mContext , HomeActivity.class);
									//								startActivity(intent);
									//								LoginActivity.this.finish();
								}
							}
						}).executeAsync();
					}
				}
			}, newPermissionsRequest);
		}catch(Exception e){
			new GrocermaxBaseException("Registeration","facebookLoginWithEmailPermission",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private static Session openActiveSession(Activity activity, boolean allowLoginUI, Session.StatusCallback callback, List<String> permissions) {
		Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(activity).build();

		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
			Session.setActiveSession(session);
			session.openForRead(openRequest);
			return session;
		}
		return null;
	}

	/**
	 * for saving facebook data
	 * */
	private void saveUserData(GraphUser user) {
		String USER_ID = "";
		String USER_FNAME = "";
		String USER_MNAME = "";
		String USER_LNAME = "";


		String USER_NAME = "";

		USER_FNAME = user.getFirstName();
		USER_MNAME = user.getMiddleName();
		USER_LNAME = user.getLastName();
		try {
			USER_EMAIL = user.getProperty("email").toString();
		} catch (Exception e) {
			Log.e("ERROR", "Enable to get email");
		}

		USER_ID = user.getId();

		if (USER_FNAME != null && USER_FNAME.length() > 0)
			USER_NAME = USER_FNAME;
		if (USER_MNAME != null && USER_MNAME.length() > 0)
			USER_NAME = USER_NAME + " " + USER_MNAME;
		if (USER_LNAME != null && USER_LNAME.length() > 0)
			USER_NAME = USER_NAME + " " + USER_LNAME;

		if (USER_NAME != null && USER_NAME.length() > 0)
			MySharedPrefs.INSTANCE.putFacebookName(USER_FNAME);

		if (USER_ID != null && USER_ID.length() > 0)
			MySharedPrefs.INSTANCE.putFacebookId(USER_ID);

		if (USER_EMAIL != null && USER_EMAIL.length() > 0)
			MySharedPrefs.INSTANCE.putFacebookEmail(USER_EMAIL);

		MySharedPrefs.INSTANCE.putUserDataSet(true);

		if (UtilityMethods.isInternetAvailable(mContext)) {
			showDialog();
			String url;
			if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
			{
				url = UrlsConstants.FB_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getFacebookEmail() + "&quote_id=no&fname=" + USER_FNAME+"&lname="+USER_LNAME+"&number=0000000000";
			}else{
				QUOTE_ID_AFTER_FB=MySharedPrefs.INSTANCE.getQuoteId();
				url = UrlsConstants.FB_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getFacebookEmail() + "&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&fname=" + USER_FNAME+"&lname="+USER_LNAME+"&number=0000000000";
			}

			myApi.reqLogin(url);

		} else {
			UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
		}
	}

	/**
	 * FB Methods
	 **/

	/**
	 * facebook sign in listener
	 **/
	OnClickListener fb_sign_out_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try{
				MySharedPrefs.INSTANCE.clearAllData();
				Session session = Session.getActiveSession();
				if (!session.isClosed()) {
					session.closeAndClearTokenInformation();
					tvFacebook.setText("Sign in with Facebook");
					tvFacebook.setOnClickListener(fb_signin_listener);
					ivFacebook.setOnClickListener(fb_signin_listener);
				}
			}catch(Exception e){
				new GrocermaxBaseException("Registeration","fb_sign_out_listener",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
		}
	};

	/**
	 * for logging with user id and password
	 * */




}
