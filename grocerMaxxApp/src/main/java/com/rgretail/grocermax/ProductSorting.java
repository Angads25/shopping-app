package com.rgretail.grocermax;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class ProductSorting extends BaseActivity implements View.OnClickListener{

    private Context context;
    private int position = 0;
    private ImageView ivPopularity,ivDiscounts,ivTitleAtoZ,ivTitleZtoA,ivPriceLtoH,ivPriceHtoL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_sorting);

        context = this;

        RelativeLayout rlPopularity = (RelativeLayout)findViewById(R.id.rl_popularity);
        RelativeLayout rlDiscounts = (RelativeLayout)findViewById(R.id.rl_discounts);
        RelativeLayout rlTitleAtoZ = (RelativeLayout)findViewById(R.id.rl_title_atoz);
        RelativeLayout rlTitleZtoA = (RelativeLayout)findViewById(R.id.rl_title_ztoa);
        RelativeLayout rlPriceLtoH = (RelativeLayout)findViewById(R.id.rl_price_ltoh);
        RelativeLayout rlPriceHtoL = (RelativeLayout)findViewById(R.id.rl_price_htol);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        initHeader(findViewById(R.id.header), true, "Sort");
        icon_header_search.setVisibility(View.GONE);
        icon_header_cart.setVisibility(View.GONE);
        cart_count_txt.setVisibility(View.GONE);
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
                    Toast.makeText(context,"apply",Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch(Exception e){}
    }
}
