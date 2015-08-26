package com.sakshay.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.exception.GrocermaxBaseException;

public class CODConfirmation extends BaseActivity implements OnClickListener{
	
	String orderid = "", status = "";
	EasyTracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Bundle bundle = getIntent().getExtras();
			orderid = bundle.getString("orderid");
			status = bundle.getString("status");

			if (status.equals("success")) {
				setContentView(R.layout.confirmation_activity);
				TextView tvSuccess = (TextView) findViewById(R.id.tv_success);
				TextView tvOrder = (TextView) findViewById(R.id.tv_order);
				TextView tvOrderId = (TextView) findViewById(R.id.tv_order_id);
				TextView tvCheckMailDetails = (TextView) findViewById(R.id.tv_check_mail_for_details);
//			String msg="Your order has been successfully placed.Order ID is <b>"+orderid+"</b>.Please check your mail for details.";
				String msg = "Order ID is <b>" + orderid + "</b>";
				tvOrderId.setText(Html.fromHtml(msg));
//			hint.setText(Html.fromHtml(msg));
				tvSuccess.setVisibility(View.VISIBLE);
				tvOrder.setVisibility(View.VISIBLE);
				tvOrderId.setVisibility(View.VISIBLE);
				tvCheckMailDetails.setVisibility(View.VISIBLE);


			} else {
				setContentView(R.layout.order_failure);
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
			continue_shopping.setOnClickListener(this);
			TextView order_history = (TextView) findViewById(R.id.orderHistory);
			order_history.setOnClickListener(this);

//		 initHeader(findViewById(R.id.header), true, "Order Confirmation");
			initHeader(findViewById(R.id.header), true, null);
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
				Intent intent = new Intent(CODConfirmation.this, HomeScreen.class);
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
		initHeader(findViewById(R.id.header), true, null);
		}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			Intent intent = new Intent(CODConfirmation.this, HomeScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	    	tracker.activityStart(this);
	    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
	    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onStart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	try{
	    	tracker.activityStop(this);
	    	FlurryAgent.onEndSession(this);
    	}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onStop",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }
	
	
	
	

}
