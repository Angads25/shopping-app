package com.sakshay.grocermax;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sakshay.grocermax.adapters.ProductListAdapter;
import com.sakshay.grocermax.bean.Product;
import com.sakshay.grocermax.bean.ProductListBean;
import com.sakshay.grocermax.utils.MyHttpUtils;
import com.sakshay.grocermax.utils.UrlsConstants;

public class MobiKwikWallet extends BaseActivity{
//	http://dev.grocermax.com/webservice/new_services/success.php?orderid=""&txnid=""&status=success
//	String strSuccessUrl = "http://dev.grocermax.com/webservice/new_services/success.php?orderid=";
//    String strFailureUrl = "http://dev.grocermax.com/webservice/new_services/success.php?orderid=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_wallet_response);
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
		
		String strTxnId="";
		if(intent != null)
        {
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
	}

	@Override
	void OnResponse(Bundle bundle) {
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
        }
     
        
    } 
	
}
