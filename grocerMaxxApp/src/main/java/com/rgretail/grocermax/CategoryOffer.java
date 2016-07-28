package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.DealByDealTypeBean;
import com.rgretail.grocermax.bean.DealListBean;
import com.rgretail.grocermax.hotoffers.fragment.ShopByDealItemDetailFragment;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anchit-pc on 08-Mar-16.
 */
public class CategoryOffer extends BaseActivity {


    FrameLayout menu;
    //public OfferByDealTypeBean offerByDealTypeBean;
    public DealByDealTypeBean dealByDealTypeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_offer);

        addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);

        menu=(FrameLayout)findViewById(R.id.menu);
        menu.setVisibility(View.GONE);

        //ItemDetailFragment fragment = new ItemDetailFragment();
        ShopByDealItemDetailFragment fragment = new ShopByDealItemDetailFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frame, fragment);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* Gson gson = new Gson();
        offerByDealTypeBean = gson.fromJson(jsonObject.toString(), OfferByDealTypeBean.class);
        Bundle data = new Bundle();
        data.putBoolean(Constants.SHOP_BY_DEAL, false);
        data.putSerializable(Constants.OFFER_BY_DEAL, offerByDealTypeBean);
        fragment.setArguments(data);
        fragmentTransaction.commit();*/

        Gson gson = new Gson();
        dealByDealTypeBean = gson.fromJson(jsonObject.toString(), DealByDealTypeBean.class);
        Bundle data = new Bundle();
        data.putBoolean(Constants.SHOP_BY_DEAL, true);
        data.putSerializable(Constants.DEAL_BY_DEAL, dealByDealTypeBean);
        fragment.setArguments(data);
        fragmentTransaction.commit();


        initHeader(findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
        findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        findViewById(R.id.header).setVisibility(View.GONE);
    }


    @Override
    public void onResume() {
        super.onResume();
        try{
            initHeader(findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
            initBottom(findViewById(R.id.footer1));
            showSubscriptionPopup();
            findViewById(R.id.header_left).setVisibility(View.VISIBLE);
            findViewById(R.id.header).setVisibility(View.GONE);
        }catch(Exception e){}

    }

    public void hitForDealsByDeals(String dealId) {            //responsible for clicking of [shop by deals -> ShopByDealItemDetailFragment -> DealListScreen]

        String url = UrlsConstants.PRODUCTLISTING_BY_DEAL_TYPE;
        showDialog();
        myApi.reqProductListingByDealType(url + dealId);
        System.out.println(dealId);
    }





    @Override
    public void OnResponse(Bundle bundle) {
        try {
            String action = bundle.getString("ACTION");
            if (action.equals(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE)) { //responsible for product listing through deals [ShopByDealItemDetailFragment -> DealListScreen]

                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                JSONObject json = jsonObject.getJSONObject("Product");
                Gson gson = new Gson();
                DealListBean dealListBean = gson.fromJson(json.toString(), DealListBean.class);
                if (dealListBean == null) {
                    UtilityMethods.customToast(AppConstants.ToastConstant.NO_PRODUCT, mContext);
                    return;
                }
                Intent call = new Intent(mContext,DealListScreen.class);
                Bundle call_bundle = new Bundle();
                call_bundle.putSerializable("ProductList",dealListBean);
                call_bundle.putSerializable("Header", DealListScreen.strDealHeading);
                call.putExtras(call_bundle);
                startActivity(call);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
