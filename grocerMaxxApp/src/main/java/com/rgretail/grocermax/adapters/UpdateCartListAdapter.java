package com.rgretail.grocermax.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rgretail.grocermax.CartProductList;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.UpdateCartbg;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 22-Jun-16.
 */
public class UpdateCartListAdapter extends BaseAdapter{

    Context context;
    ListView listView;

    public UpdateCartListAdapter(Context context,ListView listView) {
        this.context = context;
        this.listView=listView;
    }

    @Override
    public int getCount() {
        return CartProductList.completeList.size();
    }

    @Override
    public Object getItem(int position) {
        return CartProductList.completeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.update_cart_popup_listitem,null);
            holder = new ViewHolder();

            holder.tv_p_name = (TextView) convertView.findViewById(R.id.tv_p_name);
            holder.tv_remove = (TextView) convertView.findViewById(R.id.tv_remove);
            holder.tv_avail_qty = (TextView) convertView.findViewById(R.id.tv_avail_qty);
            holder.tv_pick_qty = (TextView) convertView.findViewById(R.id.tv_pick_qty);

            holder.ll_remove=(LinearLayout)convertView.findViewById(R.id.ll_remove);
            holder.ll_change_qty=(LinearLayout)convertView.findViewById(R.id.ll_change_qty);

            holder.img_decrease_qty=(ImageView) convertView.findViewById(R.id.img_decrease_qty);
            holder.img_increase_qty=(ImageView) convertView.findViewById(R.id.img_increase_qty);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(CartProductList.completeList.get(position).getQty()==0){
            holder.tv_p_name.setText(CartProductList.completeList.get(position).getName());
            holder.tv_p_name.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.ll_remove.setVisibility(View.GONE);
            holder.ll_change_qty.setVisibility(View.GONE);
            holder.tv_p_name.setVisibility(View.VISIBLE);
            holder.tv_p_name.setTypeface(null, Typeface.BOLD);
        }else if(Integer.parseInt(CartProductList.completeList.get(position).getWebQty()) > 0){
            holder.tv_p_name.setText(CartProductList.completeList.get(position).getName());
            holder.tv_p_name.setBackgroundColor(Color.parseColor("#F4F4F4"));
            holder.tv_avail_qty.setText("AVAILABLE QTY. "+CartProductList.completeList.get(position).getWebQty());
            holder.tv_pick_qty.setText(""+CartProductList.completeList.get(position).getQty());
            holder.ll_remove.setVisibility(View.GONE);
            holder.ll_change_qty.setVisibility(View.VISIBLE);
            holder.tv_p_name.setVisibility(View.VISIBLE);

            holder.img_decrease_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(UtilityMethods.isInternetAvailable(context)){
                        int value1=Integer.parseInt(holder.tv_pick_qty.getText().toString());
                        value1=value1-1;
                        if(value1>0)
                        {
                            ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart((Activity) context, Constants.localCloneFile);
                            if(cart_products.size()>0) {
                               /* if (cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.completeList.get(position).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description
                                    cart_products.get(position).setQty(value1);
                                }*/
                                for(int i=0;i<cart_products.size();i++){
                                    if (cart_products.get(i).getItem_id().equalsIgnoreCase(CartProductList.completeList.get(position).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description
                                        cart_products.get(i).setQty(value1);
                                    }
                                }
                            }
                            CartProductList.completeList.get(position).setQty(value1);
                            //holder.tv_pick_qty.setText(String.valueOf(value1));
                            listView.setAdapter(new UpdateCartListAdapter(context,listView));
                            if(CartProductList.completeList.get(position).getQty()<=Integer.parseInt(CartProductList.completeList.get(position).getWebQty()))
                             CartProductList.completeList.get(position).setFlag("2");

                            ((CartProductList)context).changeUpdateButtonInPopup();
                        }
                    }else{
                        UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, context);
                    }
                }
            });

        }else{
            System.out.println("Name="+CartProductList.completeList.get(position).getName());
            holder.tv_p_name.setText(CartProductList.completeList.get(position).getName());
            holder.tv_p_name.setBackgroundColor(Color.parseColor("#F4F4F4"));
            holder.ll_remove.setVisibility(View.VISIBLE);
            holder.ll_change_qty.setVisibility(View.GONE);
            holder.tv_p_name.setVisibility(View.VISIBLE);
            if(CartProductList.completeList.get(position).getFlag()!=null && CartProductList.completeList.get(position).getFlag().equals("3"))
                holder.tv_remove.setText("Removed");
            else
               holder.tv_remove.setText("Remove");

            holder.tv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UpdateCartbg.getInstance().alDeleteId.add(CartProductList.completeList.get(position).getItem_id());
                    if(CartProductList.getInstance().sbDeleteProdId != null) {
                        if (CartProductList.getInstance().sbDeleteProdId.length() > 0) {
                            CartProductList.getInstance().sbDeleteProdId.append("," + CartProductList.completeList.get(position).getItem_id());
                        } else {
                            CartProductList.getInstance().sbDeleteProdId.append(CartProductList.completeList.get(position).getItem_id());
                        }
                    }

                    ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart((Activity)context, Constants.localCloneFile);

                    if(cart_products.size() > 0) {
                        /*if (cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.completeList.get(position).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description
                            UtilityMethods.deleteCloneCartItem((Activity)context, cart_products.get(position).getItem_id());  //delete particular item from clone cart locally to update quantity on product listing and description
                        }*/
                        for(int i=0;i<cart_products.size();i++){
                            if (cart_products.get(i).getItem_id().equalsIgnoreCase(CartProductList.completeList.get(position).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description
                                UtilityMethods.deleteCloneCartItem((Activity)context, cart_products.get(i).getItem_id());
                            }
                        }
                    }
                    CartProductList.completeList.get(position).setFlag("3");
                    listView.setAdapter(new UpdateCartListAdapter(context,listView));

                    ((CartProductList)context).changeUpdateButtonInPopup();

                }
            });
        }



        return convertView;
    }

    public class ViewHolder {
        TextView tv_p_name,tv_remove,tv_avail_qty,tv_pick_qty;
        ImageView img_decrease_qty,img_increase_qty;
        LinearLayout ll_remove,ll_change_qty;

    }



}
