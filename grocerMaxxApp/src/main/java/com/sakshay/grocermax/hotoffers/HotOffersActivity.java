package com.sakshay.grocermax.hotoffers;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.api.MyApi;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.DealByDealTypeBean;
import com.sakshay.grocermax.bean.DealProductListingBean;
import com.sakshay.grocermax.bean.OfferByDealTypeBean;
import com.sakshay.grocermax.bean.OfferByDealTypeModel;
import com.sakshay.grocermax.bean.OfferByDealTypeSubModel;
import com.sakshay.grocermax.bean.ShopByCategoryBean;
import com.sakshay.grocermax.bean.ShopByDealsBean;
import com.sakshay.grocermax.hotoffers.fragment.DealProductListingItemDetailGrid;
import com.sakshay.grocermax.hotoffers.fragment.HomeFragment;
import com.sakshay.grocermax.hotoffers.fragment.ItemDetailFragment;
import com.sakshay.grocermax.hotoffers.fragment.ShopByDealItemDetailFragment;
import com.sakshay.grocermax.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HotOffersActivity extends BaseActivity {


    public ShopByCategoryBean shopByCategoryBean;
    public OfferByDealTypeBean offerByDealTypeBean;
    public DealByDealTypeBean dealByDealTypeBean;
    private ImageView menuIcon;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private ArrayList<OfferByDealTypeModel> arrayList;
    private ShopByDealsBean shopByDealsBean;
    private DealProductListingBean dealProductListingBean;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot_offer);
        addActionsInFilter(MyReceiverActions.GET_SHOP_BY_CATEGORIES);
        addActionsInFilter(MyReceiverActions.GET_SHOP_BY_DEALS);
        menuIcon = (ImageView) findViewById(R.id.menuIcon);

        progress = new ProgressDialog(this);
        progress.setTitle(null);
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        myApi.reqGetShopByCategories("http://dev.grocermax.com/webservice/new_services/shopbycategory");
        myApi.reqGetShopByDeals("http://dev.grocermax.com/webservice/new_services/shopbydealtype");

    }


    @Override
    public void OnResponse(Bundle bundle) {

        try {


            String action = bundle.getString("ACTION");

            if (action.equals(MyReceiverActions.GET_SHOP_BY_CATEGORIES) || action.equals(MyReceiverActions.GET_SHOP_BY_DEALS)) {
                if (action.equals(MyReceiverActions.GET_SHOP_BY_CATEGORIES)) {

                    System.out.println("RESPONSE" + bundle.getString("json"));
                    JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                    Gson gson = new Gson();
                    shopByCategoryBean = gson.fromJson(jsonObject.toString(), ShopByCategoryBean.class);

                    System.out.println("RESPONSE MODEL" + shopByCategoryBean.getResult());
                    System.out.println("RESPONSE MODEL" + shopByCategoryBean.getArrayList().size());


                } else if (action.equals(MyReceiverActions.GET_SHOP_BY_DEALS)) {
                    System.out.println("RESPONSE" + bundle.getString("json"));
                    JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                    Gson gson = new Gson();
                    shopByDealsBean = gson.fromJson(jsonObject.toString(), ShopByDealsBean.class);

                    System.out.println("RESPONSE MODEL" + shopByDealsBean.getResult());
                    System.out.println("RESPONSE MODEL" + shopByDealsBean.getArrayList().size());
                }

                if (shopByCategoryBean != null && shopByDealsBean != null) {
                    HomeFragment fragment = new HomeFragment();

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    Bundle data = new Bundle();
                    data.putSerializable(Constants.SHOP_BY_CATEGORY_MODEL, shopByCategoryBean);
                    data.putSerializable(Constants.SHOP_BY_DEALS_MODEL, shopByDealsBean);
                    fragment.setArguments(data);
                    fragmentTransaction.commit();
                }
            } else if (action.equals(MyReceiverActions.OFFER_BY_DEALTYPE)) {


                System.out.println("RESPONSE OFFER" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
                offerByDealTypeBean = gson.fromJson(jsonObject.toString(), OfferByDealTypeBean.class);
                System.out.println("RESPONSE OFFER" + offerByDealTypeBean.getDealcategorylisting().get("all").size());
                ItemDetailFragment fragment = new ItemDetailFragment();
                Bundle data = new Bundle();
                data.putBoolean(Constants.SHOP_BY_DEAL, false);
                data.putSerializable(Constants.OFFER_BY_DEAL, offerByDealTypeBean);
                fragment.setArguments(data);
                changeFragment(fragment);
            } else if (action.equals(MyReceiverActions.DEAL_BY_DEALTYPE)) {

                System.out.println("RESPONSE OFFER" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
                parseJson(jsonObject);
//                dealByDealTypeBean = gson.fromJson(jsonObject.toString(), DealByDealTypeBean.class);
                System.out.println("RESPONSE DEAL" + dealByDealTypeBean.getDealcategorylisting().get("all").size());
                ShopByDealItemDetailFragment fragment = new ShopByDealItemDetailFragment();
                Bundle data = new Bundle();
                data.putBoolean(Constants.SHOP_BY_DEAL, true);
                data.putSerializable(Constants.DEAL_BY_DEAL, dealByDealTypeBean);
                fragment.setArguments(data);
                changeFragment(fragment);
            }else if (action.equals(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE)) {

                System.out.println("RESPONSE DEALLISTING" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
                dealProductListingBean = gson.fromJson(jsonObject.toString(), DealProductListingBean.class);
                System.out.println("RESPONSE DEALLISTING" + dealProductListingBean.getProduct().size());
                DealProductListingItemDetailGrid fragment = new DealProductListingItemDetailGrid();
                Bundle data = new Bundle();
                data.putSerializable(Constants.PRODUCTLIST, dealProductListingBean.getProduct());
                fragment.setArguments(data);
                changeFragment(fragment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        progress.dismiss();
    }

    public void parseJson(JSONObject jsonObject) {

        try {

//            "id": "322",
//                    "category_id": "2483",
//                    "dealName": "20% off on mangat ram dal",
//                    "img": "b.jpg"
            dealByDealTypeBean = new DealByDealTypeBean();
            dealByDealTypeBean.setResult(jsonObject.getString("Result"));
            JSONObject jsonDealCategory = jsonObject.getJSONObject("dealcategory");
            JSONArray jsonCategoryArray = jsonDealCategory.getJSONArray("category");
            ArrayList<OfferByDealTypeSubModel> arrayList = new ArrayList<>();
            HashMap<String, ArrayList<OfferByDealTypeSubModel>> map = new HashMap<>();
            for (int i = 0; i < jsonCategoryArray.length(); i++) {
                JSONObject jsonObject1 = jsonCategoryArray.getJSONObject(i);
                JSONArray jsonDeal = jsonObject1.getJSONArray("deals");
                for (int j = 0; j < jsonDeal.length(); j++) {
                    JSONObject jsonObject2 = jsonDeal.getJSONObject(j);
                    OfferByDealTypeSubModel offerByDealTypeSubModel = new OfferByDealTypeSubModel();
                    offerByDealTypeSubModel.setId(jsonObject2.getString("id"));
                    offerByDealTypeSubModel.setCategory_id(jsonObject2.getString("category_id"));
                    offerByDealTypeSubModel.setDealName(jsonObject2.getString("dealName"));
                    offerByDealTypeSubModel.setImg(jsonObject2.getString("img"));
                    arrayList.add(offerByDealTypeSubModel);
                }


            }
            map.put("category", arrayList);

            JSONArray jsonAllCategoryArray = jsonDealCategory.getJSONArray("all");
            ArrayList<OfferByDealTypeSubModel> allArrayList = new ArrayList<>();
            for (int i = 0; i < jsonAllCategoryArray.length(); i++) {
                JSONObject jsonObject1 = jsonAllCategoryArray.getJSONObject(i);
                OfferByDealTypeSubModel offerByDealTypeSubModel = new OfferByDealTypeSubModel();
                offerByDealTypeSubModel.setId(jsonObject1.getString("id"));
                offerByDealTypeSubModel.setCategory_id(jsonObject1.getString("category_id"));
                offerByDealTypeSubModel.setDealName(jsonObject1.getString("dealName"));
                offerByDealTypeSubModel.setImg(jsonObject1.getString("img"));
                allArrayList.add(offerByDealTypeSubModel);

            }
            map.put("all", allArrayList);
            dealByDealTypeBean.setDealcategorylisting(map);

            dealByDealTypeBean.setFlag(jsonObject.getString("flag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void hitForShopByCategory(String categoryId) {
        addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);
        myApi.reqOfferByDealType("http://dev.grocermax.com/webservice/new_services/offerbydealtype?cat_id=?" + categoryId);
    }

    public void hitForShopByDeals(String dealId) {
        addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
        myApi.reqDealByDealType("http://dev.grocermax.com/webservice/new_services/dealsbydealtype?deal_type_id=?" + dealId);
    }
    public void hitForDealsByDeals(String dealId) {
        addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
        myApi.reqProductListingByDealType("http://dev.grocermax.com/webservice/new_services/dealproductlisting?deal_id=" + dealId);
    }

    public void changeFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
//            fragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
        }
        fragmentTransaction.add(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    public ArrayList<OfferByDealTypeModel> getOfferData(int position) {
//		System.out.println("Response pos" + offerByDealTypeBean.getDealcategorylisting().get(keyList.get(position)));
        return arrayList;
    }

    public void setOfferData(ArrayList<OfferByDealTypeModel> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public void onBackPressed() {
//        if(fragmentTransaction.getBackStackEntryCount())
        super.onBackPressed();
    }
}
