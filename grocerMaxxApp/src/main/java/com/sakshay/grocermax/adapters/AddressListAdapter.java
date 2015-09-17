package com.sakshay.grocermax.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sakshay.grocermax.AddressDetail;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.utils.CustomFonts;

import org.w3c.dom.Text;

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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.address_detail, parent, false);
			holder = new ViewHolder();
			
//			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(mContext.getResources().getColor(R.color.grey_bg));
//			holder.edit_address = (ImageView) convertView.findViewById(R.id.editaddress);
//			holder.edit_address = (TextView) convertView.findViewById(R.id.edit_address);
			holder.rl_editaddress = (RelativeLayout) convertView.findViewById(R.id.rl_editaddress);
			holder.delete_address = (ImageView) convertView.findViewById(R.id.deleteAddress);
			holder.llDeleteAddress = (LinearLayout) convertView.findViewById(R.id.ll_delete_address);
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
		holder.txtHeader.setTypeface(CustomFonts.getInstance().getRobotoBold(mContext));
		
		final Address obj = getItem(position);
		holder.profilename.setText(obj.getFirstname() + " " + obj.getLastname());

		if(obj.getRegion()!=null) {
			if(!obj.getRegion().equals("")) {
				holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() + obj.getStreet() + "," + obj.getCity() + "," + obj.getRegion() + "," + "India" + "," + obj.getPostcode());
			}
//			else{
//				holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() + obj.getStreet() + "," + obj.getCity() + ","+obj.getState()+","+"India"+","+obj.getPostcode());
//			}
		}
//		else{
//			holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() + obj.getStreet() + "," + obj.getCity() + ","+obj.getState()+","+"India"+","+obj.getPostcode());
//		}



//		holder.address1.setText(obj.getStreet()+",");
//		holder.city.setText(obj.getCity()+",");
//		if(obj.getRegion()!=null || !obj.getRegion().equals(""))
//			holder.state.setText(obj.getRegion()+",");
//		else
//			holder.state.setText(obj.getState()+",");
//		holder.country.setText("India"+",");
//		holder.pincode.setText(obj.getPostcode());
		
		holder.rl_editaddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((AddressDetail)mContext).goToAddress(obj);
			}
		});
       holder.llDeleteAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				((AddressDetail)mContext).deleteAddress(obj,position);
			}
		});
		return convertView;
	}
	
	private class ViewHolder {
		TextView profilename, address1, state, city, pincode, country;
//		TextView name,phone;
		ImageView delete_address;
		LinearLayout llDeleteAddress;
//		ImageView edit_address,
//		TextView edit_address;
		RelativeLayout rl_editaddress;
		TextView txtHeader;
	}
	
	public void updateList(ArrayList<Address> list)
	{
		addressList = list;
		notifyDataSetChanged();
	}
}

