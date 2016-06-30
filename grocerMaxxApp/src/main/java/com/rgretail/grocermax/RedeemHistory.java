package com.rgretail.grocermax;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 29-Jun-16.
 */
public class RedeemHistory extends Activity {

    TextView tv_balance,tv_no_transaction;
    private ListView listview_RedeemTransaction;
    ImageView icon_header_back;
    ArrayList<com.rgretail.grocermax.bean.RedeemHistory> historiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_transaction);

        initView();

        historiesList=(ArrayList<com.rgretail.grocermax.bean.RedeemHistory>)getIntent().getSerializableExtra("list");
        tv_balance.setText(getIntent().getStringExtra("wallet_amount"));
       if(historiesList.size()==0){
           tv_no_transaction.setVisibility(View.VISIBLE);
           listview_RedeemTransaction.setVisibility(View.GONE);
       }else{
           tv_no_transaction.setVisibility(View.GONE);
           listview_RedeemTransaction.setVisibility(View.VISIBLE);
           listview_RedeemTransaction.setAdapter(new RedeemListAdapter());
       }

        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initView(){
        tv_balance=(TextView)findViewById(R.id.tv_balance);
        tv_no_transaction=(TextView)findViewById(R.id.tv_no_transaction);
        listview_RedeemTransaction=(ListView) findViewById(R.id.listview_redeemTran);
        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
    }


    public class RedeemListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return historiesList.size();
        }

        @Override
        public Object getItem(int position) {
            return historiesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.wallet_transaction_list_item,null);
            }

            TextView tv_date=(TextView)convertView.findViewById(R.id.tv_date_time);
            TextView tv_description=(TextView)convertView.findViewById(R.id.tv_description);
            TextView tv_amount=(TextView)convertView.findViewById(R.id.tv_amount);

            tv_date.setText(historiesList.get(position).getCreated_date());
            tv_amount.setText(historiesList.get(position).getRedeem_point());
            tv_description.setText(historiesList.get(position).getDesc());


            return convertView;
        }
    }
}
