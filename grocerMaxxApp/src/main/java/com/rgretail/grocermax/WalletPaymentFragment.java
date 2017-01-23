package com.rgretail.grocermax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.utils.UtilityMethods;

/**
 * Created by anchit-pc on 23-Jan-17.
 */
public class WalletPaymentFragment extends Fragment{
    ListView mList;
    int type;

    public static WalletPaymentFragment newInstance(int position) {
        WalletPaymentFragment fragment = new WalletPaymentFragment();
        fragment.type=position;
        return fragment;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.max_coin_list, container, false);
        try{
            mList = (ListView) view.findViewById(R.id.listview_redeemTran);
            mList.setAdapter(new PaymentListAdapter());
            UtilityMethods.setListViewHeightBasedOnChildren(mList);
            return view;
        }catch(Exception e){
            new GrocermaxBaseException("ProductListFragments","onCreateView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
        return null;
    }

    public class PaymentListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return ReviewOrderAndPay.wallet_Payments.size();
        }

        @Override
        public Object getItem(int position) {
            return ReviewOrderAndPay.wallet_Payments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView==null){
                    convertView=inflater.inflate(R.layout.payment_fragment_item1,null);
            }
                TextView tv_desc=(TextView)convertView.findViewById(R.id.tv_payment_offer);
                ImageView img_icon=(ImageView)convertView.findViewById(R.id.img_payment_icon);
                ImageView img_payment_status=(ImageView)convertView.findViewById(R.id.img_payment_status);
                tv_desc.setText(ReviewOrderAndPay.wallet_Payments.get(position).getDesc());
                img_icon.setImageResource(ReviewOrderAndPay.wallet_Payments.get(position).getIcon());
                if(ReviewOrderAndPay.wallet_Payments.get(position).isChecke_status())
                    img_payment_status.setImageResource(R.drawable.chkbox_selected);
                else
                    img_payment_status.setImageResource(R.drawable.chkbox_unselected);

                img_payment_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<ReviewOrderAndPay.wallet_Payments.size();i++){
                            ReviewOrderAndPay.wallet_Payments.get(i).setChecke_status(false);
                        }
                        ReviewOrderAndPay.wallet_Payments.get(position).setChecke_status(true);
                        mList.setAdapter(new PaymentListAdapter());
                        UtilityMethods.setListViewHeightBasedOnChildren(mList);
                        /*if(ReviewOrderAndPay.wallet_Payments.get(position).getPayment_mode().equals("paytm_cc")){
                            ReviewOrderAndPay.bPayTM=true;
                            ReviewOrderAndPay.bCitrus=false;
                            ReviewOrderAndPay.bOnline=false;
                            ReviewOrderAndPay.bMobiKwik=false;
                            ReviewOrderAndPay.bCash=false;
                        }else if(paymentses.get(position).getPayment_mode().equals("moto")){
                            ReviewOrderAndPay.bPayTM=false;
                            ReviewOrderAndPay.bCitrus=true;
                            ReviewOrderAndPay.bOnline=false;
                            ReviewOrderAndPay.bMobiKwik=false;
                            ReviewOrderAndPay.bCash=false;
                        }if(paymentses.get(position).getPayment_mode().equals("wallet")){
                            ReviewOrderAndPay.bPayTM=false;
                            ReviewOrderAndPay.bCitrus=false;
                            ReviewOrderAndPay.bOnline=false;
                            ReviewOrderAndPay.bMobiKwik=true;
                            ReviewOrderAndPay.bCash=false;
                        }*/
                    }
                });

                /*for(int i=0;i<paymentses.size();i++){
                    if(paymentses.get(i).getPayment_mode().equals("paytm_cc")&&paymentses.get(i).isChecke_status()){
                        ReviewOrderAndPay.bPayTM=true;
                        ReviewOrderAndPay.bCitrus=false;
                        ReviewOrderAndPay.bOnline=false;
                        ReviewOrderAndPay.bMobiKwik=false;
                        ReviewOrderAndPay.bCash=false;
                    }else if(paymentses.get(i).getPayment_mode().equals("moto")&&paymentses.get(i).isChecke_status()){
                        ReviewOrderAndPay.bPayTM=false;
                        ReviewOrderAndPay.bCitrus=true;
                        ReviewOrderAndPay.bOnline=false;
                        ReviewOrderAndPay.bMobiKwik=false;
                        ReviewOrderAndPay.bCash=false;
                    }if(paymentses.get(i).getPayment_mode().equals("wallet")&&paymentses.get(i).isChecke_status()){
                        ReviewOrderAndPay.bPayTM=false;
                        ReviewOrderAndPay.bCitrus=false;
                        ReviewOrderAndPay.bOnline=false;
                        ReviewOrderAndPay.bMobiKwik=true;
                        ReviewOrderAndPay.bCash=false;
                    }
                }*/
            return convertView;
        }
    }
}
