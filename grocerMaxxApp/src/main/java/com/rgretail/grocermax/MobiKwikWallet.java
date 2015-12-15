package com.rgretail.grocermax;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import com.appsflyer.AppsFlyerLib;
import com.flurry.android.FlurryAgent;
//import com.google.analytics.tracking.android.EasyTracker;
import com.rgretail.grocermax.utils.MyHttpUtils;
import com.rgretail.grocermax.exception.GrocermaxBaseException;

public class MobiKwikWallet extends BaseActivity{
//	http://dev.grocermax.com/webservice/new_services/success.php?orderid=""&txnid=""&status=success
//	String strSuccessUrl = "http://dev.grocermax.com/webservice/new_services/success.php?orderid=";
//    String strFailureUrl = "http://dev.grocermax.com/webservice/new_services/success.php?orderid=";

	@Override
	protected void onStart() {
		super.onStart();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}
		try {
//			EasyTracker.getInstance(this).activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
		}catch(Exception e){
			new GrocermaxBaseException("MobiKwikWallet","onStart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try{
			AppsFlyerLib.setCurrencyCode("INR");
			AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
			AppsFlyerLib.sendTracking(getApplicationContext());
		}catch(Exception e){}
		setContentView(R.layout.activity_wallet_response);
		try {
			Intent intent = getIntent();
			String orderid = intent.getStringExtra("orderid");
			String statuscode = intent.getStringExtra("statuscode");
			String statusmessage = intent.getStringExtra("statusmessage");
			String amount = intent.getStringExtra("amount");

			String msg = "Txn Response from Mobikwik: orderid: " + orderid
					+ " , statuscode: " + statuscode + " , statusmessage: "
					+ statusmessage + " , amount: " + amount;


			EditText txtResponse = (EditText) findViewById(R.id.response);
			txtResponse.setText(msg);

			String strTxnId = "";
			if (intent != null) {
				// Toast.makeText(this, "Failed-ishan" + data.getStringExtra("result"), Toast.LENGTH_LONG).show();
//            showDialog();
//			myApi.reqSetOrderStatus(strUrl+orderid);

//            if(statusmessage.equalsIgnoreCase("0")){
//            	new CallAPI().execute(UrlsConstants.WALLET_SUCCESS_FAILURE+orderid+"&txnid="+strTxnId+"&status=success");
//            }
//            else if(statusmessage.equalsIgnoreCase("1")){
//            	new CallAPI().execute(UrlsConstants.WALLET_SUCCESS_FAILURE+orderid+"&txnid="+strTxnId+"&status=failure");
//            }

			}
		}catch(Exception e){
			new GrocermaxBaseException("MobiKwikWallet","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
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

	@Override
	public void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub
	
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}
	
	
    public class CallAPI extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

				HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				httpGet.setHeader("Content-Type", "application/json");
				HttpResponse response = null;
				try {
					response = client.execute(httpGet);
					HttpEntity resEntity = response.getEntity();
					return EntityUtils.toString(resEntity);
				} catch (ClientProtocolException e) {
					new GrocermaxBaseException("MobiKwikWallet","doInBackground",e.getMessage(),GrocermaxBaseException.CLIENT_PROTOCOL_EXCEPTION,"nodetail");
				} catch (IOException e) {
					new GrocermaxBaseException("MobiKwikWallet","doInBackground",e.getMessage(),GrocermaxBaseException.IO_EXCEPTION,"nodetail");
				}catch(Exception e){
					new GrocermaxBaseException("MobiKwikWallet","doInBackground",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
				}

			return null;
        }


    } 
	
}
