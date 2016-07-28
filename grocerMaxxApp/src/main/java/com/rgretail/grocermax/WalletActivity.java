package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.RedeemHistory;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.UrlsConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class WalletActivity extends BaseActivity {

    TextView tv_walletAmount,tv_transactions,tv_share,tv_msg,tv_header,tv_heading,tv_symbol;
    ImageView icon_header_back;
    double amount=0.00;
    String shareSubject="";
    String shareBody="";
    String comming_from="";
    String point="";
    ArrayList<com.rgretail.grocermax.bean.RedeemHistory> redeemHistoriesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

       comming_from=getIntent().getExtras().getString("coming_from");
       // initHeader(findViewById(R.id.header), true, null);
        addActionsInFilter(MyReceiverActions.WALLET_INFO);
        addActionsInFilter(MyReceiverActions.REWARD_POINT);
        initView();
        if(comming_from.equals("wallet")){
            tv_header.setText("Refund Wallet");
            tv_heading.setText("Your Balance");
            tv_symbol.setVisibility(View.VISIBLE);
            tv_msg.setVisibility(View.VISIBLE);
            tv_share.setVisibility(View.VISIBLE);
            tv_transactions.setText("View Transactions");
        }else{
            tv_header.setText("Max Coins");
            tv_heading.setText("Your Coins");
            tv_symbol.setVisibility(View.GONE);
            tv_msg.setVisibility(View.GONE);
            tv_share.setVisibility(View.GONE);
            tv_transactions.setText("History");
        }
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if(comming_from.equals("wallet")){
                 i=new Intent(WalletActivity.this,WalletTransaction.class);
                 i.putExtra("wallet_amount",amount);
                }
                else{
                 i=new Intent(WalletActivity.this, com.rgretail.grocermax.RedeemHistory.class);
                 i.putExtra("wallet_amount",point);
                 i.putExtra("list",redeemHistoriesList);
                }

                startActivity(i);
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!shareBody.equals("")){
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(shareBody));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }else{
                    Toast.makeText(WalletActivity.this,"Nothing to share",Toast.LENGTH_SHORT).show();
                }

            }
        });




        try {
            showDialog();
            if(comming_from.equals("wallet"))
            myApi.reqWallet(UrlsConstants.WALLET_INFO_URL + MySharedPrefs.INSTANCE.getUserId());
            else
            myApi.reqRedeemPoint(UrlsConstants.REDEEM_POINT + MySharedPrefs.INSTANCE.getUserId());
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
        tv_header=(TextView)findViewById(R.id.tv_header);
        tv_heading=(TextView)findViewById(R.id.tv_heading);
        tv_symbol=(TextView)findViewById(R.id.tv_symbol);
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

                if(!reOrderJSON.optString("Mail_Subject").equals("") && !reOrderJSON.optString("Mail_Body").equals("")){
                shareSubject=MySharedPrefs.INSTANCE.getFirstName()+" "+reOrderJSON.optString("Mail_Subject");
                shareBody=reOrderJSON.optString("Mail_Body")+""+MySharedPrefs.INSTANCE.getFirstName();
                }

                if(reOrderJSON.getInt("flag")==1){
                    amount=reOrderJSON.getDouble("Balance");
                    if(amount==0)
                     tv_walletAmount.setText("0");
                    else
                     tv_walletAmount.setText(String.valueOf(amount));
                }else{
                    tv_walletAmount.setText("0");
                }
            }catch(Exception e)
            {
                new GrocermaxBaseException("WalletActivity","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in wallet");
            }
        }else if (bundle.getString("ACTION").equals(MyReceiverActions.REWARD_POINT)) {
            try
            {
                String rewardResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
                JSONObject reOrderJSON=new JSONObject(rewardResponse);

                if(reOrderJSON.getInt("flag")==1){
                    point=reOrderJSON.getString("totalPoint");
                    tv_walletAmount.setText(point);
                    JSONArray redeemArray=reOrderJSON.getJSONArray("redeemLog");
                    redeemHistoriesList.clear();
                    for(int i=0;i<redeemArray.length();i++){
                        com.rgretail.grocermax.bean.RedeemHistory history=new RedeemHistory();
                        history.setId(redeemArray.getJSONObject(i).getString("id"));
                        history.setCreated_date(redeemArray.getJSONObject(i).getString("created_date"));
                        history.setRedeem_point(redeemArray.getJSONObject(i).getString("redeem_point"));
                        history.setDesc(redeemArray.getJSONObject(i).getString("comment"));
                        redeemHistoriesList.add(history);
                    }
                }else{
                    tv_walletAmount.setText("0");
                }
            }catch(Exception e)
            {
                new GrocermaxBaseException("WalletActivity","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in redeem point");
            }
        }

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

       /*------------------------------*/
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("request code="+requestCode);
        System.out.println("result code="+resultCode);

    }
}
