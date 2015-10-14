package com.sakshay.grocermax;

import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.OrderHistoryAdapter;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.OrderHistoryBean;
import com.sakshay.grocermax.bean.Orderhistory;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.utils.CustomFonts;

public class OrderHistory extends BaseActivity{
	private String header;
	private ListView mList;
	OrderHistoryBean orderHistoryBean;
	EasyTracker tracker;
	
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
			new GrocermaxBaseException("OrderHistory","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub
		
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
			EasyTracker.getInstance(this).activityStart(this);
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
			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
    }
	
	
	
	
	
}
