package com.rgretail.grocermax;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.rgretail.grocermax.GCM.GCMClientManager;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.LocationListBean;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import com.google.analytics.tracking.android.EasyTracker;

//import com.google.analytics.tracking.android.EasyTracker;

//https://developers.google.com/analytics/devguides/collection/android/v4/        //ga v4 eg
public class SplashScreen extends BaseActivity 
{
	private Handler handler;

	private TextView txvMessage;
//	EasyTracker tracker;
	private Button btnGoShoping;


	private GCMClientManager pushClientManager;
	private String DeviceRegistrationId;


	public int pxToDp(int px) {
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

        /*screen tracking using rocq*/
        RocqAnalytics.initialize(this);
        RocqAnalytics.startScreen(this);
        /*------------------------------*/

		/*deleting banner images from internal storage*/
		UtilityMethods.deleteBannerDirecort(SplashScreen.this);


        /*registering device on GCM server*/
        if(MySharedPrefs.INSTANCE.getGCMDeviceTocken()==null)
        registerGCM();
        else{
            /*set gcm registration id for Rocq Analytics*/
            try {
                RocqAnalytics.initialize(SplashScreen.this);
                RocqAnalytics.setPushtRegistrationId(MySharedPrefs.INSTANCE.getGCMDeviceTocken(),SplashScreen.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		addActionsInFilter(MyReceiverActions.LOCATION);


		MyApplication application = (MyApplication) getApplication();
		mTracker = application.getDefaultTracker();
				AppsFlyerLib.setCurrencyCode("INR");
//		4
		AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
		AppsFlyerLib.sendTracking(getApplicationContext());        //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�

//		}                                                              //place in onCreate   //6.6  to report�launches�initiated�through�deeplinks

			//////////// AppsFlyer code //////////

		try{
			//UtilityMethods.clickCapture(this,"","","","",SCREENNAME+AppConstants.SPLASH_SCREEN);
		}catch(Exception e){}

//		String sd = String.valueOf(pxToDp(420));
//		System.out.println("======pxdp=====" + sd);
		
		try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.rgretail.grocermax",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
				String str = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
		
		addActionsInFilter(MyReceiverActions.CATEGORY_LIST);


		if (MySharedPrefs.INSTANCE.getUserCity() != null) {

			handler = new Handler();
			handler.postDelayed(runningThread, 2000);
		}

	}

	Runnable runningThread4Minutes = new Runnable() {
		public void run() {
			Intent call = new Intent(SplashScreen.this, HomeScreen.class);
			startActivity(call);
			/*comment by ishan*/
            //registerGCM();
            /*-----*/
			finish();
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
	}

	@Override
	public void onResume() {
		super.onResume();

		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}

			try {
					if (!UtilityMethods.isInternetAvailable(activity)) {                 //exit app after 4 sec
						UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
						handler = new Handler();
						handler.postDelayed(runningThread, 4000);
						return;
					}

					if (MySharedPrefs.INSTANCE.getSelectedCity() != null) {
						if (UtilityMethods.isInternetAvailable(activity)) {                //start app after 4 sec
							handler = new Handler();
							handler.postDelayed(runningThread4Minutes, 4000);
						} else {
							UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
							handler = new Handler();
							handler.postDelayed(runningThread, 4000);
						}
					} else {
                        String url = UrlsConstants.GET_LOCATION;
						if (UtilityMethods.isInternetAvailable(activity)) {             //call location api
							myApi.reqLocation(url);
						} else {
							UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
							handler = new Handler();
							handler.postDelayed(runningThread, 4000);
						}
					}
			}catch(Exception e){
                Log.d("getting reg", e.getMessage());
            }


	}

	public void onBackPressed() {

		super.onBackPressed();
		if (handler != null)
			handler.removeCallbacks(runningThread);
	};

	Runnable runningThread = new Runnable() {
		public void run() {
			finish();

		}
	};

	@Override
	public void OnResponse(Bundle bundle) {
		String action = bundle.getString("ACTION");
		if (action.equals(MyReceiverActions.LOCATION)) {
//			LocationListBean locationBean = (LocationListBean) bundle.getSerializable(ConnectionService.RESPONSE);
			AppConstants.locationBean = (LocationListBean) bundle.getSerializable(ConnectionService.RESPONSE);
			if(AppConstants.locationBean.getFlag().equals("1")) {
                MyApplication.isFromDrawer=false;
				Intent call = new Intent(SplashScreen.this, CityActivity.class);
				Bundle call_bundle = new Bundle();
//				call_bundle.putSerializable("Location", locationBean);
				call_bundle.putSerializable("Location", AppConstants.locationBean);
				call_bundle.putSerializable("FromDrawer", "");
				call.putExtras(call_bundle);
				startActivity(call);
				/*comment by ishan*/
                //registerGCM();
                /*-----*/
				finish();
			}else{
				/*comment by ishan*/
                //registerGCM();
               /*-----*/
				UtilityMethods.customToast(AppConstants.ToastConstant.DATA_NOT_FOUND, mContext);
			}

		}else{                   //category
			/*dismissDialog();
			String jsonResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
			//UtilityMethods.write("response",jsonResponse,SplashScreen.this);
			ArrayList<CategorySubcategoryBean> category = UtilityMethods.getCategorySubCategory(jsonResponse);
			if (!jsonResponse.trim().equals("") && category.size() > 0) {
				UtilityMethods.writeCategoryResponse(SplashScreen.this, AppConstants.categoriesFile, jsonResponse);
				Intent call = new Intent(SplashScreen.this, HomeScreen.class);
				Bundle call_bundle = new Bundle();
				call_bundle.putSerializable("Categories", category);
				call.putExtras(call_bundle);
				startActivity(call);
                *//*comment by ishan*//*
				//registerGCM();
                *//*-----*//*
				finish();
			} else {
				*//*comment by ishan*//*
                //registerGCM();
                *//*-----*//*
				UtilityMethods.customToast(AppConstants.ToastConstant.DATA_NOT_FOUND, mContext);
			}*/
		}

//		String jsonResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
//		//UtilityMethods.write("response",jsonResponse,SplashScreen.this);
//		ArrayList<CategorySubcategoryBean> category = UtilityMethods.getCategorySubCategory(jsonResponse);
//		if (!jsonResponse.trim().equals("") && category.size() > 0) {
//			UtilityMethods.writeCategoryResponse(SplashScreen.this, AppConstants.categoriesFile, jsonResponse);
//			Intent call = new Intent(SplashScreen.this, HomeScreen.class);
//			Bundle call_bundle = new Bundle();
//			call_bundle.putSerializable("Categories", category);
//			call.putExtras(call_bundle);
//			startActivity(call);
//			finish();
//		} else {
//			UtilityMethods.customToast(ToastConstant.DATA_NOT_FOUND, mContext);
//		}

	}

	@Override
	public void onStart() {
		super.onStart();
		AppsFlyerLib.onActivityResume(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		AppsFlyerLib.onActivityPause(this);
        RocqAnalytics.stopScreen(this);
	}

	public void registerGCM() {
		pushClientManager = new GCMClientManager(this, Constants.GCM_SENDER_KEY);
		pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
			@Override
			public void onSuccess(String registrationId,
								  boolean isNewRegistration) {
				DeviceRegistrationId = registrationId;

                /*set gcm registration id for Rocq Analytics*/
                try {
                    RocqAnalytics.initialize(SplashScreen.this);
                    RocqAnalytics.setPushtRegistrationId(registrationId,SplashScreen.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

			@Override
			public void onFailure(String ex) {
				super.onFailure(ex);
				//Show Toast here.
			}
		});

	}





//	class PlayStoreInfn extends AsyncTask<String, String, String> {
//		Context context;
//		String strResponse;
//		String strCheck = "test";
//		boolean bHadResponse = false;
//
//		public PlayStoreInfn(Context mContext) {
//			try {
//				context = mContext;
//			} catch (Exception e) {
//			}
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			try {
//				MarketSession session = new MarketSession();
//				String androidId= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//				session.getContext().setAndroidId(androidId);
//
//
////				String query = "maps";
//				String query = "com.rgretail.grocermax";
//				Market.AppsRequest appsRequest = Market.AppsRequest.newBuilder()
//						.setQuery(query)
//						.setStartIndex(0).setEntriesCount(1)
//						.setWithExtendedInfo(true)
//						.build();
//
//				session.append(appsRequest, new MarketSession.Callback<Market.AppsResponse>() {
//					@Override
//					public void onResult(Market.ResponseContext context, Market.AppsResponse response) {
//						// Your code here
//						// response.getApp(0).getCreator() ...
//						// see AppsResponse class definition for more infos
//						Log.i(context.toString(),response.toString());
//						strResponse = String.valueOf(response);
//						strCheck = response.getApp(0).getRating();
//					}
//				});
//				session.flush();
//
//
////				MarketSession session = new MarketSession();
//////				session.login(email, password);
////				String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
////				androidId = "dead00beef";
////				session.getContext().setAndroidId(androidId);
////
////				String query = "maps";
////				Market.AppsRequest appsRequest = Market.AppsRequest.newBuilder()
////						.setQuery(query)
////						.setStartIndex(0).setEntriesCount(10)
////						.setWithExtendedInfo(true)
////						.build();
////
////				session.append(appsRequest, new MarketSession.Callback<Market.AppsResponse>() {
////					@Override
////					public void onResult(Market.ResponseContext context, Market.AppsResponse response) {
////						bHadResponse = true;
////						Toast.makeText(SplashScreen.this, "response====" + response, Toast.LENGTH_LONG).show();
////						strCheck = response.getApp(0).getRating();
////						strResponse = String.valueOf(response);
////						// Your code here
////						// response.getApp(0).getCreator() ...
////						// see AppsResponse class definition for more infos
////					}
////				});
////
////				if(bHadResponse) {
////					session.flush();
////				}
//
//
////				MarketSession session = new MarketSession();
////
////				Market.CommentsRequest commentsRequest = Market.CommentsRequest.newBuilder()
////						.setAppId("7065399193137006744")
////						.setStartIndex(0)
////						.setEntriesCount(10)
////						.build();
////
////				session.append(commentsRequest, new MarketSession.Callback<Market.CommentsResponse>() {
////					@Override
////					public void onResult(Market.ResponseContext context, Market.CommentsResponse response) {
////						System.out.println("Response : " + response);
////						int rate = response.getComments(0).getRating();
////						// response.getComments(0).getAuthorName()
////						// response.getComments(0).getCreationTime()
////						// ...
////					}
////				});
//
////				session.flush();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			return strResponse;
//		}
//
//		@Override
//		protected void onPostExecute(String str) {
//			super.onPostExecute(str);
//			Toast.makeText(SplashScreen.this, strCheck+"response onpostexecute====" + str, Toast.LENGTH_LONG).show();
//		}
//	}

	}
