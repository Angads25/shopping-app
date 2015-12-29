package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

    TextView tv_walletAmount,tv_transactions,tv_share,tv_msg;
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

                String shareSubject = MySharedPrefs.INSTANCE.getFirstName()+" Sent You Rs. 100 GrocerMax Coupon";
                String shareBody="Hi,<br/><br/>I'm delighted with honest quality products and deals that GrocerMax.com offers. I believe you'll love them too. " +
                        "Here's a Rs. 100 coupon just for you that you should use in your 1st purchase on GrocerMax.<br/><br/>Coupon Code - <b>APP100</b><br/><br/>"+
                        "GrocerMax bundles groceries the way you need it, at the prices you always look for. " +
                        "<a href='https://grocermax.com/noida/hotoffer?ref=hotoffer'>Please sample their hot offers</a> ,choose your pick, and do redeem the coupon.<br/><br/>Lovingly,<br/>"+MySharedPrefs.INSTANCE.getFirstName();


                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(shareBody));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                /*for testint citrus library*/

              /*  com.citrus.sdk.ui.utils.CitrusFlowManager.initCitrusConfig("test-signup",
                        "c78ec84e389814a05d3ae46546d16d2e", "test-signin",
                        "52f7e15efd4208cf5345dd554443fd99",
                        getResources().getColor(R.color.white),YourActivity.this,
                        Environment.SANDBOX,”prepaid”, sandboxBillGeneratorURL , sandboxReturnURL);*/


                
                /*----------------------*/







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
        tv_msg=(TextView)findViewById(R.id.tv_msg);
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
                tv_msg.setText(Html.fromHtml(reOrderJSON.getString("Massege").replace("Rs. ",getResources().getString(R.string.Rs))));
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
