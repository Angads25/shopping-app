package com.rgretail.grocermax.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.WalletTranactionList;

/**
 * Created by anchit-pc on 17-Dec-15.
 */
public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionAdapter.ViewHolder>{
    Context con;
    WalletTranactionList walletTranactionListBean;

    public WalletTransactionAdapter(Context con) {
        this.con = con;
    }
    public void setListData(WalletTranactionList walletTranactionListBean) {
        this.walletTranactionListBean = walletTranactionListBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_transaction_list_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv_dateTime.setText(walletTranactionListBean.walletTransactionsList.get(i).actionDate);
        viewHolder.tv_amount.setText(walletTranactionListBean.walletTransactionsList.get(i).valueChange);
        String description = walletTranactionListBean.walletTransactionsList.get(i).comment;
        if(walletTranactionListBean.walletTransactionsList.get(i).orderId==null || walletTranactionListBean.walletTransactionsList.get(i).orderId.equals("")){
            viewHolder.tv_description.setText(description);
        }else {
               description=description
                    + "\n"
                    + "order ID " + walletTranactionListBean.walletTransactionsList.get(i).orderId;
            viewHolder.tv_description.setText(description);
        }

    }

    @Override
    public int getItemCount() {
        return walletTranactionListBean.walletTransactionsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_dateTime,tv_amount,tv_description;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_dateTime=(TextView)itemView.findViewById(R.id.tv_date_time);
            tv_amount=(TextView)itemView.findViewById(R.id.tv_amount);
            tv_description=(TextView)itemView.findViewById(R.id.tv_description);
        }
    }

}
