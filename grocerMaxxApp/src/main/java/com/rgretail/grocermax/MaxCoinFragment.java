package com.rgretail.grocermax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rgretail.grocermax.exception.GrocermaxBaseException;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 04-Sep-16.
 */
public class MaxCoinFragment extends Fragment {

    RedeemHistory redeemHistory;
    ListView mList;
    ArrayList<com.rgretail.grocermax.bean.RedeemHistory> historiesList_used;

    public static MaxCoinFragment newInstance( ArrayList<com.rgretail.grocermax.bean.RedeemHistory> historiesList_used) {
        MaxCoinFragment fragment = new MaxCoinFragment();
       fragment.historiesList_used=historiesList_used;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try{
            redeemHistory = ((RedeemHistory) getActivity());
        }catch(Exception e){
            new GrocermaxBaseException("ProductListFragments","onActivityCreated",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.max_coin_list, container, false);
        try{
            mList = (ListView) view.findViewById(R.id.listview_redeemTran);
            mList.setAdapter(new RedeemListAdapter());
            return view;
        }catch(Exception e){
            new GrocermaxBaseException("ProductListFragments","onCreateView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }

        return null;
    }


     public class RedeemListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return historiesList_used.size();
        }

        @Override
        public Object getItem(int position) {
            return historiesList_used.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.wallet_transaction_list_item,null);
            }

            TextView tv_date=(TextView)convertView.findViewById(R.id.tv_date_time);
            TextView tv_description=(TextView)convertView.findViewById(R.id.tv_description);
            TextView tv_amount=(TextView)convertView.findViewById(R.id.tv_amount);
            TextView tv_coupon_used=(TextView)convertView.findViewById(R.id.tv_coupon_used);
            TextView tv_valid_date=(TextView)convertView.findViewById(R.id.tv_valid_date);
            LinearLayout ll=(LinearLayout)convertView.findViewById(R.id.ll);

            tv_date.setText(historiesList_used.get(position).getCreated_date().split(" ")[0]);
            tv_amount.setText(historiesList_used.get(position).getRedeem_point());
            tv_description.setText(historiesList_used.get(position).getDesc());
            if(!historiesList_used.get(position).getExp_date().equals("null") || !historiesList_used.get(position).getUsed_coupon().equals("null")){
               ll.setVisibility(View.VISIBLE);
                if(!historiesList_used.get(position).getExp_date().equals("null") && !historiesList_used.get(position).getExp_date().equals("0000-00-00")){
                    tv_valid_date.setText("Valid Till : "+historiesList_used.get(position).getExp_date());
                    tv_valid_date.setVisibility(View.VISIBLE);
                }else{
                    tv_valid_date.setText("Valid Till : N/A");
                    tv_valid_date.setVisibility(View.VISIBLE);
                }
                if(!historiesList_used.get(position).getUsed_coupon().equals("null")){
                    if(historiesList_used.get(position).getType_action().equals("1"))
                      tv_coupon_used.setText(Html.fromHtml("Coupon Used : <font color=black>"+historiesList_used.get(position).getUsed_coupon()+"</font>"));
                    else if(historiesList_used.get(position).getType_action().equals("2"))
                      tv_coupon_used.setText(Html.fromHtml("Coupon Refunded : <font color=black>"+historiesList_used.get(position).getUsed_coupon()+"</font>"));
                    else if(historiesList_used.get(position).getType_action().equals("4"))
                        tv_coupon_used.setText(Html.fromHtml("Coupon Canceled : <font color=black>"+historiesList_used.get(position).getUsed_coupon()+"</font>"));
                    else if(historiesList_used.get(position).getType_action().equals("8"))
                        tv_coupon_used.setText(Html.fromHtml("Coupon Imported : <font color=black>"+historiesList_used.get(position).getUsed_coupon()+"</font>"));
                    else
                        tv_coupon_used.setText(Html.fromHtml("Coupon : <font color=black>"+historiesList_used.get(position).getUsed_coupon()+"</font>"));
                    tv_coupon_used.setVisibility(View.VISIBLE);
                }else{
                    tv_coupon_used.setVisibility(View.GONE);
                }
            }
            else
                ll.setVisibility(View.GONE);




            return convertView;
        }
    }


}
