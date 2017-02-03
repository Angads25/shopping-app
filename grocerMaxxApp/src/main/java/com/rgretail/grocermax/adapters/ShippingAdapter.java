package com.rgretail.grocermax.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.ShippingAddress;
import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

/**
 * Created by Abhishek on 8/22/2015.
 */
public class ShippingAdapter extends BaseAdapter{

    ArrayList<Address> addressList = null;
    Context mContext;
    private LayoutInflater inflater = null;
    boolean bIsSelect[];


    public ShippingAdapter(Context mContext, ArrayList<Address> list,boolean bIsSelect[]) {
        this.mContext = mContext;
        this.addressList = list;
        this.bIsSelect = bIsSelect;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.address_row1, parent, false);
            holder = new ViewHolder();

            holder.edit_address = (RelativeLayout) convertView
                    .findViewById(R.id.rl_editaddress);
            holder.ivCbCheckOut = (ImageView) convertView
                    .findViewById(R.id.iv_cb_checkout);
            holder.iv_add = (ImageView) convertView
                    .findViewById(R.id.iv_add1);
            holder.iv_edit = (ImageView) convertView
                    .findViewById(R.id.iv_edit1);
            holder.llCbCheckout  = (LinearLayout) convertView
                    .findViewById(R.id.ll_cb_checkout);


//            holder.delete_address = (ImageView) convertView.findViewById(R.id.deleteAddress);
            holder.profilename = (TextView) convertView
                    .findViewById(R.id.text_header1);
            holder.address1 = (TextView) convertView
                    .findViewById(R.id.address2);
            holder.state = (TextView) convertView
                    .findViewById(R.id.state);
//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            holder.city = (TextView) convertView.findViewById(R.id.city);
            holder.pincode = (TextView) convertView
                    .findViewById(R.id.pincode);
            holder.country = (TextView) convertView
                    .findViewById(R.id.country);
           /* holder.txtHeader = (TextView) convertView
                    .findViewById(R.id.text_header1);*/
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.profilename.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.address1.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.state.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
//        holder.name.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
//        holder.phone.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.city.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.pincode.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.country.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        //holder.txtHeader.setTypeface(CustomFonts.getInstance().getRobotoBold(mContext));

        final Address obj = getItem(position);

        holder.profilename.setText(obj.getFirstname() + " " + obj.getLastname());

            if(bIsSelect[position] == false){
                holder.ivCbCheckOut.setImageResource(R.drawable.chkbox_unselected);
            }

            if(position == ((ShippingAddress)mContext).selectedPosition) {
                holder.ivCbCheckOut.setImageResource(R.drawable.chkbox_selected);
            }else{
                holder.ivCbCheckOut.setImageResource(R.drawable.chkbox_unselected);
            }
            holder.llCbCheckout.setTag(position);

            if (!obj.getFirstname().equals("Add")) {
                holder.profilename.setTextColor(Color.parseColor("#212121"));
                ((ShippingAddress) mContext).selectedPosition = (Integer)holder.llCbCheckout.getTag();
                holder.iv_edit.setVisibility(View.VISIBLE);
                holder.iv_add.setVisibility(View.GONE);
                if(obj.getRegion()!=null) {
                    if(!obj.getRegion().equals("")) {
                        String strAddress = obj.getFirstname() + " " + obj.getLastname() +",";
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
            } else {
                holder.profilename.setTextColor(Color.parseColor("#b22603"));
                holder.iv_edit.setVisibility(View.GONE);
                holder.iv_add.setVisibility(View.VISIBLE);
                holder.address1.setText(Html.fromHtml(obj.getStreetAddress()));
            }


            holder.edit_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (obj.getRegionId() != null && MySharedPrefs.INSTANCE.getSelectedStateRegionId() != null) {
                        if (!obj.getRegionId().equals(MySharedPrefs.INSTANCE.getSelectedStateRegionId())) {
                            UtilityMethods.customToast(AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_FIRST + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_SECOND, mContext);
                            return;
                        }else{
                            ((ShippingAddress) mContext).goToAddress(obj,position);
                        }
                    }
                }catch(Exception e){}


            }
        });

            holder.iv_edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        /*if (obj.getRegionId() != null && MySharedPrefs.INSTANCE.getSelectedStateRegionId() != null) {
                            if (!obj.getRegionId().equals(MySharedPrefs.INSTANCE.getSelectedStateRegionId())) {
                                UtilityMethods.customToast(AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_FIRST + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_SECOND, mContext);
                                return;
                            }else{
                                ((ShippingAddress) mContext).goToAddress(obj,position);
                            }
                        }*/
                        ((ShippingAddress)mContext).selectAddress();
                    }catch(Exception e){}


                }
            });
            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getAddressFrom="shipping_page";
                    ((ShippingAddress)mContext).addAddress();
                }
            });



        holder.llCbCheckout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            try {
                ((ShippingAddress) mContext).selectedPosition = (Integer) v.getTag();
                notifyDataSetChanged();
            }catch(Exception e){}
            }

        });
        }catch(Exception e){
            new GrocermaxBaseException("ShippingAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView profilename, address1, state, city, pincode, country;
//        TextView name,phone;
        RelativeLayout edit_address;
        ImageView ivCbCheckOut,iv_add,iv_edit;
        LinearLayout llCbCheckout;
        //        ,delete_address;
        TextView txtHeader;
    }

    public void updateList(ArrayList<Address> list)
    {
        try{
            addressList = list;
            notifyDataSetChanged();
        }catch(Exception e){
            new GrocermaxBaseException("ShippingAdapter","updateList",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    private void call(ViewHolder holder){

    }
}


