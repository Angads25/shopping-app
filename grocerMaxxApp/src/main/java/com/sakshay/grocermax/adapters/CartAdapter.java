package com.sakshay.grocermax.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera.Size;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.CartProductList;
import com.sakshay.grocermax.MyApplication;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.UpdateCartbg;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.OrderReviewBean;
import com.sakshay.grocermax.bean.OrderedProductList;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.CustomTypefaceSpan;
import com.sakshay.grocermax.utils.UtilityMethods;

public class CartAdapter extends BaseAdapter{

	Activity activity;
	private LayoutInflater inflater = null;
	int positionIndex = 0;                       //delete and add product of clone cart after confirm deletion in CartProductList onResponse()
	//ArrayList<CartDetail> cartBean;



	public CartAdapter(Activity activity) {
		((BaseActivity)activity).initImageLoaderM();
		this.activity = activity;

		//this.cartBean = cartBean;
		this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if(CartProductList.cartList != null)
			return CartProductList.cartList.size();
		else {
			return 0;
		}
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

			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.prod_brand_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.prod_name.setTypeface(CustomFonts.getInstance().getRobotoBold(activity));
		holder.prod_gmorml.setTypeface(CustomFonts.getInstance().getRobotoLight(activity));
//		holder.prod_old_price.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.prod_mul_quantity.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
//		holder.tvMultiply.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.price.setTypeface(CustomFonts.getInstance().getRobotoBold(activity));
//		holder.quantity.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));
		holder.tv_quantity.setTypeface(CustomFonts.getInstance().getRobotoRegular(activity));

		final CartDetail obj = getItem(position);
//		holder.prod_brand_name.setText(obj.getBrand());
		holder.prod_brand_name.setText(obj.getBrand());
//		holder.prod_brand_name.setText(obj.getName());
//		holder.prod_name_1.setText(obj.getName());

		holder.prod_name.setText(obj.getProductName());
		holder.prod_gmorml.setText(obj.getGramsORml());

		if(obj.getPromotionLevel() != null){
			holder.offerImage.setVisibility(View.VISIBLE);
			holder.tvOffers.setVisibility(View.VISIBLE);
			holder.tvOffers.setText(obj.getPromotionLevel());
		}else{
			holder.tvOffers.setVisibility(View.GONE);
			holder.offerImage.setVisibility(View.GONE);
		}

		String price=obj.getPrice().toString().replace(",", "");
//		holder.price.setText("Rs. " + String.format("%.2f", Float.parseFloat(price)));

		String mrp=obj.getMrp().toString().replace(",", "");

//		holder.amount.setText("Rs. " + String.format("%.2f", Float.parseFloat(mrp)));

		float saving=obj.getQty()*(Float.parseFloat(mrp)-Float.parseFloat(price));
//		holder.saving.setText("You Save : Rs. " + String.format("%.2f", saving));

		if(Float.parseFloat(price)==0)
		{
			holder.increase_quantity.setVisibility(View.INVISIBLE);
			holder.decrease_quantity.setVisibility(View.INVISIBLE);
			holder.delete_item.setVisibility(View.INVISIBLE);
			holder.llCancel.setVisibility(View.INVISIBLE);

		}else{
			holder.increase_quantity.setVisibility(View.VISIBLE);
			holder.decrease_quantity.setVisibility(View.VISIBLE);
			holder.delete_item.setVisibility(View.VISIBLE);
			holder.llCancel.setVisibility(View.VISIBLE);
		}

		ImageLoader.getInstance().displayImage(obj.getProduct_thumbnail(),holder.prod_image, ((BaseActivity)activity).baseImageoptions);

		int value = obj.getQty();

//		String price=obj.getPrice().toString().replace(",", "");

		float total = value * Float.parseFloat(obj.getPrice().replace(",", ""));

//		String quant = value + " * " + String.format("%.2f", Float.parseFloat(price)) + " = Rs. " + String.format("%.2f", total);

//		String quant = "`" + String.format("%.2f", Float.parseFloat(price) );//+"|";
		String quant = String.format("%.2f", Float.parseFloat(price) );//+"|";

		holder.prod_mul_quantity.setText(String.valueOf(value));

		holder.tv_quantity.setText(String.valueOf(value));

//		holder.quantity.setTypeface(CustomFonts.getInstance().getRupee(activity));
//		holder.quantity.setText(quant);

		Typeface font4 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
		Typeface font3 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
		SpannableStringBuilder SS1 = new SpannableStringBuilder("`"+quant);
		SS1.setSpan (new CustomTypefaceSpan("", font3), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		SS1.setSpan (new CustomTypefaceSpan("", font4), 1, quant.length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		holder.quantity.setText(SS1);

//		String price=obj.getPrice().toString().replace(",", "");
//		holder.price.setText("Rs. " + String.format("%.2f", Float.parseFloat(price)));


//		font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Bold.ttf");
//	    font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
//        SS = new SpannableStringBuilder("`"+obj.getSalePrice().toString());
//        SS.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
////        SS.setSpan (new CustomTypefaceSpan("", font2), 1, obj.getSalePrice().toString().length()-(obj.getSalePrice().toString().length()-1),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        SS.setSpan (new CustomTypefaceSpan("", font2), 1, obj.getSalePrice().toString().length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        holder.sale_price.setText(SS);
//

		String str = String.format("%.2f", total).toString();
		Typeface font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Bold.ttf");
		Typeface font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
		SpannableStringBuilder SS = new SpannableStringBuilder("`"+str);
		SS.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		SS.setSpan (new CustomTypefaceSpan("", font2), 1, str.length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		holder.price.setText(SS);

//		holder.price.setTypeface(CustomFonts.getInstance().getRupee(activity));

//		holder.price.setText("`"+String.format("%.2f", total));
//		holder.prod_old_price.setTypeface(CustomFonts.getInstance().getRupee(activity));
//		holder.prod_old_price.setText("`" + String.format("%.2f", total));

		String strmrp = String.format("%.2f", Float.parseFloat(mrp)).toString();
		font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Bold.ttf");
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
				if(BaseActivity.keyboardVisibility)
					UtilityMethods.hideKeyBoard(activity);
				if(UtilityMethods.isInternetAvailable(activity)){
					if(CartProductList.getInstance().place_order != null && CartProductList.getInstance().update_cart != null) {
						CartProductList.getInstance().place_order.setVisibility(View.GONE);
						CartProductList.getInstance().update_cart.setVisibility(View.VISIBLE);
					}
					int value1=Integer.parseInt(holder.tv_quantity.getText().toString());
					//int value1 = Integer.parseInt(quantity.getText().toString());
					value1=value1+1;

					ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);

//					int size1 = cart_products.size();
//					int size2 = CartProductList.cartList.size();

//					if(cart_products.size() > 0 && CartProductList.cartList.size() > 0){
					if(cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.cartList.get(position).getItem_id())){  //manage clone cart when added or deleted to show update quantity on product listing and description
//						cart_products.get(position).setQty(CartProductList.cartList.get(position).getQty());  //plus
						cart_products.get(position).setQty(value1);  //plus
					}
//					}

					CartProductList.cartList.get(position).setQty(value1);
					holder.tv_quantity.setText(String.valueOf(value1));
					holder.prod_mul_quantity.setText(String.valueOf(value1));

					float total = value1 * Float.parseFloat(obj.getPrice().replace(",", ""));
					String str = String.format("%.2f", total).toString();

//					Typeface font4 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
//					Typeface font3 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
//					SpannableStringBuilder SS1 = new SpannableStringBuilder("`"+str);
//					SS1.setSpan (new CustomTypefaceSpan("", font3), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//					SS1.setSpan (new CustomTypefaceSpan("", font4), 1, str.length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//					holder.price.setText(SS1);

					float sub_totalPriceYouPay = 	Float.parseFloat(CartProductList.getInstance().tv_subTotal.getText().toString().replace("Rs.", ""))
							+Float.parseFloat(CartProductList.cartList.get(position).getPrice().replace(",", ""));

					OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

//			        if(totalPriceYouPay >= Float.parseFloat(orderReviewBean.getShipping_ammount())){
					if(sub_totalPriceYouPay >= Float.parseFloat(CartProductList.strShippingChargeLimit)){
						CartProductList.getInstance().tv_shipping.setText("Rs.0.0");
					}else{                                 //shipping and billing charges
//						totalPriceYouPay += 50;
//						CartProductList.getInstance().tv_shipping.setText("Rs.50.0");
						CartProductList.getInstance().tv_shipping.setText(orderReviewBean.getShipping_ammount());
					}

//  				    CartProductList.getInstance().tv_grandTotal.setText("Rs."+String.valueOf(totalPriceYouPay));
					CartProductList.getInstance().tv_subTotal.setText("Rs." + String.valueOf(sub_totalPriceYouPay));


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
				if(BaseActivity.keyboardVisibility)
					UtilityMethods.hideKeyBoard(activity);
				if(UtilityMethods.isInternetAvailable(activity)){
					if(CartProductList.getInstance().place_order != null && CartProductList.getInstance().update_cart != null) {
						CartProductList.getInstance().place_order.setVisibility(View.GONE);
						CartProductList.getInstance().update_cart.setVisibility(View.VISIBLE);
					}
					int value1=Integer.parseInt(holder.tv_quantity.getText().toString());
					//int value1 = Integer.parseInt(quantity.getText().toString());
					value1=value1-1;
					if(value1>0)
					{
						ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
//						if(cart_products.size() > 0 && CartProductList.cartList.size() > 0){
						if(cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.cartList.get(position).getItem_id())){  //manage clone cart when added or deleted to show update quantity on product listing and description
							//						cart_products.get(position).setQty(CartProductList.cartList.get(position).getQty());  //minus
							cart_products.get(position).setQty(value1);  //minus
						}
//						}

						CartProductList.cartList.get(position).setQty(value1);
						holder.tv_quantity.setText(String.valueOf(value1));


						holder.prod_mul_quantity.setText(String.valueOf(value1));

						float total = value1 * Float.parseFloat(obj.getPrice().replace(",", ""));
						String str = String.format("%.2f", total).toString();

//						Typeface font4 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
//						Typeface font3 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
//						SpannableStringBuilder SS1 = new SpannableStringBuilder("`"+str);
//						SS1.setSpan (new CustomTypefaceSpan("", font3), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//						SS1.setSpan (new CustomTypefaceSpan("", font4), 1, str.length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//						holder.price.setText(SS1);

						OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

						float sub_totalPriceYouPay = 	Float.parseFloat(CartProductList.getInstance().tv_subTotal.getText().toString().replace("Rs.", ""))
								-Float.parseFloat(CartProductList.cartList.get(position).getPrice().replace(",", ""));
//			        if(totalPriceYouPay >= Float.parseFloat(orderReviewBean.getShipping_ammount())){
						if(sub_totalPriceYouPay >= Float.parseFloat(CartProductList.strShippingChargeLimit)){
							CartProductList.getInstance().tv_shipping.setText("Rs.0.0");
						}else{                                 //shipping and billing charges
//						totalPriceYouPay += 50;
//						CartProductList.getInstance().tv_shipping.setText("Rs.50.0");
							CartProductList.getInstance().tv_shipping.setText(orderReviewBean.getShipping_ammount());
						}

//				    CartProductList.getInstance().tv_grandTotal.setText("Rs."+String.valueOf(totalPriceYouPay));
						CartProductList.getInstance().tv_subTotal.setText("Rs."+String.valueOf(sub_totalPriceYouPay));

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
		TextView tvMultiply;
		//		TextView quantity;
//		TextView price,amount,tv_quantity,;
		ImageView prod_image, increase_quantity,decrease_quantity, delete_item;
		LinearLayout llCancel;
	}

	public void updateList(ArrayList<CartDetail> cartBean)
	{
		CartProductList.cartList = cartBean;
		notifyDataSetChanged();
	}

	private void deleteLocal(View v,int position,ViewHolder holder)
	{
		String userId = MySharedPrefs.INSTANCE.getUserId();
		if(UtilityMethods.isInternetAvailable(activity)){
			if(CartProductList.getInstance().place_order != null && CartProductList.getInstance().update_cart != null) {
				CartProductList.getInstance().place_order.setVisibility(View.GONE);
				CartProductList.getInstance().update_cart.setVisibility(View.VISIBLE);
			}
//			if(UpdateCartbg.getInstance().alDeleteId.size() > 0){
//				UpdateCartbg.getInstance().alDeleteId.add(","+CartProductList.cartList.get(position).getItem_id());  //saves deleted id for background hitting URL for edit
//			}else{
			UpdateCartbg.getInstance().alDeleteId.add(CartProductList.cartList.get(position).getItem_id());  //saves deleted id for background hitting URL for edit
//			}


			if(CartProductList.getInstance().sbDeleteProdId != null) {
				if (CartProductList.getInstance().sbDeleteProdId.length() > 0) {
					CartProductList.getInstance().sbDeleteProdId.append("," + CartProductList.cartList.get(position).getItem_id());
				} else {
					CartProductList.getInstance().sbDeleteProdId.append(CartProductList.cartList.get(position).getItem_id());
				}
			}

			ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(activity, Constants.localCloneFile);
			if(cart_products.get(position).getItem_id().equalsIgnoreCase(CartProductList.cartList.get(position).getItem_id())){  //manage clone cart when added or deleted to show update quantity on product listing and description
				UtilityMethods.deleteCloneCartItem(activity, cart_products.get(position).getItem_id());  //delete particular item from clone cart locally to update quantity on product listing and description
			}

			int quantity = Integer.parseInt(holder.tv_quantity.getText().toString());
			float price = Float.parseFloat(CartProductList.cartList.get(position).getPrice().replace(",", ""));
			float totalDeltedPrice = quantity * price;

			OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

			float sub_totalPriceYouPay = Float.parseFloat(CartProductList.getInstance().tv_subTotal.getText().toString().replace("Rs.", "")) - totalDeltedPrice;
//	        if(totalPriceYouPay >= Float.parseFloat(orderReviewBean.getShipping_ammount())){
			if(sub_totalPriceYouPay >= Float.parseFloat(CartProductList.strShippingChargeLimit)){
				CartProductList.getInstance().tv_shipping.setText("Rs.0.0");
			}else{                                 //shipping and billing charges
//				sub_totalPriceYouPay += 50;
//				CartProductList.getInstance().tv_shipping.setText("Rs.50.0");
				CartProductList.getInstance().tv_shipping.setText(orderReviewBean.getShipping_ammount());
			}

//		    CartProductList.getInstance().tv_grandTotal.setText("Rs."+String.valueOf(totalPriceYouPay));
			CartProductList.getInstance().tv_subTotal.setText("Rs."+String.valueOf(sub_totalPriceYouPay));

			int value1=Integer.parseInt(holder.tv_quantity.getText().toString());
			CartProductList.getInstance().updateHeaderQuantity(String.valueOf(value1),"minus");

			CartProductList.cartList.remove(position);
			if(CartProductList.cartList.size() > 0){
				updateList(CartProductList.cartList);         //update list after delete particular item
			}else{
				CartProductList.getInstance().finish();
				UtilityMethods.customToast(Constants.ToastConstant.VIEW_CART_EMPTY, activity);
			}

			if(String.valueOf(sub_totalPriceYouPay).equals("0") ||
					String.valueOf(sub_totalPriceYouPay).equals("0.0") ||
					String.valueOf(sub_totalPriceYouPay).equals("0.00")){
				CartProductList.getInstance().finish();
			}

		}else{
			UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, activity);
		}
	}


}


