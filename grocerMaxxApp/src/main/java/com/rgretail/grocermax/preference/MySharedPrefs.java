package com.rgretail.grocermax.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.bean.OrderReviewBean;

public enum MySharedPrefs {
	INSTANCE;
	
	private String APP_PREFERENCE_NAME = "com.sakshay.grocermax";
	
	private final String GOOGLE_NAME_KEY = "key_google_name";
	private final String GOOGLE_ID_KEY = "key_google_name";
	private final String GOOGLE_EMAIL_KEY = "key_google_name";
	private final String IS_GOOGLE_DATA_SET = "key_google_name";

	private final String FACEBOOK_ID_KEY = "key_facebook_id";
	private final String FACEBOOK_EMAIL_KEY = "key_facebook_email";
	private final String FACEBOOK_NAME_KEY = "key_facebook_name";
	private final String FACEBOOK_LOCATION = "key_facebook_loc";
	private final String IS_DATA_SET = "key_user_data";

	private final String USER_EMAIL = "email_id";
	private final String ITEM_QUANTITY = "item_quantity";
	private final String ITEM_POSITION = "item_position";
	private final String USER_ID = "user_id";
	private final String USER_CITY = "user_city";
	private final String IS_LOGIN = "is_login";
    private final String GCM_DEVICE_TOKEN_FIRST = "gcm_device_token_first";
    private final String GCM_DEVICE_TOKEN = "gcm_device_token";
	
	private final String FIRST_NAME = "first_name";
	private final String LAST_NAME = "last_name";
    private final String OTP_SCREEN = "otp_screen";

	private final String SEARCH_KEY = "search_key";
	private final String TOTAL_ITEM = "total_item";
	private final String BRADCRUM = "bradcrum";
	private final String CATG_ID = "catg_id";

	private final String IS_SEARCH_KEY = "is_search";
	
	private final String REMEMBER_ME = "remember_me";
	private final String REMEMBER_ME_EMAIL = "remember_me_email";
	private final String QUOTE_ID="Quote_id";
	private final String ORDER_REVIEW_BEAN="order_review_bean";
	private final String COUPON_APPLY = "coupon_apply";
	private final String COUPON_AMOUNT = "coupon_amount";
	private final String MOBILE_NUMBER = "mobile_no";
    private final String LOGIN_REG_METHOD = "login_method";
	private final String SELECTED_CITY = "selected_city";
	private final String SELECTED_STATE = "selected_state";
	private final String SELECTED_STATEID = "selected_state_id";
	private final String SELECTED_STATEREGIONID = "selected_state_region_id";
	private final String SELECTED_STOREID = "selected_store_id";

	private SharedPreferences getAppPreference() {
		SharedPreferences preferences = BaseActivity.mContext
				.getSharedPreferences(APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		return preferences;
	}

	//////////  LOCATION USER SELECTED DATA  /////////



	public void putSelectedCity(String strCity) {
		getAppPreference().edit().putString(SELECTED_CITY, strCity).commit();
	}

	public String getSelectedCity() {
		return getAppPreference().getString(SELECTED_CITY, null);
	}

	public void putSelectedState(String strState) {
		getAppPreference().edit().putString(SELECTED_STATE, strState).commit();
	}

    public String getLoginMethod() {
        return getAppPreference().getString(LOGIN_REG_METHOD, null);
    }

    public void putLoginMethod(String loginMethod) {
        getAppPreference().edit().putString(LOGIN_REG_METHOD, loginMethod).commit();
    }

	public String getSelectedState() {
		return getAppPreference().getString(SELECTED_STATE, null);
	}

	public void putSelectedStateId(String strStateId) {
		getAppPreference().edit().putString(SELECTED_STATEID, strStateId).commit();
	}

	public String getSelectedStateId() {
		return getAppPreference().getString(SELECTED_STATEID, null);
	}

	public void putSelectedStateRegionId(String strStateRegionId) {
		getAppPreference().edit().putString(SELECTED_STATEREGIONID, strStateRegionId).commit();
	}

	public String getSelectedStateRegionId() {
		return getAppPreference().getString(SELECTED_STATEREGIONID, null);
	}

	public void putSelectedStoreId(String strStoreId) {
		getAppPreference().edit().putString(SELECTED_STOREID, strStoreId).commit();
	}

	public String getSelectedStoreId() {
		return getAppPreference().getString(SELECTED_STOREID, null);
	}

	//////////  LOCATION USER SELECTED DATA  /////////

	
	///////// COUPON DATA /////////
	public void putisCouponApply(String strCoupon) {                                       //put true OR false
		getAppPreference().edit().putString(COUPON_APPLY, strCoupon).commit();
	}	
	
	public String getCouponApply() {                                                       //get true OR false
		return getAppPreference().getString(COUPON_APPLY, null);
	}
	
	public void putCouponAmount(String strCouponAmount) {                                //put amount or "0"
		getAppPreference().edit().putString(COUPON_AMOUNT, strCouponAmount).commit();
	}	
	
	public String getCouponAmount() {                                                       //get amount or "0"
		return getAppPreference().getString(COUPON_AMOUNT, null);
	}
	///////// COUPON DATA /////////
	

	public void putFacebookId(String fbId) {
		getAppPreference().edit().putString(FACEBOOK_ID_KEY, fbId).commit();
	}

	public String getFacebookId() {
		return getAppPreference().getString(FACEBOOK_ID_KEY, null);
	}

	public void putFacebookEmail(String fbEmail) {
		getAppPreference().edit().putString(FACEBOOK_EMAIL_KEY, fbEmail)
				.commit();
	}

	public String getFacebookEmail() {
		return getAppPreference().getString(FACEBOOK_EMAIL_KEY, null);
	}
	
	

	public void putFacebookName(String name) {
		getAppPreference().edit().putString(FACEBOOK_NAME_KEY, name).commit();
	}

	public String getFacebookName() {
		return getAppPreference().getString(FACEBOOK_NAME_KEY, null);
	}

	public void putFacebookLocation(String loc) {
		getAppPreference().edit().putString(FACEBOOK_LOCATION, loc).commit();
	}

	public String getFacebookLocation() {
		return getAppPreference().getString(FACEBOOK_LOCATION, "");
	}

	public void putUserDataSet(boolean isSet) {
		getAppPreference().edit().putBoolean(IS_DATA_SET, isSet).commit();
	}

	public boolean isUserDataSet() {
		return getAppPreference().getBoolean(IS_DATA_SET, false);
	}

	public boolean clearAllData() {
		return getAppPreference().edit().clear().commit();
	}

	public void putUserEmail(String username) {
		getAppPreference().edit().putString(USER_EMAIL, username).commit();
	}

	public String getUserEmail() {
		return getAppPreference().getString(USER_EMAIL, null);
	}
	
	public void putItemQuantity(String quantity) {
		getAppPreference().edit().putString(ITEM_QUANTITY, quantity).commit();
	}

	public String getItemQuantity() {
		return getAppPreference().getString(ITEM_QUANTITY, null);
	}
	

	public void putUserId(String location) {
		getAppPreference().edit().putString(USER_ID, location).commit();
	}

	public String getUserId() {
		return getAppPreference().getString(USER_ID, null);
	}

    public String getGCMDeviceTocken() {
        return getAppPreference().getString(GCM_DEVICE_TOKEN, null);
    }

    public void putGCMDeviceTocken(String device_token) {
        getAppPreference().edit().putString(GCM_DEVICE_TOKEN, device_token).commit();
    }

    public String getGCMDeviceTockenFirst() {
        return getAppPreference().getString(GCM_DEVICE_TOKEN_FIRST, null);
    }

    public void putGCMDeviceTockenFirst(String device_token) {
        getAppPreference().edit().putString(GCM_DEVICE_TOKEN_FIRST, device_token).commit();
    }


	
	public void putQuoteId(String ouoteId) {
		getAppPreference().edit().putString(QUOTE_ID, ouoteId).commit();
	}

	public String getQuoteId() {
		return getAppPreference().getString(QUOTE_ID, null);
	}
	
	public void putOrderReviewBean(OrderReviewBean orderReviewBean) {
		  Gson gson = new Gson();
	     String json = gson.toJson(orderReviewBean);
		getAppPreference().edit().putString(ORDER_REVIEW_BEAN, json).commit();
	}

	public OrderReviewBean getOrderReviewBean() {
		Gson gson = new Gson();
		return gson.fromJson(getAppPreference().getString(ORDER_REVIEW_BEAN, null),OrderReviewBean.class);
	}

	public void putLoginStatus(boolean status) {
		getAppPreference().edit().putBoolean(IS_LOGIN, status).commit();
	}

	public boolean getLoginStatus() {
		return getAppPreference().getBoolean(IS_LOGIN, false);
	}

	public void putUserCity(String token) {
		getAppPreference().edit().putString(USER_CITY, token).commit();
	}

	public String getUserCity() {
		return getAppPreference().getString(USER_CITY, null);
	}

	public void putFirstName(String token) {
		getAppPreference().edit().putString(FIRST_NAME, token).commit();
	}

	public String getFirstName() {
		return getAppPreference().getString(FIRST_NAME, null);
	}
	
	public void putLastName(String token) {
		getAppPreference().edit().putString(LAST_NAME, token).commit();
	}

	public String getLastName() {
		return getAppPreference().getString(LAST_NAME, null);
	}

    public void putOTPScreenName(String otp) {
        getAppPreference().edit().putString(OTP_SCREEN, otp).commit();
    }

    public String getOTPScreenName() {
        return getAppPreference().getString(OTP_SCREEN, "");
    }

	
	public void putMobileNo(String strMobile) {                               
		getAppPreference().edit().putString(MOBILE_NUMBER, strMobile).commit();
	}	
	
	public String getMobileNo() {                                                  
		return getAppPreference().getString(MOBILE_NUMBER, null);
	}
	
	public void putIsSearched(boolean isSearch) {
		getAppPreference().edit().putBoolean(IS_SEARCH_KEY, isSearch).commit();
	}

	public boolean getIsSearched() {
		return getAppPreference().getBoolean(IS_SEARCH_KEY, false);
	}

	public void putSearchKey(String search_key) {
		getAppPreference().edit().putString(SEARCH_KEY, search_key).commit();
	}

	public String getSearchKey() {
		return getAppPreference().getString(SEARCH_KEY, null);
	}
	
	public void putCatId(String catgId) {
		getAppPreference().edit().putString(CATG_ID, catgId).commit();
	}

	public String getCatId() {
		return getAppPreference().getString(CATG_ID, null);
	}
	
	public void putTotalItem(String totalitem) {
		getAppPreference().edit().putString(TOTAL_ITEM, totalitem).commit();
	}

	public String getTotalItem() {
		return getAppPreference().getString(TOTAL_ITEM, null);
	}
	
	
	public void putBradecrum(String bradcrum) {
		getAppPreference().edit().putString(BRADCRUM, bradcrum).commit();
	}

	public String getBradecrum() {
		return getAppPreference().getString(BRADCRUM, null);
	}
	
	public void putRememberMe(boolean remember_me, String email)
	{
		getAppPreference().edit().putBoolean(REMEMBER_ME, remember_me)
								 .putString(REMEMBER_ME_EMAIL, email).commit();
	}
	
	public boolean getRememberMe()
	{
		return getAppPreference().getBoolean(REMEMBER_ME, false);
	}
	
	public String getRememberMeEmail()
	{
		return getAppPreference().getString(REMEMBER_ME_EMAIL, null);
	}
	
	public void clearUserInfo()
	{
		getAppPreference().edit().remove(USER_CITY)
								 .remove(USER_EMAIL)
								 .remove(USER_ID)
								 .remove(QUOTE_ID)
								 .remove(TOTAL_ITEM)
								 .remove(FIRST_NAME)
								 .remove(LAST_NAME)
								 .remove(MOBILE_NUMBER)
								 .remove(IS_LOGIN).commit();
	}
	public void clearQuote()
	{
		getAppPreference().edit().remove(QUOTE_ID).commit();
	}

	
	
	/********       GOOGLE PLUS DATA       ****/
	
	public void putGoogleName(String googlename) {
		getAppPreference().edit().putString(GOOGLE_NAME_KEY, googlename).commit();
	}

	public String getGoogleName() {
		return getAppPreference().getString(GOOGLE_NAME_KEY, null);
	}
	
	public void putGoogleId(String googleId) {
		getAppPreference().edit().putString(GOOGLE_ID_KEY, googleId).commit();
	}

	public String getGoogleId() {
		return getAppPreference().getString(GOOGLE_ID_KEY, null);
	}
	
	public void putGoogleEmail(String googleEmail) {
		getAppPreference().edit().putString(GOOGLE_EMAIL_KEY, googleEmail)
				.commit();
	}

	public String getGoogleEmail() {
		return getAppPreference().getString(GOOGLE_EMAIL_KEY, null);
	}
	
	public void putUserGoogleDataSet(boolean isSet) {
		getAppPreference().edit().putBoolean(IS_GOOGLE_DATA_SET, isSet).commit();
	}

	public boolean isUserGoogleDataSet() {
		return getAppPreference().getBoolean(IS_GOOGLE_DATA_SET, false);
	}
	
	/********       GOOGLE PLUS DATA       ****/
	
}
