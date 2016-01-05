package com.rgretail.grocermax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.bean.CartDetailBean;
import com.rgretail.grocermax.bean.LoginResponse;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.AppLoadingScreen;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.Constants.ToastConstant;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.google.analytics.tracking.android.EasyTracker;
//import android.widget.Toast;


public class LoginActivity extends BaseActivity
		implements ConnectionCallbacks, OnConnectionFailedListener
{
	ImageView button_facebook;
	TextView button_skip;
	EditText username, password;
	//	ImageView googlePlus;
	private ImageView tv_google_btn;
	Context context=this;
	//	CheckBox remember_me;
	String QUOTE_ID_AFTER_FB = "";
//	SignInButton signinButton;

	private static final int RC_SIGN_IN = 0;          //
	private static final int FB_SIGN_IN = 64206;
	// Google client to communicate with Google        //
	public static GoogleApiClient mGoogleApiClient;            //
	private boolean mIntentInProgress;                      //
	private boolean signedInUser;                            //
	private ConnectionResult mConnectionResult;
	String USER_EMAIL = "";          //common for facebook and google plus
	private String SCREENNAME = "LoginActivity-";

//	EasyTracker tracker;
	int requestcodecart = 00;  //uses for gettng separation of whether calling from cartactivity or from home screen.

	public static LoginActivity instance = null;
	public static LoginActivity getInstance(){
		if(instance == null){
			instance = new LoginActivity();
		}
		return instance;
	}
	public LoginActivity(){
		instance = this;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.login_screen);
		setContentView(R.layout.login_new);
//		setContentView(R.layout.confirmation_activity);
//		setContentView(R.layout.order_failure);


		try{
			AppsFlyerLib.setCurrencyCode("INR");
			AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
			AppsFlyerLib.sendTracking(getApplicationContext());
		}catch(Exception e){}

		try {
			//context = this;

			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				requestcodecart = bundle.getInt("requestCode");

			}

//			tracker = EasyTracker.getInstance(this);

			try {
				mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this).
						addConnectionCallbacks(LoginActivity.this).addOnConnectionFailedListener(LoginActivity.this).
						addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
				mGoogleApiClient.connect();
			} catch (Exception e) {
			}

			addActionsInFilter(MyReceiverActions.LOGIN);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART);
//		addActionsInFilter(MyReceiverActions.VIEW_CART);
			addActionsInFilter(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN);

			TextView register = (TextView) findViewById(R.id.register);
			//Spannable word = new SpannableString("New Here? ");

			//word.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_text)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			//register.setText(word);
			//Spannable wordTwo = new SpannableString("Register Now");

			//wordTwo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_grey)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			//register.append(wordTwo);

			TextView txtHello = (TextView) findViewById(R.id.txt_hello);
			txtHello.setTypeface(CustomFonts.getInstance().getRobotoLight(context));

			register.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					replaceScreen("Registration");

				}
			});
			register.setTypeface(CustomFonts.getInstance().getRobotoRegular(context));

			TextView forgot_pwd = (TextView) findViewById(R.id.forgot_password);
			forgot_pwd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					replaceScreen("ForgotPassword");
				}
			});
			forgot_pwd.setTypeface(CustomFonts.getInstance().getRobotoBold(this));


			final View viewMail = (View) findViewById(R.id.view_mail_line);
			final View viewPwd = (View) findViewById(R.id.view_pwd_line);


			username = (EditText) findViewById(R.id.username);
			password = (EditText) findViewById(R.id.password);
			username.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			password.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
		/*username.setText("suman.ditm07@gmail.com");
		password.setText("XHqvaHnW");*/

//		remember_me = (CheckBox) findViewById(R.id.remember_me);
//		if(MySharedPrefs.INSTANCE.getRememberMe())
//		{
			username.setText(MySharedPrefs.INSTANCE.getRememberMeEmail());
//		}

			username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						viewPwd.setBackgroundColor(getResources().getColor(R.color.grey));
						viewMail.setBackgroundColor(getResources().getColor(R.color.white));
					}
				}
			});

			password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						viewMail.setBackgroundColor(getResources().getColor(R.color.grey));
						viewPwd.setBackgroundColor(getResources().getColor(R.color.white));
					}
				}
			});

			button_facebook = (ImageView) findViewById(R.id.button_facebook);
			button_facebook.setOnClickListener(fb_signin_listener);

//			googlePlus = (ImageView) findViewById(R.id.google_plus_left_icon);
			tv_google_btn = (ImageView) findViewById(R.id.button_google);

//	    signinButton = (SignInButton) findViewById(R.id.google_plus_icon);

//			googlePlus.setOnClickListener(google_signin_listener);
			tv_google_btn.setOnClickListener(google_signin_listener);


//	    signinButton.setOnClickListener(clickListener);

//		Session session = Session.getActiveSession();
//		if (session != null && session.isOpened()) {
//			button_facebook.setText("Sign out Facebook");
//			button_facebook.setOnClickListener(fb_sign_out_listener);
//		} else {
//			button_facebook.setText("Sign in with Facebook");
//			button_facebook.setOnClickListener(fb_signin_listener);
//		}

			button_skip = (TextView) findViewById(R.id.login);
			button_skip.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					logIn();
				}
			});

			button_skip.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public void gotoHome(View v)
	{
		try{
			Intent intent = new Intent(mContext, HomeScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();

//			Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			finish();
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","gotoHome",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}


//	android.view.View.OnClickListener clickListener = new  OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if(v.getId()== R.id.google_plus_icon){
//				googleLoginWithEmailPermission();
//			}
//
//		}
//	};


	/**
	 * facebook sign in listener
	 * */
	public OnClickListener fb_signin_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{

				if (UtilityMethods.isInternetAvailable(mContext)) {
					facebookLoginWithEmailPermission();
				} else
					UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
			}catch(Exception e){
				new GrocermaxBaseException("LoginActivity","fb_signin_listener",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
		}
	};

	/**
	 * Function to open Registration and Forgot Password Screen
	 * */
	private void replaceScreen(String whichAction) {
		try{
			Intent intent = new Intent(this, Registration.class);
			intent.putExtra("whichScreen", whichAction);
			startActivityForResult(intent,111);
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","gotoHome",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	/**
	 * facebook sign in listener
	 * */
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
//					button_facebook.setText("Sign in with Facebook");
					button_facebook.setOnClickListener(fb_signin_listener);
				}
			}catch(Exception e){
				new GrocermaxBaseException("LoginActivity","fb_sign_out_listener",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
		}
	};

	/**
	 * for logging with user id and password
	 * */

	private void logIn()
	{
		try{
			String userN = username.getText().toString().trim();
			String pwd = password.getText().toString().trim();
			if(userN.length() == 0)
			{
				UtilityMethods.customToast(ToastConstant.ENTER_USERNAME, this);
				return;
			}
			else if(pwd.length() == 0)
			{
				UtilityMethods.customToast(ToastConstant.ENTER_PWD, this);
				return;
			}
			// email validity check
			else if (!UtilityMethods.isValidEmail(userN)) {
				UtilityMethods.customToast(ToastConstant.ENTER_CORRECT_EMAIL, this);
				return;
			}
			else
			{
//			if(remember_me.isChecked())
//			{
//				MySharedPrefs.INSTANCE.putRememberMe(true, userN);
//			}
				if (UtilityMethods.isInternetAvailable(mContext)) {
					showDialog();
					String url;
					try{UtilityMethods.clickCapture(context,"Login","","Regular","",MySharedPrefs.INSTANCE.getSelectedCity());
                    }catch(Exception e){}
//					HashMap<String, String> hashMap = new HashMap<String,String>();
					JSONObject jsonObject = new JSONObject();
					if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
					{
//						url = UrlsConstants.LOGIN_URL+"uemail="+ userN + "&password=" + pwd+"&quote_id=no";
						url = UrlsConstants.LOGIN_URL;
						jsonObject.put("uemail",userN);
						jsonObject.put("password",pwd);
						jsonObject.put("quote_id","no");
//						jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
						System.out.println("==jsonobject=="+jsonObject);

					}else{
//						url = UrlsConstants.LOGIN_URL+"uemail="+ userN + "&password=" + pwd+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
						url = UrlsConstants.LOGIN_URL;
						jsonObject.put("uemail",userN);
						jsonObject.put("password",pwd);
						jsonObject.put("quote_id", MySharedPrefs.INSTANCE.getQuoteId());
//						jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
						System.out.println("==jsonobject==" + jsonObject);

					}
					try {
                        jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                        jsonObject.put("device_id",UtilityMethods.getDeviceId(LoginActivity.this));
						MySharedPrefs.INSTANCE.putFirstName(null);          //
						MySharedPrefs.INSTANCE.putLastName(null);           //
						Registration.googleName = null;                     //
						MySharedPrefs.INSTANCE.putGoogleName(null);         //
						Registration.facebookName = null;                   //
						MySharedPrefs.INSTANCE.putFacebookName(null);       //
					}catch(Exception e){}
					myApi.reqLogin(url,jsonObject);
					//String url = UrlsConstants.LOGIN_URL+"uemail="+ userN + "&password=" + pwd;

//					myApi.reqLogin(url);
//					myApi.reqLogin(url, hashMap);

				} else {
					UtilityMethods.customToast(ToastConstant.msgNoInternet, this);
//					Toast.makeText(mContext, ToastConstant.msgNoInternet ,Toast.LENGTH_LONG).show();
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","logIn",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	/**
	 * for saving facebook data
	 * */
	private void saveUserData(GraphUser user) {
		try{
			String USER_ID = "";
			String USER_FNAME = "";
			String USER_MNAME = "";
			String USER_LNAME = "";

			String USER_NAME = "";

			try{UtilityMethods.clickCapture(context,"Login","","Facebook","",MySharedPrefs.INSTANCE.getSelectedCity());}catch(Exception e){}

			Registration.googleName = null;
			MySharedPrefs.INSTANCE.putGoogleName(null);

			USER_FNAME = user.getFirstName();
			USER_MNAME = user.getMiddleName();
			USER_LNAME = user.getLastName();

			try{
				MySharedPrefs.INSTANCE.putFirstName(USER_FNAME);
				MySharedPrefs.INSTANCE.putLastName(USER_LNAME);
			}catch(Exception e){}

			try {
//			String str1 = 	MySharedPrefs.INSTANCE.getFirstName();
//			String str2 = 	MySharedPrefs.INSTANCE.getLastName();
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

			Registration.facebookName = USER_NAME;

			if (USER_NAME != null && USER_NAME.length() > 0)
				MySharedPrefs.INSTANCE.putFacebookName(USER_FNAME);

			if (USER_ID != null && USER_ID.length() > 0)
				MySharedPrefs.INSTANCE.putFacebookId(USER_ID);

			if (USER_EMAIL != null && USER_EMAIL.length() > 0)
				MySharedPrefs.INSTANCE.putFacebookEmail(USER_EMAIL);

			MySharedPrefs.INSTANCE.putUserDataSet(true);
			Registration.fbORgoogle = true;

			if (UtilityMethods.isInternetAvailable(mContext)) {
				showDialog();
				String url;
//				HashMap<String, String> hashMap = new HashMap<String,String>();
				JSONObject jsonObject = new JSONObject();
				if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
				{
//					url = UrlsConstants.FB_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getFacebookEmail() + "&quote_id=no&fname=" + USER_FNAME+"&lname="+USER_LNAME+"&number=0000000000";
					url = UrlsConstants.FB_LOGIN_URL;
//					hashMap.put("uemail",MySharedPrefs.INSTANCE.getFacebookEmail());
//					hashMap.put("quote_id","no");
//					hashMap.put("fname",USER_FNAME);
//					hashMap.put("lname",USER_LNAME);
//					hashMap.put("number","0000000000");
					jsonObject.put("uemail",MySharedPrefs.INSTANCE.getFacebookEmail());
					jsonObject.put("quote_id","no");
					jsonObject.put("fname",USER_FNAME);
					jsonObject.put("lname", USER_LNAME);
					jsonObject.put("number", 0000000000);
//					jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
					System.out.println("==jsonobject==" + jsonObject);


				}else{
					QUOTE_ID_AFTER_FB=MySharedPrefs.INSTANCE.getQuoteId();
//					url = UrlsConstants.FB_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getFacebookEmail() + "&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&fname=" + USER_FNAME+"&lname="+USER_LNAME+"&number=0000000000";

					url = UrlsConstants.FB_LOGIN_URL;
//					hashMap.put("uemail",MySharedPrefs.INSTANCE.getFacebookEmail());
//					hashMap.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
//					hashMap.put("fname",USER_FNAME);
//					hashMap.put("lname",USER_LNAME);
//					hashMap.put("number","0000000000");

					jsonObject.put("uemail",MySharedPrefs.INSTANCE.getFacebookEmail());
					jsonObject.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
					jsonObject.put("fname",USER_FNAME);
					jsonObject.put("lname", USER_LNAME);
					jsonObject.put("number", 0000000000);
//					jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
//					System.out.println("==jsonobject==" + jsonObject);
				}
                jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                jsonObject.put("device_id",UtilityMethods.getDeviceId(LoginActivity.this));
				myApi.reqLogin(url,jsonObject);

//				myApi.reqLogin(url);
//				myApi.reqLogin(url,hashMap);

			} else {
				UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","saveUserData",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	/**
	 *
	 * FB Methods
	 * */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		64206 requestCode
		super.onActivityResult(requestCode, resultCode, data);
		try{
			switch (requestCode) {
				case RC_SIGN_IN:
					try{
//						Toast.makeText(mContext, "b1b1b1b1b1b1b1b1b1b1"+requestCode, Toast.LENGTH_SHORT).show();
						if (resultCode == RESULT_OK) {
//							Toast.makeText(mContext, "c1c1c1c1c1c1c1", Toast.LENGTH_SHORT).show();
							signedInUser = false;
						}
						mIntentInProgress = false;
						if (!mGoogleApiClient.isConnecting()) {
//							Toast.makeText(mContext, "d1d1d1d1d1d1d1d1", Toast.LENGTH_SHORT).show();
							mGoogleApiClient.connect();
						}
					}catch(Exception e){
//						Toast.makeText(mContext, "exception===="+e.getStackTrace(), Toast.LENGTH_SHORT).show();
						new GrocermaxBaseException("LoginAactivity","OnResponseRcSignIn",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
					}

					break;

				case FB_SIGN_IN:
					try{
						Session.getActiveSession().onActivityResult(LoginActivity.this, requestCode,resultCode, data);
					}catch(Exception e){
						new GrocermaxBaseException("LoginActivity","OnResponseFbSignIn",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
					}
			}

			if(requestCode==111)
			{
				if(resultCode==RESULT_OK)
					setResult(RESULT_OK);
				finish();
			}

//		if(requestCode == 64206){
//			Session.getActiveSession().onActivityResult(LoginActivity.this, requestCode,resultCode, data);
//		}else{
//
//		}

		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onActivityResult",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}

	}


//	@Override
//	protected void onActivityResult(int requestCode, int responseCode,
//			Intent intent) {
//		switch (requestCode) {
//		case RC_SIGN_IN:
//			if (responseCode == RESULT_OK) {
//				signedInUser = false;
//			}
//			mIntentInProgress = false;
//			if (!mGoogleApiClient.isConnecting()) {
//				mGoogleApiClient.connect();
//			}
//
//			break;
//		}
//		Toast.makeText(this, "activity result", Toast.LENGTH_LONG).show();
//	}


	private void facebookLoginWithEmailPermission() {
		try {
			final List<String> newPermissionsRequest = new ArrayList<String>(Arrays.asList("email", "user_location"));
			openActiveSession(LoginActivity.this, true, new Session.StatusCallback() {
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
//									button_facebook.setText("Sign out Facebook");
									button_facebook.setOnClickListener(fb_sign_out_listener);
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
			new GrocermaxBaseException("LoginActivity","facebookLoginWithEmailPermission",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private static Session openActiveSession(Activity activity, boolean allowLoginUI, Session.StatusCallback callback, List<String> permissions) {
		try{
			Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
			Session session = new Session.Builder(activity).build();
			if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
				Session.setActiveSession(session);
				session.openForRead(openRequest);
				return session;
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","openActiveSession",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
		return null;
	}

	@Override
	public void OnResponse(Bundle bundle) {
		try {
			if (bundle.getString("ACTION").equals(MyReceiverActions.LOGIN)) {
				LoginResponse userDataBean = (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);

				if (userDataBean.getFlag().equalsIgnoreCase("1")) {          //successfull login
					MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
					if(MySharedPrefs.INSTANCE.getFirstName() == null){//if(MySharedPrefs.INSTANCE.getFirstName() != null){    //changed 17/9/15
						MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
					}
					if(MySharedPrefs.INSTANCE.getLastName() == null){//if(MySharedPrefs.INSTANCE.getLastName() != null){      //changed 17/9/15
						MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
					}

					MySharedPrefs.INSTANCE.putMobileNo(userDataBean.getMobile());
//					boolean bbbb = MySharedPrefs.INSTANCE.isUserDataSet();
//					String id = MySharedPrefs.INSTANCE.getFacebookId();
//					if(MySharedPrefs.INSTANCE.getFacebookId() != null){
//						System.out.print("");
//					}else{
//						System.out.print("");
//					}

//					MySharedPrefs.INSTANCE.putGoogleId(USER_ID);
//					String id1 = MySharedPrefs.INSTANCE.getGoogleId();
//					if(MySharedPrefs.INSTANCE.getGoogleId() != null){
//						System.out.print("");
//					}else{
//						System.out.print("");
//					}
//					if(!MySharedPrefs.INSTANCE.isUserDataSet()) {                            //if true then no need to enter in it b/c from server first and last name not coming,So already saved when data getting from facebook api.
//						MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
//						MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
//					}

//					BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon);  //login icon
					if (USER_EMAIL.equals("")) {
						MySharedPrefs.INSTANCE.putUserEmail(username.getText().toString());
						if(userDataBean.getQuoteId() != null && !userDataBean.getQuoteId().equals("") ) {
							MySharedPrefs.INSTANCE.clearQuote();
							MySharedPrefs.INSTANCE.putQuoteId(userDataBean.getQuoteId());
						}
					} else {
						MySharedPrefs.INSTANCE.putUserEmail(USER_EMAIL);
						if(QUOTE_ID_AFTER_FB != "") {
							MySharedPrefs.INSTANCE.clearQuote();
							MySharedPrefs.INSTANCE.putQuoteId(QUOTE_ID_AFTER_FB);
						}
					}

					MySharedPrefs.INSTANCE.putLoginStatus(true);
					if(userDataBean.getQuoteId() != null && !userDataBean.getQuoteId().equals("")) {
						MySharedPrefs.INSTANCE.clearQuote();
						MySharedPrefs.INSTANCE.putQuoteId(userDataBean.getQuoteId());/////////last change
					}
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(userDataBean.getTotalItem()));
//					ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(LoginActivity.this, AppConstants.localCartFile);  //send to server if local cart has products for adding.
//					if (cart_products != null && cart_products.size() > 0)            //it will call when user has come from HOME | PRODUCT LISTING | DESCRIPTION only.
//					{
//						try {
//							JSONArray products = new JSONArray();
//							for (int i = 0; i < cart_products.size(); i++) {
//								JSONObject prod_obj = new JSONObject();
//								prod_obj.put("productid", cart_products.get(i).getItem_id());
//								prod_obj.put("quantity", cart_products.get(i).getQty());
//								products.put(prod_obj);
//							}
//							showDialog();
//							String url;
//							if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
//								System.out.println("without quote json=" + products.toString());
//								url = UrlsConstants.ADD_TO_CART_URL
//										+ userDataBean.getUserID() + "&products="
//										+ URLEncoder.encode(products.toString(), "UTF-8");
//							} else {
//								System.out.println("with quote json=" + products.toString());
//								url = UrlsConstants.ADD_TO_CART_URL
//										+ userDataBean.getUserID() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&products="
//										+ URLEncoder.encode(products.toString(), "UTF-8");
//							}
//							//String url = UrlsConstants.ADD_TO_CART_URL + userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+ "&products="+ URLEncoder.encode(products.toString(), "UTF-8");
//							myApi.reqAddToCart(url);
//							finish();                                         //added
////								finishAffinity();
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else          //SIMPLE LOGIN AND FACEBOOK CASE : user can come here by [direct login] OR [from product listing] and [home screen] OR [from view cart].
//					{             //quote id will not return from server in SIMPLE LOGIN case BUT quote id will return in case of FACEBOOK login.
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


					if (requestcodecart != 00) {
						if (requestcodecart == AppConstants.LOGIN_REQUEST_CODE) {           //call from cartproductlist
							setResult(RESULT_OK);
							finish();
						}else{                                                                  //move on to homescreen
							Intent intent = new Intent(mContext, HomeScreen.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						}
					}else {                                                                   //move on to homescreen
						Intent intent = new Intent(mContext, HomeScreen.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}

//					}

//					}

				} else {
					UtilityMethods.customToast(ToastConstant.LOGIN_FAIL, mContext);
				}
			} else if (bundle.getString("ACTION").equals(MyReceiverActions.ADD_TO_CART))  //it will call when user has come from HOME | PRODUCT LISTING | DESCRIPTION only.
			{
				BaseResponseBean response = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (response.getFlag().equals("1")) {
//				String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId();
//				myApi.reqViewCart(url);  MySharedPrefs.INSTANCE.getUserId().equals("")
					showDialog();
					if (MySharedPrefs.INSTANCE.getQuoteId() != null && !MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
						System.out.println("==some value sharedprefs==" + MySharedPrefs.INSTANCE.getQuoteId() + ">>");
						String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
//					myApi.reqViewCart(url);
						myApi.reqViewCartGoHomeScreen(url);
					} else if (response.getQuoteId() != null && !response.getQuoteId().equals("")) {
						System.out.println("==some value simple==" + response.getQuoteId() + "||||");
						String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + response.getQuoteId();
//					myApi.reqViewCart(url);
						myApi.reqViewCartGoHomeScreen(url);
					} else {

					}
					UtilityMethods.deleteLocalCart(LoginActivity.this);
//				finish();
				} else {
					setResult(RESULT_OK);
					finish();
				}
				if(response.getQuoteId() != null && !response.getQuoteId().equals("")) {
					MySharedPrefs.INSTANCE.clearQuote();
					MySharedPrefs.INSTANCE.putQuoteId(response.getQuoteId());
				}
				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(response.getTotalItem()));
			} else if (bundle.getString("ACTION").equals(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN)) {
				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);


//			if(cartBean.getFlag().equals("1"))
//			{
				if (cartBean.getItems().size() > 0) {
					UtilityMethods.deleteCloneCart(LoginActivity.this);
					for (int i = 0; i < cartBean.getItems().size(); i++) {
						UtilityMethods.writeCloneCart(LoginActivity.this, Constants.localCloneFile, cartBean.getItems().get(i));
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
			new GrocermaxBaseException("LoginActivity","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
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
	public void onResume() {
		super.onResume();

		try{
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
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
		try{
//			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
			googlePlusLogout();
			if(mGoogleApiClient != null){
				if (mGoogleApiClient.isConnected()) {
					mGoogleApiClient.disconnect();
				}
			}
		}catch(Exception e){}
	}

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
//					if (tv_google_btn.getText().toString().equalsIgnoreCase("Login with Google")) {
					if(MySharedPrefs.INSTANCE.getGoogleEmail() == null) {
//						Toast.makeText(mContext, "1111111111111111111", Toast.LENGTH_SHORT).show();
						googleLoginWithEmailPermission();
//					} else if (tv_google_btn.getText().toString().equalsIgnoreCase("Logout with Google")) {
					}else if(MySharedPrefs.INSTANCE.getGoogleEmail() != null){
//					googlePlusLogoutLocally();
						googlePlusLogout();
					}
				} else {
//				Toast.makeText(mContext, ToastConstant.msgNoInternet, Toast.LENGTH_SHORT).show();
					UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
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
//			Toast.makeText(mContext, "onConnectionFailed", Toast.LENGTH_SHORT).show();
//		if (!result.hasResolution()) {
			if (!mIntentInProgress && signedInUser && result.hasResolution()) {                         //pop up comes when user press on login with google and user click on cancel
//            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
//				Toast.makeText(mContext, "onConnectionFailed11111", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!result.hasResolution()) {
//				GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
//				Toast.makeText(mContext, "onConnectionFailed2222222", Toast.LENGTH_SHORT).show();
				return;

			}
			if (!mIntentInProgress) {
//				Toast.makeText(mContext, "onConnectionFailed33333333", Toast.LENGTH_SHORT).show();
				// store mConnectionResult
				mConnectionResult = result;
				if (signedInUser) {
//					Toast.makeText(mContext, "onConnectionFailed4444444", Toast.LENGTH_SHORT).show();
					resolveSignInError();
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onConnectionFailederror",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		try {
//			Toast.makeText(mContext, "onConnected", Toast.LENGTH_SHORT).show();
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
//			Toast.makeText(mContext, "2222222222222222222", Toast.LENGTH_SHORT).show();
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
//			Toast.makeText(mContext, "333333333333333333", Toast.LENGTH_SHORT).show();
			if (mGoogleApiClient != null) {
//				Toast.makeText(mContext, "444444444444444444", Toast.LENGTH_SHORT).show();
				if (!mGoogleApiClient.isConnecting()) {
//					Toast.makeText(mContext, "555555555555555555", Toast.LENGTH_SHORT).show();
					signedInUser = true;
					resolveSignInError();
				}
//				UtilityMethods.customToast("googlewkwk plus login else", context);
			} else {
//				Toast.makeText(mContext, "66666666666666666", Toast.LENGTH_SHORT).show();
//        	Toast.makeText(context,"google plus login else", Toast.LENGTH_SHORT).show();
//				UtilityMethods.customToast("google plus login else", context);
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","googlePlusLogin",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private void resolveSignInError() {
//		Toast.makeText(context,"88888", Toast.LENGTH_SHORT).show();
		try{
			if(mConnectionResult != null){
				if (mConnectionResult.hasResolution()) {
					try {
//						Toast.makeText(mContext, "a1a1a1a1a1a1a1a1a1a1", Toast.LENGTH_SHORT).show();
						mIntentInProgress = true;
						mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
					} catch (SendIntentException e) {
//						Toast.makeText(mContext, "a2a2a2a2a2a2a2a2a2a2", Toast.LENGTH_SHORT).show();
						mIntentInProgress = false;
						mGoogleApiClient.connect();
					}
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","resolveSignInError",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
//		else{
//		}
	}

	private void getProfileInformation() {
		try {
//			Toast.makeText(mContext, "a3a3a3a3a3a3a3a3a3", Toast.LENGTH_SHORT).show();
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//				tv_google_btn.setText("Logout with Google");
//				Toast.makeText(mContext, "a4a4a4a4a4a4a4a4a4", Toast.LENGTH_SHORT).show();
				Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
				saveGoogleUserData(currentPerson);

				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);



//                UtilityMethods.customToast(personName+email, this);
//                UtilityMethods.customToast(personPhotoUrl, this);

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
			new GrocermaxBaseException("LoginActivity","getProfileInformation",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public static void googlePlusLogout() {
		try{
			if(mGoogleApiClient != null){
				if (mGoogleApiClient.isConnected()) {
					Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
					mGoogleApiClient.disconnect();
					mGoogleApiClient.connect();
//					if(tv_google_btn != null){
//						tv_google_btn.setText("Logout with Google");
//					}
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","googlePlusLogout",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
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
//            	tv_google_btn.setText("Login with Google");
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

			USER_EMAIL = "";
			String USER_NAME = "";

			try{UtilityMethods.clickCapture(context,"Login","","Google","",MySharedPrefs.INSTANCE.getSelectedCity());}catch(Exception e){}

			Registration.facebookName = null;
			MySharedPrefs.INSTANCE.putFacebookName(null);

			USER_NAME = currentPerson.getDisplayName();
			Registration.googleName = USER_NAME;
//		USER_FNAME = currentPerson.getName();
//		USER_MNAME = user.getMiddleName();
//		USER_LNAME = user.getLastName();
			try {
				MySharedPrefs.INSTANCE.putFirstName(USER_NAME);
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

			if (USER_NAME != null && USER_NAME.length() > 0)
				MySharedPrefs.INSTANCE.putGoogleName(USER_NAME);

			if (USER_ID != null && USER_ID.length() > 0)
				MySharedPrefs.INSTANCE.putGoogleId(USER_ID);

			if (USER_EMAIL != null && USER_EMAIL.length() > 0)
				MySharedPrefs.INSTANCE.putGoogleEmail(USER_EMAIL);

			MySharedPrefs.INSTANCE.putUserDataSet(true);
			Registration.fbORgoogle = true;

			if (UtilityMethods.isInternetAvailable(mContext)) {
				showDialog();
				//-----------String url = UrlsConstants.LOGIN_URL+"uemail="+ userN + "&password=" + pwd;
//			String url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ USER_EMAIL + "&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";

				String url = "";
//				HashMap<String, String> hashMap = new HashMap<String,String>();
				JSONObject jsonObject = new JSONObject();
				if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
				{
//					url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ USER_EMAIL + "&quote_id=no&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";
					url = UrlsConstants.GOOGLE_LOGIN_URL;
//					hashMap.put("uemail",USER_EMAIL);
//					hashMap.put("quote_id","no");
//					hashMap.put("fname",USER_NAME);
//					hashMap.put("lname","");
//					hashMap.put("number","0000000000");

					jsonObject.put("uemail",USER_EMAIL);
					jsonObject.put("quote_id","no");
					jsonObject.put("fname",USER_NAME);
					jsonObject.put("lname", "");
					jsonObject.put("number", 0000000000);
//					jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
					System.out.println("==jsonobject==" + jsonObject);
				}else{
//					url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ USER_EMAIL + "&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";
					url = UrlsConstants.GOOGLE_LOGIN_URL;
//					hashMap.put("uemail",USER_EMAIL);
//					hashMap.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
//					hashMap.put("fname",USER_NAME);
//					hashMap.put("lname","");
//					hashMap.put("number","0000000000");

					jsonObject.put("uemail",USER_EMAIL);
					jsonObject.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
					jsonObject.put("fname",USER_NAME);
					jsonObject.put("lname", "");
					jsonObject.put("number", 0000000000);
//					jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
					System.out.println("==jsonobject==" + jsonObject);
				}
                jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                jsonObject.put("device_id",UtilityMethods.getDeviceId(LoginActivity.this));
				myApi.reqLogin(url,jsonObject);
//			String url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getGoogleEmail() + "&fname=" + USER_NAME+"&lname="+""+"&number=0000000000";
//			String url = UrlsConstants.GOOGLE_LOGIN_URL+"uemail="+ MySharedPrefs.INSTANCE.getGoogleEmail() + "&fname=" + USER_NAME+"&number=0000000000";

//				myApi.reqLogin(url);
//				myApi.reqLogin(url, hashMap);


			} else {
//			Toast.makeText(mContext, ToastConstant.msgNoInternet ,Toast.LENGTH_LONG).show();
				UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
			}
		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","saveGoogleUserData",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	/**************************************************  GOOGLE PLUS INTEGARTION *************************************************/

}



/* LOGIN SCENARIOS :
* 1) SIMPLE LOGIN:
*    a) user will move on to home screen OR product listing screen OR description screen (b/c finally call finish) as user also came from following screens.
 *   b) user will move on to product listing screen as user came from the same screen and we are checking the name of screen in BaseActivity Userprofile icon
*
* */