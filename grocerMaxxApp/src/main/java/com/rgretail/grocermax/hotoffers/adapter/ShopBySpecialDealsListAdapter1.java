package com.rgretail.grocermax.hotoffers.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.ShopByDealModel;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

public class ShopBySpecialDealsListAdapter1 extends BaseAdapter{

    private Activity context;
    private Fragment fragment;
    private ArrayList<ShopByDealModel> data;
    private String SCREENNAME = "ShopByDealsListAdapter-";
    Typeface typeface;
    private LayoutInflater inflater = null;

    public ShopBySpecialDealsListAdapter1(Activity activity, Fragment fragment) {
        this.context = activity;
        this.fragment = fragment;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeface=Typeface.createFromAsset(activity.getAssets(),"Gotham-Book.ttf");
    }

    public void setListData(ArrayList<ShopByDealModel> data) {

        this.data = data;
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {

        ViewHolder holder = null;
        try {
            if (itemView == null) {
                itemView = inflater.inflate(R.layout.option_list_item, parent,false);
                holder = new ViewHolder();

                holder.imageView = (ImageView) itemView.findViewById(R.id.img);
                holder.parentLayout  = (CardView) itemView.findViewById(R.id.layoutParent);
                holder.tv_spe_deal_name = (TextView) itemView.findViewById(R.id.tv_spe_deal_name);
                holder.view = (View) itemView.findViewById(R.id.view);

                holder.parentLayout.setShadowPadding(0,0,0,0);
                holder.parentLayout.setCardElevation(0);
                itemView.setTag(holder);
            } else {
                holder = (ViewHolder) itemView.getTag();
            }

            ImageLoader.getInstance().displayImage(data.get(position).getImageurl(),holder.imageView, ((BaseActivity) context).baseImageoptions);

            holder.tv_spe_deal_name.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.tv_spe_deal_name.setText(data.get(position).getName());
            holder.tv_spe_deal_name.setTypeface(typeface);

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConstants.strTitleHotDeal = "";
                    AppConstants.strTitleHotDeal = data.get(position).getName();
                    ((HomeScreen) context).hitForSpecialDealsByDeals(data.get(position).getName(),data.get(position).getLinkurl());

                /*  tracking GA event for click on Shop By Deals from Home screen */
                    try{
                        MyApplication.isFromDrawer=false;
                        UtilityMethods.clickCapture(context, "Special Deal-"+data.get(position).getName(), "", data.get(position).getName(),"", MySharedPrefs.INSTANCE.getSelectedCity());
                        UtilityMethods.sendGTMEvent(context,"deal page",data.get(position).getName(),"Android Category Interaction");
                        RocqAnalytics.trackEvent("Special Deal-"+data.get(position).getName(), new ActionProperties("Category", "Special Deal-"+data.get(position).getName(), "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label",data.get(position).getName()));
                    }catch(Exception e){}
                /*-----------------------------------------------------*/
                }
            });


        }catch(Exception e){
        }



        return itemView;
    }

    private class ViewHolder {
        ImageView imageView;
        CardView parentLayout;
        TextView tv_spe_deal_name;
        View view;
    }





}
