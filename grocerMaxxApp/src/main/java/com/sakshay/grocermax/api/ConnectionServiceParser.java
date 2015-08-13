package com.sakshay.grocermax.api;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sakshay.grocermax.CartProductList;
import com.sakshay.grocermax.OffersPromoCode;
import com.sakshay.grocermax.UpdateCartbg;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.AddressList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.CartDetailBean;
import com.sakshay.grocermax.bean.CategoryListBean;
import com.sakshay.grocermax.bean.CheckoutAddressBean;
import com.sakshay.grocermax.bean.FinalCheckoutBean;
import com.sakshay.grocermax.bean.LoginResponse;
import com.sakshay.grocermax.bean.OrderHistoryBean;
import com.sakshay.grocermax.bean.OrderReviewBean;
import com.sakshay.grocermax.bean.OrderedProductList;
import com.sakshay.grocermax.bean.PersonalInfo;
import com.sakshay.grocermax.bean.ProductDetailsListBean;
import com.sakshay.grocermax.bean.ProductListBean;
import com.sakshay.grocermax.bean.UserDetailBean;
import com.sakshay.grocermax.preference.MySharedPrefs;

public class ConnectionServiceParser {

	public interface MyParserType {
		
		int SEARCH_PRODUCT_LIST=99;
		int CATEGORY_SUBCATEGORY_LIST = 100;
		int LOGIN = 101;
		int REGISTRATION = 102;
		int FORGOT_PWD = 103;
		int USER_DETAILS = 104;
		int CHANGE_PWD = 105;
		int CATEGORY_LIST = 106;
		int PRODUCT_LIST = 107;
		int PRODUCT_CONTENT_LIST = 108;
		int ADD_TO_CART = 109;
		int VIEW_CART = 110;
		int ORDER_HISTORY = 111;
		int ADDRESS_BOOK = 112;
		int ADD_ADDRESS = 113;
		int FINAL_CHECKOUT = 114;
		int DELETE_FROM_CART = 115;
		int CHECKOUT_ADDRESS = 116;
		int DELETE_ADDRESS = 117;
		int ORDER_DETAIL = 118;
		int EDIT_PROFILE = 119;
		int GET_SET_ORDERSTATUS = 120;
		int VIEW_CART_UPDATE_LOCALLY = 121;
	}

	public static BaseResponseBean parseSimpleResponse(String jsonString)
			throws JSONException {
		Gson gson = new Gson();
		//BaseResponseBean responseBean = gson.fromJson(jsonString,BaseResponseBean.class);
		LoginResponse responseBean = gson.fromJson(jsonString,LoginResponse.class);
		return responseBean;
	}
	
	

	public static LoginResponse parseLoginResponse(String jsonString)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		Gson gson = new Gson();
		LoginResponse bean = gson.fromJson(jsonString, LoginResponse.class);
		return bean;
	}

	public static CategoryListBean parseCategoryResponse(String jsonString)
			throws JSONException {
		Gson gson = new Gson();
		CategoryListBean categoryBean = gson.fromJson(jsonString,
				CategoryListBean.class);
		return categoryBean;
	}

	public static ProductListBean parseProductResponse(String jsonString)
			throws JSONException {
		Gson gson = new Gson();
		ProductListBean productListBean = gson.fromJson(jsonString,ProductListBean.class);
		return productListBean;
	}

	public static ProductDetailsListBean parseProductContentResponse(
			String jsonString) throws JSONException {
		Gson gson = new Gson();
		ProductDetailsListBean contentListBean = gson.fromJson(jsonString,
				ProductDetailsListBean.class);
		return contentListBean;
	}

	public static CartDetailBean parseViewCartResponse(String jsonString)
			throws JSONException {

		String str = new JSONObject(jsonString).getString("shippingChargeLimit").toString();
		CartProductList.strShippingChargeLimit = new JSONObject(jsonString).getString("shippingChargeLimit").toString();
		jsonString=new JSONObject(jsonString).getJSONObject("CartDetail").toString();
		OrderReviewBean orderReviewBean=new OrderReviewBean();
		orderReviewBean.setTax_ammount(new JSONObject(jsonString).getJSONObject("shipping_address").optString("tax_amount"));
		orderReviewBean.setShipping_ammount(new JSONObject(jsonString).getJSONObject("shipping_address").optString("shipping_amount"));
		orderReviewBean.setGrandTotal(new JSONObject(jsonString).getString("grand_total"));
//		orderReviewBean.setGrandTotal(new JSONObject(jsonString).getString("grand_total"));
		orderReviewBean.setSubTotal(new JSONObject(jsonString).getString("subtotal"));
		orderReviewBean.setCouponCode(new JSONObject(jsonString).getString("coupon_code"));
		orderReviewBean.setCouponSubtotalWithDiscount(new JSONObject(jsonString).getString("subtotal_with_discount"));
		
//		CartProductList.getInstance().strCouponCode = new JSONObject(jsonString).getString("coupon_code");
//		CartProductList.getInstance().strSubTotal = new JSONObject(jsonString).getString("subtotal"); 
//		CartProductList.getInstance().strSubtotalWithDiscount = new JSONObject(jsonString).getString("subtotal_with_discount");
		
		orderReviewBean.setDiscount_amount(new JSONObject(jsonString).getJSONObject("shipping_address").optString("discount_amount"));
		MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);
			
		Gson gson = new Gson();
		CartDetailBean cartBean = gson.fromJson(jsonString,CartDetailBean.class);
		return cartBean;
	}
	static String strDiscount="";
	///// parse there b/c updated cart values in OrderReviewBean used in shared preference for updating the values of cart///// 
	//  useful when user moves to timing slot screen it updates the data of cart which need to send the data to Review order and Pay screen  //
	public static CartDetailBean parseViewCartResponseLocally(String jsonString)
			throws JSONException {
		CartDetailBean cartBean = null;
		try
		{
			UpdateCartbg.getInstance().bLocally = false;
			jsonString=new JSONObject(jsonString).getJSONObject("CartDetail").toString();
			
	//		OrderReviewBean orderReviewBean=new OrderReviewBean();
			OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
			orderReviewBean.setTax_ammount(new JSONObject(jsonString).getJSONObject("shipping_address").optString("tax_amount"));
			orderReviewBean.setShipping_ammount(new JSONObject(jsonString).getJSONObject("shipping_address").optString("shipping_amount"));
			orderReviewBean.setGrandTotal(new JSONObject(jsonString).getString("grand_total"));
//			orderReviewBean.setGrandTotal(new JSONObject(jsonString).getString("grand_total"));
			orderReviewBean.setSubTotal(new JSONObject(jsonString).getString("subtotal"));
			orderReviewBean.setCouponCode(new JSONObject(jsonString).getString("coupon_code"));
			orderReviewBean.setCouponSubtotalWithDiscount(new JSONObject(jsonString).getString("subtotal_with_discount"));
			
//			CartProductList.getInstance().strCouponCode = new JSONObject(jsonString).getString("coupon_code");
//			CartProductList.getInstance().strSubTotal = new JSONObject(jsonString).getString("subtotal"); 
//			CartProductList.getInstance().strSubtotalWithDiscount = new JSONObject(jsonString).getString("subtotal_with_discount");
			
			strDiscount = new JSONObject(jsonString).getJSONObject("shipping_address").optString("discount_amount");
			orderReviewBean.setDiscount_amount(strDiscount);
			MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);

		
		Gson gson = new Gson();
		cartBean = gson.fromJson(jsonString,CartDetailBean.class);
		
//		UtilityMethods.deleteCloneCart(BaseActivity.activity);
//		for(int i=0;i<cartBean.getItems().size();i++)
//		{
//			UtilityMethods.writeCloneCart(BaseActivity.activity, Constants.localCloneFile, cartBean.getItems().get(i));
//		}		
		
	    ArrayList<CartDetail> cartList;  
		cartList = cartBean.getItems();
		if(cartList != null && cartList.size() > 0)
		{
			if(cartBean!=null)
				{
					float saving=0;
					for(int i=0;i<cartList.size();i++)
					{
						saving=saving+(cartList.get(i).getQty()*(Float.parseFloat(cartList.get(i).getMrp())-Float.parseFloat(cartList.get(i).getPrice())));
					}

					if(!strDiscount.equals("") && !strDiscount.equals(" ")) {
						saving = saving - (Float.parseFloat(strDiscount));
					}

//					if(MySharedPrefs.INSTANCE.getTotalItem()!=null)
//					{
//						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf((int)Float.parseFloat(cartBean.getItems_qty())));
	//							BaseActivity.cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());       //can't update b/c it's on UI thread
//					}
					OrderReviewBean orderReviewBean1=MySharedPrefs.INSTANCE.getOrderReviewBean();
			    	orderReviewBean1.setProduct(cartList);
//			    	orderReviewBean1.setSubTotal(cartBean.getGrandTotal());
			    	orderReviewBean1.setSaving(String.valueOf(saving));                      //think will use when Order and review Pay for displaying order data
			    	MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean1);		    	
				}
		}
		}catch(Exception e){}
		return cartBean;
	}
	///// parse there b/c updated cart values in OrderReviewBean used in shared preference for updating the values of cart/////

	public static UserDetailBean parseUserDetailsResponse(String jsonString)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		Gson gson = new Gson();
		UserDetailBean bean = new UserDetailBean();
		PersonalInfo personalInfo = gson.fromJson(
				jsonObject.optString("Personal Info"), PersonalInfo.class);
		Address shippingAddress = gson.fromJson(
				jsonObject.optString("ShippingAddress"), Address.class);
		Address billingAddress = gson.fromJson(
				jsonObject.optString("BillingAddress"), Address.class);
		bean.setPersonalInfo(personalInfo);
		bean.setShippingAddress(shippingAddress);
		bean.setBillingAddress(billingAddress);
		return bean;
	}

	public static OrderHistoryBean parseOrderHistoryResponse(String jsonString)
			throws JSONException {
		Gson gson = new Gson();
		OrderHistoryBean bean = gson.fromJson(jsonString,
				OrderHistoryBean.class);
		return bean;
	}

	public static AddressList parseAddressBookResponse(String jsonString)
			throws JSONException {
		Gson gson = new Gson();
		AddressList bean = gson.fromJson(jsonString, AddressList.class);
		return bean;
	}

	public static FinalCheckoutBean parseFinalCheckoutResponse(String jsonString)
			throws JSONException {
		Gson gson = new Gson();
		FinalCheckoutBean bean = gson.fromJson(jsonString,
				FinalCheckoutBean.class);
		return bean;
	}

	public static CheckoutAddressBean parseCheckoutAddressResponse(
			String jsonString) throws JSONException {
		Gson gson = new Gson();
		CheckoutAddressBean bean = gson.fromJson(jsonString,CheckoutAddressBean.class);

		JSONObject response = new JSONObject(jsonString);
		JSONArray shipping_obj = response.getJSONArray("Shipping");
		HashMap<String,ArrayList<String>> date_timeSlot=new HashMap<String, ArrayList<String>>();
		for(int i=0;i<shipping_obj.length();i++)
		{
			JSONObject slot_obj=shipping_obj.getJSONObject(i);
			if(date_timeSlot.containsKey(slot_obj.getString("Date")))
			{
				ArrayList<String> timeSlot=date_timeSlot.get(slot_obj.getString("Date"));
				timeSlot.add(slot_obj.getString("TimeSlot"));
				date_timeSlot.put(slot_obj.getString("Date"), timeSlot);
			}else{
				ArrayList<String> timeSlot=new ArrayList<String>();
				timeSlot.add(slot_obj.getString("TimeSlot"));
				date_timeSlot.put(slot_obj.getString("Date"), timeSlot);
			}
		}
		bean.setDate_timeSlot(date_timeSlot);
		
		return bean;
	}

}
