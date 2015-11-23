package com.rgretail.grocermax.hotoffers.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.DealListScreen;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.OfferByDealTypeSubModel;
import com.rgretail.grocermax.hotoffers.HotOffersActivity;
import com.rgretail.grocermax.utils.AppConstants;

import java.util.ArrayList;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<OfferByDealTypeSubModel> data;
    private  static Activity activity;
    public DetailListAdapter(Activity activity, Fragment fragment) {
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
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
//            parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
//            footer = (TextView) itemView.findViewById(R.id.footer);
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
//        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_list_item, parent, false);
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_by_item_detail, parent, false);
//        LinearLayout ll = (LinearLayout)root.findViewById(R.id.ll_);
//        ll.setVisibility(View.GONE);

//        ((BaseActivity) context).initHeader(context.findViewById(R.id.header_left), true, ShopByCategoryListAdapter.strDealListCategoryHeading);
        ((BaseActivity) context).initHeader(context.findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
        ((BaseActivity) context).findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        ((BaseActivity) context).findViewById(R.id.header).setVisibility(View.GONE);

        return new ViewHolder(root);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ImageLoader.getInstance().displayImage(data.get(position).getImage(),
                holder.imageView, ((BaseActivity) context).baseImageoptions);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HotOffersActivity) context).hitForDealsByDeals(data.get(position).getId());
                ((HotOffersActivity) context).isFromFragment = true;
//                    AppConstants.strTitleHotDeal = data.get(position).getName();
//                AppConstants.strTitleHotDeal = "Offer Detail";
                DealListScreen.strDealHeading  = "Offer Detail";
            }
        });


//        holder.footer.setText(data.get(position).getName());
//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((HotOffersActivity) context).hitForDealsByDeals(data.get(position).getId());
//
//                AppConstants.strTitleHotDeal = data.get(position).getName();
//
/////                ItemDetailFragment fragment = new ItemDetailFragment();
/////                fragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
/////                ((MainActivity)context).changeFragment(fragment,holder.parentLayout);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
