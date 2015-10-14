package com.sakshay.grocermax.utils;

/**
 * Class to Hold the Url AppConstants
 */
public class UrlsConstants {
	// public final static String
	// BASE_URL="http://www.beta.grocermax.com/webservice/";
	// public final static String BASE_URL="http://54.164.75.220/webservice/";

	// test url
	// public final static String BASE_URL =
	// "http://54.174.241.173/webservice/";	

	//	public static final String imagePic = "http://3.bp.blogspot.com/-NBE-2uL3Vjo/UnjtecEkx0I/AAAAAAAAJTU/20EvZblLCLQ/s640/1831359.jpg";
	public static final String imagePic = "https://i.vimeocdn.com/portrait/1049808_300x300.jpg";

	// uat url
//	public final static String BASE_URL = "http://uat.grocermax.com/webservice/";
//	public final static String NEW_BASE_URL = "http://uat.grocermax.com/webservice/new_services/";

	// dev url
//	public final static String BASE_URL = "http://dev.grocermax.com/webservice/";
//	public final static String NEW_BASE_URL = "http://dev.grocermax.com/webservice/new_services/";

	//staging
//	public final static String BASE_URL = "http://staging.grocermax.com/webservice/";
//	public final static String NEW_BASE_URL = "http://staging.grocermax.com/webservice/new_services/";

//	public final static String BASE_URL = "http://staging.grocermax.com/api/";
//	public final static String NEW_BASE_URL = "http://staging.grocermax.com/api/";

	// live url
//	public final static String BASE_URL = "http://grocermax.com/webservice/";
//	public final static String NEW_BASE_URL = "http://grocermax.com/webservice/new_services/";

	///////////////////////////////////////NEW URL'S
//	public final static String BASE_URL = "http://grocermax.com/gmx_api.php/";
//	public final static String NEW_BASE_URL = "http://grocermax.com/gmx_api.php/";
	///////////////////////////////////////NEW URL'S

	public final static String BASE_URL = "https://grocermax.com/api/";
	public final static String NEW_BASE_URL = "https://grocermax.com/api/";

//	public final static String BASE_URL = "http://api.grocermax.com/gmx_api.php/";
//	public final static String NEW_BASE_URL = "http://api.grocermax.com/gmx_api.php/";

	// dev url
//	public final static String BASE_URL = "http://dev.api.grocermax.com/";
//	public final static String NEW_BASE_URL = "http://dev.api.grocermax.com/";

	/*public final static String CATEGORY_LISTING_URL = BASE_URL
			+ "category.php?parentid=";*/

	/*public final static String CATEGORY_COLLECTION_LISTING_URL = BASE_URL
			+ "categorycollection.php";*/

//	http://staging.grocermax.com/webservice/new_services/getlocation

//	http://dev.grocermax.com/webservice/new_services/getstate

//	http://staging.grocermax.com/webservice/new_services/dealproductlisting

	public final static String LOGIN_URL = NEW_BASE_URL + "login?";

//	public final static String LOGIN_URL = NEW_BASE_URL + "loginnew";

	public final static String GET_ALL_PRODUCTS_OF_CATEGORY = NEW_BASE_URL + "productlistall?cat_id=";

	public final static String GET_DEAL_LISTING = NEW_BASE_URL                                   //FOR GETTING STATE OF BILLING
			+ "dealproductlisting";

	public final static String GET_STATE = NEW_BASE_URL                                   //FOR GETTING STATE OF BILLING
			+ "getstate";

	public final static String GET_LOCATION_SHIPPING = NEW_BASE_URL                                   //FOR GETTING STATE OF SHIPPING LOCATION
			+ "getlocality?cityid=";

	public final static String GET_LOCATION = NEW_BASE_URL
			+ "getlocation";

	public final static String CATEGORY_COLLECTION_LISTING_URL = NEW_BASE_URL
			+ "category";

	/*public final static String PRODUCT_LIST_URL = BASE_URL
			+ "product.php?cat_id=";*/
	public final static String PRODUCT_LIST_URL = NEW_BASE_URL
			+ "productlist?cat_id=";

	/*public final static String PRODUCT_DETAIL_URL = BASE_URL
			+ "product.php?pro_id=";*/
	public final static String PRODUCT_DETAIL_URL = NEW_BASE_URL
			+ "productdetail?pro_id=";

	public final static String ADD_COUPON = NEW_BASE_URL
			+ "addcoupon?userid=";

	public final static String REMOVE_COUPON = NEW_BASE_URL
			+ "removecoupon?userid=";

	public final static String ERROR_REPORT = NEW_BASE_URL
			+ "errorlog?error=";

	//public final static String LOGIN_URL = BASE_URL + "login.php?";// uemail=suman.ditm07@gmail.com&password=XHqvaHnW



	public final static String FB_LOGIN_URL = NEW_BASE_URL + "fbregister?";

	public final static String GOOGLE_LOGIN_URL = NEW_BASE_URL + "fbregister?";

	/*public final static String REGESTRATION_URL = BASE_URL
			+ "registeration.php?";*/

//	public final static String REGESTRATION_URL_OTP = NEW_BASE_URL                               //use when first time hits the registeration service and getting otp
//			+ "createuser?otp=0&";
	public final static String REGESTRATION_URL_OTP = NEW_BASE_URL                               //use when first time hits the registeration service and getting otp
		+ "createuser";

//	public final static String REGESTRATION_URL = NEW_BASE_URL                                    //use when first time hits the registeration service and after match otp by user and coming from server
//			+ "createuser?otp=1&";

	public final static String REGESTRATION_URL = NEW_BASE_URL                                    //use when first time hits the registeration service and after match otp by user and coming from server
			+ "createuser";

	public final static String EDIT_USER_URL = BASE_URL
			+ "editprofile.php?UserID=";// 166&fname=Suman&uemail=suman.tripath333i@sakshay.in

	/*public final static String FORGET_PASSWORD_URL = BASE_URL
			+ "forgotpassword.php?uemail=";// suman.tripathi@sakshay.in
*/
//	public final static String FORGET_PASSWORD_URL = NEW_BASE_URL
//			+ "forgotpassword?uemail=";

	public final static String FORGET_PASSWORD_URL = NEW_BASE_URL
			+ "forgotpassword";

	public final static String CHANGE_PASSWORD_URL = NEW_BASE_URL
			+ "changepassword?";// 278&password=happysuman&old_password=happy

	public final static String EDIT_PROFILE_URL = NEW_BASE_URL
			+ "editprofile?";

	/*public final static String USER_DETAIL_URL = BASE_URL
			+ "userdetail.php?UserID=";*/
	public final static String USER_DETAIL_URL = NEW_BASE_URL
			+ "userdetail?userid=";

	/*public final static String ADD_TO_CART_URL = BASE_URL
			+ "addtocart.php?cus_id="; // 229&products=[{%22productid%22:3190,%22quantity%22:3},{%22productid%22:3191,%22quantity%22:4}]
*/
	public final static String ADD_TO_CART_URL = NEW_BASE_URL
			+ "addtocart?cus_id=";

	public final static String ADD_TO_CART_GUEST_URL = NEW_BASE_URL
			+ "addtocartgust?";
	
	/*public static final String DELETE_FROM_CART_URL = BASE_URL
			+ "deleteitem.php?UserID=";*/

	public static final String DELETE_FROM_CART_URL = NEW_BASE_URL
			+ "deleteitem?userid=";

	/*public final static String VIEW_CART_URL = BASE_URL
			+ "viewcart.php?UserID=";*/

	public final static String VIEW_CART_URL = NEW_BASE_URL
			+ "cartdetail?userid=";

	public final static String UPDATE_CART_URL = NEW_BASE_URL
			+ "updatecart?cus_id=";

	/*public final static String ORDER_HISTORY_URL = BASE_URL
			+ "orderhistory.php?email=";*/
	public final static String ORDER_HISTORY_URL = NEW_BASE_URL
			+ "orderhistory?email=";

	public final static String ORDER_DETAIL_URL = NEW_BASE_URL
			+ "getorderdetail?orderid=";

	public final static String ACTIVE_ORDER_URL = BASE_URL
			+ "activeorder.php?email=";

	/*public final static String ADDRESS_BOOK = BASE_URL
			+ "addressdetail.php?UserID=";*/
	public final static String ADDRESS_BOOK = NEW_BASE_URL
			+ "getaddress?userid=";

	/*public final static String ADD_ADDRESS = BASE_URL
			+ "changeaddress.php?new_billing=";*/


	public final static String ADD_ADDRESS = NEW_BASE_URL
			+ "addaddress?";

	public final static String DELETE_ADDRESS = NEW_BASE_URL
			+ "deleteaddress?addressid=";

	/*public final static String EDIT_ADDRESS = BASE_URL
			+ "editaddress.php?UserID=";*/

	public final static String EDIT_ADDRESS = NEW_BASE_URL
			+ "editaddress?";

//	public final static String FINAL_CHECKOUT = NEW_BASE_URL
//			+ "checkout?shipping=";
	public final static String FINAL_CHECKOUT = NEW_BASE_URL
		+ "checkout";

	/*public final static String SEARCH_PRODUCT = BASE_URL + "search.php?name=";*/

	public final static String SEARCH_PRODUCT = NEW_BASE_URL + "search?keyword=";

	/*public final static String CHECKOUT_ADDRESS_BOOK = BASE_URL
			+ "addressdetail1.php?UserID=";*/
	public final static String CHECKOUT_ADDRESS_BOOK = NEW_BASE_URL
			+ "getaddresswithtimeslot?userid=";

	public static final String CHANGE_ORDER_STATUS = NEW_BASE_URL;

	public final static String GET_ORDER_STATUS = NEW_BASE_URL
			+ "getstatus?orderid=";

//	public final static String SET_ORDER_STATUS = NEW_BASE_URL
//			+ "setstatus?status=canceled&orderid=";
	public final static String SET_ORDER_STATUS = NEW_BASE_URL
			+ "setstatus";

	public final static String SET_PAYTM_ORDER_STATUS_SUCCESS = NEW_BASE_URL
			+ "setstatus?status=success&orderid=";

	public final static String GET_MOBILE_HASH = NEW_BASE_URL
			+ "getmobilehash?";

	public final static String WALLET_SUCCESS_FAILURE = NEW_BASE_URL
			+ "success.php?orderid=";

}


