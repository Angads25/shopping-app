package com.rgretail.grocermax;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rgretail.grocermax.adapters.AddressListAdapter;
import com.rgretail.grocermax.api.BillingStateCityLoader;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.api.ShippingLocationLoader;
import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.bean.AddressList;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Tracker;

public class AddressDetail extends BaseActivity{

	AddressList address_bean;
	ArrayList<Address> mAddressList;
	Address address;                                    //holds the billing address
	ListView mList;
	AddressListAdapter mAdapter;
	public static int requestNewAddress = 111;
	public static int requestNewAddress_billing = 222;
	public static int delete_address_position;
//	EasyTracker tracker;
	TextView tvAddressHeader;
	boolean bIsBilling = false;  //true when billing address true
	int indexBilling = 0;       //holds the position of billing address

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
				if(address_bean != null) {
					mAddressList = address_bean.getAddress();
				}
			}

			if (mAddressList != null && mAddressList.size() > 0) {
				if (mAddressList.size() == 1 && mAddressList.get(0).getFirstname().equals("")) {
					TextView msg = (TextView) findViewById(R.id.msg);
					msg.setVisibility(View.VISIBLE);
					msg.setText("You dont have any address saved. Please add a new address");
				} else {
					mList = (ListView) findViewById(R.id.address_list);

					for(int i=0;i<mAddressList.size();i++){
						if(mAddressList.get(i).getDefaultBilling().equalsIgnoreCase("true")){
							address = mAddressList.get(i);
							bIsBilling = true;
							indexBilling = i;
//							RelativeLayout rl = header.findViewById(R.id.rl_editaddress);
							break;
						}
					}

					if(MyApplication.getAddressFrom.equals("shipping_page"))
						bIsBilling=false;

					if(bIsBilling){
//						mAddressList.remove(indexBilling);
						LayoutInflater inflater = getLayoutInflater();
						View header = inflater.inflate(R.layout.address_detail, mList, false);

						RelativeLayout rlAddress = (RelativeLayout) header.findViewById(R.id.rl_editaddress);
						ImageView deleteAddress = (ImageView) header.findViewById(R.id.deleteAddress);
						LinearLayout llDeleteAddress = (LinearLayout) header.findViewById(R.id.ll_delete_address);
						LinearLayout llHeader = (LinearLayout) header.findViewById(R.id.ll_address_header);
						TextView tvHeader = (TextView) header.findViewById(R.id.tv_address_header);
						TextView profilename = (TextView) header.findViewById(R.id.text_header);
						TextView address1 = (TextView) header.findViewById(R.id.address1);
						TextView state = (TextView) header.findViewById(R.id.state);
						TextView city = (TextView) header.findViewById(R.id.city);
						TextView pincode = (TextView) header.findViewById(R.id.pincode);
						TextView country = (TextView) header.findViewById(R.id.country);
						TextView txtHeader = (TextView) header.findViewById(R.id.text_header);

						profilename.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
						address1.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
						state.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
						city.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
						pincode.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
						country.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
						txtHeader.setTypeface(CustomFonts.getInstance().getRobotoBold(mContext));

						llHeader.setVisibility(View.VISIBLE);
						tvHeader.setText("Billing Address");

						profilename.setText(address.getFirstname() + " " + address.getLastname());

						if(address.getDefaultBilling().equalsIgnoreCase("true")){             //user can't be deleted.
							llDeleteAddress.setEnabled(false);
							llDeleteAddress.setVisibility(View.GONE);
						}
// else{
//							llDeleteAddress.setVisibility(View.VISIBLE);
//						}

						if(address.getRegion()!=null) {
							if(!address.getRegion().equals("")) {
								address1.setText(address.getFirstname() + " " + address.getLastname() + address.getStreet() + "," + address.getCity() + "," + address.getRegion() + "," + "India" + "," + address.getPostcode());
							}
						}

						rlAddress.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
//								((AddressDetail)mContext).goToAddress(obj,position);
								goToAddressBilling(address,indexBilling);
							}
						});

						llDeleteAddress.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								((AddressDetail)mContext).deleteAddress(address,indexBilling);
							}
						});

						mList.addHeaderView(header, null, false);
					}

					ArrayList<Address> addressList = new ArrayList<Address>();
					for(int i=0;i<mAddressList.size();i++){
						if(!mAddressList.get(i).getDefaultBilling().equals("true")){
							addressList.add(mAddressList.get(i));
						}
					}
					mAddressList = addressList;
					if(mAddressList.size() > 0) {
						mAdapter = new AddressListAdapter(AddressDetail.this, mAddressList);
						mList.setAdapter(mAdapter);
					}
					/*if (MyApplication.getAddressFrom.equals("shipping_page")) {
						mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i=new Intent();
                                i.putExtra("AddressList", mAddressList.get(position));
                                setResult(Activity.RESULT_OK,i);
                                finish();
                            }
                        });
					}*/
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

			create_address.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			addActionsInFilter(MyReceiverActions.ADD_ADDRESS);

			initHeader(findViewById(R.id.header), true, "My Addresses");
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","onCreate",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	public void goToAddressBilling(Address address,int position) {          //edit address for Billing in MyProfile
		try{
			if (BillingStateCityLoader.alState == null || BillingStateCityLoader.alState.size() == 0) {                //first time call this service for getting states
				new BillingStateCityLoader(AddressDetail.this, address, "profilenewaddressbilling", String.valueOf(position)).execute(UrlsConstants.GET_STATE);
			} else {
				Intent intent = new Intent(mContext, CreateNewAddress.class);
				intent.putExtra("address", address);
				intent.putExtra("shippingorbillingaddress", "profilenewaddressbilling");
				intent.putExtra("editindex", String.valueOf(position));                                    //means editing the address not adding.
				startActivityForResult(intent, requestNewAddress);
			}
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","goToAddressBilling",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	public void goToAddress(Address address,int position) {          //edit address for Shipping in MyProfile
		try{
		if (ShippingLocationLoader.alLocationShipping == null || ShippingLocationLoader.alLocationShipping.size() == 0) {                //first time call this service for getting states
//			System.out.println("=============Id============"+MySharedPrefs.INSTANCE.getSelectedStateId());
//			new ShippingLocationLoader(AddressDetail.this, address, "profilenewaddress", String.valueOf(position)).execute(UrlsConstants.GET_LOCATION_SHIPPING + AppConstants.strSelectedStateId);
			new ShippingLocationLoader(AddressDetail.this, address, "profilenewaddress", String.valueOf(position)).execute(UrlsConstants.GET_LOCATION_SHIPPING + MySharedPrefs.INSTANCE.getSelectedStateId());
		} else {
			Intent intent = new Intent(mContext, CreateNewAddress.class);
			intent.putExtra("address", address);
			intent.putExtra("shippingorbillingaddress", "profilenewaddress");
			intent.putExtra("editindex", String.valueOf(position));                                    //means editing the address not adding.
			startActivityForResult(intent, requestNewAddress);
		}
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","goToAddress",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	public void goToAddress(Address address)        //add address for shipping in MyProfile
	{
		try{
			try {
				if (ShippingLocationLoader.alLocationShipping == null || ShippingLocationLoader.alLocationShipping.size() == 0) {                //first time call this service for getting states
					Address addres = null;
//					new ShippingLocationLoader(AddressDetail.this, addres, "profilenewaddress", "-1").execute(UrlsConstants.GET_LOCATION_SHIPPING + AppConstants.strSelectedStateId);
					new ShippingLocationLoader(AddressDetail.this, addres, "profilenewaddress", "-1").execute(UrlsConstants.GET_LOCATION_SHIPPING + MySharedPrefs.INSTANCE.getSelectedStateId());
				} else {
					Intent intent = new Intent(mContext, CreateNewAddress.class);
					intent.putExtra("shippingorbillingaddress", "profilenewaddress");
					intent.putExtra("editindex", "-1");                                    //means adding the address not editing.
					startActivityForResult(intent, requestNewAddress);
				}
			} catch (Exception e) {
				new GrocermaxBaseException("AddressDetail", "goToAddress", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
			}

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
	public void OnResponse(Bundle bundle) {
		BaseResponseBean baseResponseBean = null;
		try {
			if (bundle.getString("ACTION").equals(MyReceiverActions.DELETE_ADDRESS)) {
				dismissDialog();
				baseResponseBean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (baseResponseBean.getFlag().equalsIgnoreCase("1")) {
					UtilityMethods.customToast(Constants.ToastConstant.ADDR_DELETED, mContext);
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
				if(bIsBilling){
					//i.e. billing address has been changed.
					finish();
					AddressList address_bean = ((AddressList) data.getSerializableExtra("addressBean"));
					Intent intent = new Intent(AddressDetail.this, AddressDetail.class);
					intent.putExtra("AddressList", address_bean);
					startActivity(intent);
					finish();
				}else{
					//i.e. shipping address has been changed.
					if (mList != null ) {
						mAddressList = ((AddressList) data.getSerializableExtra("addressBean")).getAddress();
						mAdapter.updateList(mAddressList);
					}else{
//						mAddressList = ((AddressList) data.getSerializableExtra("addressBean")).getAddress();
						AddressList addressList  = ((AddressList) data.getSerializableExtra("addressBean"));
						Intent intent = new Intent(AddressDetail.this, AddressDetail.class);
//						intent.putExtra("addressBean", mAddressList);
						intent.putExtra("AddressList", addressList);
						startActivity(intent);
						finish();
					}
				}

//				if (mList == null) {
//					findViewById(R.id.msg).setVisibility(View.GONE);
//					mList = (ListView) findViewById(R.id.address_list);
//					mAddressList = ((AddressList) data.getSerializableExtra("addressBean")).getAddress();
//					mAdapter = new AddressListAdapter(AddressDetail.this, mAddressList);
//					mList.setAdapter(mAdapter);
//				} else {
//					mAddressList = ((AddressList) data.getSerializableExtra("addressBean")).getAddress();
//					mAdapter.updateList(mAddressList);
//				}
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
			if (MyApplication.getAddressFrom.equals("shipping_page")) {
				icon_header_search.setVisibility(View.GONE);
				icon_header_cart.setVisibility(View.GONE);
				cart_count_txt.setVisibility(View.GONE);
				LinearLayout llIcon = (LinearLayout)findViewById(R.id.ll_placeholder_logoIcon_appBar);
				llIcon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 7f));
			}
		}catch(Exception e){
			new GrocermaxBaseException("AddressDetail","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
		super.onStop();
    }

}
