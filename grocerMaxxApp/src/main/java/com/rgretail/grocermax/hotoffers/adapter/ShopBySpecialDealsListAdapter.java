package com.rgretail.grocermax.hotoffers.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShopBySpecialDealsListAdapter extends RecyclerView.Adapter<ShopBySpecialDealsListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<ShopByDealModel> data;
    private String SCREENNAME = "ShopByDealsListAdapter-";
    Typeface typeface;

    public ShopBySpecialDealsListAdapter(Activity activity, Fragment fragment) {
        this.context = activity;
        this.fragment = fragment;
        typeface=Typeface.createFromAsset(activity.getAssets(),"Gotham-Book.ttf");
    }

    public void setListData(ArrayList<ShopByDealModel> data) {

        this.data = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView parentLayout;
        TextView tv_spe_deal_name;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            parentLayout  = (CardView) itemView.findViewById(R.id.layoutParent);
            tv_spe_deal_name = (TextView) itemView.findViewById(R.id.tv_spe_deal_name);
            view = (View) itemView.findViewById(R.id.view);

           //parentLayout.setShadowPadding(0,0,0,0);
            parentLayout.setCardElevation(0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_list_item, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(data.get(position).getImageurl(),holder.imageView, ((BaseActivity) context).baseImageoptions);

        holder.tv_spe_deal_name.setVisibility(View.VISIBLE);
        holder.view.setVisibility(View.VISIBLE);
        holder.tv_spe_deal_name.setText(data.get(position).getName());
        holder.tv_spe_deal_name.setTypeface(typeface);

     /*   try{
            UtilityMethods.clickCapture(context, "", "", data.get(position).getId(),data.get(position).getDealType(), SCREENNAME + AppConstants.SHOP_BY_DEAL_SCROLLING);
        }catch(Exception e){}*/

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
                    RocqAnalytics.trackEvent("Special Deal-"+data.get(position).getName(), new ActionProperties("Category", "Special Deal-"+data.get(position).getName(), "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label",data.get(position).getName()));
                }catch(Exception e){}
                /*-----------------------------------------------------*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
