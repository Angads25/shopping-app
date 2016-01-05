package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.adapters.OrderHistoryAdapter;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.OrderHistoryBean;
import com.rgretail.grocermax.bean.Orderhistory;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

//import com.google.analytics.tracking.android.EasyTracker;

public class OrderHistory extends BaseActivity{
	private String header;
	private ListView mList;
	OrderHistoryBean orderHistoryBean;
//	EasyTracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		Bundle bundle = getIntent().getExtras();
		if(bundle != null)
		{
			orderHistoryBean = (OrderHistoryBean) bundle.getSerializable("OrderHistory");
			header = bundle.getString("Header");
		}
		setContentView(R.layout.order_history);
		
		TextView tvHeaderOrderHistory = (TextView) findViewById(R.id.msg);
		tvHeaderOrderHistory.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

		addActionsInFilter(MyReceiverActions.ORDER_HISTORY);
		addActionsInFilter(MyReceiverActions.ORDER_REORDER);
		addActionsInFilter(MyReceiverActions.VIEW_CART);
		
		if(orderHistoryBean.getOrderhistory() != null && orderHistoryBean.getOrderhistory().size() > 0){
			mList = (ListView) findViewById(R.id.category_list);
			List<Orderhistory> orderList=orderHistoryBean.getOrderhistory();
			Collections.sort(orderList,Orderhistory.dateTime);                      //2015-07-02 10:14:41
			OrderHistoryAdapter mAdapter = new OrderHistoryAdapter(OrderHistory.this, orderList);
			mList.setAdapter(mAdapter);
			mList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
					Intent order_detail=new Intent(OrderHistory.this,OrderDetail.class);
					order_detail.putExtra("order_id", orderHistoryBean.getOrderhistory().get(position).getOrder_id());  //order id as in his adapter using same values.
					order_detail.putExtra("order_increement_id", orderHistoryBean.getOrderhistory().get(position).getIncrement_id());
					startActivity(order_detail);
				}
			});
		}
		else
		{
			TextView msg = (TextView) findViewById(R.id.msg);
			msg.setVisibility(View.VISIBLE);
			msg.setText("Your order history is empty");
		}
		
		initHeader(findViewById(R.id.header), true, "Order History");
		}catch(Exception e){
			new GrocermaxBaseException("OrderHistory","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}

	}


	/////code added by Ishan///////////
	public void reOrderItems(String order_id){

        /*tracking event for reorder*/
        try{
            UtilityMethods.clickCapture(activity,"Profile Activity","","Reorder","",MySharedPrefs.INSTANCE.getSelectedCity());
        }catch(Exception e){
            e.printStackTrace();
        }
        /*------------------------*/

		showDialog();
		myApi.reqReorder(UrlsConstants.ORDER_REORDER_URL + order_id);

	}
	///////end code here////////////////

	@Override
	public void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub
		////code added by Ishan//////////////
		dismissDialog();
		if (bundle.getString("ACTION").equals(MyReceiverActions.ORDER_REORDER)) {
			try
			{
				String quoteResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
				//System.out.println("reorder Response = "+quoteResponse);
				JSONObject reOrderJSON=new JSONObject(quoteResponse);
				if(reOrderJSON.getInt("flag")==1){
					String quote_id=reOrderJSON.getString("QuoteId");
					MySharedPrefs.INSTANCE.putQuoteId(quote_id);
					showDialog();
					String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
					myApi.reqViewCart(url);

				}else{
					UtilityMethods.customToast(com.rgretail.grocermax.utils.Constants.ToastConstant.QUOTE_FAIL, OrderHistory.this);
				}
			}catch(Exception e)
			{
				new GrocermaxBaseException("Reorder","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"no quote id");
			}
		}
		/////////////////code ends here/////////////////////////////
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			initHeader(findViewById(R.id.header), true, "Order History");
		}catch(Exception e){
			new GrocermaxBaseException("OrderHistory","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
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
//			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
    }
	
	
	
	
	
}
