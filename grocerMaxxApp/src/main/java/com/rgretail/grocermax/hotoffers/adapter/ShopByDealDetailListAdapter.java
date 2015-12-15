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
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

public class ShopByDealDetailListAdapter extends RecyclerView.Adapter<ShopByDealDetailListAdapter.ViewHolder> {

    private Activity context;
    private Fragment fragment;
    private ArrayList<OfferByDealTypeSubModel> data;
    private static  Activity activity;
    private String SCREENNAME = "ShopByDealDetailListAdapter-";
//    public static String strDealListDeatilHeading;
    public ShopByDealDetailListAdapter(Activity activity, Fragment fragment) {
//        this.context = context;
        this.context = activity;
        this.fragment = fragment;
        this.activity = activity;
        ((BaseActivity) activity).initImageLoaderMCtegoryDeal();

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

//        ((HomeScreen) context).isFromFragment = true;
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
                ((HomeScreen)context).hitForDealsByDeals(data.get(position).getPromo_id());
//                AppConstants.strTitleHotDeal = data.get(position).getTitle();

//                  AppConstants.strTitleHotDeal = "Offer Detail";
                DealListScreen.strDealHeading  = "Offer Detail";
                ((HomeScreen) context).isFromFragment = true;

                try{UtilityMethods.clickCapture(context,"","",data.get(position).getPromo_id(),"",SCREENNAME+data.get(position).getName()+"-"+AppConstants.GA_EVENT_DEAL_SELECTION);}catch(Exception e){}
//                String str1 = data.get(position).getDealName();
//                String str2 = data.get(position).getName();
//                String str3 = data.get(position).getTitle();
//                System.out.println(data.get(position).getDealName() + "==================" + data.get(position).getName());

//                ItemDetailFragment fragment = new ItemDetailFragment();
//                fragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.explode));
//                ((HomeScreen)context).hitForDealsByDeals(data.get(position).getId());
            }
        });


        try{UtilityMethods.clickCapture(context, "", "", data.get(position).getPromo_id(),"",SCREENNAME+data.get(position).getName()+"-"+ AppConstants.GA_EVENT_DEAL_SCROLLER);}catch(Exception e){}

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
