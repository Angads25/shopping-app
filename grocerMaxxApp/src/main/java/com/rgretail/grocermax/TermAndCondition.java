package com.rgretail.grocermax;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.utils.UrlsConstants;

import org.json.JSONObject;

/**
 * Created by anchit-pc on 19-Feb-16.
 */
public class TermAndCondition extends BaseActivity {

    TextView tv_term,tv;
    ImageView icon_header_back;
    String reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termandcondition);

        // initHeader(findViewById(R.id.header), true, null);
        addActionsInFilter(MyReceiverActions.TERM_CONDITION);
        reference=getIntent().getStringExtra("reference");
        initView();
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try {
            showDialog();
            if(reference.equals("terms")){
            tv.setText(getResources().getString(R.string.term));
            myApi.reqTermAndCondition(UrlsConstants.TERM_AND_CONDITION);
            }
            else{
                tv.setText("Contact us");
                myApi.reqTermAndCondition(UrlsConstants.CONTACT);
            }
        } catch (Exception e) {
            new GrocermaxBaseException("TermAndCondition", "onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting term and condition");
        }

    }

    public void initView() {
        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
        tv_term = (TextView) findViewById(R.id.tv_term);
        tv = (TextView) findViewById(R.id.tv);
    }

    @Override
    public void OnResponse(Bundle bundle) {
        dismissDialog();
        if (bundle.getString("ACTION").equals(MyReceiverActions.TERM_CONDITION)) {
            try {
                String response = (String) bundle.getSerializable(ConnectionService.RESPONSE);
                JSONObject termJSON=new JSONObject(response);
                if(termJSON.getInt("flag")==1){
                    String term=termJSON.getString("term");
                    tv_term.setText(Html.fromHtml(term));
                }
            } catch (Exception e) {
                e.printStackTrace();
                new GrocermaxBaseException("TermAndCondition","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in term and condition");
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(1221);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

       /*------------------------------*/
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }
}
