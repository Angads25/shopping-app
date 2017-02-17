package com.rgretail.grocermax.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rgretail.grocermax.AddressDetail;
import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

public class AddressListAdapter extends BaseAdapter{
	
	ArrayList<Address> addressList = null;
	Context mContext;
	private LayoutInflater inflater = null;
	
	public AddressListAdapter(Context mContext, ArrayList<Address> list) {
		this.mContext = mContext;
		this.addressList = list;
		this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (addressList != null){
			return addressList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Address getItem(int position) {
		return addressList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try{
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.address_detail, parent, false);
			holder = new ViewHolder();
			
//			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(mContext.getResources().getColor(R.color.grey_bg));
//			holder.edit_address = (ImageView) convertView.findViewById(R.id.editaddress);
//			holder.edit_address = (TextView) convertView.findViewById(R.id.edit_address);
			holder.rl_editaddress = (RelativeLayout) convertView.findViewById(R.id.rl_editaddress);
			holder.img_edit_address = (ImageView) convertView.findViewById(R.id.img_edit_address);
			holder.delete_address = (ImageView) convertView.findViewById(R.id.deleteAddress);
			holder.img_pick = (ImageView) convertView.findViewById(R.id.pickAddress);
			holder.llDeleteAddress = (LinearLayout) convertView.findViewById(R.id.ll_delete_address);
			holder.ll_edit_address = (LinearLayout) convertView.findViewById(R.id.ll_edit_address);
			holder.llPickAddress = (LinearLayout) convertView.findViewById(R.id.ll_pick_address);
			holder.profilename = (TextView) convertView
					.findViewById(R.id.text_header);

			holder.address1 = (TextView) convertView
					.findViewById(R.id.address1);
			holder.state = (TextView) convertView
					.findViewById(R.id.state);
//			holder.name = (TextView) convertView.findViewById(R.id.name);
//			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.city = (TextView) convertView.findViewById(R.id.city);
			holder.pincode = (TextView) convertView
					.findViewById(R.id.pincode);
			holder.country = (TextView) convertView
					.findViewById(R.id.country);
			holder.txtHeader = (TextView) convertView
					.findViewById(R.id.text_header);

			holder.llHeader  = (LinearLayout) convertView.findViewById(R.id.ll_address_header);
			holder.tvHeader = (TextView) convertView.findViewById(R.id.tv_address_header);

			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.profilename.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
		holder.address1.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
		holder.state.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
//		holder.name.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
//		holder.phone.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
		holder.city.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
		holder.pincode.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
		holder.country.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
		holder.txtHeader.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
		
		final Address obj = getItem(position);

		if(position == 0){
			holder.tvHeader.setText("Shipping Address");
			//holder.tvHeader.setVisibility(View.VISIBLE);
			//holder.llHeader.setVisibility(View.VISIBLE);
			holder.tvHeader.setVisibility(View.GONE);
			holder.llHeader.setVisibility(View.GONE);
		}else{
			holder.tvHeader.setVisibility(View.GONE);
			holder.llHeader.setVisibility(View.GONE);
		}


		holder.profilename.setText(obj.getFirstname() + " " + obj.getLastname());

//		if(obj.getDefaultBilling().equalsIgnoreCase("true") || obj.getDefaultShipping().equalsIgnoreCase("true")){             //user can't be deleted.
			if(obj.getDefaultShipping().equalsIgnoreCase("true")){             //user can't be deleted.
			holder.llDeleteAddress.setEnabled(false);
			holder.llDeleteAddress.setVisibility(View.GONE);
		}else{
			holder.llDeleteAddress.setEnabled(true);
			holder.llDeleteAddress.setVisibility(View.VISIBLE);
		}

//			String addr = address.getStreet();
//			tvHouseNo.setText(addr.split("\n")[0]);
//			tvLocation.setText(addr.split("\n")[1]);
//			tvLandMark.setText(addr.split("\n")[2]);


		if(obj.getRegion()!=null) {
			if(!obj.getRegion().equals("")) {
				//String strAddress = obj.getFirstname() + " " + obj.getLastname() +",";
				String strAddress = "";
				try{
				if(obj.getStreet() != null){
					String addr = obj.getStreet();
					strAddress += addr.split("\n")[0] + ","+ addr.split("\n")[1] + ","+addr.split("\n")[2]+",";
					strAddress += obj.getCity() + "," + obj.getRegion() + "," + "India" + "," + obj.getPostcode();
					holder.address1.setText(strAddress);
				}}
				catch(Exception e){
					holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() +","+ obj.getStreet() + "," + obj.getCity() + "," + obj.getRegion() + "," + "India" + "," + obj.getPostcode());

				}
			}
		}

			if (MyApplication.getAddressFrom.equals("shipping_page")) {
				holder.llPickAddress.setVisibility(View.VISIBLE);
				holder.llDeleteAddress.setVisibility(View.GONE);
				if(MyApplication.customerAddressID.equals(addressList.get(position).getCustomer_address_id()))
					holder.img_pick.setImageResource(R.drawable.chkbox_selected);
				else
					holder.img_pick.setImageResource(R.drawable.chkbox_unselected);
			}else{
				holder.llPickAddress.setVisibility(View.GONE);
				if(obj.getDefaultShipping().equalsIgnoreCase("true")){             //user can't be deleted.
					holder.llDeleteAddress.setEnabled(false);
					holder.llDeleteAddress.setVisibility(View.GONE);
				}else{
					holder.llDeleteAddress.setEnabled(true);
					holder.llDeleteAddress.setVisibility(View.VISIBLE);
				}
			}
		
		holder.rl_editaddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					if (obj.getRegionId() != null && MySharedPrefs.INSTANCE.getSelectedStateRegionId() != null) {
						if (!obj.getRegionId().equals(MySharedPrefs.INSTANCE.getSelectedStateRegionId())) {
//							UtilityMethods.customToast("Your Selected location is " + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + ".Kindly select add new address", mContext);
							UtilityMethods.customToast(AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_FIRST + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_SECOND, mContext);
						}else{
							((AddressDetail)mContext).goToAddress(obj, position);
						}
					}
				}catch(Exception e){}

//				((AddressDetail)mContext).goToAddress(obj, position);
			}
		});
			holder.ll_edit_address.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try{
						if (obj.getRegionId() != null && MySharedPrefs.INSTANCE.getSelectedStateRegionId() != null) {
							if (!obj.getRegionId().equals(MySharedPrefs.INSTANCE.getSelectedStateRegionId())) {
//							UtilityMethods.customToast("Your Selected location is " + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + ".Kindly select add new address", mContext);
								UtilityMethods.customToast(AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_FIRST + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_SECOND, mContext);
							}else{
								((AddressDetail)mContext).goToAddress(obj, position);
							}
						}
					}catch(Exception e){}

//				((AddressDetail)mContext).goToAddress(obj, position);
				}
			});
       holder.llDeleteAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				((AddressDetail)mContext).deleteAddress(obj,position);
			}
		});

			holder.llPickAddress.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyApplication.customerAddressID=addressList.get(position).getCustomer_address_id();
					Intent i=new Intent();
					i.putExtra("AddressList", addressList.get(position));
					((AddressDetail)mContext).setResult(Activity.RESULT_OK,i);
					((AddressDetail)mContext).finish();
				}
			});
		}catch(Exception e){
			new GrocermaxBaseException("AddressListAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return convertView;
	}
	
	private class ViewHolder {
		TextView profilename, address1, state, city, pincode, country;
//		TextView name,phone;
		ImageView delete_address,img_pick,img_edit_address;
		LinearLayout llDeleteAddress,llPickAddress;
//		ImageView edit_address,
//		TextView edit_address;
		LinearLayout llHeader,ll_edit_address;
		TextView tvHeader;
		RelativeLayout rl_editaddress;
		TextView txtHeader;
	}
	
	public void updateList(ArrayList<Address> list)
	{
		try{
			addressList = list;
			notifyDataSetChanged();
		}catch(Exception e){
			new GrocermaxBaseException("AddressListAdapter","updateList",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
}

