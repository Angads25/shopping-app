package com.rgretail.grocermax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.adapters.CartAdapter;
import com.rgretail.grocermax.adapters.UpdateCartListAdapter;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.CartDetailBean;
import com.rgretail.grocermax.bean.CheckoutAddressBean;
import com.rgretail.grocermax.bean.OrderReviewBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

//import com.google.analytics.tracking.android.EasyTracker;

public class CartProductList extends BaseActivity implements OnClickListener{

	ListView mList;
	public Button update_cart;
	public LinearLayout ll_place_order,button_place_order;
	public static ArrayList<CartDetail> cartList;           //managing cart items on delete and add on view cart screen locally
	public static ArrayList<CartDetail> OFS_list=new ArrayList<>();
	public static ArrayList<CartDetail> QTY_RED_list=new ArrayList<>();
	String user_id;
	CartDetailBean cartBean;
	OrderReviewBean orderReviewBean;
	CartAdapter mAdapter;
	int position = -1;
	TextView txt_subTotal,txt_shipping,txt_grand_total,txt_discount,txt_yousaved,tv_bill_buster;
	public TextView tv_subTotal,tv_discount;
	public TextView tv_grandTotal,tv_shipping;
	//	TextView tv_yousave;
	TextView tvSavePrice;
	TextView txtDiscount;
	TextView textView1;
	TextView textViewCoupon,tvCoupon;
	TextView tvYourCart;
	LinearLayout ll_coupon_apply;
	LinearLayout ll_coupon_change;
	LinearLayout ll_discount,ll_shipping,ll_coupon;
	public JSONObject jsonObjectUpdate = null;
	public StringBuilder sbDeleteProdId;
	public static String strShippingChargeLimit = "500";
	public static String loginThrough;

	public static String savingGlobal,totalGlobal,shippingGlobal;

	private boolean bIsEdit = false;   //true if user plus or minus anything in cart otherwise false and use in onDestroy.

	String strUserIdtemp,strQuoteIdtemp;
	private String SCREENNAME = "CartProductList-";

	public static CartProductList instance;
	public static CartProductList getInstance(){
		if(instance == null){
			instance = new CartProductList();
		}
		return instance;
	}
	public CartProductList(){
		instance = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categoty_list);

		try{
			AppsFlyerLib.setCurrencyCode("INR");
			AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
			AppsFlyerLib.sendTracking(getApplicationContext());
		}catch(Exception e){}

		try {
			user_id = MySharedPrefs.INSTANCE.getUserId();
			sbDeleteProdId = new StringBuilder();

			UpdateCartbg.getInstance().alDeleteId = new ArrayList<String>();

			addActionsInFilter(MyReceiverActions.DELETE_FROM_CART);
			addActionsInFilter(MyReceiverActions.CHECKOUT_ADDRESS);
			addActionsInFilter(MyReceiverActions.CART_DETAIL_AFTER_DELETE);
			addActionsInFilter(MyReceiverActions.CART_DETAIL_AFTER_LOGIN);
			addActionsInFilter(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN);

			addActionsInFilter(MyReceiverActions.VIEW_CART_ERROR_ON_CART);                          //uses when on local update SLIM APPLICATION ERROR comes then call VIEWCART just to update cart


			addActionsInFilter(MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);

			tv_subTotal = (TextView) findViewById(R.id.tv_subTotal);
			tv_discount = (TextView) findViewById(R.id.tv_discount);
			tvCoupon = (TextView) findViewById(R.id.tv_coupon_apply);
			tvSavePrice = (TextView) findViewById(R.id.tv_save_price);
			txtDiscount = (TextView) findViewById(R.id.txt_discount);
			tv_grandTotal = (TextView) findViewById(R.id.tv_grandTotal);
			tv_shipping = (TextView) findViewById(R.id.tv_shipping);
			tvYourCart = (TextView) findViewById(R.id.tv_your_cart);
            tv_bill_buster=(TextView)findViewById(R.id.tv_bill_buster);
			ll_discount = (LinearLayout) findViewById(R.id.ll_discount);
			ll_shipping = (LinearLayout) findViewById(R.id.ll_shipping);
			ll_coupon = (LinearLayout) findViewById(R.id.ll_coupon);
			ll_coupon_apply=(LinearLayout)findViewById(R.id.ll_apply_coupon);
			ll_coupon_change=(LinearLayout)findViewById(R.id.ll_change_coupon);

			txt_subTotal = (TextView) findViewById(R.id.txt_subtotal);
			txt_shipping = (TextView) findViewById(R.id.txt_shipping);
			txt_yousaved = (TextView) findViewById(R.id.txt_yousaved);
			txt_grand_total = (TextView) findViewById(R.id.txt_grand_total);
			txt_discount = (TextView) findViewById(R.id.txt_discount);
			textView1 = (TextView) findViewById(R.id.textView1);
			textViewCoupon = (TextView) findViewById(R.id.textViewcoupon);


			txt_subTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txt_shipping.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txt_grand_total.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txt_discount.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			textView1.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			textViewCoupon.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			tvSavePrice.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			tvCartItemCount.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			tvCartTotalTop.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			txtItems.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			txtTotal.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

			tv_subTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			tv_discount.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			tvCoupon.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			tv_grandTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			tv_shipping.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txtDiscount.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			tvYourCart.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				cartList = bundle.getParcelableArrayList("cartList");
				cartBean = (CartDetailBean) bundle.getSerializable("cartBean");

				String Sku_All_Product="";
				for(int i=0;i<cartBean.getItems().size();i++)
				{
					Sku_All_Product=","+cartBean.getItems().get(i).getSku();
				}
							/*QGraph event*/
				JSONObject json=new JSONObject();
				json.put("Total Qty",String.valueOf(MySharedPrefs.INSTANCE.getTotalItem()));
				json.put("Subtotal",String.format("%.2f",Float.parseFloat(cartBean.getGrandTotal().replace(",",""))));
				if(cartBean.getCoupon_code()!=null)
					json.put("Coupon Code",cartBean.getCoupon_code());
				json.put("Sku Id",Sku_All_Product);
				if(MySharedPrefs.INSTANCE.getUserId()!=null)
					json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
				UtilityMethods.setQGraphevent("Andriod View Cart",json);
                          /*--------------*/





				initHeader(findViewById(R.id.header), true, "Your Basket");
				setCartList(cartBean);
				setAppliedCoupon(cartBean);
				initFooter(findViewById(R.id.footer), 1, -1);
			}
			icon_header_cart.setClickable(false);
			cart_count_txt.setClickable(false);

			tvCoupon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String userId = MySharedPrefs.INSTANCE.getUserId();
					if (userId == null || userId.length() == 0) {
						Intent intent = new Intent(mContext, LoginActivity.class);
						intent.putExtra("requestCode", AppConstants.LOGIN_REQUEST_CODE);
						loginThrough="coupon_page";
						startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
					} else {
						Intent i=new Intent(CartProductList.this,CouponPage.class);
						startActivity(i);
					}
				}
			});


		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}
	public void setAppliedCoupon(CartDetailBean cartBean){
		try {
			if(cartBean!=null){
                if(cartBean.getCoupon_code()==null){
                    ll_coupon_apply.setVisibility(View.VISIBLE);
                    ll_coupon_change.setVisibility(View.GONE);
                    MySharedPrefs.INSTANCE.putCouponCode("");
                }else{
                    ll_coupon_apply.setVisibility(View.GONE);
                    ll_coupon_change.setVisibility(View.VISIBLE);
                    TextView tv_couponApplied=(TextView)findViewById(R.id.tv_coupon_applied);
                    TextView tv_coupon_change=(TextView)findViewById(R.id.tv_coupon_change);
                    TextView tv_coupon_desc=(TextView)findViewById(R.id.tv_coupon_detail);
                    tv_couponApplied.setText("Coupon Applied - "+cartBean.getCoupon_code());
                    tv_coupon_desc.setText(cartBean.getCoupon_desc());
                    MySharedPrefs.INSTANCE.putCouponCode(cartBean.getCoupon_code());
                    MySharedPrefs.INSTANCE.putCouponAmount(String.valueOf(Float.parseFloat(cartBean.getSubTotal())-Float.parseFloat(cartBean.getSubtotal_with_discount())));
                    tv_coupon_change.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(CartProductList.this,CouponPage.class);
                            startActivity(i);
                        }
                    });
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void setCartList(CartDetailBean cartBean)
	{
		try {

			if (cartList != null && cartList.size() > 0) {
				//ll_total.setVisibility(View.VISIBLE);
				orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
				this.cartBean=cartBean;

				if (cartBean != null) {
					float saving = 0;

					int totalcount = 0;
					for (int i = 0; i < cartList.size(); i++) {
						saving = saving + (cartList.get(i).getQty() * (Float.parseFloat(cartList.get(i).getMrp()) - Float.parseFloat(cartList.get(i).getPrice())));
						totalcount += cartBean.getItems().get(i).getQty();
					}




					System.out.println(totalcount+"==size is========="+cartList.size());
					try {
//						cart_count_txt.setText(String.valueOf(totalcount));
//						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(totalcount));
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(totalcount));
						cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					} catch (Exception e) {
					}

					//float discount=Float.parseFloat(cartBean.getSubTotal())-Float.parseFloat(cartBean.getGrandTotal());
					saving = saving - (Float.parseFloat(orderReviewBean.getDiscount_amount()));
//		    tv_yousave.setText("Rs."+String.format("%.2f",saving));
					tv_subTotal.setText(getResources().getString(R.string.rs)+""+ String.format("%.2f", Float.parseFloat(cartBean.getSubTotal())));
					tv_discount.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(orderReviewBean.getDiscount_amount())));
					tvSavePrice.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", saving));
					savingGlobal=String.valueOf(saving);
			/*if(Float.parseFloat(orderReviewBean.getDiscount_amount())==0)
			{
				ll_discount.setVisibility(View.GONE);
			}
			else
				ll_discount.setVisibility(View.VISIBLE);*/
//					tv_shipping.setText("Rs." + Float.parseFloat(orderReviewBean.getShipping_ammount()));
					tv_shipping.setText(getResources().getString(R.string.rs)+""+String.format("%.2f", Float.parseFloat(orderReviewBean.getShipping_ammount())));
					shippingGlobal=orderReviewBean.getShipping_ammount();


                    /*set data on view for bill buster*/
                    if(cartBean.getBill_buster()!=null) {
                        if (cartBean.getBill_buster().equals("")) {
                            tv_bill_buster.setVisibility(View.GONE);
                        } else {
                            tv_bill_buster.setText(cartBean.getBill_buster());
                            tv_bill_buster.setVisibility(View.VISIBLE);
                        }
                    }
                    /*-----------------*/


			if(Float.parseFloat(orderReviewBean.getShipping_ammount())==0)
			{
				ll_shipping.setVisibility(View.VISIBLE);
				tv_shipping.setText("Free");
			}
			else{
				ll_shipping.setVisibility(View.VISIBLE);
			}

					//tv_discount.setText("-"+String.format("%.2f",discount));
					tv_grandTotal.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(cartBean.getGrandTotal())));
					totalGlobal=cartBean.getGrandTotal();

					OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
					orderReviewBean.setProduct(cartList);
					orderReviewBean.setSaving(String.valueOf(saving));                      //think will use when Order and review Pay for displaying order data
					MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);

					///////////////// used b/c when user deleted or edit products from cart and sign out then below id's becomes empty ////////////////
					if (MySharedPrefs.INSTANCE.getUserId() != null && !MySharedPrefs.INSTANCE.getUserId().equals("")) {
						strUserIdtemp = MySharedPrefs.INSTANCE.getUserId();
					}
					if (MySharedPrefs.INSTANCE.getQuoteId() != null && !MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
						strQuoteIdtemp = MySharedPrefs.INSTANCE.getQuoteId();
					}
					///////////////// used b/c when user deleted or edit products from cart and sign out then below id's becomes empty ////////////////


				} else{
					//ll_total.setVisibility(View.GONE);
					}


				mList = (ListView) findViewById(R.id.category_list);
				button_place_order = (LinearLayout) findViewById(R.id.button_place_order);
				ll_place_order = (LinearLayout) findViewById(R.id.ll_place_order);



				update_cart = (Button) findViewById(R.id.button_update_cart1);
				update_cart.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

				mAdapter = new CartAdapter(CartProductList.this);
				mList.setAdapter(mAdapter);

				mList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						//				showDialog();
					}
				});
				boolean boolupdate = false;
				for (int i = 0; i < cartList.size(); i++) {
					try {
						if (cartBean.getItems().get(i).getStatus().equals("0")) {
							boolupdate = true;
							CartProductList.getInstance().update_cart.setBackgroundColor(activity.getResources().getColor(R.color.updateshade));
						}
					}catch(Exception e){}
				}
				if(boolupdate){
					update_cart.setVisibility(View.VISIBLE);
					update_cart.setOnClickListener(this);
					ll_place_order.setVisibility(View.GONE);
					button_place_order.setOnClickListener(this);
					icon_header_cart.setClickable(false);
					cart_count_txt.setClickable(false);
				}else{
					ll_place_order.setVisibility(View.VISIBLE);
					button_place_order.setOnClickListener(this);
					update_cart.setVisibility(View.GONE);
					update_cart.setOnClickListener(this);
				}

				}else{
					UtilityMethods.customToast(Constants.ToastConstant.CART_EMPTY, mContext);
				//ll_total.setVisibility(View.GONE);
				Intent intent = new Intent(mContext, HomeScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();

//				UtilityMethods.customToast(ToastConstant.CART_EMPTY, mContext);
//				ll_total.setVisibility(View.GONE);
//				Intent intent = new Intent(CartProductList.this, HomeScreen.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				finish();
			}
			icon_header_cart.setClickable(false);
			cart_count_txt.setClickable(false);
		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "setCartList", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "setCartList", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}
	AlertDialog alert_update;
	TextView tv_popup_update;
	public static ArrayList<CartDetail> completeList=new ArrayList<>();
	public void showUpdateCartPopup(){
		completeList.clear();
		if(OFS_list.size()>0)
		{
            CartDetail cartDetail=new CartDetail();
			cartDetail.setQty(0);
			cartDetail.setFlag("2");
			if(OFS_list.size()>1)
			cartDetail.setName("Please remove items to proceed.");
			else
			cartDetail.setName("Please remove item to proceed.");
			completeList.add(cartDetail);
		}
		for(int i=0;i<OFS_list.size();i++){
			OFS_list.get(i).setNumber(""+(i+1));
		    completeList.add(OFS_list.get(i));
		}
		if(QTY_RED_list.size()>0)
		{
			CartDetail cartDetail=new CartDetail();
			cartDetail.setQty(0);
			cartDetail.setFlag("2");
			cartDetail.setName("Please reduce quantity to proceed.");
			completeList.add(cartDetail);
		}
		for(int i=0;i<QTY_RED_list.size();i++){
			QTY_RED_list.get(i).setNumber(""+(OFS_list.size()+i+1));
			completeList.add(QTY_RED_list.get(i));
		}

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartProductList.this);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.update_cart_popup, null);
		alertDialog.setView(dialogView);
		alertDialog.setCancelable(true);
		alert_update = alertDialog.create();
		ListView lv_update_list=(ListView) dialogView.findViewById(R.id.lv_update_cart);
		tv_popup_update=(TextView)dialogView.findViewById(R.id.tv_popup_update);
		changeUpdateButtonInPopup();
		lv_update_list.setAdapter(new UpdateCartListAdapter(CartProductList.this,lv_update_list));
		alert_update.setCanceledOnTouchOutside(false);
		alert_update.show();

		alert_update.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onBackPressed();
			}
		});

	}

	public void changeUpdateButtonInPopup(){
		boolean status=true;
		for(int i=0;i<completeList.size();i++){
			if(completeList.get(i).getFlag()==null || !(completeList.get(i).getFlag().equals("2") || completeList.get(i).getFlag().equals("3"))){
				status=false;
				break;
			}
		}
		if(status){
			tv_popup_update.setBackgroundColor(Color.parseColor("#f57c00"));
			tv_popup_update.setOnClickListener(CartProductList.this);
		}
		else
			tv_popup_update.setBackgroundColor(getResources().getColor(R.color.gray_1));


		//tv_popup_update.setBackgroundColor(Color.parseColor("#f57c00"));
		//tv_popup_update.setOnClickListener(CartProductList.this);
	}

	public void deleteItem(int position)
	{
		try{
		String userId = MySharedPrefs.INSTANCE.getUserId();
		if(userId!=null && userId.trim().length()>0)
		{
			showDialog();
			String url = UrlsConstants.DELETE_FROM_CART_URL + user_id + "&productid=" + cartList.get(position).getItem_id()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
//			String url = UrlsConstants.DELETE_FROM_CART_URL + user_id + "&productid=" + cartList.get(0).getItem_id() +","+cartList.get(1).getItem_id()  +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
			myApi.reqDeleteFromCart(url);
			this.position = position;
		}
		else
		{
			showDialog();
			String url = UrlsConstants.DELETE_FROM_CART_URL + user_id + "&productid=" + cartList.get(position).getItem_id()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
//			String url = UrlsConstants.DELETE_FROM_CART_URL + user_id + "&productid=" + cartList.get(0).getItem_id() +","+cartList.get(1).getItem_id()  +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
			myApi.reqDeleteFromCart(url);
			this.position = position;
			/*boolean delete = UtilityMethods.deleteCartItem(this, cartList.get(position).getItem_id());
			if(delete)
			{
				cartList = UtilityMethods.readLocalCart(this, AppConstants.localCartFile);
				updateCart();
				Toast.makeText(this,ToastConstant.PRODUCT_DELETED, Toast.LENGTH_LONG).show();
			}*/
		}

		/*showDialog();
		String url = UrlsConstants.DELETE_FROM_CART_URL + user_id + "&productid=" + cartList.get(position).getProductId();
		myApi.reqDeleteFromCart(url);
		this.position = position;*/
		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "deleteItem", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "deleteItem", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	public void changeQuantity(String item_id,int qty)
	{
		try{
		if(qty>0)
		{
				JSONArray products = new JSONArray();
				JSONObject prod_obj = new JSONObject();
				prod_obj.put("productid", item_id);
				prod_obj.put("quantity", qty);
				products.put(prod_obj);

				showDialog();
				String	url = UrlsConstants.UPDATE_CART_URL
						+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
						+ URLEncoder.encode(products.toString(), "UTF-8");
				myApi.reqViewCartAfterDelete(url,MyReceiverActions.CART_DETAIL_AFTER_DELETE);
		}
		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "changeQuantity", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "changeQuantity", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void updateCart()
	{
		try{
		AppConstants.cart_count = cartList.size();
		mAdapter.updateList(cartList);
		if(cartList.size() == 0)
		{
			ll_place_order.setVisibility(View.GONE);
		}
		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "updateCart", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "updateCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	public void OnResponse(Bundle bundle) {
		try {
			String action = bundle.getString("ACTION");
			if (action.equals(MyReceiverActions.DELETE_FROM_CART)) {
				BaseResponseBean responseBean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (responseBean.getFlag().equalsIgnoreCase("1")) {
					//cartList.remove(position);
					//updateCart();
					showDialog();
					String url = UrlsConstants.VIEW_CART_URL
							+ MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
					myApi.reqViewCartAfterDelete(url, MyReceiverActions.CART_DETAIL_AFTER_DELETE);
				} else
					position = -1;

			} else if (action.equals(MyReceiverActions.CHECKOUT_ADDRESS)) {

					CheckoutAddressBean bean = (CheckoutAddressBean) bundle.getSerializable(ConnectionService.RESPONSE);
			/*if(bean.getAddress().size()>0)
			{*/
					Intent intent = new Intent(CartProductList.this, ShippingAddress.class);
//					Intent intent = new Intent(CartProductList.this, BillingAddress.class);
//					Intent intent = new Intent(CartProductList.this, ChooseAddress.class);
//					Intent intent = new Intent(CartProductList.this, DeliveryDetails.class);
					intent.putExtra("addressBean", bean);
					startActivity(intent);
			/*}else{
				Toast.makeText(CartProductList.this,ToastConstant.NO_ACCOUNT_ADDR,0).show();
			}*/


			} else if (action.equals(MyReceiverActions.CART_DETAIL_AFTER_DELETE)) {
				dismissDialog();
				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int) Float.parseFloat(cartBean.getItems_qty())));
					cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
				}
				cartList.clear();
				cartList = cartBean.getItems();
				setCartList(cartBean);
				checkConditionToShowUpdatePopup();
				boolean bSingleItemFlag = false;    //it will use when only 1 item in cart and deleted(b/c in that case it will not compare using loops as cartList will empty coming from server)
				boolean bFlag = false;             //it will call when all items of clone cart compare in j loop and is on last index and not found with same id in cartList (means this id not found and should be deleted from local cart)

				ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(this, Constants.localCloneFile);
				if (cart_products != null && cart_products.size() > 0 && cartList != null && cartList.size() > 0) {

						for (int i = 0; i < cart_products.size(); i++) {
							bSingleItemFlag = true;
							bFlag = false;
							for (int j = 0; j < cartList.size(); j++) {           //cart coming from server
								if (cart_products.get(i).getItem_id().equalsIgnoreCase(cartList.get(j).getItem_id())) {  //manage clone cart when added or deleted to show update quantity on product listing and description

									bFlag = true;

									cart_products.get(i).setQty(cartList.get(j).getQty());  //plus OR minus

									CartDetail cart_obj = new CartDetail();
									cart_obj.setPrice(cart_products.get(i).getPrice());
									cart_obj.setFlag(cart_products.get(i).getFlag());
									cart_obj.setItem_id(cart_products.get(i).getItem_id());
									cart_obj.setName(cart_products.get(i).getName());
									cart_obj.setPrice(cart_products.get(i).getPrice());
									cart_obj.setItem_id(cart_products.get(i).getItem_id());
									cart_obj.setProduct_thumbnail(cart_products.get(i).getProduct_thumbnail());
									cart_obj.setQty(cartList.get(j).getQty());
									cart_obj.setQuoteId(cart_products.get(i).getQuoteId());
									cart_obj.setResult(cart_products.get(i).getResult());
									cart_obj.setSku(cart_products.get(i).getSku());
									cart_obj.setTotalItem(cart_products.get(i).getTotalItem());

									UtilityMethods.deleteCloneCartItem(this, cart_products.get(i).getItem_id());
									UtilityMethods.writeCloneCart(this, Constants.localCloneFile, cart_obj);
								} else if (!bFlag && (j == cartList.size() - 1)) {  //false means not enter in j loop AND (j== size-1) mean last index of j loop i.e particular item deleted
									UtilityMethods.deleteCloneCartItem(this, cart_products.get(i).getItem_id());  //delete particular item from clone cart locally to update quantity on product listing and description
								}
							}
						}

				} else {
					if (cart_products.size() == 1) {
						UtilityMethods.deleteCloneCartItem(this, cart_products.get(0).getItem_id());  //when there is single item in local clone file and not going in loop so delete there.
					}
				}
			} else if (action.equals(MyReceiverActions.CART_DETAIL_AFTER_LOGIN)) {
				dismissDialog();

				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
				UtilityMethods.deleteCloneCart(this);
				for (int i = 0; i < cartBean.getItems().size(); i++) {
					UtilityMethods.writeCloneCart(this, Constants.localCloneFile, cartBean.getItems().get(i));
				}
				if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int) Float.parseFloat(cartBean.getItems_qty())));
					cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
				}
				cartList.clear();
				cartList = cartBean.getItems();
				setCartList(cartBean);

				callAddressApi();                        //commented on 4/8/15
			} else if (action.equals(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN)) {
				dismissDialog();
				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
			} else if (action.equals(MyReceiverActions.VIEW_CART_UPDATE_LOCALLY)) {
				dismissDialog();

				sbDeleteProdId = new StringBuilder();                                //b/c whenever come there means deleteid already contained should be removed.

				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if(cartBean != null) {                                                         //uses null condition b/c sometimes result coming from server SLIM APPLICATION ERROR
					UtilityMethods.deleteCloneCart(this);
					for (int i = 0; i < cartBean.getItems().size(); i++) {
						UtilityMethods.writeCloneCart(this, Constants.localCloneFile, cartBean.getItems().get(i));
					}

					if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int) Float.parseFloat(cartBean.getItems_qty())));
						cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					}
					cartList.clear();
					cartList = cartBean.getItems();

					setCartList(cartBean);


					/*QGraph event*/
					String Sku_All_Product="";
					for(int i=0;i<cartBean.getItems().size();i++)
					{
						Sku_All_Product=","+cartBean.getItems().get(i).getSku();
					}
					JSONObject json=new JSONObject();
					json.put("Total Qty",String.valueOf(MySharedPrefs.INSTANCE.getTotalItem()));
					json.put("Subtotal",String.format("%.2f",Float.parseFloat(cartBean.getGrandTotal().replace(",",""))));
					if(cartBean.getCoupon_code()!=null)
						json.put("Coupon Code",cartBean.getCoupon_code());
					json.put("Sku Id",Sku_All_Product);
					if(MySharedPrefs.INSTANCE.getUserId()!=null)
						json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
					UtilityMethods.setQGraphevent("Andriod Update Cart",json);
                          /*--------------*/

					UtilityMethods.sendGTMEvent(CartProductList.this,""+Sku_All_Product,String.valueOf(MySharedPrefs.INSTANCE.getTotalItem()),"Android Update Cart");

					checkConditionToShowUpdatePopup();
				}else{                                                           //in case when update service got fail view cart will run [just for precaution].
					showDialog();
					String url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
					myApi.reqViewCartSlipErrorApp(url);
				}

				icon_header_cart.setClickable(false);
				cart_count_txt.setClickable(false);

//			   }else{
//				   finish();
//			   }
			}
			else if (action.equals(
					MyReceiverActions.VIEW_CART_ERROR_ON_CART)) {
				dismissDialog();
				cart_count_txt.setText(String.valueOf(MySharedPrefs.INSTANCE.getTotalItem()));
				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if(cartBean.getItems().size()>0)
				{
					UtilityMethods.deleteLocalCart(CartProductList.this);                   //new 1/9/2015
//						UtilityMethods.deleteCloneCart(BaseActivity.this);
					for(int i=0;i<cartBean.getItems().size();i++)
					{
						UtilityMethods.writeCloneCart(CartProductList.this, Constants.localCloneFile, cartBean.getItems().get(i));
					}
					if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
						//				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int) Float.parseFloat(cartBean.getItems_qty())));
						cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					}
					cartList.clear();
					cartList = cartBean.getItems();
					setCartList(cartBean);
					checkConditionToShowUpdatePopup();

//					bIsEdit = false;                //used b/c finish call and then onDestroy call and update will call but when user hits viewcart then no need of calling update service from onDestroy()
//					sbDeleteProdId = null;          //used b/c finish call and then onDestroy call and update will call but when user hits viewcart then no need of calling update service from onDestroy()
//					finish();
//					Intent i = new Intent(mContext, CartProductList.class);
//					Bundle bundle_cart = new Bundle();
//					bundle_cart.putParcelableArrayList("cartList", cartBean.getItems());
//					bundle_cart.putSerializable("cartBean", cartBean);
//					i.putExtras(bundle_cart);
//					startActivity(i);
				}
				else
				{
					UtilityMethods.customToast(AppConstants.ToastConstant.CART_EMPTY, mContext);
				}
			}

		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "OnResponse", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "OnResponse", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}

	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {

				case R.id.button_place_order:

					if (CartProductList.cartList.size() == 0) {
						UtilityMethods.customToast(Constants.ToastConstant.ATLEAST_ONE_ITEM_IN_CART, mContext);
						return;
					}

                    /* tracking the event for proceed to checkout*/
                    try{
                        UtilityMethods.clickCapture(activity,"Proceed to Checkout","","","",MySharedPrefs.INSTANCE.getSelectedCity());

						UtilityMethods.sendGTMEvent(activity,"Proceed Details","totalQty="+MySharedPrefs.INSTANCE.getTotalItem()+"/order_amount="+String.format("%.2f", Float.parseFloat(orderReviewBean.getGrandTotal())),"Android Proceed to Checkout");

						RocqAnalytics.trackEvent("Proceed to Checkout", new ActionProperties("Category", "Proceed to Checkout", "Action", MySharedPrefs.INSTANCE.getSelectedCity()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    /*-----------------------------------------*/


					String userId = MySharedPrefs.INSTANCE.getUserId();
					if (userId == null || userId.length() == 0) {
						Intent intent = new Intent(mContext, LoginActivity.class);
						intent.putExtra("requestCode", AppConstants.LOGIN_REQUEST_CODE);
						loginThrough="view_cart";
						startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
					} else {

						String Sku_All_Product="";
						for(int i=0;i<cartBean.getItems().size();i++)
						{
							Sku_All_Product=","+cartBean.getItems().get(i).getSku();
						}
							/*QGraph event*/
						JSONObject json=new JSONObject();
						json.put("Total Qty",String.valueOf(MySharedPrefs.INSTANCE.getTotalItem()));
						json.put("Subtotal",String.format("%.2f",Float.parseFloat(cartBean.getGrandTotal().replace(",",""))));
						if(cartBean.getCoupon_code()!=null)
							json.put("Coupon Code",cartBean.getCoupon_code());
						json.put("Sku Id",Sku_All_Product);
						if(MySharedPrefs.INSTANCE.getUserId()!=null)
							json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
						UtilityMethods.setQGraphevent("Andriod Proceed to checkout",json);
                          /*--------------*/
						callAddressApi();
					}

					break;

				case R.id.button_update_cart1:
					updateItemInCartBackToCart("cart");
					bIsEdit = false;
					//try{UtilityMethods.clickCapture(mContext,"","","","",SCREENNAME+AppConstants.BOTTOM_CART_UPDATE_BUTTON_PRESSED);}catch(Exception e){}
					break;

				case R.id.tv_popup_update:
					alert_update.dismiss();
					updateItemInCartBackToCart("popup");
					bIsEdit = false;
					//try{UtilityMethods.clickCapture(mContext,"","","","",SCREENNAME+AppConstants.BOTTOM_CART_UPDATE_BUTTON_PRESSED);}catch(Exception e){}
					break;

			}
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "onClick", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}


//	public void updateItemInCart()
//	{
//		try {
//			JSONArray productsUpdate = new JSONArray();
//			for(int i=0;i<cartList.size();i++)
////			for(int i=0;i<2;i++)
//			{
//			if(Float.parseFloat(cartList.get(i).getPrice())!=0)
//				{
//					JSONObject prod_obj = new JSONObject();
//					prod_obj.put("productid", cartList.get(i).getItem_id());
//					prod_obj.put("quantity", cartList.get(i).getQty());
//					System.out.println(cartList.get(i).getQty()+"==qty===item id=="+cartList.get(i).getItem_id());
//					productsUpdate.put(prod_obj);
//				}
//			}
//
//			System.out.println("====PRODUCTS===="+productsUpdate);
////			HashMap<String, JSONArray> valuePairs = new HashMap<String, JSONArray>();
//
//			JSONArray productsDelete = new JSONArray();
//			for(int j=0;j<UpdateCartbg.getInstance().alDeleteId.size();j++)
//			{
//				JSONObject prod_obj = new JSONObject();
//				prod_obj.put("prod", cartList.get(j).toString());
//				productsDelete.put(prod_obj);
//			}
//
////			valuePairs.put("delete_id", productsDelete);
////			valuePairs.put("update_id", productsUpdate);
////			jsonArray = new JSONArray();
////			jsonArray.put(valuePairs);
//
//			jsonObjectUpdate = new JSONObject();
//			jsonObjectUpdate.put("update_id", productsUpdate);
//			jsonObjectUpdate.put("delete_id", productsDelete);
//			System.out.println("==Json object value is=="+jsonObjectUpdate);
////			showDialog();
//
////			http://uat.grocermax.com/webservice/new_services  /deleteitem?userid=+ user_id + "&productid=" + productid +
////				"&quote_id="+ quoteid &"&updateid="+ URLEncoder.encode(products.toString(), "UTF-8");
//
//
////			String url = UrlsConstants.NEW_BASE_URL+"deleteitem?userid="+MySharedPrefs.INSTANCE.getUserId()+
////					"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
//
////			System.out.println("==URL'S HERE=="+url);
////			if(UtilityMethods.isInternetAvailable(this)){
////				UpdateCartbg.getInstance().bLocally = true;
//////				myApi.reqEditCart(url,MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
////				myApi.reqEditCart(url,jsonObjectUpdate);
////			}
////			String	url = UrlsConstants.UPDATE_CART_URL
////						+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
////						+ URLEncoder.encode(products.toString(), "UTF-8");
////			myApi.reqViewCartAfterDelete(url,MyReceiverActions.CART_DETAIL_AFTER_DELETE);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void updateItemInCart(){
		try {
//			JSONArray products = new JSONArray();
//			for(int i=0;i<cartList.size();i++)
//			{
//				if(Float.parseFloat(cartList.get(i).getPrice())!=0)
//				{
//					JSONObject prod_obj = new JSONObject();
//					prod_obj.put("productid", cartList.get(i).getItem_id());
//					prod_obj.put("quantity", cartList.get(i).getQty());
//					System.out.println(cartList.get(i).getQty()+"==qty===item id=="+cartList.get(i).getItem_id());
//					products.put(prod_obj);
//				}
//			}

//			String url = UrlsConstants.NEW_BASE_URL+"deleteitem?userid="+ strUserIdtemp +
//					"&productid=" + sbDeleteProdId +
//					"&quote_id="+ strQuoteIdtemp +"&updateid="+ URLEncoder.encode(products.toString(), "UTF-8");
//			System.out.println("==URL'S HERE=="+url);
//			if(UtilityMethods.isInternetAvailable(this)){
//				UpdateCartbg.getInstance().bLocally = true;
//				myApi.reqEditCart(url,MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
//				myApi.reqEditCart(url);
//			}


			try {
				////////////////POST/////////////
				String strurl = UrlsConstants.NEW_BASE_URL+"deleteitem";
				JSONObject jsonObject = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				if(sbDeleteProdId.length() > 0) {
					String sd[] = sbDeleteProdId.toString().split(",");
					for (int k = 0; k < sd.length; k++) {
						String s1 = sd[k];
						jsonArray.put(Integer.parseInt(s1));
					}
				}
//				if(sbDeleteProdId.length() > 0) {
//					String sd[] = sbDeleteProdId.toString().split(",");
//					for (int k = 0; k < sd.length; k++) {
//						String s1 = sd[k];
//						if(CartAdapter.alOutOfStockId.size() > 0) {
////							boolean boutofstock = false;
////							for(int i=0;i<CartAdapter.alOutOfStockId.size();i++){
////
////							}
//						    Iterator<String> iterator = CartAdapter.alOutOfStockId.iterator();
//     						while (iterator.hasNext()) {
//								for (Iterator<String> iteratorvalue = CartAdapter.alOutOfStockId.iterator(); iterator.hasNext(); ) {
//									String string = iteratorvalue.next();
//									if (string.isEmpty()) {
//										// Remove the current element from the iterator and the list.
//										iteratorvalue.remove();
//										jsonArray.put(Integer.parseInt(s1));
//										System.out.println("=======Inner Loop========"+s1);
//									}
//								}
//							}
//						}else {
//							jsonArray.put(Integer.parseInt(s1));
//						}
//
//						if(CartAdapter.alOutOfStockId.size() > 0) {
//							for(int i=0;i<CartAdapter.alOutOfStockId.size();i++){
//								jsonArray.put(Integer.parseInt(CartAdapter.alOutOfStockId.get(i).toString()));
//								System.out.println("=======Outer Loop========" + s1);
//							}
//						}
//
//					}
//				}
//				System.out.println("============JsonaRRAY value is============="+jsonArray);


				jsonObject.put("userid", strUserIdtemp);
				jsonObject.put("productid", jsonArray);
				jsonObject.put("quote_id", strQuoteIdtemp);
				JSONArray products1 = new JSONArray();
				for(int i=0;i<cartList.size();i++)
				{
					if(Float.parseFloat(cartList.get(i).getPrice())!=0)
					{
						if(cartList.get(i).getStatus().equals("1")) {                                //products available
							JSONObject prod_obj = new JSONObject();
							prod_obj.put("productid", cartList.get(i).getItem_id());
							prod_obj.put("quantity", cartList.get(i).getQty());
							System.out.println(cartList.get(i).getQty() + "==qty===item id==" + cartList.get(i).getItem_id());
							products1.put(prod_obj);
						}
					}
				}
				jsonObject.put("updateid", products1);

//				jsonObject.put("version", "1.0");

//				System.out.println("==delete and back to previous screen==" + strurl);
//				System.out.println("==delete and back json==" + jsonObject);
				if (UtilityMethods.isInternetAvailable(this)) {
					UpdateCartbg.getInstance().bLocally = true;
//				myApi.reqEditCart(url,MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
				myApi.reqEditCart(strurl, jsonObject);
				}
			}catch(Exception e){}

			////////////////POST/////////////

			try{
			////////////////POST/////////////
//				String strurl = UrlsConstants.NEW_BASE_URL;
//				JSONObject jsonObject = new JSONObject();
//				jsonObject.put("deleteitem?userid=",strUserIdtemp);
//				jsonObject.put("productid",String.valueOf(sbDeleteProdId));
//				jsonObject.put("quote_id",strQuoteIdtemp);
//				jsonObject.put("updateid",products.toString());
//				System.out.println("==URL'S HERE=="+strurl);
//				if(UtilityMethods.isInternetAvailable(this)) {
//					UpdateCartbg.getInstance().bLocally = true;
//					myApi.reqEditCart(url, jsonObject);
//				}
			}catch(Exception e){}
//			}
			////////////////POST/////////////

		} catch (Exception e) {
			new GrocermaxBaseException("CartProductList", "updateItemInCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void updateItemInCartBackToCart(String from)
	{
		try {
            /*-----track event for update cart-------*/
            try{
                UtilityMethods.clickCapture(activity,"Update Cart","","","",MySharedPrefs.INSTANCE.getSelectedCity());
				RocqAnalytics.trackEvent("Update Cart", new ActionProperties("Category", "Update Cart", "Action", MySharedPrefs.INSTANCE.getSelectedCity()));
            }catch(Exception e){
                e.printStackTrace();
            }
            /*----------------------*/


			showDialog();
			String strUserId = "";
			if(MySharedPrefs.INSTANCE.getUserId() != null && !MySharedPrefs.INSTANCE.getUserId().equals(""))
			{
				strUserId = MySharedPrefs.INSTANCE.getUserId();
			}

			String strQuoteId = "";
			if(MySharedPrefs.INSTANCE.getQuoteId() != null && !MySharedPrefs.INSTANCE.getQuoteId().equals(""))
			{
				strQuoteId = MySharedPrefs.INSTANCE.getQuoteId();
			}

			try {
				////////////////POST/////////////
				String strurl = UrlsConstants.NEW_BASE_URL+"deleteitem";
				JSONObject jsonObject = new JSONObject();

				JSONArray jsonArray = new JSONArray();
				if(sbDeleteProdId.length() > 0) {
					String sd[] = sbDeleteProdId.toString().split(",");
					for (int k = 0; k < sd.length; k++) {
						String s1 = sd[k];
						jsonArray.put(Integer.parseInt(s1));
					}
				}

				jsonObject.put("userid", strUserId);
				jsonObject.put("productid", jsonArray);
				jsonObject.put("quote_id", strQuoteId);
				JSONArray products1 = new JSONArray();
				if (from.equals("cart")) {
					for(int i=0;i<cartList.size();i++)
                    {
                        if(Float.parseFloat(cartList.get(i).getPrice())!=0)
                        {
                            JSONObject prod_obj = new JSONObject();
                            prod_obj.put("productid", cartList.get(i).getItem_id());
                            prod_obj.put("quantity", cartList.get(i).getQty());
                            products1.put(prod_obj);
                        }
                    }
				} else {

					for(int i=0;i<completeList.size();i++)
					{
						if(completeList.get(i).getQty()>0 && !completeList.get(i).getFlag().equals("3"))
						{
							JSONObject prod_obj = new JSONObject();
							prod_obj.put("productid", completeList.get(i).getItem_id());
							prod_obj.put("quantity", completeList.get(i).getQty());
							products1.put(prod_obj);
						}
					}
					for(int i=0;i<cartList.size();i++)
					{
						if(Float.parseFloat(cartList.get(i).getPrice())!=0 && !isIdAvailableInCompleteList(cartList.get(i).getItem_id()))
						{
							JSONObject prod_obj = new JSONObject();
							prod_obj.put("productid", cartList.get(i).getItem_id());
							prod_obj.put("quantity", cartList.get(i).getQty());
							products1.put(prod_obj);
						}
					}
				}
				jsonObject.put("updateid", products1);

				if (UtilityMethods.isInternetAvailable(this)) {
					UpdateCartbg.getInstance().bLocally = true;
					myApi.reqEditCartBackToCart(strurl, MyReceiverActions.VIEW_CART_UPDATE_LOCALLY, jsonObject);
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			////////////////POST/////////////

		} catch (Exception e) {
			new GrocermaxBaseException("CartProductList", "updateItemInCartBackToCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	public boolean isIdAvailableInCompleteList(String id){
		for(int i=0;i<completeList.size();i++)
		{
			if (completeList.get(i).getItem_id()!=null) {
				if(completeList.get(i).getItem_id().equals(id))
                {
                    return true;
                }
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK && requestCode == AppConstants.LOGIN_REQUEST_CODE) {
				if(loginThrough.equals("view_cart")){
				showDialog();
				String url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
				myApi.reqViewCartAfterDelete(url, MyReceiverActions.CART_DETAIL_AFTER_LOGIN);
				}else{
					Intent i=new Intent(CartProductList.this,CouponPage.class);
					startActivity(i);
				}
			}
		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "onActivityResult", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "onActivityResult", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void callAddressApi()
	{
		try {
			showDialog();
			String url = UrlsConstants.CHECKOUT_ADDRESS_BOOK + MySharedPrefs.INSTANCE.getUserId();
			myApi.reqCheckOutAddress(url,MyReceiverActions.CHECKOUT_ADDRESS);
		}catch(NullPointerException e){
		new GrocermaxBaseException("CartProductList", "callAddressApi", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "callAddressApi", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	public boolean isOutofStockDueToFreeItems(String p_id,String web_qty){

		if (web_qty!=null) {
			int quant=0;
			int j=0;
			for(int i=0;i<cartList.size();i++){
                if(p_id.equals(cartList.get(i).getItem_id())){
                  quant=quant+cartList.get(i).getQty();
					j=j+1;
				}
            }
			if(j==1)
				return false;

			if(Float.parseFloat(web_qty)<(float)quant)
                return true;
            else
                return false;
		} else {
			return true;
		}

	}

	public void checkConditionToShowUpdatePopup(){
		if (cartList != null && cartList.size() > 0) {
			OFS_list.clear();
			QTY_RED_list.clear();
			for(int i=0;i<cartList.size();i++){
					CartDetail obj=cartList.get(i);
					String price=obj.getPrice().toString().replace(",", "");
				  	if (Float.parseFloat(price)>0) {
					if(isOutofStockDueToFreeItems(obj.getItem_id(),obj.getWebQty())){

						obj.setOfs("true");
						OFS_list.add(obj);

                    }else {
                        if (obj.getWebQty() != null) {
                            if (Integer.parseInt(obj.getWebQty()) > 0) {
                                if (Integer.parseInt(obj.getWebQty()) < obj.getQty()) {
                                    QTY_RED_list.add(obj);
                                }
                            } else {
                                OFS_list.add(obj);
                            }
                        } else {
                            OFS_list.add(obj);
                        }
                    }
				}/*else if(Float.parseFloat(obj.getWebQty())<=0){
						OFS_list.add(obj);
					}*/
			}
			if(OFS_list.size()>0 || QTY_RED_list.size()>0){
                if (alert_update==null || !alert_update.isShowing())
                    showUpdateCartPopup();
            }
		}
	}



	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}

		try {
			System.out.println("resume entered");
			if (cartList != null && cartList.size() > 0) {
				orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
				checkConditionToShowUpdatePopup();
				if (orderReviewBean != null) {
					float saving = 0;
					for (int i = 0; i < cartList.size(); i++) {
						saving = saving + (cartList.get(i).getQty() * (Float.parseFloat(cartList.get(i).getMrp()) - Float.parseFloat(cartList.get(i).getPrice())));
					}
					saving = saving - (Float.parseFloat(orderReviewBean.getDiscount_amount()));

					//			tv_subTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(cartBean.getSubTotal())));
					tv_subTotal.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(orderReviewBean.getSubTotal())));
					tv_discount.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(orderReviewBean.getDiscount_amount())));
					tvSavePrice.setText(getResources().getString(R.string.rs)+""+ String.format("%.2f", saving));
					savingGlobal=String.valueOf(saving);



					if(Float.parseFloat(orderReviewBean.getShipping_ammount())==0)
					{
						tv_shipping.setText("Free");
					}
					else{
						tv_shipping.setText(getResources().getString(R.string.rs)+""+String.format("%.2f", Float.parseFloat(orderReviewBean.getShipping_ammount())));
						shippingGlobal=orderReviewBean.getShipping_ammount();
					}

					tv_grandTotal.setText(getResources().getString(R.string.rs)+""+ String.format("%.2f", Float.parseFloat(orderReviewBean.getGrandTotal())));
					totalGlobal=orderReviewBean.getGrandTotal();


					try {
						UtilityMethods.sendGTMEvent(activity,"Cart Details","totalitem="+MySharedPrefs.INSTANCE.getTotalItem()+"/order_amount="+String.format("%.2f", Float.parseFloat(orderReviewBean.getGrandTotal())),"Android View Cart");
					} catch (Exception e) {
						e.printStackTrace();
					}


				}

			}
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "onResume", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}

		try {
            initHeader(findViewById(R.id.header), false, "Your Cart");
			icon_header_cart.setClickable(false);
			cart_count_txt.setClickable(false);

//            ((ImageView)findViewById(R.id.icon_header_cart)).setVisibility(View.INVISIBLE);
//            ((TextView)findViewById(R.id.nom_producte)).setVisibility(View.INVISIBLE);
        }catch(Exception e){
            new GrocermaxBaseException("OrderHistory","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
	}


//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		initHeader(findViewById(R.id.header), true, null);
//
//		orderReviewBean=MySharedPrefs.INSTANCE.getOrderReviewBean();
//		if(orderReviewBean != null){
//			List<CartDetail> cartList = orderReviewBean.getProduct();
//			if(cartList!=null && cartList.size() > 0)
//			{
//				float saving=0;
//				for(int i=0;i<cartList.size();i++)
//				{
//					saving=saving+(cartList.get(i).getQty()*(Float.parseFloat(cartList.get(i).getMrp())-Float.parseFloat(cartList.get(i).getPrice())));
//				}
//
//				saving = saving-(Float.parseFloat(orderReviewBean.getDiscount_amount()));
////				if(orderReviewBean.getCouponCode() != null && !orderReviewBean.getCouponCode().equalsIgnoreCase("null")){
////					float save = Float.parseFloat(orderReviewBean.getSubTotal()) - Float.parseFloat(orderReviewBean.getCouponSubtotalWithDsicount());
////					saving = save + saving;
////				}
//				tvSavePrice.setText("Rs."+String.format("%.2f",saving));
////				tv_yousave.setText("Rs."+String.format("%.2f",saving));
//
//	//			tv_subTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(cartBean.getSubTotal())));
//				if(orderReviewBean.getSubTotal() != null && orderReviewBean.getSubTotal().length() > 0){
//					tv_subTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean.getSubTotal())));
//				}
//				if(orderReviewBean.getDiscount_amount() != null){
//					tv_discount.setText("Rs."+String.format("%.2f", Float.parseFloat(orderReviewBean.getDiscount_amount())));
//				}
//
//				if(!MySharedPrefs.INSTANCE.getCouponAmount().equalsIgnoreCase("0") && MySharedPrefs.INSTANCE.getCouponAmount() != null){
//					tvCoupon.setText("Rs."+String.format("%.2f", Float.parseFloat(MySharedPrefs.INSTANCE.getCouponAmount())));
//					ll_coupon.setVisibility(View.VISIBLE);
//				}else{
//					ll_coupon.setVisibility(View.GONE);
//				}
//
//				if(orderReviewBean.getShipping_ammount() != null){
//				 	tv_shipping.setText("Rs."+Float.parseFloat(orderReviewBean.getShipping_ammount()));
//				}
//	//			tv_grandTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(cartBean.getGrandTotal())));
//				if(!MySharedPrefs.INSTANCE.getCouponAmount().equalsIgnoreCase("0") && MySharedPrefs.INSTANCE.getCouponAmount() != null){
//				if(orderReviewBean.getGrandTotal() != null && orderReviewBean.getGrandTotal().length() > 0){
//					Float gtotal = Float.parseFloat(orderReviewBean.getGrandTotal()) - saving;
//					tv_grandTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(String.valueOf(gtotal))));
////					tv_grandTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean.getGrandTotal())));
//					}
//				}
//				else{
//					tv_grandTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean.getGrandTotal())));
//				}
//
//
//				if(MySharedPrefs.INSTANCE.getTotalItem()!=null)
//				{
//					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int)Float.parseFloat(cartBean.getItems_qty())));
//					cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
//				}
//			}
//		}
//
//	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}

		try{
//			EasyTracker.getInstance(this).activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
		}catch(Exception e){}
		/*screen tracking using rocq*/
		try {
			RocqAnalytics.initialize(this);
			RocqAnalytics.startScreen(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
       /*------------------------------*/
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
		try{
//			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
		}catch(Exception e){}
		try {
			RocqAnalytics.stopScreen(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			if ((sbDeleteProdId != null && sbDeleteProdId.length() > 0) || (bIsEdit)) {
				updateItemInCart();
				bIsEdit = false;
			}
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "onDestroy", e.toString(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try {
			if(HomeScreen.bFromHome) {
				HomeScreen.isFromFragment = false;    //work fine for home
			}else{
				HomeScreen.isFromFragment = true;
			}

		}catch(Exception e){}
	}

	//    public String getHeaderUpdateQuantity(){
//    	return cart_count_txt.getText().toString();
//    }

	public void updateHeaderQuantity(String strUpdateQuantity,String plusMinus){
		try {
			bIsEdit = true;
			if (plusMinus.equalsIgnoreCase("plus")) {
				int updated = Integer.parseInt(cart_count_txt.getText().toString()) + Integer.parseInt(strUpdateQuantity);
				cart_count_txt.setText(String.valueOf(updated));
				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(updated));  //it holds local value of cart b/c when pressed back in base activity it updates value.
//				initHeader(findViewById(R.id.header), true, null);
				initHeader(findViewById(R.id.header), false, "Your Cart");

			} else if (plusMinus.equalsIgnoreCase("minus")) {
				if (plusMinus.equalsIgnoreCase("minus")) {
					int updated = Integer.parseInt(cart_count_txt.getText().toString()) - Integer.parseInt(strUpdateQuantity);
					cart_count_txt.setText(String.valueOf(updated));
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(updated));  //it holds local value of cart b/c when pressed back in base activity it updates value.
//					initHeader(findViewById(R.id.header), true, null);
					initHeader(findViewById(R.id.header), false, "Your Cart");

				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "updateHeaderQuantity", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}

	}

}
