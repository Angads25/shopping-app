package com.rgretail.grocermax.utils;

import com.rgretail.grocermax.bean.LocationListBean;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Class to Hold the AppConstants
 * */
public class AppConstants {
	public static String strTitleHotDeal;
//	public static String strSelectedCity="",strSelectedState,
//			strSelectedStateId,                                          //store id
//			strSelectedStateRegionId;                                   //state id for create new address
	public static float densityPhone = 0;
	public static boolean bBack = false;
	public static String strUpgradeValue = "0";                 //1-> will show to user an update dialog box to download app forcibly.
	public static String strCompareUpgradeValue = "1";
	public static boolean b2DaysUpdateDialog = false;
	public static LocationListBean locationBean;

    public static String strPopupData = "";


    public static String token = "gpjaic0pdzqsv3jyouqsu3cd";
	
	public static String user_id = "61723373-785e-407d-a966-ad3b2694ceef";
	
	public static int cart_count = 0;

	public static boolean DEBUG = true;

	public static String PKG_NAME = "com.rgretail.grocermax";

	public static ArrayList<String> placesList = new ArrayList<String>(Arrays.asList("Choose Your Location", "Gurgaon"));

	public static ArrayList<String> quantity = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5"));
	
	public static final String localCartFile = "localcart.txt";
	
	public static final String localCartFileCount = "localcartcount.txt";
	
	public static final String serverCartFile = "servercart.txt";
	
	public static final int LOGIN_REQUEST_CODE = 101;
	
	public static final String subject = "GrocerMax Android App";
	
	public static final String email = "hello@grocermax.com";
	
	public static final String customer_care = "tel:+91 8010500700";

	public static final String categoriesFile = "categories.txt";

	//  event handling //
	public static final String SPLASH_SCREEN = "Splash screen";
	public static final String OPEN_DRAWER_MENU = "open Drawer";
	public static final String CLOSE_DRAWER_MENU = "Close Drawer";
	public static final String DRAWER_SHOP_BY_CATEGORY = "Drawer shop by category selection";
	public static final String DRAWER_SHOP_BY_DEALS = "Drawer shop by deal selection";
	public static final String DRAWER_GET_IN_TOUCH_WITH_US = "Drawer get in touch with us";
	public static final String DRAWER_RATE_US = "Drawer rate us";
	public static final String DRAWER_STORE = "Drawer store";
	public static final String SEARCH_BUTTON = "Bottom Search Button Pressed";
	public static final String CATEGORY_SCROLL_BROWSING_CATEGORIES = "Scrolling through browse categories";   //like staples,dry fruits
	public static final String CATEGORY_SELECTION_MAIN = "Category Selection main";   //like staples,dry fruits
	public static final String BOTTOM_CART_PROCEED_BUTTON_PRESSED = "Bottom cart proceed button pressed";
	public static final String BOTTOM_CART_UPDATE_BUTTON_PRESSED = "Bottom cart update button pressed";
	public static final String BANNER_SCROLLING = "Banner scroll";
	public static final String BANNER_CLICK = "Banner Selection";
	public static final String SHOP_BY_CATEGORY_IMAGE_CLICK = "SHOPBYCATEGORYIMAGE";
	public static final String SHOP_BY_CATEGORY_FOOTER_CLICK = "SHOPBYCATEGORYFOOTER";
	public static final String SHOP_BY_CATEGORY_SCROLLING = "SHOPBYCATEGORYSCROLLING";
	public static final String SHOP_BY_DEAL_CLICK = "SHOPBYDEALCLICK";
	public static final String SHOP_BY_DEAL_SCROLLING = "SHOPBYDEALSCROLLING";
	public static final String SELECTED_STORE = "Track the button for city selection";
	public static final String SELECTED_STORE_BUTTON = "Save selected City";
	public static final String GA_EVENT_OFFER_IN_CATEGORY = "Offer In Category Selection";
	public static final String GA_EVENT_DEAL_SCROLLER = "Deal Scroll";
	public static final String GA_EVENT_DEAL_SELECTION = "Deal Selection";
	public static final String GA_EVENT_DEAL_LISTING_SCROLLING = "Deal list scrolling";         //ShopByDealItemDetailFragment->ShopByDealItemDetailGrid->ShopByDealDetailListAdapter
	public static final String GA_EVENT_DRAWER_EXPANDABLE = "Drawer category expanding";
	public static final String GA_EVENT_DRAWER_SUB_CATEGORY_CLICK = "Drawer sub category selection";
	public static final String GA_EVENT_CATEGORY_OPENED = "Category opened";
	public static final String GA_EVENT_SUB_CATEGORY = "next level of subcategory selection";
	public static final String GA_EVENT_SUB_SUB_CATEGORY = "next level of sub sub category selection";
	public static final String GA_EVENT_ADD_CART_ITEMS = "Item added in cart";
	public static final String GA_EVENT_PRODUCT_HAVING_OFFER = "Product having Offers";
	public static final String GA_EVENT_DEAL_CATEGORY_OPENED = "Deal category opened";
	public static final String GA_EVENT_SELECTION_OF_DEAL_CATEGORY = "Selection of deal category";
	public static final String GA_EVENT_PRODUCT_LISTING_THROUGH_HOME_BANNER = "Productlistng through HomeBanner";
	public static final String GA_EVENT_DEALS_THROUGH_HOME_BANNER = "Deallistng through HomeBanner";
	public static final String GA_EVENT_DEALS_OFFER_THROUGH_HOME_BANNER = "Offer by dealtype";
	public static final String GA_EVENT_DEALS_PRODUCT_LISTING_THROUGH_HOME_BANNER = "Deal product listng through HomeBanner";
	public static final String GA_EVENT_SEARCH_LISTING_THROUGH_HOME_BANNER = "Search listng through HomeBanner";
	public static final String GA_EVENT_OPEN_SEARCH = "Opening of the search option";
	public static final String GA_EVENT_FACEBOOK_LOGIN = "Login By Facebook";
	public static final String GA_EVENT_GOOGLE_LOGIN = "Login By Google";
	public static final String GA_EVENT_EMAIL_LOGIN = "Login By Email";
	public static final String GA_EVENT_REGISTER_EMAIL = "Register By Email";
	public static final String GA_EVENT_CART_SCROLLER = "Scrolling behavior";
	public static final String GA_EVENT_CART_UPDATE = "Cart Update behavior";
	public static final String GA_EVENT_EXISTING_SHIPPING_SELECT = "Existing Shipping Address Selected";
	public static final String GA_EVENT_EXISTING_SHIPPING_EDIT = "Existing Shipping Address Edit";
	public static final String GA_EVENT_NEW_SHIPPING_ADDRESS_SELECT = "New Shipping Address Selected";
	public static final String GA_EVENT_PROCEED_SHIPPING = "Proceed to billing address button selection";
	public static final String GA_EVENT_PROCEED_SHIPPING_BILLING = "Proceed to billing address selection button selection";
	public static final String GA_EVENT_PROCEED_BILLING_CLICK = "Proceed to delivery details button selection";
	public static final String GA_EVENT_EXISTING_BILLING_SELECT = "Existing Billing Address Selected";
	public static final String GA_EVENT_EXISTING_BILLING_EDIT = "Existing Billing Address Edit";
	public static final String GA_EVENT_NEW_BILLING_ADDRESS_SELECT = "New Billing Address Selected";
	public static final String GA_EVENT_PROCEED_BILLING = "Proceed to payment method button selection";
	public static final String GA_EVENT_DATE_SELECT = "Date selection";
	public static final String GA_EVENT_TIME_SLOT_SELECT = "Slot selection";
	public static final String GA_EVENT_CASH_ON_DELIVERY = "Cash on delivery";
	public static final String GA_EVENT_PAYU = "PayU";
	public static final String GA_EVENT_PAYTM = "PayTM";
	public static final String GA_EVENT_CODE_APPLIED = "Code applied";
	public static final String GA_EVENT_REMOVE_CODE = "Code removed";
	public static final String GA_EVENT_PLACE_ORDER = "Place Order button selected";
	public static final String GA_EVENT_ORDER_SUCCESS = "Order success screen displayed";
	public static final String GA_EVENT_ORDER_FAILURE = "Order failure screen displayed";
	//  event handling //
	
	public static class ToastConstant{

//		public static final String VERSION_NAME = "version";
//		public static final String VERSION = "1.0";

		public static final String REMOVE_ITEM_FOR_PROCEED = "Sorry, requested item is sold out. Please remove it from cart to proceed";
		public static final String PRODUCT_REMOVED = "Product removed successfully";
		public static final String PRODUCT_ADDED_CART = "Product successfully added to cart";
		public static final String UNABLE_TO_WRITE = "Unable to write to external storage.";
		public static final String ADDR_DELETED = "Address deleted successfully";
		public static final String ENTER_TEXT = "Please enter text.";
		public static final String NO_ITEM_CART = "Your cart is empty";
		public static final String LOGIN_TO_SEE_CART = "Please Login to see your cart";
		public static final String BASE_ACTIVITY_FOOTER_OTHER = "Base activity footer other";
		public static final String LOGIN_FIRST = "Please login first";
		public static final String LOGOUT_SUCCESS = "You have been logged out successfully";
		public static final String NO_PRODUCT = "No product found.";
		public static final String ERROR_MSG = "Some error occured, try again.";
		public static final String DATA_NOT_FOUND = "Data not found";
		public static final String CART_EMPTY = "Your cart is empty";
		public static final String NO_RESULT_FOUND = "No result found";

		public static final String SHIPPING_ADDRESS_EMPTY = "Please select shipping address";
		public static final String BILLING_ADDRESS_EMPTY = "Please select billing address";
		public static final String APPROPRIATE_QUERY = "Please enter appropriate search";

		
		public static final String PRODUCT_DELETED = "Product deleted successfully";
		public static final String NO_ACCOUNT_ADDR = "Please create an address in your account";
		public static final String SELECT_SHIP_ADDR = "Please select shipping address";
		
		public static final String SELECT_BILL_ADDR = "Please select billing address";
		public static final String SELECT_TIME = "Please select time slot";
		public static final String FNAME_BLANCK = "Enter your first name";
		public static final String LNAME_BLANCK = "Enter your last name";
		public static final String MOB_BLANCK = "Enter your mobile number";
		public static final String MOB_NUMBER_DIGIT = "Mobile number should be of 10 digits";
		public static final String ADDR_BLANCK = "Address can not be blank";
		public static final String PIN_BLANCK = "Pincode can not be blank";
		public static final String PIN_NUMBER_DIGIT = "Pincode should be of 6 digits";
		public static final String OPWD_BLANK = "Old password should not be empty";
		public static final String PWD_BLANK = "Password should not be empty";
		public static final String NPWD_BLANK = "New password should not be empty";
		public static final String PWD_NUMBER_DIGIT = "Password should be of 6 characters";
		public static final String CPWD_BLANK = "Confirm Password should not be empty";
		public static final String PWD_NOT_MATCH = "Password not matching";
		public static String msgNoInternet = "No internet connection. Please try again.";
		public static final String PROFILE_UPDATED = "Profile updated successfully";
		public static final String ENTER_USERNAME = "Please enter Username";
		public static final String ENTER_PWD = "Please enter password";
		public static final String ENTER_CORRECT_EMAIL = "Please enter correct mail id";
		public static final String LOGIN_FAIL = "Failed to Login. Please try again.";
		public static final String listFull = "That's all for this category";
		public static final String EMAIL_BLANCK = "Email Id should not be empty";
		public static String invalid_email = "The email entered is not valid";
		public static final String FNAME_SINGLE_WORD = "First Name should be a single word";
		public static final String LNAME_SINGLE_WORD = "LAst Name should be a single word";
		public static final String SELECT_GENDER = "Please select gender";
		public static final String REGISTER_SUCCESSFULL = "You're registered with us successfully";
		public static final String SELECT_PAYMENT_MODE = "Please select payment method";
		public static final String SELECT_COUPON_CODE = "Please enter coupon code";
        public static final String NO_NEED_SELECT_PAYMENT_MODE = "You have sufficient balance in your wallet. Dont need to select any payment method";
		
		public static final String SHIP_FNAME = "Please enter first name in shipping address";
		public static final String SHIP_LNAME = "Please enter last name in shipping address";
		public static final String SHIP_PHONE = "Please enter phone number in shipping address";
		public static final String SHIP_PHONE_VALID = "Phone number should be of 10 digits";
		public static final String SHIP_ADDR1 = "Please enter shipping address";
		public static final String SHIP_CITY = "Please enter city in shipping address";
		public static final String SHIP_STATE = "Please enter state in shipping address";
		public static final String SHIP_COUNTRY = "Please enter country in shipping address";
		public static final String SHIP_PINCODE = "Please enter pincode in shipping address";
		public static final String SHIP_PINCODE_VALID = "Pincode should be of 6 digits";
		
		public static final String BILL_FNAME = "Please enter first name in billing address";
		public static final String BILL_LNAME = "Please enter first name in billing address";
		public static final String BILL_PHONE = "Please enter phone number in billing address";
		public static final String BILL_ADDR1 = "Please enter billing address";
		public static final String BILL_CITY = "Please enter city in billing address";
		public static final String BILL_STATE = "Please enter state in billing address";
		public static final String BILL_COUNTRY = "Please enter country in billing address";
		public static final String BILL_PINCODE = "Please enter pincode in billing address";
		public static final String BILL_PHONE_VALID = "Phone number should be of 10 digit in billing address";
		public static final String BILL_PINCODE_VALID = "Pincode should be of 6 digit in billing address";

		public static final String REDUCE_QUANT_FIRST_PART = "Sorry, only ";
		public static final String REDUCE_QUANT_SECOND_PART = " items in stock. Please reduce quantity to proceed";

		public static final String EDIT_DIFFERENT_ADDRESS_FIRST = "You've chosen ";
		public static final String EDIT_DIFFERENT_ADDRESS_SECOND = "location. Like to change/add new address?";

		public static final String BANNER_SCREEN = "BANNER_SCREEN";
		public static final String SHOP_BY_CATEGORY_SCREEN = "SHOP_BY_CATEGORY_SCREEN";
		public static final String SHOP_BY_DEALS_SCREEN = "SHOP_BY_DEALS_SCREEN";
		public static final String SHOP_BY_CATEGORY_OFFERS_SCREEN = "SHOP_BY_CATEGORY_OFFERS_SCREEN";
		public static final String MAIN_CATEGORY_SCREEN = "MAIN_CATEGORY_SCREEN";                            //categoryactivity
		public static final String MAIN_CATEGORY_DRAWER_SCREEN = "MAIN_CATEGORY_DRAWER_SCREEN";
		public static final String MENU_DRAWER_SCREEN = "MENU_DRAWER_SCREEN";
		public static final String CATEGORY_TABS_SCREEN = "CATEGORY_TABS_SCREEN";
		public static final String SEARCH_SCREEN = "SEARCH_SCREEN";
		public static final String CART_SCREEN = "CART_SCREEN";
		public static final String SHIPPING_SCREEN = "SHIPPING_SCREEN";
		public static final String BILLING_SCREEN = "BILLING_SCREEN";
		public static final String DELIVERY_DETAILS_SCREEN = "DELIVERY_DETAILS_SCREEN";
		public static final String PAYMENT_SCREEN = "PAYMENT_SCREEN";
		public static final String PROFILE_SCREEN_ORDER_HISTORY = "PROFILE_SCREEN_ORDER_HISTORY";
		public static final String PROFILE_SCREEN_ADDRESSES = "PROFILE_SCREEN_ADDRESSES";
		public static final String PROFILE_EDIT = "PROFILE_EDIT";
		public static final String PROFILE_INVITE_FRIENDS = "PROFILE_INVITE_FRIENDS";
		public static final String PROFILE_CALL_US = "PROFILE_CALL_US";
		public static final String PROFILE_WRITE_TO_US = "PROFILE_WRITE_TO_US";
		public static final String PROFILE_SIGN_OUT = "PAYMENTPAGE";

	}

}
