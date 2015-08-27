package com.sakshay.grocermax;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.AddressListAdapter;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.AddressList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

public class AddressDetail extends BaseActivity{

	AddressList address_bean;
	ArrayList<Address> mAddressList;
	ListView mList;
	AddressListAdapter mAdapter;
	private int requestNewAddress = 111;
	public static int delete_address_position;
	EasyTracker tracker;
	TextView tvAddressHeader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_details);
		try {
			addActionsInFilter(MyReceiverActions.DELETE_ADDRESS);
			Bundle bundle = getIntent().getExtras();
			tvAddressHeader = (TextView) findViewById(R.id.tv_address_header);
			tvAddressHeader.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			if (bundle != null) {
				address_bean = (AddressList) bundle.getSerializable("AddressList");
				mAddressList = address_bean.getAddress();
			}

			if (mAddressList != null && mAddressList.size() > 0) {
				if (mAddressList.size() == 1 && mAddressList.get(0).getFirstname().equals("")) {
					TextView msg = (TextView) findViewById(R.id.msg);
					msg.setVisibility(View.VISIBLE);
					msg.setText("You dont have any address saved. Please add a new address");
				} else {
					mList = (ListView) findViewById(R.id.address_list);
					mAdapter = new AddressListAdapter(AddressDetail.this, mAddressList);
					mList.setAdapter(mAdapter);
				}
			} else {
				TextView msg = (TextView) findViewById(R.id.msg);
				msg.setVisibility(View.VISIBLE);
				msg.setText("You dont have any address saved. Please add a new address");
			}

			Button create_address = (Button) findViewById(R.id.button_create_address);
			create_address.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToAddress(null);
				}
			});

			create_address.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			addActionsInFilter(MyReceiverActions.ADD_ADDRESS);
			findViewById(R.id.footer).setVisibility(View.GONE);
			initHeader(findViewById(R.id.header), true, "My Addresses");
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","onCreate",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");

		}
	}
	
	public void goToAddress(Address address)
	{
		try{
			Intent intent = new Intent(mContext, CreateNewAddress.class);
			intent.putExtra("address", address);
			startActivityForResult(intent, requestNewAddress);
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","goToAddress",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	public void deleteAddress(Address address,int position)
	{
		askConfirmAlert(address,position);
	}
	private void askConfirmAlert(final Address address,final int position) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("GrocerMax");
			builder.setMessage("Do you want to delete this address?");
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							delete_address_position = position;
							showDialog();
							myApi.reqDeleteAddress(UrlsConstants.DELETE_ADDRESS + address.getCustomer_address_id());
						}
					});
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			builder.show();
		}
		catch(Exception e){
			new GrocermaxBaseException("AddressDetail","askConfirmAlert",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
	void OnResponse(Bundle bundle) {
		BaseResponseBean baseResponseBean = null;
		try {
			if (bundle.getString("ACTION").equals(MyReceiverActions.DELETE_ADDRESS)) {
				dismissDialog();
				baseResponseBean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (baseResponseBean.getFlag().equalsIgnoreCase("1")) {
					UtilityMethods.customToast(ToastConstant.ADDR_DELETED, mContext);
					mAddressList.remove(delete_address_position);
					mAdapter = new AddressListAdapter(AddressDetail.this, mAddressList);
					mList.setAdapter(mAdapter);
				} else {
					UtilityMethods.customToast(baseResponseBean.getResult(), mContext);
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,baseResponseBean.getResult());
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == requestNewAddress && resultCode == RESULT_OK) {
				if (mList == null) {
					findViewById(R.id.msg).setVisibility(View.GONE);
					mList = (ListView) findViewById(R.id.address_list);
					mAddressList = ((AddressList) data.getSerializableExtra("addressBean")).getAddress();
					mAdapter = new AddressListAdapter(AddressDetail.this, mAddressList);
					mList.setAdapter(mAdapter);
				} else {
					mAddressList = ((AddressList) data.getSerializableExtra("addressBean")).getAddress();
					mAdapter.updateList(mAddressList);
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			initHeader(findViewById(R.id.header), true, "My Addresses");
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
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
			new GrocermaxBaseException("AddressDetail","OnStart",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
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
			new GrocermaxBaseException("AddressDetail","OnStop",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }

}
