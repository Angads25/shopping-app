package com.rgretail.grocermax;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.adapters.WalletTransactionAdapter;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.WalletTranactionList;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.UrlsConstants;

/**
 * Created by anchit-pc on 17-Dec-15.
 */
public class WalletTransaction extends BaseActivity {

    TextView tv_balance,tv_no_transaction;
    private RecyclerView recyclerView_WalletTransaction;
    ImageView icon_header_back;
    WalletTransactionAdapter walletTransactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_transaction);

        addActionsInFilter(MyReceiverActions.WALLET_TRANSACTIONS);
        initView();
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //String amount=getIntent().getExtras().getString("")
        tv_balance.setText(getResources().getString(R.string.Rs)+" "+String.format("%.2f",getIntent().getDoubleExtra("wallet_amount",0.0)));


        try {
            showDialog();
            myApi.reqWalletTransactions(UrlsConstants.WALLET_TRANSACTION_URL + MySharedPrefs.INSTANCE.getUserId());
        } catch (Exception e) {
            new GrocermaxBaseException("WalletTransaction","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting wallet transactions");
        }

    }

    public void initView(){
       tv_balance=(TextView)findViewById(R.id.tv_balance);
        tv_no_transaction=(TextView)findViewById(R.id.tv_no_transaction);
        recyclerView_WalletTransaction=(RecyclerView)findViewById(R.id.recyclerView_walletTran);
        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView_WalletTransaction.setLayoutManager(llm);
        walletTransactionAdapter = new WalletTransactionAdapter(this);
        recyclerView_WalletTransaction.setAdapter(walletTransactionAdapter);
    }


    @Override
    public void OnResponse(Bundle bundle) {
        dismissDialog();
        if (bundle.getString("ACTION").equals(MyReceiverActions.WALLET_TRANSACTIONS)) {
            try
            {
                WalletTranactionList wallTransactionBean = (WalletTranactionList) bundle.getSerializable(ConnectionService.RESPONSE);
                if(wallTransactionBean.flag==1){
                    tv_no_transaction.setVisibility(View.GONE);
                    recyclerView_WalletTransaction.setVisibility(View.VISIBLE);
                    walletTransactionAdapter.setListData(wallTransactionBean);

                }else{
                  tv_no_transaction.setVisibility(View.VISIBLE);
                  recyclerView_WalletTransaction.setVisibility(View.GONE);
                }
            }catch(Exception e)
            {
                new GrocermaxBaseException("WalletActivity","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in wallet");
            }
        }
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
//			EasyTracker.getInstance(this).activityStart(this);
            FlurryAgent.onStartSession(this, getResources().getString(R.string.flurry_api_key));
            FlurryAgent.onPageView();         //Use onPageView to report page view count.
        }catch(Exception e){
        }
        /*screen tracking using rocq*/
        try {
            RocqAnalytics.initialize(this);
            RocqAnalytics.startScreen(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /*------------------------------*/
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try{
//			EasyTracker.getInstance(this).activityStop(this);
            FlurryAgent.onEndSession(this);
        }catch(Exception e){}
        try {
            RocqAnalytics.stopScreen(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
