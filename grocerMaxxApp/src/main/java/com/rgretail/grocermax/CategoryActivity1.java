package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.rgretail.grocermax.adapters.CategoryScreenAdapter;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.DealListBean;
import com.rgretail.grocermax.bean.ShopByCategoryModel;
import com.rgretail.grocermax.bean.ShopByDealModel;
import com.rgretail.grocermax.bean.ShopByDealsBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anchit-pc on 04-Mar-16.
 */
public class CategoryActivity1 extends BaseActivity {


    String strCatName;                          //shown on header of screen
    ArrayList<CategorySubcategoryBean> alcatObjSend;
    public ArrayList<CategorySubcategoryBean> catObj;
    public static ArrayList<ShopByCategoryModel> data;
    int mainCatPosition = 0;
    int mainCatLength = 9;
    private List<ShopByDealModel> product_list;

    ShopByDealsBean listBean;
    String strCatIdByCat = "";
    ListView lv_catg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_category1);

           /* addActionsInFilter(MyReceiverActions.TOP_PRODUCTS_LIST);*/
            addActionsInFilter(MyReceiverActions.CATEGORY_BANNER);
            addActionsInFilter(MyReceiverActions.ALL_PRODUCTS_CATEGORY);
            addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);
            addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);



            lv_catg=(ListView)findViewById(R.id.cat_list);



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

            initHeader(findViewById(R.id.app_bar_header), true, strCatName);

            lv_catg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                    if (listBean != null) {
                        if (listBean.getSubcategorybanner().size()>0) {
                           /* MySharedPrefs.INSTANCE.putItemQuantity(listBean.getProduct().get(position-2).getQuantity());
                            showDialog();
                            String url = UrlsConstants.PRODUCT_DETAIL_URL + listBean.getProduct().get(position-2).getProductid();
                            myApi.reqProductDetailFromNotification(url);*/
                        }
                    }
                }
            });

            try {
                showDialog();
               //myApi.reqTopProduct(UrlsConstants.TOP_PRODUCT_URL + strCatIdByCat);
                myApi.reqCategoryBanner(UrlsConstants.CATG_BANNER_URL + strCatIdByCat);
                System.out.println("cat id="+strCatIdByCat);
            } catch (Exception e) {
                new GrocermaxBaseException("CategoryActivity","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting top product");
            }
        }catch (Exception e){
        }
    }

    public void getTopOffers(){
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

    public void hitForSpecialDealsByDeals(String sku) {            //responsible for clicking of [shop by deals -> ShopByDealItemDetailFragment -> DealListScreen]

        String url = UrlsConstants.PRODUCTLISTING_BY_SPECIAL_DEAL_TYPE;
        showDialog();
        System.out.println("Url??--"+url + sku);
        myApi.reqProductListingByDealType(url + sku);
    }




    @Override
    public void onStart() {
        super.onStart();

       /*------------------------------*/
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            initHeader(findViewById(R.id.app_bar_header), true, strCatName);
            initBottom(findViewById(R.id.footer));
            showSubscriptionPopup();
        }catch(Exception e){}

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void OnResponse(final Bundle bundle) {
        if (bundle.getString("ACTION").equals(MyReceiverActions.CATEGORY_BANNER)) {
            try
            {
                dismissDialog();
                listBean = (ShopByDealsBean) bundle.getSerializable(ConnectionService.RESPONSE);
                if (listBean != null)
                {
                    if (listBean.getSubcategorybanner().size() > 0)
                    {
                        product_list = listBean.getSubcategorybanner();
                    }
                    else {
                        product_list = new ArrayList<ShopByDealModel>();
                    }
                    if (Integer.parseInt(data.get(mainCatPosition).getOffercount()) > 0)
                    {
                        CategorySubcategoryBean categorySubcategoryBean=new CategorySubcategoryBean();
                        categorySubcategoryBean.setCategoryId("");
                        categorySubcategoryBean.setCategory("TOP OFFERS");
                        alcatObjSend.add(categorySubcategoryBean);
                    }
                    lv_catg.setAdapter(new CategoryScreenAdapter(CategoryActivity1.this, product_list,alcatObjSend,mainCatPosition,catObj));
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
        } else if (bundle.getString("ACTION").equals(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE)) {         //responsible for product listing through deals [ShopByDealItemDetailFragment -> DealListScreen]

            try {
                dismissDialog();
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
                if(!AppConstants.strTitleHotDeal.equals(""))
                    call_bundle.putSerializable("Header", AppConstants.strTitleHotDeal);
                else
                    call_bundle.putSerializable("Header", DealListScreen.strDealHeading);
                call.putExtras(call_bundle);
                startActivity(call);
            } catch (JSONException e) {
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
            int y = items%columns;
            if(y>0)
                x=x+1;
            System.out.println("x=" + x+" item="+items+" col="+columns);
            rows = (int) (x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

   /* public class SubCategoryListAdapter extends BaseAdapter {


        public DisplayImageOptions baseImageoptions1;
        public SubCategoryListAdapter() {
            //initImageLoaderMCtegoryDeal();
            baseImageoptions1=UtilityMethods.initImageLoaderMCtegoryDeal(CategoryActivity1.this,R.drawable.placeholder_level2);
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
                holder.imageView_l2 = (ImageView) itemView.findViewById(R.id.img_l2);
                holder.footer = (TextView) itemView.findViewById(R.id.footer);
                holder.parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
                holder.imageView_l2.setImageResource(R.drawable.cancel_icon);

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
            holder.imageView.setVisibility(View.GONE);
            holder.imageView_l2.setVisibility(View.VISIBLE);
            //holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(108,80));

            String strurlImage = Constants.base_url_category_image + alcatObjSend.get(position).getCategoryId() + ".png";

            if (baseImageoptions1!=null) {
                ImageLoader.getInstance().displayImage(strurlImage,holder.imageView_l2,baseImageoptions1);
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

                    *//*GA event Tracking for this event*//*
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

            ImageView imageView,imageView_l2;
            TextView footer;
            CardView parentLayout;
        }

    }*/
}
