package com.rgretail.grocermax.hotoffers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.CategoryActivity1;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.ShopByCategoryModel;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

public class ShopByCategoryListAdapter extends BaseAdapter {

    private Activity context;
    private Fragment fragment;
    private ArrayList<ShopByCategoryModel> data;
    private static Activity activity;
    private String SCREENNAME = "ShopByCategoryListAdapter-";
    public DisplayImageOptions baseImageoptions;
//    public static String strDealListCategoryHeading;
    public ShopByCategoryListAdapter(Activity activity, Fragment fragment) {
//        this.context = context;
        this.context = activity;
        this.fragment = fragment;
        this.activity = activity;
//        ((BaseActivity) activity).initImageLoaderM();
        baseImageoptions=UtilityMethods.initImageLoaderMCtegoryDeal(context);
    }





    public void setListData(ArrayList<ShopByCategoryModel> data) {

        this.data = data;

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {

        ViewHolder holder;

        if (itemView == null) {
            LayoutInflater mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = mInflater.inflate(R.layout.catg_list_home, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) itemView.findViewById(R.id.img);
            holder.footer = (TextView) itemView.findViewById(R.id.footer);
            holder.parentLayout = (CardView) itemView.findViewById(R.id.layoutParent);
            holder.imageView.setImageResource(R.drawable.cancel_icon);

            Typeface type = Typeface.createFromAsset(activity.getAssets(), "Gotham-Book.ttf");
            holder.footer.setTypeface(type);

            holder.parentLayout.setShadowPadding(0, 0, 0, 0);
            holder.parentLayout.setCardElevation(0);
            itemView.setTag(holder);
        }
        else {
            holder = (ViewHolder) itemView.getTag();
        }
        Display display = context.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
         width=width/3;
        //itemView.setLayoutParams(new LinearLayout.LayoutParams(width-2,width-40));
        //holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,width-40));

        if (baseImageoptions!=null) {
            ImageLoader.getInstance().displayImage(data.get(position).getImages(),
                    holder.imageView, baseImageoptions);
        }

        holder.footer.setText(data.get(position).getName());



        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((HomeScreen) context), CategoryActivity1.class);
                Bundle call_bundle = new Bundle();
                call_bundle.putSerializable("Categories", (((HomeScreen) context).catObj));
                call_bundle.putSerializable("data", data);
                call_bundle.putSerializable("maincategoryposition", String.valueOf(position));
                call_bundle.putSerializable("CategoryName", data.get(position).getName());
                call_bundle.putSerializable("CategoryId", data.get(position).getCategory_id());           //catid of shop deals
                intent.putExtras(call_bundle);
                ((HomeScreen) context).startActivity(intent);
                ((HomeScreen)context).isFromFragment=false;

                try{
                    UtilityMethods.clickCapture(context, "L1", "",data.get(position).getName(), "", MySharedPrefs.INSTANCE.getSelectedCity());
                    RocqAnalytics.trackEvent("L1", new ActionProperties("Category", "L1", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", data.get(position).getName()));

                }catch(Exception e){}

            }
        });

        return itemView;
    }

     static class ViewHolder {

        ImageView imageView;
        TextView footer;
        CardView parentLayout;
    }

}
