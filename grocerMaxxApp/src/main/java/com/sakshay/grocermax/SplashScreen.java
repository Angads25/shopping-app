package com.sakshay.grocermax;

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

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.LocationDetail;
import com.sakshay.grocermax.bean.LocationListBean;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

public class SplashScreen extends BaseActivity 
{
	private Handler handler;
	private Spinner spinner;

	private TextView txvTitle;
	private TextView txvMessage;
	EasyTracker tracker;
	private Button btnGoShoping;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		addActionsInFilter(MyReceiverActions.LOCATION);
		
		try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sakshay.grocermax", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
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
	
	@Override
	public void onResume() {
		super.onResume();
		String url  = UrlsConstants.GET_LOCATION;
		myApi.reqLocation(url);
//		String url = UrlsConstants.CATEGORY_COLLECTION_LISTING_URL;
//		myApi.reqCategorySubCategoryList(url);
	}

	public void onBackPressed() {

		super.onBackPressed();
		if (handler != null)
			handler.removeCallbacks(runningThread);
	};

	Runnable runningThread = new Runnable() {
		public void run() {
			/*Intent intent;
			intent = new Intent(SplashScreen.this, HomeScreen.class);
			startActivity(intent);
			finish();*/
		}
	};

	@Override
	void OnResponse(Bundle bundle) {
		String action = bundle.getString("ACTION");
		if (action.equals(MyReceiverActions.LOCATION)) {
//			LocationListBean locationBean = (LocationListBean) bundle.getSerializable(ConnectionService.RESPONSE);
			LocationListBean locationBean = (LocationListBean) bundle.getSerializable(ConnectionService.RESPONSE);
			if(locationBean.getFlag().equals("1")) {
				Intent call = new Intent(SplashScreen.this, LocationActivity.class);
				Bundle call_bundle = new Bundle();
				call_bundle.putSerializable("Location", locationBean);
				call.putExtras(call_bundle);
				startActivity(call);
				finish();
			}else{
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
