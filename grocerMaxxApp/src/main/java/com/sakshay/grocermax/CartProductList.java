package com.sakshay.grocermax;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.flurry.sdk.in;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.CartAdapter;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.CartDetailBean;
import com.sakshay.grocermax.bean.CheckoutAddressBean;
import com.sakshay.grocermax.bean.OrderReviewBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

public class CartProductList extends BaseActivity implements OnClickListener{

	ListView mList;
	public Button place_order,update_cart;
	public static ArrayList<CartDetail> cartList;           //managing cart items on delete and add on view cart screen locally
	String user_id;
	CartDetailBean cartBean;
	OrderReviewBean orderReviewBean;
	CartAdapter mAdapter;
	int position = -1;
	TextView txt_subTotal,txt_shipping,txt_grand_total,txt_discount,txt_yousaved;
	public TextView tv_subTotal,tv_discount;
	public TextView tv_grandTotal,tv_shipping;
	//	TextView tv_yousave;
	TextView tvSavePrice;
	TextView txtDiscount;
	TextView textView1;
	TextView textViewCoupon,tvCoupon;
	TextView tvYourCart;
//	TextView tvCartItemCount,tvCartTotalTop;
//	TextView txtItems,txtTotal;
	LinearLayout ll_total,ll_discount,ll_shipping,ll_coupon;
	EasyTracker tracker;
	//	public JSONArray jsonArray ;
	public JSONObject jsonObjectUpdate = null;
	public StringBuilder sbDeleteProdId;
	public static String strShippingChargeLimit = "500";

	private boolean bIsEdit = false;   //true if user plus or minus anything in cart otherwise false and use in onDestroy.

	String strUserIdtemp,strQuoteIdtemp;
//	public String strCouponCode,
//				  strSubTotal,
//				  strSubtotalWithDiscount;

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

//		addActionsInFilter(MyReceiverActions.VIEW_CART);
			addActionsInFilter(MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);

			tv_subTotal = (TextView) findViewById(R.id.tv_subTotal);
			tv_discount = (TextView) findViewById(R.id.tv_discount);
			tvCoupon = (TextView) findViewById(R.id.tv_coupon);
			tvSavePrice = (TextView) findViewById(R.id.tv_save_price);
			txtDiscount = (TextView) findViewById(R.id.txt_discount);
			tv_grandTotal = (TextView) findViewById(R.id.tv_grandTotal);
			tv_shipping = (TextView) findViewById(R.id.tv_shipping);
//		tv_yousave=(TextView)findViewById(R.id.tv_yousave);
			tvYourCart = (TextView) findViewById(R.id.tv_your_cart);
//			tvCartItemCount = (TextView) findViewById(R.id.tv_cart_item_count);
//			tvCartTotalTop = (TextView) findViewById(R.id.tv_cart_total);
//			txtItems = (TextView) findViewById(R.id.txt_items);
//			txtTotal = (TextView) findViewById(R.id.txt_total);

			ll_total = (LinearLayout) findViewById(R.id.ll_total);
			ll_discount = (LinearLayout) findViewById(R.id.ll_discount);
			ll_shipping = (LinearLayout) findViewById(R.id.ll_shipping);
			ll_coupon = (LinearLayout) findViewById(R.id.ll_coupon);

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

				initHeader(findViewById(R.id.header), true, "Your Basket");
				setCartList(cartBean);
				initFooter(findViewById(R.id.footer), 1, -1);
			}
			icon_header_cart.setClickable(false);
			cart_count_txt.setClickable(false);
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	public void setCartList(CartDetailBean cartBean)
	{
		try {
			if (cartList != null && cartList.size() > 0) {
				ll_total.setVisibility(View.VISIBLE);
				orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

				if (cartBean != null) {
					float saving = 0;

					for (int i = 0; i < cartList.size(); i++) {
						saving = saving + (cartList.get(i).getQty() * (Float.parseFloat(cartList.get(i).getMrp()) - Float.parseFloat(cartList.get(i).getPrice())));
					}
					//float discount=Float.parseFloat(cartBean.getSubTotal())-Float.parseFloat(cartBean.getGrandTotal());
					saving = saving - (Float.parseFloat(orderReviewBean.getDiscount_amount()));
//		    tv_yousave.setText("Rs."+String.format("%.2f",saving));
					tv_subTotal.setText("Rs." + String.format("%.2f", Float.parseFloat(cartBean.getSubTotal())));
					tv_discount.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getDiscount_amount())));
					tvSavePrice.setText("Rs." + String.format("%.2f", saving));
			/*if(Float.parseFloat(orderReviewBean.getDiscount_amount())==0)
			{
				ll_discount.setVisibility(View.GONE);
			}
			else
				ll_discount.setVisibility(View.VISIBLE);*/
					tv_shipping.setText("Rs." + Float.parseFloat(orderReviewBean.getShipping_ammount()));

			if(Float.parseFloat(orderReviewBean.getShipping_ammount())==0)
			{
				ll_shipping.setVisibility(View.GONE);
			}
			else
				ll_shipping.setVisibility(View.VISIBLE);

					//tv_discount.setText("-"+String.format("%.2f",discount));
					tv_grandTotal.setText("Rs." + String.format("%.2f", Float.parseFloat(cartBean.getGrandTotal())));

//					if(tvCartTotalTop != null){
//						tvCartTotalTop.setText("Rs." + String.format("%.2f", Float.parseFloat(cartBean.getGrandTotal())));
//					}
//					if(tvCartItemCount != null) {
//						tvCartItemCount.setText(String.valueOf(cartList.size()));
//					}
					if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int) Float.parseFloat(cartBean.getItems_qty())));
						cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					}

					//cart_count_txt.setText(cartBean.getItems_count());


					OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
					orderReviewBean.setProduct(cartList);
//	    	orderReviewBean.setSubTotal(cartBean.getGrandTotal());
//	    	orderReviewBean.setSubTotal(cartBean.get);
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


				} else
					ll_total.setVisibility(View.GONE);


				mList = (ListView) findViewById(R.id.category_list);
				place_order = (Button) findViewById(R.id.button_place_order);

//			mAdapter = new CartAdapter(CartProductList.this, cartList);
//			mAdapter = new CartAdapter(CartProductList.this);
				update_cart = (Button) findViewById(R.id.button_update_cart1);

				place_order.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
				update_cart.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

				mAdapter = new CartAdapter(CartProductList.this);
				mList.setAdapter(mAdapter);

				mList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						//				showDialog();
					}
				});
				place_order.setVisibility(View.VISIBLE);
				place_order.setOnClickListener(this);
				update_cart.setVisibility(View.GONE);
				update_cart.setOnClickListener(this);
			} else {
				UtilityMethods.customToast(ToastConstant.CART_EMPTY, mContext);
				ll_total.setVisibility(View.GONE);
				Intent intent = new Intent(CartProductList.this, HomeScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "setCartList", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "setCartList", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
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
			place_order.setVisibility(View.GONE);
		}
		}catch(NullPointerException e){
			new GrocermaxBaseException("CartProductList", "updateCart", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "updateCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	void OnResponse(Bundle bundle) {
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
					Intent intent = new Intent(CartProductList.this, com.sakshay.grocermax.ShippingAddress.class);
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
					BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
				}
				cartList.clear();
				cartList = cartBean.getItems();
				setCartList(cartBean);
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
					BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
				}
				cartList.clear();
				cartList = cartBean.getItems();
				setCartList(cartBean);
//			callAddressApi();                        //commented on 4/8/15
			} else if (action.equals(MyReceiverActions.VIEW_CART_GO_HOME_SCREEN)) {
				dismissDialog();
				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
			} else if (action.equals(MyReceiverActions.VIEW_CART_UPDATE_LOCALLY)) {
				dismissDialog();

				sbDeleteProdId = new StringBuilder();                                //b/c whenever come there means deleteid already contained should be removed.

				CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
//				   if(cartBean.getFlag().equalsIgnoreCase(Constants.SERVER_SUCCESS)) {
				if(cartBean != null) {                                                         //uses null condition b/c sometimes result coming from server SLIM APPLICATION ERROR
					UtilityMethods.deleteCloneCart(this);
					for (int i = 0; i < cartBean.getItems().size(); i++) {
						UtilityMethods.writeCloneCart(this, Constants.localCloneFile, cartBean.getItems().get(i));
					}

					if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
	//				MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int) Float.parseFloat(cartBean.getItems_qty())));
						BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					}
					cartList.clear();
					cartList = cartBean.getItems();
					setCartList(cartBean);
				}else{
					showDialog();
					String url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
					myApi.reqViewCartSlipErrorApp(url);
				}
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
						BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
					}
					cartList.clear();
					cartList = cartBean.getItems();
					setCartList(cartBean);

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
						UtilityMethods.customToast(ToastConstant.ATLEAST_ONE_ITEM_IN_CART, mContext);
						return;
					}
					String userId = MySharedPrefs.INSTANCE.getUserId();
					if (userId == null || userId.length() == 0) {
						Intent intent = new Intent(mContext, LoginActivity.class);
						startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
					} else {
						callAddressApi();
					}

					break;

				case R.id.button_update_cart1:
					updateItemInCartBackToCart();
					bIsEdit = false;
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

	public void updateItemInCart()
	{
		try {
			JSONArray products = new JSONArray();
			for(int i=0;i<cartList.size();i++)
			{
				if(Float.parseFloat(cartList.get(i).getPrice())!=0)
				{
					JSONObject prod_obj = new JSONObject();
					prod_obj.put("productid", cartList.get(i).getItem_id());
					prod_obj.put("quantity", cartList.get(i).getQty());
					System.out.println(cartList.get(i).getQty()+"==qty===item id=="+cartList.get(i).getItem_id());
					products.put(prod_obj);
				}
			}



//			String url = UrlsConstants.NEW_BASE_URL+"deleteitem?userid="+ MySharedPrefs.INSTANCE.getUserId() +
//					"&productid=" + sbDeleteProdId +
//					"&quote_id="+ MySharedPrefs.INSTANCE.getQuoteId() +"&updateid="+ URLEncoder.encode(products.toString(), "UTF-8");

			String url = UrlsConstants.NEW_BASE_URL+"deleteitem?userid="+ strUserIdtemp +
					"&productid=" + sbDeleteProdId +
					"&quote_id="+ strQuoteIdtemp +"&updateid="+ URLEncoder.encode(products.toString(), "UTF-8");

			System.out.println("==URL'S HERE=="+url);
			if(UtilityMethods.isInternetAvailable(this)){
				UpdateCartbg.getInstance().bLocally = true;
//				myApi.reqEditCart(url,MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
				myApi.reqEditCart(url);
			}
//			String	url = UrlsConstants.UPDATE_CART_URL
//						+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//						+ URLEncoder.encode(products.toString(), "UTF-8");
//			myApi.reqViewCartAfterDelete(url,MyReceiverActions.CART_DETAIL_AFTER_DELETE);

		} catch (Exception e) {
			new GrocermaxBaseException("CartProductList", "updateItemInCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void updateItemInCartBackToCart()
	{
		try {
			JSONArray products = new JSONArray();
			for(int i=0;i<cartList.size();i++)
			{
				if(Float.parseFloat(cartList.get(i).getPrice())!=0)
				{
					JSONObject prod_obj = new JSONObject();
					prod_obj.put("productid", cartList.get(i).getItem_id());
					prod_obj.put("quantity", cartList.get(i).getQty());
					System.out.println(cartList.get(i).getQty()+"==qty===item id=="+cartList.get(i).getItem_id());
					products.put(prod_obj);
				}
			}
			showDialog();

//			String url = UrlsConstants.NEW_BASE_URL+"deleteitem?userid="+ MySharedPrefs.INSTANCE.getUserId() +
//					"&productid=" + UpdateCartbg.getInstance().alDeleteId +
//					"&quote_id="+ MySharedPrefs.INSTANCE.getQuoteId() +"&updateid="+ URLEncoder.encode(products.toString(), "UTF-8");

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

//			String url = UrlsConstants.NEW_BASE_URL+"deleteitem?userid="+ MySharedPrefs.INSTANCE.getUserId() +
//					"&productid=" + sbDeleteProdId +
//					"&quote_id="+ MySharedPrefs.INSTANCE.getQuoteId() +"&updateid="+ URLEncoder.encode(products.toString(), "UTF-8");

			String url = UrlsConstants.NEW_BASE_URL+"deleteitem?userid="+ strUserId +
					"&productid=" + sbDeleteProdId +
					"&quote_id="+ strQuoteId +"&updateid="+ URLEncoder.encode(products.toString(), "UTF-8");

//			System.out.println("==URL'S HERE=="+url);
			if(UtilityMethods.isInternetAvailable(this)){
				UpdateCartbg.getInstance().bLocally = true;
				myApi.reqEditCartBackToCart(url, MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
//				myApi.reqEditCart(url);
			}
//			String	url = UrlsConstants.UPDATE_CART_URL
//						+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//						+ URLEncoder.encode(products.toString(), "UTF-8");
//			myApi.reqViewCartAfterDelete(url,MyReceiverActions.CART_DETAIL_AFTER_DELETE);

		} catch (Exception e) {
			new GrocermaxBaseException("CartProductList", "updateItemInCartBackToCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK && requestCode == AppConstants.LOGIN_REQUEST_CODE) {
//			callAddressApi();
				showDialog();
				String url = UrlsConstants.VIEW_CART_URL
						+ MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
				myApi.reqViewCartAfterDelete(url, MyReceiverActions.CART_DETAIL_AFTER_LOGIN);
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
			myApi.reqCheckOutAddress(url);
		}catch(NullPointerException e){
		new GrocermaxBaseException("CartProductList", "callAddressApi", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CartProductList", "callAddressApi", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			if (cartList != null && cartList.size() > 0) {
				ll_total.setVisibility(View.VISIBLE);
				orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
//			if(cartBean!=null)
				if (orderReviewBean != null) {
					float saving = 0;
//					if(tvCartItemCount != null) {
//						tvCartItemCount.setText(String.valueOf(cartList.size()));
//					}

					for (int i = 0; i < cartList.size(); i++) {
						saving = saving + (cartList.get(i).getQty() * (Float.parseFloat(cartList.get(i).getMrp()) - Float.parseFloat(cartList.get(i).getPrice())));
					}
					saving = saving - (Float.parseFloat(orderReviewBean.getDiscount_amount()));

					//			tv_subTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(cartBean.getSubTotal())));
					tv_subTotal.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getSubTotal())));
					tv_discount.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getDiscount_amount())));
					tvSavePrice.setText("Rs." + String.format("%.2f", saving));
					tv_shipping.setText("Rs." + Float.parseFloat(orderReviewBean.getShipping_ammount()));
//				tv_grandTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(cartBean.getGrandTotal())));
					tv_grandTotal.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getGrandTotal())));
//					if(tvCartTotalTop != null){
//						tvCartTotalTop.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getGrandTotal())));
//					}
				}
//			else
//				ll_total.setVisibility(View.GONE);

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

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try{
			EasyTracker.getInstance(this).activityStart(this);
//			tracker.activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
		}catch(Exception e){}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
			tracker.activityStop(this);
			FlurryAgent.onEndSession(this);
		}catch(Exception e){}
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
			new GrocermaxBaseException("CartProductList", "onDestroy", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
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
