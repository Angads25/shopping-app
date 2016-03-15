package com.rgretail.grocermax;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.adapters.ProductListAdapter;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.bean.ProductListBean;
import com.rgretail.grocermax.bean.ShopByCategoryModel;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anchit-pc on 04-Mar-16.
 */
public class CategoryActivity1 extends BaseActivity {


    String strCatName;                          //shown on header of screen
    ArrayList<CategorySubcategoryBean> alcatObjSend;
    public ArrayList<CategorySubcategoryBean> catObj;
    private ArrayList<ShopByCategoryModel> data;
    int mainCatPosition = 0;
    int mainCatLength = 9;
    GridView subCatGrid;
    ExpandableHeightListView lv_top_product;
    private List<Product> product_list;
    private ProductListAdapter mAdapter;

    ProductListBean listBean;
    String strCatIdByCat = "";

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_category1);

            addActionsInFilter(MyReceiverActions.TOP_PRODUCTS_LIST);
            addActionsInFilter(MyReceiverActions.ALL_PRODUCTS_CATEGORY);
            addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);

            try {
                AppsFlyerLib.setCurrencyCode("INR");
                AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
                AppsFlyerLib.sendTracking(getApplicationContext());
            } catch (Exception e) {}

            subCatGrid=(GridView)findViewById(R.id.sub_catg_grid);
            lv_top_product=(ExpandableHeightListView)findViewById(R.id.top_product_list);
            lv_top_product.setExpanded(true);


            Bundle bundle = getIntent().getExtras();
            ArrayList<CategorySubcategoryBean> alSubCat;
            if (bundle != null) {
                try {
                    catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");//main category name on left side like staples
                    data = (ArrayList<ShopByCategoryModel>) bundle.getSerializable("data");
                    strCatName = bundle.getString("CategoryName");
                    strCatIdByCat = bundle.getString("CategoryId");
                    String str = bundle.getString("maincategoryposition");
                    mainCatPosition = Integer.parseInt(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < catObj.size(); i++) {
                if (catObj.get(i).getCategoryId().equals(strCatIdByCat)) {
                    mainCatPosition = i;
                    strCatName = catObj.get(i).getCategory();
                }
            }

            alSubCat = catObj.get(mainCatPosition).getChildren();                   //under main category [right side top category e.g. dryfruits]

            alcatObjSend = new ArrayList<CategorySubcategoryBean>();
            for (int i = 0; i < alSubCat.size(); i++) {
                if (alSubCat.get(i).getIsActive().equals("1")) {
                    alcatObjSend.add(alSubCat.get(i));
                }
            }

            subCatGrid.setAdapter(new SubCategoryListAdapter());
            setGridViewHeightBasedOnChildren(subCatGrid, 4);

            initHeader(findViewById(R.id.app_bar_header), true, strCatName);

            lv_top_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    if (listBean != null) {
                        if (listBean.getProduct().size()>0) {
                            MySharedPrefs.INSTANCE.putItemQuantity(listBean.getProduct().get(position).getQuantity());
                            showDialog();
                            String url = UrlsConstants.PRODUCT_DETAIL_URL + listBean.getProduct().get(position).getProductid();
                            myApi.reqProductDetailFromNotification(url);
                        }
                    }
                }
            });



            try {
                showDialog();
                myApi.reqTopProduct(UrlsConstants.TOP_PRODUCT_URL + strCatIdByCat);
            } catch (Exception e) {
                new GrocermaxBaseException("CategoryActivity","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting top product");
            }
        }catch (Exception e){
        }
    }

    public void getTopOffers(View v){
        try {
            if (Integer.parseInt(data.get(mainCatPosition).getOffercount()) > 0) {
                String url = UrlsConstants.OFFER_BY_DEAL_TYPE;
                showDialog();
                myApi.reqOfferByDealType(url + strCatIdByCat);
                for (int i = 0; i < catObj.size(); i++) {
                    if (catObj.get(i).getCategoryId().equals(data.get(mainCatPosition).getCategory_id())) {
                        AppConstants.strTitleHotDeal = catObj.get(i).getCategory();
                    }
                }
            }
        }catch(Exception e){}
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
            initHeader(findViewById(R.id.app_bar_header), true, strCatName);
            AppsFlyerLib.onActivityResume(this);
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
    public void OnResponse(final Bundle bundle) {
        if (bundle.getString("ACTION").equals(MyReceiverActions.TOP_PRODUCTS_LIST)) {
            try
            {
                listBean = (ProductListBean) bundle.getSerializable(ConnectionService.RESPONSE);
                if (listBean != null)
                {
                    if (listBean.getProduct().size() > 0)
                    {
                        product_list = listBean.getProduct();
                        mAdapter = new ProductListAdapter(CategoryActivity1.this, product_list);
                        lv_top_product.setAdapter(mAdapter);
                    }
                    else {
                        product_list = new ArrayList<Product>();
                        Product product = new Product("No product found for this category");
                        product_list.add(product);
                        mAdapter = new ProductListAdapter(CategoryActivity1.this, product_list);
                        lv_top_product.setAdapter(mAdapter);
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissDialog();
                        }
                    }, 3000);
                }
            }catch(Exception e)
            {
                new GrocermaxBaseException("CategoryActivity","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error intop product");
            }
        }else if (bundle.getString("ACTION").equals(MyReceiverActions.OFFER_BY_DEALTYPE)) {       //responsible for getting data after click of offers [which is showing on bottom of Shop by categories]
            try {
                dismissDialog();
                Intent i=new Intent(CategoryActivity1.this,CategoryOffer.class);
                i.putExtra("json",bundle.getString("json"));
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();
        // Display display = getActivity().getWindowManager().getDefaultDisplay();
        //totalHeight = display.getWidth()/3;
        //width=width/3;

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    public class SubCategoryListAdapter extends BaseAdapter {


        public DisplayImageOptions baseImageoptions1;
        public SubCategoryListAdapter() {
            //initImageLoaderMCtegoryDeal();
            baseImageoptions1=UtilityMethods.initImageLoaderMCtegoryDeal(CategoryActivity1.this);
        }

        @Override
        public int getCount() {
            return alcatObjSend == null ? 0 : alcatObjSend.size();
        }

        @Override
        public Object getItem(int position) {
            return alcatObjSend.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View itemView, ViewGroup parent) {

            ViewHolder holder;

            if (itemView == null) {
                LayoutInflater mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = mInflater.inflate(R.layout.catg_list_home, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) itemView.findViewById(R.id.img);
                holder.footer = (TextView) itemView.findViewById(R.id.footer);
                holder.parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
                holder.imageView.setImageResource(R.drawable.cancel_icon);

                Typeface type = Typeface.createFromAsset(activity.getAssets(), "Gotham-Book.ttf");
                holder.footer.setTypeface(type);

                holder.parentLayout.setShadowPadding(0, 0, 0, 0);
                holder.parentLayout.setCardElevation(0);
                itemView.setTag(holder);
            }
            else {
                holder = (ViewHolder) itemView.getTag();
            }
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            width=width/4;
            //holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(width-3,width-40));

            String strurlImage = Constants.base_url_category_image + alcatObjSend.get(position).getCategoryId() + ".png";

            if (UtilityMethods.initImageLoaderMCtegoryDeal(CategoryActivity1.this)!=null) {
                ImageLoader.getInstance().displayImage(strurlImage,holder.imageView,baseImageoptions1);
            }

            holder.footer.setText(alcatObjSend.get(position).getCategory());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AppConstants.strTitleHotDeal = alcatObjSend.get(position).getCategory();
                        showDialog();
                        String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(mainCatPosition).getChildren().get(position).getCategoryId();
                        myApi.reqAllProductsCategory(url);
                    } catch (Exception e) {
                        new GrocermaxBaseException("CategoryActivity","onitemClick",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
                    }

                    /*GA event Tracking for this event*/
                    try{
                        UtilityMethods.clickCapture(mContext, "L2", "", alcatObjSend.get(position).getCategory(), "", MySharedPrefs.INSTANCE.getSelectedCity());
                        RocqAnalytics.trackEvent("L2", new ActionProperties("Category", "L2", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label",alcatObjSend.get(position).getCategory()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            return itemView;
        }
        class ViewHolder {

            ImageView imageView;
            TextView footer;
            CardView parentLayout;
        }

    }






}
