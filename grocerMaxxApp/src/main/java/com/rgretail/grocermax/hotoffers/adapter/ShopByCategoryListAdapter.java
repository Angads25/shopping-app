package com.rgretail.grocermax.hotoffers.adapter;

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
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.CategoryActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.ShopByCategoryModel;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

public class ShopByCategoryListAdapter extends RecyclerView.Adapter<ShopByCategoryListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<ShopByCategoryModel> data;
    private static Activity activity;
    private String SCREENNAME = "ShopByCategoryListAdapter-";
//    public static String strDealListCategoryHeading;
    public ShopByCategoryListAdapter(Activity activity, Fragment fragment) {
//        this.context = context;
        this.context = activity;
        this.fragment = fragment;
        this.activity = activity;
//        ((BaseActivity) activity).initImageLoaderM();
        ((BaseActivity) activity).initImageLoaderMCtegoryDeal();
    }

    public void setListData(ArrayList<ShopByCategoryModel> data) {

        this.data = data;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView footer;
        CardView parentLayout;
        ImageView ivRightCarrot;
        View view1Space,viewVerticalLine,view2Space;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            footer = (TextView) itemView.findViewById(R.id.footer);
            parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
            ivRightCarrot = (ImageView) itemView.findViewById(R.id.iv_right_carrot);

            view1Space = (View) itemView.findViewById(R.id.view_1_space);
            viewVerticalLine = (View) itemView.findViewById(R.id.view_line);
            view2Space = (View) itemView.findViewById(R.id.view_2_space);

            imageView.setImageResource(R.drawable.cancel_icon);

            Typeface type = Typeface.createFromAsset(activity.getAssets(), "Lato-Bol.ttf");
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
                holder.imageView, ((BaseActivity) activity).baseImageoptions);

//        position
//        data.size()

        try{UtilityMethods.clickCapture(context, "", "", data.get(position).getCategory_id(),"", SCREENNAME+data.get(position).getName()+"-"+AppConstants.SHOP_BY_CATEGORY_SCROLLING);}catch(Exception e){}

        if(position == data.size()-1){
            holder.view1Space.setVisibility(View.GONE);
            holder.viewVerticalLine.setVisibility(View.GONE);
            holder.view2Space.setVisibility(View.GONE);
        }else{
            holder.view1Space.setVisibility(View.VISIBLE);
            holder.viewVerticalLine.setVisibility(View.VISIBLE);
            holder.view2Space.setVisibility(View.VISIBLE);
        }

        holder.footer.setText(data.get(position).getOffercount() + " Offers");
        holder.ivRightCarrot.setVisibility(View.VISIBLE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((HomeScreen) context).hitForShopByCategory(data.get(position).getCategory_id());
//                ShopByCategoryListAdapter.strDealListCategoryHeading = data.get(position).getName();


//                ArrayList<CategorySubcategoryBean> catObjSend = new ArrayList<CategorySubcategoryBean>();
//                int size = ((HomeScreen) context).catObj.get(position).getChildren().size();
//
//                for (int i = 0; i < ((HomeScreen) context).catObj.get(position).getChildren().size(); i++) {
//                    if (((HomeScreen) context).catObj.get(position).getChildren().get(i).getIsActive().equals("1")) {
//                           catObjSend.add((((HomeScreen) context).catObj.get(i)));
//                    }
//                    .get(0).getcategory
//                    if (((HomeScreen) context).catObj.get(position).getChildren().get(i).getIsActive().equals("1")){
//                        catObjSend.add((((HomeScreen) context).catObj.get(i)));
//                    }
//                }


                Intent intent = new Intent(((HomeScreen) context), CategoryActivity.class);
                Bundle call_bundle = new Bundle();
                call_bundle.putSerializable("Categories", (((HomeScreen) context).catObj));
//                call_bundle.putSerializable("Categories", catObjSend);
                call_bundle.putSerializable("maincategoryposition", String.valueOf(position));
                call_bundle.putSerializable("CategoryName", data.get(position).getName());
                call_bundle.putSerializable("CategoryId", data.get(position).getCategory_id());           //catid of shop deals
                intent.putExtras(call_bundle);
                ((HomeScreen) context).startActivity(intent);
                ((HomeScreen)context).isFromFragment=false;


                try{UtilityMethods.clickCapture(context, "", "", data.get(position).getCategory_id(),"", SCREENNAME+data.get(position).getName()+"-"+AppConstants.SHOP_BY_CATEGORY_IMAGE_CLICK);}catch(Exception e){}

//                System.out.println("=====idss1111111===" + data.get(position).getCategory_id());

                //fragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
//                ((HomeScreen)context).hitForShopByCategory(data.get(position).getCategory_id());
            }
        });

        holder.footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            try {
                int clickedFooter = 0;
                if (Integer.parseInt(data.get(position).getOffercount()) > 0) {

                    ((HomeScreen) context).hitForShopByCategory(data.get(position).getCategory_id());
                    for (int i = 0; i < ((HomeScreen) context).catObj.size(); i++) {
                        if (((HomeScreen) context).catObj.get(i).getCategoryId().equals(data.get(position).getCategory_id())) {
//                        strCatName = catObj.get(i).getCategory();
//                        ShopByCategoryListAdapter.strDealListCategoryHeading = ((HomeScreen) context).catObj.get(i).getCategory();

//                        AppConstants.strTitleHotDeal = ((HomeScreen) context).catObj.get(i).getCategory();
                            AppConstants.strTitleHotDeal = ((HomeScreen) context).catObj.get(i).getCategory();
                        }
                    }
                }
                try{UtilityMethods.clickCapture(context, "", "", ((HomeScreen) context).catObj.get(clickedFooter).getCategoryId(),"", SCREENNAME+((HomeScreen) context).catObj.get(clickedFooter).getCategory()+"-"+AppConstants.SHOP_BY_CATEGORY_FOOTER_CLICK);}catch(Exception e){}
            }catch(Exception e){}
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
