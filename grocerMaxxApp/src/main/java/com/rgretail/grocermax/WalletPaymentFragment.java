package com.rgretail.grocermax;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rgretail.grocermax.exception.GrocermaxBaseException;

import org.json.JSONObject;

/**
 * Created by anchit-pc on 23-Jan-17.
 */
public class WalletPaymentFragment extends Fragment{
    ListView mList;
    TextView tv_paytm_offer,tv_citrus_offer,tv_mobikwik_offer;
    ImageView img_paytm_status,img_citrus_status,img_mobikwik_status;
    LinearLayout ll_paytm,ll_citrus,ll_mobikwik;
    View view_paytm,view_citrus;
    JSONObject payment;

    public static WalletPaymentFragment newInstance(JSONObject payment) {
        WalletPaymentFragment fragment = new WalletPaymentFragment();
        fragment.payment=payment;
        return fragment;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.max_coin_list, container, false);
        View view = inflater.inflate(R.layout.wallet_payment_fragment, container, false);
        try{
            tv_paytm_offer=(TextView)view.findViewById(R.id.tv_payment_offer);
            tv_citrus_offer=(TextView)view.findViewById(R.id.tv_citrus_offer);
            tv_mobikwik_offer=(TextView)view.findViewById(R.id.tv_mobikwik_offer);

            img_paytm_status=(ImageView)view.findViewById(R.id.img_payment_status);
            img_citrus_status=(ImageView)view.findViewById(R.id.img_citrus_status);
            img_mobikwik_status=(ImageView)view.findViewById(R.id.img_mobikwik_status);

            ll_paytm=(LinearLayout) view.findViewById(R.id.llPaytm);
            ll_citrus=(LinearLayout) view.findViewById(R.id.ll_citrus);
            ll_mobikwik=(LinearLayout) view.findViewById(R.id.ll_mobikwik);

            view_paytm=(View) view.findViewById(R.id.view_payTm);
            view_citrus=(View) view.findViewById(R.id.view_citrus);

            if(payment.has("paytm_cc")){
                ll_paytm.setVisibility(View.VISIBLE);
                view_paytm.setVisibility(View.VISIBLE);
                if(payment.getJSONObject("paytm_cc").getString("mobile_label")!=null && !payment.getJSONObject("paytm_cc").getString("mobile_label").equals("null")){
                    tv_paytm_offer.setText(payment.getJSONObject("paytm_cc").getString("mobile_label"));
                    tv_paytm_offer.setVisibility(View.VISIBLE);
                }else{
                    tv_paytm_offer.setText("");
                    tv_paytm_offer.setVisibility(View.GONE);
                }
            }else{
                ll_paytm.setVisibility(View.GONE);
                view_paytm.setVisibility(View.GONE);
            }

            /*to check if payment with Citrus will be available or not*/
            if(payment.has("moto")){
                ll_citrus.setVisibility(View.VISIBLE);
                view_citrus.setVisibility(View.VISIBLE);
                if(payment.getJSONObject("moto").getString("mobile_label")!=null && !payment.getJSONObject("moto").getString("mobile_label").equals("null")) {
                    tv_citrus_offer.setText(payment.getJSONObject("moto").getString("mobile_label"));
                    tv_citrus_offer.setVisibility(View.VISIBLE);
                }else{
                    tv_citrus_offer.setText("");
                    tv_citrus_offer.setVisibility(View.GONE);
                }
            }else{
                ll_citrus.setVisibility(View.GONE);
                view_citrus.setVisibility(View.GONE);
            }
           /*to check if payment with Mobikwik will be available or not*/
            if(payment.has("wallet")){
                ll_mobikwik.setVisibility(View.VISIBLE);
                if(payment.getJSONObject("wallet").getString("mobile_label")!=null && !payment.getJSONObject("wallet").getString("mobile_label").equals("null")) {
                    tv_mobikwik_offer.setText(payment.getJSONObject("wallet").getString("mobile_label"));
                    tv_mobikwik_offer.setVisibility(View.VISIBLE);
                }else{
                    tv_mobikwik_offer.setText("");
                    tv_mobikwik_offer.setVisibility(View.GONE);
                }
            }else{
                ll_mobikwik.setVisibility(View.GONE);
            }

            /*Paytm should be selected initially*/

            img_paytm_status.setImageResource(R.drawable.chkbox_selected);
            img_citrus_status.setImageResource(R.drawable.chkbox_unselected);
            img_mobikwik_status.setImageResource(R.drawable.chkbox_unselected);
            ReviewOrderAndPay.bPayTM=true;
            ReviewOrderAndPay.bCitrus=false;
            ReviewOrderAndPay.bOnline=false;
            ReviewOrderAndPay.bMobiKwik=false;
            ReviewOrderAndPay.bCash=false;

            img_paytm_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_paytm_status.setImageResource(R.drawable.chkbox_selected);
                    img_citrus_status.setImageResource(R.drawable.chkbox_unselected);
                    img_mobikwik_status.setImageResource(R.drawable.chkbox_unselected);

                    ReviewOrderAndPay.bPayTM=true;
                    ReviewOrderAndPay.bCitrus=false;
                    ReviewOrderAndPay.bOnline=false;
                    ReviewOrderAndPay.bMobiKwik=false;
                    ReviewOrderAndPay.bCash=false;
                }
            });
            img_citrus_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_paytm_status.setImageResource(R.drawable.chkbox_unselected);
                    img_citrus_status.setImageResource(R.drawable.chkbox_selected);
                    img_mobikwik_status.setImageResource(R.drawable.chkbox_unselected);

                    ReviewOrderAndPay.bPayTM=false;
                    ReviewOrderAndPay.bCitrus=true;
                    ReviewOrderAndPay.bOnline=false;
                    ReviewOrderAndPay.bMobiKwik=false;
                    ReviewOrderAndPay.bCash=false;
                }
            });
            img_mobikwik_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_paytm_status.setImageResource(R.drawable.chkbox_unselected);
                    img_citrus_status.setImageResource(R.drawable.chkbox_unselected);
                    img_mobikwik_status.setImageResource(R.drawable.chkbox_selected);

                    ReviewOrderAndPay.bPayTM=false;
                    ReviewOrderAndPay.bCitrus=false;
                    ReviewOrderAndPay.bOnline=false;
                    ReviewOrderAndPay.bMobiKwik=true;
                    ReviewOrderAndPay.bCash=false;
                }
            });


           // mList = (ListView) view.findViewById(R.id.listview_redeemTran);
            //mList.setAdapter(new PaymentListAdapter());
            //UtilityMethods.setListViewHeightBasedOnChildren(mList);
            return view;
        }catch(Exception e){
            new GrocermaxBaseException("ProductListFragments","onCreateView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
        return null;
    }

    /*public class PaymentListAdapter extends BaseAdapter {
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
            //tv_desc.setText("jhhjhd hdggd gdjgjsg hgdsgjhs djgsjhgjd ggjsgjdg gjdghsjgj hsgjgdjg gsjdgjgsjdg gdgsjgdjsgj");
                tv_desc.setTypeface(CustomFonts.getInstance().getRobotoRegular(getActivity()));
                img_icon.setImageResource(ReviewOrderAndPay.wallet_Payments.get(position).getIcon());

                if(ReviewOrderAndPay.wallet_Payments.get(position).getDesc().equals(""))
                   tv_desc.setVisibility(View.GONE);
                    //tv_desc.setVisibility(View.VISIBLE);
                else
                    tv_desc.setVisibility(View.VISIBLE);


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
                    }
                });
            return convertView;
        }
    }*/
}
