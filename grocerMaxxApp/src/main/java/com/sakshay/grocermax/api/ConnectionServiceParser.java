package com.sakshay.grocermax.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.CartProductList;
import com.sakshay.grocermax.UpdateCartbg;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.AddressList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.CartDetailBean;
import com.sakshay.grocermax.bean.CategoriesProducts;
import com.sakshay.grocermax.bean.CategoryListBean;
import com.sakshay.grocermax.bean.CheckoutAddressBean;
import com.sakshay.grocermax.bean.DealListBean;
import com.sakshay.grocermax.bean.FinalCheckoutBean;
import com.sakshay.grocermax.bean.LocationDetail;
import com.sakshay.grocermax.bean.LocationListBean;
import com.sakshay.grocermax.bean.LoginResponse;
import com.sakshay.grocermax.bean.OTPResponse;
import com.sakshay.grocermax.bean.OrderHistoryBean;
import com.sakshay.grocermax.bean.OrderReviewBean;
import com.sakshay.grocermax.bean.PersonalInfo;
import com.sakshay.grocermax.bean.ProductDetailsListBean;
import com.sakshay.grocermax.bean.ProductListBean;
import com.sakshay.grocermax.bean.SearchListBean;
import com.sakshay.grocermax.bean.Simple;
import com.sakshay.grocermax.bean.UserDetailBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.UtilityMethods;

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
		int LOCATION = 122;
		int OTP_SUCCESSFULL = 123;
		int SEARCH_BY_CATEGORY = 124;
		int SET_PAYTM_ORDER_STATUS_SUCCESS = 125;
		int DEAL_PRODUCT_LIST = 126;
		int ALL_PRODUCTS_CATEGORY = 127;
		int SHOP_BY_CATEGORY_LIST = 128;
		int OFFER_BY_DEALTYPE = 129;
		int SHOP_BY_DEALS_LIST = 130;
		int DEAL_BY_DEAL_TYPE = 131;
		int HOME_BANNER = 132;
		int PRODUCT_LISTING_BY_DEALTYPE = 133;
	}

	public static BaseResponseBean parseSimpleResponse(String jsonString)
			throws JSONException {
		LoginResponse responseBean = null;
		try{
			Gson gson = new Gson();
			//BaseResponseBean responseBean = gson.fromJson(jsonString,BaseResponseBean.class);
			responseBean = gson.fromJson(jsonString,LoginResponse.class);
			if (responseBean.getQuoteId() != null && !responseBean.getQuoteId().equals("")) {
				MySharedPrefs.INSTANCE.clearQuote();
				MySharedPrefs.INSTANCE.putQuoteId(new JSONObject(jsonString).getString("QuoteId").toString());                            //added latest
			}

		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseSimpleResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return responseBean;
	}

	public static OTPResponse parseOTPResponse(String jsonString)
			throws JSONException {
		OTPResponse otpresponseBean = null;
		try{
			Gson gson = new Gson();
			//BaseResponseBean responseBean = gson.fromJson(jsonString,BaseResponseBean.class);
			otpresponseBean = gson.fromJson(jsonString,OTPResponse.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseOTPResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return otpresponseBean;
	}

	public static LoginResponse parseLoginResponse(String jsonString)
			throws JSONException {
		LoginResponse bean = null;
		try{
			JSONObject jsonObject = new JSONObject();
			Gson gson = new Gson();
			bean = gson.fromJson(jsonString, LoginResponse.class);

		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseLoginResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return bean;
	}

	public static LocationListBean parseLocationResponse(String jsonString)
			throws JSONException {
		LocationListBean locationBean = null;
		try{
			Gson gson = new Gson();
			locationBean = gson.fromJson(jsonString, LocationListBean.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseLocationResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return locationBean;
	}

	public static Simple parseAllProductsCategoriesResponse(String jsonString)
			throws JSONException {
		Simple simple = null;
		try{
			Gson gson = new Gson();
			simple = gson.fromJson(jsonString,Simple.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseAllProductsCategoriesResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return simple;
	}

	public static CategoryListBean parseCategoryResponse(String jsonString)
			throws JSONException {
		CategoryListBean categoryBean = null;
		try{
			Gson gson = new Gson();
			categoryBean = gson.fromJson(jsonString,
				CategoryListBean.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseCategoryResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return categoryBean;
	}

	public static ProductListBean parseProductResponse(String jsonString)
			throws JSONException {
		ProductListBean productListBean = null;
		try{
			Gson gson = new Gson();
			productListBean = gson.fromJson(jsonString,ProductListBean.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseProductResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return productListBean;
	}

	public static DealListBean parseDeal(String jsonString)
			throws JSONException {
		DealListBean dealListBean = null;
		try{
			JSONObject jsonObject = new JSONObject(jsonString);
			String json = jsonObject.getString("Product");
			Gson gson = new Gson();
			dealListBean = gson.fromJson(json,DealListBean.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseUserDetailsResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return dealListBean;
	}

	public static ProductDetailsListBean parseProductContentResponse(
			String jsonString) throws JSONException {
		ProductDetailsListBean contentListBean = null;
		try{
		Gson gson = new Gson();
		contentListBean = gson.fromJson(jsonString,
				ProductDetailsListBean.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseProductContentResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return contentListBean;
	}


//	public static List<HashMap<String, String>> listCategory;
//	//	public static HashMap<String, JSONArray> valuePairs;
////	public static JSONObject jsonObject[];
//	public static JSONObject jsonObjectTop[];                                 //contain array for individual tab.
//	private final static String TAG_CAT_CATEGORY_ID = "category_id";
//	private final static String TAG_CAT_PARENT_ID = "parent_id";
//	public static final String TAG_CAT_NAME = "name";
//	private final static String TAG_CAT_IS_ACTIVE = "is_active";
//	private final static String TAG_CAT_POSITION = "position";
//	private final static String TAG_CAT_LEVEL = "level";
//	private final static String TAG_PROD_FULL_NAME = "Name";
//	private final static String TAG_PROD_BRAND = "p_brand";
//	private final static String TAG_PROD_P_NAME = "p_name";
//	private final static String TAG_PROD_PACK = "p_pack";
//	private final static String TAG_PROD_PROMOTION_LEVEL = "promotion_level";
//	private final static String TAG_PROD_STATUS = "Status";
//	private final static String TAG_PROD_PRODUCT_ID = "productid";
//	private final static String TAG_PROD_PRICE = "Price";
//	private final static String TAG_PROD_SALE_PRICE = "sale_price";
//	private final static String TAG_PROD_IMAGE = "Image";
//	//	private final String TAG_PROD_PRODUCT_QTY = "product_qty";
////	private final String TAG_PROD_DESCRIPTION = "product_description";
//	private final static String TAG_PROD_CURRENCY_CODE = "currencycode";
//	private final static String TAG_PROD_CAT_ID = "categoryid";

//	public static SearchListBean parseSearchResponse(String jsonString) throws JSONException{
//
//		System.out.print("==Result=="+jsonString);
//		JSONObject jsonObject = new JSONObject(jsonString);
//		if(jsonObject.getString("Result").equalsIgnoreCase("Categories Found")){
//
//		}
//
////		if(jsonObject.getString("Result").equalsIgnoreCase("No Result Found")){
////			((BaseActivity)context).dismissDialog();
////			UtilityMethods.customToast(jsonObject.getString("Result"), context);
////			return;
////		}
//
//		listCategory = new ArrayList<HashMap<String,String>>();
//		List<HashMap<String, String>> listProd = new ArrayList<HashMap<String,String>>();
//		HashMap<String, String> map;
//
//		JSONArray jsonArrCat = jsonObject.getJSONArray("Category");
//		for(int i=0;i<jsonArrCat.length();i++)
//		{
//			map = new HashMap<String, String>();
//			JSONObject jsonObjectCat = jsonArrCat.getJSONObject(i);
//			String strCatId = jsonObjectCat.getString("category_id");
//			String strParentId = jsonObjectCat.getString("parent_id");
//			String strName = jsonObjectCat.getString("name");
//			String strIsActive = jsonObjectCat.getString("is_active");
//			String strPosition = jsonObjectCat.getString("position");
//			String strLevel = jsonObjectCat.getString("level");
//
//			map.put(TAG_CAT_CATEGORY_ID, strCatId);
//			map.put(TAG_CAT_PARENT_ID, strParentId);
//			map.put(TAG_CAT_NAME, strName);
//			map.put(TAG_CAT_IS_ACTIVE, strIsActive);
//			map.put(TAG_CAT_POSITION, strPosition);
//			map.put(TAG_CAT_LEVEL, strLevel);
//
//			listCategory.add(map);
//		}
//
//		JSONArray jsonArrProd = jsonObject.getJSONArray("Product");
//		ArrayList<JSONObject> alProd = new ArrayList<JSONObject>();                            //contain product in JsonObject form so that assign in JsonArray
//		for(int i=0;i<jsonArrProd.length();i++)
//		{
//			map = new HashMap<String, String>();
//			JSONObject jsonObjectProd = jsonArrProd.getJSONObject(i);
//			JSONArray json = jsonObjectProd.getJSONArray("categoryid");
//			if(json.length()>0) {
//				String strProdCatId = json.getString(0);
//				String strProdName = jsonObjectProd.getString("Name");
//				String strProdBrand = jsonObjectProd.getString("p_brand");
//				String strProdPName = jsonObjectProd.getString("p_name");
//				String strProdPack = jsonObjectProd.getString("p_pack");
//				String strProdPositionLevel = jsonObjectProd.getString("promotion_level");
//				String strProdStatus = jsonObjectProd.getString("Status");
//				String strProdId = jsonObjectProd.getString("productid");
//				String strProdPrice = jsonObjectProd.getString("Price");
//				String strProdSalePrice = jsonObjectProd.getString("sale_price");
//				String strProdImage = jsonObjectProd.getString("Image");
//				//    			String strProdQuantity = jsonObjectProd.getString("product_qty");
//				//    			String strProdDesc = jsonObjectProd.getString("product_description");
//				String strProdCurrencyCode = jsonObjectProd.getString("currencycode");
//
//				//    			String strProdCatId = jsonObjectProd.getJSONArray("categoryid").getString(0);
//
//
//
//				map.put(TAG_PROD_FULL_NAME, strProdName);
//				map.put(TAG_PROD_BRAND, strProdBrand);
//				map.put(TAG_PROD_P_NAME, strProdPName);
//				map.put(TAG_PROD_PACK, strProdPack);
//				map.put(TAG_PROD_PROMOTION_LEVEL, strProdPositionLevel);
//				map.put(TAG_PROD_STATUS, strProdStatus);
//				map.put(TAG_PROD_PRODUCT_ID, strProdId);
//				map.put(TAG_PROD_PRICE, strProdPrice);
//				map.put(TAG_PROD_SALE_PRICE, strProdSalePrice);
//				map.put(TAG_PROD_IMAGE, strProdImage);
////    			map.put(TAG_PROD_PRODUCT_QTY, strProdQuantity);
////    			map.put(TAG_PROD_DESCRIPTION, strProdDesc);
//				map.put(TAG_PROD_CURRENCY_CODE, strProdCurrencyCode);
//				map.put(TAG_PROD_CAT_ID, strProdCatId);
//
//				alProd.add(jsonObjectProd);
//				listProd.add(map);
//			}
//		}
//
//		JSONArray jsonArrMulProd[] = new JSONArray[listCategory.size()];
////    	valuePairs = new HashMap<String, JSONArray>();
//		jsonObjectTop = new JSONObject[listCategory.size()];
//
//
//
//		for(int m=0;m<listCategory.size();m++)
//		{
//			jsonArrMulProd[m] = new JSONArray();
//			jsonObjectTop[m] = new JSONObject();
//		}
//
//		for(int i=0;i<listProd.size();i++)
//		{
//			for(int j=0;j<listCategory.size();j++)
//			{
//				String strCatid = listCategory.get(j).get(TAG_CAT_CATEGORY_ID);
//				String strCatName = listCategory.get(j).get(TAG_CAT_NAME);
//				String strProdId = listProd.get(i).get(TAG_PROD_CAT_ID);
//				String strProdName = listProd.get(i).get(TAG_PROD_FULL_NAME);
//				System.out.println(strCatName+"==catid=="+strCatid);
//				System.out.println(strProdName+"==prodid=="+strProdId);
//				if(listCategory.get(j).get(TAG_CAT_CATEGORY_ID).equalsIgnoreCase(listProd.get(i).get(TAG_PROD_CAT_ID)))
//				{
//					jsonArrMulProd[j].put(alProd.get(i));
//					break;
//				}
//			}
//		}
//
//		JSONArray jsonArray = new JSONArray();
//		for(int k=0;k<jsonArrMulProd.length;k++)
//		{
//			System.out.println("==Final Prod Array==" + jsonArrMulProd[k]);
//			jsonObjectTop[k].put("Product", jsonArrMulProd[k]);
//
//
////			JSONObject prod_obj = new JSONObject();
////			jsonArray.put(prod_obj);
//
////			jsonArray.put(jsonObjectTop[k]);
////			jsonArray.put("Product",jsonObjectTop[k]);
//		}
//
//		JSONArray js = new JSONArray();
//		js.put(jsonObjectTop);
//
//
//			Gson gson = new Gson();
////			SearchListBean searchListBean = gson.fromJson(jsonString,SearchListBean.class);
//			SearchListBean searchListBean = gson.fromJson(String.valueOf(js),SearchListBean.class);
//			return  searchListBean;
//	}


	public static CartDetailBean parseViewCartResponse(String jsonString)
			throws JSONException {
		CartDetailBean cartBean = null;
	try {
		String cartDetailString = "";
		CartProductList.strShippingChargeLimit = new JSONObject(jsonString).optString("shippingChargeLimit").toString();      //500

		if (new JSONObject(jsonString).getString("QuoteId").toString() != null && !new JSONObject(jsonString).getString("QuoteId").toString().equals("")) {
			MySharedPrefs.INSTANCE.clearQuote();
			MySharedPrefs.INSTANCE.putQuoteId(new JSONObject(jsonString).getString("QuoteId").toString());                            //added latest
		}

//		MySharedPrefs.INSTANCE.putTotalItem(new JSONObject(jsonString).getString("TotalItem").toString());                        //added latest
		cartDetailString = new JSONObject(jsonString).getJSONObject("CartDetail").toString();

		OrderReviewBean orderReviewBean = new OrderReviewBean();

		if(new JSONObject(cartDetailString).getJSONObject("shipping_address").optString("tax_amount") != null) {
			orderReviewBean.setTax_ammount(new JSONObject(cartDetailString).getJSONObject("shipping_address").optString("tax_amount"));
		}else{                                                 //if server not return tax_amount tag and value[just for precaution].
			orderReviewBean.setTax_ammount("0.00");
		}

		orderReviewBean.setShipping_ammount(new JSONObject(cartDetailString).getJSONObject("shipping_address").optString("shipping_amount"));

		orderReviewBean.setGrandTotal(new JSONObject(cartDetailString).getString("grand_total"));
//		orderReviewBean.setGrandTotal(new JSONObject(jsonString).getString("grand_total"));
		orderReviewBean.setSubTotal(new JSONObject(cartDetailString).getString("subtotal"));
		orderReviewBean.setCouponCode(new JSONObject(cartDetailString).getString("coupon_code"));
		orderReviewBean.setCouponSubtotalWithDiscount(new JSONObject(cartDetailString).getString("subtotal_with_discount"));

//		CartProductList.getInstance().strCouponCode = new JSONObject(jsonString).getString("coupon_code");
//		CartProductList.getInstance().strSubTotal = new JSONObject(jsonString).getString("subtotal"); 
//		CartProductList.getInstance().strSubtotalWithDiscount = new JSONObject(jsonString).getString("subtotal_with_discount");

		orderReviewBean.setDiscount_amount(new JSONObject(cartDetailString).getJSONObject("shipping_address").optString("discount_amount"));
		MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);

		Gson gson = new Gson();
		jsonString = new JSONObject(jsonString).getJSONObject("CartDetail").toString();
		cartBean = gson.fromJson(jsonString, CartDetailBean.class);

	}catch(Exception e){
		new GrocermaxBaseException("ConnectionServiceParser","parseViewCartResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
	}
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

			if(new JSONObject(jsonString).getString("QuoteId").toString() != null) {
				MySharedPrefs.INSTANCE.clearQuote();
				MySharedPrefs.INSTANCE.putQuoteId(new JSONObject(jsonString).getString("QuoteId").toString());                            //added
			}

//			MySharedPrefs.INSTANCE.putTotalItem(new JSONObject(jsonString).getString("TotalItem").toString());                            //added
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
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseViewCartResponseLocally",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return cartBean;
	}
	///// parse there b/c updated cart values in OrderReviewBean used in shared preference for updating the values of cart/////

	public static UserDetailBean parseUserDetailsResponse(String jsonString)
			throws JSONException {
		UserDetailBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			Gson gson = new Gson();
			bean = new UserDetailBean();
			PersonalInfo personalInfo = gson.fromJson(
					jsonObject.optString("Personal Info"), PersonalInfo.class);
			Address shippingAddress = gson.fromJson(
					jsonObject.optString("ShippingAddress"), Address.class);
			Address billingAddress = gson.fromJson(
					jsonObject.optString("BillingAddress"), Address.class);
			bean.setPersonalInfo(personalInfo);
			bean.setShippingAddress(shippingAddress);
			bean.setBillingAddress(billingAddress);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseUserDetailsResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return bean;
	}

	public static OrderHistoryBean parseOrderHistoryResponse(String jsonString)
			throws JSONException {
		OrderHistoryBean bean = null;
		try {
			Gson gson = new Gson();
			bean = gson.fromJson(jsonString,
					OrderHistoryBean.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseOrderHistoryResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return bean;
	}

	public static AddressList parseAddressBookResponse(String jsonString)
			throws JSONException {
		AddressList bean = null;
		try {
			Gson gson = new Gson();
			bean = gson.fromJson(jsonString, AddressList.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseAddressBookResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return bean;
	}

	public static FinalCheckoutBean parseFinalCheckoutResponse(String jsonString)
			throws JSONException {
		FinalCheckoutBean bean = null;
		try{
			Gson gson = new Gson();
			bean = gson.fromJson(jsonString, FinalCheckoutBean.class);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseFinalCheckoutResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return bean;
	}

	public static CheckoutAddressBean parseCheckoutAddressResponse(
			String jsonString) throws JSONException {
		CheckoutAddressBean bean = null;
		try{
			Gson gson = new Gson();
			bean = gson.fromJson(jsonString,CheckoutAddressBean.class);

			JSONObject response = new JSONObject(jsonString);
			JSONArray shipping_obj = response.getJSONArray("Shipping");
			HashMap<String,ArrayList<String>> date_timeSlot=new HashMap<String, ArrayList<String>>();
	//		HashMap<String,ArrayList<String>> date_timeAvailableSlot=new HashMap<String, ArrayList<String>>();             //////
			for(int i=0;i<shipping_obj.length();i++)
			{
				JSONObject slot_obj=shipping_obj.getJSONObject(i);
				if(date_timeSlot.containsKey(slot_obj.getString("Date")))
				{
					ArrayList<String> timeSlot=date_timeSlot.get(slot_obj.getString("Date"));
					timeSlot.add(slot_obj.getString("TimeSlot"));
					date_timeSlot.put(slot_obj.getString("Date"), timeSlot);

	//				ArrayList<String> timeSlot11 = date_timeSlot.get(slot_obj.getString("Date")); //////////
	//				timeSlot11.add(String.valueOf(slot_obj.getInt("Available"))); /////////////////////////////
	////				ArrayList<String> timeSlot11=new ArrayList<String>();              ///////////////
	////				timeSlot11.add(String.valueOf(slot_obj.getInt("Available")));
	//				date_timeAvailableSlot.put(slot_obj.getString("Date"), timeSlot11);///////////////

				}else{
					ArrayList<String> timeSlot=new ArrayList<String>();
					timeSlot.add(slot_obj.getString("TimeSlot"));
					date_timeSlot.put(slot_obj.getString("Date"), timeSlot);

	//				ArrayList<String> timeSlot11=new ArrayList<String>();              ///////////////
	//				timeSlot11.add(String.valueOf(slot_obj.getInt("Available")));                  ////////////////
	//				date_timeAvailableSlot.put(slot_obj.getString("Date"), timeSlot11);//////////////
				}
			}


			HashMap<String,ArrayList<String>> date_timeAvailableSlot=new HashMap<String, ArrayList<String>>();             //////
			for(int i=0;i<shipping_obj.length();i++)
			{
				JSONObject slot_obj=shipping_obj.getJSONObject(i);
				if(date_timeSlot.containsKey(slot_obj.getString("Date")))
				{
					ArrayList<String> timeSlot11 = date_timeSlot.get(slot_obj.getString("Date")); //////////
					timeSlot11.add(String.valueOf(slot_obj.getInt("Available"))); /////////////////////////////
	//				ArrayList<String> timeSlot11=new ArrayList<String>();              ///////////////
	//				timeSlot11.add(String.valueOf(slot_obj.getInt("Available")));
					date_timeAvailableSlot.put(slot_obj.getString("Date"), timeSlot11);///////////////
				}else{
					ArrayList<String> timeSlot11=new ArrayList<String>();              ///////////////
					timeSlot11.add(String.valueOf(slot_obj.getInt("Available")));                  ////////////////
					date_timeAvailableSlot.put(slot_obj.getString("Date"), timeSlot11);//////////////
				}
			}

			bean.setDate_timeSlot(date_timeSlot);
			bean.setDate_timeAvailableSlot(date_timeAvailableSlot);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionServiceParser","parseCheckoutAddressResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return bean;
	}

}
