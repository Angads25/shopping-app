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
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Tracker;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.google.android.gms.analytics.Tracker;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.HashMap;
import java.util.Map;

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

//			strTempAmount,strTempSelectedState,strTempTotal,strTempTaxAmount

//			shippingamount = ReviewOrderAndPay.strTempAmount;
//			state = bundle.getString("shippingamount");
//			grandtotal = bundle.getString("grandtotal");
//			taxamount = bundle.getString("grandtotal");




			if (ReviewOrderAndPay.cartListGA != null) {
				for (int i = 0; i < ReviewOrderAndPay.cartListGA.size(); i++) {
					try {
						params = new HashMap<String,String>();
						params.put("id", String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getItem_id()));
						params.put("name", String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getProductName()));
						params.put("sku", String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getSku()));
						params.put("category", String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getBrand()));
						params.put("price", String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getPrice()));
						params.put("quantity", String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getQty()));
					}catch(Exception e){
						e.printStackTrace();
						try {
							String str1 = ReviewOrderAndPay.cartListGA.get(i).getItem_id();
							String str2 = ReviewOrderAndPay.cartListGA.get(i).getProductName();
							String str3 = ReviewOrderAndPay.cartListGA.get(i).getSku();
							String str4 = ReviewOrderAndPay.cartListGA.get(i).getBrand();
							String str5 = ReviewOrderAndPay.cartListGA.get(i).getPrice();
							String str6 = String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getQty());
						}catch(Exception w){
							e.printStackTrace();
						}
					}
				}
			}


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

//			String msg="Your order has been successfully placed.Order ID is <b>"+orderid+"</b>.Please check your mail for details.";
				String msg = "Order ID is <b>" + orderid + "</b>";
//				tvOrderId.setText(Html.fromHtml(msg));
				tvOrderIdCode.setText(Html.fromHtml(msg));
//			hint.setText(Html.fromHtml(msg));
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

//				try{UtilityMethods.clickCapture(mContext,"","","","", AppConstants.GA_EVENT_ORDER_SUCCESS);}catch(Exception e){}
//				sendDataToTwoTrackers(params);

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

//			setContentView(R.layout.confirmation_activity);
//			TextView tvSuccess = (TextView) findViewById(R.id.tv_success);
//			TextView tvOrder = (TextView) findViewById(R.id.tv_order);
//			TextView tvOrderId = (TextView) findViewById(R.id.tv_order_id);
//			TextView tvCheckMailDetails = (TextView) findViewById(R.id.tv_check_mail_for_details);
//			tvSuccess.setVisibility(View.GONE);
//			tvOrder.setVisibility(View.GONE);
//			tvOrderId.setVisibility(View.GONE);
//			tvCheckMailDetails.setVisibility(View.GONE);

//			TextView tvOrderFailure = (TextView) findViewById(R.id.order_failure);
//			String msg="Your order has been failed";
//			hint.setText(Html.fromHtml(msg));
//			tvOrderFailure.setText(Html.fromHtml(msg));
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

			try{UtilityMethods.clickCapture(mContext,"","","","", AppConstants.GA_EVENT_ORDER_FAILURE);}catch(Exception e){}
		}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	// Sends the ecommerce data.
	private void sendDataToTwoTrackers(Map<String, String> params) {

//		String PROPERTY_ID = getResources().getString(R.string.ga_trackingId);
//		AnalyticsSampleApp application = (AnalyticsSampleApp)getApplication();
//		application.getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);

//		MyApplication.tracker()

		// Build the transaction.
		sendDataToTwoTrackers(new HitBuilders.TransactionBuilder()
				.setTransactionId(orderid)
				.setAffiliation(ReviewOrderAndPay.strTempSelectedState)
				.setRevenue(Double.parseDouble(ReviewOrderAndPay.strTempTotal))
				.setShipping(Double.parseDouble(ReviewOrderAndPay.strTempShippingAmount))
				.setTax(Double.parseDouble(ReviewOrderAndPay.strTempTaxAmount))
				.setCurrencyCode("INR")
				.build());



// Build an item.
//		sendDataToTwoTrackers(new HitBuilders.ItemBuilder()
//				.setTransactionId(orderid)
//				.setName(getItemName(1))
//				.setSku(getItemSku(1))
//				.setCategory(getItemCategory(1))
//				.setPrice(getItemPrice(getView(), 1))
//				.setQuantity(getItemQuantity(getView(), 1))
//				.setCurrencyCode("INR")
//				.build());



//		AnalyticsSampleApp app = ((AnalyticsSampleApp) getActivity().getApplication());


//		AnalyticsSampleApp app = ((AnalyticsSampleApp) this.getApplication());
//		Tracker appTracker = app.getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);
//		Tracker ecommerceTracker = app.getTracker(AnalyticsSampleApp.TrackerName.ECOMMERCE_TRACKER);
//		appTracker.send(params);
//		ecommerceTracker.send(params);
	}

	// Sends the ecommerce data.
//	private void sendDataToTwoTrackers(Map<String, String> params) {
//
//		HitBuild
//
//		// Build the transaction.
//		sendDataToTwoTrackers(new HitBuilders.TransactionBuilder()
//				.setTransactionId(getOrderId())
//				.setAffiliation(getStoreName())
//				.setRevenue(getTotalOrder())
//				.setTax(getTotalTax())
//				.setShipping(getShippingCost())
//				.setCurrencyCode("INR")
//				.build());
//
//		// Build an item.
//		sendDataToTwoTrackers(new HitBuilders.ItemBuilder()
//				.setTransactionId(getOrderId())
//				.setName(getItemName(1))
//				.setSku(getItemSku(1))
//				.setCategory(getItemCategory(1))
//				.setPrice(getItemPrice(getView(), 1))
//				.setQuantity(getItemQuantity(getView(), 1))
//				.setCurrencyCode("USD")
//				.build());
//
//		// Get tracker.
////		Tracker t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(
////				TrackerName.APP_TRACKER);
////		AnalyticsSampleApp app = ((AnalyticsSampleApp) getActivity().getApplication());
////		CODConfirmation app = ((CODConfirmation) this.getApplication());
////		Tracker appTracker = app.getTracker(TrackerName.APP_TRACKER);
////		Tracker ecommerceTracker = app.getTracker(TrackerName.ECOMMERCE_TRACKER);
////		appTracker.send(params);
////		ecommerceTracker.send(params);
//	}

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

//				Intent intent = new Intent(CODConfirmation.this, HomeScreen.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				finish();
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
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();

//			Intent intent = new Intent(CODConfirmation.this, HomeScreen.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			finish();
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
