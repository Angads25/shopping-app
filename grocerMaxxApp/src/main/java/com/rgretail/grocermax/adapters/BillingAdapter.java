package com.rgretail.grocermax.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rgretail.grocermax.BillingAddress;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;

import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

/**
 * Created by Abhishek on 8/22/2015.
 */
public class BillingAdapter extends BaseAdapter{

    ArrayList<Address> addressList = null;
    Context mContext;
    private LayoutInflater inflater = null;
    boolean bIsSelect[];
    int selectedPosition = -1;

    public BillingAdapter(Context mContext, ArrayList<Address> list,boolean bIsSelect[]) {
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
            convertView = inflater.inflate(R.layout.address_row, parent, false);
            holder = new ViewHolder();

//			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(mContext.getResources().getColor(R.color.grey_bg));

            holder.edit_address = (RelativeLayout) convertView
                    .findViewById(R.id.rl_editaddress);
            holder.ivCbCheckoutBilling = (ImageView) convertView
                    .findViewById(R.id.iv_cb_checkout);
            holder.llCbCheckout  = (LinearLayout) convertView
                    .findViewById(R.id.ll_cb_checkout);
//            holder.delete_address = (ImageView) convertView.findViewById(R.id.deleteAddress);
            holder.profilename = (TextView) convertView
                    .findViewById(R.id.text_header);
            holder.address1 = (TextView) convertView
                    .findViewById(R.id.address1);
            holder.state = (TextView) convertView
                    .findViewById(R.id.state);
//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.phone = (TextView) convertView.findViewById(R.id.phone);
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
//        holder.name.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
//        holder.phone.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.city.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.pincode.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.country.setTypeface(CustomFonts.getInstance().getRobotoRegular(mContext));
        holder.txtHeader.setTypeface(CustomFonts.getInstance().getRobotoBold(mContext));

        final Address obj = getItem(position);
        holder.profilename.setText(obj.getFirstname() + " " + obj.getLastname());

        holder.address1.setText(obj.getStreet()+",");
//        holder.name.setText(obj.getFirstname() + " " + obj.getLastname());
//        holder.phone.setText(obj.getTelephone());
        holder.city.setText(obj.getCity()+",");


//        if(obj.getRegion()!=null ) {
//            if (!obj.getRegion().equals("")) {
//                holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() + obj.getStreet() + "," + obj.getCity() + "," + obj.getRegion() + "," + "India" + "," + obj.getPostcode());
//            }
//        }

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



//        else {
//            holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() + obj.getStreet() + "," + obj.getCity() + "," + obj.getState() + "," + "India" + "," + obj.getPostcode());
//        }

//        holder.country.setText("India"+",");
//        holder.pincode.setText(obj.getPostcode());

        holder.edit_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((BillingAddress) mContext).goToAddress(obj,position);          //edit address and edit index
                try{UtilityMethods.clickCapture(mContext,"","","","",AppConstants.GA_EVENT_EXISTING_BILLING_EDIT);}catch(Exception e){}
            }
        });

        if(bIsSelect[position] == false){
            holder.ivCbCheckoutBilling.setImageResource(R.drawable.chkbox_unselected);
        }

        if(position == ((BillingAddress)mContext).selectedPosition) {
            holder.ivCbCheckoutBilling.setImageResource(R.drawable.chkbox_selected);
        }else{
            holder.ivCbCheckoutBilling.setImageResource(R.drawable.chkbox_unselected);
        }
        holder.llCbCheckout.setTag(position);

        holder.llCbCheckout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    try{
                        UtilityMethods.clickCapture(mContext, "", "", "", "", AppConstants.GA_EVENT_EXISTING_BILLING_SELECT);}catch(Exception e){}
                    ((BillingAddress) mContext).selectedPosition = (Integer) v.getTag();
                    notifyDataSetChanged();
                }catch(Exception e){}
//                if(bIsSelect[position] == true){
//                    bIsSelect[position] = false;
//                    holder.ivCbCheckoutBilling.setImageResource(R.drawable.uncheck_pay);
//                }else{
//                    bIsSelect[position] = true;
//                    holder.ivCbCheckoutBilling.setImageResource(R.drawable.check_pay);
//                }
//                notifyDataSetChanged();
            }
        });
//        holder.delete_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ((ChooseAddress)mContext).deleteAddress(obj,position);
//            }
//        });
        }catch(Exception e){
            new GrocermaxBaseException("BillingAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView profilename, address1, state, city, pincode, country;
//        TextView name,phone;
        RelativeLayout edit_address;
        ImageView ivCbCheckoutBilling;
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
            new GrocermaxBaseException("BillingAdapter","updateList",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }
}


