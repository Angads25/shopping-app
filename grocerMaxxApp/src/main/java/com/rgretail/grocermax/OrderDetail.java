package com.rgretail.grocermax;

import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.OrderDetailItem;
import com.rgretail.grocermax.bean.OrderedProductList;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

public class OrderDetail extends BaseActivity{

	TextView tv_about_order,tv_shipping_method,tv_payment_mode,tv_delivery_date,tv_shipping_address,tv_billing_address;
	//	TextView tvOrderId;
	String order_id,order_increement_id;
	List<OrderDetailItem> items;
	LinearLayout ll_product,ll_discount;
	TextView tv_subtotal_2,tv_shipping,tv_youpay,tv_discount;
	TextView tvShippingCharges;
	TextView tvCoupon;

	//	LinearLayout ll_header;
	LinearLayout ll_subtotal,ll_shipping,ll_youpay;
	TextView tv_delivery_time;
	Button btn_reOrder;
//	EasyTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		try {
			order_id = getIntent().getStringExtra("order_id");
			order_increement_id = getIntent().getStringExtra("order_increement_id");
			addActionsInFilter(MyReceiverActions.ORDER_DETAIL);
			addActionsInFilter(MyReceiverActions.ORDER_REORDER);
			addActionsInFilter(MyReceiverActions.VIEW_CART);
			initHeader(findViewById(R.id.header), true, "Order Detail");
			initViews();
			showDialog();
			myApi.reqOrderDetail(UrlsConstants.ORDER_DETAIL_URL + order_id);
		}catch(NullPointerException e){
			new GrocermaxBaseException("OrderDetail","onCreate",e.getMessage(), GrocermaxBaseException.NULL_POINTER,"nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	public void initViews()
	{
		try{
//		TextView tvProductNameHeading = (TextView) findViewById(R.id.tv_name);
//		TextView tvProductPriceHeading = (TextView) findViewById(R.id.tv_price);
//		TextView tvProductQuantityHeading = (TextView) findViewById(R.id.tv_quantity);
//		TextView tvProductSubTotalHeading = (TextView) findViewById(R.id.tv_subtotal);

//		tvProductNameHeading.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		tvProductPriceHeading.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		tvProductQuantityHeading.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		tvProductSubTotalHeading.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

		TextView txtCoupon = (TextView) findViewById(R.id.txt_coupon);
		TextView tvAboutOrder = (TextView) findViewById(R.id.txt_about_order);
		TextView tvDeliveryDate = (TextView) findViewById(R.id.txt_delivery_date);
		TextView tvDeliveryTime = (TextView) findViewById(R.id.txt_delivery_time);
		TextView tvShippingAddress = (TextView) findViewById(R.id.txt_shipping_address);
		TextView tvBillingAddress = (TextView) findViewById(R.id.txt_billing_address);
		TextView tvShippingMethod = (TextView) findViewById(R.id.txt_shipping_method);
		TextView tvPaymentMode = (TextView) findViewById(R.id.txt_payment_mode);
		TextView txtShippingCharges = (TextView) findViewById(R.id.txt_shipping_charge);

		tvAboutOrder.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvDeliveryDate.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvDeliveryTime.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvShippingAddress.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvBillingAddress.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvShippingMethod.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvPaymentMode.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		txtShippingCharges.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		txtCoupon.setTypeface(CustomFonts.getInstance().getRobotoBold(this));


//		tvOrderId = (TextView) findViewById(R.id.order_id);
//		tvOrderId.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

		tv_about_order=(TextView)findViewById(R.id.tv_about_order);
		tv_shipping_method=(TextView)findViewById(R.id.tv_shipping_method);
		tv_payment_mode=(TextView)findViewById(R.id.tv_payment_mode);
		tv_delivery_date=(TextView)findViewById(R.id.tv_delivery_date);
		tv_delivery_time=(TextView)findViewById(R.id.tv_delivery_time);
		tv_shipping_address=(TextView)findViewById(R.id.tv_shipping_address);
		tv_billing_address=(TextView)findViewById(R.id.tv_billing_address);
		tvShippingCharges = (TextView)findViewById(R.id.tv_shipping_charges);
		tvCoupon = (TextView) findViewById(R.id.tv_coupon);

		tv_about_order.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
		tv_shipping_method.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
		tv_payment_mode.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
		tv_delivery_date.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
		tv_delivery_time.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
		tv_shipping_address.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
		tv_billing_address.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
		tvShippingCharges.setTypeface(CustomFonts.getInstance().getRobotoLight(this));

		tv_discount=(TextView)findViewById(R.id.tv_discount);
		ll_product=(LinearLayout)findViewById(R.id.ll_product_list);
		ll_discount=(LinearLayout)findViewById(R.id.ll_discount);

		ll_subtotal=(LinearLayout)findViewById(R.id.ll_subtotal);
		ll_youpay=(LinearLayout)findViewById(R.id.ll_youpay);
		ll_shipping=(LinearLayout)findViewById(R.id.ll_shipping);

//		ll_header=(LinearLayout)findViewById(R.id.ll_header);

		tv_subtotal_2=(TextView)findViewById(R.id.tv_subtotal_2);
		tv_shipping=(TextView)findViewById(R.id.tv_shipping);
		tv_youpay=(TextView)findViewById(R.id.tv_youpay);

		tv_about_order.setVisibility(View.GONE);
		tv_shipping_method.setVisibility(View.GONE);
		tv_payment_mode.setVisibility(View.GONE);
		tv_delivery_date.setVisibility(View.GONE);
		tv_delivery_time.setVisibility(View.GONE);
		tv_shipping_address.setVisibility(View.GONE);
		tv_billing_address.setVisibility(View.GONE);
//		tvOrderId.setVisibility(View.GONE);

//		ll_header.setVisibility(View.GONE);
		ll_subtotal.setVisibility(View.GONE);
		ll_youpay.setVisibility(View.GONE);
		ll_shipping.setVisibility(View.GONE);

		TextView textSubTotal = (TextView) findViewById(R.id.txt_subtotal);
		TextView textShipping = (TextView) findViewById(R.id.txt_shipping);
		TextView textGrandTotal = (TextView) findViewById(R.id.txt_grand_total);

		textSubTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tv_subtotal_2.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		textShipping.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tv_shipping.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		textGrandTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tv_youpay.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
		tvCoupon.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			///// code added by Ishan/////////////////////////////////
			btn_reOrder=(Button)findViewById(R.id.btn_reOrder);
			btn_reOrder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showDialog();
					myApi.reqReorder(UrlsConstants.ORDER_REORDER_URL + order_increement_id);
				}
			});
			/////////code ends here//////////////////

		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","initViews",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
//		line.setVisibility(View.GONE);
	}
	public void visibleViews()
	{
		try{
	//		tvOrderId.setVisibility(View.VISIBLE);
			tv_about_order.setVisibility(View.VISIBLE);
			tv_shipping_method.setVisibility(View.VISIBLE);
			tv_payment_mode.setVisibility(View.VISIBLE);
			tv_delivery_date.setVisibility(View.VISIBLE);
			tv_delivery_time.setVisibility(View.VISIBLE);
			tv_shipping_address.setVisibility(View.VISIBLE);
			tv_shipping.setVisibility(View.VISIBLE);
			tvShippingCharges.setVisibility(View.VISIBLE);
			tv_billing_address.setVisibility(View.VISIBLE);
	//		ll_header.setVisibility(View.VISIBLE);
			ll_subtotal.setVisibility(View.VISIBLE);
			ll_youpay.setVisibility(View.VISIBLE);
			ll_shipping.setVisibility(View.VISIBLE);
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","visibleViews",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	@Override
	public void OnResponse(Bundle bundle) {
		dismissDialog();
		try
		{
			if (bundle.getString("ACTION").equals(MyReceiverActions.ORDER_DETAIL)) {

				String orderDetail= (String) bundle.getSerializable(ConnectionService.RESPONSE);
				JSONObject orderDetailJson=new JSONObject(orderDetail);
				if (orderDetailJson.getString("flag").equalsIgnoreCase("1")) {
					orderDetailJson=orderDetailJson.getJSONObject("OrderDetail");
					visibleViews();

//					tvOrderId.setText("Order ID : "+order_id);
//					tvOrderId.setText("Order ID : "+order_increement_id);

//					setOrderInformation(orderDetailJson.getString("created_at").split(" ")[0]);
					setOrderInformation(orderDetailJson.getString("created_at"));

					try {
						if (orderDetailJson.getString("discount_amount") != null) {
							String strCouponDiscount;
//							if (orderDetailJson.getString("discount_amount").startsWith("-")) {
//								strCouponDiscount = orderDetailJson.getString("discount_amount").substring(1, orderDetailJson.getString("discount_amount").length() - 1);
//							} else {
								strCouponDiscount = orderDetailJson.getString("discount_amount");
//							}
							tvCoupon.setText("Rs. " + Float.parseFloat(strCouponDiscount));
						}
					}catch(Exception e){}

					setShippingMethod(orderDetailJson.getString("shipping_description"));
					setPaymentMethod(orderDetailJson.getJSONObject("payment").getString("method"));
					String date=orderDetailJson.getJSONArray("deliverydate").getJSONObject(0).getString("value");
					String time=orderDetailJson.getJSONArray("deliverydate").getJSONObject(1).getString("value");
					setDeliveryDate(date);
					setDeliveryTime(time);
					String name=orderDetailJson.getJSONObject("shipping_address").getString("firstname")+" "+orderDetailJson.getJSONObject("shipping_address").getString("lastname");
					String street=orderDetailJson.getJSONObject("shipping_address").getString("street");
					String city=orderDetailJson.getJSONObject("shipping_address").getString("city");
//					if(orderDetailJson.getJSONObject("shipping_address").getString("region")!=null || orderDetailJson.getJSONObject("shipping_address").getString("region")!=null) {
//						String state=orderDetailJson.getJSONObject("shipping_address").getString("region");
//					}else{
//						String state=orderDetailJson.getJSONObject("shipping_address").getString("region");
//					}
					String state=orderDetailJson.getJSONObject("shipping_address").getString("region");
					String phone=orderDetailJson.getJSONObject("shipping_address").getString("telephone");
					String pin=orderDetailJson.getJSONObject("shipping_address").getString("postcode");
//					setAddress("Shipping Address : ",name, street, city, state, phone, pin, tv_shipping_address);
					setAddress("",name, street, city, state, phone, pin, tv_shipping_address);

					String name_b=orderDetailJson.getJSONObject("shipping_address").getString("firstname")+" "+orderDetailJson.getJSONObject("shipping_address").getString("lastname");
					String street_b=orderDetailJson.getJSONObject("shipping_address").getString("street");
					String city_b=orderDetailJson.getJSONObject("shipping_address").getString("city");
					String state_b=orderDetailJson.getJSONObject("shipping_address").getString("region");
					String phone_b=orderDetailJson.getJSONObject("shipping_address").getString("telephone");
					String pin_b=orderDetailJson.getJSONObject("shipping_address").getString("postcode");
//					setAddress("Billing Address : ",name_b, street_b, city_b, state_b, phone_b, pin_b, tv_billing_address);
					setAddress("",name_b, street_b, city_b, state_b, phone_b, pin_b, tv_billing_address);
					Gson gson=new Gson();
					OrderedProductList orderedProductList=gson.fromJson(orderDetailJson.toString(),OrderedProductList.class);
					setProductData(orderedProductList.getItems());
					tv_shipping.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderDetailJson.getJSONObject("payment").getString("shipping_amount"))));      //in your order
					tvShippingCharges.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderDetailJson.getJSONObject("payment").getString("shipping_amount"))));
					tv_subtotal_2.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderDetailJson.getString("subtotal"))));
					float discount=Float.parseFloat(orderDetailJson.getString("discount_amount"));
					if(discount<0)
					{
//						ll_discount.setVisibility(View.VISIBLE);                       //commented 15/9/15
						ll_discount.setVisibility(View.GONE);
						tv_discount.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderDetailJson.getString("discount_amount"))));
					}
					tv_youpay.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderDetailJson.getJSONObject("payment").getString("amount_ordered"))));

					/////////code added by Ishan//////////////////////////
					String store_id=orderDetailJson.getString("store_id");
					//System.out.println("id by server="+orderDetailJson.getString("store_id"));
					//System.out.println("id saved="+MySharedPrefs.INSTANCE.getSelectedStoreId());
					if(store_id.equals(MySharedPrefs.INSTANCE.getSelectedStoreId())){
						btn_reOrder.setVisibility(View.VISIBLE);
					}else{
						btn_reOrder.setVisibility(View.GONE);
					}
					/////////////////////////////////////////////////
				}
				else{
//					TextView msg = (TextView) findViewById(R.id.msg);
//					msg.setVisibility(View.VISIBLE);
//					msg.setText("No order detail available");
//					tvOrderId.setVisibility(View.VISIBLE);
//					tvOrderId.setText("No order detail available");
				}

		}


			////// code added by Ishan//////////////////////////
			if (bundle.getString("ACTION").equals(MyReceiverActions.ORDER_REORDER)) {
				try
				{
					String quoteResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
					//System.out.println("reorder Response = "+quoteResponse);
					JSONObject reOrderJSON=new JSONObject(quoteResponse);
					if(reOrderJSON.getInt("flag")==1){
						String quote_id=reOrderJSON.getString("QuoteId");
						MySharedPrefs.INSTANCE.putQuoteId(quote_id);
						showDialog();
						String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
						myApi.reqViewCart(url);

					}else{
						UtilityMethods.customToast(Constants.ToastConstant.QUOTE_FAIL, OrderDetail.this);
					}
				}catch(Exception e)
				{
					new GrocermaxBaseException("Reorder","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"no quote id");
				}
			}
			/////code ends here////////////////


		}catch(Exception e)
		{
			new GrocermaxBaseException("OrderDetail","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}

	}

	public void setOrderInformation(String date)
	{
		try{
//		String value="<font color='black'><b>Order Date : </b>"+date+"</font>";
//		tv_about_order.setText(Html.fromHtml(value));
			tv_about_order.setText(Html.fromHtml(date));
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","setOrderInformation",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}
	public void setShippingMethod(String method)
	{
		try{
//		String value="<font color='black'><b>Shipping Method : </b>"+method+"</font>";
//		tv_shipping_method.setText(Html.fromHtml(value));
			tv_shipping_method.setText(Html.fromHtml(method));
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","setShippingMethod",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}
	public void setPaymentMethod(String method)
	{
		try{
			/////added by Ishan//////////////
			tv_payment_mode.setText(Html.fromHtml(method));
			/////////end here////////////////////
//			if(method.equalsIgnoreCase("cashondelivery"))
//			{
//	//			String value="<font color='black'><b>Payment Method : </b>Cash On Delivery/Sodexo</font>";
//	//			tv_payment_mode.setText(Html.fromHtml(value));
//				tv_payment_mode.setText(Html.fromHtml("Cash On Delivery/Sodexo"));
//			}
//			else
//			{
//	//			String value="<font color='black'><b>Payment Method : </b>Credit Card/Debit Card/Net Banking</font>";
//	//			tv_payment_mode.setText(Html.fromHtml(value));
//				tv_payment_mode.setText(Html.fromHtml("Credit Card/Debit Card/Net Banking"));
//			}
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","setPaymentMethod",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}
	public void setDeliveryDate(String date)
	{
		try{
//		String value="<font color='black'><b>Delivery Date : </b>"+date+"</font>";
//		tv_delivery_date.setText(Html.fromHtml(value));

			tv_delivery_date.setText(Html.fromHtml(date));
//		changeDateFormat(date);
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","setDeliveryDate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public void setDeliveryTime(String time)
	{
		try{
//		String value="<font color='black'><b>Delivery Time : </b>"+time+"</font>";
//		tv_delivery_time.setText(Html.fromHtml(value));
			tv_delivery_time.setText(Html.fromHtml(time));
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","setDeliveryTime",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public void setAddress(String address,String name,String street,String city,String state,String phone,String pin,TextView tv)
	{
		try{
//		String value="<font color='black'><b>"+address +"</b>"+name+","+street+","+city+""+state+","+pin+"India T:"+phone+"</font>";
//		tv.setText(Html.fromHtml(value));
			String value=name+","+street+","+city+""+state+","+pin+"India T:"+phone;
			tv.setText(Html.fromHtml(value));
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","setAddress",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public void setProductData(List<OrderDetailItem> items){
		try{
			this.items=items;
			for(int i=0;i<items.size();i++)
			{
				String name=items.get(i).getName();
				String qty=String.valueOf((int)Float.parseFloat(items.get(i).getQty_ordered()));
				String price=String.format("%.2f",Float.parseFloat(items.get(i).getPrice()));
				String subtotal=String.format("%.2f",Float.parseFloat(items.get(i).getRow_total()));
				addView(name,qty,price,subtotal);
			}
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","setProductData",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public void addView(String name,String qty,String price,String total){
		try{
		LinearLayout ll=new LinearLayout(this);
		LinearLayout .LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getdp(50));
		ll.setLayoutParams(layoutParams);
		ll.setOrientation(LinearLayout.HORIZONTAL);

		TextView tv1=new TextView(this);
		LinearLayout .LayoutParams layoutParams1= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,4f);
		tv1.setLayoutParams(layoutParams1);
		tv1.setGravity(Gravity.CENTER_VERTICAL|Gravity.TOP);
		tv1.setPadding(0,0,0,0);
		tv1.setTextColor(Color.BLACK);
		tv1.setText(name);

		TextView tv2=new TextView(this);
		LinearLayout .LayoutParams layoutParams2= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1f);
		tv2.setLayoutParams(layoutParams2);
		tv2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		tv2.setTextColor(Color.BLACK);
		tv2.setText(qty);

		TextView tv4=new TextView(this);
		LinearLayout .LayoutParams layoutParams4= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,2f);
		tv4.setLayoutParams(layoutParams4);
		tv4.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		tv4.setTextColor(Color.BLACK);
		tv4.setText("Rs. "+total);
		tv4.setPadding(0, 0, 5, 0);

		TextView tv3=new TextView(this);
		LinearLayout .LayoutParams layoutParams3= new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,2f);
		tv3.setLayoutParams(layoutParams3);
		tv3.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		tv3.setTextColor(Color.BLACK);
		tv3.setText("Rs. "+price);

		View divider_view=new View(this);
		LinearLayout .LayoutParams divider_layout_param= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,20);
		divider_view.setLayoutParams(divider_layout_param);

		ll.removeAllViews();
		ll.addView(tv1);
		ll.addView(tv3);
		ll.addView(tv2);
		ll.addView(tv4);

		ll_product.addView(ll);
		ll_product.addView(divider_view);
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","addView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	public int getdp(int a)
	{
		try{
			int paddingPixel = a;
			float density = this.getResources().getDisplayMetrics().density;
			int paddingDp = (int)(paddingPixel * density);
			return paddingDp;
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","getdp",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
		return  0;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			initHeader(findViewById(R.id.header), true, "Order ID : "+order_increement_id);
		}catch(Exception e){
			new GrocermaxBaseException("OrderDetail","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try{
//			EasyTracker.getInstance(this).activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
		}catch(Exception e){}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
//			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
		}catch(Exception e){}
	}

	private void changeDateFormat(String date){
		try {
//			date = "2015-09-19";
////			21/09/2015



//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//			DateFormat formatter3 = null;
//			Date convertedDate = null;
//			String yyyyMMdd = date;
//
//			formatter3 = new SimpleDateFormat("yyyy-MM-dd");
//			convertedDate = (Date) formatter.parse(yyyyMMdd);
//
//			System.out.println("Date from yyyyMMdd String in Java : " + convertedDate);    //Wed Sep 14 00:00:00 GMT+05:45 2011
//			String changedate = String.valueOf(convertedDate);
//			DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
//			Date date1 = (Date) formatter1.parse(changedate);
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(date1);
//			String strDay = "";
//			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//			if (dayOfWeek == 1) {
//				strDay = "Sunday";
//			} else if (dayOfWeek == 2) {
//				strDay = "Monday";
//			} else if (dayOfWeek == 3) {
//				strDay = "Tuesday";
//			} else if (dayOfWeek == 4) {
//				strDay = "Wednesday";
//			} else if (dayOfWeek == 5) {
//				strDay = "Thursday";
//			} else if (dayOfWeek == 6) {
//				strDay = "Friday";
//			} else if (dayOfWeek == 7) {
//				strDay = "Saturday";
//			}
//
//			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
//			Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)), cal.get(Calendar.DATE));
//			String strFinalDate = strDay + ", " + sdf.format(calendar.getTime());
//			System.out.println(strFinalDate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}






}
