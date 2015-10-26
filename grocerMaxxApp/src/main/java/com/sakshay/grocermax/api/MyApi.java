package com.sakshay.grocermax.api;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonObject;
import com.sakshay.grocermax.api.ConnectionServiceParser.MyParserType;
import com.sakshay.grocermax.exception.GrocermaxBaseException;

public class MyApi {
	private Context m_context;

	public MyApi(Context context) {
		m_context = context;
	}

	/**
	 * This is used to login user
	 */
	public void reqSearchByCategory(String url) {
		try {
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SEARCH_BY_CATEGORY);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE,MyParserType.SEARCH_BY_CATEGORY);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqSearchByCategory",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	/**
	 * This is used to login user
	 */
	public void reqLogin(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOGIN);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE,MyParserType.LOGIN);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqLogin",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}


//	/**
//	 * This is used to login user using json
//	 */
//	public void reqLogin(String url, HashMap<String, String> valuePairs) {
//		try{
//			Intent reqIntent = new Intent(m_context, ConnectionService.class);
//			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOGIN);
//			reqIntent.putExtra(ConnectionService.URL, url);
//			if(valuePairs.size() > 0){
//				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
//				reqIntent.putExtra(ConnectionService.PAIRS, valuePairs);
//			}else{
//				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
//			}
//			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.LOGIN);
//			m_context.startService(reqIntent);
//		}catch(Exception e){
//			new GrocermaxBaseException("MyApi","reqLoginPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+valuePairs);
//		}
//	}


	/**
	 * This is used to login user using json
	 */
	public void reqLogin(String url, JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOGIN);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.LOGIN);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqLoginPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+jsonObject);
		}
	}



	/**This is used to get register.
	 * @param url
	 * @param valuePairs
	 */
	public void reqUserRegistration(String url,JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.REGISTER_USER);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.REGISTRATION);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqUserRegistration",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqUserRegistrationOTP(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.OTP);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.OTP_SUCCESSFULL);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqUserRegistrationOTP",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}


	/**This is used to get otp while register.
	 * @param url
	 * @param valuePairs
	 */
//	public void reqUserRegistrationOTP(String url, HashMap<String, String> valuePairs) {
//		try{
//			Intent reqIntent = new Intent(m_context, ConnectionService.class);
//			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.OTP);
//			reqIntent.putExtra(ConnectionService.URL, url);
//			reqIntent.putExtra(ConnectionService.PAIRS, valuePairs);
//			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
//			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.OTP_SUCCESSFULL);
//			m_context.startService(reqIntent);
//		}catch(Exception e){
//			new GrocermaxBaseException("MyApi","reqUserRegistrationOTPPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+valuePairs);
//		}
//	}

	public void reqUserRegistrationOTP(String url, JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.OTP);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.OTP_SUCCESSFULL);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqUserRegistrationOTPPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+jsonObject);
		}
	}
	
	/**This is used to get register.
	 * @param url
	 * @param valuePairs
	 */
//	public void reqForgetPassword(String url) {
//		try{
//			Intent reqIntent = new Intent(m_context, ConnectionService.class);
//			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.FORGOT_PWD);
//			reqIntent.putExtra(ConnectionService.URL, url);
//			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
//			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.FORGOT_PWD);
//			m_context.startService(reqIntent);
//		}catch(Exception e){
//			new GrocermaxBaseException("MyApi","reqForgetPassword",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
//		}
//	}

	public void reqForgetPassword(String url,JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.FORGOT_PWD);
			reqIntent.putExtra(ConnectionService.URL, url);

			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.FORGOT_PWD);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqForgetPassword",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqEditProfile(String url,JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.EDIT_PROFILE);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.EDIT_PROFILE);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqEditProfile",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
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
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CATEGORY_LIST);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CATEGORY_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqCategoryList",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqCategorySubCategoryList(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CATEGORY_LIST);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CATEGORY_SUBCATEGORY_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqCategorySubCategoryList",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqLocation(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOCATION);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.LOCATION);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqLocation",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

//	public void reqLocationBackground(String url) {               //hit when start the app in background not to come on listener
//		try{
//			Intent reqIntent = new Intent(m_context, ConnectionService.class);
////			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.LOCATION);
//			reqIntent.putExtra(ConnectionService.URL, url);
//			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
//			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.LOCATION);
//			m_context.startService(reqIntent);
//		}catch(Exception e){
//			new GrocermaxBaseException("MyApi","reqLocation",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
//		}
//	}

	public void reqProductList(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.PRODUCT_LIST);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PRODUCT_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqProductList",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	public void reqSearchProductList(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SEARCH_PRODUCT_LIST);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SEARCH_PRODUCT_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqSearchProductList",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqDealProductList(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.DEAL_PRODUCT_LIST);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.DEAL_PRODUCT_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqDealProductList",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqProductListFromHomeScreen(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.PRODUCT_LIST_FROM_HOME);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PRODUCT_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqProductListFromHomeScreen",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqUserDetails(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.USER_DETAILS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.USER_DETAILS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqUserDetails",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	public void reqUserDetails1(String url) {
		try {
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.USER_DETAILS1);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.USER_DETAILS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqUserDetails1",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqProductContentList(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.PRODUCT_CONTENT_LIST);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PRODUCT_CONTENT_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqProductContentList",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqAddToCart(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqAddToCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqAddToCartNewProduct(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqAddToCartNewProduct",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqAddToCartGuest(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqAddToCartGuest",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqDeleteFromCart(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.DELETE_FROM_CART);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.DELETE_FROM_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqDeleteFromCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqAllProductsCategory(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ALL_PRODUCTS_CATEGORY);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ALL_PRODUCTS_CATEGORY);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqAllProductsCategory",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqActiveOrder(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CATEGORY_LIST);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CATEGORY_LIST);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqActiveOrder",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqViewCart(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqViewCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqViewCartSlipErrorApp(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART_ERROR_ON_CART);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqViewCartSlipErrorApp",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqViewCartGoHomeScreen(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART_GO_HOME_SCREEN);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqViewCartGoHomeScreen",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqViewCartAfterDelete(String url,String action) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
	//		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CART_DETAIL_AFTER_DELETE);
			reqIntent.putExtra(ConnectionService.ACTION, action);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqViewCartAfterDelete",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
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


	public void reqEditCart(String url, JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.VIEW_CART_UPDATE_LOCALLY);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqEditCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}


//	public void reqEditCart(String url,JSONObject jsonObject) {
	public void reqEditCart(String url) {
		try{
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
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqEditCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqEditCartBackToCart(String url ,String action,JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, action);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART_UPDATE_LOCALLY);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqEditCartBackToCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}


	public void reqEditCartBackToCart(String url ,String action) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, action);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.VIEW_CART_UPDATE_LOCALLY);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqEditCartBackToCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqOrderHistory(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ORDER_HISTORY);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ORDER_HISTORY);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqOrderHistory",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	public void reqOrderDetail(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ORDER_DETAIL);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ORDER_DETAIL);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqOrderDetail",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqAddressBook(String url, String action) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, action);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADDRESS_BOOK);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqAddressBook",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqEditAddress(String url, HashMap<String, String> valuePairs) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.EDIT_ADDRESS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.PAIRS, valuePairs);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_ADDRESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqEditAddressPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+valuePairs);
		}
	}

	public void reqEditAddress(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.EDIT_ADDRESS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_ADDRESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqEditAddress",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

//	public void reqAddAddress(String url,String action,HashMap<String, String> valuePairs) {
//		try{
//			Intent reqIntent = new Intent(m_context, ConnectionService.class);
//			reqIntent.putExtra(ConnectionService.ACTION, action);
//			reqIntent.putExtra(ConnectionService.URL, url);
//			reqIntent.putExtra(ConnectionService.PAIRS, valuePairs);
//			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
//			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_ADDRESS);
//			m_context.startService(reqIntent);
//		}catch(Exception e){
//			new GrocermaxBaseException("MyApi","reqAddAddressPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+valuePairs);
//		}
//	}

	public void reqAddAddress(String url,String action,JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, action);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_ADDRESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqAddAddressPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+jsonObject);
		}
	}


	public void reqAddAddress(String url,String action) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, action);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_ADDRESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqAddAddress",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	
	public void reqDeleteAddress(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.DELETE_ADDRESS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.DELETE_ADDRESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqDeleteAddress",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqFinalCheckout(String url,JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.FINAL_CHECKOUT);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.FINAL_CHECKOUT);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqFinalCheckoutPOST",e.getMessage(), GrocermaxBaseException.EXCEPTION,url+jsonObject);
		}
	}
	
	public void reqFinalCheckout(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.FINAL_CHECKOUT);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.FINAL_CHECKOUT);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqFinalCheckout",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqCheckOutAddress(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.CHECKOUT_ADDRESS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.CHECKOUT_ADDRESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqCheckOutAddress",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
		}

	public void reqGetOrderStatus(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.GET_ORDER_STATUS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.GET_SET_ORDERSTATUS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqGetOrderStatus",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}
	public void reqSetOrderStatus(String url) {                                            //using in failed condition of payu and paytm
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SET_ORDER_STATUS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.GET_SET_ORDERSTATUS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqSetOrderStatus",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqSetOrderStatus(String url,JSONObject jsonObject) {                                            //using in failed condition of payu and paytm
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SET_ORDER_STATUS);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.GET_SET_ORDERSTATUS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqSetOrderStatus",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqSetOrderStatusPaytmSuccess(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SET_PAYTM_ORDER_STATUS_SUCCESS);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SET_PAYTM_ORDER_STATUS_SUCCESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqSetOrderStatusPaytmSuccess",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqSetOrderStatusPaytmSuccess(String url,JSONObject jsonObject) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SET_PAYTM_ORDER_STATUS_SUCCESS);
			reqIntent.putExtra(ConnectionService.URL, url);
			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SET_PAYTM_ORDER_STATUS_SUCCESS);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqSetOrderStatusPaytmSuccess",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}


//	public void reqSetOrderStatusPaytmSuccess(String url,JSONObject jsonObject) {
//		try{
//			Intent reqIntent = new Intent(m_context, ConnectionService.class);
//			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.SET_PAYTM_ORDER_STATUS_SUCCESS);
//			reqIntent.putExtra(ConnectionService.URL, url);
//			if(jsonObject.length() > 0){
//				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
//				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
//			}else{
//				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
//			}
//			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SET_PAYTM_ORDER_STATUS_SUCCESS);
//			m_context.startService(reqIntent);
//		}catch(Exception e){
//			new GrocermaxBaseException("MyApi","reqSetOrderStatusPaytmSuccess",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
//		}
//	}


//	public void reqBackGroundAddToCartGuest(String url) {
//		try{
//			Intent reqIntent = new Intent(m_context, ConnectionCartService.class);
//			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ADD_TO_CART);
//			reqIntent.putExtra(ConnectionService.URL, url);
//			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
//			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_TO_CART);
//			m_context.startService(reqIntent);
//		}catch(Exception e){
//			new GrocermaxBaseException("MyApi","reqBackGroundAddToCartGuest",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
//		}
//	}

//	public void reqBackGroundAddToCartGuest(String url,HashMap<String, String> valuePairs) {
//	public void reqBackGroundAddToCartGuest(String url,JSONArray products) {
	public void reqBackGroundAddToCartGuest(String url,JSONObject jsonObject) {

		try{
			Intent reqIntent = new Intent(m_context, ConnectionCartService.class);
//			Intent reqIntent = new Intent(m_context, ConnectionService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ADD_TO_CART);
			reqIntent.putExtra(ConnectionService.URL, url);

			System.out.println("====productse===="+jsonObject);

			if(jsonObject.length() > 0){
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "POST");
				reqIntent.putExtra(ConnectionService.JSON_STRING, String.valueOf(jsonObject));
			}else{
				reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			}
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_TO_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqBackGroundAddToCartGuest",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqBackgroundAddToCartNewProduct(String url) {
		try{
			Intent reqIntent = new Intent(m_context, ConnectionCartService.class);
			reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.ADD_TO_CART);
			reqIntent.putExtra(ConnectionService.URL, url);
			reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
			reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.ADD_TO_CART);
			m_context.startService(reqIntent);
		}catch(Exception e){
			new GrocermaxBaseException("MyApi","reqBackgroundAddToCartNewProduct",e.getMessage(), GrocermaxBaseException.EXCEPTION,url);
		}
	}

	public void reqGetShopByCategories(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.GET_SHOP_BY_CATEGORIES);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SHOP_BY_CATEGORY_LIST);
		m_context.startService(reqIntent);
	}
	public void reqGetShopByDeals(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.GET_SHOP_BY_DEALS);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.SHOP_BY_DEALS_LIST);
		m_context.startService(reqIntent);
	}
	public void reqGetHomePageBanner(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.GET_BANNER);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.HOME_BANNER);
		m_context.startService(reqIntent);
	}
	public void reqDealByDealType(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.DEAL_BY_DEALTYPE);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.DEAL_BY_DEAL_TYPE);
		m_context.startService(reqIntent);
	}
	public void reqProductListingByDealType(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.PRODUCT_LISTING_BY_DEALTYPE);
		m_context.startService(reqIntent);
	}
	public void reqOfferByDealType(String url) {
		Intent reqIntent = new Intent(m_context, ConnectionService.class);
		reqIntent.putExtra(ConnectionService.ACTION, MyReceiverActions.OFFER_BY_DEALTYPE);
		reqIntent.putExtra(ConnectionService.URL, url);
		reqIntent.putExtra(ConnectionService.HTTP_REQUEST_TYPE, "GET");
		reqIntent.putExtra(ConnectionService.PARSE_TYPE, MyParserType.OFFER_BY_DEALTYPE);
		m_context.startService(reqIntent);
	}

}
