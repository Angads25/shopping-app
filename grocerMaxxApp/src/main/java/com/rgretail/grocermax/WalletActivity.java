package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.UrlsConstants;

import org.json.JSONObject;


public class WalletActivity extends BaseActivity {

    TextView tv_walletAmount,tv_transactions,tv_share;
    ImageView icon_header_back;
    double amount=0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

       // initHeader(findViewById(R.id.header), true, null);
        addActionsInFilter(MyReceiverActions.WALLET_INFO);
        initView();
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(WalletActivity.this,WalletTransaction.class);
                i.putExtra("wallet_amount",amount);
                startActivity(i);
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        try {
            showDialog();
            myApi.reqWallet(UrlsConstants.WALLET_INFO_URL + MySharedPrefs.INSTANCE.getUserId());
        } catch (Exception e) {
            new GrocermaxBaseException("WalletActivity","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting wallet amount");
        }
    }

    public void initView(){
        tv_walletAmount=(TextView)findViewById(R.id.tv_walletAmount);
        tv_transactions=(TextView)findViewById(R.id.tv_transactions);
        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
        tv_share=(TextView)findViewById(R.id.tv_share);
    }

    @Override
    public void OnResponse(Bundle bundle) {

        dismissDialog();
        if (bundle.getString("ACTION").equals(MyReceiverActions.WALLET_INFO)) {
            try
            {
                String walletResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
                //System.out.println("reorder Response = "+quoteResponse);
                JSONObject reOrderJSON=new JSONObject(walletResponse);
                if(reOrderJSON.getInt("flag")==1){
                    amount=reOrderJSON.getDouble("Balance");
                    if(amount==0)
                     tv_walletAmount.setText("0.00");
                    else
                     tv_walletAmount.setText(String.format("%.2f",amount));
                }else{
                    tv_walletAmount.setText("0.00");
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
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try{
//			EasyTracker.getInstance(this).activityStop(this);
            FlurryAgent.onEndSession(this);
        }catch(Exception e){}
    }


}
