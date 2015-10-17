package com.sakshay.grocermax.hotoffers.adapter;

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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.ShopByCategoryModel;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.hotoffers.fragment.ItemDetailFragment;

import java.util.ArrayList;

public class ShopByCategoryListAdapter extends RecyclerView.Adapter<ShopByCategoryListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<ShopByCategoryModel> data;
    private static Activity activity;
    public static String strDealListCategoryHeading;
    public ShopByCategoryListAdapter(Activity activity, Fragment fragment) {
//        this.context = context;
        this.context = activity;
        this.fragment = fragment;
        this.activity = activity;
        ((BaseActivity) activity).initImageLoaderM();
    }

    public void setListData(ArrayList<ShopByCategoryModel> data) {

        this.data = data;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView footer;
        CardView parentLayout;
        ImageView ivRightCarrot;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            footer = (TextView) itemView.findViewById(R.id.footer);
            parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
            ivRightCarrot = (ImageView) itemView.findViewById(R.id.iv_right_carrot);

            Typeface type = Typeface.createFromAsset(activity.getAssets(), "Lato-Lig.ttf");
		    footer.setTypeface(type);

            parentLayout.setShadowPadding(0,0,0,0);
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
        ImageLoader.getInstance().displayImage(data.get(position).getImages(),
                holder.imageView, ((BaseActivity) context).baseImageoptions);

        holder.footer.setText(data.get(position).getOffercount()+" Offer");
        holder.ivRightCarrot.setVisibility(View.VISIBLE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HotOffersActivity) context).hitForShopByCategory(data.get(position).getCategory_id());
                ShopByCategoryListAdapter.strDealListCategoryHeading = data.get(position).getName();
                //fragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
//                ((HotOffersActivity)context).hitForShopByCategory(data.get(position).getCategory_id());
            }
        });

        holder.footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HotOffersActivity)context).hitForShopByCategory(data.get(position).getCategory_id());
                ShopByCategoryListAdapter.strDealListCategoryHeading = data.get(position).getName();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
