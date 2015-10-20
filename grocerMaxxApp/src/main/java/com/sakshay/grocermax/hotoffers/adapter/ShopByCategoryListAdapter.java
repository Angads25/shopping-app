package com.sakshay.grocermax.hotoffers.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.sakshay.grocermax.CategoryActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.bean.ShopByCategoryModel;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;

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

        holder.footer.setText(data.get(position).getOffercount() + " Offer");
        holder.ivRightCarrot.setVisibility(View.VISIBLE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((HotOffersActivity) context).hitForShopByCategory(data.get(position).getCategory_id());
//                ShopByCategoryListAdapter.strDealListCategoryHeading = data.get(position).getName();


//                ArrayList<CategorySubcategoryBean> catObjSend = new ArrayList<CategorySubcategoryBean>();
//                int size = ((HotOffersActivity) context).catObj.get(position).getChildren().size();
//
//                for (int i = 0; i < ((HotOffersActivity) context).catObj.get(position).getChildren().size(); i++) {
//                    if (((HotOffersActivity) context).catObj.get(position).getChildren().get(i).getIsActive().equals("1")) {
//                           catObjSend.add((((HotOffersActivity) context).catObj.get(i)));
//                    }
//                    .get(0).getcategory
//                    if (((HotOffersActivity) context).catObj.get(position).getChildren().get(i).getIsActive().equals("1")){
//                        catObjSend.add((((HotOffersActivity) context).catObj.get(i)));
//                    }
//                }


                Intent intent = new Intent(((HotOffersActivity) context), CategoryActivity.class);
                Bundle call_bundle = new Bundle();
                call_bundle.putSerializable("Categories", (((HotOffersActivity) context).catObj));
//                call_bundle.putSerializable("Categories", catObjSend);
                call_bundle.putSerializable("maincategoryposition", String.valueOf(position));
                call_bundle.putSerializable("CategoryName", data.get(position).getName());
                call_bundle.putSerializable("CategoryId", data.get(position).getCategory_id());           //catid of shop deals
                intent.putExtras(call_bundle);
                ((HotOffersActivity) context).startActivity(intent);

                System.out.println("=====idss1111111===" + data.get(position).getCategory_id());

                //fragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
//                ((HotOffersActivity)context).hitForShopByCategory(data.get(position).getCategory_id());
            }
        });

        holder.footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HotOffersActivity)context).hitForShopByCategory(data.get(position).getCategory_id());

                System.out.println("=====idss==="+data.get(position).getCategory_id());

                ShopByCategoryListAdapter.strDealListCategoryHeading = data.get(position).getName();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
