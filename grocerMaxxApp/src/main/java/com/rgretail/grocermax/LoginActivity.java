package com.rgretail.grocermax;

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
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

//import com.google.analytics.tracking.android.EasyTracker;
//import android.widget.Toast;


public class LoginActivity extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener
{
	ImageView button_facebook;
	LoginButton loginButton;
	TextView button_skip;
	EditText username, password;
	//	ImageView googlePlus;
	private ImageView tv_google_btn;
    ImageView icon_header_back;
	Context context=this;
	//	CheckBox remember_me;
	String QUOTE_ID_AFTER_FB = "";
//	SignInButton signinButton;

	private static final int RC_SIGN_IN = 0;          //
	private static final int FB_SIGN_IN = 64206;
    private static final int SOCIAL_LOGIN_PHONE_NUMBER = 333;
	// Google client to communicate with Google        //
	public static GoogleApiClient mGoogleApiClient;            //
	private boolean mIntentInProgress;                      //
	private boolean signedInUser;                            //
	private ConnectionResult mConnectionResult;
	String USER_EMAIL = "";          //common for facebook and google plus
	private String SCREENNAME = "LoginActivity-";

	CallbackManager callbackManager;

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

			try {

				mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this).
						addConnectionCallbacks(LoginActivity.this).addOnConnectionFailedListener(LoginActivity.this).
						addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
				mGoogleApiClient.connect();
			} catch (Exception e) {
			}

            icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
            icon_header_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

			addActionsInFilter(MyReceiverActions.LOGIN);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART);
			addActionsInFilter(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN);

			TextView register = (TextView) findViewById(R.id.register);

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

			username = (EditText) findViewById(R.id.username);
			password = (EditText) findViewById(R.id.password);
			username.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			password.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			username.setText(MySharedPrefs.INSTANCE.getRememberMeEmail());
			button_facebook = (ImageView) findViewById(R.id.button_facebook);
			button_facebook.setOnClickListener(fb_signin_listener);
			loginButton = (LoginButton)findViewById(R.id.login_button);


			tv_google_btn = (ImageView) findViewById(R.id.button_google);
			tv_google_btn.setOnClickListener(google_signin_listener);

			button_skip = (TextView) findViewById(R.id.login);
			button_skip.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					logIn();
				}
			});

			button_skip.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			callbackManager = CallbackManager.Factory.create();
			loginButton.setReadPermissions("email","user_location");
			loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult loginResult) {
					new_Fb_login(loginResult);
				}

				@Override
				public void onCancel() {
					// App code
				}

				@Override
				public void onError(FacebookException exception) {
					// App code
				}
			});

		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public void new_Fb_login(LoginResult loginResult){
		GraphRequest request = GraphRequest.newMeRequest(
				loginResult.getAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					 public void onCompleted(JSONObject object,GraphResponse response) {
					  try {
						  saveUserData(object);
						  button_facebook.setOnClickListener(fb_sign_out_listener);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				});
		Bundle parameters = new Bundle();
		parameters.putString("fields",
				"id,first_name,email,last_name");
		request.setParameters(parameters);
		request.executeAsync();
	}


	public void gotoHome(View v)
	{
		try{
			Intent intent = new Intent(mContext, HomeScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();

		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","gotoHome",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	/**
	 * facebook sign in listener
	 * */
	public OnClickListener fb_signin_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{

				if (UtilityMethods.isInternetAvailable(mContext)) {
					loginButton.performClick();
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

				if (AccessToken.getCurrentAccessToken() != null) {
					LoginManager.getInstance().logOut();
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
					/*Tracking event for regular login*/
					try{
						UtilityMethods.sendGTMEvent(activity,"Login","Existing User","Android Checkout Funnel");
						UtilityMethods.clickCapture(context,"Login","","Regular","",MySharedPrefs.INSTANCE.getSelectedCity()); /*GA Tracking*/
                        MySharedPrefs.INSTANCE.putLoginMethod("Regular");
						RocqAnalytics.trackEvent("Login", new ActionProperties("Category", "Login", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", "Regular")); /*ROCQ Tracking*/
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
	private void saveUserData(JSONObject object) {
		try{
			String USER_ID = "";
			String USER_FNAME = "";
			String USER_MNAME = "";
			String USER_LNAME = "";

			String USER_NAME = "";

			/*tracking event for facebook login*/
			try{
				UtilityMethods.sendGTMEvent(activity,"Login","Existing User","Android Checkout Funnel");
				UtilityMethods.clickCapture(context, "Login", "", "Facebook", "", MySharedPrefs.INSTANCE.getSelectedCity()); /*GA Tracking*/
				RocqAnalytics.trackEvent("Login", new ActionProperties("Category", "Login", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", "Facebook")); /*ROCQ Tracking*/

			}catch(Exception e){}

            MySharedPrefs.INSTANCE.putLoginMethod("Social");


			Registration.googleName = null;
			MySharedPrefs.INSTANCE.putGoogleName(null);

			USER_FNAME = object.getString("first_name");
			USER_MNAME = object.optString("middle_name");
			USER_LNAME = object.getString("last_name");

			try{
				MySharedPrefs.INSTANCE.putFirstName(USER_FNAME);
				MySharedPrefs.INSTANCE.putLastName(USER_LNAME);
			}catch(Exception e){}

			try {
				//USER_EMAIL = user.getProperty("email").toString();
				USER_EMAIL = object.getString("email");
			} catch (Exception e) {
				Log.e("ERROR", "Enable to get email");
			}

			//USER_ID = user.getId();
			USER_ID=object.getString("id");

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
					url = UrlsConstants.FB_LOGIN_URL;
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
					jsonObject.put("uemail",MySharedPrefs.INSTANCE.getFacebookEmail());
					jsonObject.put("quote_id",MySharedPrefs.INSTANCE.getQuoteId());
					jsonObject.put("fname",USER_FNAME);
					jsonObject.put("lname", USER_LNAME);
					jsonObject.put("number", 0000000000);
				}
                jsonObject.put("otp", "0");
                jsonObject.put("device_token",MySharedPrefs.INSTANCE.getGCMDeviceTocken());
                jsonObject.put("device_id",UtilityMethods.getDeviceId(LoginActivity.this));
				myApi.reqLogin(url,jsonObject);


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
						callbackManager.onActivityResult(requestCode, resultCode, data);
					}catch(Exception e){
						new GrocermaxBaseException("LoginActivity","OnResponseFbSignIn",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
					}
			}

			if(requestCode==111)
			{
                if(resultCode==1221){

                }else{
				if(resultCode==RESULT_OK)
					setResult(RESULT_OK);
				finish();
                }
			}

            /*Handing response from PhoneNumberforOTP activity page for social login otp generation*/
            if(requestCode==SOCIAL_LOGIN_PHONE_NUMBER){
                Intent intent = new Intent(mContext, HomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

		}catch(Exception e){
			new GrocermaxBaseException("LoginActivity","onActivityResult",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}

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
					if (USER_EMAIL.equals("")) {
						MySharedPrefs.INSTANCE.putUserEmail(username.getText().toString().trim());
						if(userDataBean.getQuoteId() != null && !userDataBean.getQuoteId().equals("") ) {
							MySharedPrefs.INSTANCE.clearQuote();
							MySharedPrefs.INSTANCE.putQuoteId(userDataBean.getQuoteId());
						}
					} else {
						MySharedPrefs.INSTANCE.putUserEmail(USER_EMAIL.trim());
						if(QUOTE_ID_AFTER_FB != "") {
							MySharedPrefs.INSTANCE.clearQuote();
							MySharedPrefs.INSTANCE.putQuoteId(QUOTE_ID_AFTER_FB);
						}
					}

					/*save gcm token to our server*/
					try {
						saveGcmTokenTOServer();
					} catch (Exception e) {
						e.printStackTrace();
					}

					MySharedPrefs.INSTANCE.putLoginStatus(true);
					if(userDataBean.getQuoteId() != null && !userDataBean.getQuoteId().equals("")) {
						MySharedPrefs.INSTANCE.clearQuote();
						MySharedPrefs.INSTANCE.putQuoteId(userDataBean.getQuoteId());/////////last change
					}
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(userDataBean.getTotalItem()));

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

				}else if(userDataBean.getFlag().equalsIgnoreCase("2")){   /*this is for opening Phonenumberfor Otp page from social login*/

                    if(MySharedPrefs.INSTANCE.getFirstName() == null){//if(MySharedPrefs.INSTANCE.getFirstName() != null){    //changed 17/9/15
                        MySharedPrefs.INSTANCE.putFirstName(userDataBean.getFirstName());
                    }
                    if(MySharedPrefs.INSTANCE.getLastName() == null){//if(MySharedPrefs.INSTANCE.getLastName() != null){      //changed 17/9/15
                        MySharedPrefs.INSTANCE.putLastName(userDataBean.getLastName());
                    }
                    MySharedPrefs.INSTANCE.putUserEmail(USER_EMAIL.trim());
                    Intent i=new Intent(LoginActivity.this,PhoneNumberForOTP.class);
                    startActivityForResult(i,SOCIAL_LOGIN_PHONE_NUMBER);

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
            System.out.println("Ishan onPAuse");
		}catch(Exception e){}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
        System.out.println("Ishan onStop");
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
		try {
			RocqAnalytics.stopScreen(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**************************************************  GOOGLE PLUS INTEGARTION *************************************************/

	/**
	 * google sign in listener
	 * */
	OnClickListener google_signin_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {

				if (UtilityMethods.isInternetAvailable(mContext)) {
					if(MySharedPrefs.INSTANCE.getGoogleEmail() == null) {
						googleLoginWithEmailPermission();
					}else if(MySharedPrefs.INSTANCE.getGoogleEmail() != null){
						//googlePlusLogout();
                        googleLoginWithEmailPermission();
					}
				} else {
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
            System.out.println("Ishan onConnected");
			AppLoadingScreen.getInstance(context).dismissDialog();
			signedInUser = false;
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
            System.out.println("Ishan onConnection suspended");
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
			if (mGoogleApiClient != null) {
				if (!mGoogleApiClient.isConnecting()) {
					signedInUser = true;
					resolveSignInError();
				}
			} else {
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
					//mGoogleApiClient.connect();

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

			/*Tracking event for google login*/
			try{
				UtilityMethods.sendGTMEvent(activity,"Login","Existing User","Android Checkout Funnel");
				UtilityMethods.clickCapture(context, "Login", "", "Google", "", MySharedPrefs.INSTANCE.getSelectedCity()); /*GA Tracking*/
				RocqAnalytics.trackEvent("Login",new ActionProperties("Category","Login","Action",MySharedPrefs.INSTANCE.getSelectedCity(),"Label","Google")); /*ROCQ Tracking*/

			}catch(Exception e){}
            MySharedPrefs.INSTANCE.putLoginMethod("Social");
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
                jsonObject.put("otp", "0");
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