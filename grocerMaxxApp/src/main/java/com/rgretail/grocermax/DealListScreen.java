package com.rgretail.grocermax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;
import com.rgretail.grocermax.adapters.ProductListAdapter;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.DealListBean;
import com.rgretail.grocermax.bean.ProductDetailsListBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HotOffersActivity;
import com.rgretail.grocermax.utils.UrlsConstants;

import java.util.List;


public class DealListScreen extends BaseActivity implements AbsListView.OnScrollListener {
    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 10;
    int totalItemCount = 10;
    int currentScrollState = SCROLL_STATE_IDLE;
    boolean isLoading = false;
    int itemPerPage = 10;
    boolean hasMoreItem = true;
    private String header = "";
    private ListView mList;
    ProductListAdapter mAdapter;
    private DealListBean productListBean;
    private Product product;
    public int pageNo = 1;
    String cat_id = "";
    List<Product> product_list;
//    View footerView;
    public static int clickStatus=0;
//    TextView tv_bradcrum;
    View hrc;
    EasyTracker tracker;
    public static String strDealHeading = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                productListBean = (DealListBean) bundle
                        .getSerializable("ProductList");

                header = bundle.getString("Header");
                cat_id = bundle.getString("cat_id");
            }
            if (header.equals("")) {
                header = DealListScreen.strDealHeading;
            }
            setContentView(R.layout.activity_deal_list_screen);

//            if(!header.equals("")){
//                header = AppConstants.strTitleHotDeal;
//            }

//            if (productListBean.getProduct().size() < itemPerPage) {
//                hasMoreItem = false;
//            } else {
//                hasMoreItem = true;
//            }

//            TextView tvHeader = (TextView) findViewById(R.id.tv_your_cart);
//            tvHeader.setText(header);

//            tv_bradcrum = (TextView) findViewById(R.id.tv_Bradcrum);
//            hrc = (View) findViewById(R.id.hrc_Bradcrum);
//            if (MySharedPrefs.INSTANCE.getBradecrum().equals("")) {
//                tv_bradcrum.setVisibility(View.VISIBLE);
//                hrc.setVisibility(View.VISIBLE);
//                tv_bradcrum.setText("Search result for '" + MySharedPrefs.INSTANCE.getSearchKey() + "'");
//                tv_bradcrum.setClickable(false);
//                tv_bradcrum.setEnabled(false);
//            } else {
//                hrc.setVisibility(View.VISIBLE);
//                tv_bradcrum.setVisibility(View.VISIBLE);
//                tv_bradcrum.setText(MySharedPrefs.INSTANCE.getBradecrum());
//                tv_bradcrum.setClickable(true);
//                tv_bradcrum.setEnabled(true);
//            }
//            tv_bradcrum.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });

            addActionsInFilter(MyReceiverActions.PRODUCT_CONTENT_LIST);
            addActionsInFilter(MyReceiverActions.PRODUCT_LIST);
            addActionsInFilter(MyReceiverActions.ADD_TO_CART);

            mList = (ListView) findViewById(R.id.category_list);
//            footerView = (LinearLayout) findViewById(R.id.load_more_progressBar);
            product_list = productListBean.getProduct();
            Activity activity = (Activity) DealListScreen.this;
            mAdapter = new ProductListAdapter(activity, product_list);
            mList.setAdapter(mAdapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {

//                    if (clickStatus == 0) {
//                        clickStatus = 1;
//                        product = productListBean.getProduct().get(position);
//                        MySharedPrefs.INSTANCE.putItemQuantity(productListBean.getProduct().get(position).getQuantity());
//                        showDialog();
//                        String url = UrlsConstants.PRODUCT_DETAIL_URL
//                                + product.getProductid();
//                        myApi.reqProductContentList(url);
//                    }

                    if (productListBean != null) {
                        product = productListBean.getProduct().get(position);
                        MySharedPrefs.INSTANCE.putItemQuantity(productListBean.getProduct().get(position).getQuantity());
                        showDialog();
                        String url = UrlsConstants.PRODUCT_DETAIL_URL
                                + product.getProductid();
                        myApi.reqProductContentList(url);
                    }


                }
            });
            mList.setOnScrollListener(this);

            initHeader(findViewById(R.id.header), true, header);
            initFooter(findViewById(R.id.footer), 0, -1);
        }catch(Exception e){
            new GrocermaxBaseException("DealListScreen","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    public void OnResponse(Bundle bundle) {
        try{
            String action = bundle.getString("ACTION");
            if (action.equals(MyReceiverActions.PRODUCT_CONTENT_LIST)) {
                ProductDetailsListBean contentListBean = (ProductDetailsListBean) bundle
                        .getSerializable(ConnectionService.RESPONSE);
                if (contentListBean.getFlag().equalsIgnoreCase("1")) {
                    Intent call = new Intent(mContext, ProductDetailScreen.class);
                    Bundle call_bundle = new Bundle();
                    call_bundle.putSerializable("ProductContent", contentListBean
                            .getProductDetail().get(0));
                    call_bundle.putSerializable("Product", product);
                    call.putExtras(call_bundle);
                    startActivity(call);
                } else {
                    UtilityMethods.customToast(contentListBean.getResult(), mContext);
                }
            } else if (action.equals(MyReceiverActions.ADD_TO_CART)) {
                if (action.equalsIgnoreCase(MyReceiverActions.ADD_TO_CART)) {
                    BaseResponseBean bean = (BaseResponseBean) bundle
                            .getSerializable(ConnectionService.RESPONSE);
                    if (bean.getFlag().equalsIgnoreCase("1")) {
                        UtilityMethods.customToast(Constants.ToastConstant.PRODUCT_ADDED_CART, mContext);
                        MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
                        MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
                        cart_count_txt.setText(String.valueOf(bean.getTotalItem()));
                    } else {
                        UtilityMethods.customToast(bean.getResult(), mContext);
                    }
                }
            } else if (action.equals(MyReceiverActions.PRODUCT_LIST)) {
                isLoading = false;
                DealListBean productListBean = (DealListBean) bundle
                        .getSerializable(ConnectionService.RESPONSE);
                if (productListBean.getFlag().equalsIgnoreCase("1")) {
                    if (productListBean.getProduct().size() < itemPerPage) {
                        hasMoreItem = false;
                    } else {
                        hasMoreItem = true;
                    }
//                    footerView.setVisibility(View.GONE);
                    for(int i=0;i<productListBean.getProduct().size();i++)
                        productListBean.getProduct().get(i).setQuantity("1");
                    product_list.addAll(productListBean.getProduct());
                    mAdapter.updateList(product_list);
                } else {
                    UtilityMethods.customToast(productListBean.getResult(), mContext);
                }
            }
        }catch(Exception e){
            new GrocermaxBaseException("DealListScreen","onResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

//    public void addToCart(String product_id, String quantity) {
//        showDialog();
//
//        try {
//            JSONArray products = new JSONArray();
//            JSONObject prod_obj = new JSONObject();
//            prod_obj.put("productid", product_id);
//            prod_obj.put("quantity", quantity);
//            products.put(prod_obj);
//
//            String url;
//            if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
//            {
//                url = UrlsConstants.ADD_TO_CART_URL
//                        + MySharedPrefs.INSTANCE.getUserId() +"&products="
//                        + URLEncoder.encode(products.toString(), "UTF-8");
//            }
//            else
//            {
//                url = UrlsConstants.ADD_TO_CART_URL
//                        + MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//                        + URLEncoder.encode(products.toString(), "UTF-8");
//            }
//
//
//
//			/*String url = UrlsConstants.ADD_TO_CART_URL
//					+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&quote_id=&products="
//					+ URLEncoder.encode(products.toString(), "UTF-8");*/
//            myApi.reqAddToCart(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            new GrocermaxBaseException("DealListScreen","addToCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
//        }
//
//    }

//    public void addToCartGuest(String product_id, String quantity) {
//        showDialog();
//        try {
//            JSONArray products = new JSONArray();
//            JSONObject prod_obj = new JSONObject();
//            prod_obj.put("productid", product_id);
//            prod_obj.put("quantity", quantity);
//            products.put(prod_obj);
//            String url;
//
//            url = UrlsConstants.ADD_TO_CART_GUEST_URL+"quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//                    + URLEncoder.encode(products.toString(), "UTF-8");
//            myApi.reqAddToCart(url);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            new GrocermaxBaseException("DealListScreen","addToCartGuest",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
//        }
//    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        try{
//            this.currentFirstVisibleItem = firstVisibleItem;
//            this.currentVisibleItemCount = visibleItemCount;
//            this.totalItemCount = totalItemCount;
        }catch(Exception e){
            new GrocermaxBaseException("ProductListScreen","onScroll",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        try{
//            this.currentScrollState = scrollState;
//            this.isScrollCompleted();
        }catch(Exception e){
            new GrocermaxBaseException("DealListScreen","onScrollStateChanged",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
        }
    }

    private void isScrollCompleted() {
//        try{
//            if (this.currentVisibleItemCount + this.currentFirstVisibleItem >= totalItemCount
//                    && this.currentScrollState == SCROLL_STATE_IDLE) {
//                /***
//                 * In this way I detect if there's been a scroll which has completed
//                 ***/
//                /*** do the work for load more date! ***/
//                if (!isLoading) {
//                    //isLoading = true;
////                    loadMoreData();
//                }
//            }
//        }catch(Exception e){
//            new GrocermaxBaseException("DealListScreen","isScrollCompleted",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
//        }
    }

//    private void loadMoreData() {
//        try{
//            if (UtilityMethods.isInternetAvailable(mContext)) {
//                if (hasMoreItem) {
//                    // mList.addFooterView(footerView);
//                    pageNo++;
//                    String url;
//                    footerView.setVisibility(View.VISIBLE);
//                    if (MySharedPrefs.INSTANCE.getIsSearched()) {
//                        String search_key = MySharedPrefs.INSTANCE.getSearchKey()
//                                .trim();
//                        url = UrlsConstants.SEARCH_PRODUCT + search_key + "&page="
//                                + pageNo;
//                    } else {
//                        MySharedPrefs.INSTANCE.putIsSearched(false);
//                        url = UrlsConstants.PRODUCT_LIST_URL + cat_id + "&page="
//                                + pageNo;
//                    }
//                    myApi.reqProductList(url);
//                } else {
//                    UtilityMethods.customToast(Constants.ToastConstant.listFull, this);
//                }
//            } else {
//                UtilityMethods.customToast(Constants.ToastConstant.msgNoInternet, this);
//
//            }
//        }catch(Exception e){
//            new GrocermaxBaseException("DealListScreen","loadMoreData",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
//        }
//    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try{
            initHeader(findViewById(R.id.header), true, header);
            clickStatus=0;
        }catch(Exception e){
            new GrocermaxBaseException("DealListScreen","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            EasyTracker.getInstance(this).activityStart(this);
            FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
            FlurryAgent.onPageView();         //Use onPageView to report page view count.
        }catch(Exception e){}
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try{
            EasyTracker.getInstance(this).activityStop(this);
            FlurryAgent.onEndSession(this);
        }catch(Exception e){}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if(HotOffersActivity.bFromHome) {
                HotOffersActivity.isFromFragment = false;    //work fine for home
            }else{
                HotOffersActivity.isFromFragment = true;
            }
        }catch(Exception e){}
    }
}
