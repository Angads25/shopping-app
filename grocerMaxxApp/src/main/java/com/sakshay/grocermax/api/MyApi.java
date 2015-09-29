package com.sakshay.grocermax.api;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonObject;
import com.sakshay.grocermax.api.ConnectionServiceParser.MyParserType;

public class MyApi {
	private Context m_context;

	public MyApi(Context context) {
		m_context = context;
	}

	/**
	 * This is used to login user
	 */
	public void reqSearchByCategory(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SEARCH_BY_CATEGORY);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE,MyParserType.SEARCH_BY_CATEGORY);
		m_context.startService(reqIntent);
	}

	/**
	 * This is used to login user
	 */
	public void reqLogin(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOGIN);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE,MyParserType.LOGIN);
		m_context.startService(reqIntent);
	}
	
	/**
	 * This is used to login user using json
	 */
	public void reqLogin(String url, HashMap<String, String> valuePairs) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOGIN);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.PAIRS, valuePairs);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.LOGIN);

//		HashMap<String, String> mHashMap = new HashMap<String,String>();
//		mHashMap.put("first","calues first");
//		mHashMap.put("second","ealues second");
//		mHashMap.put("third","talues third");
//		mHashMap.put("fourth","yalues fourth");
//		reqIntent.putExtra(ConnectionService.PAIRS, mHashMap);

		m_context.startService(reqIntent);
	}


	/**This is used to get register.
	 * @param url
	 * @param valuePairs
	 */
	public void reqUserRegistration(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.REGISTER_USER);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.REGISTRATION);
		m_context.startService(reqIntent);
	}

	/**This is used to get otp while register.
	 * @param url
	 * @param valuePairs
	 */
	public void reqUserRegistrationOTP(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.OTP);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.OTP_SUCCESSFULL);
		m_context.startService(reqIntent);
	}
	
	/**This is used to get register.
	 * @param url
	 * @param valuePairs
	 */
	public void reqForgetPassword(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.FORGOT_PWD);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.FORGOT_PWD);
		m_context.startService(reqIntent);
	}
	
	public void reqEditProfile(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.EDIT_PROFILE);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.EDIT_PROFILE);
		m_context.startService(reqIntent);
	}

	/**
	 * This is used to get register using json
	 */
//	public void reqUserRegistration(String url, String jsonString) {
//		HashMap<String, String> valuePairs = new HashMap<String, String>();
//		valuePairs.put("InputFormat", "application/Json");
//		valuePairs.put("InputData", jsonString);
//		valuePairs.put("MerchantId", UrlsConstants.MERCHANT_ID);
//		Intent reqIntent = new Intent(m_context, ConnectionService.class);
//		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.REGISTER_USER);
//		reqIntent.putExtra(ConnectionService.URL, url);
////		reqIntent.putExtra(ConnectionService.JSON_STRING, jsonString);
//		reqIntent.putExtra(ConnectionService.PAIRS, valuePairs);
//		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
//		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.REGISTRATION);
//		m_context.startService(reqIntent);
//	}
	
	/**This is used to get the user profile data.
	 * @param url
	 * @param valuePairs
	 */
	/*public void reqUserProfileData(String url, HashMap<String, String> valuePairs) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.USER_PROFILE_DATA);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.PAIRS, valuePairs);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PROFILE_DATA);
		m_context.startService(reqIntent);
	}*/
	
	/**
	 * This is used to get register using json
	 */
	public void reqCategoryList(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CATEGORY_LIST);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CATEGORY_LIST);
		m_context.startService(reqIntent);
	}
	
	public void reqCategorySubCategoryList(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CATEGORY_LIST);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CATEGORY_SUBCATEGORY_LIST);
		m_context.startService(reqIntent);
	}

	public void reqLocation(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOCATION);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.LOCATION);
		m_context.startService(reqIntent);
	}

	public void reqProductList(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.PRODUCT_LIST);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PRODUCT_LIST);
		m_context.startService(reqIntent);
	}
	public void reqSearchProductList(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SEARCH_PRODUCT_LIST);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SEARCH_PRODUCT_LIST);
		m_context.startService(reqIntent);
	}

	public void reqDealProductList(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.DEAL_PRODUCT_LIST);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.DEAL_PRODUCT_LIST);
		m_context.startService(reqIntent);
	}



	public void reqProductListFromHomeScreen(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.PRODUCT_LIST_FROM_HOME);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PRODUCT_LIST);
		m_context.startService(reqIntent);
	}
	
	public void reqUserDetails(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.USER_DETAILS);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.USER_DETAILS);
		m_context.startService(reqIntent);
	}
	public void reqUserDetails1(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.USER_DETAILS1);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.USER_DETAILS);
		m_context.startService(reqIntent);
	}
	
	public void reqProductContentList(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.PRODUCT_CONTENT_LIST);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PRODUCT_CONTENT_LIST);
		m_context.startService(reqIntent);
	}
	
	public void reqAddToCart(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
		m_context.startService(reqIntent);
	}

	public void reqAddToCartNewProduct(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
		m_context.startService(reqIntent);
	}

	public void reqAddToCartGuest(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
		m_context.startService(reqIntent);
	}
	
	public void reqDeleteFromCart(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.DELETE_FROM_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.DELETE_FROM_CART);
		m_context.startService(reqIntent);
	}
	
	public void reqActiveOrder(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CATEGORY_LIST);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CATEGORY_LIST);
		m_context.startService(reqIntent);
	}
	
	public void reqViewCart(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
		m_context.startService(reqIntent);
	}

	public void reqViewCartSlipErrorApp(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART_ERROR_ON_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
		m_context.startService(reqIntent);
	}
	
	public void reqViewCartGoHomeScreen(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART_GO_HOME_SCREEN);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
		m_context.startService(reqIntent);
	}
	
	public void reqViewCartAfterDelete(String url,String action) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
//		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CART_DETAIL_AFTER_DELETE);
		reqIntent.putExtra(ConnectionService.ACTION, action);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
		m_context.startService(reqIntent);
	}
	
//	public void reqEditCart(String url) {
//		Intent reqIntent = new Intent(m_context, ConnectionService.class);
//		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
////		reqIntent.putExtra(ConnectionService.ACTION, action);
//		reqIntent.putExtra(ConnectionService.URL, url);
////		reqIntent.putExtra(ConnectionService.PAIRS, jsonArray);
//		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
//		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART_UPDATE_LOCALLY);
//		m_context.startService(reqIntent);
//	}
	
//	public void reqEditCart(String url,JSONObject jsonObject) {
	public void reqEditCart(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
//		reqIntent.putExtra(ConnectionService.ACTION, action);
		reqIntent.putExtra(ConnectionService.URL, url);
//		HashMap<String, JSONObject> hashMap = new HashMap<String, JSONObject>();
//		hashMap.put(ConnectionService.JSON_OBJECT, jsonObject);
//		reqIntent.putExtra(ConnectionService.JSON_OBJECT, hashMap);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable(ConnectionService.JSON_OBJECT, hashMap);
//		reqIntent.putExtras(bundle);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable(ConnectionService.JSON_OBJECT, jsonObject.toString());
//		reqIntent.putExtras(bundle);
//		reqIntent.putExtra(ConnectionService.JSON_OBJECT, jsonObject.toString());
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
//		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART_UPDATE_LOCALLY);
		m_context.startService(reqIntent);
	}

	public void reqEditCartBackToCart(String url ,String action) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, action);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART_UPDATE_LOCALLY);
		m_context.startService(reqIntent);
	}
	
	public void reqOrderHistory(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ORDER_HISTORY);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ORDER_HISTORY);
		m_context.startService(reqIntent);
	}
	public void reqOrderDetail(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ORDER_DETAIL);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ORDER_DETAIL);
		m_context.startService(reqIntent);
	}
	
	public void reqAddressBook(String url, String action) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, action);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADDRESS_BOOK);
		m_context.startService(reqIntent);
	}
	
	public void reqEditAddress(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.EDIT_ADDRESS);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_ADDRESS);
		m_context.startService(reqIntent);
	}
	
	public void reqAddAddress(String url,String action) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, action);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_ADDRESS);
		m_context.startService(reqIntent);
	}
	
	public void reqDeleteAddress(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.DELETE_ADDRESS);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.DELETE_ADDRESS);
		m_context.startService(reqIntent);
	}

	
	public void reqFinalCheckout(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.FINAL_CHECKOUT);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.FINAL_CHECKOUT);
		m_context.startService(reqIntent);
	}
public void reqCheckOutAddress(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CHECKOUT_ADDRESS);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CHECKOUT_ADDRESS);
		m_context.startService(reqIntent);
	}

public void reqGetOrderStatus(String url) {
	Intent reqIntent = new Intent(m_context, ConnectionService.class);
	reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.GET_ORDER_STATUS);
	reqIntent.putExtra(ConnectionService.URL, url);
	reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
	reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.GET_SET_ORDERSTATUS);
	m_context.startService(reqIntent);
}
public void reqSetOrderStatus(String url) {                                            //using in failed condition of payu and paytm
	Intent reqIntent = new Intent(m_context, ConnectionService.class);
	reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SET_ORDER_STATUS);
	reqIntent.putExtra(ConnectionService.URL, url);
	reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
	reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.GET_SET_ORDERSTATUS);
	m_context.startService(reqIntent);
}

public void reqSetOrderStatusPaytmSuccess(String url) {
	Intent reqIntent = new Intent(m_context, ConnectionService.class);
	reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SET_PAYTM_ORDER_STATUS_SUCCESS);
	reqIntent.putExtra(ConnectionService.URL, url);
	reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
	reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SET_PAYTM_ORDER_STATUS_SUCCESS);
	m_context.startService(reqIntent);
}


	public void reqBackGroundAddToCartGuest(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionCartService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ADD_TO_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_TO_CART);
		m_context.startService(reqIntent);
	}

	public void reqBackgroundAddToCartNewProduct(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionCartService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ADD_TO_CART);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_TO_CART);
		m_context.startService(reqIntent);
	}


}
