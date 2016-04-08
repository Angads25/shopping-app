package com.rgretail.grocermax.hotoffers;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.google.gson.Gson;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.DealListScreen;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.UnderMaintanance;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.api.SearchLoader;
import com.rgretail.grocermax.bean.DealByDealTypeBean;
import com.rgretail.grocermax.bean.DealListBean;
import com.rgretail.grocermax.bean.DealProductListingBean;
import com.rgretail.grocermax.bean.HomeBannerBean;
import com.rgretail.grocermax.bean.OfferByDealTypeBean;
import com.rgretail.grocermax.bean.OfferByDealTypeModel;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.bean.ShopByCategoryBean;
import com.rgretail.grocermax.bean.ShopByDealsBean;
import com.rgretail.grocermax.hotoffers.fragment.ExpandableMenuFragment;
import com.rgretail.grocermax.hotoffers.fragment.HomeFragment;
import com.rgretail.grocermax.hotoffers.fragment.MenuFragment;
import com.rgretail.grocermax.hotoffers.fragment.ShopByDealItemDetailFragment;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.DataHandler;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeScreen extends BaseActivity {


    public ShopByCategoryBean shopByCategoryBean;
    public OfferByDealTypeBean offerByDealTypeBean;
    public DealByDealTypeBean dealByDealTypeBean;
    public HomeBannerBean homeBannerBean;
    private ImageView menuIcon, homeDrawer;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private ArrayList<OfferByDealTypeModel> arrayList;
    private ShopByDealsBean shopByDealsBean;
    private ShopByDealsBean shopBySpecialDealsBean;
    private DealProductListingBean dealProductListingBean;
    private ProgressDialog progress;
    private String url;
    private DrawerLayout drawerLayout;
    public ArrayList<CategorySubcategoryBean> catObj;
    public static boolean isFirstFragment = true;
    private ArrayList<String> menuArray = new ArrayList<>();
    public static boolean isFromFragment = false;
    boolean isFromNotification=false;
    public static boolean bFromHome = false;          //track for home screen(1st level fragment) fragment and deal detail fragment(2nd level fragment)
    public static Context mContext;
    public static String SCREENNAME = "HomeScreen-";

    public static ArrayList<Product> autoSuggestList = new ArrayList<Product>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot_offer);
        mContext = this;
        System.out.println("Notification testing1");
        try{

        }catch(Exception e){}


        addActionsInFilter(MyReceiverActions.GET_HOME_PAGE);
        addActionsInFilter(MyReceiverActions.GET_SHOP_BY_CATEGORIES);
        addActionsInFilter(MyReceiverActions.GET_SHOP_BY_DEALS);
        addActionsInFilter(MyReceiverActions.GET_BANNER);
        addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);
        addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
        addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
        addActionsInFilter(MyReceiverActions.ALL_PRODUCTS_CATEGORY);
        addActionsInFilter(MyReceiverActions.LOCATION);
        addActionsInFilter(MyReceiverActions.CATEGORY_LIST);

        menuIcon = (ImageView) findViewById(R.id.menuIcon);
        homeDrawer = (ImageView) findViewById(R.id.homedrawer);
//        martHeader.setVisibility(View.VISIBLE);
       /* Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean("IS_FROM_NOTIFICATION", false)) {
            getNotificationData(bundle);
            isFromNotification = true;
        }
        else {
            setHomePage();
        }*/
        setHomePage();
    }

    private android.support.v4.app.FragmentManager.OnBackStackChangedListener getListener()
    {
        android.support.v4.app.FragmentManager.OnBackStackChangedListener result = new android.support.v4.app.FragmentManager.OnBackStackChangedListener()
        {
            public void onBackStackChanged()
            {
                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                if (manager != null)
                {
//                    MyFragment currFrag = (MyFragment)manager.
//                            findFragmentById(R.id.fragmentItem);
//                    currFrag.onFragmentResume();
//                    HomeFragment homeFragment = (HomeFragment) manager.findFragmentById(R.id.fragment);
                }
            }
        };

        return result;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                this.getSupportFragmentManager().popBackStack();
            } else {
                getDrawerLayout().closeDrawers();
            }
        } else {
            if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                this.getSupportFragmentManager().popBackStack();

//                if (this.getSupportFragmentManager().getBackStackEntryCount() == 1) {
//                    findViewById(R.id.header).setVisibility(View.VISIBLE);
//                    findViewById(R.id.header_left).setVisibility(View.GONE);
//                } else if (this.getSupportFragmentManager().getBackStackEntryCount() == 2) {
//                    findViewById(R.id.header).setVisibility(View.GONE);
//                    findViewById(R.id.header_left).setVisibility(View.VISIBLE);
//                }
            } else {

                super.onBackPressed();
            }
        }
    }


    public void setMenu(ArrayList<CategorySubcategoryBean> arrayList, String name) {
        Bundle call_bundle = new Bundle();
        call_bundle.putSerializable("Categories", arrayList);
        call_bundle.putString("Title", name);
        call_bundle.putSerializable(Constants.SHOP_BY_DEALS_MODEL, shopByDealsBean);
        call_bundle.putBoolean("isListView", false);
        call_bundle.putBoolean("isFromDrawer", true);
        ExpandableMenuFragment fragment = new ExpandableMenuFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.menu, fragment);
//        fragment.setArguments(call_bundle);
//        fragmentTransaction.commit();

//        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
//        FragmentTransaction ft = manager.beginTransaction();
//        fragmentTransaction.add(R.id.menu, fragment);
        fragmentTransaction.replace(R.id.menu, fragment, name);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_UNSET);
        fragment.setArguments(call_bundle);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
        try{
            RocqAnalytics.initialize(this);
            RocqAnalytics.startScreen(this);
        }catch(Exception e){}
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
        try{
            RocqAnalytics.stopScreen(this);
        }catch(Exception e){}
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
    }

    @Override
    public void onResume() {
        super.onResume();

        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}

       /* if(isFromNotification)
        {
            setHomePage();
            isFromNotification = false;
        }
*/
        if(!isFromFragment) {
            initHeader(findViewById(R.id.header), true, null);
            findViewById(R.id.header).setVisibility(View.VISIBLE);
            findViewById(R.id.header_left).setVisibility(View.GONE);
        }else{
//            isFromFragment =false;
            initHeader(findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
            findViewById(R.id.header).setVisibility(View.GONE);
            findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        }

//        initHeader(findViewById(R.id.header), true, null);

        if (isFromCategoryTabs && this.getSupportFragmentManager().getBackStackEntryCount() == 1) {
            isFromCategoryTabs = false;
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void OnResponse(Bundle bundle) {

        try {
            System.out.println("Notification testing2");
            String action = bundle.getString("ACTION");
//            if(action.equals(MyReceiverActions.CATEGORY_LIST)) {
//                dismissDialog();
//                String jsonResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
//                //UtilityMethods.write("response",jsonResponse,SplashScreen.this);
//                ArrayList<CategorySubcategoryBean> category = UtilityMethods.getCategorySubCategory(jsonResponse);
//                if (!jsonResponse.trim().equals("") && category.size() > 0) {
//                    UtilityMethods.writeCategoryResponse(HomeScreen.this, AppConstants.categoriesFile, jsonResponse);
//                    catObj = UtilityMethods.getCategorySubCategory(jsonResponse);
//                }
//            }

            if (action.equals(MyReceiverActions.GET_HOME_PAGE)) {
                try {
                    if(AppConstants.strUpgradeValue.equals("0") || AppConstants.strUpgradeValue.equals("2")){
                    System.out.println("RESPONSE" + bundle.getString("json"));
                    JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                    Gson gson = new Gson();
                    shopByCategoryBean = gson.fromJson(jsonObject.toString(), ShopByCategoryBean.class);

                    System.out.println("RESPONSE MODEL CATEGORY" + shopByCategoryBean.getResult());
                    System.out.println("RESPONSE MODEL CATEGORY1" + shopByCategoryBean.getArrayList().size());


                    JSONObject jsonO = new JSONObject(bundle.getString("json"));
                    Constants.base_url_category_image = jsonObject.getString("urlImg");                                                           //
                    JSONObject jsonArrayCccategory = jsonO.getJSONObject("Category");                                                             //
                    JSONArray jsonArrayCategory = jsonO.getJSONArray("category");
                    JSONArray jsonArrayBanner = jsonO.getJSONArray("banner");
                    JSONArray jsonDealType = jsonO.getJSONArray("deal_type");
                    JSONArray jsonSpecialDealType = jsonO.getJSONArray("specialdeal");

                     /*for storing search keyword*/

                        try {
                            JSONArray jsonTrendingData = jsonO.getJSONObject("trending").getJSONObject("Result").getJSONArray("data");
                            DataHandler db=new DataHandler(HomeScreen.this);
                            db.Open();
                            ContentValues values;
                            for(int i=0;i<jsonTrendingData.length();i++) {
                                values=new ContentValues();
                                values.put(Constants.DatabaseConstant.C_KEYWORD, jsonTrendingData.getJSONObject(i).getString("name1"));
                                values.put(Constants.DatabaseConstant.C_KEYWORD_STATUS, "1");
                                db.insertDataInSearch(Constants.DatabaseConstant.T_SEARCH_KEYWORD, values);
                            }
                            db.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                      /*  try {

                            for(int i=0;i<jsonTrendingData.length();i++){
                                Product p=new Product(jsonTrendingData.getJSONObject(i).getString("name1"));
                                autoSuggestList.add(p);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        /*--------------------------*/

                        JSONObject jsonOCccategory = new JSONObject();                                                                                  //
                    jsonOCccategory.put("Category", jsonArrayCccategory);                                                                           //
                    ArrayList<CategorySubcategoryBean> categoryi = UtilityMethods.getCategorySubCategory(String.valueOf(jsonOCccategory));          //
//                    if (!jsonResponse.trim().equals("") && categoryi.size() > 0) {                                                               //
                    if (categoryi.size() > 0) {                                                                                                      //
//                        UtilityMethods.writeCategoryResponse(HomeScreen.this, AppConstants.categoriesFile, String.valueOf(categoryi));      //
//                        catObj = UtilityMethods.getCategorySubCategory(String.valueOf(categoryi));
                        catObj = categoryi;                                                                                                          //
                    }

                    JSONObject jsonOCategory = new JSONObject();
                    jsonOCategory.put("category", jsonArrayCategory);
                    Gson gson1 = new Gson();
                    shopByCategoryBean = gson1.fromJson(jsonOCategory.toString(), ShopByCategoryBean.class);       //

                    JSONObject jsonOBanner = new JSONObject();
                    jsonOBanner.put("banner", jsonArrayBanner);
                    Gson gson2 = new Gson();
                    homeBannerBean = gson2.fromJson(jsonOBanner.toString(), HomeBannerBean.class);             //

                    JSONObject jsonODealType = new JSONObject();
                    jsonODealType.put("deal_type", jsonDealType);
                    Gson gson3 = new Gson();
                    shopByDealsBean = gson3.fromJson(jsonODealType.toString(), ShopByDealsBean.class);

                    JSONObject jsonOSpecialDealType = new JSONObject();
                    jsonOSpecialDealType.put("specialdeal", jsonSpecialDealType);
                    Gson gson4 = new Gson();
                    shopBySpecialDealsBean = gson4.fromJson(jsonOSpecialDealType.toString(), ShopByDealsBean.class);


                    if (shopByCategoryBean != null && shopByDealsBean != null && homeBannerBean != null && catObj != null) {
                        HomeFragment fragment = new HomeFragment();                                    //displaying home screen
                        dismissDialog();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        Bundle data = new Bundle();
                        data.putSerializable(Constants.SHOP_BY_CATEGORY_MODEL, shopByCategoryBean);
                        data.putSerializable(Constants.SHOP_BY_DEALS_MODEL, shopByDealsBean);
                        data.putSerializable(Constants.HOME_BANNER, homeBannerBean);
                        data.putSerializable(Constants.SHOP_BY_SPECIAL_DEALS_MODEL, shopBySpecialDealsBean);
                        fragment.setArguments(data);
                        fragmentTransaction.commit();

                        Bundle call_bundle = new Bundle();
                        call_bundle.putSerializable("Categories", catObj);
                        call_bundle.putSerializable(Constants.SHOP_BY_DEALS_MODEL, shopByDealsBean);
                        call_bundle.putString("Title", "Home");
                        call_bundle.putBoolean("isListView", true);
                        MenuFragment fragment1 = new MenuFragment();                                  //displaying drawer
                        android.support.v4.app.FragmentTransaction fragmentTransact = getSupportFragmentManager().beginTransaction();
                        fragmentTransact.add(R.id.menu, fragment1);
                        fragment1.setArguments(call_bundle);
                        fragmentTransact.commit();
                        progress.dismiss();


                        /*  coming form notification */
                        try {
                            Bundle bundle1 = getIntent().getExtras();
                            if (bundle1 != null && bundle1.getBoolean("IS_FROM_NOTIFICATION", false)) {
                                getNotificationData(bundle1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /*  coming form deep linking and rocq notification*/
                        try {
                            Intent intent = getIntent();
                            if (intent != null || intent.getData() != null) {
                                Bundle bundle2=new Bundle();
                                String path=intent.getData().toString();
                                path=path.replace("grocermax://","");
                                System.out.println("path=" + path);
                                if(path.contains("&")){
                                    String url_title[]=path.split("&");
                                    bundle2.putString("linkurl", url_title[0]);
                                    if(url_title.length>1)
                                     bundle2.putString("name", url_title[1].split("=")[1]);
                                }else{
                                    bundle2.putString("linkurl", path);
                                    bundle2.putString("name", "");
                                }
                                getNotificationData(bundle2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                        if(AppConstants.strUpgradeValue.equals("2")){
                            dismissDialog();
                            UtilityMethods.downloadPopUpNew(this, AppConstants.strUpgradeValue);
                        }

                }else if(AppConstants.strUpgradeValue.equals("1")){
                    dismissDialog();
                    UtilityMethods.downloadPopUpNew(this,AppConstants.strUpgradeValue);
                }else if(AppConstants.strUpgradeValue.equals("3")){
                        dismissDialog();
                        startActivity(new Intent(HomeScreen.this, UnderMaintanance.class));
                    }

                } catch (Exception e) {
                }
            }

//            if (action.equals(MyReceiverActions.GET_SHOP_BY_CATEGORIES) || action.equals(MyReceiverActions.GET_SHOP_BY_DEALS) || action.equals(MyReceiverActions.GET_BANNER)) {
//                if (action.equals(MyReceiverActions.GET_SHOP_BY_CATEGORIES)) {
//
//                    System.out.println("RESPONSE" + bundle.getString("json"));
//                    JSONObject jsonObject = new JSONObject(bundle.getString("json"));
//                    Gson gson = new Gson();
//                    shopByCategoryBean = gson.fromJson(jsonObject.toString(), ShopByCategoryBean.class);
//
//                    System.out.println("RESPONSE MODEL CATEGORY" + shopByCategoryBean.getResult());
//                    System.out.println("RESPONSE MODEL CATEGORY1" + shopByCategoryBean.getArrayList().size());
//
//                    JSONObject jsonO = new JSONObject(bundle.getString("json"));
//                    jsonO.getString("Result");
//                    JSONArray jsonArrayCategory = jsonO.getJSONArray("category");
//                    JSONArray jsonArrayBanner = jsonO.getJSONArray("banner");
//                    JSONArray jsonDealType = jsonO.getJSONArray("deal_type");
//
//                    JSONObject jsonOCategory= new JSONObject();
//                    jsonOCategory.put("category",jsonArrayCategory);
//                    Gson gson1 = new Gson();
//                    shopByCategoryBean = gson1.fromJson(jsonOCategory.toString(), ShopByCategoryBean.class);       //
//
//                    JSONObject jsonOBanner= new JSONObject();
//                    jsonOBanner.put("banner",jsonArrayBanner);
//                    Gson gson2 = new Gson();
//                    homeBannerBean = gson2.fromJson(jsonOBanner.toString(), HomeBannerBean.class);             //
//
//                    JSONObject jsonODealType= new JSONObject();
//                    jsonODealType.put("deal_type",jsonDealType);
//                    Gson gson3 = new Gson();
//                    shopByDealsBean = gson3.fromJson(jsonODealType.toString(), ShopByDealsBean.class);
//
//                } else if (action.equals(MyReceiverActions.GET_SHOP_BY_DEALS)) {
//                    System.out.println("RESPONSE" + bundle.getString("json"));
//                    JSONObject jsonObject = new JSONObject(bundle.getString("json"));
//                    Gson gson = new Gson();
//                    shopByDealsBean = gson.fromJson(jsonObject.toString(), ShopByDealsBean.class);
//
//                    System.out.println("RESPONSE MODEL" + shopByDealsBean.getResult());
//                    System.out.println("RESPONSE MODEL" + shopByDealsBean.getArrayList().size());
//                } else if (action.equals(MyReceiverActions.GET_BANNER)) {
//                    System.out.println("RESPONSE" + bundle.getString("json"));
//                    JSONObject jsonObject = new JSONObject(bundle.getString("json"));
//                    Gson gson = new Gson();
//                    homeBannerBean = gson.fromJson(jsonObject.toString(), HomeBannerBean.class);
//                }
//
//                if (shopByCategoryBean != null && shopByDealsBean != null && homeBannerBean != null) {
//                    HomeFragment fragment = new HomeFragment();
//                    dismissDialog();
//                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frame, fragment);
//                    Bundle data = new Bundle();
//                    data.putSerializable(Constants.SHOP_BY_CATEGORY_MODEL, shopByCategoryBean);
//                    data.putSerializable(Constants.SHOP_BY_DEALS_MODEL, shopByDealsBean);
//                    data.putSerializable(Constants.HOME_BANNER, homeBannerBean);
//                    fragment.setArguments(data);
//                    fragmentTransaction.commit();
//
//                    Bundle call_bundle = new Bundle();
//                    call_bundle.putSerializable("Categories", catObj);
//                    call_bundle.putSerializable(Constants.SHOP_BY_DEALS_MODEL, shopByDealsBean);
//                    call_bundle.putString("Title", "Home");
//                    call_bundle.putBoolean("isListView", true);
//                    MenuFragment fragment1 = new MenuFragment();
//                    android.support.v4.app.FragmentTransaction fragmentTransact = getSupportFragmentManager().beginTransaction();
//                    fragmentTransact.add(R.id.menu, fragment1);
//                    fragment1.setArguments(call_bundle);
//                    fragmentTransact.commit();
//
//                    progress.dismiss();
//                }
//
//
//            }
            else if (action.equals(MyReceiverActions.OFFER_BY_DEALTYPE)) {       //responsible for getting data after click of offers [which is showing on bottom of Shop by categories]
                dismissDialog();
                System.out.println("RESPONSE OFFER" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                /*Gson gson = new Gson();
                offerByDealTypeBean = gson.fromJson(jsonObject.toString(), OfferByDealTypeBean.class);
                ItemDetailFragment fragment = new ItemDetailFragment();
                Bundle data = new Bundle();
                data.putBoolean(Constants.SHOP_BY_DEAL, false);
                data.putSerializable(Constants.OFFER_BY_DEAL, offerByDealTypeBean);
                fragment.setArguments(data);
                changeFragment(fragment);*/

                Gson gson = new Gson();
                dealByDealTypeBean = gson.fromJson(jsonObject.toString(), DealByDealTypeBean.class);
                ShopByDealItemDetailFragment fragment = new ShopByDealItemDetailFragment();
                Bundle data = new Bundle();
                data.putBoolean(Constants.SHOP_BY_DEAL, true);
                data.putSerializable(Constants.DEAL_BY_DEAL, dealByDealTypeBean);
                fragment.setArguments(data);
                changeFragment(fragment);

            } else if (action.equals(MyReceiverActions.DEAL_BY_DEALTYPE)) {     //responsible for click of deals [which is showing on shop by deals]
                dismissDialog();
                System.out.println("RESPONSE OFFER" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
                dealByDealTypeBean = gson.fromJson(jsonObject.toString(), DealByDealTypeBean.class);
                ShopByDealItemDetailFragment fragment = new ShopByDealItemDetailFragment();
                Bundle data = new Bundle();
                data.putBoolean(Constants.SHOP_BY_DEAL, true);
                data.putSerializable(Constants.DEAL_BY_DEAL, dealByDealTypeBean);
                fragment.setArguments(data);
                changeFragment(fragment);

            } else if (action.equals(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE)) {         //responsible for product listing through deals [ShopByDealItemDetailFragment -> DealListScreen]

//                DealListBean dealListBean = (DealListBean) bundle
                //                      .getSerializable(ConnectionService.RESPONSE);
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                JSONObject json = jsonObject.getJSONObject("Product");
                Gson gson = new Gson();
                DealListBean dealListBean = gson.fromJson(json.toString(), DealListBean.class);
//                System.out.println("RESPONSE DEALLISTING" + dealProductListingBean.getProduct().size());
                if (dealListBean == null) {
                    UtilityMethods.customToast(AppConstants.ToastConstant.NO_PRODUCT, mContext);
                    return;
                }
                Intent call = new Intent(mContext,DealListScreen.class);
                Bundle call_bundle = new Bundle();
                call_bundle.putSerializable("ProductList",dealListBean);
                if(!AppConstants.strTitleHotDeal.equals(""))
                call_bundle.putSerializable("Header", AppConstants.strTitleHotDeal);
                else
                call_bundle.putSerializable("Header", DealListScreen.strDealHeading);
                call.putExtras(call_bundle);
                startActivity(call);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (this.getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            findViewById(R.id.header).setVisibility(View.VISIBLE);
//            findViewById(R.id.header_left).setVisibility(View.GONE);
//        }


//        progress.dismiss();
    }

    public ShopByDealsBean getShopByDeals() {
        return shopByDealsBean;
    }

    //responsible for opening offers of shop by category
    public void hitForShopByCategory(String categoryId) {
        String url = UrlsConstants.OFFER_BY_DEAL_TYPE;
        showDialog();
        myApi.reqOfferByDealType(url + categoryId);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }


    //responsible for opening deals category
    public void hitForShopByDeals(String dealId) {

        showDialog();
        String url = UrlsConstants.DEAL_BY_DEAL_TYPE;
        System.out.print("==my work==" + url);
        myApi.reqDealByDealType(url + dealId);
    }

    public void hitForDealsByDeals(String dealId) {            //responsible for clicking of [shop by deals -> ShopByDealItemDetailFragment -> DealListScreen]

        String url = UrlsConstants.PRODUCTLISTING_BY_DEAL_TYPE;
        showDialog();
        myApi.reqProductListingByDealType(url + dealId);
        System.out.println(dealId);
    }

    public void hitForSpecialDealsByDeals(String sku) {            //responsible for clicking of [shop by deals -> ShopByDealItemDetailFragment -> DealListScreen]

        String url = UrlsConstants.PRODUCTLISTING_BY_SPECIAL_DEAL_TYPE;
        showDialog();
        myApi.reqProductListingByDealType(url + sku);
        System.out.println(sku);
    }

    public void changeFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
//            fragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
        }
        fragmentTransaction.replace(R.id.frame, fragment);
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

    public void getNotificationData(Bundle data)
    {
        String strName = data.getString("name");
        String strLinkurl = data.getString("linkurl");
        //String strImageUrl = data.getString("imageurl");
        int index = 0;
        String strType = "";
        if (strLinkurl.contains("?")) {
            index = strLinkurl.indexOf("?");
            if (strLinkurl.length() >= index) {
                strType = strLinkurl.substring(0, index);
                System.out.println("====result is====" + strType);
            }
        } else {
            strType = strLinkurl;
        }

        AppConstants.strTitleHotDeal = strName;
        if(strType.equalsIgnoreCase("dealproductlisting")){
            addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
            String url = UrlsConstants.NEW_BASE_URL;
            showDialog();
            myApi.reqProductListingByDealType(url + strLinkurl);
        } else if (strType.equalsIgnoreCase("dealsbydealtype")) {
            addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
            showDialog();
            String url = UrlsConstants.NEW_BASE_URL;
            myApi.reqDealByDealType(url + strLinkurl);
        } else if (strType.equalsIgnoreCase("productlistall")) {
            addActionsInFilter(MyReceiverActions.ALL_PRODUCTS_CATEGORY);
            showDialog();
            String strUrl = UrlsConstants.NEW_BASE_URL;
            myApi.reqAllProductsCategory(strUrl + strLinkurl);
            System.out.println("===complete url====" + strUrl + strLinkurl);
        } else if (strType.equalsIgnoreCase("shopbydealtype")) {


        } else if (strType.equalsIgnoreCase("search")) {
            String strSearch = "";
            index = strLinkurl.indexOf("?");
            int indexequal = strLinkurl.indexOf("=");
            if (strLinkurl.length() >= index) {
                strSearch = strLinkurl.substring(indexequal + 1, strLinkurl.length());
                System.out.println("====indexequals is====>>" + strSearch);
            }

            System.out.println("====values OF is====" + strLinkurl);
            String url = UrlsConstants.BANNER_SEARCH_PRODUCT + strLinkurl;
            url = url.replace(" ", "%20");
            if(strSearch.contains("&")){
                strSearch=strSearch.split("&")[0];
            }
            SearchLoader searchLoader = new SearchLoader(this, strSearch,"");
            searchLoader.execute(url);
        } else if (strType.equalsIgnoreCase("offerbydealtype")) {
            String strId = "";
            index = strLinkurl.indexOf("?");
            int indexequal = strLinkurl.indexOf("=");
            if (strLinkurl.length() >= index) {
                strId = strLinkurl.substring(indexequal + 1, strLinkurl.length());
                System.out.println("====indexequals is====>>" + strId);
            }
            addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);
            hitForShopByCategory(strId);
        } else if(strType.equalsIgnoreCase("productdetail")){

            AppConstants.strPopupData="";
            showDialog();
            String url = UrlsConstants.NEW_BASE_URL + strLinkurl;
            myApi.reqProductDetailFromNotification(url);
        }
    }

    public void setHomePage()
    {
        progress = new ProgressDialog(this);
        progress.setTitle(null);
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

         /*this code was used to send gcm registration id for first time only*/

        /*if(MySharedPrefs.INSTANCE.getGCMDeviceTockenFirst()==null){
            myApi.reqHome(UrlsConstants.GET_HOME_PAGE+"?device_token="+ MySharedPrefs.INSTANCE.getGCMDeviceTocken()+"&device_id="+UtilityMethods.getDeviceId(HomeScreen.this));
            MySharedPrefs.INSTANCE.putGCMDeviceTockenFirst(MySharedPrefs.INSTANCE.getGCMDeviceTocken());
        }else{
          myApi.reqHome(UrlsConstants.GET_HOME_PAGE+"?device_token=");
        }*/

        myApi.reqHome(UrlsConstants.GET_HOME_PAGE+"?device_token="+ MySharedPrefs.INSTANCE.getGCMDeviceTocken()+"&device_id="+UtilityMethods.getDeviceId(HomeScreen.this));


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

        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        try {
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        }catch(Exception e){}

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawers();
                    //try{UtilityMethods.clickCapture(mContext,"","","","",SCREENNAME+AppConstants.CLOSE_DRAWER_MENU);}catch(Exception e){}
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                    try{
                        UtilityMethods.clickCapture(mContext,"Open Drawer","","","",MySharedPrefs.INSTANCE.getSelectedCity());
                        RocqAnalytics.trackEvent("Open Drawer", new ActionProperties("Category", "Open Drawer", "Action", MySharedPrefs.INSTANCE.getSelectedCity()));
                    }catch(Exception e){}
                }
            }
        });

//        homeDrawer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
//                    drawerLayout.closeDrawers();
//                } else {
//                    drawerLayout.openDrawer(Gravity.LEFT);
//                }
//            }
//        });

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        try{
            //calling sync state is necessay or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();
        }catch(Exception e){}

    }
}
