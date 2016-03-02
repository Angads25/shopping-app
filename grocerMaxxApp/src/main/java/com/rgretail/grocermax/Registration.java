package com.rgretail.grocermax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.CartDetailBean;
import com.rgretail.grocermax.bean.LoginResponse;
import com.rgretail.grocermax.bean.OTPResponse;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.AppLoadingScreen;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * @author Pratyesh Singh
 */

public class Registration extends BaseActivity implements
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static String SCREEN_NAME = "Registration";
	public static boolean fbORgoogle = false;              //uses b/c putUserDataSet sets true when login with FB or GOOGLE but when uses after to get response from LOGIN in onResponse() returns false always.
	public static String facebookName = null;
	public static String googleName = null;
//	ForgotPasswordTask forgotPasswordTask = null;
//	RegistrationTask registrationTask = null;
	boolean clickMale = false;
	boolean clickFemale = false;
	boolean clickOther = false;
	String USER_EMAIL = "";
	String QUOTE_ID_AFTER_FB = "";
	ImageView iv_googlePlus,img_changePwdType;
	Context context = this;
//	EasyTracker tracker;
	ImageView ivFacebook;
	TextView tvFacebook;
	private static final int FB_SIGN_IN = 64206;
	private static final int RC_SIGN_IN = 0;
    private static final int SOCIAL_LOGIN_PHONE_NUMBER = 333;
	private static TextView tv_google_btn;
	private boolean signedInUser;
	private ConnectionResult mConnectionResult;
	private boolean mIntentInProgress;
	public static GoogleApiClient mGoogleApiClient;
	private JSONObject jsonObjectParams;            //will use in OneTimePassword class

	String params;               //used when navigate to OTP screen
	String strEmail;            //used when navigate to OTP screen

    boolean isPasswordShow=false;
    ImageView icon_header_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		SCREEN_NAME = getIntent().getExtras().getString("whichScreen");

		mContext = this;

			try {
				mGoogleApiClient = new GoogleApiClient.Builder(Registration.this).
						addConnectionCallbacks(Registration.this).addOnConnectionFailedListener(Registration.this).
						addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
				mGoogleApiClient.connect();
			} catch (Exception e) {
			}
		
		if (SCREEN_NAME.equals("ForgotPassword")) {
			setContentView(R.layout.forgot_p);
			TextView tvHeaderForgot = (TextView) findViewById(R.id.tv_heading_forgot);
			tvHeaderForgot.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			addActionsInFilter(MyReceiverActions.FORGOT_PWD);
            icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
            icon_header_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
			
			displayForgotPasswordView();

		} else if (SCREEN_NAME.equals("Registration")) {
			setContentView(R.layout.registeration);
			TextView tvHeaderRegister = (TextView) findViewById(R.id.header_register);
            TextView tv_termandcond=(TextView)findViewById(R.id.tv_termandcond);
            tv_termandcond.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Registration.this,TermAndCondition.class);
                    i.putExtra("reference","terms");
                    startActivityForResult(i,123);
                }
            });
            icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
            icon_header_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
			//TODO this ttf is not lookiap
			//tvHeaderRegister.setTypeface(CustomFonts.getInstance().getRobotoBlack(this));
			addActionsInFilter(MyReceiverActions.REGISTER_USER);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART);
			addActionsInFilter(MyReceiverActions.LOGIN);
			addActionsInFilter(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN);
			addActionsInFilter(MyReceiverActions.OTP);

			//TODO abhi, now that lots of views are gone please rewire registration screen
			displayRegistrationView();
		}
		}catch(Exception e){
			new GrocermaxBaseException("Registeration","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private void displayForgotPasswordView() {
		try{
		final EditText email_id = (EditText) findViewById(R.id.email_id);

		TextView forgot_password = (TextView) findViewById(R.id.forgot_password);
		forgot_password.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		forgot_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String _email_id = email_id.getText().toString().trim();

				if (_email_id.equalsIgnoreCase("")) {
					UtilityMethods.customToast(Constants.ToastConstant.EMAIL_BLANCK, Registration.this);
					return;
				} else {
					// email validity check
					if (!UtilityMethods.isValidEmail(_email_id)) {
						UtilityMethods.customToast(Constants.ToastConstant.invalid_email, Registration.this);
						return;
					}
				}
				if (UtilityMethods.isInternetAvailable(mContext)) {
					showDialog();

					try {
//						String url = UrlsConstants.FORGET_PASSWORD_URL + _email_id;
						String url = UrlsConstants.FORGET_PASSWORD_URL;
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("uemail", _email_id);
//						jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
						myApi.reqForgetPassword(url, jsonObject);
//						myApi.reqForgetPassword(url);
					}catch(Exception e){}
				} else {
					UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
				}
			}
		});
		}catch(Exception e){
			new GrocermaxBaseException("Registration","displayForgotPasswordView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

    @Override
    public void onBackPressed() {
        setResult(1221);
        finish();
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
 		    img_changePwdType = (ImageView) findViewById(R.id.img_changePwdType);

			final CheckBox cbMale = (CheckBox) findViewById(R.id.cb_male);
			final CheckBox cbFemale = (CheckBox) findViewById(R.id.cb_female);
//			final CheckBox cbOther = (CheckBox) findViewById(R.id.cb_other);

			tv_google_btn = (TextView) findViewById(R.id.reg_button_google);
			iv_googlePlus = (ImageView) findViewById(R.id.reg_google_plus_left_icon);
			tv_google_btn.setOnClickListener(google_signin_listener);
			iv_googlePlus.setOnClickListener(google_signin_listener);

			ivFacebook = (ImageView)findViewById(R.id.facebook_icon);
			tvFacebook = (TextView)findViewById(R.id.button_facebook);

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


//			ivFB.setClickable(this);
//			tvFB.setClickable(this);
//			ivGoogle.setClickable(true);
//			tvGoogle.setClickable(true);
//			rlbtnGoogle.setClickable(true);
//			rlbtnFB.setClickable(true);


            /*password.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            if(isPasswordShow){
                                isPasswordShow=false;
                                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_pwd, 0);
                            }else{
                                isPasswordShow=true;
                                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_pwd, 0);
                            }
                            password.setSelection(password.length());
                            return true;
                        }
                    }
                    return false;
                }
            });*/

            img_changePwdType.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if(password.getText().toString().length()>0) {
                        password.setSelection(password.length());
                        if (isPasswordShow) {
                            isPasswordShow = false;
                            //password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            img_changePwdType.setImageResource(R.drawable.show_pwd);
                            //password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_pwd, 0);
                        } else {
                            isPasswordShow = true;
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            //password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            //password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_pwd, 0);
                            img_changePwdType.setImageResource(R.drawable.hide_pwd);
                        }

                   // }
                }
            });




			cbMale.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (cbMale.isChecked()) {
						cbFemale.setChecked(false);
//						cbOther.setChecked(false);
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
//						iv_other.setImageResource(R.drawable.checkbox_unselect);        //unselect
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
//						iv_other.setImageResource(R.drawable.checkbox_unselect);        //unselect
					} else {
						iv_female.setImageResource(R.drawable.checkbox_unselect);         //unselect
					}
				}
			});

//			iv_other.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					clickMale = false;
//					clickFemale = false;
//					if (clickOther) {
//						clickOther = false;
//					} else {
//						clickOther = true;
//					}
//
//					if (clickOther) {
//						iv_other.setImageResource(R.drawable.checkbox_select);           //select
//						iv_male.setImageResource(R.drawable.checkbox_unselect);       //unselect
//						iv_female.setImageResource(R.drawable.checkbox_unselect);        //unselect
//					} else {
//						iv_other.setImageResource(R.drawable.checkbox_unselect);         //unselect
//					}
//
//				}
//			});

			TextView account_create = (TextView) findViewById(R.id.account_create);
			account_create.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
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
						UtilityMethods.customToast(Constants.ToastConstant.FNAME_BLANCK, Registration.this);
						return;
					}
					/*if (_fname.contains(" ")) {
//					Toast.makeText(Registration.this,ToastConstant.FNAME_SINGLE_WORD, Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(Constants.ToastConstant.FNAME_SINGLE_WORD, Registration.this);
						return;
					}*/
                    // commented because of new design
					/*if (_lname.equals("")) {
						UtilityMethods.customToast(Constants.ToastConstant.LNAME_BLANCK, Registration.this);
						return;
					}
					if (_lname.contains(" ")) {
//					Toast.makeText(Registration.this, ToastConstant.LNAME_SINGLE_WORD, Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(Constants.ToastConstant.LNAME_SINGLE_WORD, Registration.this);
						return;
					}*/

					if (_mobile_no.equals("")) {
						UtilityMethods.customToast(Constants.ToastConstant.MOB_BLANCK, Registration.this);
						return;
					}

					if (_mobile_no.length() < 10 || _mobile_no.length() > 10) {
//					Toast.makeText(Registration.this, ToastConstant.MOB_NUMBER_DIGIT, Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(Constants.ToastConstant.MOB_NUMBER_DIGIT, Registration.this);
						return;
					}

//				if(gender.getSelectedItem().equals("Select")){
//					Toast.makeText(Registration.this, ToastConstant.SELECT_GENDER, Toast.LENGTH_LONG).show();
//					return;
//				}
                    // commented because of new design
					/*if (!clickMale && !clickFemale && !clickOther) {
						UtilityMethods.customToast(Constants.ToastConstant.SELECT_GENDER, Registration.this);
						return;
					}*/

					if (_email_id.equalsIgnoreCase("")) {
						UtilityMethods.customToast(Constants.ToastConstant.EMAIL_BLANCK, Registration.this);
						return;
					} else {
						// email validity check
						if (!UtilityMethods.isValidEmail(_email_id)) {
							UtilityMethods.customToast(Constants.ToastConstant.invalid_email, Registration.this);
							return;
						}
					}

					if (_password.equalsIgnoreCase("")) {
						UtilityMethods.customToast(Constants.ToastConstant.PWD_BLANK, Registration.this);
						return;
					}

					if (_password.length() < 6) {
						UtilityMethods.customToast(Constants.ToastConstant.PWD_NUMBER_DIGIT, Registration.this);
						return;
					}

                    // commented because of new design
					/*if (_confirmation.equalsIgnoreCase("")) {
						UtilityMethods.customToast(Constants.ToastConstant.CPWD_BLANK, Registration.this);
						return;
					}

					if (!_password.equals(_confirmation)) {
						UtilityMethods.customToast(Constants.ToastConstant.PWD_NOT_MATCH, Registration.this);
						return;
					}*/
					if (UtilityMethods.isInternetAvailable(mContext)) {
						showDialog();

                        MySharedPrefs.INSTANCE.putLoginMethod("Regular");

//						String url = UrlsConstants.REGESTRATION_URL;
						String url = UrlsConstants.REGESTRATION_URL_OTP;

						strEmail = _email_id;
						//String params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password;

						try {
                            jsonObjectParams = new JSONObject();
                            jsonObjectParams.put("fname", _fname);
                            jsonObjectParams.put("lname", _lname);
                            jsonObjectParams.put("uemail", _email_id);
                            jsonObjectParams.put("number", _mobile_no);
                            jsonObjectParams.put("password", _password);
                            jsonObjectParams.put("otp", "1");
//							jsonObjectParams.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
                        } catch (Exception e) {
                        }

						params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password;
						if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
//							params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password + "&quote_id=no";
//							url += params;
//						myApi.reqUserRegistration(url);
//							myApi.reqUserRegistrationOTP(url);
							MySharedPrefs.INSTANCE.putMobileNo(_mobile_no);//14/09/15
                            MySharedPrefs.INSTANCE.putFirstName(_fname);
                           // MySharedPrefs.INSTANCE.putLastName(_lname);
                            MySharedPrefs.INSTANCE.putLastName(".");
                            MySharedPrefs.INSTANCE.putUserEmail(_email_id);

							try {
								JSONObject jsonObject = new JSONObject();

								jsonObject.put("fname", _fname);
								jsonObject.put("lname", _lname);
								jsonObject.put("uemail", _email_id);
								jsonObject.put("number", _mobile_no);
								jsonObject.put("password", _password);
								jsonObject.put("quote_id", "no");
								jsonObject.put("otp", "0");
                                jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                                jsonObject.put("device_id",UtilityMethods.getDeviceId(Registration.this));
//								jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);

								myApi.reqUserRegistrationOTP(url, jsonObject);

							} catch (Exception e) {
							}

							////////////////POST/////////////
//							String strurl = UrlsConstants.REGESTRATION_URL_OTP;
//							HashMap<String, String> hashMap = new HashMap<String,String>();
//							hashMap.put("fname",_fname);
//							hashMap.put("lname",_lname);
//							hashMap.put("uemail",_email_id);
//							hashMap.put("number",_mobile_no);
//							hashMap.put("password",_password);
//							hashMap.put("quote_id","no");
//							myApi.reqUserRegistrationOTP(strurl,hashMap);
							////////////////POST/////////////
						} else {
//							params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password +
//									"&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
//							url += params;
//						myApi.reqUserRegistration(url);
//							myApi.reqUserRegistrationOTP(url);
							MySharedPrefs.INSTANCE.putMobileNo(_mobile_no);                           //14/09/15
                            MySharedPrefs.INSTANCE.putFirstName(_fname);
                            MySharedPrefs.INSTANCE.putLastName(_lname);
                            MySharedPrefs.INSTANCE.putUserEmail(_email_id);


							try {
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("fname", _fname);
								jsonObject.put("lname", _lname);
								jsonObject.put("uemail", _email_id);
								jsonObject.put("number", _mobile_no);
								jsonObject.put("password", _password);
								jsonObject.put("quote_id", MySharedPrefs.INSTANCE.getQuoteId());
								jsonObject.put("otp", "0");
                                jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                                jsonObject.put("device_id",UtilityMethods.getDeviceId(Registration.this));
//								jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
								myApi.reqUserRegistrationOTP(url, jsonObject);
							} catch (Exception e) {

							}
							////////////////POST/////////////
//							String strurl = UrlsConstants.REGESTRATION_URL_OTP;
//							HashMap<String, String> hashMap = new HashMap<String,String>();
//							hashMap.put("fname",_fname);
//							hashMap.put("lname",_lname);
//							hashMap.put("uemail",_email_id);
//							hashMap.put("number",_mobile_no);
//							hashMap.put("password",_password);
//							hashMap.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
//							myApi.reqUserRegistrationOTP(strurl, hashMap);
							////////////////POST/////////////
						}





/*HashMap<String, String> map = new HashMap<String, String>();
					
					map.put("fname", _fname);
					map.put("lname", _lname);
					map.put("uemail", _email_id);
					map.put("number", _mobile_no);
					map.put("password", _password);*/


					} else {
						UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
					}
				}
			});

//			initHeader(findViewById(R.id.header), true, null);
			if (SCREEN_NAME.equals("ForgotPassword")) {
				initHeader(findViewById(R.id.header), false, "Forgot Password");
				icon_header_search.setVisibility(View.GONE);
				icon_header_cart.setVisibility(View.GONE);
				cart_count_txt.setVisibility(View.GONE);
				icon_header_cart.setClickable(false);
				cart_count_txt.setClickable(false);
				icon_header_user.setClickable(false);
//				icon_header_search.setVisibility(View.GONE);
//				cart_count_txt.setVisibility(View.GONE);
//				icon_header_logo_without_search.setVisibility(View.GONE);
//				icon_header_logo_with_search.setVisibility(View.GONE);
//				icon_header_cart.setVisibility(View.GONE);
			}else if (SCREEN_NAME.equals("Registration")){
				initHeader(findViewById(R.id.header), false, "Register");
				icon_header_search.setVisibility(View.GONE);
				icon_header_cart.setVisibility(View.GONE);
				cart_count_txt.setVisibility(View.GONE);
				icon_header_cart.setClickable(false);
				cart_count_txt.setClickable(false);
				icon_header_user.setClickable(false);
//				cart_count_txt.setClickable(false);
//				icon_header_search.setVisibility(View.GONE);
//				cart_count_txt.setVisibility(View.GONE);
//				icon_header_logo_without_search.setVisibility(View.GONE);
//				icon_header_logo_with_search.setVisibility(View.GONE);
//				icon_header_cart.setVisibility(View.GONE);
			}
		}catch(NullPointerException e){
			new GrocermaxBaseException("Registeration", "displayRegistrationView", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("Registeration", "displayRegistrationView", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	public void OnResponse(Bundle bundle) {
		try{
			if (bundle.getString("ACTION").equals(MyReceiverActions.LOGIN)) {
				LoginResponse userDataBean = (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);

				if (userDataBean.getFlag().equalsIgnoreCase("1")) {          //successful login

					MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());


					if(MySharedPrefs.INSTANCE.getFirstName() == null){//if(MySharedPrefs.INSTANCE.getFirstName() != null){    //changed 17/9/15
						MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
					}
					if(MySharedPrefs.INSTANCE.getLastName() == null){//if(MySharedPrefs.INSTANCE.getLastName() != null){      //changed 17/9/15
						MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
					}
//
					MySharedPrefs.INSTANCE.putMobileNo(userDataBean.getMobile());

					icon_header_user.setImageResource(R.drawable.user_icon);  //login icon
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

				}else if(userDataBean.getFlag().equalsIgnoreCase("2")){   /*this is for opening Phonenumberfor Otp page from social login*/

                    if(MySharedPrefs.INSTANCE.getFirstName() == null){//if(MySharedPrefs.INSTANCE.getFirstName() != null){    //changed 17/9/15
                        MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
                    }
                    if(MySharedPrefs.INSTANCE.getLastName() == null){//if(MySharedPrefs.INSTANCE.getLastName() != null){      //changed 17/9/15
                        MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
                    }
                    MySharedPrefs.INSTANCE.putUserEmail(USER_EMAIL);
                    Intent i=new Intent(Registration.this,PhoneNumberForOTP.class);
                    startActivityForResult(i,SOCIAL_LOGIN_PHONE_NUMBER);

                }else {
					UtilityMethods.customToast(Constants.ToastConstant.LOGIN_FAIL, mContext);
				}
			}
		else if (bundle.getString("ACTION").equals(MyReceiverActions.OTP)) {
				dismissDialog();
				OTPResponse otpDataBean = (OTPResponse) bundle.getSerializable(ConnectionService.RESPONSE);
				if(otpDataBean.getFlag().equals("1")) {
					Intent intent = new Intent(this, OneTimePassword.class);
					Bundle call_bundle = new Bundle();
					call_bundle.putSerializable("Otp", otpDataBean);
					call_bundle.putString("USER_REGISTER_DATA", params);

					call_bundle.putSerializable("USER_REGISTER_DATA", String.valueOf(jsonObjectParams));

					call_bundle.putString("USER_EMAIL", strEmail);
                    call_bundle.putString("REGISTRATION_METHOD", "regular");
					intent.putExtras(call_bundle);
//					startActivity(intent);
					startActivityForResult(intent, 123);
				}else{
					UtilityMethods.customToast(otpDataBean.getResult(), mContext);
				}
			}
		else if (bundle.getString("ACTION").equals(MyReceiverActions.REGISTER_USER)) {
			LoginResponse userDataBean= (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);

			LoginResponse userDataBean1= (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);


//			if (userDataBean.getFlag().equalsIgnoreCase("1")) {
//				UtilityMethods.customToast(ToastConstant.REGISTER_SUCCESSFULL, mContext);
//				//finish();
//				MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
//				if(MySharedPrefs.INSTANCE.getFacebookEmail()==null)
//				MySharedPrefs.INSTANCE.putUserEmail(((EditText)findViewById(R.id.et_register_email)).getText().toString().trim());
//				else
//				MySharedPrefs.INSTANCE.putUserEmail(MySharedPrefs.INSTANCE.getFacebookEmail());
//				MySharedPrefs.INSTANCE.putLoginStatus(true);
//
//
//				ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(Registration.this, AppConstants.localCartFile);
//				if(cart_products != null && cart_products.size() >= 0)
//				{
//					try
//					{
//						JSONArray products = new JSONArray();
//						for(int i=0; i<cart_products.size(); i++)
//						{
//							JSONObject prod_obj = new JSONObject();
//							prod_obj.put("productid", cart_products.get(i).getItem_id());
//							prod_obj.put("quantity",cart_products.get(i).getQty());
//							products.put(prod_obj);
//						}
//						showDialog();
//						String url;
//						if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
//						{
//							System.out.println("without quote json="+products.toString());
//							url = UrlsConstants.ADD_TO_CART_URL
//									+ userDataBean.getUserID() +"&products="
//									+ URLEncoder.encode(products.toString(), "UTF-8");
//						}
//						else
//						{
//							System.out.println("with quote json="+products.toString());
//							url = UrlsConstants.ADD_TO_CART_URL
//									+ userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//									+ URLEncoder.encode(products.toString(), "UTF-8");
//						}
//						//String url = UrlsConstants.ADD_TO_CART_URL + userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+ "&products="+ URLEncoder.encode(products.toString(), "UTF-8");
//						myApi.reqAddToCart(url);
//					}
//					catch(Exception e)
//					{
//						e.printStackTrace();
//					}
//				}
//				else
//				{
//					setResult(RESULT_OK);
//					finish();
//				}
//			}
//			else{
//				UtilityMethods.customToast( userDataBean.getResult(), mContext);
//			}
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
		try {
//			initHeader(findViewById(R.id.header), true, null);
			if (SCREEN_NAME.equals("ForgotPassword")) {
				initHeader(findViewById(R.id.header), false, "Forgot Password");
				icon_header_cart.setClickable(false);
				cart_count_txt.setClickable(false);
				icon_header_user.setClickable(false);
//				icon_header_search.setVisibility(View.GONE);
//				cart_count_txt.setVisibility(View.GONE);
//				icon_header_logo_without_search.setVisibility(View.GONE);
//				icon_header_logo_with_search.setVisibility(View.GONE);
//				icon_header_cart.setVisibility(View.GONE);

				icon_header_search.setVisibility(View.GONE);
				icon_header_cart.setVisibility(View.GONE);
				cart_count_txt.setVisibility(View.GONE);
			}else if (SCREEN_NAME.equals("Registration")){
				initHeader(findViewById(R.id.header), false, "Register");
				icon_header_search.setVisibility(View.GONE);
				icon_header_cart.setVisibility(View.GONE);
				cart_count_txt.setVisibility(View.GONE);
				icon_header_cart.setClickable(false);
				cart_count_txt.setClickable(false);
				icon_header_user.setClickable(false);
//				icon_header_search.setVisibility(View.GONE);
//				cart_count_txt.setVisibility(View.GONE);
//				icon_header_logo_without_search.setVisibility(View.GONE);
//				icon_header_logo_with_search.setVisibility(View.GONE);
//				icon_header_cart.setVisibility(View.GONE);
			}
		}catch(Exception e){
			new GrocermaxBaseException("Registeration","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	try{
//			EasyTracker.getInstance(this).activityStart(this);
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
//			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
		try {
			RocqAnalytics.stopScreen(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		64206 requestCode

		switch (requestCode) {
			case RC_SIGN_IN:
				try{
					if (resultCode == RESULT_OK) {
						signedInUser = false;
					}
					mIntentInProgress = false;
					if (!mGoogleApiClient.isConnecting()) {
						mGoogleApiClient.connect();
					}
				}catch(Exception e){
					new GrocermaxBaseException("LoginAactivity","OnResponseRcSignIn",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
				}

				break;

			case FB_SIGN_IN:
				try{
					Session.getActiveSession().onActivityResult(Registration.this, requestCode,resultCode, data);
				}catch(Exception e){
					new GrocermaxBaseException("Registeration","onActivityResult",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
				}
		}

		try{
			if(requestCode==123)                  //uses when coming from OneTimePassword screen after to register successfully this will finish and go back to loginactivity and loginactivity also uses same funct
			{
                if(resultCode==1221){

                }else {
                    if (resultCode == RESULT_OK)
                        setResult(RESULT_OK);
                    finish();
                }
			}
		}catch(Exception e){
			new GrocermaxBaseException("Registeration","requestCode123",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
        try{
         /*Handing response from PhoneNumberforOTP activity page for social login otp generation*/
        if(requestCode==SOCIAL_LOGIN_PHONE_NUMBER){
            Intent intent = new Intent(mContext, HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        }catch(Exception e){
            new GrocermaxBaseException("Registeration","for otp on registration page-requestcode=333",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
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
					UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
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
		try {
		String USER_ID = "";
		String USER_FNAME = "";
		String USER_MNAME = "";
		String USER_LNAME = "";

		String USER_NAME = "";

		Registration.googleName = null;
		MySharedPrefs.INSTANCE.putGoogleName(null);

		USER_FNAME = user.getFirstName();
		USER_MNAME = user.getMiddleName();
		USER_LNAME = user.getLastName();

		try{
			MySharedPrefs.INSTANCE.putFirstName(USER_FNAME);
			MySharedPrefs.INSTANCE.putLastName(USER_LNAME);
		}catch(Exception e){}

		USER_EMAIL = user.getProperty("email").toString();
        MySharedPrefs.INSTANCE.putLoginMethod("Social");

		USER_ID = user.getId();

		if (USER_FNAME != null && USER_FNAME.length() > 0)
			USER_NAME = USER_FNAME;
		if (USER_MNAME != null && USER_MNAME.length() > 0)
			USER_NAME = USER_NAME + " " + USER_MNAME;
		if (USER_LNAME != null && USER_LNAME.length() > 0)
			USER_NAME = USER_NAME + " " + USER_LNAME;

		facebookName = USER_NAME;

		if (USER_NAME != null && USER_NAME.length() > 0)
			MySharedPrefs.INSTANCE.putFacebookName(USER_FNAME);

		if (USER_ID != null && USER_ID.length() > 0)
			MySharedPrefs.INSTANCE.putFacebookId(USER_ID);

		if (USER_EMAIL != null && USER_EMAIL.length() > 0)
			MySharedPrefs.INSTANCE.putFacebookEmail(USER_EMAIL);

		MySharedPrefs.INSTANCE.putUserDataSet(true);
		fbORgoogle = true;

		if (UtilityMethods.isInternetAvailable(mContext)) {
			showDialog();
			String url;
			JSONObject jsonObject = new JSONObject();
			if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
			{
//				url = UrlsConstants.FB_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getFacebookEmail() + "&quote_id=no&fname=" + USER_FNAME+"&lname="+USER_LNAME+"&number=0000000000";
				url = UrlsConstants.FB_LOGIN_URL;
				jsonObject.put("uemail",MySharedPrefs.INSTANCE.getFacebookEmail());
				jsonObject.put("quote_id","no");
				jsonObject.put("fname",USER_FNAME);
				jsonObject.put("lname", USER_LNAME);
				jsonObject.put("number", 0000000000);
//				jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
				System.out.println("==jsonobject==" + jsonObject);

			}else{
				QUOTE_ID_AFTER_FB=MySharedPrefs.INSTANCE.getQuoteId();
//				url = UrlsConstants.FB_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getFacebookEmail() + "&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&fname=" + USER_FNAME+"&lname="+USER_LNAME+"&number=0000000000";

				url = UrlsConstants.FB_LOGIN_URL;
				jsonObject.put("uemail",MySharedPrefs.INSTANCE.getFacebookEmail());
				jsonObject.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
				jsonObject.put("fname",USER_FNAME);
				jsonObject.put("lname", USER_LNAME);
				jsonObject.put("number", 0000000000);
//				jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
				System.out.println("==jsonobject==" + jsonObject);
			}
//			myApi.reqLogin(url);
            jsonObject.put("otp", "0");
            jsonObject.put("device_id",UtilityMethods.getDeviceId(Registration.this));
            jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
			myApi.reqLogin(url,jsonObject);

//			if (UtilityMethods.isInternetAvailable(mContext)) {
//				showDialog();
////						String url = UrlsConstants.REGESTRATION_URL;
//				String url = UrlsConstants.REGESTRATION_URL_OTP;
//				strEmail = USER_EMAIL;
//				//String params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password;
//				params = "fname=" + USER_FNAME + "&lname=" + USER_LNAME + "&uemail=" + USER_EMAIL + "&number=" + _mobile_no + "&password=" + _password;
//				if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals(""))
//					params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password + "&quote_id=no";
//				else
//					params = "fname=" + _fname + "&lname=" + _lname + "&uemail=" + _email_id + "&number=" + _mobile_no + "&password=" + _password + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
//				url += params;
////						myApi.reqUserRegistration(url);
//				myApi.reqUserRegistrationOTP(url);
//				MySharedPrefs.INSTANCE.putMobileNo(_mobile_no);

			} else {
			UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
		  }
		} catch (Exception e) {
			Log.e("ERROR", "Enable to get email");
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
				String strCity = MySharedPrefs.INSTANCE.getSelectedCity();
				String strRegionId = MySharedPrefs.INSTANCE.getSelectedStateRegionId();
				String strState = MySharedPrefs.INSTANCE.getSelectedState();
				String strStoreId = MySharedPrefs.INSTANCE.getSelectedStoreId();
				String strStateId = MySharedPrefs.INSTANCE.getSelectedStateId();

				MySharedPrefs.INSTANCE.clearAllData();

				MySharedPrefs.INSTANCE.putSelectedCity(strCity);
				MySharedPrefs.INSTANCE.putSelectedStateRegionId(strRegionId);
				MySharedPrefs.INSTANCE.putSelectedState(strState);
				MySharedPrefs.INSTANCE.putSelectedStoreId(strStoreId);
				MySharedPrefs.INSTANCE.putSelectedStateId(strStateId);

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




	/**************************************************  GOOGLE PLUS INTEGARTION *************************************************/

//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
////		googlePlusLogout();
////		if(mGoogleApiClient != null){
////			if (mGoogleApiClient.isConnected()) {
////	            mGoogleApiClient.disconnect();
////	        }
////		}
//	}

	/**
	 * google sign in listener
	 * */
	OnClickListener google_signin_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
//			try{
//				mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this).
//		        		addConnectionCallbacks(LoginActivity.this).addOnConnectionFailedListener(LoginActivity.this).
//		        		addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
//		        mGoogleApiClient.connect();
//			}catch(Exception e){}
				if (UtilityMethods.isInternetAvailable(mContext)) {
					if (tv_google_btn.getText().toString().equalsIgnoreCase("Join with Google")) {
						googleLoginWithEmailPermission();
					} else if (tv_google_btn.getText().toString().equalsIgnoreCase("LOGOUT WITH GOOGLE")) {
						//googlePlusLogoutReg();
                        googleLoginWithEmailPermission();
					}
				} else {
//				Toast.makeText(mContext, ToastConstant.msgNoInternet, Toast.LENGTH_SHORT).show();
					UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
				}
			}catch(Exception e){
				new GrocermaxBaseException("LoginActivity","google_signin_listener",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}

		}
	};


	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		try {
			//save the result and resolve the connection failure upon a user click.

//		if (!result.hasResolution()) {
			if (!mIntentInProgress && signedInUser && result.hasResolution()) {                         //pop up comes when user press on login with google and user click on cancel
//            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
				return;
			}
			if (!result.hasResolution()) {
				GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
				return;

			}
			if (!mIntentInProgress) {
				// store mConnectionResult
				mConnectionResult = result;
				if (signedInUser) {
					resolveSignInError();
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onConnectionFailed",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		try {
			AppLoadingScreen.getInstance(context).dismissDialog();
			signedInUser = false;
//        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
//			UtilityMethods.customToast("Connected", this);
			getProfileInformation();
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onConnected",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		try{
			mGoogleApiClient.connect();
//	        updateProfile(false);
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onConnectionSuspended",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private void googleLoginWithEmailPermission(){
		try{
//		AppLoadingScreen.getInstance(context).showDialog();
//		Toast.makeText(context,"22222", Toast.LENGTH_SHORT).show();

//        Toast.makeText(context,"4444", Toast.LENGTH_SHORT).show();
//        googlePlusLogin();

//		new GooglePlus((Activity)context,context);
			googlePlusLogin();

//        Toast.makeText(context,"5555", Toast.LENGTH_SHORT).show();
//		Intent intent = new Intent(this, GooglePlus.class);
//		context.startActivity(intent);
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","googleLoginWithEmailPermission",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private void googlePlusLogin() {
		try {
			if (mGoogleApiClient != null) {
				if (!mGoogleApiClient.isConnecting()) {
					signedInUser = true;
					resolveSignInError();
				}
//				UtilityMethods.customToast("googlewkwk plus login else", context);
			} else {
//        	Toast.makeText(context,"google plus login else", Toast.LENGTH_SHORT).show();
//				UtilityMethods.customToast("google plus login else", context);
			}
		}catch(Exception e){
			new GrocermaxBaseException("Registration","googlePlusLogin",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private void resolveSignInError() {
//		Toast.makeText(context,"88888", Toast.LENGTH_SHORT).show();
		try {
			if (mConnectionResult != null) {
				if (mConnectionResult.hasResolution()) {
					try {
						mIntentInProgress = true;
						mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
					} catch (IntentSender.SendIntentException e) {
						mIntentInProgress = false;
						mGoogleApiClient.connect();
					}
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("Registration","resolveSignInError",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
//		else{
//		}
	}

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				tv_google_btn.setText("LOGOUT WITH GOOGLE");
				Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
				saveGoogleUserData(currentPerson);

				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//				UtilityMethods.customToast(personName+email, this);
//				UtilityMethods.customToast(personPhotoUrl, this);

//                currentPerson.getId()


//                currentPerson.getAboutMe();
//                currentPerson.getAgeRange();
//                currentPerson.getBirthday();
//                currentPerson.getBraggingRights();
//                currentPerson.getCircledByCount();
//                currentPerson.getCover();
//                currentPerson.getCurrentLocation();
//
//                currentPerson.getGender();
//                currentPerson.getId();
//                currentPerson.getImage();
//                currentPerson.getLanguage();
//                currentPerson.getName();
//                currentPerson.getNickname();
//                currentPerson.getOrganizations();
//                currentPerson.getPlacesLived();
//                currentPerson.getPlusOneCount();
//                currentPerson.getRelationshipStatus();
//                currentPerson.getTagline();
//                currentPerson.getUrl();
//                currentPerson.getUrls();




//                username.setText(personName);
//                emailLabel.setText(email);

//                Toast.makeText(this, personName, Toast.LENGTH_LONG).show();
//                Toast.makeText(this, email, Toast.LENGTH_LONG).show();



//                new LoadProfileImage(image).execute(personPhotoUrl);
				// update profile frame with new info about Google Account
				// profile
//                updateProfile(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new GrocermaxBaseException("Registration","getProfileInformation",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	public static void googlePlusLogoutReg() {
		try{
			if(mGoogleApiClient != null){
				if (mGoogleApiClient.isConnected()) {
					Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
					mGoogleApiClient.disconnect();
					mGoogleApiClient.connect();
					if(tv_google_btn != null){
						tv_google_btn.setText("Join with Google");
					}
	//            updateProfile(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			new GrocermaxBaseException("Registration","googlePlusLogoutReg",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

//    public static void googlePlusLogoutLocally() {
//    	if(mGoogleApiClient != null){
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//
//            if(tv_google_btn != null){
//            	tv_google_btn.setText("Join with Google");
//            }
////            updateProfile(false);
//         }
//    	}
//    }


	/**
	 * for saving google plus data
	 * */
	private void saveGoogleUserData(Person currentPerson) {
		try{
		String USER_ID = "";
//		String USER_FNAME = "";
//		String USER_MNAME = "";
//		String USER_LNAME = "";

		Registration.facebookName = null;
		MySharedPrefs.INSTANCE.putFacebookName(null);

		USER_EMAIL = "";
		String USER_NAME = "";
		USER_NAME = currentPerson.getDisplayName();

         MySharedPrefs.INSTANCE.putLoginMethod("Social");

//		USER_FNAME = currentPerson.getName();
//		USER_MNAME = user.getMiddleName();
//		USER_LNAME = user.getLastName();
		try {
			USER_EMAIL = Plus.AccountApi.getAccountName(mGoogleApiClient);
		} catch (Exception e) {
			Log.e("ERROR", "Enable to get email");
		}

		USER_ID = currentPerson.getId();

//		if (USER_FNAME != null && USER_FNAME.length() > 0)
//			USER_NAME = USER_FNAME;
//		if (USER_MNAME != null && USER_MNAME.length() > 0)
//			USER_NAME = USER_NAME + " " + USER_MNAME;
//		if (USER_LNAME != null && USER_LNAME.length() > 0)
//			USER_NAME = USER_NAME + " " + USER_LNAME;

			try{
				MySharedPrefs.INSTANCE.putFirstName(USER_NAME);
			}catch(Exception e){}

		if (USER_NAME != null && USER_NAME.length() > 0)
			MySharedPrefs.INSTANCE.putGoogleName(USER_NAME);

		googleName = USER_NAME;

		if (USER_ID != null && USER_ID.length() > 0)
			MySharedPrefs.INSTANCE.putGoogleId(USER_ID);

		if (USER_EMAIL != null && USER_EMAIL.length() > 0)
			MySharedPrefs.INSTANCE.putGoogleEmail(USER_EMAIL);

		MySharedPrefs.INSTANCE.putUserDataSet(true);
		fbORgoogle = true;

		if (UtilityMethods.isInternetAvailable(mContext)) {
			showDialog();
			//-----------String url = UrlsConstants.LOGIN_URL+"uemail="+ userN + "&password=" + pwd;
//			String url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ USER_EMAIL + "&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";

			String url = "";
			JSONObject jsonObject = new JSONObject();
			if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
			{
//				url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ USER_EMAIL + "&quote_id=no&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";
				url = UrlsConstants.GOOGLE_LOGIN_URL;
				jsonObject.put("uemail",USER_EMAIL);
				jsonObject.put("quote_id","no");
				jsonObject.put("fname",USER_NAME);
				jsonObject.put("lname", "");
				jsonObject.put("number", 0000000000);
//				jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
				System.out.println("==jsonobject==" + jsonObject);
			}else{
				url = UrlsConstants.GOOGLE_LOGIN_URL;
//				url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ USER_EMAIL + "&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";
				jsonObject.put("uemail",USER_EMAIL);
				jsonObject.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
				jsonObject.put("fname",USER_NAME);
				jsonObject.put("lname", "");
				jsonObject.put("number", 0000000000);
//				jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
				System.out.println("==jsonobject==" + jsonObject);
			}

//			String url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getGoogleEmail() + "&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";
//			String url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getGoogleEmail() + "&fname=" + USER_NAME+"&number=0000000000";
//			myApi.reqLogin(url);
            jsonObject.put("otp", "0");
            jsonObject.put("device_id",UtilityMethods.getDeviceId(Registration.this));
            jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
			myApi.reqLogin(url,jsonObject);

		} else {
//			Toast.makeText(mContext, ToastConstant.msgNoInternet ,Toast.LENGTH_LONG).show();
			UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, mContext);
		}
		} catch (Exception e) {
			e.printStackTrace();
			new GrocermaxBaseException("Registration","saveGoogleUserData",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	/**************************************************  GOOGLE PLUS INTEGARTION *************************************************/




}
