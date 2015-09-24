package com.sakshay.grocermax.utils;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Class to Hold the Constants
 * */
public class Constants {
	
	public static final String SERVER_SUCCESS = "1";
	
	public static final String SERVER_FAILURE = "0";
	
	public static final String localCartFile = "localcart.txt";
	
	public static final String localCloneFile = "localclonecart.txt";
	
	public static String token = "gpjaic0pdzqsv3jyouqsu3cd";
	
	public static String user_id = "61723373-785e-407d-a966-ad3b2694ceef";
	
	public static int cart_count = 0;

	public static boolean DEBUG = true;

	public static String PKG_NAME = "com.sakshay.grocermax";

	public static ArrayList<String> placesList = new ArrayList<String>(Arrays.asList("Choose Your Location", "Gurgaon"));

	public static ArrayList<String> quantity = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5"));

	public static final String GCM_SENDER_KEY = "441080484159";

	public static final String localCartFileCount = "localcartcount.txt";
	
	public static final String serverCartFile = "servercart.txt";
	
	public static final int LOGIN_REQUEST_CODE = 101;
	
	public static final String subject = "GrocerMax Android App";
	
	public static final String email = "hello@grocermax.com";
	
	public static final String customer_care = "tel:+91 8010500700";

	public static final String SHOP_BY_CATEGORY_MODEL = "shopbycategorymodel";
	
	public static final String categoriesFile = "categories.txt";
	
	
	public static class ToastConstant{
		
		public static final String PRODUCT_REMOVED = "Product removed successfully";
		public static final String PRODUCT_ADDED_CART = "Product successfully added to cart";
		public static final String UNABLE_TO_WRITE = "Unable to write to external storage.";
		public static final String ADDR_DELETED = "Address deleted successfully";
		public static final String ENTER_TEXT = "Please enter text.";
		public static final String NO_ITEM_CART = "No Item in your cart";
		public static final String LOGIN_TO_SEE_CART = "Please Login to see your cart";
		public static final String BASE_ACTIVITY_FOOTER_OTHER = "Base activity footer other";
		public static final String LOGIN_FIRST = "Please login first";
		public static final String LOGOUT_SUCCESS = "You have been logged out successfully";
		public static final String NO_PRODUCT = "No product found.";
		public static final String ERROR_MSG = "Some error occured, try again.";
		public static final String DATA_NOT_FOUND = "Data not found";
		public static final String CART_EMPTY = "Your cart is empty";
		public static final String ATLEAST_ONE_ITEM_IN_CART = "Atleast one product should be in cart";
		
		public static final String PRODUCT_DELETED = "Product deleted successfully";
		public static final String NO_ACCOUNT_ADDR = "Your account dont have any address.Please create an address from address book";
		public static final String SELECT_SHIP_ADDR = "Please select shipping address";
		public static final String MAKE_SHIPPING_BILLING = "Please make this address shipping or billing";
		
		public static final String SELECT_BILL_ADDR = "Please select billing address";
		public static final String SELECT_TIME = "Please select time slot";
		public static final String FNAME_BLANCK = "First name can not be blank";
		public static final String LNAME_BLANCK = "Last name can not be blank";
		public static final String MOB_BLANCK = "Mobile number can not be blank";
		public static final String MOB_NUMBER_DIGIT = "Mobile number should be 10 digit number";
		public static final String ADDR_BLANCK = "Address can not be blank";
		public static final String PIN_BLANCK = "Pincode can not be blank";
		public static final String PIN_NUMBER_DIGIT = "Pincode is of 6 numbers";
		public static final String OPWD_BLANK = "Old password should not be empty";
		public static final String PWD_BLANK = "Password should not be empty";
		public static final String NPWD_BLANK = "New password should not be empty";
		public static final String PWD_NUMBER_DIGIT = "Password should be of 6 characters";
		public static final String CPWD_BLANK = "Confirm Password should not be empty";
		public static final String PWD_NOT_MATCH = "Password not matching";
		public static String msgNoInternet = "No internet connection. Please try again";
		public static final String PROFILE_UPDATED = "Profile updated successfully";
		public static final String ENTER_USERNAME = "Please enter Username";
		public static final String ENTER_PWD = "Please enter password";
		public static final String ENTER_CORRECT_EMAIL = "Please enter correct mail id";
		public static final String LOGIN_FAIL = "Failed to Login. Please try again";
		public static final String listFull = "Listed all products in this category";
		public static final String EMAIL_BLANCK = "Email Id should not be empty";
		public static String invalid_email = "The email entered is not a valid email";
		public static final String FNAME_SINGLE_WORD = "First Name should be a single word";
		public static final String LNAME_SINGLE_WORD = "LAst Name should be a single word";
		public static final String SELECT_GENDER = "Please select gender";
		public static final String REGISTER_SUCCESSFULL = "User registered Successfully";
		public static final String SELECT_PAYMENT_MODE = "Please select payment method";
		public static final String VIEW_CART_EMPTY = "All products have deleted";
		
		public static final String SHIP_FNAME = "Please enter first name in shipping address";
		public static final String SHIP_LNAME = "Please enter last name in shipping address";
		public static final String SHIP_PHONE = "Please enter phone number in shipping address";
		public static final String SHIP_PHONE_VALID = "Phone number should be of 10 digit in shipping address";
		public static final String SHIP_ADDR1 = "Please enter shipping address";
		public static final String SHIP_CITY = "Please enter city in shipping address";
		public static final String SHIP_STATE = "Please enter state in shipping address";
		public static final String SHIP_COUNTRY = "Please enter country in shipping address";
		public static final String SHIP_PINCODE = "Please enter pincode in shipping address";
		public static final String SHIP_PINCODE_VALID = "Pincode should be of 6 digit in shipping address";
		
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




	}

}
