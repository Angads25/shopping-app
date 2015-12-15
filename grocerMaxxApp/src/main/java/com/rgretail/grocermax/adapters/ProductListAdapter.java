package com.rgretail.grocermax.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.CategoryTabs;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UtilityMethods;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.utils.CustomTypefaceSpan;

public class ProductListAdapter extends BaseAdapter {

    List<Product> products = null;
    Activity activity;
    private LayoutInflater inflater = null;
    ProgressDialog progressDialog;
    Typeface face;
    Typeface font1,font2,font3,font4;

    public ProductListAdapter() {

    }

    public ProductListAdapter(Activity activity, List<Product> list) {
        ((BaseActivity) activity).initImageLoaderM();
        this.activity = activity;
        this.products = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < products.size(); i++)
            products.get(i).setQuantity("1");
    }

    @Override
    public int getCount() {
        if (products != null) {
            return products.size();
        } else {
            return 0;
        }
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        try{
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_list_row, parent,
                    false);
            holder = new ViewHolder();
            holder.sale_price = (TextView) convertView
                    .findViewById(R.id.sale_price);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            holder.amount.setPaintFlags(holder.amount.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.quantity = (TextView) convertView
                    .findViewById(R.id.quantity);
            holder.add_to_cart = (TextView) convertView
                    .findViewById(R.id.add_cart_prod_list);
            holder.prod_image = (ImageView) convertView
                    .findViewById(R.id.product_image);
            holder.increase_quantity = (ImageView) convertView
                    .findViewById(R.id.increase_quantity);
            holder.decrease_quantity = (ImageView) convertView
                    .findViewById(R.id.decrease_quantity);
            holder.tvOffers = (TextView) convertView
                    .findViewById(R.id.tv_offers);


            holder.tvVerticalBar = (TextView) convertView
                    .findViewById(R.id.tv_vertical_bar);


            holder.prod_brand = (TextView) convertView
                    .findViewById(R.id.product_brand);
            holder.prod_name = (TextView) convertView
                    .findViewById(R.id.product_name);
            holder.prod_gram_or_ml = (TextView) convertView
                    .findViewById(R.id.product_gram_or_ml);
            holder.rlOutofStock = (RelativeLayout) convertView
                    .findViewById(R.id.rl_out_of_stock);


            holder.added_product_count = (TextView) convertView
                    .findViewById(R.id.added_product_count);
            holder.img_added_product_count = (ImageView) convertView
                    .findViewById(R.id.img_added_product_count);
            holder.iv_offer_image = (ImageView) convertView
                    .findViewById(R.id.offer_image);
            holder.tvNoProducts = (TextView) convertView
                    .findViewById(R.id.tv_no_products);


            holder.prod_brand.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
            holder.prod_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
            holder.prod_gram_or_ml.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));

            font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
            font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
            font3 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
            font4 =   Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product obj = getItem(position);

        if (!obj.getName().equals("No product found for this category")) {
            holder.tvNoProducts.setVisibility(View.GONE);
            holder.prod_name.setText(obj.getName());
            if (AppConstants.DEBUG) {
                Log.d("", "PRICE WITHOUT DISSCOUNT::" + obj.getSalePrice());
            }

            holder.prod_brand.setText(obj.getBrand());
            holder.prod_gram_or_ml.setText(obj.getGramsORml());
            holder.prod_name.setText(obj.getProductName());

            if (obj.getPromotionLevel() != null) {
                holder.tvOffers.setText(obj.getPromotionLevel());
                holder.tvOffers.setVisibility(View.VISIBLE);
                holder.iv_offer_image.setVisibility(View.VISIBLE);
                try{UtilityMethods.clickCapture(activity,obj.getPrice(),"",obj.getProductid(),"",CategoryTabs.SCREENNAME+"-"+obj.getProductName()+"-"+AppConstants.GA_EVENT_PRODUCT_HAVING_OFFER);}catch(Exception e){}
            } else {
                holder.tvOffers.setVisibility(View.GONE);
                holder.iv_offer_image.setVisibility(View.GONE);
            }




            if(obj.getPrice().toString() != null) {
                SpannableStringBuilder SS = new SpannableStringBuilder("`" + obj.getPrice().toString());
                SS.setSpan(new CustomTypefaceSpan("", font1), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                SS.setSpan(new CustomTypefaceSpan("", font2), 1, obj.getPrice().toString().length() - (obj.getPrice().toString().length() - 1), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.amount.setText(SS);
            }

            Typeface font3 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
            Typeface font4 =   Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
            if(obj.getSalePrice().toString() != null) {
                SpannableStringBuilder SS = new SpannableStringBuilder("`" + obj.getSalePrice().toString());
                SS.setSpan(new CustomTypefaceSpan("", font4), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                SS.setSpan(new CustomTypefaceSpan("", font3), 1, obj.getSalePrice().toString().length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.sale_price.setText(SS);
            }

            holder.quantity.setText("1");

            int edit_quantity = 0;
            ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
            if (cart_products != null && cart_products.size() > 0) {
                try {
                    for (int i = 0; i < cart_products.size(); i++) {
                        if (cart_products.get(i).getItem_id().equalsIgnoreCase(obj.getProductid())) {
                            edit_quantity = edit_quantity + cart_products.get(i).getQty();
                        }
                    }
                } catch (Exception e) {
                }
            }


            if (edit_quantity > 0) {
                holder.img_added_product_count.setVisibility(View.VISIBLE);
                holder.added_product_count.setVisibility(View.VISIBLE);
                holder.added_product_count.setText(String.valueOf(edit_quantity));
            } else {
                holder.added_product_count.setVisibility(View.INVISIBLE);
                holder.img_added_product_count.setVisibility(View.INVISIBLE);
            }

            holder.increase_quantity.setTag(holder.quantity);
            holder.decrease_quantity.setTag(holder.quantity);
            holder.add_to_cart.setTag(R.id.amount, holder.added_product_count);
            holder.add_to_cart.setTag(R.id.sale_price, holder.quantity);
            holder.add_to_cart.setTag(R.id.product_brand, holder.img_added_product_count);

            ImageLoader.getInstance().displayImage(obj.getImage(),
                    holder.prod_image, ((BaseActivity) activity).baseImageoptions);

            holder.prod_image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.increase_quantity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView quantity = (TextView) v.getTag();
                    int quant = Integer.parseInt(quantity.getText().toString());
                    quantity.setText("" + (quant + 1));
                    products.get(position).setQuantity(quantity.getText().toString());
                    MySharedPrefs.INSTANCE.putItemQuantity(quantity.getText().toString());
                }
            });

            holder.decrease_quantity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView quantity = (TextView) v.getTag();
                    int quant = Integer.parseInt(quantity.getText().toString());
                    if (quant >= 2) {
                        quantity.setText("" + (quant - 1));
                        products.get(position).setQuantity(quantity.getText().toString());
                        MySharedPrefs.INSTANCE.putItemQuantity(quantity.getText().toString());
                    }
                }
            });

            holder.add_to_cart.setTag(holder.quantity);
            holder.add_to_cart.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final TextView added_product_count = (TextView) v.getTag(R.id.amount);
                    final TextView quantity = (TextView) v.getTag(R.id.sale_price);
                    final ImageView img_added_product_count = (ImageView) v.getTag(R.id.product_brand);
                    if (UtilityMethods.isInternetAvailable(activity)) {
                    CartDetail cart_obj = new CartDetail();
                    cart_obj.setPrice(obj.getPrice());
                    cart_obj.setItem_id(obj.getProductid());
                    cart_obj.setName(obj.getName());
                    cart_obj.setQty(Integer.parseInt(quantity.getText().toString()));
                    cart_obj.setBrand(obj.getBrand());
                    cart_obj.setGramsORml(obj.getGramsORml());
                    cart_obj.setProductName(obj.getProductName());
                    cart_obj.setPromotionLevel(obj.getPromotionLevel());
                    if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
                        MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(Integer.parseInt(MySharedPrefs.INSTANCE.getTotalItem()) + Integer.parseInt(quantity.getText().toString())));
                        BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
                    } else {
                        MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(0 + Integer.parseInt(quantity.getText().toString())));
                        BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
                    }
                    cart_obj.setProduct_thumbnail(obj.getImage());
                        boolean result = UtilityMethods.writeLocalCart(activity, Constants.localCartFile, cart_obj);
                        UtilityMethods.writeCloneCart(activity, Constants.localCloneFile, cart_obj);
                        if (result) {
//					progressDialog.dismiss();
//					Toast.makeText(activity,ToastConstant.PRODUCT_ADDED_CART,Toast.LENGTH_LONG).show();
                            UtilityMethods.customToast(Constants.ToastConstant.PRODUCT_ADDED_CART, activity);
                            if (UtilityMethods.isInternetAvailable(activity)) {
                                BaseActivity.addToCart(cart_obj);
//							BaseActivity.syncStack();
                            } else {
                                UtilityMethods.customToast(Constants.ToastConstant.INTERNET_NOT_AVAILABLE, activity);
                            }
                        }
                    } else {
                        UtilityMethods.customToast(Constants.ToastConstant.INTERNET_NOT_AVAILABLE, activity);
                    }

                    int edit_quantity = 0;
                    ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
                    if (cart_products != null && cart_products.size() > 0) {
                        try {
                            for (int i = 0; i < cart_products.size(); i++) {
                                if (cart_products.get(i).getItem_id().equalsIgnoreCase(obj.getProductid())) {
                                    edit_quantity = edit_quantity + cart_products.get(i).getQty();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }

                    added_product_count.setText(String.valueOf(edit_quantity));
                    added_product_count.setVisibility(View.VISIBLE);
                    img_added_product_count.setVisibility(View.VISIBLE);

                    try{UtilityMethods.clickCapture(activity,obj.getPrice(),"",obj.getProductid(),"", CategoryTabs.SCREENNAME+"-"+obj.getProductName()+"-"+AppConstants.GA_EVENT_ADD_CART_ITEMS);}catch(Exception e){}
                }
            });

            if (obj.getStatus().equals("In stock")) {
                holder.rlOutofStock.setVisibility(View.GONE);
                holder.decrease_quantity.setVisibility(View.VISIBLE);
                holder.increase_quantity.setVisibility(View.VISIBLE);
                holder.quantity.setVisibility(View.VISIBLE);
                holder.add_to_cart.setVisibility(View.VISIBLE);

                holder.add_to_cart.setClickable(true);
                holder.increase_quantity.setClickable(true);
                holder.decrease_quantity.setClickable(true);
            } else {
                holder.rlOutofStock.setVisibility(View.VISIBLE);
                holder.decrease_quantity.setVisibility(View.GONE);
                holder.increase_quantity.setVisibility(View.GONE);
                holder.quantity.setVisibility(View.GONE);
                holder.add_to_cart.setVisibility(View.GONE);

                products.get(position).setQuantity("0");
                holder.add_to_cart.setClickable(false);
                holder.increase_quantity.setClickable(false);
                holder.decrease_quantity.setClickable(false);
            }

        } else {
            holder.prod_name.setVisibility(View.GONE);
            holder.prod_image.setVisibility(View.GONE);
            holder.sale_price.setVisibility(View.GONE);

            holder.amount.setVisibility(View.GONE);
            holder.quantity.setVisibility(View.GONE);
            holder.increase_quantity.setVisibility(View.GONE);
            holder.decrease_quantity.setVisibility(View.GONE);
            holder.add_to_cart.setVisibility(View.GONE);
            holder.img_added_product_count.setVisibility(View.GONE);
            holder.added_product_count.setVisibility(View.GONE);
            holder.iv_offer_image.setVisibility(View.GONE);
            holder.tvOffers.setVisibility(View.GONE);

            holder.tvVerticalBar.setVisibility(View.GONE);

            holder.prod_brand.setVisibility(View.GONE);
            holder.prod_gram_or_ml.setVisibility(View.GONE);
            holder.tvNoProducts.setVisibility(View.VISIBLE);
        }

        holder.quantity.setText(products.get(position).getQuantity());

        }catch(Exception e){
            new GrocermaxBaseException("ProductListAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView added_product_count, quantity, add_to_cart, amount, sale_price;
        ImageView img_added_product_count;
        TextView prod_brand, prod_name, prod_gram_or_ml;
        ImageView prod_image, increase_quantity, decrease_quantity;
        ImageView iv_offer_image;
        TextView tvOffers;
        TextView tvNoProducts;
        RelativeLayout rlOutofStock;
        TextView tvVerticalBar;
    }

    public void updateList(List<Product> list) {
        try{
            this.products = list;
            notifyDataSetChanged();
        }catch(Exception e){
            new GrocermaxBaseException("ProductListAdapter","updateList",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }
}

