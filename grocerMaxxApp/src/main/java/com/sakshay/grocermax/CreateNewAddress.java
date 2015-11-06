package com.sakshay.grocermax;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.api.BillingStateCityLoader;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.api.ShippingLocationLoader;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.AddressList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import java.io.IOException;
import java.util.HashMap;

/*
USER can update address from MyAddresses ,their checkbox of shipping and billing address will be visible.
USER can update address from checkout screen ,their checkbox of shipping and billing address will not be visible(as it make shipping or billing from which screen of checkout user has come).
*/
public class CreateNewAddress extends BaseActivity{

	EditText tvFirstName,tvLastName,tvHouseNo,tvLocation,tvLandMark,tvCity,tvState,tvPinCode,tvPhone;
	private Spinner spinner_billing;
	private Spinner spinnerLocationShipping;
	private Button button_create_address;
	private RelativeLayout rlStateSpinner,rlLocationSpinnerShipping;
	RelativeLayout rlLocation;
	int spinnerIndexSelected = 0;                                         //used for selected billing spinner
	String strSelectedSpinnerState = "";                                  //used for selected billing spinner
	int spinnerIndexLocationShipping = 0;		                          //used for selected shipping spinner
	String strSelectedSpinnerLocationShipping = "";                       //used for selected shipping spinner
//	private Button button_cancel;
	private Button button_cancel;
	RelativeLayout rlState;                            //hide in case of shipping
	
	private ImageView ivCBDefaultBilling,ivCBDefaultShipping;
	private TextView tvCBDefaultBilling,tvCBDefaultShipping;
	private boolean bShipping = false,bBilling = false;
	
	private CheckBox check_default_billing;
	private CheckBox check_default_shipping;
	private EditText edit_first_name;
	private EditText edit_last_name;
	private EditText edit_address1;
//	private EditText edit_address2;
	private EditText text_city, text_state;
	private EditText edit_contact;
	private EditText edit_pin;
	EasyTracker tracker;
	Address address = null;
	TextView txtHeaderAddres;
//	TextView tvFirstNameLeft,tvFirstNameMiddle,tvFirstNameRight,
//			 tvLastNameLeft,tvLastNameMiddle,tvLastNameRight,
//			 tvAddressLeft,tvAddressMiddle,tvAddressRight,
//			 tvContactLeft,tvContactMiddle,tvContactRight,
//			 tvPinCodeLeft,tvPinCodeMiddle,tvPinCodeRight;
	String strShippingorBilling = "";      //becomes non empty when coming from shipping address OR billing address screen.
	String editIndex;                          //-1 when adding the address on Checkout AND integer value when editing the address

//NEW
//	edit_flat_new_addr
//	edit_location_new_addr
//	edit_landmark_new_addr
//	OLD
//	edit_last_name_new_addr
//	edit_first_name_new_addr
//	edit_state_new_addr
//	edit_mobileno_new_addr

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_address);
		try {
			String header = "";

			if (getIntent().getSerializableExtra("address") != null) {                        //when editing the address from checkout shipping or billing.
				address = (Address) getIntent().getSerializableExtra("address");
//				strShippingorBilling = getIntent().getStringExtra("shippingorbillingaddress");
//				editIndex = getIntent().getStringExtra("editindex");
			}

			if(getIntent().getStringExtra("shippingorbillingaddress") != null){               //when adding the address from checkout shipping or billing.
				strShippingorBilling = getIntent().getStringExtra("shippingorbillingaddress");
				editIndex = getIntent().getStringExtra("editindex");
			}

			rlState = (RelativeLayout)findViewById(R.id.rl_state);

			addActionsInFilter(MyReceiverActions.ADD_ADDRESS);
			addActionsInFilter(MyReceiverActions.EDIT_ADDRESS);                           //just return message address updated successfully with flag 1 in success case.
			addActionsInFilter(MyReceiverActions.EDIT_ADDRESS_BOOK);

//			TextView txtAddressName = (TextView) findViewById(R.id.txt_address_name);
//			TextView txtCity = (TextView) findViewById(R.id.txt_city);
//			TextView txtFlatNo = (TextView) findViewById(R.id.txt_flat_no);
//			TextView txtLocation = (TextView) findViewById(R.id.txt_location);
//			TextView txtClosestLandmark = (TextView) findViewById(R.id.txt_closest_landmark);
//			TextView txtPinCode = (TextView) findViewById(R.id.txt_pincode);
//
//			txtAddressName.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			txtCity.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			txtFlatNo.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			txtLocation.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			txtClosestLandmark.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			txtPinCode.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

//			tvFirstNameLeft = (TextView) findViewById(R.id.left_line_first_name_new_addr);
//			tvFirstNameMiddle = (TextView) findViewById(R.id.middle_line_first_name_new_addr);
//			tvFirstNameRight = (TextView) findViewById(R.id.right_line_first_name_new_addr);
//			tvLastNameLeft = (TextView) findViewById(R.id.left_line_last_name_new_addr);
//			tvLastNameMiddle = (TextView) findViewById(R.id.middle_line_last_name_new_addr);
//			tvLastNameRight = (TextView) findViewById(R.id.right_line_last_name_new_addr);
//			tvAddressLeft = (TextView) findViewById(R.id.left_line_address_name_new_addr);
//			tvAddressMiddle = (TextView) findViewById(R.id.middle_line_address_name_new_addr);
//			tvAddressRight = (TextView) findViewById(R.id.right_line_address_name_new_addr);
//			tvContactLeft = (TextView) findViewById(R.id.left_line_mobileno_new_addr);
//			tvContactMiddle = (TextView) findViewById(R.id.middle_line_mobileno_new_addr);
//			tvContactRight = (TextView) findViewById(R.id.right_line_mobileno_new_addr);
//			tvPinCodeLeft = (TextView) findViewById(R.id.left_line_pincode_new_addr);
//			//tvPinCodeMiddle = (TextView) findViewById(R.id.middle_line_pincode_new_addr);
//			tvPinCodeRight = (TextView) findViewById(R.id.left_line_pincode_new_addr);

			button_create_address = (Button) findViewById(R.id.button_create_address);
//		button_cancel = (Button) findViewById(R.id.button_cancel);

			ivCBDefaultBilling = (ImageView) findViewById(R.id.cb_iv_billing);
			tvCBDefaultBilling = (TextView) findViewById(R.id.cb_tv_billing);
			ivCBDefaultShipping = (ImageView) findViewById(R.id.cb_iv_shipping);
			tvCBDefaultShipping = (TextView) findViewById(R.id.cb_tv_shipping);

//			if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("billing")){
//				ivCBDefaultBilling.setVisibility(View.GONE);
//				tvCBDefaultBilling.setVisibility(View.GONE);
//				ivCBDefaultShipping.setVisibility(View.GONE);
//				tvCBDefaultShipping.setVisibility(View.GONE);
//			}else{
//				ivCBDefaultBilling.setVisibility(View.VISIBLE);
//				tvCBDefaultBilling.setVisibility(View.VISIBLE);
//				ivCBDefaultShipping.setVisibility(View.VISIBLE);
//				tvCBDefaultShipping.setVisibility(View.VISIBLE);
//			}

			check_default_billing = (CheckBox) findViewById(R.id.check_default);
			check_default_shipping = (CheckBox) findViewById(R.id.check_default_shipping);

//			edit_pin = (EditText) findViewById(R.id.edit_pincode_new_addr);
//			text_city = (EditText) findViewById(R.id.edit_city_new_addr);            /////////////
//			edit_last_name = (EditText) findViewById(R.id.edit_last_name_new_addr);
//			edit_first_name = (EditText) findViewById(R.id.edit_first_name_new_addr);
//			text_state = (EditText) findViewById(R.id.edit_state_new_addr);
//			edit_address1 = (EditText) findViewById(R.id.edit_address_name_new_addr);       /////////
//			edit_contact = (EditText) findViewById(R.id.edit_mobileno_new_addr);

			txtHeaderAddres = (TextView) findViewById(R.id.txt_create_address);

//			edit_first_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						tvFirstNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvFirstNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvFirstNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						tvLastNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvAddressLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvContactLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvPinCodeLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			edit_last_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						tvFirstNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvLastNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvLastNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvLastNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						tvAddressLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvContactLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvPinCodeLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			edit_address1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						tvFirstNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvLastNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvAddressLeft.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvAddressMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvAddressRight.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						tvContactLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvPinCodeLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			edit_contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						tvFirstNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvLastNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvAddressLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvContactLeft.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvContactMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvContactRight.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//
//						tvPinCodeLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvPinCodeRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//					}
//				}
//			});

//			edit_pin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//					// TODO Auto-generated method stub
//					if (hasFocus) {
//						tvFirstNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvFirstNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvLastNameLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvLastNameRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvAddressLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvAddressRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvContactLeft.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//						tvContactRight.setBackgroundColor(getResources().getColor(R.color.register_address_line_color));
//
//						tvPinCodeLeft.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvPinCodeMiddle.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//						tvPinCodeRight.setBackgroundColor(getResources().getColor(R.color.register_address_selected_line_color));
//					}
//				}
//			});


//		edit_pin = (EditText) findViewById(R.id.edit_pin);
//		text_city = (EditText) findViewById(R.id.edt_city);            /////////////
//		edit_last_name = (EditText) findViewById(R.id.edit_last_name);
//		edit_first_name = (EditText) findViewById(R.id.edit_first_name);
//		text_state = (EditText) findViewById(R.id.edt_state);
//		edit_address1 = (EditText) findViewById(R.id.edit_address);       /////////
//		edit_address2 = (EditText) findViewById(R.id.edit_address2);    ////////////
//		edit_contact = (EditText) findViewById(R.id.edit_contact);


//		TextView tvName = (TextView) findViewById(R.id.edit_name_new_addr);         //
//		TextView tvCity = (TextView) findViewById(R.id.edit_city_new_addr);         //
//		TextView tvFlatNo = (TextView) findViewById(R.id.edit_flat_new_addr);
//		TextView tvLocation = (TextView) findViewById(R.id.edit_location_new_addr);
//		TextView tvLandmark = (TextView) findViewById(R.id.edit_landmark_new_addr);
//		TextView tvPincode = (TextView) findViewById(R.id.edit_pincode_new_addr);     //

//			edit_pin.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
//			text_city.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
//			edit_last_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
//			edit_first_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
//			text_state.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
//			edit_address1.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
//			edit_contact.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			check_default_billing.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			check_default_shipping.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			tvCBDefaultBilling.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			tvCBDefaultShipping.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			button_create_address.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txtHeaderAddres.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("billing") ||
					strShippingorBilling.equalsIgnoreCase("profilenewaddress") || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling")){

				if(!strShippingorBilling.equals("")){
				if(strShippingorBilling.equalsIgnoreCase("billing") || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling")){     //billing edit OR add case(in checkout) AND MyAddress billing case.
					TextView txtLocation = (TextView)findViewById(R.id.txt_location);
					txtLocation.setText("Street Address / Locality");
				}else{                                                                           //shipping edit OR add case AND (from myprofile)addnewaddress or edit
					TextView txtLocation = (TextView)findViewById(R.id.txt_location);
					txtLocation.setText("Location");
				}
				}
				}else{                                                                           //create new address case
					TextView txtLocation = (TextView)findViewById(R.id.txt_location);
					txtLocation.setText("Location");
				}

			if(strShippingorBilling.equalsIgnoreCase("billing")  || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling")) {  //billing edit OR add case(in checkout) AND MyAddress billing case.
				if(BillingStateCityLoader.alStateId.size() > 0) {
					strSelectedSpinnerState = BillingStateCityLoader.alStateId.get(0);
				}
			}

			if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("profilenewaddress")) {    //shipping edit OR add case(in checkout) AND MyAddress shipping case.
				if(ShippingLocationLoader.alLocationShipping.size() > 0) {
					strSelectedSpinnerLocationShipping = ShippingLocationLoader.alLocationShipping.get(0);
				}
			}

			 tvFirstName = (EditText) findViewById(R.id.edit_first_name);
			 tvLastName = (EditText) findViewById(R.id.edit_last_name);
			 tvHouseNo = (EditText) findViewById(R.id.edit_house_no);
			 tvLocation = (EditText) findViewById(R.id.edit_location);
			 tvLandMark = (EditText) findViewById(R.id.edit_landmark);
			 tvCity = (EditText) findViewById(R.id.edit_city);

			 tvPinCode = (EditText) findViewById(R.id.edit_pincode);
			 tvPhone = (EditText) findViewById(R.id.edit_phone);

			 rlLocationSpinnerShipping = (RelativeLayout) findViewById(R.id.rl_location_spinner_shipping);
			 rlLocation = (RelativeLayout) findViewById(R.id.rl_location);
//			 int indexTempLocation = 0;                                          //default index set for spinner so that selected state should be visible in spinner.
			 if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("profilenewaddress")) {
				 rlLocationSpinnerShipping.setVisibility(View.VISIBLE);
				 rlLocation.setVisibility(View.GONE);
				 spinnerLocationShipping = (Spinner) findViewById(R.id.spinner_location_shipping);
				 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
						 R.layout.spinner_textview, ShippingLocationLoader.alLocationShipping);
				 spinnerLocationShipping.setAdapter(dataAdapter);
//				 for(int i=0;i<ShippingLocationLoader.alLocationShipping.size();i++){
//					 if(LocationActivity.strSelectedState.equalsIgnoreCase(ShippingLocationLoader.alLocationShipping.get(i))){
//						 indexTempLocation = i;
//					 }
//				 }
//				 spinnerLocationShipping.setSelection(indexTempLocation);
//				 spinnerIndexLocationShipping = indexTempLocation;
				 spinnerLocationShipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					 @Override
					 public void onItemSelected(AdapterView<?> parent, View view,
												int position, long id) {

						 spinnerIndexLocationShipping = position;
						 strSelectedSpinnerLocationShipping = ShippingLocationLoader.alLocationShipping.get(spinnerIndexLocationShipping);
						 //parent.getItemAtPosition(position);      //selected item
						 //position

					 }

					 @Override
					 public void onNothingSelected(AdapterView<?> arg0) {
					 }
				 });

			 }else{
				 rlLocationSpinnerShipping.setVisibility(View.GONE);
				 rlLocation.setVisibility(View.VISIBLE);
			 }

			 tvState = (EditText) findViewById(R.id.edit_state);
			 rlStateSpinner = (RelativeLayout) findViewById(R.id.rl_state_spinner);
			 rlState = (RelativeLayout) findViewById(R.id.rl_state);
			 int indexTemp = 0;                                          //default index set for spinner so that selected state should be visible in spinner.
			 if(strShippingorBilling.equalsIgnoreCase("billing") || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling")) {
				 rlStateSpinner.setVisibility(View.VISIBLE);
				 rlState.setVisibility(View.GONE);
				 spinner_billing = (Spinner) findViewById(R.id.spinner_billing);
				 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
						 R.layout.spinner_textview, BillingStateCityLoader.alState);
				 spinner_billing.setAdapter(dataAdapter);
				 try {
					 for (int i = 0; i < BillingStateCityLoader.alState.size(); i++) {
//						 System.out.println("===state1==" + AppConstants.strSelectedState);
//						 System.out.println("===state2===" + BillingStateCityLoader.alState.get(i));
//						 if (AppConstants.strSelectedState.equalsIgnoreCase(BillingStateCityLoader.alState.get(i))) {
						 if (MySharedPrefs.INSTANCE.getSelectedState().equalsIgnoreCase(BillingStateCityLoader.alState.get(i))) {
							 indexTemp = i;
						 }
					 }
				 }catch(Exception e){}
				 spinner_billing.setSelection(indexTemp);
				 spinnerIndexSelected = indexTemp;
				 spinner_billing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
											   int position, long id) {

						spinnerIndexSelected = position;
						strSelectedSpinnerState = BillingStateCityLoader.alStateId.get(spinnerIndexSelected);
						//parent.getItemAtPosition(position);      //selected item
						//position
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

			 }else{
				rlStateSpinner.setVisibility(View.GONE);
				rlState.setVisibility(View.VISIBLE);
			 }

			if (address == null)       //new address
			{
//				tvCity.setText("Gurgaon");
				tvFirstName.setText(MySharedPrefs.INSTANCE.getFirstName());
				tvLastName.setText(MySharedPrefs.INSTANCE.getLastName());

//				tvCity.setText(AppConstants.strSelectedCity);
				tvCity.setText(MySharedPrefs.INSTANCE.getSelectedCity());
				tvCity.setEnabled(false);
//				tvState.setText(AppConstants.strSelectedState);
				tvState.setText(MySharedPrefs.INSTANCE.getSelectedState());
				tvState.setEnabled(false);

				if(strShippingorBilling.equalsIgnoreCase("billing")) {  //drop down come in place of state
					tvState.setEnabled(false);
					rlState.setVisibility(View.GONE);
					tvCity.setEnabled(true);
					rlStateSpinner.setVisibility(View.VISIBLE);
				}
				if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("profilenewaddress")){
					tvState.setEnabled(false);
					rlState.setVisibility(View.GONE);
					tvCity.setEnabled(false);
					rlStateSpinner.setVisibility(View.GONE);

					try {
						for (int i = 0; i < LocationActivity.locationList.getItems().size(); i++) {

//							String str1 = address.getRegionId();
//							String str2 = LocationActivity.locationList.getItems().get(i).getStateId();
							if (address.getRegionId().equals(LocationActivity.locationList.getItems().get(i).getStateId())) {
								tvState.setText(LocationActivity.locationList.getItems().get(i).getStateName());
							}
						}
					}catch(Exception e){}
				}

//				tvState.setText("Haryana");

				tvPhone.setText(MySharedPrefs.INSTANCE.getMobileNo());
//				LocationActivity.strSelectedCity
//						LocationActivity.strSelectedState
				header = "Create New Address";
				txtHeaderAddres.setText(header);


//				text_city.setText("Gurgaon");
//				text_city.setEnabled(false);
//				text_state.setText("Haryana");
//				text_state.setEnabled(false);
//				header = "Create New Address";
//				txtHeaderAddres.setText(header);
			} else                  //update address
			{

				header = "Update Address";
				txtHeaderAddres.setText(header);
				tvPinCode.setText(address.getPostcode());
				tvCity.setText(address.getCity());
				tvCity.setEnabled(true);                                                           //enable in billing address case.
//				if (address.getRegion() != null || !address.getRegion().equals("")){
//					tvState.setText(address.getRegion());}
//				else{
//				tvState.setText(address.getState());}


//				tvState.setText(address.getRegion());

				tvState.setEnabled(false);

				if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("profilenewaddress")){
//					if(tvCity.getText().toString().equalsIgnoreCase(LocationActivity.strSelectedCity)){


						tvCity.setEnabled(false);
//					}
//					if(tvState.getText().toString().equalsIgnoreCase(LocationActivity.strSelectedState)){
						tvState.setEnabled(false);
						rlState.setVisibility(View.GONE);

					String addr = address.getStreet();
					tvHouseNo.setText(addr.split("\n")[0]);
//					tvLocation.setText(addr.split("\n")[1]);
					tvLandMark.setText(addr.split("\n")[2]);

					int indexTempLocation = 0;
					for(int i=0;i<ShippingLocationLoader.alLocationShipping.size();i++){
					 if(addr.split("\n")[1].equalsIgnoreCase(ShippingLocationLoader.alLocationShipping.get(i))){
							 indexTempLocation = i;
						 }
				 	}
				 	spinnerLocationShipping.setSelection(indexTempLocation);
				 	spinnerIndexLocationShipping = indexTempLocation;

					try {
						for (int i = 0; i < LocationActivity.locationList.getItems().size(); i++) {

//							String str1 = address.getRegionId();
//							String str2 = LocationActivity.locationList.getItems().get(i).getStateId();
							if (address.getRegionId().equals(LocationActivity.locationList.getItems().get(i).getStateId())) {
								tvState.setText(LocationActivity.locationList.getItems().get(i).getStateName());
							}
						}
					}catch(Exception e){}
//					}
				}else if(strShippingorBilling.equalsIgnoreCase("billing") || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling")){                                                //use spinner in case of billing
					tvState.setEnabled(false);
					rlState.setVisibility(View.GONE);
					String addr = address.getStreet();

					try {
						tvHouseNo.setText(addr.split("\n")[0]);
						tvLocation.setText(addr.split("\n")[1]);
						tvLandMark.setText(addr.split("\n")[2]);
					}catch(Exception e){}
				}


//				edit_address1.setText(addr.split("\n")[0]);
				tvPhone.setText(address.getTelephone());
				tvFirstName.setText(address.getFirstname());
				tvLastName.setText(address.getLastname());
				button_create_address.setText("Update");

//				header = "Update Address";
//				txtHeaderAddres.setText(header);
//				edit_pin.setText(address.getPostcode());
//				text_city.setText(address.getCity());
//				text_city.setEnabled(true);
//
//				if (address.getRegion() != null || !address.getRegion().equals(""))
//					text_state.setText(address.getRegion());
//				else
//					text_state.setText(address.getState());
//
//				text_state.setEnabled(false);
//
//				String addr = address.getStreet();
//				edit_address1.setText(addr.split("\n")[0]);
////			edit_address2.setText(addr.split("\n")[1]);
//				edit_contact.setText(address.getTelephone());
//				edit_first_name.setText(address.getFirstname());
//				edit_last_name.setText(address.getLastname());
//
//				button_create_address.setText("Update");
			}

			button_create_address.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					createAddress();
				}
			});

//		button_cancel.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				setResult(RESULT_CANCELED);
//				finish();
//			}
//		});

			if (address == null){
				initHeader(findViewById(R.id.header), true, "Create New Address");
			}else{
				initHeader(findViewById(R.id.header), true, "Update Address");
			}

			findViewById(R.id.footer).setVisibility(View.GONE);

			ivCBDefaultBilling.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(CreateNewAddress.this);
					if (bBilling) {
						bBilling = false;
						bShipping = false;
					} else {
						bBilling = true;
						bShipping = false;
					}

					if (bBilling) {
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_select);
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_unselect);
					} else {
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_unselect);
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_unselect);
					}

				}
			});

			tvCBDefaultBilling.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(CreateNewAddress.this);
					if (bBilling) {
						bBilling = false;
						bShipping = false;
					} else {
						bBilling = true;
						bShipping = false;
					}

					if (bBilling) {
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_select);
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_unselect);
					} else {
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_unselect);
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_unselect);
					}

				}
			});

			ivCBDefaultShipping.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(CreateNewAddress.this);
					if (bShipping) {
						bShipping = false;
						bBilling = false;
					} else {
						bShipping = true;
						bBilling = false;
					}

					if (bShipping) {
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_select);
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_unselect);     //
					} else {
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_unselect);
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_unselect);
					}

				}
			});

			tvCBDefaultShipping.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(CreateNewAddress.this);
					if (bShipping) {
						bShipping = false;
						bBilling = false;
					} else {
						bShipping = true;
						bBilling = false;
					}

					if (bShipping) {
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_select);
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_unselect);
					} else {
						ivCBDefaultShipping.setImageResource(R.drawable.checkbox_unselect);
						ivCBDefaultBilling.setImageResource(R.drawable.checkbox_unselect);
					}
				}
			});


			check_default_billing.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(CreateNewAddress.this);
				}
			});

			check_default_shipping.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UtilityMethods.hideKeyBoard(CreateNewAddress.this);
				}
			});
			}catch(Exception e){
			new GrocermaxBaseException("CreateNewAddress","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	private void createAddress()
	{
		try {
			//UNCOMMENT BELOW CODE
//			edit_first_name.setText("Abhi");
//			edit_last_name.setText("yadav");
//			edit_contact.setText("9999999999");
			//UNCOMMENT ABOVE CODE

//			if (edit_first_name.getText().toString().equals("")) {
//				UtilityMethods.customToast(ToastConstant.FNAME_BLANCK, mContext);
//				return;
//			}
//			if (edit_last_name.getText().toString().equals("")) {
//				UtilityMethods.customToast(ToastConstant.LNAME_BLANCK, mContext);
//				return;
//			}
//			if (edit_contact.getText().toString().equals("")) {
//				UtilityMethods.customToast(ToastConstant.MOB_BLANCK, mContext);
//				return;
//			}
//			if (edit_contact.getText().toString().length() != 10) {
//				UtilityMethods.customToast(ToastConstant.MOB_NUMBER_DIGIT, mContext);
//				return;
//			}
//			if (edit_address1.getText().toString().equals("")) {
//				UtilityMethods.customToast(ToastConstant.ADDR_BLANCK, mContext);
//				return;
//			}
//			if (edit_pin.getText().toString().equals("")) {
//				UtilityMethods.customToast(ToastConstant.PIN_BLANCK, mContext);
//				return;
//			}
//			if (edit_pin.getText().toString().length() != 6) {
//				UtilityMethods.customToast(ToastConstant.PIN_NUMBER_DIGIT, mContext);
//				return;
//			}


			if (tvFirstName.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.FNAME_BLANCK, mContext);
				return;
			}
			if (tvLastName.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.LNAME_BLANCK, mContext);
				return;
			}
			if (tvHouseNo.getText().toString().equals("")) {
				UtilityMethods.customToast("House no can't be blank", mContext);
				return;
			}


			if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("profilenewaddress")) {
				if (strSelectedSpinnerLocationShipping.equals("")) {
					UtilityMethods.customToast("Location can't be blank", mContext);
					return;
				}
			}else {
				if (tvLocation.getText().toString().equals("")) {
					UtilityMethods.customToast("Location can't be blank", mContext);
					return;
				}
			}

			if (tvLandMark.getText().toString().equals("")) {
				UtilityMethods.customToast("Landmark can't be blank", mContext);
				return;
			}
			if (tvCity.getText().toString().equals("")) {
				UtilityMethods.customToast("City can't be blank", mContext);
				return;
			}

			if(strShippingorBilling.equalsIgnoreCase("billing") || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling")) {
				if (strSelectedSpinnerState.equals("")) {
					UtilityMethods.customToast("State can't be blank", mContext);
					return;
				}
			}
//			else {                                             //in shipping case and profilenewaddress case this will be gone
//				if (tvState.getText().toString().equals("")) {
//					UtilityMethods.customToast("State can't be blank", mContext);
//					return;
//				}
//			}


			if (tvPinCode.getText().toString().length() != 6) {
				UtilityMethods.customToast(ToastConstant.PIN_NUMBER_DIGIT, mContext);
				return;
			}
			if (tvPhone.getText().toString().length() != 10) {
				UtilityMethods.customToast(ToastConstant.MOB_NUMBER_DIGIT, mContext);
				return;
			}
			if (tvPhone.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.MOB_BLANCK, mContext);
				return;
			}

			JSONObject json_obj = new JSONObject();


			json_obj.put("firstname", tvFirstName.getText().toString());
			json_obj.put("lastname", tvLastName.getText().toString());
			json_obj.put("city", tvCity.getText().toString());
			json_obj.put("region", tvState.getText().toString());
			json_obj.put("postcode", tvPinCode.getText().toString());
			json_obj.put("country_id", "IN");
			json_obj.put("telephone", tvPhone.getText().toString());
			json_obj.put("street", tvHouseNo.getText().toString()+","+tvLocation.getText().toString());

//			json_obj.put("firstname", edit_first_name.getText().toString());
//			json_obj.put("lastname", edit_last_name.getText().toString());
//			json_obj.put("city", text_city.getText().toString());
//			json_obj.put("region", text_state.getText().toString());
//			json_obj.put("postcode", edit_pin.getText().toString());
//			json_obj.put("country_id", "IN");
//			json_obj.put("telephone", edit_contact.getText().toString());
//			json_obj.put("street", edit_address1.getText().toString());


			int default_billing = 0;
			int default_shipping = 0;

//		if(check_default_billing.isChecked())
//			default_billing = 1;
//		if(check_default_shipping.isChecked())
//			default_shipping = 1;

			if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("profilenewaddress")){            //shipping checkout screen AND Myprofile shipping
				default_shipping = 1;
			}else if(strShippingorBilling.equalsIgnoreCase("billing") || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling") ){      //billing checkout screen AND MyProfile billing
				default_billing = 1;
			}else{
				default_shipping = 0;
				default_billing = 0;
//				if (bBilling){
//					default_billing = 1;
//				}else if (bShipping) {
//					default_shipping = 1;
//				}
			}

//			if(default_billing == 0 && default_shipping == 0){
//				UtilityMethods.customToast(ToastConstant.MAKE_SHIPPING_BILLING,mContext);
//				return;
//			}

//			String fname = edit_first_name.getText().toString();
//			String lname = edit_last_name.getText().toString();
//			String addressline1 = edit_address1.getText().toString();
////		String addressline2=edit_address2.getText().toString();
//			String city = text_city.getText().toString();
//			String state = text_state.getText().toString();
//			String pin = edit_pin.getText().toString();
//			String countrycode = "IN";
//			String phone = edit_contact.getText().toString();

			String fname = tvFirstName.getText().toString();
			String lname = tvLastName.getText().toString();
//			String addressline1 = edit_address1.getText().toString();
//		String addressline2=edit_address2.getText().toString();
			String city = tvCity.getText().toString();

			String state = "";
			if(strShippingorBilling.equalsIgnoreCase("billing") || strShippingorBilling.equalsIgnoreCase("profilenewaddressbilling")) {
//				if (!strSelectedSpinnerState.equals("")) {
//					state = strSelectedSpinnerState;
					state = BillingStateCityLoader.alStateId.get(spinnerIndexSelected);
//				}
			}else {
				state = tvState.getText().toString();

//				state = AppConstants.strSelectedStateRegionId;
				state = MySharedPrefs.INSTANCE.getSelectedStateRegionId();
			}

			String pin = tvPinCode.getText().toString();
			String countrycode = "IN";
			String phone = tvPhone.getText().toString();

			String addressLine1 = tvHouseNo.getText().toString();
			String addressLine2 = "";
			if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("profilenewaddress")) {              //state relative layout should be hide
				addressLine2 = strSelectedSpinnerLocationShipping;
			}else {
				addressLine2 = tvLocation.getText().toString();
			}
			String addressLine3 = tvLandMark.getText().toString();

//			String url_param = "fname=" + fname + "&lname=" + lname + "&addressline1=" + addressline1 + "&addressline2=" + "" + "&city=" + city + "&state=" + state + "&pin=" + pin + "&countrycode=" + countrycode + "&phone=" + phone;

//			String url_param = "fname=" + fname + "&lname=" + lname + "&addressline1=" + addressLine1 + "&addressline2=" + addressLine2 + "&addressline3=" + addressLine3+ "&city=" + city + "&state=" + state + "&pin=" + pin + "&countrycode=" + countrycode + "&phone=" + phone;
//			url_param = url_param.replaceAll(" ", "%20");
			showDialog();

			if (address == null) {
				//String url = UrlsConstants.ADD_ADDRESS + URLEncoder.encode(json_obj.toString(), "UTF-8") + "&UserID=" + MySharedPrefs.INSTANCE.getUserId() + "&default_billing=" + default_billing;


//				String url = UrlsConstants.ADD_ADDRESS + url_param + "&userid=" + MySharedPrefs.INSTANCE.getUserId() + "&default_billing=" + default_billing + "&default_shipping=" + default_shipping;
//				System.out.println("====URL new address====" + url);
//				myApi.reqAddAddress(url, MyReceiverActions.ADD_ADDRESS);

				String url = UrlsConstants.ADD_ADDRESS;

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fname",fname);
				jsonObject.put("lname",lname);
				jsonObject.put("addressline1",addressLine1);
				jsonObject.put("addressline2",addressLine2);
				jsonObject.put("addressline3",addressLine3);
				jsonObject.put("city",city);
				jsonObject.put("state",state);
				jsonObject.put("pin",pin);
				jsonObject.put("countrycode",countrycode);
				jsonObject.put("phone",phone);
				jsonObject.put("userid",MySharedPrefs.INSTANCE.getUserId());
				jsonObject.put("default_billing",String.valueOf(default_billing));
				jsonObject.put("default_shipping",String.valueOf(default_shipping));
//				jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
				myApi.reqAddAddress(url, MyReceiverActions.ADD_ADDRESS, jsonObject);

				////////////////POST/////////////
//				String strurl = UrlsConstants.ADD_ADDRESS;
//				HashMap<String, String> hashMap = new HashMap<String,String>();
//				hashMap.put("fname",fname);
//				hashMap.put("lname",lname);
//				hashMap.put("addressline1",addressLine1);
//				hashMap.put("addressline2",addressLine2);
//				hashMap.put("addressline3",addressLine3);
//				hashMap.put("city",city);
//				hashMap.put("state",state);
//				hashMap.put("pin",pin);
//				hashMap.put("countrycode",countrycode);
//				hashMap.put("phone",phone);
//				hashMap.put("userid",MySharedPrefs.INSTANCE.getUserId());
//				hashMap.put("default_billing",String.valueOf(default_billing));
//				hashMap.put("default_shipping",String.valueOf(default_shipping));
//				myApi.reqAddAddress(strurl, MyReceiverActions.ADD_ADDRESS,hashMap);
				////////////////POST/////////////
			} else {
				/*String params = "";
				Iterator<String> itr = json_obj.keys();
				while (itr.hasNext()) {
					String key = itr.next();
					params += "&" + key + "=" + json_obj.getString(key); 
				}
				String url = UrlsConstants.EDIT_ADDRESS + MySharedPrefs.INSTANCE.getUserId() + "&id=" + address.getid() + params + "&default_shipping=" + default_billing;;*/


//				String url = UrlsConstants.EDIT_ADDRESS + url_param + "&userid=" + MySharedPrefs.INSTANCE.getUserId() + "&addressid=" + address.getCustomer_address_id() + "&default_billing=" + default_billing + "&default_shipping=" + default_shipping;
//				myApi.reqEditAddress(url);

				String url = UrlsConstants.EDIT_ADDRESS;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fname",fname);
				jsonObject.put("lname",lname);
				jsonObject.put("addressline1",addressLine1);
				jsonObject.put("addressline2",addressLine2);
				jsonObject.put("addressline3",addressLine3);
				jsonObject.put("city",city);
				jsonObject.put("state",state);
				jsonObject.put("pin",pin);
				jsonObject.put("countrycode",countrycode);
				jsonObject.put("phone",phone);
				jsonObject.put("userid",MySharedPrefs.INSTANCE.getUserId());
				jsonObject.put("addressid",address.getCustomer_address_id());
				jsonObject.put("default_billing",String.valueOf(default_billing));
				jsonObject.put("default_shipping", String.valueOf(default_shipping));
//				jsonObject.put(AppConstants.ToastConstant.VERSION_NAME,AppConstants.ToastConstant.VERSION);
				myApi.reqAddAddress(url, MyReceiverActions.ADD_ADDRESS, jsonObject);


				////////////////POST/////////////
//			String strurl = UrlsConstants.EDIT_ADDRESS;
//			HashMap<String, String> hashMap = new HashMap<String,String>();
//			hashMap.put("fname",fname);
//			hashMap.put("lname",lname);
//			hashMap.put("addressline1",addressLine1);
//			hashMap.put("addressline2",addressLine2);
//			hashMap.put("addressline3",addressLine3);
//			hashMap.put("city",city);
//			hashMap.put("state",state);
//			hashMap.put("pin",pin);
//			hashMap.put("countrycode",countrycode);
//			hashMap.put("phone",phone);
//			hashMap.put("userid",MySharedPrefs.INSTANCE.getUserId());
//			hashMap.put("addressid",address.getCustomer_address_id());
//			hashMap.put("default_billing",String.valueOf(default_billing));
//			hashMap.put("default_shipping",String.valueOf(default_shipping));
//			myApi.reqEditAddress(strurl,hashMap);
				////////////////POST/////////////
			}
		}
//		catch(IOException e){
//			new GrocermaxBaseException("CreateNewAddress","createAddress",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
		catch (Exception e) {
			new GrocermaxBaseException("CreateNewAddress","createAddress",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	
	}

	@Override
	public void OnResponse(Bundle bundle) {
		try {
			String action = bundle.getString("ACTION");
			if (action.equals(MyReceiverActions.ADD_ADDRESS) || action.equals(MyReceiverActions.EDIT_ADDRESS)) {
				BaseResponseBean responseBean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (responseBean.getFlag().equalsIgnoreCase("1")) {
					showDialog();
					String url = UrlsConstants.ADDRESS_BOOK + MySharedPrefs.INSTANCE.getUserId();
					UtilityMethods.customToast(responseBean.getResult(), mContext);
					myApi.reqAddressBook(url, MyReceiverActions.EDIT_ADDRESS_BOOK);
				} else {
					UtilityMethods.customToast(responseBean.getResult(), mContext);
				}
			} else if (action.equals(MyReceiverActions.EDIT_ADDRESS_BOOK)) {
				AddressList bean = (AddressList) bundle.getSerializable(ConnectionService.RESPONSE);
				Intent intent = new Intent();
				intent.putExtra("addressBean", bean);
				intent.putExtra("editIndex", editIndex);
				setResult(RESULT_OK, intent);
				finish();
			}
		}catch(Exception e){
			new GrocermaxBaseException("CreateNewAddress","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,String.valueOf(bundle));
		}

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			if (address == null){
				initHeader(findViewById(R.id.header), true, "CREATE NEW ADDRESS");
			}else{
				initHeader(findViewById(R.id.header), true, "UPDATE ADDRESS");
			}
		}catch(Exception e){
			new GrocermaxBaseException("CreateNewAddress","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
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
    	}catch(Exception e){}
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
