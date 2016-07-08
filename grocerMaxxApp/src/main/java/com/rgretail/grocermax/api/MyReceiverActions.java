package com.rgretail.grocermax.api;

import com.rgretail.grocermax.BaseActivity;

/**
 * For Holding The Receiver Actions
 * 
 */
public interface MyReceiverActions {

	String PKG_NAME = BaseActivity.mContext.getPackageName();

	String LOGIN = PKG_NAME + ".login";

	String REGISTER_USER = PKG_NAME + ".registerUser";

	String FORGOT_PWD = PKG_NAME + ".forgotpwd";

	String SEARCH_BY_CATEGORY = PKG_NAME + ".searchbycategory";

	String CART_DETAIL_AFTER_LOGIN = PKG_NAME + ".cart_detail_after_login";
	
	String USER_DETAILS = PKG_NAME + ".userdetails";
	String USER_DETAILS1 = PKG_NAME + ".userdetails1";

	String EDIT_USER_DETAILS = PKG_NAME + ".edituserdetails";

	String EDIT_PROFILE = PKG_NAME + ".editProfile";

	String CATEGORY_LIST = PKG_NAME + ".categoryList";

	String CATEGORY_SUB_CATEGORY_LIST = PKG_NAME + ".categorySubCategoryList";

	String PRODUCT_LIST = PKG_NAME + ".productlist";

	String SEARCH_PRODUCT_LIST = PKG_NAME + ".searchproductlist";

	String DEAL_PRODUCT_LIST = PKG_NAME + ".dealproductlist";

	String PRODUCT_CONTENT_LIST = PKG_NAME + ".proudct_content_list";
    String PRODUCT_DETAIL_FROM_NOTIFICATION = PKG_NAME + ".product_detail_from_notification";

	String ADD_TO_CART = PKG_NAME + ".add_to_cart";
	
	String ADD_TO_CART_NEW_PRODUCT = PKG_NAME + ".add_to_cart_new_product";    //when user has already product on server and adding more products   
	
	String ADD_TO_CART_GUEST = PKG_NAME + ".add_to_cart_guest";

	String DELETE_FROM_CART = PKG_NAME + ".delete_from_cart";

	String ALL_PRODUCTS_CATEGORY = PKG_NAME + ".all_products_category";

	String VIEW_CART = PKG_NAME + ".view_cart";
	String APPLY_REMOVE_COUPON = PKG_NAME + ".apply_remove_coupon";

	String VIEW_CART_ERROR_ON_CART = PKG_NAME + ".view_cart_error_on_cart";    //when Slim Application Error occurred ,in that case handle by calling view cart again.
	
	String VIEW_CART_GO_HOME_SCREEN = PKG_NAME + ".view_cart_go_home_screen";       //come from home screen and login call view cart just to sync clone cart with server,so products quantity should display in product listing. 

	String CHECKOUT = PKG_NAME + ".checkout";

	String ORDER_HISTORY = PKG_NAME + ".order_history";
	String ORDER_DETAIL = PKG_NAME + ".order_detail";

	String ADDRESS_BOOK = PKG_NAME + ".address_book";

	String ADD_ADDRESS = PKG_NAME + ".add_address";
	String ADD_BILL_ADDRESS = PKG_NAME + ".add_bill_address";

	String EDIT_ADDRESS = PKG_NAME + ".edit_address";

	String EDIT_ADDRESS_BOOK = PKG_NAME + ".edit_address_book";

	String FINAL_CHECKOUT = PKG_NAME + ".final_checkout";
	String GET_ORDER_STATUS = PKG_NAME + ".get_order_status";
	String SET_ORDER_STATUS = PKG_NAME + ".set_order_status";
    String SET_ORDER_STATUS_CITRUS = PKG_NAME + ".set_order_status_citrus";

	String SET_PAYTM_ORDER_STATUS_SUCCESS = PKG_NAME + ".set_order_status_paytm_success";

	String CHECKOUT_ADDRESS = PKG_NAME + ".checkout_address";
	String CHECKOUT_ADDRESS_BILLING = PKG_NAME + ".checkout_address_billing";
	
	String DELETE_ADDRESS = PKG_NAME + ".delete_address";
	
	String CART_DETAIL_AFTER_DELETE = PKG_NAME + ".cart_detail_after_delete";
	
//	String CART_EDIT = PKG_NAME + ".cart_edit";
	String VIEW_CART_UPDATE_LOCALLY = PKG_NAME + ".cart_update_locally";
	
	String PRODUCT_LIST_FROM_HOME = PKG_NAME + ".product_list_from_home";

	String LOCATION = PKG_NAME + ".location";                                //carrying store location URLS and id's

	String OTP = PKG_NAME + ".otp";

	String GET_SHOP_BY_CATEGORIES = PKG_NAME + ".get_shop_by_categories";
	String GET_SHOP_BY_DEALS = PKG_NAME + ".get_shop_by_deals";

	String GET_HOME_PAGE = PKG_NAME + ".home_page";

	String OFFER_BY_DEALTYPE = PKG_NAME + ".offer_by_deal_type";
	String DEAL_BY_DEALTYPE = PKG_NAME + ".deal_by_deal_type";
	String GET_BANNER = PKG_NAME + ".get_banner";
	String PRODUCT_LISTING_BY_DEALTYPE = PKG_NAME + ".product_listing_by_deal_type";

	String ORDER_REORDER = PKG_NAME + ".order_reorder";
    String WALLET_INFO = PKG_NAME + ".wallet_info";
	String REWARD_POINT = PKG_NAME + ".reward_point";
    String WALLET_TRANSACTIONS = PKG_NAME + ".wallet_transactions";
    String TERM_CONDITION = PKG_NAME + ".term_condition";

	String TOP_PRODUCTS_LIST= PKG_NAME + ".top_product_list";
	String CATEGORY_BANNER= PKG_NAME + ".category_banner";

	String REG_DEVICE_TOKEN = PKG_NAME + ".regdevicetoken";

	String PRAMOTION_DATA = PKG_NAME + ".pramotion_data";
	String SUBSCRIBE_USER = PKG_NAME + ".subscribe_user";
}
