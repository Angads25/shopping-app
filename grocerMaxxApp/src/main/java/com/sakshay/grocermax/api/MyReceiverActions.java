package com.sakshay.grocermax.api;

import com.sakshay.grocermax.MyApplication;

/**
 * For Holding The Receiver Actions
 */
public interface MyReceiverActions {

    String PKG_NAME = MyApplication.getInstance().getPackageName();

    String LOGIN = PKG_NAME + ".login";

    String REGISTER_USER = PKG_NAME + ".registerUser";

    String FORGOT_PWD = PKG_NAME + ".forgotpwd";


    String CART_DETAIL_AFTER_LOGIN = PKG_NAME + ".cart_detail_after_login";

    String USER_DETAILS = PKG_NAME + ".userdetails";
    String USER_DETAILS1 = PKG_NAME + ".userdetails1";

    String EDIT_USER_DETAILS = PKG_NAME + ".edituserdetails";

    String EDIT_PROFILE = PKG_NAME + ".editProfile";

    String CATEGORY_LIST = PKG_NAME + ".categoryList";

    String CATEGORY_SUB_CATEGORY_LIST = PKG_NAME + ".categorySubCategoryList";

    String PRODUCT_LIST = PKG_NAME + ".productlist";

    String SEARCH_PRODUCT_LIST = PKG_NAME + ".searchproductlist";

    String PRODUCT_CONTENT_LIST = PKG_NAME + ".proudct_content_list";

    String ADD_TO_CART = PKG_NAME + ".add_to_cart";

    String ADD_TO_CART_NEW_PRODUCT = PKG_NAME + ".add_to_cart_new_product";    //when user has already product on server and adding more products

    String ADD_TO_CART_GUEST = PKG_NAME + ".add_to_cart_guest";

    String GET_SHOP_BY_CATEGORIES = PKG_NAME + ".get_shop_by_categories";
    String GET_SHOP_BY_DEALS = PKG_NAME + ".get_shop_by_deals";

    String OFFER_BY_DEALTYPE = PKG_NAME + ".offer_by_deal_type";
    String DEAL_BY_DEALTYPE = PKG_NAME + ".deal_by_deal_type";
    String PRODUCT_LISTING_BY_DEALTYPE = PKG_NAME + ".product_listing_by_deal_type";

    String DELETE_FROM_CART = PKG_NAME + ".delete_from_cart";

    String VIEW_CART = PKG_NAME + ".view_cart";

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

    String CHECKOUT_ADDRESS = PKG_NAME + ".checkout_address";

    String DELETE_ADDRESS = PKG_NAME + ".delete_address";

    String CART_DETAIL_AFTER_DELETE = PKG_NAME + ".cart_detail_after_delete";

    //	String CART_EDIT = PKG_NAME + ".cart_edit";
    String VIEW_CART_UPDATE_LOCALLY = PKG_NAME + ".cart_update_locally";

    String PRODUCT_LIST_FROM_HOME = PKG_NAME + ".product_list_from_home";

    String LOCATION = PKG_NAME + ".location";                                //carrying store location URLS and id's

    String OTP = PKG_NAME + ".otp";

    String GET_BANNER = PKG_NAME + ".get_banner";

}
