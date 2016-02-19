package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.Map;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Tracker;
//import com.google.analytics.tracking.android.EasyTracker;

public class CODConfirmation extends BaseActivity implements OnClickListener{
	
	String orderid = "", status = "";
//	EasyTracker tracker;
	Map<String, String> params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
//			((AnalyticsSampleApp) getApplication()).getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);
			AppsFlyerLib.setCurrencyCode("INR");
			AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");
			AppsFlyerLib.sendTracking(getApplicationContext());
		}catch(Exception e){}

		try{
				UtilityMethods.screenView(this,"CODConfirmation");
			}catch(Exception e){}

		try {
			Bundle bundle = getIntent().getExtras();
			orderid = bundle.getString("orderid");
			status = bundle.getString("status");

			if (status.equals("success")) {
				setContentView(R.layout.confirmation_activity);
				TextView tvSuccess = (TextView) findViewById(R.id.tv_success);
				TextView tvOrder = (TextView) findViewById(R.id.tv_order);
				TextView tvOrderId = (TextView) findViewById(R.id.tv_order_id);
				TextView tvOrderIdCode = (TextView) findViewById(R.id.tv_order_id_code);
				TextView tvCheckMailDetails = (TextView) findViewById(R.id.tv_check_mail_for_details);
				TextView tvTellYourFriends = (TextView) findViewById(R.id.tv_tell_your_friends);

				//////code added by ishan///////
				if(orderid.equals("no_order_id")){
					LinearLayout ll=(LinearLayout)findViewById(R.id.ll);
					ll.setVisibility(View.GONE);
					tvCheckMailDetails.setText(getResources().getString(R.string.no_order_id_success_message));
				}
				/////////////////////////////

				String msg = "<b>" + orderid + "</b>";
				tvOrderIdCode.setText(Html.fromHtml(msg));
				tvSuccess.setVisibility(View.VISIBLE);
				tvOrder.setVisibility(View.VISIBLE);
				tvOrderId.setVisibility(View.VISIBLE);
				tvOrderIdCode.setVisibility(View.VISIBLE);
				tvCheckMailDetails.setVisibility(View.VISIBLE);

				tvSuccess.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvOrder.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvOrderId.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvOrderIdCode.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvOrderIdCode.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
				tvCheckMailDetails.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvTellYourFriends.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

                /*Tracking the GA event for successful payment*/
				try{UtilityMethods.clickCapture(mContext,"Order Successful","","","", MySharedPrefs.INSTANCE.getSelectedCity());}catch(Exception e){}
                /*-------------------------------------------*/

                /*Tracking the GA ecommerce for successful transaction*/
               /* if(orderid.equals("")){

                }


                try{
                    System.out.println("Total="+ReviewOrderAndPay.strTempTotal);
                    UtilityMethods.transactionCapture(mContext, orderid, "Grocermax Store", ReviewOrderAndPay.strTempTotal,
                            ReviewOrderAndPay.strTempShippingAmount, ReviewOrderAndPay.strTempTaxAmount);
                }catch(Exception e){
                    e.printStackTrace();
                }*/
                /*-------------------------------------------*/

                   /*Tracking the GA ecommerce for Items in a success order transaction*/

              /*  if (ReviewOrderAndPay.cartListGA != null) {
                    for (int i = 0; i < ReviewOrderAndPay.cartListGA.size(); i++) {
                        try {
                            try{
                                System.out.println("Name="+ReviewOrderAndPay.cartListGA.get(i).getProductName());
                                UtilityMethods.captureItemsInAOrder(mContext, orderid,ReviewOrderAndPay.cartListGA.get(i).getProductName(),
                                        ReviewOrderAndPay.cartListGA.get(i).getSku(), ReviewOrderAndPay.cartListGA.get(i).getBrand(),
                                        ReviewOrderAndPay.cartListGA.get(i).getPrice(),String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getQty()));
                               *//* UtilityMethods.captureItemsInAOrder(mContext, orderid,"Basmati RIce",
                                        "SKU-15RICE", "Brand-Rajdhani",
                                        "500.50","2");*//*
                            }catch(Exception e){}
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }*/
                  /*-------------------------------------------------*/




			} else {
				setContentView(R.layout.order_failure);
				TextView tvPaymentFailed = (TextView) findViewById(R.id.tv_payment_failed);
				TextView tvFailureMsg = (TextView) findViewById(R.id.tv_failure_msg);
				TextView tvFailureMsg2 = (TextView) findViewById(R.id.tv_failure_msg_part2);
				TextView tvContinueBtn = (TextView) findViewById(R.id.continueButton);
				TextView tvOrderHistory = (TextView) findViewById(R.id.orderHistory);

				tvPaymentFailed.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvFailureMsg.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvFailureMsg2.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvContinueBtn.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				tvOrderHistory.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

               /*Tracking the GA event for Failure payment*/
                try{UtilityMethods.clickCapture(mContext,"Order Failed","","","", MySharedPrefs.INSTANCE.getSelectedCity());}catch(Exception e){}
                /*-------------------------------------------*/
			}

			addActionsInFilter(MyReceiverActions.ORDER_HISTORY);
			TextView continue_shopping = (TextView) findViewById(R.id.continueButton);
			continue_shopping.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			continue_shopping.setOnClickListener(this);
			TextView order_history = (TextView) findViewById(R.id.orderHistory);
			order_history.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			order_history.setOnClickListener(this);

//		 initHeader(findViewById(R.id.header), true, "Order Confirmation");
			initHeader(findViewById(R.id.header), true, null);

		//	try{UtilityMethods.clickCapture(mContext,"","","","", AppConstants.GA_EVENT_ORDER_FAILURE);}catch(Exception e){}
		}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}


	@Override
	public void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		try{
		switch(v.getId())
		{
			case R.id.continueButton:
				Intent intent = new Intent(mContext, HomeScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();

				break;
			case R.id.orderHistory:
				openOrderHistory();
				break;
		}
		}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onClick",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}
		try{
			initHeader(findViewById(R.id.header), true, null);
		}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			Intent intent = new Intent(mContext, HomeScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onBackPressed",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
		try{
//			GoogleAnalytics.getInstance(this).reportActivityStart(this);
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}
    	try{
//			EasyTracker.getInstance(this).activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){
		}
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
		try{
			GoogleAnalytics.getInstance(this).reportActivityStop(this);
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
    	try{
//			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
    	}catch(Exception e){
		}
    }
	
	
	
	

}
