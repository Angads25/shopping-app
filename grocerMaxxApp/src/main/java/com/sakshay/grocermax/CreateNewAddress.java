package com.sakshay.grocermax;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.AddressList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import java.io.IOException;
/*
USER can update address from MyAddresses ,their checkbox of shipping and billing address will be visible.
USER can update address from checkout screen ,their checkbox of shipping and billing address will not be visible(as it make shipping or billing from which screen of checkout user has come).
*/
public class CreateNewAddress extends BaseActivity{
	private Button button_create_address;
//	private Button button_cancel;
	private Button button_cancel;
	
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
	TextView tvFirstNameLeft,tvFirstNameMiddle,tvFirstNameRight,
			 tvLastNameLeft,tvLastNameMiddle,tvLastNameRight,
			 tvAddressLeft,tvAddressMiddle,tvAddressRight,
			 tvContactLeft,tvContactMiddle,tvContactRight,
			 tvPinCodeLeft,tvPinCodeMiddle,tvPinCodeRight;
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



			addActionsInFilter(MyReceiverActions.ADD_ADDRESS);
			addActionsInFilter(MyReceiverActions.EDIT_ADDRESS);                           //just return message address updated successfully with flag 1 in success case.
			addActionsInFilter(MyReceiverActions.EDIT_ADDRESS_BOOK);

			TextView txtAddressName = (TextView) findViewById(R.id.txt_address_name);
			TextView txtCity = (TextView) findViewById(R.id.txt_city);
			TextView txtFlatNo = (TextView) findViewById(R.id.txt_flat_no);
			TextView txtLocation = (TextView) findViewById(R.id.txt_location);
			TextView txtClosestLandmark = (TextView) findViewById(R.id.txt_closest_landmark);
			TextView txtPinCode = (TextView) findViewById(R.id.txt_pincode);

			txtAddressName.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtCity.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtFlatNo.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtLocation.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtClosestLandmark.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtPinCode.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

			tvFirstNameLeft = (TextView) findViewById(R.id.left_line_first_name_new_addr);
			tvFirstNameMiddle = (TextView) findViewById(R.id.middle_line_first_name_new_addr);
			tvFirstNameRight = (TextView) findViewById(R.id.right_line_first_name_new_addr);

			tvLastNameLeft = (TextView) findViewById(R.id.left_line_last_name_new_addr);
			tvLastNameMiddle = (TextView) findViewById(R.id.middle_line_last_name_new_addr);
			tvLastNameRight = (TextView) findViewById(R.id.right_line_last_name_new_addr);

			tvAddressLeft = (TextView) findViewById(R.id.left_line_address_name_new_addr);
			tvAddressMiddle = (TextView) findViewById(R.id.middle_line_address_name_new_addr);
			tvAddressRight = (TextView) findViewById(R.id.right_line_address_name_new_addr);

			tvContactLeft = (TextView) findViewById(R.id.left_line_mobileno_new_addr);
			tvContactMiddle = (TextView) findViewById(R.id.middle_line_mobileno_new_addr);
			tvContactRight = (TextView) findViewById(R.id.right_line_mobileno_new_addr);

			tvPinCodeLeft = (TextView) findViewById(R.id.left_line_pincode_new_addr);
			//tvPinCodeMiddle = (TextView) findViewById(R.id.middle_line_pincode_new_addr);
			tvPinCodeRight = (TextView) findViewById(R.id.left_line_pincode_new_addr);

			button_create_address = (Button) findViewById(R.id.button_create_address);
//		button_cancel = (Button) findViewById(R.id.button_cancel);

			ivCBDefaultBilling = (ImageView) findViewById(R.id.cb_iv_billing);
			tvCBDefaultBilling = (TextView) findViewById(R.id.cb_tv_billing);
			ivCBDefaultShipping = (ImageView) findViewById(R.id.cb_iv_shipping);
			tvCBDefaultShipping = (TextView) findViewById(R.id.cb_tv_shipping);

			if(strShippingorBilling.equalsIgnoreCase("shipping") || strShippingorBilling.equalsIgnoreCase("billing")){
				ivCBDefaultBilling.setVisibility(View.GONE);
				tvCBDefaultBilling.setVisibility(View.GONE);
				ivCBDefaultShipping.setVisibility(View.GONE);
				tvCBDefaultShipping.setVisibility(View.GONE);
			}else{
				ivCBDefaultBilling.setVisibility(View.VISIBLE);
				tvCBDefaultBilling.setVisibility(View.VISIBLE);
				ivCBDefaultShipping.setVisibility(View.VISIBLE);
				tvCBDefaultShipping.setVisibility(View.VISIBLE);
			}

			check_default_billing = (CheckBox) findViewById(R.id.check_default);
			check_default_shipping = (CheckBox) findViewById(R.id.check_default_shipping);

			edit_pin = (EditText) findViewById(R.id.edit_pincode_new_addr);
			text_city = (EditText) findViewById(R.id.edit_city_new_addr);            /////////////
			edit_last_name = (EditText) findViewById(R.id.edit_last_name_new_addr);
			edit_first_name = (EditText) findViewById(R.id.edit_first_name_new_addr);
			text_state = (EditText) findViewById(R.id.edit_state_new_addr);
			edit_address1 = (EditText) findViewById(R.id.edit_address_name_new_addr);       /////////
//		edit_address2 = (EditText) findViewById(R.id.edit_address2);    ////////////
			edit_contact = (EditText) findViewById(R.id.edit_mobileno_new_addr);

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

			edit_pin.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			text_city.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			edit_last_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			edit_first_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			text_state.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			edit_address1.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			edit_contact.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			check_default_billing.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			check_default_shipping.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			tvCBDefaultBilling.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			tvCBDefaultShipping.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

			button_create_address.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txtHeaderAddres.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			if (address == null)       //new address
			{
				text_city.setText("Gurgaon");
				text_city.setEnabled(false);

				text_state.setText("Haryana");
				text_state.setEnabled(false);
				header = "Create New Address";
				txtHeaderAddres.setText(header);
			} else                  //update address
			{
				header = "Update Address";
				txtHeaderAddres.setText(header);
				edit_pin.setText(address.getPostcode());
				text_city.setText(address.getCity());
				text_city.setEnabled(true);

				if (address.getRegion() != null || !address.getRegion().equals(""))
					text_state.setText(address.getRegion());
				else
					text_state.setText(address.getState());

//			text_state.setEnabled(true);
				text_state.setEnabled(false);

				String addr = address.getStreet();
				edit_address1.setText(addr.split("\n")[0]);
//			edit_address2.setText(addr.split("\n")[1]);
				edit_contact.setText(address.getTelephone());
				edit_first_name.setText(address.getFirstname());
				edit_last_name.setText(address.getLastname());

				button_create_address.setText("Update");
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
				initHeader(findViewById(R.id.header), true, "CREATE NEW ADDRESS");
			}else{
				initHeader(findViewById(R.id.header), true, "UPDATE ADDRESS");
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
			edit_first_name.setText("Abhi");
			edit_last_name.setText("yadav");
			edit_contact.setText("9999999999");
			//UNCOMMENT ABOVE CODE

			if (edit_first_name.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.FNAME_BLANCK, mContext);
				return;
			}
			if (edit_last_name.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.LNAME_BLANCK, mContext);
				return;
			}
			if (edit_contact.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.MOB_BLANCK, mContext);
				return;
			}
			if (edit_contact.getText().toString().length() != 10) {
				UtilityMethods.customToast(ToastConstant.MOB_NUMBER_DIGIT, mContext);
				return;
			}
			if (edit_address1.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.ADDR_BLANCK, mContext);
				return;
			}
			if (edit_pin.getText().toString().equals("")) {
				UtilityMethods.customToast(ToastConstant.PIN_BLANCK, mContext);
				return;
			}
			if (edit_pin.getText().toString().length() != 6) {
				UtilityMethods.customToast(ToastConstant.PIN_NUMBER_DIGIT, mContext);
				return;
			}
			JSONObject json_obj = new JSONObject();

			json_obj.put("firstname", edit_first_name.getText().toString());
			json_obj.put("lastname", edit_last_name.getText().toString());
			json_obj.put("city", text_city.getText().toString());
			json_obj.put("region", text_state.getText().toString());
			json_obj.put("postcode", edit_pin.getText().toString());
			json_obj.put("country_id", "IN");
			json_obj.put("telephone", edit_contact.getText().toString());
			json_obj.put("street", edit_address1.getText().toString());


			int default_billing = 0;
			int default_shipping = 0;

//		if(check_default_billing.isChecked())
//			default_billing = 1;
//		if(check_default_shipping.isChecked())
//			default_shipping = 1;

			if(strShippingorBilling.equalsIgnoreCase("shipping")){            //when user coming from checkout screen
				default_shipping = 1;
			}else if(strShippingorBilling.equalsIgnoreCase("billing")){      //when user coming from checkout screen
				default_billing = 1;
			}else{
				if (bBilling){
					default_billing = 1;
				}else if (bShipping) {
					default_shipping = 1;
				}
			}

			if(default_billing == 0 && default_shipping == 0){
				UtilityMethods.customToast(ToastConstant.MAKE_SHIPPING_BILLING,mContext);
				return;
			}

			String fname = edit_first_name.getText().toString();
			String lname = edit_last_name.getText().toString();
			String addressline1 = edit_address1.getText().toString();
//		String addressline2=edit_address2.getText().toString();
			String city = text_city.getText().toString();
			String state = text_state.getText().toString();
			String pin = edit_pin.getText().toString();
			String countrycode = "IN";
			String phone = edit_contact.getText().toString();

			String url_param = "fname=" + fname + "&lname=" + lname + "&addressline1=" + addressline1 + "&addressline2=" + "" + "&city=" + city + "&state=" + state + "&pin=" + pin + "&countrycode=" + countrycode + "&phone=" + phone;
			url_param = url_param.replaceAll(" ", "%20");
			showDialog();

			if (address == null) {
				//String url = UrlsConstants.ADD_ADDRESS + URLEncoder.encode(json_obj.toString(), "UTF-8") + "&UserID=" + MySharedPrefs.INSTANCE.getUserId() + "&default_billing=" + default_billing;
				String url = UrlsConstants.ADD_ADDRESS + url_param + "&userid=" + MySharedPrefs.INSTANCE.getUserId() + "&default_billing=" + default_billing + "&default_shipping=" + default_shipping;
				myApi.reqAddAddress(url, MyReceiverActions.ADD_ADDRESS);
			} else {
				/*String params = "";
				Iterator<String> itr = json_obj.keys();
				while (itr.hasNext()) {
					String key = itr.next();
					params += "&" + key + "=" + json_obj.getString(key); 
				}
				String url = UrlsConstants.EDIT_ADDRESS + MySharedPrefs.INSTANCE.getUserId() + "&id=" + address.getid() + params + "&default_shipping=" + default_billing;;*/
				String url = UrlsConstants.EDIT_ADDRESS + url_param + "&userid=" + MySharedPrefs.INSTANCE.getUserId() + "&addressid=" + address.getCustomer_address_id() + "&default_billing=" + default_billing + "&default_shipping=" + default_shipping;
				myApi.reqEditAddress(url);
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
	void OnResponse(Bundle bundle) {
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
				setResult
				(RESULT_OK, intent);
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
	    	tracker.activityStart(this);
	    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
	    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){
			new GrocermaxBaseException("CreateNewAddress","onStart",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
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
			new GrocermaxBaseException("CreateNewAddress","onStop",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }
	
	
	
	
	
}
