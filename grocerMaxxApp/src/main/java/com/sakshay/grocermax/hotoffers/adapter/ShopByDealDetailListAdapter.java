package com.sakshay.grocermax.hotoffers.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.OfferByDealTypeModel;
import com.sakshay.grocermax.bean.OfferByDealTypeSubModel;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;

import java.util.ArrayList;

public class ShopByDealDetailListAdapter extends RecyclerView.Adapter<ShopByDealDetailListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<OfferByDealTypeSubModel> data;
    private static  Activity activity;
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
        CardView parentLayout;
        TextView footer;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
            footer = (TextView) itemView.findViewById(R.id.footer);

            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            int width,height;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {      //13
                display.getSize(size);
                width = size.x;
                height = size.y;
            }else{
                width = display.getWidth();
                height = display.getHeight();
            }

            // SET THE IMAGEVIEW DIMENSIONS
            int dimens = (width/2)-20;
            float density = activity.getResources().getDisplayMetrics().density;
//            int finalDimens = (int)(dimens * density);
            int finalDimens = (int)(dimens);
            LinearLayout.LayoutParams imgvwDimens =
                    new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imageView.setLayoutParams(imgvwDimens);
// SET SCALETYPE
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_list_item, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(data.get(position).getDeal_image(),
                holder.imageView, ((BaseActivity) context).baseImageoptions);

        holder.footer.setText(data.get(position).getTitle() + "");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ItemDetailFragment fragment = new ItemDetailFragment();
//                fragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
                ((HotOffersActivity)context).hitForDealsByDeals(data.get(position).getPromo_id(), data.get(position).getDealName());
//                ((HotOffersActivity)context).hitForDealsByDeals(data.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
