package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.invitereferrals.invitereferrals.InviteReferralsApi;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

import java.util.Date;
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

				tvTellYourFriends.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						InviteReferralsApi.getInstance(CODConfirmation.this).inline_btn(Integer.parseInt(MySharedPrefs.INSTANCE.getInviteReferralId()));
					}
				});

                /*Tracking the GA event for successful payment*/
				try{
					UtilityMethods.clickCapture(mContext,"Order Successful","","","", MySharedPrefs.INSTANCE.getSelectedCity());
					UtilityMethods.sendGTMEvent(CODConfirmation.this,"Order Successful",MySharedPrefs.INSTANCE.getUserEmail(),"Android Checkout Funnel");

					/*QGraph event*/
					JSONObject json=new JSONObject();
					json.put("Payment Option",ReviewOrderAndPay.payment_mode);
					json.put("Sub Total",ReviewOrderAndPay.strTempTotal);
					if(MySharedPrefs.INSTANCE.getUserId()!=null)
						json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
					UtilityMethods.setQGraphevent("Android Checkout Funnnel - Order Successful",json);
                   /*--------------*/

				}catch(Exception e){}
                /*-------------------------------------------*/

                /*Tracking the GA ecommerce for successful transaction*/
                if(orderid.equals("no_order_id")){
                  orderid=MySharedPrefs.INSTANCE.getUserId()+"-"+new Date();
                }

				//Send mail to fererrer using invite referral
				System.out.println("Success Transaction = "+orderid);
				Log.e("Invite Referral","Tracking start");
				InviteReferralsApi.getInstance(this).track_fp(null);
				InviteReferralsApi.getInstance(this).tracking("sale", (String)orderid, (int)Float.parseFloat(ReviewOrderAndPay.strTempTotal.replace(",","")));
				Log.e("Invite Referral","Tracking finish");


				/*Tracking the GA ecommerce for successful transaction*/
                try{
                    System.out.println("Total="+ReviewOrderAndPay.strTempTotal);
                    UtilityMethods.transactionCapture(mContext, orderid, "Grocermax Store", ReviewOrderAndPay.strTempTotal,
                            ReviewOrderAndPay.strTempShippingAmount, ReviewOrderAndPay.strTempTaxAmount);
                }catch(Exception e){
                    e.printStackTrace();
                }
                /*-------------------------------------------*/

                   /*Tracking the GA ecommerce for Items in a success order transaction*/

                if (ReviewOrderAndPay.cartListGA != null) {
                    for (int i = 0; i < ReviewOrderAndPay.cartListGA.size(); i++) {
                        try {
                            try{
                                //System.out.println("Name=" + ReviewOrderAndPay.cartListGA.get(i).getName());
								UtilityMethods.captureItemsInAOrder(mContext, orderid,ReviewOrderAndPay.cartListGA.get(i).getName(),
                                        ReviewOrderAndPay.cartListGA.get(i).getSku(), ReviewOrderAndPay.cartListGA.get(i).getBrand(),
                                        ReviewOrderAndPay.cartListGA.get(i).getPrice(),String.valueOf(ReviewOrderAndPay.cartListGA.get(i).getQty()));
                            }catch(Exception e){}
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
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
                try{
					UtilityMethods.clickCapture(mContext,"Order Failed","","","", MySharedPrefs.INSTANCE.getSelectedCity());
					UtilityMethods.sendGTMEvent(CODConfirmation.this,"Order Failure",MySharedPrefs.INSTANCE.getUserEmail(),"Android Checkout Funnel");
				/*QGraph event*/
					JSONObject json=new JSONObject();
					json.put("Payment Option",ReviewOrderAndPay.payment_mode);
					json.put("Sub Total",ReviewOrderAndPay.strTempTotal);
					if(MySharedPrefs.INSTANCE.getUserId()!=null)
						json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
					UtilityMethods.setQGraphevent("Android Checkout Funnnel - Order Failure",json);
                   /*--------------*/

				}catch(Exception e){}
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
			initHeader(findViewById(R.id.header), true, null);
		}catch(Exception e){
			new GrocermaxBaseException("CODConfirmation","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

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

       /*------------------------------*/
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();

    }
	
	
	
	

}
