package com.rgretail.grocermax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.utils.CustomFonts;

import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.bean.UserDetailBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.UrlsConstants;


public class UserProfile extends BaseActivity{
	public static TextView textName,textPhoneNo;
	private TextView textEmail;
	private LinearLayout address_lay;
	private UserDetailBean userDetailBean;
	private int requestNewAddress = 111;
	LayoutInflater inflater = null;
	View convertView = null;
	AddressViewHolder holder = null;
	EasyTracker tracker;
	TextView tvHeaderProfile;
	ImageView imgUser;
	Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		try{
		addActionsInFilter(MyReceiverActions.USER_DETAILS1);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null)
		{
			userDetailBean = (UserDetailBean) bundle.getSerializable("UserDetailBean");
		}
		
		activity = this;
		
		imgUser = (ImageView) findViewById(R.id.iv_user_image);
		textName = (TextView) findViewById(R.id.textName);
		textEmail = (TextView) findViewById(R.id.textEmail);
		textPhoneNo = (TextView) findViewById(R.id.textPhoneNo);
		address_lay = (LinearLayout) findViewById(R.id.address);
		tvHeaderProfile = (TextView) findViewById(R.id.tv_header_profile);
		
		textName.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
		textEmail.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
		textPhoneNo.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
		tvHeaderProfile.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		
		textName.setText(userDetailBean.getPersonalInfo().getFirstname()+" "+userDetailBean.getPersonalInfo().getLastname());
		textEmail.setText(userDetailBean.getPersonalInfo().getEmail());
		textPhoneNo.setText(userDetailBean.getPersonalInfo().getMobile());
		
		LayoutInflater inflater = null;
		View convertView = null;
		AddressViewHolder holder = null;

		initImageLoaderM();
		ImageLoader.getInstance().displayImage(UrlsConstants.imagePic,imgUser, ((BaseActivity)activity).baseImageoptions);
		
		TextView tvShippingAddress = new TextView(this);
		tvShippingAddress.setText("Shipping Address");
		tvShippingAddress.setPadding(20, 10, 5, 10);
		tvShippingAddress.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvShippingAddress.setTextColor(getResources().getColor(R.color.headmultiplepage));
		address_lay.addView(tvShippingAddress);
		
		if(userDetailBean.getShippingAddress().getCity() != null)
		{
			Address ship_add = userDetailBean.getShippingAddress();
			inflater = (LayoutInflater) getSystemService(
					LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.profile_row, null);
//			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(getResources().getColor(R.color.grey_bg));
//			convertView.findViewById(R.id.editaddress).setVisibility(View.GONE);
//			convertView.findViewById(R.id.deleteAddress).setVisibility(View.GONE);
			holder = new AddressViewHolder();

			holder.profilename = (TextView) convertView
					.findViewById(R.id.text_header_profile);
			holder.address1 = (TextView) convertView
					.findViewById(R.id.address1_profile);
			holder.state = (TextView) convertView
					.findViewById(R.id.state_profile);
			holder.name = (TextView) convertView.findViewById(R.id.name_profile);
			holder.phone = (TextView) convertView.findViewById(R.id.phone_profile);
			holder.city = (TextView) convertView.findViewById(R.id.city_profile);
			holder.pincode = (TextView) convertView
					.findViewById(R.id.pincode_profile);
			holder.country = (TextView) convertView
					.findViewById(R.id.country_profile);
			
			holder.profilename.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.address1.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.state.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.name.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.phone.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.city.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.pincode.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.country.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			
//			holder.profilename.setText("Shipping Address");
			holder.profilename.setText(ship_add.getFirstname()+" "+ship_add.getLastname());
			if(ship_add.getStreet()!=null)
				holder.address1.setText(ship_add.getStreet());
			if(ship_add.getRegion()!=null)
				holder.state.setText(ship_add.getRegion());
			if(ship_add.getFirstname()!=null)
				holder.name.setText(ship_add.getFirstname()+" "+ship_add.getLastname());
			if(ship_add.getTelephone()!=null)
				holder.phone.setText(ship_add.getTelephone());
			if(ship_add.getCity()!=null)
				holder.city.setText(ship_add.getCity());
			if(ship_add.getPostcode()!=null)
				holder.pincode.setText(ship_add.getPostcode());
			holder.country.setText("India");
			
			address_lay.addView(convertView);
		}else{
			
		}
		
		TextView tvBillingAddress = new TextView(this);
		tvBillingAddress.setText("Billing Address");
		tvBillingAddress.setPadding(20, 10, 5, 10);
		tvBillingAddress.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvBillingAddress.setTextColor(getResources().getColor(R.color.headmultiplepage));
		address_lay.addView(tvBillingAddress);
		
		if(userDetailBean.getBillingAddress().getCity() != null)
		{
			Address ship_add = userDetailBean.getBillingAddress();
			inflater = (LayoutInflater) getSystemService(
					LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.profile_row, null);
//			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(getResources().getColor(R.color.grey_bg));
//			convertView.findViewById(R.id.editaddress).setVisibility(View.GONE);
//			convertView.findViewById(R.id.deleteAddress).setVisibility(View.GONE);
			holder = new AddressViewHolder();

			holder.profilename = (TextView) convertView
					.findViewById(R.id.text_header_profile);
			holder.address1 = (TextView) convertView
					.findViewById(R.id.address1_profile);
			holder.state = (TextView) convertView
					.findViewById(R.id.state_profile);
			holder.name = (TextView) convertView.findViewById(R.id.name_profile);
			holder.phone = (TextView) convertView.findViewById(R.id.phone_profile);
			holder.city = (TextView) convertView.findViewById(R.id.city_profile);
			holder.pincode = (TextView) convertView
					.findViewById(R.id.pincode_profile);
			holder.country = (TextView) convertView
					.findViewById(R.id.country_profile);
			
			holder.profilename.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.address1.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.state.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.name.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.phone.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.city.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.pincode.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			holder.country.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			
//			holder.profilename.setText("Billing Address");
			holder.profilename.setText(ship_add.getFirstname()+" "+ship_add.getLastname());
			holder.address1.setText(ship_add.getStreet());
			holder.state.setText(ship_add.getRegion());
			holder.name.setText(ship_add.getFirstname()+" "+ship_add.getLastname());
			holder.phone.setText(ship_add.getTelephone());
			holder.city.setText(ship_add.getCity());
			holder.pincode.setText(ship_add.getPostcode());
			holder.country.setText("India");
			
			address_lay.addView(convertView);
		}
		if(userDetailBean.getBillingAddress().getCity()==null)
		{
			 Button btnTag = new Button(this);
			// btnTag.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			 
			  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			  layoutParams.setMargins(10, 10, 10, 10);
			  btnTag.setPadding(20,20,20,20);
			  btnTag.setLayoutParams(layoutParams);
			  btnTag.setText("CREATE ADDRESS");
			  btnTag.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_place_update));
			  btnTag.setTextColor(getResources().getColor(R.color.white));
			  address_lay.addView(btnTag);
			  btnTag.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					goToAddress(null);
					
				}
			});
			    
		}
		} catch (Exception e) {
			new GrocermaxBaseException("UserProfile","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	public void goToAddress(Address address)
	{	
		Intent intent = new Intent(mContext, CreateNewAddress.class);
		intent.putExtra("address", address);
		startActivityForResult(intent, requestNewAddress);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		if(requestcode==111)
		{
			try{
				showDialog();
				String url = UrlsConstants.USER_DETAIL_URL + MySharedPrefs.INSTANCE.getUserId();
				myApi.reqUserDetails1(url);
			} catch (Exception e) {
				new GrocermaxBaseException("UserProfile","onActivityResult",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
		}
	}
	
	  public void gotoEditProfile(View v)
	  {
		  Intent intent = new Intent(mContext, EditProfile.class);
		  startActivity(intent);
	  }
	
	public static class AddressViewHolder {
		TextView profilename, address1, state, city, pincode, name, phone, country;
	}

	@Override
	public void OnResponse(Bundle bundle) {
		try{
			if (bundle.getString("ACTION").equals(MyReceiverActions.USER_DETAILS1)) {
				userDetailBean = (UserDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
				setAddress();
			}
		} catch (Exception e) {
			new GrocermaxBaseException("UserProfile","OnResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,String.valueOf(bundle));
		}
	}
	
	public void setAddress()
	{
		try{
		address_lay.removeAllViews();
		if(userDetailBean.getShippingAddress().getCity() != null)
		{
			Address ship_add = userDetailBean.getShippingAddress();
			inflater = (LayoutInflater) getSystemService(
					LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.address_row, null);
			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(getResources().getColor(R.color.grey_bg));
			convertView.findViewById(R.id.editaddress).setVisibility(View.GONE);
			convertView.findViewById(R.id.deleteAddress).setVisibility(View.GONE);
			holder = new AddressViewHolder();

			holder.profilename = (TextView) convertView
					.findViewById(R.id.text_header);
			holder.address1 = (TextView) convertView
					.findViewById(R.id.address1);
			holder.state = (TextView) convertView
					.findViewById(R.id.state);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.city = (TextView) convertView.findViewById(R.id.city);
			holder.pincode = (TextView) convertView
					.findViewById(R.id.pincode);
			holder.country = (TextView) convertView
					.findViewById(R.id.country);
			
			holder.profilename.setText("Shipping Address");
            if(ship_add.getStreet()!=null)
			holder.address1.setText(ship_add.getStreet());
            if(ship_add.getRegion()!=null)
			holder.state.setText(ship_add.getRegion());
            if(ship_add.getFirstname()!=null)
			holder.name.setText(ship_add.getFirstname()+" "+ship_add.getLastname());
            if(ship_add.getTelephone()!=null)
			holder.phone.setText(ship_add.getTelephone());
            if(ship_add.getCity()!=null)
			holder.city.setText(ship_add.getCity());
            if(ship_add.getPostcode()!=null)
			holder.pincode.setText(ship_add.getPostcode());
			holder.country.setText("India");
			
			address_lay.addView(convertView);
		}
		if(userDetailBean.getBillingAddress().getCity() != null)
		{
			Address ship_add = userDetailBean.getBillingAddress();
			inflater = (LayoutInflater) getSystemService(
					LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.address_row, null);
			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(getResources().getColor(R.color.grey_bg));
			convertView.findViewById(R.id.editaddress).setVisibility(View.GONE);
			convertView.findViewById(R.id.deleteAddress).setVisibility(View.GONE);
			holder = new AddressViewHolder();

			holder.profilename = (TextView) convertView
					.findViewById(R.id.text_header);
			holder.address1 = (TextView) convertView
					.findViewById(R.id.address1);
			holder.state = (TextView) convertView
					.findViewById(R.id.state);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.city = (TextView) convertView.findViewById(R.id.city);
			holder.pincode = (TextView) convertView
					.findViewById(R.id.pincode);
			holder.country = (TextView) convertView
					.findViewById(R.id.country);
			
			holder.profilename.setText("Billing Address");
			holder.address1.setText(ship_add.getStreet());
			holder.state.setText(ship_add.getRegion());
			holder.name.setText(ship_add.getFirstname()+" "+ship_add.getLastname());
			holder.phone.setText(ship_add.getTelephone());
			holder.city.setText(ship_add.getCity());
			holder.pincode.setText(ship_add.getPostcode());
			holder.country.setText("India");
			
			address_lay.addView(convertView);
		}
		if(userDetailBean.getBillingAddress().getCity()==null)
		{
			 Button btnTag = new Button(this);
			// btnTag.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			 
			 LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			  layoutParams.setMargins(10, 10, 10, 10);
			  btnTag.setPadding(10,10,10,10);
			  btnTag.setLayoutParams(layoutParams);
			  btnTag.setText("CREATE ADDRESS");
			  btnTag.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_place_update));
			  btnTag.setTextColor(getResources().getColor(R.color.white));
			  address_lay.addView(btnTag);
			  btnTag.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					goToAddress(null);
					
				}
			});
			    
		}
		} catch (NullPointerException e) {
			new GrocermaxBaseException("UserProfile","setAddress",e.getMessage(), GrocermaxBaseException.NULL_POINTER,"nodetail");
		}
	 	catch (Exception e) {
			new GrocermaxBaseException("UserProfile","setAddress",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
		initHeader(findViewById(R.id.header), true, null);
		} catch (Exception e) {
			new GrocermaxBaseException("UserProfile","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
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
    	}catch(Exception e){
		}
    }
	
	
	
	
	
	
}
