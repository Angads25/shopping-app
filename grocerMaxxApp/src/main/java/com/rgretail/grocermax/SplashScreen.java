package com.rgretail.grocermax;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.LocationListBean;
import com.rgretail.grocermax.hotoffers.HotOffersActivity;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;
import com.rgretail.grocermax.GCM.GCMClientManager;

import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.utils.UrlsConstants;

public class SplashScreen extends BaseActivity 
{
	private Handler handler;
	private Spinner spinner;

	private TextView txvTitle;
	private TextView txvMessage;
	EasyTracker tracker;
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
		System.out.println("======pxdp=====");
		addActionsInFilter(MyReceiverActions.LOCATION);

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

		spinner = (Spinner) findViewById(R.id.spinner1);

		txvTitle = (TextView) findViewById(R.id.txvTitle);
		txvMessage = (TextView) findViewById(R.id.txvMessage);
		btnGoShoping = (Button) findViewById(R.id.btnGoShoping);

		if (MySharedPrefs.INSTANCE.getUserCity() != null) {
			spinner.setVisibility(View.INVISIBLE);
			btnGoShoping.setVisibility(View.VISIBLE);

			handler = new Handler();
			handler.postDelayed(runningThread, 2000);
		}

		String strTitle = "<html><br><br><font color=\"red\">* Coming soon to Delhi NCR</font></html>"; // "<html><font color=\"black\">Choose your Location.</font><font color=\"red\">*</font></html>";
		Spanned spanned = Html.fromHtml(strTitle, null, null);
		txvTitle.setText(spanned);

		txvMessage
				.setText(Html
						.fromHtml("<html><font color=\"black\">Easy Shoping, Great Value, Friendly Service<br><br>Currently delivering in Gurgaon.</font></html>"));

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.location_spinner_item, AppConstants.placesList);
		spinner.setAdapter(dataAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position > 0) {
					MySharedPrefs.INSTANCE.putUserCity(AppConstants.placesList
							.get(position));
					handler = new Handler();
					handler.postDelayed(runningThread, 500);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnGoShoping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MySharedPrefs.INSTANCE.putUserCity(AppConstants.placesList.get(1));
				handler = new Handler();
				handler.postDelayed(runningThread, 500);

			}
		});
	}

	Runnable runningThread4Minutes = new Runnable() {
		public void run() {
			Intent call = new Intent(SplashScreen.this, HotOffersActivity.class);
			startActivity(call);
			registerGCM();
			finish();
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();

		if(!UtilityMethods.isInternetAvailable(activity)) {                 //exit app after 4 sec
			UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
			handler = new Handler();
			handler.postDelayed(runningThread, 4000);
			return;
		}
		if(MySharedPrefs.INSTANCE.getSelectedCity() != null){
//			showDialog();
			String url = UrlsConstants.CATEGORY_COLLECTION_LISTING_URL;
			if(UtilityMethods.isInternetAvailable(activity)) {                //start app after 4 sec
//				myApi.reqCategorySubCategoryList(url);
				handler = new Handler();
				handler.postDelayed(runningThread4Minutes, 4000);
			}else{
				UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
				handler = new Handler();
				handler.postDelayed(runningThread, 4000);
			}
		}else {
			String url = UrlsConstants.GET_LOCATION;
			if(UtilityMethods.isInternetAvailable(activity)) {             //call location api
				myApi.reqLocation(url);
			}else{
				UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
				handler = new Handler();
				handler.postDelayed(runningThread, 4000);
			}
		}

//		if(MySharedPrefs.INSTANCE.getSelectedCity() != null){
//			Intent call = new Intent(SplashScreen.this, HotOffersActivity.class);
////			Bundle call_bundle = new Bundle();
////			call_bundle.putSerializable("Categories", category);
////			call.putExtras(call_bundle);
//			startActivity(call);
//			registerGCM();
//			finish();
//		}else{
//			String url = UrlsConstants.GET_LOCATION;
//			if(UtilityMethods.isInternetAvailable(activity)) {
//				myApi.reqLocation(url);
//			}else{
//				UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
//				handler = new Handler();
//				handler.postDelayed(runningThread, 4000);
//			}
//		}

	}

	public void onBackPressed() {

		super.onBackPressed();
		if (handler != null)
			handler.removeCallbacks(runningThread);
	};

	Runnable runningThread = new Runnable() {
		public void run() {
			finish();
			/*Intent intent;
			intent = new Intent(SplashScreen.this, HomeScreen.class);
			startActivity(intent);
			finish();*/
//			registerGCM();
		}
	};

	@Override
	public void OnResponse(Bundle bundle) {
		String action = bundle.getString("ACTION");
		if (action.equals(MyReceiverActions.LOCATION)) {
//			LocationListBean locationBean = (LocationListBean) bundle.getSerializable(ConnectionService.RESPONSE);
			AppConstants.locationBean = (LocationListBean) bundle.getSerializable(ConnectionService.RESPONSE);
			if(AppConstants.locationBean.getFlag().equals("1")) {

				Intent call = new Intent(SplashScreen.this, LocationActivity.class);
				Bundle call_bundle = new Bundle();
//				call_bundle.putSerializable("Location", locationBean);
				call_bundle.putSerializable("Location", AppConstants.locationBean);
				call_bundle.putSerializable("FromDrawer", "");
				call.putExtras(call_bundle);
				startActivity(call);
				registerGCM();
				finish();
			}else{
				registerGCM();
				UtilityMethods.customToast(AppConstants.ToastConstant.DATA_NOT_FOUND, mContext);
			}

		}else{                   //category
			dismissDialog();
			String jsonResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
			//UtilityMethods.write("response",jsonResponse,SplashScreen.this);
			ArrayList<CategorySubcategoryBean> category = UtilityMethods.getCategorySubCategory(jsonResponse);
			if (!jsonResponse.trim().equals("") && category.size() > 0) {
				UtilityMethods.writeCategoryResponse(SplashScreen.this, AppConstants.categoriesFile, jsonResponse);
				Intent call = new Intent(SplashScreen.this, HotOffersActivity.class);
				Bundle call_bundle = new Bundle();
				call_bundle.putSerializable("Categories", category);
				call.putExtras(call_bundle);
				startActivity(call);
				registerGCM();
				finish();
			} else {
				registerGCM();
				UtilityMethods.customToast(AppConstants.ToastConstant.DATA_NOT_FOUND, mContext);
			}
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
	
//	@Override
//    protected void onStart() {
//    	// TODO Auto-generated method stub
//    	super.onStart();
//    	try{
//			EasyTracker.getInstance(this).activityStart(this);
////	    	tracker.activityStart(this);
//	    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
//	    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
//    	}catch(Exception e){}
//    }
    
//    @Override
//    protected void onStop() {
//    	// TODO Auto-generated method stub
//    	super.onStop();
//    	try{
//	    	tracker.activityStop(this);
//	    	FlurryAgent.onEndSession(this);
//    	}catch(Exception e){}
//    }

	public void registerGCM() {
		pushClientManager = new GCMClientManager(this, Constants.GCM_SENDER_KEY);
		pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
			@Override
			public void onSuccess(String registrationId,
								  boolean isNewRegistration) {
				DeviceRegistrationId = registrationId;
//				new PreferenceHelper(HoverSplashActivity.this)
//						.putDeviceToken(registrationId);,
//				Toast.makeText(SplashScreen.this,"RegistrationScreen"+DeviceRegistrationId,Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(String ex) {
				super.onFailure(ex);
				//Show Toast here.
			}
		});

	}



	}
