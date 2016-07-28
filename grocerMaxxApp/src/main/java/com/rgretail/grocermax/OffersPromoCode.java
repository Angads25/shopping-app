package com.rgretail.grocermax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.OrderReviewBean;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants.ToastConstant;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.util.List;

//import com.google.analytics.tracking.android.EasyTracker;

public class OffersPromoCode extends BaseActivity implements OnClickListener{
	
	private String header;
//	EasyTracker tracker;
	EditText etCouponCode;
	Button btnApplyCoupon,btnApplyCode,btnRemoveCode,btnPay;
	Context mContext;
	String strApplyCoupon;
	String strRemoveCoupon;
	TextView txtItemCount,txtSubTotal,txtShippingCharges,txtYouSaved,txtTotal;
	TextView tvItemCount,tvSubTotal,tvShippingCharges,tvYouSaved,tvTotal;
	LinearLayout llFirstPage,llSecondPage;
	TextView tvEnterCode;
	TextView tvMiddleLineCoupon;
	OrderReviewBean orderReviewBean;
	float saving=0;
	String test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		
		setContentView(R.layout.offers_before_apply);
		etCouponCode = (EditText) findViewById(R.id.edit_coupon_code);
		btnApplyCoupon = (Button) findViewById(R.id.btn_apply_coupon);
		btnApplyCode = (Button)  findViewById(R.id.btn_apply_code);
		btnRemoveCode = (Button) findViewById(R.id.btn_remove_code);
		btnPay = (Button)  findViewById(R.id.btn_pay);
		
		txtItemCount = (TextView) findViewById(R.id.txt_item_count);
		txtSubTotal = (TextView) findViewById(R.id.txt_subtotal);
		txtShippingCharges = (TextView) findViewById(R.id.txt_shipping_charges);
		txtYouSaved = (TextView) findViewById(R.id.txt_you_saved);
		txtTotal = (TextView) findViewById(R.id.txt_total);
		
		tvItemCount = (TextView) findViewById(R.id.tv_item_count);
		tvSubTotal = (TextView) findViewById(R.id.tv_subtotal);
		tvShippingCharges = (TextView) findViewById(R.id.tv_shipping_charges);
		tvYouSaved = (TextView) findViewById(R.id.tv_you_saved);
		tvTotal = (TextView) findViewById(R.id.tv_total);
		
		tvMiddleLineCoupon = (TextView) findViewById(R.id.middle_line_coupon);
		tvEnterCode = (TextView) findViewById(R.id.tv_enter_code);
		llFirstPage = (LinearLayout) findViewById(R.id.ll_first_page);
		llSecondPage = (LinearLayout) findViewById(R.id.ll_second_page);
		llSecondPage.setVisibility(View.INVISIBLE);
		
		orderReviewBean=MySharedPrefs.INSTANCE.getOrderReviewBean();
		List<CartDetail> cartList = orderReviewBean.getProduct();
		if(cartList!=null)
		{
			for(int i=0;i<cartList.size();i++)
			{
				saving=saving+(cartList.get(i).getQty()*(Float.parseFloat(cartList.get(i).getMrp())-Float.parseFloat(cartList.get(i).getPrice())));
			}
			saving = saving-(Float.parseFloat(orderReviewBean.getDiscount_amount()));   //- - plus
			
//			if(orderReviewBean.getCouponCode() != null && !orderReviewBean.getCouponCode().equalsIgnoreCase("null")){
//				float save = Float.parseFloat(orderReviewBean.getSubTotal()) - Float.parseFloat(orderReviewBean.getCouponSubtotalWithDsicount());
//				saving = save + saving;
//			}
			
			tvSubTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean.getSubTotal())));
			tvYouSaved.setText("Rs."+String.format("%.2f",saving));
			if(orderReviewBean.getShipping_ammount() != null && orderReviewBean.getShipping_ammount().length() > 0){
				tvShippingCharges.setText("Rs."+Float.parseFloat(orderReviewBean.getShipping_ammount()));
			}
			tvTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean.getGrandTotal())));
			tvItemCount.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean.getGrandTotal())));
			if(MySharedPrefs.INSTANCE.getTotalItem()!=null)
			{
				txtItemCount.setText(MySharedPrefs.INSTANCE.getTotalItem()+" item");
			}
		}

		if(orderReviewBean.getCouponCode() != null && !orderReviewBean.getCouponCode().equalsIgnoreCase("null")){
			llFirstPage.setVisibility(View.GONE);
			llSecondPage.setVisibility(View.VISIBLE);
			tvEnterCode.setText("Applied Code");
			etCouponCode.setEnabled(false);
			etCouponCode.setText(orderReviewBean.getCouponCode());
			tvMiddleLineCoupon.setBackgroundDrawable(getResources().getDrawable(R.color.gray_1));
		}else{
			llFirstPage.setVisibility(View.VISIBLE);
			llSecondPage.setVisibility(View.GONE);
			tvEnterCode.setText("Enter Code");
			etCouponCode.setEnabled(true);
			etCouponCode.setHint("Type Here");
			tvMiddleLineCoupon.setBackgroundDrawable(getResources().getDrawable(R.color.red));
		}
		
		strApplyCoupon = UrlsConstants.ADD_COUPON+MySharedPrefs.INSTANCE.getUserId()+"&quote_id="
							+MySharedPrefs.INSTANCE.getQuoteId()+"&couponcode=";

		strRemoveCoupon = UrlsConstants.REMOVE_COUPON+MySharedPrefs.INSTANCE.getUserId()+"&quote_id="
				+MySharedPrefs.INSTANCE.getQuoteId()+"&couponcode=";
		
		btnApplyCoupon.setOnClickListener(this);
		btnApplyCode.setOnClickListener(this);
		btnRemoveCode.setOnClickListener(this);
		btnPay.setOnClickListener(this);
		
//		addActionsInFilter(MyReceiverActions.ORDER_HISTORY);
//		initHeader(findViewById(R.id.header), true, "Order History");
    }

	@Override
	public void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initHeader(findViewById(R.id.header), true, null);
	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();

       /*------------------------------*/
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();

    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_apply_coupon:
			if(etCouponCode.getText().toString().length()>0){
				new Coupon(mContext,"Apply").execute(strApplyCoupon+etCouponCode.getText().toString());
			}else{
				UtilityMethods.customToast(ToastConstant.SELECT_COUPON_CODE, mContext);
			}
			break;
		case R.id.btn_apply_code:
			if(etCouponCode.getText().toString().length()>0){
				new Coupon(mContext,"Apply").execute(strApplyCoupon+etCouponCode.getText().toString());
			}else{
//				orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
//				orderReviewBean.setSubTotal(tvSubTotal.getText().toString().replace("Rs.", ""));
//				orderReviewBean.setShipping_ammount(tvShippingCharges.getText().toString().replace("Rs.", ""));
//				orderReviewBean.setSaving(tvYouSaved.getText().toString().replace("Rs.", ""));
//				orderReviewBean.setGrandTotal(tvTotal.getText().toString().replace("Rs.", ""));
//				MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);
				Intent call = new Intent(this, ReviewOrderAndPay.class);
				startActivity(call);
//				UtilityMethods.customToast("Please enter coupon code", mContext);
			}
			break;
		case R.id.btn_remove_code:
			if(etCouponCode.getText().toString().length()>0){
				new Coupon(mContext,"Remove").execute(strRemoveCoupon+etCouponCode.getText().toString());
			}else{
				UtilityMethods.customToast(ToastConstant.SELECT_COUPON_CODE, mContext);
			}
			break;
		case R.id.btn_pay:
//			orderReviewBean.setSubTotal(tvSubTotal.getText().toString().replace("Rs.", ""));
//			orderReviewBean.setShipping_ammount(tvShippingCharges.getText().toString().replace("Rs.", ""));
//			orderReviewBean.setSaving(tvYouSaved.getText().toString().replace("Rs.", ""));
//			orderReviewBean.setGrandTotal(tvTotal.getText().toString().replace("Rs.", ""));
//			MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);
			Intent callPay = new Intent(this, ReviewOrderAndPay.class);
			startActivity(callPay);
			break;
		default:
			break;
		}
	}
	
}

//class Coupon extends AsyncTask<String, String, String>
//{
//	Context context;
//	String strApplyorRemove;
//	String strGrandTotal,strShippingCharge;
////	strCouponCode,strSubTotal,strSubTotalDiscount,strYouSave;
//	public Coupon(Context mContext,String strapplyorremove){
//		context = mContext;
//		strApplyorRemove = strapplyorremove;
//	}
//	
//	@Override
//	protected void onPreExecute() {
//		// TODO Auto-generated method stub
//		super.onPreExecute();
//		((BaseActivity)context).showDialog();
//	}
//
//	@Override
//	protected String doInBackground(String... params) {
//		// TODO Auto-generated method stub
////		strApplyorRemove = params[1];
//		HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
//    	HttpGet httpGet = new HttpGet(params[0]);
//		httpGet.setHeader("Content-Type", "application/json");
//		HttpResponse response = null;
//		try {
//			response = client.execute(httpGet);
//			HttpEntity resEntity = response.getEntity();
//			return EntityUtils.toString(resEntity);
//		} catch (ClientProtocolException e) {
//			((BaseActivity)context).dismissDialog();
//			e.printStackTrace();
//		} catch (IOException e) {
//			((BaseActivity)context).dismissDialog();
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
//	
//	@Override
//	protected void onPostExecute(String result) {
//		// TODO Auto-generated method stub
//		super.onPostExecute(result);
//		try{
//			JSONObject jsonObject = new JSONObject(result);
//			String strResult = jsonObject.getString("Result");
//			String strFlag = jsonObject.getString("flag");
////			JSONArray jsonArray = jsonObject.getJSONArray("CartDetails");
//			
//			UtilityMethods.customToast(strResult, context);
//			if(strApplyorRemove.equalsIgnoreCase("Apply"))
//			{
//				if(strFlag.equalsIgnoreCase("1")){  											//success
//					
//					JSONObject jsoncartObject = jsonObject.getJSONObject("CartDetails");
//					if(jsoncartObject.length() > 0){
////						CartProductList.getInstance().strCouponCode
////						CartProductList.getInstance().strSubTotal
////						CartProductList.getInstance().strSubtotalWithDiscount
//						
////						strCouponCode = jsoncartObject.getString("coupon_code");          	         //coupon_code
////						strYouSave = jsoncartObject.getString("you_save");             			     //you_save
////					    strSubTotal = jsoncartObject.getString("subtotal");            		         //subtotal
////						strGrandTotal = jsoncartObject.getString("grand_total");          			 //grand_total
////						strShippingCharge = jsoncartObject.getString("ShippingCharge");      		 //ShippingCharge 		
////						strSubTotalDiscount = jsoncartObject.getString("subtotal_with_discount");    //subtotal_with_discount
//					
//						float savee = 0;
//						OrderReviewBean orderReviewBean1 = MySharedPrefs.INSTANCE.getOrderReviewBean();
//						List<CartDetail> cartList = orderReviewBean1.getProduct();
//						if(cartList!=null)
//						{
//							for(int i=0;i<cartList.size();i++)
//							{
//								savee = savee+(cartList.get(i).getQty()*(Float.parseFloat(cartList.get(i).getMrp())-Float.parseFloat(cartList.get(i).getPrice())));
//							}
//							savee = savee+(Float.parseFloat(jsoncartObject.getString("you_save")));
//						}
//						
////						Float save = ((OffersPromoCode)context).saving+Float.parseFloat(jsoncartObject.getString("you_save"));
//						
//						orderReviewBean1.setCouponCode(jsoncartObject.getString("coupon_code"));
//						orderReviewBean1.setCouponSubtotalWithDiscount(jsoncartObject.getString("subtotal_with_discount"));
//						orderReviewBean1.setSubTotal(jsoncartObject.getString("subtotal"));
//						orderReviewBean1.setShipping_ammount(jsoncartObject.getString("ShippingCharge"));
//						orderReviewBean1.setSaving(String.valueOf(savee));
//						orderReviewBean1.setDiscount_amount("-"+jsoncartObject.getString("you_save"));
////						((OffersPromoCode)context).orderReviewBean.setGrandTotal(jsoncartObject.getString("grand_total"));
//						orderReviewBean1.setGrandTotal(jsoncartObject.getString("grand_total"));
//						MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean1);
//						MySharedPrefs.INSTANCE.putisCouponApply("true");
//						MySharedPrefs.INSTANCE.putCouponAmount(jsoncartObject.getString("you_save"));
//						
////						((OffersPromoCode)context).orderReviewBean.setGrandTotal(((OffersPromoCode)context).orderReviewBean.getSubTotal());
//						((OffersPromoCode)context).tvSubTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean1.getSubTotal())));
//						((OffersPromoCode)context).tvShippingCharges.setText("Rs."+Float.parseFloat(orderReviewBean1.getShipping_ammount()));
//						((OffersPromoCode)context).tvYouSaved.setText("Rs."+String.format("%.2f",savee));
//						((OffersPromoCode)context).tvTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean1.getGrandTotal())));
//						((OffersPromoCode)context).tvItemCount.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean1.getGrandTotal())));
//						
//						((OffersPromoCode)context).llFirstPage.setVisibility(View.GONE);
//						((OffersPromoCode)context).llSecondPage.setVisibility(View.VISIBLE);
//						((OffersPromoCode)context).tvEnterCode.setText("Applied Code");
//						((OffersPromoCode)context).etCouponCode.setEnabled(false);
//						((OffersPromoCode)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.gray_1));
//						
////						CartProductList.getInstance().strCouponCode = jsoncartObject.getString("coupon_code");          	         //coupon_code
////						CartProductList.getInstance().strSubTotal = jsoncartObject.getString("subtotal");            		         //subtotal
////						strGrandTotal = jsoncartObject.getString("grand_total");          			 								 //grand_total
////						strShippingCharge = jsoncartObject.getString("ShippingCharge");      		 								 //ShippingCharge
////						CartProductList.getInstance().strSubtotalWithDiscount = jsoncartObject.getString("subtotal_with_discount");  //subtotal_with_discount
////						String strcouponsave = String.valueOf((Integer.parseInt(CartProductList.getInstance().strSubTotal) -
////								   Integer.parseInt(CartProductList.getInstance().strSubtotalWithDiscount)));
//					}
////					UtilityMethods.customToast(strResult, context);
//				}else if(strFlag.equalsIgnoreCase("0")){  										//failure
//					((OffersPromoCode)context).llFirstPage.setVisibility(View.VISIBLE);
//					((OffersPromoCode)context).llSecondPage.setVisibility(View.GONE);
//					((OffersPromoCode)context).tvEnterCode.setText("Enter Code");
//					((OffersPromoCode)context).etCouponCode.setEnabled(true);
//					((OffersPromoCode)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.red));
//				}
//			}else if(strApplyorRemove.equalsIgnoreCase("Remove")){
//				if(strFlag.equalsIgnoreCase("1")){  											//Remove success
//
//					JSONObject jsoncartObject = jsonObject.getJSONObject("CartDetails");
//					
//					float savee2 = 0;
//					OrderReviewBean orderReviewBean2 = MySharedPrefs.INSTANCE.getOrderReviewBean();
//					List<CartDetail> cartList = orderReviewBean2.getProduct();
//					if(cartList!=null)
//					{
//						for(int i=0;i<cartList.size();i++)
//						{
//							savee2 = savee2+(cartList.get(i).getQty()*(Float.parseFloat(cartList.get(i).getMrp())-Float.parseFloat(cartList.get(i).getPrice())));
//						}
//						savee2 = savee2-(Float.parseFloat(jsoncartObject.getString("you_save")));
//					}
//
//					Float totalremove = Float.parseFloat(orderReviewBean2.getGrandTotal()) +
//						 	   Float.parseFloat(MySharedPrefs.INSTANCE.getCouponAmount());
//					Float couponwithdiscount = Float.parseFloat(orderReviewBean2.getCouponSubtotalWithDsicount()) +
//					 	   Float.parseFloat(MySharedPrefs.INSTANCE.getCouponAmount());
//					
//					orderReviewBean2.setSubTotal(String.valueOf(Float.parseFloat(orderReviewBean2.getSubTotal())));
//					orderReviewBean2.setShipping_ammount(jsoncartObject.getString("ShippingCharge"));
//					orderReviewBean2.setSaving(String.valueOf(savee2));
//					orderReviewBean2.setDiscount_amount(jsoncartObject.getString("you_save"));
//					orderReviewBean2.setGrandTotal(String.valueOf(totalremove));
//					orderReviewBean2.setCouponCode(null);
//					orderReviewBean2.setCouponSubtotalWithDiscount(String.valueOf(couponwithdiscount));
//					MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean2);
//					MySharedPrefs.INSTANCE.putisCouponApply("false");
//					MySharedPrefs.INSTANCE.putCouponAmount("0");
//					
//				 	((OffersPromoCode)context).tvSubTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(String.valueOf(Float.parseFloat(orderReviewBean2.getSubTotal())))));
//					((OffersPromoCode)context).tvShippingCharges.setText("Rs."+Float.parseFloat(orderReviewBean2.getShipping_ammount()));
//					((OffersPromoCode)context).tvYouSaved.setText("Rs."+String.format("%.2f",savee2));
//					((OffersPromoCode)context).tvTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(String.valueOf(totalremove))));
//					((OffersPromoCode)context).tvItemCount.setText("Rs."+String.format("%.2f",Float.parseFloat(String.valueOf(totalremove))));
//					 
//					((OffersPromoCode)context).llFirstPage.setVisibility(View.VISIBLE);
//					((OffersPromoCode)context).llSecondPage.setVisibility(View.GONE);
//					((OffersPromoCode)context).tvEnterCode.setText("Enter Code");
//					((OffersPromoCode)context).etCouponCode.setEnabled(true);
//					((OffersPromoCode)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.red));
////					((OffersPromoCode)context).tvTotal.setText(((OffersPromoCode)context).orderReviewBean.getGrandTotal());
////					UtilityMethods.customToast(strResult, context);
//				}else if(strFlag.equalsIgnoreCase("0")){  										//Remove failure
//					((OffersPromoCode)context).llFirstPage.setVisibility(View.GONE);
//					((OffersPromoCode)context).llSecondPage.setVisibility(View.VISIBLE);
//					((OffersPromoCode)context).tvEnterCode.setText("Applied Code");
//					((OffersPromoCode)context).etCouponCode.setEnabled(false);
//					((OffersPromoCode)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.gray_1));
//				}
//			}
//		}catch(Exception e){
//		}
//		((BaseActivity)context).dismissDialog();
//	}
//	
//}

