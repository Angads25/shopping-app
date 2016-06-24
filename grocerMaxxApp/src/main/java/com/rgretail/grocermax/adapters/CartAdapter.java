package com.rgretail.grocermax.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.CartProductList;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.UpdateCartbg;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.OrderReviewBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.CustomTypefaceSpan;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter{

	Activity activity;
	private LayoutInflater inflater = null;
	int positionIndex = 0;                       //delete and add product of clone cart after confirm deletion in CartProductList onResponse()
	public static ArrayList<String> alOutOfStockId = new ArrayList<String>();
	//ArrayList<CartDetail> cartBean;

	public CartAdapter(Activity activity) {
		try {
			((BaseActivity) activity).initImageLoaderM();
			this.activity = activity;
			this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}catch(Exception e){
			new GrocermaxBaseException("CartAdapter", "CartAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	public int getCount() {
		try{
		if(CartProductList.cartList != null)
			return CartProductList.cartList.size();
		else {
			return 0;
		}
		}catch(Exception e){
			new GrocermaxBaseException("CartAdapter", "CartAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
		return 0;
	}

	@Override
	public CartDetail getItem(int position) {
		return CartProductList.cartList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try{
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_for_cart, parent, false);
			holder = new ViewHolder();
			holder.prod_brand_name = (TextView) convertView.findViewById(R.id.product_cart_brand_name);
			holder.prod_name = (TextView) convertView.findViewById(R.id.product_cart_name);
			holder.prod_gmorml = (TextView) convertView.findViewById(R.id.product_cart_mlorgm);
			holder.prod_old_price = (TextView) convertView.findViewById(R.id.old_price);
			holder.prod_mul_quantity = (TextView) convertView.findViewById(R.id.mul_quantity);               //starting quantity to which product multiply
//			holder.prod_name_1 = (TextView) convertView.findViewById(R.id.product_name1);
			holder.price = (TextView) convertView.findViewById(R.id.mrp);
			holder.offerImage = (ImageView) convertView.findViewById(R.id.offer_image);
			holder.tvOffers = (TextView) convertView.findViewById(R.id.tv_offers_cart);
			holder.ll_offer=(LinearLayout)convertView.findViewById(R.id.ll_offers_cart);
			holder.tv_saving_free = (TextView) convertView.findViewById(R.id.tv_saving_free);
	//		holder.tvMultiply = (TextView) convertView.findViewById(R.id.tv_multiply);

			holder.prod_old_price.setPaintFlags(holder.prod_old_price.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);

//			holder.amount = (TextView) convertView.findViewById(R.id.amount);
//			holder.amount.setPaintFlags(holder.amount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);


//			holder.saving = (TextView) convertView.findViewById(R.id.saving);
			holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
			holder.tv_quantity= (TextView) convertView.findViewById(R.id.tv_quantity);
			holder.prod_image = (ImageView) convertView.findViewById(R.id.product_image);
			holder.increase_quantity = (ImageView) convertView.findViewById(R.id.increase_quantity);
			holder.decrease_quantity = (ImageView) convertView.findViewById(R.id.decrease_quantity);

			holder.delete_item = (ImageView) convertView.findViewById(R.id.cancel_image);
			holder.llCancel = (LinearLayout) convertView.findViewById(R.id.ll_cancel);

			holder.rlOutofStock = (RelativeLayout) convertView.findViewById(R.id.rl_out_of_stock);
			holder.llPlusMinus = (LinearLayout) convertView.findViewById(R.id.llplusminus);

			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.prod_brand_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.prod_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.prod_gmorml.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
//		holder.prod_old_price.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.prod_mul_quantity.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
//		holder.tvMultiply.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.price.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
//		holder.quantity.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.tv_quantity.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));

		final CartDetail obj = getItem(position);

			if(obj.getStatus().equals("1")){  //product available
				holder.rlOutofStock.setVisibility(View.GONE);
				holder.rlOutofStock.setEnabled(true);
				holder.tvOffers.setAllCaps(false);
				//holder.tvOffers.setTextColor(Color.parseColor("#e5111e"));
				//holder.tvOffers.setBackgroundColor(Color.parseColor("#fec70d"));

				if(obj.getPromotionLevel() != null){
					holder.offerImage.setVisibility(View.VISIBLE);
					holder.ll_offer.setVisibility(View.VISIBLE);
					holder.tvOffers.setText(obj.getPromotionLevel());
				}else{
					holder.ll_offer.setVisibility(View.GONE);
					holder.offerImage.setVisibility(View.GONE);
				}
			}else{                            //product not available
				holder.rlOutofStock.setVisibility(View.GONE);
				holder.rlOutofStock.setEnabled(true);
				holder.tvOffers.setAllCaps(true);
				holder.tvOffers.setTextColor(activity.getResources().getColor(R.color.white));
				holder.tvOffers.setBackgroundColor(activity.getResources().getColor(R.color.primaryColor));

				if(CartProductList.getInstance().ll_place_order != null && CartProductList.getInstance().update_cart != null) {
					CartProductList.getInstance().ll_place_order.setVisibility(View.GONE);
					CartProductList.getInstance().update_cart.setVisibility(View.VISIBLE);
					CartProductList.getInstance().update_cart.setBackgroundColor(activity.getResources().getColor(R.color.updateshade));
					holder.increase_quantity.setVisibility(View.INVISIBLE);
					holder.decrease_quantity.setVisibility(View.INVISIBLE);
					holder.ll_offer.setVisibility(View.VISIBLE);
					try {
						System.out.println("QTY="+Integer.parseInt(obj.getWebQty()));
						if (obj.getWebQty() != null) {
							if (Integer.parseInt(obj.getWebQty()) > 0) {
								holder.tvOffers.setText(AppConstants.ToastConstant.REDUCE_QUANT_FIRST_PART+obj.getWebQty()+ AppConstants.ToastConstant.REDUCE_QUANT_SECOND_PART);
							} else {
								holder.tvOffers.setText(AppConstants.ToastConstant.REMOVE_ITEM_FOR_PROCEED);
							}
						}else{
								holder.tvOffers.setText(AppConstants.ToastConstant.REMOVE_ITEM_FOR_PROCEED);
						}
					}catch(Exception e){}

					if(obj.getPromotionLevel() != null){
						holder.offerImage.setVisibility(View.VISIBLE);
					}else{
					}
				}
			}
//		}



		holder.prod_brand_name.setText(obj.getBrand());

		holder.prod_name.setText(obj.getProductName());
		holder.prod_gmorml.setText(obj.getGramsORml());

		String price=obj.getPrice().toString().replace(",", "");

		String mrp=obj.getMrp().toString().replace(",", "");


		//float saving=obj.getQty()*(Float.parseFloat(mrp)-Float.parseFloat(price));
			//int value = obj.getQty();
			float row_total=Float.parseFloat(obj.getRow_total().replace(",",""));
			float saving= (obj.getQty()*Float.parseFloat(mrp))-row_total;
			if(Float.parseFloat(price)==0)
			{
				holder.increase_quantity.setVisibility(View.INVISIBLE);
				holder.decrease_quantity.setVisibility(View.INVISIBLE);
				holder.delete_item.setVisibility(View.INVISIBLE);
				holder.llCancel.setVisibility(View.INVISIBLE);
				holder.tv_saving_free.setText("Free Item");
				holder.tv_saving_free.setTextColor(Color.parseColor("#e5111e"));
				holder.tv_saving_free.setBackground(activity.getResources().getDrawable(R.drawable.free_item_bg));
			}else{
				holder.increase_quantity.setVisibility(View.VISIBLE);
				holder.decrease_quantity.setVisibility(View.VISIBLE);
				holder.delete_item.setVisibility(View.VISIBLE);
				holder.llCancel.setVisibility(View.VISIBLE);
				holder.tv_saving_free.setText("SAVING "+activity.getResources().getString(R.string.rs)+""+String.format("%.2f", saving));
				holder.tv_saving_free.setTextColor(activity.getResources().getColor(R.color.white));
				holder.tv_saving_free.setBackground(activity.getResources().getDrawable(R.drawable.saving_bg));
				if(obj.getStatus().equals("0")){      //product not available
					holder.increase_quantity.setVisibility(View.VISIBLE);
					holder.decrease_quantity.setVisibility(View.VISIBLE);
					holder.delete_item.setVisibility(View.VISIBLE);
					holder.llCancel.setVisibility(View.VISIBLE);
				}
			}

		ImageLoader.getInstance().displayImage(obj.getProduct_thumbnail(),holder.prod_image, ((BaseActivity)activity).baseImageoptions);

		int value = obj.getQty();
		//float total = value * Float.parseFloat(obj.getPrice().replace(",", ""));
		 float total=Float.parseFloat(obj.getRow_total().replace(",",""));
		String quant = String.format("%.2f", Float.parseFloat(price) );//+"|";
		holder.prod_mul_quantity.setText(String.valueOf(value));
		holder.tv_quantity.setText(String.valueOf(value));
		Typeface font4 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
		Typeface font3 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
		SpannableStringBuilder SS1 = new SpannableStringBuilder("`"+quant);
		SS1.setSpan (new CustomTypefaceSpan("", font3), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		SS1.setSpan (new CustomTypefaceSpan("", font4), 1, quant.length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		holder.quantity.setText(SS1);

		String str = String.format("%.2f", total).toString();
		Typeface font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
		Typeface font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
		SpannableStringBuilder SS = new SpannableStringBuilder("`"+str);
		SS.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		SS.setSpan (new CustomTypefaceSpan("", font2), 1, str.length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		holder.price.setText(SS);

		String strmrp = String.format("%.2f", Float.parseFloat(mrp)).toString();
		font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
		font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
		SpannableStringBuilder SSmrp = new SpannableStringBuilder("`"+strmrp.toString());
		SSmrp.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		SSmrp.setSpan (new CustomTypefaceSpan("", font2), 1, strmrp.length() - (strmrp.length()-1) ,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		holder.prod_old_price.setText(SSmrp);

		holder.increase_quantity.setTag(value);
		holder.decrease_quantity.setTag(value);
		holder.increase_quantity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					BaseActivity.keyboardVisibility = true;
				} else {
					BaseActivity.keyboardVisibility = false;
				}
				if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					if (!BaseActivity.keyboardVisibility)
						imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

				} else {
					if (BaseActivity.keyboardVisibility)
						imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
				}

				if(UtilityMethods.isInternetAvailable(activity)){
					if(CartProductList.getInstance().ll_place_order != null && CartProductList.getInstance().update_cart != null) {
						CartProductList.getInstance().ll_place_order.setVisibility(View.GONE);
						CartProductList.getInstance().update_cart.setVisibility(View.VISIBLE);
						CartProductList.getInstance().update_cart.setBackgroundColor(activity.getResources().getColor(R.color.updateshade));
					}
					int value1=Integer.parseInt(holder.tv_quantity.getText().toString());
					value1=value1+1;

					ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
					if(cart_products.size() > 0) {
						if (cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.cartList.get(position).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description
							cart_products.get(position).setQty(value1);  //plus
						}
					}
					CartProductList.cartList.get(position).setQty(value1);
					holder.tv_quantity.setText(String.valueOf(value1));
					holder.prod_mul_quantity.setText(String.valueOf(value1));

					//float total = value1 * Float.parseFloat(obj.getPrice().replace(",", ""));
					float total=Float.parseFloat(obj.getRow_total().replace(",",""));
					String str = String.format("%.2f", total).toString();
					float sub_totalPriceYouPay = 	Float.parseFloat(CartProductList.getInstance().tv_subTotal.getText().toString().replace(activity.getResources().getString(R.string.rs), ""))
							+Float.parseFloat(CartProductList.cartList.get(position).getPrice().replace(",", ""));

					OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

					if(sub_totalPriceYouPay >= Float.parseFloat(CartProductList.strShippingChargeLimit)){
						CartProductList.getInstance().tv_shipping.setText("Free");
					}else{                                 //shipping and billing charges
						CartProductList.getInstance().tv_shipping.setText(activity.getResources().getString(R.string.rs)+""+String.format("%.2f",Float.parseFloat(orderReviewBean.getShipping_ammount())));

					}

//  				    CartProductList.getInstance().tv_grandTotal.setText("Rs."+String.valueOf(totalPriceYouPay));
					CartProductList.getInstance().tv_subTotal.setText(activity.getResources().getString(R.string.rs) + String.valueOf(sub_totalPriceYouPay));

					CartProductList.getInstance().updateHeaderQuantity("1", "plus");

					v.setTag(value1);

				}else{
					UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
				}
				//((CartProductList)activity).changeQuantity(CartProductList.cartList.get(position).getItem_id(),CartProductList.cartList.get(position).getQty());
			}
		});
		holder.decrease_quantity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				positionIndex = position;                                             //latest
//				if(BaseActivity.keyboardVisibility)
//					UtilityMethods.hideKeyBoard(activity);

				InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					BaseActivity.keyboardVisibility = true;
				} else {
					BaseActivity.keyboardVisibility = false;
				}
				if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					if (!BaseActivity.keyboardVisibility)
						imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

				} else {
					if (BaseActivity.keyboardVisibility)
						imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
				}

				if(UtilityMethods.isInternetAvailable(activity)){
					if(CartProductList.getInstance().ll_place_order != null && CartProductList.getInstance().update_cart != null) {
						CartProductList.getInstance().ll_place_order.setVisibility(View.GONE);
						CartProductList.getInstance().update_cart.setVisibility(View.VISIBLE);
						CartProductList.getInstance().update_cart.setBackgroundColor(activity.getResources().getColor(R.color.updateshade));
					}
					int value1=Integer.parseInt(holder.tv_quantity.getText().toString());
					//int value1 = Integer.parseInt(quantity.getText().toString());
					value1=value1-1;
					if(value1>0)
					{
						ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
//						if(cart_products.size() > 0 && CartProductList.cartList.size() > 0){


						if(cart_products.size()>0) {
							if (cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.cartList.get(position).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description
								cart_products.get(position).setQty(value1);  //minus
							}
						}

						CartProductList.cartList.get(position).setQty(value1);
						holder.tv_quantity.setText(String.valueOf(value1));

						holder.prod_mul_quantity.setText(String.valueOf(value1));

						//float total = value1 * Float.parseFloat(obj.getPrice().replace(",", ""));
						float total=Float.parseFloat(obj.getRow_total().replace(",",""));
						String str = String.format("%.2f", total).toString();

						OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

						float sub_totalPriceYouPay = 	Float.parseFloat(CartProductList.getInstance().tv_subTotal.getText().toString().replace(activity.getResources().getString(R.string.rs), ""))
								-Float.parseFloat(CartProductList.cartList.get(position).getPrice().replace(",", ""));
//			        if(totalPriceYouPay >= Float.parseFloat(orderReviewBean.getShipping_ammount())){
						if(sub_totalPriceYouPay >= Float.parseFloat(CartProductList.strShippingChargeLimit)){
							CartProductList.getInstance().tv_shipping.setText("Free");
						}else{                                 //shipping and billing charges
//						totalPriceYouPay += 50;
//						CartProductList.getInstance().tv_shipping.setText("Rs.50.0");

//						CartProductList.getInstance().tv_shipping.setText(orderReviewBean.getShipping_ammount());
						CartProductList.getInstance().tv_shipping.setText(activity.getResources().getString(R.string.rs)+""+String.format("%.2f",Float.parseFloat(orderReviewBean.getShipping_ammount())));
						}

//				    CartProductList.getInstance().tv_grandTotal.setText("Rs."+String.valueOf(totalPriceYouPay));
						CartProductList.getInstance().tv_subTotal.setText(activity.getResources().getString(R.string.rs)+""+String.valueOf(sub_totalPriceYouPay));

						CartProductList.getInstance().updateHeaderQuantity("1","minus");

						v.setTag(value1);
						//((CartProductList)activity).changeQuantity(CartProductList.cartList.get(position).getItem_id(),CartProductList.cartList.get(position).getQty());
					}
				}else{
					UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
				}
			}
		});

		holder.delete_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					deleteLocal(v, position, holder);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
		});

		holder.llCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteLocal(v, position,holder);
			}
		});
		}catch(Exception e){
			new GrocermaxBaseException("CartAdapter", "getView", e.getMessage(), GrocermaxBaseException.EXCEPTION, String.valueOf(CartProductList.cartList.get(position)));
		}

		return convertView;
	}

	private class ViewHolder {
		TextView prod_brand_name, quantity, price,tv_quantity;
		TextView prod_name,prod_gmorml;
		TextView prod_old_price;
		TextView prod_mul_quantity;
		TextView prod_name_1;
		TextView saving;
		ImageView offerImage;
		TextView tvOffers;
		TextView tv_saving_free;
		TextView tvMultiply;
		//		TextView quantity;
//		TextView price,amount,tv_quantity,;
		ImageView prod_image, increase_quantity,decrease_quantity, delete_item;
		LinearLayout llCancel,ll_offer;
		RelativeLayout rlOutofStock;
		LinearLayout llPlusMinus;

	}

	public void updateList(ArrayList<CartDetail> cartBean)
	{
		try {
			CartProductList.cartList = cartBean;
			notifyDataSetChanged();
		}catch(Exception e){
			new GrocermaxBaseException("CartAdapter", "updateList", e.getMessage(), GrocermaxBaseException.EXCEPTION, "no detail");
		}
	}

	private void deleteLocal(View v,int position,ViewHolder holder)
	{
		try{
		String userId = MySharedPrefs.INSTANCE.getUserId();
		if(UtilityMethods.isInternetAvailable(activity)){
			if(CartProductList.getInstance().ll_place_order != null && CartProductList.getInstance().update_cart != null) {
				CartProductList.getInstance().ll_place_order.setVisibility(View.GONE);
				CartProductList.getInstance().update_cart.setVisibility(View.VISIBLE);
				CartProductList.getInstance().update_cart.setBackgroundColor(activity.getResources().getColor(R.color.updateshade));
			}
			UpdateCartbg.getInstance().alDeleteId.add(CartProductList.cartList.get(position).getItem_id());  //saves deleted id for background hitting URL for edit

			if(CartProductList.getInstance().sbDeleteProdId != null) {
				if (CartProductList.getInstance().sbDeleteProdId.length() > 0) {
					CartProductList.getInstance().sbDeleteProdId.append("," + CartProductList.cartList.get(position).getItem_id());
				} else {
					CartProductList.getInstance().sbDeleteProdId.append(CartProductList.cartList.get(position).getItem_id());
				}
			}

			ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);

			if(cart_products.size() > 0) {
				if (cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.cartList.get(position).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description
					UtilityMethods.deleteCloneCartItem(activity, cart_products.get(position).getItem_id());  //delete particular item from clone cart locally to update quantity on product listing and description
				}
			}

			int quantity = Integer.parseInt(holder.tv_quantity.getText().toString());
			float price = Float.parseFloat(CartProductList.cartList.get(position).getPrice().replace(",", ""));
			float totalDeltedPrice = quantity * price;

			OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

			float sub_totalPriceYouPay = Float.parseFloat(CartProductList.getInstance().tv_grandTotal.getText().toString().replace(activity.getResources().getString(R.string.rs), "")) - totalDeltedPrice;    //new

			if(sub_totalPriceYouPay >= Float.parseFloat(CartProductList.strShippingChargeLimit)){
				CartProductList.getInstance().tv_shipping.setText("Free");
			}else{                                 //shipping and billing charges
				CartProductList.getInstance().tv_shipping.setText(activity.getResources().getString(R.string.rs)+""+String.format("%.2f",Float.parseFloat(orderReviewBean.getShipping_ammount())));
			}

			CartProductList.getInstance().tv_grandTotal.setText(activity.getResources().getString(R.string.rs)+"" + String.format("%.2f", sub_totalPriceYouPay));

			int value1=Integer.parseInt(holder.tv_quantity.getText().toString());
			CartProductList.getInstance().updateHeaderQuantity(String.valueOf(value1), "minus");

			CartProductList.cartList.remove(position);
			boolean bMoveToHomeScreen = true;
			if(CartProductList.cartList.size() > 0){
				for(int i=0;i<CartProductList.cartList.size();i++)
				{
					CartDetail cartvalue = CartProductList.cartList.get(i);
					String pricee = cartvalue.getPrice().toString().replace(",", "");
					if(Float.parseFloat(pricee) != 0){                 //for product not free item
						bMoveToHomeScreen = false;
					}
				}
				if(bMoveToHomeScreen){
					UtilityMethods.customToast(Constants.ToastConstant.VIEW_CART_EMPTY, activity);
					((CartProductList) activity).cart_count_txt.setText("0");          //all item remove but offers remain in clone cart.so manually put 0.
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf("0"));
					((CartProductList) activity).finish();
				}else{
					updateList(CartProductList.cartList);         //update list after delete particular item
				}

			}else{
				UtilityMethods.customToast(Constants.ToastConstant.VIEW_CART_EMPTY, activity);
				((CartProductList) activity).cart_count_txt.setText("0");          //all item remove but offers remain in clone cart.so manually put 0.
				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf("0"));
				((CartProductList) activity).finish();
			}
		}else{
			UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
		}
		}catch(Exception e){
			new GrocermaxBaseException("CartAdapter", "deleteLocal", e.getMessage(), GrocermaxBaseException.EXCEPTION, "no detail");
		}
	}


}


