package com.rgretail.grocermax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;


public class ProductSorting extends BaseActivity implements View.OnClickListener{

    private Context context;
    private int position = 0;
    private ImageView ivPopularity,ivDiscounts,ivTitleAtoZ,ivTitleZtoA,ivPriceLtoH,ivPriceHtoL;
    private ImageView icon_header_back;
    String comming_from;
    RelativeLayout rlPopularity,rlDiscounts,rlTitleAtoZ,rlTitleZtoA,rlPriceLtoH,rlPriceHtoL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_sorting);

        try{
            AppsFlyerLib.setCurrencyCode("INR");
            AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");
            AppsFlyerLib.sendTracking(getApplicationContext());
        }catch(Exception e){}
        context = this;

        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                if(comming_from.equals("category"))
                i.putExtra("condition",CategoryTabs.sort_condition);
                else
                i.putExtra("condition",SearchTabs.sort_condition);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        comming_from=getIntent().getStringExtra("comming_from"); //category,search

         rlPopularity = (RelativeLayout)findViewById(R.id.rl_popularity);
         rlDiscounts = (RelativeLayout)findViewById(R.id.rl_discounts);
         rlTitleAtoZ = (RelativeLayout)findViewById(R.id.rl_title_atoz);
         rlTitleZtoA = (RelativeLayout)findViewById(R.id.rl_title_ztoa);
         rlPriceLtoH = (RelativeLayout)findViewById(R.id.rl_price_ltoh);
         rlPriceHtoL = (RelativeLayout)findViewById(R.id.rl_price_htol);
        Button btnApply  = (Button)findViewById(R.id.btn_apply);

        ivPopularity = (ImageView)findViewById(R.id.iv_popularity);
        ivDiscounts = (ImageView)findViewById(R.id.iv_discounts);
        ivTitleAtoZ = (ImageView)findViewById(R.id.iv_title_atoz);
        ivTitleZtoA = (ImageView)findViewById(R.id.iv_title_ztoa);
        ivPriceLtoH = (ImageView)findViewById(R.id.iv_price_ltoh);
        ivPriceHtoL = (ImageView)findViewById(R.id.iv_price_htol);

        rlPopularity.setOnClickListener(this);
        rlDiscounts.setOnClickListener(this);
        rlTitleAtoZ.setOnClickListener(this);
        rlTitleZtoA.setOnClickListener(this);
        rlPriceLtoH.setOnClickListener(this);
        rlPriceHtoL.setOnClickListener(this);
        btnApply.setOnClickListener(this);

        if(comming_from.equals("category"))
        viewAlreadySelected(CategoryTabs.sort_condition);
        else
        viewAlreadySelected(SearchTabs.sort_condition);
    }

    public void viewAlreadySelected(String condition){
        if(condition.equals("popularity"))
         onClick(rlPopularity);
        else if(condition.equals("price_low_to_hign"))
            onClick(rlPriceLtoH);
        else if(condition.equals("a_to_z"))
            onClick(rlTitleAtoZ);
        else if(condition.equals("z_to_a"))
            onClick(rlTitleZtoA);
        else if(condition.equals("price_hign_to_low"))
            onClick(rlPriceHtoL);
        else if(condition.equals("discount"))
            onClick(rlDiscounts);

    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
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
    public void onResume() {
        super.onResume();
        try{
           // initHeader(findViewById(R.id.header), true, "Sort");
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}

       /* try {
            icon_header_search.setVisibility(View.GONE);
            icon_header_cart.setVisibility(View.GONE);
            cart_count_txt.setVisibility(View.GONE);
        }catch(Exception e){}*/
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
    }

    @Override
    public void onStop() {
        super.onStop();
        try{
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
        try {
            RocqAnalytics.stopScreen(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnResponse(Bundle bundle) {
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.rl_popularity:
                    position = 0;
                    ivPopularity.setImageResource(R.drawable.sort_product_select);
                    ivDiscounts.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleAtoZ.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleZtoA.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceLtoH.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceHtoL.setImageResource(R.drawable.sort_product_unselect);
                    break;
                case R.id.rl_discounts:
                    position = 1;
                    ivPopularity.setImageResource(R.drawable.sort_product_unselect);
                    ivDiscounts.setImageResource(R.drawable.sort_product_select);
                    ivTitleAtoZ.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleZtoA.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceLtoH.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceHtoL.setImageResource(R.drawable.sort_product_unselect);
                    break;
                case R.id.rl_title_atoz:
                    position = 2;
                    ivPopularity.setImageResource(R.drawable.sort_product_unselect);
                    ivDiscounts.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleAtoZ.setImageResource(R.drawable.sort_product_select);
                    ivTitleZtoA.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceLtoH.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceHtoL.setImageResource(R.drawable.sort_product_unselect);
                    break;
                case R.id.rl_title_ztoa:
                    position = 3;
                    ivPopularity.setImageResource(R.drawable.sort_product_unselect);
                    ivDiscounts.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleAtoZ.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleZtoA.setImageResource(R.drawable.sort_product_select);
                    ivPriceLtoH.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceHtoL.setImageResource(R.drawable.sort_product_unselect);
                    break;
                case R.id.rl_price_ltoh:
                    position = 4;
                    ivPopularity.setImageResource(R.drawable.sort_product_unselect);
                    ivDiscounts.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleAtoZ.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleZtoA.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceLtoH.setImageResource(R.drawable.sort_product_select);
                    ivPriceHtoL.setImageResource(R.drawable.sort_product_unselect);
                    break;
                case R.id.rl_price_htol:
                    position = 5;
                    ivPopularity.setImageResource(R.drawable.sort_product_unselect);
                    ivDiscounts.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleAtoZ.setImageResource(R.drawable.sort_product_unselect);
                    ivTitleZtoA.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceLtoH.setImageResource(R.drawable.sort_product_unselect);
                    ivPriceHtoL.setImageResource(R.drawable.sort_product_select);
                    break;
                case R.id.btn_apply:
                    Intent i=new Intent();
                    if(position==4)
                        i.putExtra("condition","price_low_to_hign");
                    else if(position==2)
                        i.putExtra("condition","a_to_z");
                    else if(position==3)
                        i.putExtra("condition","z_to_a");
                    else if(position==5)
                        i.putExtra("condition","price_hign_to_low");
                    else if(position==0)
                        i.putExtra("condition","popularity");
                    else if(position==1)
                        i.putExtra("condition","discount");
                    setResult(RESULT_OK,i);
                    finish();

                    break;
            }
        }catch(Exception e){}
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent();
        if(comming_from.equals("category"))
            i.putExtra("condition",CategoryTabs.sort_condition);
        else
            i.putExtra("condition",SearchTabs.sort_condition);
        setResult(RESULT_OK, i);
        finish();
    }
}
