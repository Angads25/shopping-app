package com.sakshay.grocermax;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.sakshay.grocermax.preference.MySharedPrefs;

public class PayTMActivity extends Activity 
{
	public static String THEME = "merchant";
	public static String WEBSITE = "Retailwap";
	public static String PAYTM_CHANNEL_ID = "WAP";
	public static String INDUSTRY_TYPE_ID = "Retail101";
	public static String PAYTM_CUSTOMER_ID = "cust_009";
	public static String PAYTM_MERCHANT_kEY = "A7%mB8UWj7D!JIkj";
	public static String PAYTM_MERCHANT_ID = "grocer28494183264317";
	EasyTracker tracker;
	private int randomInt = 0;
	private PaytmPGService Service = null;
	String amount,order_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.d("LOG", "onCreate of MainActivity");
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.paytm);
		amount=getIntent().getStringExtra("amount");
		order_id=getIntent().getStringExtra("order_id");
	//	Random randomGenerator = new Random();
		//randomInt = randomGenerator.nextInt(1000);
		
		randomInt=getRandomReferenceNumber();
		
		//Service = PaytmPGService.getStagingService();
		Service = PaytmPGService.getProductionService();
		

			/*PaytmMerchant constructor takes two parameters
		1) Checksum generation url
		2) Checksum verification url
		Merchant should replace the below values with his values*/
		randomInt=getRandomReferenceNumber();
		
//	PaytmMerchant merchant = new PaytmMerchant("http://dev.grocermax.com/generateChecksum.php","http://dev.grocermax.com/verifyChecksum.php");
	PaytmMerchant merchant = new PaytmMerchant("http://grocermax.com/generateChecksum.php","http://grocermax.com/verifyChecksum.php");
	        Map<String, String> paramMap = new HashMap<String,String>();

	        //these are mandatory parameters
	       
	        paramMap.put("ORDER_ID", ""+order_id);
	        paramMap.put("MID", PAYTM_MERCHANT_ID);
	        paramMap.put("CUST_ID",MySharedPrefs.INSTANCE.getUserId());
	        paramMap.put("CHANNEL_ID", PAYTM_CHANNEL_ID);
	        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
	        paramMap.put("WEBSITE", WEBSITE);
	        paramMap.put("TXN_AMOUNT", "1");//amount
	        paramMap.put("THEME", THEME);
	        paramMap.put("EMAIL", MySharedPrefs.INSTANCE.getUserEmail());
	        paramMap.put("MOBILE_NO", "9911500574");
						
		PaytmOrder Order = new PaytmOrder(paramMap);

		Service.initialize(Order, merchant,null);
		
		Service.startPaymentTransaction(this,false,false,new PaytmPaymentTransactionCallback() 
		{
	      
	        @Override
	        public void onTransactionSuccess(Bundle inResponse) 
	        {
//	        	Log.i("Error","onTransactionSuccess :"+inResponse);
//	        	String respmsg = inResponse.getString("RESPMSG");
//	        	Log.i("Kushal", respmsg);
//	        	//onTransactionSuccess :Bundle[{GATEWAYNAME=WALLET, PAYMENTMODE=PPI, TXNDATE=2015-02-19 17:01:42.0, STATUS=TXN_SUCCESS, MID=sumjkE62398232705701, CURRENCY=INR, ORDERID=5384643, TXNID=70013, IS_CHECKSUM_VALID=N, TXNAMOUNT=100.00, BANKTXNID=CC9795B5013489B9, BANKNAME=, RESPMSG=Txn Successful., RESPCODE=01, CHECKSUMHASH=8liiSa0uQ0S1lCALiQA3FsyQx6xMey9m8VrF+WZu1tTxG+72c3bU1UYZZg+j/UMS5w9F8iHXq051G4/XtVe4L7FSTk5PGnQpp4r6+QkuyWM=}]
	        	
	        	String strTXNid = inResponse.getString("TXNID");
	        	String strBankTXNid = inResponse.getString("BANKTXNID");
	        	String strOrderId = inResponse.getString("ORDERID");
	        	String strTXNamount = inResponse.getString("TXNAMOUNT");
	        	String strStatus = inResponse.getString("STATUS");
	        	String strTXNtype = inResponse.getString("TXNTYPE");
	        	String strCurrency = inResponse.getString("CURRENCY");
	        	String strGatewayName = inResponse.getString("GATEWAYNAME");
	        	String strResponseCode = inResponse.getString("RESPCODE");
	        	String strResponseMsg = inResponse.getString("RESPMSG");
	        	String strBankName = inResponse.getString("BANKNAME");
	        	String strMID = inResponse.getString("MID");
	        	String strPaymentMode = inResponse.getString("PAYMENTMODE");
	        	String strRefundAmount = inResponse.getString("REFUNDAMT");
	        	String strTXNdate = inResponse.getString("TXNDATE");
	        	String strIsCheckSumValid =  inResponse.getString("IS_CHECKSUM_VALID");
	        		        	
	        	finish();
	        	
	        	System.out.println("==success=="+strTXNid+"="+strBankTXNid+"="+strOrderId+"="+strTXNamount+"="
	        			+strStatus+"="+strTXNtype+"="+strCurrency+"="+strGatewayName+"="+strResponseCode+"="+strResponseMsg+"="
	        			+strBankName+"="+strMID+"="+strPaymentMode+"="+strRefundAmount+"="+strTXNdate+"="+strIsCheckSumValid+"===");
	        	
	        	
	        }

	        @Override
	        public void onTransactionFailure(String inErrorMessage,Bundle inResponse) 
	        {
//	        	Log.i("Error","onTransactionFailure :"+inErrorMessage);
//	        	
//	        	String respmsg = inResponse.getString("RESPMSG");
//	        	
//	        	Log.i("Test", respmsg);
	        	String strTXNid = inResponse.getString("TXNID");
	        	String strBankTXNid = inResponse.getString("BANKTXNID");
	        	String strOrderId = inResponse.getString("ORDERID");
	        	String strTXNamount = inResponse.getString("TXNAMOUNT");
	        	String strStatus = inResponse.getString("STATUS");
	        	String strTXNtype = inResponse.getString("TXNTYPE");
	        	String strCurrency = inResponse.getString("CURRENCY");
	        	String strGatewayName = inResponse.getString("GATEWAYNAME");
	        	String strResponseCode = inResponse.getString("RESPCODE");
	        	String strResponseMsg = inResponse.getString("RESPMSG");
	        	String strBankName = inResponse.getString("BANKNAME");
	        	String strMID = inResponse.getString("MID");
	        	String strPaymentMode = inResponse.getString("PAYMENTMODE");
	        	String strRefundAmount = inResponse.getString("REFUNDAMT");
	        	String strTXNdate = inResponse.getString("TXNDATE");
	        	String strIsCheckSumValid =  inResponse.getString("IS_CHECKSUM_VALID");
	        	
	        	System.out.println(strTXNid+"==failure=="+strBankTXNid+"="+strOrderId+"="+strTXNamount+"="
	        			+strStatus+"="+strTXNtype+"="+strCurrency+"="+strGatewayName+"="+strResponseCode+"="+strResponseMsg+"="
	        			+strBankName+"="+strMID+"="+strPaymentMode+"="+strRefundAmount+"="+strTXNdate+"="+strIsCheckSumValid+"===");
	        	
	        	finish();
	        }

	    
	        @Override
	        public void clientAuthenticationFailed(String inErrorMessage) 
	        {
//	        	Log.i("Error","clientAuthenticationFailed :"+inErrorMessage);
	        }

	    
			@Override
			public void networkNotAvailable() {
				// TODO Auto-generated method stub
				Log.i("Error","networkNotAvailable");
			}

			@Override
			public void onErrorLoadingWebPage(int arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				Log.i("Error","onErrorLoadingWebPage arg0  :"+arg0);
				Log.i("Error","onErrorLoadingWebPage arg1  :"+arg1);
				Log.i("Error","onErrorLoadingWebPage arg2  :"+arg2);
			}

			@Override
			public void someUIErrorOccurred(String arg0) {
				// TODO Auto-generated method stub
				Log.i("Error","someUIErrorOccurred :"+arg0);
			}
       });
	}

	public static int getRandomReferenceNumber() {
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(9000000);
        return n;
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
