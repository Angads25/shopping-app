package com.sakshay.grocermax.hotoffers.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.SignalStrength;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.DealListScreen;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.OfferByDealTypeSubModel;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;

import java.util.ArrayList;

public class ShopByDealDetailListAdapter extends RecyclerView.Adapter<ShopByDealDetailListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<OfferByDealTypeSubModel> data;
    private static  Activity activity;
//    public static String strDealListDeatilHeading;
    public ShopByDealDetailListAdapter(Activity activity, Fragment fragment) {
//        this.context = context;
        this.context = activity;
        this.fragment = fragment;
        this.activity = activity;

    }

    public void setListData(ArrayList<OfferByDealTypeSubModel> data) {

        this.data = data;

//        if(data!=null)
//        adminReservationList.clear();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
//        CardView parentLayout;
//        TextView footer;
//        LinearLayout ll;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);


//            parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
//            footer = (TextView) itemView.findViewById(R.id.footer);
//            ll = (LinearLayout) itemView.findViewById(R.id.ll_);

//            Display display = activity.getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            int width,height;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {      //13
//                display.getSize(size);
//                width = size.x;
//                height = size.y;
//            }else{
//                width = display.getWidth();
//                height = display.getHeight();
//            }

            // SET THE IMAGEVIEW DIMENSIONS
//            int dimens = (width/2)-20;
//            float density = activity.getResources().getDisplayMetrics().density;
//            int finalDimens = (int)(dimens);
//            LinearLayout.LayoutParams imgvwDimens =
//                    new LinearLayout.LayoutParams(finalDimens, finalDimens);
//            imageView.setLayoutParams(imgvwDimens);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_by_item_detail, parent, false);

//        ((BaseActivity) context).initHeader(context.findViewById(R.id.header_left), true, ShopByDealDetailListAdapter.strDealListDeatilHeading);

//        ((HotOffersActivity) context).isFromFragment = true;
//        ((BaseActivity) context).showSearchView(false);

        ((BaseActivity) context).initHeader(context.findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
        ((BaseActivity) context).findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        ((BaseActivity) context).findViewById(R.id.header).setVisibility(View.GONE);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(data.get(position).getDeal_image(),
                holder.imageView, ((BaseActivity) context).baseImageoptions);



//        holder.ll.setVisibility(View.GONE);
//        holder.footer.setText(data.get(position).getTitle() + "");

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HotOffersActivity)context).hitForDealsByDeals(data.get(position).getPromo_id());
//                AppConstants.strTitleHotDeal = data.get(position).getTitle();

//                  AppConstants.strTitleHotDeal = "Offer Detail";
                DealListScreen.strDealHeading  = "Offer Detail";

                    ((HotOffersActivity) context).isFromFragment = true;
//                String str1 = data.get(position).getDealName();
//                String str2 = data.get(position).getName();
//                String str3 = data.get(position).getTitle();
//                System.out.println(data.get(position).getDealName() + "==================" + data.get(position).getName());

//                ItemDetailFragment fragment = new ItemDetailFragment();
//                fragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
//                ((HotOffersActivity)context).hitForDealsByDeals(data.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
