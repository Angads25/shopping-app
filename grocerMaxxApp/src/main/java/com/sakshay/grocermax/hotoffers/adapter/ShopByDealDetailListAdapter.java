package com.sakshay.grocermax.hotoffers.adapter;

import android.app.Activity;
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
import com.sakshay.grocermax.bean.OfferByDealTypeModel;
import com.sakshay.grocermax.bean.OfferByDealTypeSubModel;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;

import java.util.ArrayList;

public class ShopByDealDetailListAdapter extends RecyclerView.Adapter<ShopByDealDetailListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<OfferByDealTypeSubModel> data;
    public ShopByDealDetailListAdapter(Activity activity, Fragment fragment) {
//        this.context = context;
        this.context = activity;
        this.fragment = fragment;
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
                ((HotOffersActivity)context).hitForDealsByDeals(data.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
