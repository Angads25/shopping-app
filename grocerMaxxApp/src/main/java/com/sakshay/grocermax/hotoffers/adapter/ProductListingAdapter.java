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

import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.DealProductListingModel;

import java.util.ArrayList;

public class ProductListingAdapter extends RecyclerView.Adapter<ProductListingAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<DealProductListingModel> data;
    public ProductListingAdapter(Activity activity, Fragment fragment) {
//        this.context = context;
        this.context = activity;
        this.fragment = fragment;
    }

    public void setListData(ArrayList<DealProductListingModel> data) {

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
            imageView = (ImageView) itemView.findViewById(R.id.title);
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

        holder.footer.setText(data.get(position).getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
