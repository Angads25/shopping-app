package com.sakshay.grocermax;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.Product;
import com.sakshay.grocermax.bean.ProductDetail;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants.ToastConstant;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.CustomTypefaceSpan;
import com.sakshay.grocermax.utils.UtilityMethods;
//import android.widget.Toast;

public class ProductDetailScreen extends BaseActivity implements
		OnClickListener {
	private String screenName = "Product Detail";
	private TextView text_description;
//	private TextView text_product_name;
	private TextView text_mow_price;
	private TextView text_weight;
	private TextView quantity;
	private TextView add_cart;
	private ImageView product_image;
	private TextView quantity_2;

	private ImageView decrease_quantity;
	private ImageView increase_quantity;
    private TextView prod_desc_brand,prod_desc_name,prod_desc_gmorml;
	private Product product;
	private ProductDetail productDetail;
	ProgressDialog progressDialog;
	ImageView imgCancel;
	Typeface face;
	TextView addedProductCountDesc;
	String strBrand,strName,strGramorml,strPromotion;
	private RelativeLayout rlOutOfStockDesc;
	private ImageView iv_cart;
	private TextView tvCancelPrice;
	private TextView tvOffers;
	EasyTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		try {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				product = (Product) bundle.getSerializable("Product");
				strBrand = bundle.getString("BRAND");
				strName = bundle.getString("NAME");
				strGramorml = bundle.getString("GRAMSORML");
				strPromotion = bundle.getString("PROMOTION");
				productDetail = (ProductDetail) bundle.getSerializable("ProductContent");
			}

			setContentView(R.layout.product_details_screen);

			addActionsInFilter(MyReceiverActions.ADD_TO_CART);

//		text_product_name = (TextView) findViewById(R.id.text_product_name);
			product_image = (ImageView) findViewById(R.id.product_image);

			text_description = (TextView) findViewById(R.id.text_description);
			text_mow_price = (TextView) findViewById(R.id.text_mow_price);
			text_weight = (TextView) findViewById(R.id.text_weight);
			quantity = (TextView) findViewById(R.id.quantity);
			add_cart = (TextView) findViewById(R.id.add_cart);
			rlOutOfStockDesc = (RelativeLayout) findViewById(R.id.rl_out_of_stock_desc);
			addedProductCountDesc = (TextView) findViewById(R.id.added_product_count_desc);
			quantity_2 = (TextView) findViewById(R.id.quantity_2);

			tvOffers = (TextView) findViewById(R.id.tv_offers_details);

			prod_desc_brand = (TextView) findViewById(R.id.product_desc_brand);
			prod_desc_name = (TextView) findViewById(R.id.product_desc_name);
			prod_desc_gmorml = (TextView) findViewById(R.id.product_desc_gmorml);
			prod_desc_brand.setText(strBrand);
			prod_desc_name.setText(strName);
			prod_desc_gmorml.setText(strGramorml);

			iv_cart = (ImageView) findViewById(R.id.iv_cart);

			prod_desc_brand.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));
			prod_desc_name.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			prod_desc_gmorml.setTypeface(CustomFonts.getInstance().getRobotoLight(this));

			decrease_quantity = (ImageView) findViewById(R.id.decrease_quantity);
			increase_quantity = (ImageView) findViewById(R.id.increase_quantity);

			decrease_quantity.setOnClickListener(this);
			increase_quantity.setOnClickListener(this);

//		text_mow_price.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			imgCancel = (ImageView) findViewById(R.id.img_cancel);
			tvCancelPrice = (TextView) findViewById(R.id.tv_cancel_price);
			tvCancelPrice.setPaintFlags(tvCancelPrice.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);

			imgCancel.setOnClickListener(this);
			add_cart.setOnClickListener(this);

//		tvCancelPrice.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

//		face = Typeface.createFromAsset(this.getAssets(), "Rupee.ttf");
//		text_mow_price.setTypeface(face);
//		text_mow_price.setText("`"+ productDetail.getSale_price());

			if (product.getPromotionLevel() != null) {
				tvOffers.setVisibility(View.VISIBLE);
				tvOffers.setText(product.getPromotionLevel());
			} else {
				tvOffers.setVisibility(View.GONE);
			}


//		face = Typeface.createFromAsset(this.getAssets(), "Rupee.ttf");
//		tvCancelPrice.setTypeface(face);
//		tvCancelPrice.setText("`"+ productDetail.getProductPrice());

			Typeface font2 = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), "Roboto-Bold.ttf");
			Typeface font1 = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), "Rupee.ttf");
			SpannableStringBuilder SS = new SpannableStringBuilder("`" + productDetail.getProductPrice());
			SS.setSpan(new CustomTypefaceSpan("", font1), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			SS.setSpan(new CustomTypefaceSpan("", font2), 1, productDetail.getProductPrice().toString().length() - (productDetail.getProductPrice().toString().length() - 1), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        SS.setSpan (new CustomTypefaceSpan("", font2), 1, productDetail.getProductPrice().toString().length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        SS.setSpan (new CustomTypefaceSpan("", font2), 1, productDetail.getProductPrice().toString().length()+1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			tvCancelPrice.setText(SS);

			font1 = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), "Rupee.ttf");
			font2 = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), "Roboto-Bold.ttf");
			SS = new SpannableStringBuilder("`" + productDetail.getSale_price());
			SS.setSpan(new CustomTypefaceSpan("", font1), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        SS.setSpan (new CustomTypefaceSpan("", font2), 1, productDetail.getSale_price().length()-(productDetail.getSale_price().length()-1),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			SS.setSpan(new CustomTypefaceSpan("", font2), 1, productDetail.getSale_price().toString().length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			text_mow_price.setText(SS);

			font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Bold.ttf");
			font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
			SS = new SpannableStringBuilder("`"+"1");
			SS.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
			SS.setSpan(new CustomTypefaceSpan("", font2), 1, "1".length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			quantity_2.setText(SS);


//			System.out.println("==length==" + productDetail.getProductPrice().toString().length());

//		String productName = product.getName();
//		productName = productName.replaceAll("  ", " ");
//		productName = productName.replaceAll("   ", " ");
//		productName = productName.replaceAll("    ", " ");
//		String[] productNameArray = productName.split(" ");
//		int halfpart = productNameArray.length / 2;
//		String firstPart = "";
//		String secondPart = "";
//		for (int i = 0; i < halfpart; i++) {
//			if (i > 0) {
//				firstPart = firstPart + " " + productNameArray[i];
//			} else {
//				firstPart = firstPart + productNameArray[i];
//			}
//		}
//		for (int i = halfpart; i < productNameArray.length; i++) {
//			secondPart = secondPart + " " + productNameArray[i];
//		}

//		setSpanText(firstPart, secondPart, text_product_name);

			initImageLoaderM();
			ImageLoader.getInstance().displayImage(
					productDetail.getProductThumbnail(), product_image,
					baseImageoptions);

			text_weight.setText("Weight ");


			int edit_quantity = 0;
			ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(this, Constants.localCloneFile);
			if (cart_products != null && cart_products.size() > 0) {
				try {
					for (int i = 0; i < cart_products.size(); i++) {
						if (cart_products.get(i).getItem_id().equalsIgnoreCase(product.getProductid())) {
							edit_quantity = edit_quantity + cart_products.get(i).getQty();
						}
					}
				} catch (Exception e) {
				}
			}

			if (edit_quantity > 0) {
				iv_cart.setVisibility(View.VISIBLE);
				addedProductCountDesc.setVisibility(View.VISIBLE);
				addedProductCountDesc.setText(String.valueOf(edit_quantity));
			} else {
				iv_cart.setVisibility(View.INVISIBLE);
				addedProductCountDesc.setVisibility(View.INVISIBLE);
			}

//			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) addedProductCountDesc.getLayoutParams();
//			if (String.valueOf(edit_quantity).length() > 1) {
//				params.setMargins(0, 7, 12, 0);  // left, top, right, bottom
//				addedProductCountDesc.setLayoutParams(params);
//			} else if (String.valueOf(edit_quantity).length() == 1) {
//				params.setMargins(0, 7, 5, 0);  // left, top, right, bottom
//				addedProductCountDesc.setLayoutParams(params);
//			}

//		setSpanText("GrocerMax Price:", "   Rs. "+productDetail.getSale_price(),text_mow_price);
//		text_mow_price.setText("Rs. "+productDetail.getSale_price()); 

			text_description.setText(product.getName());

			if (productDetail.getStatus().equals("In stock")) {
//			increase_quantity.setImageResource(R.drawable.plus_icon);
//			decrease_quantity.setImageResource(R.drawable.minus_icon);
//			add_cart.setBackgroundResource(R.drawable.orange_border_gradient_box);
				rlOutOfStockDesc.setVisibility(View.GONE);
				decrease_quantity.setVisibility(View.VISIBLE);
				increase_quantity.setVisibility(View.VISIBLE);
				quantity.setVisibility(View.VISIBLE);
				add_cart.setVisibility(View.VISIBLE);
				add_cart.setClickable(true);
				increase_quantity.setClickable(true);
				decrease_quantity.setClickable(true);
			} else {
//			increase_quantity.setImageResource(R.drawable.plus_icon_disable);
//			decrease_quantity.setImageResource(R.drawable.minus_icon_disable);
//			add_cart.setBackgroundResource(R.drawable.gray_border_gradient_box);
				rlOutOfStockDesc.setVisibility(View.VISIBLE);
				decrease_quantity.setVisibility(View.GONE);
				increase_quantity.setVisibility(View.GONE);
				quantity.setVisibility(View.GONE);
				add_cart.setVisibility(View.GONE);
				quantity.setText("0");
				add_cart.setClickable(false);
				increase_quantity.setClickable(false);
				decrease_quantity.setClickable(false);
			}

			if (MySharedPrefs.INSTANCE.getItemQuantity() != null)
				quantity.setText(MySharedPrefs.INSTANCE.getItemQuantity());

			initHeader(findViewById(R.id.header), true, screenName);
			initFooter(findViewById(R.id.footer), 2, -1);
		}catch(Exception e){
			new GrocermaxBaseException("PayTMActivity","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onClick(View view) {
		try{
		switch (view.getId()) {
		case R.id.decrease_quantity:
			int quant = Integer.parseInt(quantity.getText().toString());
			if (quant >= 2) {
				quantity.setText("" + (quant - 1));
			}
			break;
		case R.id.increase_quantity:
			quant = Integer.parseInt(quantity.getText().toString());
			quantity.setText("" + (quant + 1));
			break;
		case R.id.img_cancel:
			finish();
			break;
		case R.id.add_cart:
//			if (MySharedPrefs.INSTANCE.getUserId() == null|| MySharedPrefs.INSTANCE.getUserId().equals("")) {
				
//				progressDialog = new ProgressDialog(ProductDetailScreen.this);
//				progressDialog.setMessage("Loading...");
//				progressDialog.show();
//				progressDialog.setCancelable(false);
				
//				final Handler handler = new Handler();
//				handler.postDelayed(new Runnable() {
//				  @Override
//				  public void run() {
					  CartDetail cart_obj = new CartDetail();
						cart_obj.setPrice(product.getPrice());
						cart_obj.setItem_id(product.getProductid());
						cart_obj.setName(product.getName());
						cart_obj.setQty(Integer.parseInt(quantity.getText()
								.toString()));
						cart_obj.setBrand(product.getBrand());
						cart_obj.setGramsORml(product.getGramsORml());
						cart_obj.setProductName(product.getProductName());
						if(MySharedPrefs.INSTANCE.getTotalItem()!=null)
						{
							MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(Integer.parseInt(MySharedPrefs.INSTANCE.getTotalItem())+Integer.parseInt(quantity.getText().toString())));
						    cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
						}
						else
						{
							MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(0+Integer.parseInt(quantity.getText().toString())));
							cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
						}
						boolean result = UtilityMethods.writeLocalCart(
								ProductDetailScreen.this, Constants.localCartFile,
								cart_obj);
						boolean result1 = UtilityMethods.writeCloneCart(ProductDetailScreen.this,Constants.localCloneFile, cart_obj);      
						
						if (result && result1)
						{
//							progressDialog.dismiss();
//							Toast.makeText(ProductDetailScreen.this,ToastConstant.PRODUCT_ADDED_CART,Toast.LENGTH_LONG).show();
							UtilityMethods.customToast(ToastConstant.PRODUCT_ADDED_CART, ProductDetailScreen.this);
						}
						
						int edit_quantity = 0;
						ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(this, Constants.localCloneFile);
						if(cart_products != null && cart_products.size() > 0)
						{
							try
							{
								for(int i=0; i<cart_products.size(); i++)
								{
									if(cart_products.get(i).getItem_id().equalsIgnoreCase(product.getProductid()))
									{
										edit_quantity  = edit_quantity+cart_products.get(i).getQty();
									}
								}
							}catch(Exception e){}
						}

						
						
						iv_cart.setVisibility(View.VISIBLE);
						addedProductCountDesc.setText(String.valueOf(edit_quantity));
						addedProductCountDesc.setVisibility(View.VISIBLE);
						
						if(ProductListFragments.tvGlobalUpdateProductList != null && ProductListFragments.imgAddedProductCount != null){
							ProductListFragments.imgAddedProductCount.setVisibility(View.VISIBLE);
							ProductListFragments.tvGlobalUpdateProductList.setVisibility(View.VISIBLE);
							ProductListFragments.tvGlobalUpdateProductList.setText(String.valueOf(edit_quantity));    //update quantity on product listing when add to cart on this page
						}


//						RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)addedProductCountDesc.getLayoutParams();
//						if(String.valueOf(edit_quantity).length() > 1){
//							params1.setMargins(0, 7, 12, 0);  // left, top, right, bottom
//							addedProductCountDesc.setLayoutParams(params1);
//						}
//						else if(String.valueOf(edit_quantity).length() == 1){
//							params1.setMargins(0, 7, 5, 0);  // left, top, right, bottom
//							addedProductCountDesc.setLayoutParams(params1);
//						}
						
//				  }
//				}, 1000);
				
//			} else {
////				showDialog();
//
//				/*String url = UrlsConstants.ADD_TO_CART_URL
//						+ MySharedPrefs.INSTANCE.getUserId();*/
//
//				JSONArray products = new JSONArray();
//				JSONObject jsonObject = new JSONObject();
//				try {
//					jsonObject.put("productid", product.getProductid() + "");
//					jsonObject.put("quantity", quantity.getText().toString());
//					products.put(jsonObject);
//					// TODO: put token
//					// 229&products=[{%22productid%22:3190,%22quantity%22:3},{%22productid%22:3191,%22quantity%22:4}]
//
//					
//					String url;
//					if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
//					{
//						url = UrlsConstants.ADD_TO_CART_URL
//								+ MySharedPrefs.INSTANCE.getUserId() +"&products="
//								+ URLEncoder.encode(products.toString(), "UTF-8");
//					}
//					else
//					{
//						url = UrlsConstants.ADD_TO_CART_URL
//								+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//								+ URLEncoder.encode(products.toString(), "UTF-8");
//					}
//					myApi.reqAddToCart(url);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
			break;
		default:
			break;
		}
		}catch(Exception e){
			new GrocermaxBaseException("ProductDetailScreen","onClick",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private void setSpanText(String fString, String sString, TextView textView) {
		Spannable word = new SpannableString(fString);
		word.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.orange_text)), 0, word.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		textView.setText(word);
		Spannable wordTwo = new SpannableString(sString);

		wordTwo.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.text_grey)), 0, wordTwo.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.append(wordTwo);
	}

	@Override
	void OnResponse(Bundle bundle) {
		try{
		String action = bundle.getString("ACTION");
		if (action.equalsIgnoreCase(MyReceiverActions.ADD_TO_CART)) {
			BaseResponseBean bean = (BaseResponseBean) bundle
					.getSerializable(ConnectionService.RESPONSE);
			if (bean.getFlag().equalsIgnoreCase("1")) {
//				Toast.makeText(mContext,ToastConstant.PRODUCT_ADDED_CART, Toast.LENGTH_LONG)
//						.show();
				UtilityMethods.customToast(ToastConstant.PRODUCT_ADDED_CART, mContext);
				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
				cart_count_txt.setText(String.valueOf(bean.getTotalItem()));
				MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
			} else if(bean.getFlag().equalsIgnoreCase("0")){
//				Toast.makeText(mContext, ""+bean.getResult(),
//						Toast.LENGTH_LONG).show();
				UtilityMethods.customToast(""+bean.getResult(), mContext);
			}else {
//				Toast.makeText(mContext,ToastConstant.ERROR_MSG,
//						Toast.LENGTH_LONG).show();
				UtilityMethods.customToast(ToastConstant.ERROR_MSG, mContext);
			}
		}
		}catch(Exception e){
			new GrocermaxBaseException("ProductDetailScreen","OnResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			initHeader(findViewById(R.id.header), true, null);
		}catch(Exception e){
			new GrocermaxBaseException("ProductDetailScreen","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	try{
	    	tracker.activityStart(this);
	    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
	    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){
			new GrocermaxBaseException("ProductDetailScreen","onStart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	try{
	    	tracker.activityStop(this);
	    	FlurryAgent.onEndSession(this);
    	}catch(Exception e){
			new GrocermaxBaseException("ProductDetailScreen","onStart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }
	
	
	
	
	
	
}