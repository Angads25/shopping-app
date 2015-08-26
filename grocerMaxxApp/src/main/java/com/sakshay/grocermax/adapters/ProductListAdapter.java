package com.sakshay.grocermax.adapters;

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
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.Product;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.CustomTypefaceSpan;
import com.sakshay.grocermax.utils.UtilityMethods;

public class ProductListAdapter extends BaseAdapter {

	List<Product> products = null;
	Activity activity;
	private LayoutInflater inflater = null;
	ProgressDialog progressDialog;
	Typeface face;


	public ProductListAdapter(){

	}

	public ProductListAdapter(Activity activity, List<Product> list) {
		((BaseActivity) activity).initImageLoaderM();
		this.activity = activity;
		this.products = list;
		this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		for(int i=0;i<products.size();i++)
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.product_list_row, parent,
					false);
			holder = new ViewHolder();
//			holder.prod_name = (TextView) convertView
//					.findViewById(R.id.product_name);
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
			holder.rlOutofStock =  (RelativeLayout) convertView
					.findViewById(R.id.rl_out_of_stock);


			holder.added_product_count = (TextView) convertView
					.findViewById(R.id.added_product_count);
			holder.img_added_product_count = (ImageView) convertView
					.findViewById(R.id.img_added_product_count);
			holder.iv_offer_image = (ImageView) convertView
					.findViewById(R.id.offer_image);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Product obj = getItem(position);

		holder.prod_brand.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.prod_name.setTypeface(CustomFonts.getInstance().getRobotoBold(activity));
		holder.prod_gram_or_ml.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));


		if(!obj.getName().equals("No product found for this category"))
		{
			holder.prod_name.setText(obj.getName());
			if (AppConstants.DEBUG) {
				Log.d("", "PRICE WITHOUT DISSCOUNT::" + obj.getSalePrice());
			}


			holder.prod_brand.setText(obj.getBrand());
			holder.prod_gram_or_ml.setText(obj.getGramsORml());
			holder.prod_name.setText(obj.getProductName());

			if(obj.getPromotionLevel() != null){
				holder.tvOffers.setText(obj.getPromotionLevel());
				holder.tvOffers.setVisibility(View.VISIBLE);
				holder.iv_offer_image.setVisibility(View.VISIBLE);
			}else{
				holder.tvOffers.setVisibility(View.GONE);
				holder.iv_offer_image.setVisibility(View.GONE);
			}

//		face = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
//		holder.amount.setText("Rs. " + obj.getPrice().toString());
//		holder.amount.setTypeface(face);

//		TextView txt = (TextView) findViewById(R.id.custom_fonts);  
//        txt.setTextSize(30);
//        Typeface font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");


			Typeface font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
			Typeface font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf");
			SpannableStringBuilder SS = new SpannableStringBuilder("`"+obj.getPrice().toString());
			SS.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			SS.setSpan (new CustomTypefaceSpan("", font2), 1, obj.getPrice().toString().length()-(obj.getPrice().toString().length()-1),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			holder.amount.setText(SS);

//		Typeface type = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
//        holder.amount.setText("`"+obj.getPrice().toString());
//		holder.amount.setTypeface(type);

//		holder.amount.setTypeface(CustomFonts.getInstance().getRobotoLight(activity));


//	    holder.sale_price.setTypeface(face);
//		holder.sale_price.setText("`"+ obj.getSalePrice().toString());
//		holder.sale_price.setText("Rs. " + obj.getSalePrice());

			font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Bold.ttf");
			font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
			SS = new SpannableStringBuilder("`"+obj.getSalePrice().toString());
			SS.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        SS.setSpan (new CustomTypefaceSpan("", font2), 1, obj.getSalePrice().toString().length()-(obj.getSalePrice().toString().length()-1),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			SS.setSpan(new CustomTypefaceSpan("", font2), 1, obj.getSalePrice().toString().length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			holder.sale_price.setText(SS);


			holder.quantity.setText("1");

//        holder.sale_price.setText("`"+ obj.getSalePrice().toString());
//        holder.sale_price.setTypeface(CustomFonts.getInstance().getRobotoBold(activity));

			int edit_quantity = 0;
			ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
			if(cart_products != null && cart_products.size() > 0)
			{
				try
				{
					for(int i=0; i<cart_products.size(); i++)
					{
						if(cart_products.get(i).getItem_id().equalsIgnoreCase(obj.getProductid()))
						{
							edit_quantity  = edit_quantity + cart_products.get(i).getQty();
						}
					}
				}catch(Exception e){}
			}


			if(edit_quantity > 0){
				holder.img_added_product_count.setVisibility(View.VISIBLE);
//			holder.added_product_count.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 8));
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			holder.added_product_count.setLayoutParams(params);
//			if(String.valueOf(holder.added_product_count).length() == 1){
//				params.setMargins(10,10,10,10);
//			}else{
//				params.setMargins(10,10,10,10);
//			}
				holder.added_product_count.setVisibility(View.VISIBLE);
				holder.added_product_count.setText(String.valueOf(edit_quantity));
			}else{
				holder.added_product_count.setVisibility(View.INVISIBLE);
				holder.img_added_product_count.setVisibility(View.INVISIBLE);
			}


//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.added_product_count.getLayoutParams();
//			if(String.valueOf(edit_quantity).length() > 1){
//				params.setMargins(39, 17, 0, 0);  // left, top, right, bottom
//				holder.added_product_count.setLayoutParams(params);
//			}
//			else if(String.valueOf(edit_quantity).length() == 1){
//				params.setMargins(46, 17, 0, 0);  // left, top, right, bottom
//				holder.added_product_count.setLayoutParams(params);
//			}

			//holder.quantity.setText("1");
			holder.increase_quantity.setTag(holder.quantity);
			holder.decrease_quantity.setTag(holder.quantity);
			holder.add_to_cart.setTag(R.id.amount,holder.added_product_count);
			holder.add_to_cart.setTag(R.id.sale_price,holder.quantity);
			holder.add_to_cart.setTag(R.id.product_brand,holder.img_added_product_count);

			ImageLoader.getInstance().displayImage(obj.getImage(),
					holder.prod_image, ((BaseActivity) activity).baseImageoptions);

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
//				final TextView quantity = (TextView) v.getTag();
					final TextView added_product_count = (TextView) v.getTag(R.id.amount);
					final TextView quantity = (TextView) v.getTag(R.id.sale_price);
					final ImageView img_added_product_count = (ImageView) v.getTag(R.id.product_brand);

					CartDetail cart_obj = new CartDetail();
					cart_obj.setPrice(obj.getPrice());
					cart_obj.setItem_id(obj.getProductid());
					cart_obj.setName(obj.getName());
					cart_obj.setQty(Integer.parseInt(quantity.getText().toString()));
					cart_obj.setBrand(obj.getBrand());
					cart_obj.setGramsORml(obj.getGramsORml());
					cart_obj.setProductName(obj.getProductName());
					cart_obj.setPromotionLevel(obj.getPromotionLevel());
					if(MySharedPrefs.INSTANCE.getTotalItem()!=null)
					{
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(Integer.parseInt(MySharedPrefs.INSTANCE.getTotalItem())+Integer.parseInt(quantity.getText().toString())));
						BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					}
					else
					{
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(0+Integer.parseInt(quantity.getText().toString())));
						BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					}
					cart_obj.setProduct_thumbnail(obj.getImage());
					// String key = obj.getProductid().toString();
					boolean result = UtilityMethods.writeLocalCart(activity,Constants.localCartFile, cart_obj);
					UtilityMethods.writeCloneCart(activity,Constants.localCloneFile, cart_obj);
					if (result)
					{
//					progressDialog.dismiss();
//					Toast.makeText(activity,ToastConstant.PRODUCT_ADDED_CART,Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(ToastConstant.PRODUCT_ADDED_CART, activity);
					}

					int edit_quantity = 0;
					ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
					if(cart_products != null && cart_products.size() > 0)
					{
						try
						{
							for(int i=0; i<cart_products.size(); i++)
							{
								if(cart_products.get(i).getItem_id().equalsIgnoreCase(obj.getProductid()))
								{
									edit_quantity  = edit_quantity+cart_products.get(i).getQty();
								}
							}
						}catch(Exception e){}
					}

					added_product_count.setText(String.valueOf(edit_quantity));
					added_product_count.setVisibility(View.VISIBLE);
					img_added_product_count.setVisibility(View.VISIBLE);


//				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)added_product_count.getLayoutParams();
//				if(String.valueOf(edit_quantity).length() > 1){
//					params.setMargins(39, 17, 0, 0);  // left, top, right, bottom
//					added_product_count.setLayoutParams(params);
//				}
//				else if(String.valueOf(edit_quantity).length() == 1){
//					params.setMargins(46, 17, 0, 0);  // left, top, right, bottom
//					added_product_count.setLayoutParams(params);
//				}

//				if (MySharedPrefs.INSTANCE.getUserId() == null|| MySharedPrefs.INSTANCE.getUserId().equals("")) 
//				{
//					if(MySharedPrefs.INSTANCE.getQuoteId()==null || MySharedPrefs.INSTANCE.getQuoteId().equals(""))   //when user is not login and added products in cart and display view cart then quote id create of user.
//					{
////					
////					progressDialog = new ProgressDialog(activity);
////					progressDialog.setMessage("Loading...");
////					progressDialog.show();
////					progressDialog.setCancelable(false);
////					
////					final Handler handler = new Handler();
////					handler.postDelayed(new Runnable() {
////					  @Override
////					  public void run() {
//						  CartDetail cart_obj = new CartDetail();
//							cart_obj.setPrice(obj.getPrice());
//							cart_obj.setItem_id(obj.getProductid());
//							cart_obj.setName(obj.getName());
//							cart_obj.setQty(Integer.parseInt(quantity.getText().toString()));
//							if(MySharedPrefs.INSTANCE.getTotalItem()!=null)
//							{
//								MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(Integer.parseInt(MySharedPrefs.INSTANCE.getTotalItem())+Integer.parseInt(quantity.getText().toString())));
//								BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
//							}
//							else
//							{
//								MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(0+Integer.parseInt(quantity.getText().toString())));
//								BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
//							}
//							cart_obj.setProduct_thumbnail(obj.getImage());
//							// String key = obj.getProductid().toString();
//							boolean result = UtilityMethods.writeLocalCart(activity,Constants.localCartFile, cart_obj);
//							UtilityMethods.writeCloneCart(activity,Constants.localCloneFile, cart_obj);      
//							if (result)
//							{
////								progressDialog.dismiss();
////								Toast.makeText(activity,ToastConstant.PRODUCT_ADDED_CART,Toast.LENGTH_LONG).show();
//								UtilityMethods.customToast(ToastConstant.PRODUCT_ADDED_CART, activity);
//							}
//							
//							int edit_quantity = 0;
//					        ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
//							if(cart_products != null && cart_products.size() > 0)
//							{
//								try
//								{
//									for(int i=0; i<cart_products.size(); i++)
//									{
//										if(cart_products.get(i).getItem_id().equalsIgnoreCase(obj.getProductid()))
//										{
//											edit_quantity  = edit_quantity+cart_products.get(i).getQty();
//										}
//									}
//								}catch(Exception e){}
//							}
//							
//							
//							added_product_count.setText(String.valueOf(edit_quantity));
//							added_product_count.setVisibility(View.VISIBLE);
//							img_added_product_count.setVisibility(View.VISIBLE);
////							notifyDataSetChanged();
//							
////					  }
////					}, 1000);
////					
//				}else
//					{
//						if (activity instanceof ProductListScreen) {
//							((ProductListScreen) activity).addToCartGuest(
//									obj.getProductid(), quantity.getText().toString());
//						}else if (activity instanceof CategoryTabs) {
//							((CategoryTabs) activity).addToCartGuest(
//									obj.getProductid(), quantity.getText().toString());
//						}
//					}
////				} else {
////					if (activity instanceof ProductListScreen) {
////						((ProductListScreen) activity).addToCart(
////								obj.getProductid(), quantity.getText().toString());
////					}else if (activity instanceof CategoryTabs) {
////						((CategoryTabs) activity).addToCart(
////								obj.getProductid(), quantity.getText().toString());
////					}
////				}
//			}
				}
			});

			if(obj.getStatus().equals("In stock"))
			{
//				holder.increase_quantity.setImageResource(R.drawable.plus_icon);
//				holder.decrease_quantity.setImageResource(R.drawable.minus_icon);
//				holder.add_to_cart.setBackgroundResource(R.drawable.orange_border_gradient_box);
				holder.rlOutofStock.setVisibility(View.GONE);
				holder.decrease_quantity.setVisibility(View.VISIBLE);
				holder.increase_quantity.setVisibility(View.VISIBLE);
				holder.quantity.setVisibility(View.VISIBLE);
				holder.add_to_cart.setVisibility(View.VISIBLE);

				holder.add_to_cart.setClickable(true);
				holder.increase_quantity.setClickable(true);
				holder.decrease_quantity.setClickable(true);
			}else{
//				holder.increase_quantity.setImageResource(R.drawable.plus_icon_disable);
//				holder.decrease_quantity.setImageResource(R.drawable.minus_icon_disable);
//				holder.add_to_cart.setBackgroundResource(R.drawable.gray_border_gradient_box);
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

		}
		else
		{
			holder.prod_name.setText(obj.getName());
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
//			holder.prod_name.setVisibility(View.GONE);
			holder.prod_gram_or_ml.setVisibility(View.GONE);
		}


		holder.quantity.setText(products.get(position).getQuantity());


		return convertView;
	}

	private class ViewHolder {

		//		TextView prod_name

		TextView added_product_count,quantity, add_to_cart, amount, sale_price;
		ImageView img_added_product_count;
		TextView prod_brand,prod_name,prod_gram_or_ml;
		ImageView prod_image, increase_quantity, decrease_quantity;
		ImageView iv_offer_image;
		TextView tvOffers;
		//		TextView prod_out_of_stock,tvMultiply;
		RelativeLayout rlOutofStock;
		TextView tvVerticalBar;
	}

	public void updateList(List<Product> list) {
		this.products = list;

		notifyDataSetChanged();
	}
}

