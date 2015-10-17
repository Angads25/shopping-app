package com.sakshay.grocermax.hotoffers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.DealListScreen;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyApi;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.DealByDealTypeBean;
import com.sakshay.grocermax.bean.DealListBean;
import com.sakshay.grocermax.bean.DealProductListingBean;
import com.sakshay.grocermax.bean.HomeBannerBean;
import com.sakshay.grocermax.bean.OfferByDealTypeBean;
import com.sakshay.grocermax.bean.OfferByDealTypeModel;
import com.sakshay.grocermax.bean.OfferByDealTypeSubModel;
import com.sakshay.grocermax.bean.ShopByCategoryBean;
import com.sakshay.grocermax.bean.ShopByDealsBean;
import com.sakshay.grocermax.hotoffers.fragment.DealProductListingItemDetailGrid;
import com.sakshay.grocermax.hotoffers.fragment.HomeFragment;
import com.sakshay.grocermax.hotoffers.fragment.ItemDetailFragment;
import com.sakshay.grocermax.hotoffers.fragment.MenuFragment;
import com.sakshay.grocermax.hotoffers.fragment.ShopByDealItemDetailFragment;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class HotOffersActivity extends BaseActivity {


    public ShopByCategoryBean shopByCategoryBean;
    public OfferByDealTypeBean offerByDealTypeBean;
    public DealByDealTypeBean dealByDealTypeBean;
    public HomeBannerBean homeBannerBean;
    private ImageView menuIcon;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private ArrayList<OfferByDealTypeModel> arrayList;
    private ShopByDealsBean shopByDealsBean;
    private DealProductListingBean dealProductListingBean;
    private ProgressDialog progress;
    private String url;
    private DrawerLayout drawerLayout;
    private ArrayList<CategorySubcategoryBean> catObj;
    private ArrayList<String> menuArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot_offer);
        addActionsInFilter(MyReceiverActions.GET_SHOP_BY_CATEGORIES);
        addActionsInFilter(MyReceiverActions.GET_SHOP_BY_DEALS);
        addActionsInFilter(MyReceiverActions.GET_BANNER);
        menuIcon = (ImageView) findViewById(R.id.menuIcon);
//        martHeader.setVisibility(View.VISIBLE);
        progress = new ProgressDialog(this);
        progress.setTitle(null);
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        myApi.reqGetShopByCategories(UrlsConstants.SHOP_BY_CATEGORY_TYPE);
        myApi.reqGetShopByDeals(UrlsConstants.SHOP_BY_DEAL_TYPE);
        myApi.reqGetHomePageBanner(UrlsConstants.GET_BANNER);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");
        } else {
            String response = UtilityMethods.readCategoryResponse(this, AppConstants.categoriesFile);
            catObj = UtilityMethods.getCategorySubCategory(response);
        }
        Bundle call_bundle = new Bundle();
        call_bundle.putSerializable("Categories", catObj);
        call_bundle.putString("Title", "Home");
        call_bundle.putBoolean("isListView",true);
        MenuFragment fragment = new MenuFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.menu, fragment);
        fragment.setArguments(call_bundle);
        fragmentTransaction.commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.open, R.string.close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void setMenu(ArrayList<CategorySubcategoryBean> arrayList,String name) {
        Bundle call_bundle = new Bundle();
        call_bundle.putSerializable("Categories", arrayList);
        call_bundle.putString("Title",name);
        call_bundle.putBoolean("isListView",false);
        MenuFragment fragment = new MenuFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.menu, fragment);
//        fragment.setArguments(call_bundle);
//        fragmentTransaction.commit();

//        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
//        FragmentTransaction ft = manager.beginTransaction();
//        fragmentTransaction.add(R.id.menu, fragment);
        fragmentTransaction.replace(R.id.menu, fragment, name);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragment.setArguments(call_bundle);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initHeader(findViewById(R.id.header), true, null);
        findViewById(R.id.header).setVisibility(View.VISIBLE);
        findViewById(R.id.header_left).setVisibility(View.GONE);
    }

    @Override
    public void OnResponse(Bundle bundle) {

        try {
            String action = bundle.getString("ACTION");

            if (action.equals(MyReceiverActions.GET_SHOP_BY_CATEGORIES) || action.equals(MyReceiverActions.GET_SHOP_BY_DEALS) || action.equals(MyReceiverActions.GET_BANNER)) {
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
                } else if (action.equals(MyReceiverActions.GET_BANNER)) {
                    System.out.println("RESPONSE" + bundle.getString("json"));
                    JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                    Gson gson = new Gson();
                    homeBannerBean = gson.fromJson(jsonObject.toString(), HomeBannerBean.class);
                }

                if (shopByCategoryBean != null && shopByDealsBean != null && homeBannerBean != null) {
                    HomeFragment fragment = new HomeFragment();
                    dismissDialog();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    Bundle data = new Bundle();
                    data.putSerializable(Constants.SHOP_BY_CATEGORY_MODEL, shopByCategoryBean);
                    data.putSerializable(Constants.SHOP_BY_DEALS_MODEL, shopByDealsBean);
                    data.putSerializable(Constants.HOME_BANNER, homeBannerBean);
                    fragment.setArguments(data);
                    fragmentTransaction.commit();
                }
            } else if (action.equals(MyReceiverActions.OFFER_BY_DEALTYPE)) {

                dismissDialog();
                System.out.println("RESPONSE OFFER" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
                offerByDealTypeBean = gson.fromJson(jsonObject.toString(), OfferByDealTypeBean.class);
                ItemDetailFragment fragment = new ItemDetailFragment();
                Bundle data = new Bundle();
                data.putBoolean(Constants.SHOP_BY_DEAL, false);
                data.putSerializable(Constants.OFFER_BY_DEAL, offerByDealTypeBean);
                fragment.setArguments(data);
                changeFragment(fragment);
            } else if (action.equals(MyReceiverActions.DEAL_BY_DEALTYPE)) {
                dismissDialog();
                System.out.println("RESPONSE OFFER" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
//                parseJson(jsonObject);
                dealByDealTypeBean = gson.fromJson(jsonObject.toString(), DealByDealTypeBean.class);
//                System.out.println("RESPONSE DEAL" + dealByDealTypeBean.getDealcategorylisting().get("all").size());
                ShopByDealItemDetailFragment fragment = new ShopByDealItemDetailFragment();
                Bundle data = new Bundle();
                data.putBoolean(Constants.SHOP_BY_DEAL, true);
                data.putSerializable(Constants.DEAL_BY_DEAL, dealByDealTypeBean);
                fragment.setArguments(data);
                changeFragment(fragment);
            } else if (action.equals(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE)) {

                DealListBean dealListBean = (DealListBean) bundle
                        .getSerializable(ConnectionService.RESPONSE);
                if (dealListBean == null) {
                    UtilityMethods.customToast(AppConstants.ToastConstant.NO_PRODUCT, mContext);
                    return;
                }
                Intent call = new Intent(mContext,
                        DealListScreen.class);
                Bundle call_bundle = new Bundle();
                call_bundle.putSerializable("ProductList",
                        dealListBean);
                call_bundle.putSerializable("Header", AppConstants.strTitleHotDeal);
                // call_bundle.putString("cat_id",
                // category.getCategoryId());
                call.putExtras(call_bundle);
                startActivity(call);

//                System.out.println("RESPONSE DEALLISTING" + bundle.getString("json"));
//                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
//                Gson gson = new Gson();
//                dealProductListingBean = gson.fromJson(jsonObject.toString(), DealProductListingBean.class);
//                System.out.println("RESPONSE DEALLISTING" + dealProductListingBean.getProduct().size());
//                DealProductListingItemDetailGrid fragment = new DealProductListingItemDetailGrid();
//                Bundle data = new Bundle();
//                data.putSerializable(Constants.PRODUCTLIST, dealProductListingBean.getProduct());
//                fragment.setArguments(data);
//                changeFragment(fragment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        progress.dismiss();
    }

    public void hitForShopByCategory(String categoryId) {
        addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);
        String url = UrlsConstants.OFFER_BY_DEAL_TYPE;
        showDialog();
        myApi.reqOfferByDealType(url + categoryId);
    }
public DrawerLayout getDrawerLayout(){
    return drawerLayout;
}
    public void hitForShopByDeals(String dealId) {
        addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
        showDialog();
        String url = UrlsConstants.DEAL_BY_DEAL_TYPE;
        System.out.print("==my work==" + url);
        myApi.reqDealByDealType(url + dealId);
    }

    public void hitForDealsByDeals(String dealId) {
        addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
        String url = UrlsConstants.PRODUCTLISTING_BY_DEAL_TYPE;
        showDialog();
        myApi.reqProductListingByDealType(url + dealId);
        System.out.println(dealId);
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


    public void setData(String url) {
        this.url = url;
    }

    public String getData() {
        return url;
    }

//    @Override
//    public void onBackPressed() {
////        if(fragmentTransaction.getBackStackEntryCount())
//        super.onBackPressed();
//    }
}
