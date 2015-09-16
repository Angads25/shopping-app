package com.sakshay.grocermax.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sakshay.grocermax.AddressDetail;
import com.sakshay.grocermax.ChooseAddress;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.ShippingAddress;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.utils.CustomFonts;

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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.address_row, parent, false);
            holder = new ViewHolder();

//			convertView.findViewById(R.id.layout_address_info).setBackgroundColor(mContext.getResources().getColor(R.color.grey_bg));

            holder.edit_address = (RelativeLayout) convertView
                    .findViewById(R.id.rl_editaddress);
            holder.ivCbCheckOut = (ImageView) convertView
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

        if(obj.getRegion()!=null || !obj.getRegion().equals("")) {
            holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() + obj.getStreet() + "," + obj.getCity() + ","+obj.getRegion()+","+"India"+","+obj.getPostcode());
        }else{
            holder.address1.setText(obj.getFirstname() + " " + obj.getLastname() + obj.getStreet() + "," + obj.getCity() + ","+obj.getState()+","+"India"+","+obj.getPostcode());
        }

//        holder.address1.setText(obj.getStreet()+",");
//        holder.city.setText(obj.getCity()+",");
//        if(obj.getRegion()!=null || !obj.getRegion().equals(""))
//            holder.state.setText(obj.getRegion()+",");
//        else
//            holder.state.setText(obj.getState()+",");
//        holder.country.setText("India"+",");
//        holder.pincode.setText(obj.getPostcode());

        holder.edit_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ShippingAddress)mContext).goToAddress(obj,position);
            }
        });
//        holder.delete_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ((ChooseAddress)mContext).deleteAddress(obj,position);
//            }
//        });


        if(bIsSelect[position] == false){
            holder.ivCbCheckOut.setImageResource(R.drawable.chkbox_unselected);
        }

        if(position == ((ShippingAddress)mContext).selectedPosition) {
            holder.ivCbCheckOut.setImageResource(R.drawable.chkbox_selected);
        }else{
            holder.ivCbCheckOut.setImageResource(R.drawable.chkbox_unselected);
        }
        holder.llCbCheckout.setTag(position);

        holder.llCbCheckout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((ShippingAddress)mContext).selectedPosition = (Integer)v.getTag();
                notifyDataSetChanged();

//                TextView quantity = (TextView) v.getTag();
//                if(bIsSelect[position] == true){
//                    bIsSelect[position] = false;
//                    holder.ivCbCheckOut.setImageResource(R.drawable.uncheck_pay);
//                }else{
//                    bIsSelect[position] = true;
//                    holder.ivCbCheckOut.setImageResource(R.drawable.check_pay);
//                }
//                notifyDataSetChanged();
            }

        });
        return convertView;
    }

    private class ViewHolder {
        TextView profilename, address1, state, city, pincode, country;
//        TextView name,phone;
        RelativeLayout edit_address;
        ImageView ivCbCheckOut;
        LinearLayout llCbCheckout;
        //        ,delete_address;
        TextView txtHeader;
    }

    public void updateList(ArrayList<Address> list)
    {
        addressList = list;
        notifyDataSetChanged();
    }

    private void call(ViewHolder holder){

    }
}


