package com.rgretail.grocermax.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.CategoryActivity1;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.ShopByDealModel;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;
import java.util.List;

public class CategoryScreenAdapter extends BaseAdapter {

    List<ShopByDealModel> banner = null;
    Activity activity;
    private LayoutInflater inflater = null;
    ProgressDialog progressDialog;
    Typeface face;
    Typeface font1,font2,font3,font4;
    ArrayList<CategorySubcategoryBean> alcatObjSend;
    int mainCatPosition;
    ArrayList<CategorySubcategoryBean> catObj;


    public CategoryScreenAdapter() {

    }

    public CategoryScreenAdapter(Activity activity, List<ShopByDealModel> list,ArrayList<CategorySubcategoryBean> alcatObjSend,int mainCatPosition,ArrayList<CategorySubcategoryBean> catObj) {
        ((BaseActivity) activity).initImageLoaderM();
        this.activity = activity;
        this.banner = list;
        this.alcatObjSend=alcatObjSend;
        this.mainCatPosition=mainCatPosition;
        this.catObj=catObj;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ((BaseActivity) activity).initImageLoaderMCtegoryDeal();
    }

    @Override
    public int getCount() {
        if (banner != null) {
            return banner.size()+1;
        } else {
            return 1;
        }
    }

    @Override
    public ShopByDealModel getItem(int position) {
        return banner.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        try{
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.shop_by_item_detail, parent,false);
                holder = new ViewHolder();

                font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
                font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
                font3 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
                font4 =   Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
                holder.card_view=(CardView)convertView.findViewById(R.id.card_view);
                holder.sub_catg_grid=(GridView)convertView.findViewById(R.id.sub_catg_grid);
                holder.img=(ImageView)convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(position==0){
             holder.card_view.setVisibility(View.GONE);
             holder.sub_catg_grid.setVisibility(View.VISIBLE);
             holder.sub_catg_grid.setAdapter(new SubCategoryListAdapter());
             ((CategoryActivity1)activity).setGridViewHeightBasedOnChildren(holder.sub_catg_grid, 3);
            }
            /*if (position==1){
                holder.card_view.setVisibility(View.GONE);
                if (Integer.parseInt(CategoryActivity1.data.get(mainCatPosition).getOffercount()) > 0)
                holder.img_top_offer.setVisibility(View.VISIBLE);
                else
                holder.img_top_offer.setVisibility(View.GONE);
                holder.sub_catg_grid.setVisibility(View.GONE);
            }*/
            else if (position>0) {
                //position=position-2;
                holder.card_view.setVisibility(View.VISIBLE);
                holder.sub_catg_grid.setVisibility(View.GONE);
                holder.card_view.setShadowPadding(0, 0, 0, 0);

                holder.card_view.setCardElevation(0);

                final ShopByDealModel obj = getItem(position-1);
                System.out.println("img url = " + obj.getImageurl());
                ImageLoader.getInstance().displayImage(obj.getImageurl(), holder.img, ((BaseActivity) activity).baseImageoptions);

                holder.card_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            AppConstants.strTitleHotDeal = "";
                            AppConstants.strTitleHotDeal = obj.getName();
                            if(!obj.getLinkurl().equals(""))
                            ((CategoryActivity1) activity).hitForSpecialDealsByDeals(obj.getLinkurl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        }catch(Exception e){
            new GrocermaxBaseException("ProductListAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
        return convertView;
    }

    private class ViewHolder {
        GridView sub_catg_grid;
        ImageView img;
        CardView card_view;
    }

    public void updateList(List<ShopByDealModel> list) {
        try{
            this.banner = list;
            notifyDataSetChanged();
        }catch(Exception e){
            new GrocermaxBaseException("ProductListAdapter","updateList",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    public class SubCategoryListAdapter extends BaseAdapter {


        public DisplayImageOptions baseImageoptions1;
        public SubCategoryListAdapter() {
            //initImageLoaderMCtegoryDeal();
            baseImageoptions1=UtilityMethods.initImageLoaderMCtegoryDeal(activity,R.drawable.placeholder_level2);
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
                LayoutInflater mInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = mInflater.inflate(R.layout.catg_list_home, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) itemView.findViewById(R.id.img);
                holder.imageView_l2 = (ImageView) itemView.findViewById(R.id.img_l2);
                holder.footer = (TextView) itemView.findViewById(R.id.footer);
                holder.parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
                holder.imageView_l2.setImageResource(R.drawable.cancel_icon);

                Typeface type = Typeface.createFromAsset(activity.getAssets(), "Gotham-Medium.ttf");
                holder.footer.setTypeface(type);

                holder.parentLayout.setShadowPadding(0, 0, 0, 0);
                holder.parentLayout.setCardElevation(0);
                itemView.setTag(holder);
            }
            else {
                holder = (ViewHolder) itemView.getTag();
            }
           /* Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            width=width/4;*/
            holder.imageView.setVisibility(View.GONE);
            holder.imageView_l2.setVisibility(View.VISIBLE);
            //holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(108,80));


            if(alcatObjSend.get(position).getCategoryId().equals("")){
                holder.imageView_l2.setImageResource(R.drawable.top_offers);
                holder.imageView_l2.setPadding(6,6,6,6);
                holder.footer.setTextColor(Color.RED);
                holder.footer.setTextSize(13);

            }else{
            String strurlImage = Constants.base_url_category_image + alcatObjSend.get(position).getCategoryId() + ".png";
            if (baseImageoptions1!=null) {
                ImageLoader.getInstance().displayImage(strurlImage,holder.imageView_l2,baseImageoptions1);
            }
            }

            holder.footer.setText(alcatObjSend.get(position).getCategory());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!alcatObjSend.get(position).getCategoryId().equals("")) {
                        try {
                            AppConstants.strTitleHotDeal = alcatObjSend.get(position).getCategory();
                            ((BaseActivity)activity).showDialog();
                            String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(mainCatPosition).getChildren().get(position).getCategoryId();
                            ((BaseActivity)activity).myApi.reqAllProductsCategory(url);
                        } catch (Exception e) {
                            new GrocermaxBaseException("CategoryActivity","onitemClick",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
                        }
                    /*GA event Tracking for this event*/
                        try{
                            UtilityMethods.clickCapture(activity, "L2", "", alcatObjSend.get(position).getCategory(), "", MySharedPrefs.INSTANCE.getSelectedCity());
                            UtilityMethods.sendGTMEvent(activity,"category page",alcatObjSend.get(position).getCategory(),"Android Category Interaction");
                            RocqAnalytics.trackEvent("L2", new ActionProperties("Category", "L2", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label",alcatObjSend.get(position).getCategory()));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            ((CategoryActivity1)activity).getTopOffers();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    }





}

