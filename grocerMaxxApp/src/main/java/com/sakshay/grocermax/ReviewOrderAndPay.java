package com.sakshay.grocermax;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.Utility;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.FinalCheckoutBean;
import com.sakshay.grocermax.bean.OrderReviewBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.MyHttpUtils;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;
//import com.payu.sdk.PayU;
//import com.payu.sdk.Payment;
//import android.widget.Toast;

public class ReviewOrderAndPay extends BaseActivity
{
	//	ListView mList;
	private Button button_pay;
	private FinalCheckoutBean finalCheckoutBean;
	TextView billing_amt, shipping_amt, tax, grand_total;
	//	TableRow tr_shipping,tr_discount;
//	LinearLayout review_footer;
//	RadioGroup radioGroup;
//	private RadioButton radioTransactionButton;
	public static String order_id;
	public static String order_db_id;
	private boolean bOnline,bCash,bPayTM,bMobiKwik;
	EasyTracker tracker;
	private String[] payment_modes = {"Online Payment", "Cash on Delivery"};
	OrderReviewBean orderReviewBean;
	String payment_mode;
	float total;
	Intent intent;
	//	ProgressDialog mProgressDialog;
	String txnId;
	TextView txtItemCount,txtSubTotal,txtShippingCharges,txtYouSaved,txtTotal,txtCouponDiscount;
	TextView tvItemCount,tvSubTotal,tvShippingCharges,tvYouSaved,tvTotal,tvCouponDiscount;
	EditText etCouponCode;
	float saving=0;
	String strApplyCoupon;
	String strRemoveCoupon;
	Button llFirstPage;
	Button llSecondPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
//		setContentView(R.layout.review_order_and_pay);
//			setContentView(R.layout.delete);
			setContentView(R.layout.checkout_process_3);
//			mProgressDialog = new ProgressDialog(ReviewOrderAndPay.this);
//		String str = MySharedPrefs.INSTANCE.getCouponApply();

			addActionsInFilter(MyReceiverActions.FINAL_CHECKOUT);
			addActionsInFilter(MyReceiverActions.GET_ORDER_STATUS);
			addActionsInFilter(MyReceiverActions.SET_ORDER_STATUS);
			orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

			TextView tv = (TextView) findViewById(R.id.tv_choose_to_pay);
			tv.setTypeface(CustomFonts.getInstance().getRobotoBlack(this));

			/****************** PROMO CODE *********************/
//			RelativeLayout llOnlinePayment = (RelativeLayout) findViewById(R.id.ll_online_payment);
//			RelativeLayout llCashOnDelivery = (RelativeLayout) findViewById(R.id.ll_cash_on_delivery);
//			RelativeLayout llPayTM = (RelativeLayout) findViewById(R.id.ll_paytm);
//			RelativeLayout llMobiKwik = (RelativeLayout) findViewById(R.id.ll_mobikwik);
			RelativeLayout llOnlinePayment = (RelativeLayout) findViewById(R.id.rl_online_payment);
			RelativeLayout llCashOnDelivery = (RelativeLayout) findViewById(R.id.rl_cash_on_delivery);
			RelativeLayout llPayTM = (RelativeLayout) findViewById(R.id.rl_paytm);
			RelativeLayout llMobiKwik = (RelativeLayout) findViewById(R.id.rl_mobikwik);


			llFirstPage = (Button) findViewById(R.id.ll_first_page);
			llSecondPage = (Button) findViewById(R.id.ll_second_page);

			txtItemCount = (TextView) findViewById(R.id.txt_item_count);
			txtSubTotal = (TextView) findViewById(R.id.txt_subtotal);
			txtShippingCharges = (TextView) findViewById(R.id.txt_shipping_charges);
			txtYouSaved = (TextView) findViewById(R.id.txt_you_saved);
			txtCouponDiscount = (TextView) findViewById(R.id.txt_coupon_discount);
			txtTotal = (TextView) findViewById(R.id.txt_total);

			tvItemCount = (TextView) findViewById(R.id.tv_item_count);
			tvSubTotal = (TextView) findViewById(R.id.tv_subtotal);
			tvShippingCharges = (TextView) findViewById(R.id.tv_shipping_charges);
			tvYouSaved = (TextView) findViewById(R.id.tv_you_saved);
			tvCouponDiscount = (TextView) findViewById(R.id.tv_coupon_discount);
			tvTotal = (TextView) findViewById(R.id.tv_total);

			txtItemCount.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txtSubTotal.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtShippingCharges.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtCouponDiscount.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			txtYouSaved.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			txtTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			tvItemCount.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
			tvSubTotal.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			tvShippingCharges.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			tvCouponDiscount.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			tvYouSaved.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			tvTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			etCouponCode = (EditText) findViewById(R.id.edit_coupon_code);
//		orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
			List<CartDetail> cartList = orderReviewBean.getProduct();
			if (cartList != null) {
				for (int i = 0; i < cartList.size(); i++) {
					saving = saving + (cartList.get(i).getQty() * (Float.parseFloat(cartList.get(i).getMrp()) - Float.parseFloat(cartList.get(i).getPrice())));
				}
				saving = saving - (Float.parseFloat(orderReviewBean.getDiscount_amount()));   //- - plus
				tvSubTotal.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getSubTotal())));
				tvYouSaved.setText("Rs." + String.format("%.2f", saving));
				if (orderReviewBean.getShipping_ammount() != null && orderReviewBean.getShipping_ammount().length() > 0) {
					tvShippingCharges.setText("Rs." + Float.parseFloat(orderReviewBean.getShipping_ammount()));
				}
				tvTotal.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getGrandTotal())));
				tvItemCount.setText("Rs." + String.format("%.2f", Float.parseFloat(orderReviewBean.getGrandTotal())));
				if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
					txtItemCount.setText(MySharedPrefs.INSTANCE.getTotalItem() + " item");
				}
			}



			if (orderReviewBean.getCouponCode() != null && !orderReviewBean.getCouponCode().equalsIgnoreCase("null")) {
				llFirstPage.setVisibility(View.GONE);
				llSecondPage.setVisibility(View.VISIBLE);
//			tvEnterCode.setText("Applied Code");
				etCouponCode.setEnabled(false);
				etCouponCode.setText(orderReviewBean.getCouponCode());
				tvCouponDiscount.setText("Rs."+MySharedPrefs.INSTANCE.getCouponAmount());
//			tvMiddleLineCoupon.setBackgroundDrawable(getResources().getDrawable(R.color.gray_1));
			} else {
				llFirstPage.setVisibility(View.VISIBLE);
				llSecondPage.setVisibility(View.GONE);
//			tvEnterCode.setText("Enter Code");
				etCouponCode.setEnabled(true);
				etCouponCode.setHint("Enter coupon code");
//			tvMiddleLineCoupon.setBackgroundDrawable(getResources().getDrawable(R.color.red));
			}

			llFirstPage.setBackgroundResource(R.drawable.pay_selected_btn);
			llSecondPage.setBackgroundResource(R.drawable.pay_selected_btn);

			llFirstPage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (etCouponCode.getText().toString().length() > 0) {
						// need to change the keyboardVisibility here
						getKeyBoardVisibility();
						if (keyboardVisibility)
							UtilityMethods.hideKeyBoard(ReviewOrderAndPay.this);
						new Coupon(mContext, "Apply").execute(strApplyCoupon + etCouponCode.getText().toString());
					} else {
						UtilityMethods.customToast(ToastConstant.SELECT_COUPON_CODE, mContext);
					}
				}
			});

			llSecondPage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (etCouponCode.getText().toString().length() > 0) {
						new Coupon(mContext, "Remove").execute(strRemoveCoupon + etCouponCode.getText().toString());
					} else {
						UtilityMethods.customToast(ToastConstant.SELECT_COUPON_CODE, mContext);
					}
				}
			});

			strApplyCoupon = UrlsConstants.ADD_COUPON + MySharedPrefs.INSTANCE.getUserId() + "&quote_id="
					+ MySharedPrefs.INSTANCE.getQuoteId() + "&couponcode=";

			strRemoveCoupon = UrlsConstants.REMOVE_COUPON + MySharedPrefs.INSTANCE.getUserId() + "&quote_id="
					+ MySharedPrefs.INSTANCE.getQuoteId() + "&couponcode=";
			/****************** PROMO CODE *********************/

			final ImageView ivOnlinePayment = (ImageView) findViewById(R.id.iv_online_payment);
			final ImageView ivCashonDelivery = (ImageView) findViewById(R.id.iv_cash_on_delivery);
			final ImageView ivPayTM = (ImageView) findViewById(R.id.iv_paytm);
			final ImageView ivMobiKwik = (ImageView) findViewById(R.id.iv_mobikwik);

			final TextView btnOnlinePayment = (TextView) findViewById(R.id.btn_online_Payment);
			final TextView btnCashonDelivery = (TextView) findViewById(R.id.btn_cash_on_delivery);
			final ImageView btnPayTM = (ImageView) findViewById(R.id.btn_paytm);
			final ImageView btnMobiKwik = (ImageView) findViewById(R.id.btn_mobikwik);


//		TextView txtSubTotal = (TextView) findViewById(R.id.txt_subtotal);
//		TextView txtShipping = (TextView) findViewById(R.id.txt_shipping);
//		TextView txtYouPay = (TextView) findViewById(R.id.txt_you_pay);
//		TextView txtYouSaved = (TextView) findViewById(R.id.txt_you_saved);
//		TextView txtDiscountReview = (TextView) findViewById(R.id.txt_discount_review);

//		TextView tvSubTotal = (TextView) findViewById(R.id.tv_subTotal_payment);
//		TextView tvShipping = (TextView) findViewById(R.id.tv_shipping_payment);
//		TextView tvYouPay = (TextView) findViewById(R.id.tv_you_pay);
//		TextView tvYouSaved = (TextView) findViewById(R.id.tv_you_saved);
//		TextView tvDiscountReview = (TextView) findViewById(R.id.tv_discount_review);
			TextView tvCreditCard = (TextView) findViewById(R.id.CreditCard);

			ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
			ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
			ivPayTM.setImageResource(R.drawable.chkbox_unselected);         //unselect
			ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);         //unselect

//		btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);       //unselect
//		btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);      //unselect
//		btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);       //unselect
//		btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);       //unselect

//		btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
//		btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));   //unselect
//		btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
//		btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect

			btnOnlinePayment.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			btnCashonDelivery.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			btnPayTM.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			btnMobiKwik.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

			tvCreditCard.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		tvSubTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		tvShipping.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		tvYouPay.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		tvYouSaved.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//		tvDiscountReview.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

//		txtSubTotal.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		txtShipping.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		txtYouPay.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//		txtYouSaved.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//		txtDiscountReview.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			btnOnlinePayment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bCash = false;
					bPayTM = false;
					bMobiKwik = false;
					if (bOnline) {
						bOnline = false;
					} else {
						bOnline = true;
					}

					if (bOnline) {
						ivOnlinePayment.setImageResource(R.drawable.chkbox_selected);           //select
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);                  //unselect
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);                  //unselect

//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_selected_text_color));      //select
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));   //unselect
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					} else {
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);           //unselect
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}
				}
			});

			llOnlinePayment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bCash = false;
					bPayTM = false;
					bMobiKwik = false;
					if (bOnline) {
						bOnline = false;
					} else {
						bOnline = true;
					}

					if (bOnline) {
						ivOnlinePayment.setImageResource(R.drawable.chkbox_selected);           //select
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);         //unselect
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);         //unselect

//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_selected_text_color));      //select
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));   //unselect
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					} else {
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);           //unselect
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}

				}
			});

			btnCashonDelivery.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bOnline = false;
					bPayTM = false;
					bMobiKwik = false;
					if (bCash) {
						bCash = false;
					} else {
						bCash = true;
					}

					if (bCash) {
						ivCashonDelivery.setImageResource(R.drawable.chkbox_selected);        //select
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);       //unselect
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);       //unselect
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);       //unselect

//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_selected_text_color));   //select
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));      //unselect
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					} else {
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);           //unselect
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnCashonDelivery.setBackgroundColor(getResources().getColor(R.color.payment_unselected_btn_bg));  //unselect
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}
				}
			});

			llCashOnDelivery.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bOnline = false;
					bPayTM = false;
					bMobiKwik = false;
					if (bCash) {
						bCash = false;
					} else {
						bCash = true;
					}

					if (bCash) {
						ivCashonDelivery.setImageResource(R.drawable.chkbox_selected);        //select
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);       //unselect
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);       //unselect
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);       //unselect

//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_selected_text_color));   //select
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));      //unselect
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					} else {
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);           //unselect
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnCashonDelivery.setBackgroundColor(getResources().getColor(R.color.payment_unselected_btn_bg));  //unselect
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}

				}
			});


			btnPayTM.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bCash = false;
					bOnline = false;
					bMobiKwik = false;
					if (bPayTM) {
						bPayTM = false;
					} else {
						bPayTM = true;
					}

					if (bPayTM) {
						ivPayTM.setImageResource(R.drawable.chkbox_selected);           //select
//						ivPayTM.setImageResource(R.drawable.check_pay);           //select
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);        //unselect

//					btnPayTM.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_selected_text_color));        //select
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));      //unselect
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));     //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));     //unselect
					} else {
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);           //unselect
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnPayTM.setBackgroundColor(getResources().getColor(R.color.payment_unselected_btn_bg));  //unselect
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}
				}
			});


			llPayTM.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bCash = false;
					bOnline = false;
					bMobiKwik = false;
					if (bPayTM) {
						bPayTM = false;
					} else {
						bPayTM = true;
					}

					if (bPayTM) {
						ivPayTM.setImageResource(R.drawable.chkbox_selected);           //select
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);        //unselect

//					btnPayTM.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_selected_text_color));        //select
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));      //unselect
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));     //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));     //unselect
					} else {
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);           //unselect
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnPayTM.setBackgroundColor(getResources().getColor(R.color.payment_unselected_btn_bg));  //unselect
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}

				}
			});

			btnMobiKwik.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bCash = false;
					bOnline = false;
					bPayTM = false;
					if (bMobiKwik) {
						bMobiKwik = false;
					} else {
						bMobiKwik = true;
					}

					if (bMobiKwik) {
						ivMobiKwik.setImageResource(R.drawable.chkbox_selected);           //select
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);        //unselect

//					btnMobiKwik.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_selected_text_color));        //select
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));      //unselect
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));     //unselect
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));     //unselect
					} else {
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);           //unselect
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnMobiKwik.setBackgroundColor(getResources().getColor(R.color.payment_unselected_btn_bg));  //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}
				}
			});

			llMobiKwik.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bCash = false;
					bOnline = false;
					bPayTM = false;
					if (bMobiKwik) {
						bMobiKwik = false;
					} else {
						bMobiKwik = true;
					}

					if (bMobiKwik) {
						ivMobiKwik.setImageResource(R.drawable.chkbox_selected);                  //select
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);              //unselect
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect

//					btnMobiKwik.setBackgroundResource(R.drawable.pay_selected_btn);
//					btnPayTM.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnCashonDelivery.setBackgroundResource(R.drawable.pay_unselected_btn);
//					btnOnlinePayment.setBackgroundResource(R.drawable.pay_unselected_btn);

//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_selected_text_color));                //select
//					btnPayTM.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));              //unselect
//					btnOnlinePayment.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));      //unselect
//					btnCashonDelivery.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));     //unselect

					} else {
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);                                         //unselect
//					btnMobiKwik.setBackgroundResource(R.drawable.pay_unselected_btn);                            //unselect
//					btnMobiKwik.setBackgroundColor(getResources().getColor(R.color.payment_unselected_btn_bg));  //unselect
//					btnMobiKwik.setTextColor(getResources().getColor(R.color.payment_unselected_text_color));    //unselect
					}

				}
			});


//		mList = (ListView) findViewById(R.id.product_list);

			button_pay = (Button) findViewById(R.id.btn_apply_coupon);
//		button_pay.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

			button_pay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
//					float str1 = Float.parseFloat(orderReviewBean.getGrandTotal());
//					float str2 = Float.parseFloat(orderReviewBean.getShipping_ammount());
//					float str3 = Float.parseFloat(orderReviewBean.getDiscount_amount());
					total = Float.parseFloat(orderReviewBean.getGrandTotal());
//					+ Float.parseFloat(orderReviewBean.getShipping_ammount())		+ Float.parseFloat(orderReviewBean.getDiscount_amount());
					if(!bCash && !bOnline && !bPayTM ){             //!bMobiKwik
						UtilityMethods.customToast(ToastConstant.SELECT_PAYMENT_MODE, mContext);
						return;
					}else

//					if (!bCash && !bOnline) {
//						UtilityMethods.customToast(ToastConstant.SELECT_PAYMENT_MODE, mContext);
//						return;
//					}


						if(bPayTM){
							payment_mode="paytm_cc";
						}else if (bOnline) {
							payment_mode = "payucheckout_shared";
						} else if (bCash) {
							payment_mode = "cashondelivery";
						}



//				else if(bPayTM){
//					payment_mode="paytm";
//				}
//				else if(bMobiKwik){
//					payment_mode="mobikwik";
//				}

					showDialog();
					OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
					String shipping = orderReviewBean.getShipping().toString();
					String billing = orderReviewBean.getBilling().toString();

					String url;
					try {
						url = UrlsConstants.FINAL_CHECKOUT + URLEncoder.encode(shipping, "UTF-8") + "&billing=" + URLEncoder.encode(billing, "UTF-8") + "&userid=" + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&timeslot=" + orderReviewBean.getTimeSlot() + "&date=" + orderReviewBean.getDate() + "&payment_method=" + payment_mode + "&shipping_method=tablerate_bestway";
						System.out.println("=URL OUTPUT==" + url);
						myApi.reqFinalCheckout(url.replaceAll(" ", "%20"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		/*if(Float.parseFloat(orderReviewBean.getShipping_ammount())==0)
		tr_shipping.setVisibility(View.GONE);
		if(Float.parseFloat(orderReviewBean.getDiscount_amount())==0)
		tr_discount.setVisibility(View.GONE);*/

		/*
		 * float total = Float.parseFloat(finalCheckoutBean.getGrandTotal()) +
		 * Float.parseFloat(shipping_amt.getText().toString()) +
		 * Float.parseFloat(tax.getText().toString());
		 */

//			float str1 = Float.parseFloat(orderReviewBean.getGrandTotal());
//			float str2 = Float.parseFloat(orderReviewBean.getShipping_ammount());
//			float str3 = Float.parseFloat(orderReviewBean.getDiscount_amount());

			total = Float.parseFloat(orderReviewBean.getGrandTotal());
//			+ Float.parseFloat(orderReviewBean.getShipping_ammount()) + Float.parseFloat(orderReviewBean.getDiscount_amount());


//		tvSubTotal.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderReviewBean.getGrandTotal())));
//		tvShipping.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderReviewBean.getShipping_ammount())));

//		tvYouSaved.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderReviewBean.getSaving())));
//		tvYouSaved.setText("Rs. "+orderReviewBean.getSaving());
//		tvYouSaved.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderReviewBean.getDiscount_amount())));
//		tvYouPay.setText("Rs. "+String.format("%.2f",Float.parseFloat(String.valueOf(total))));
//		tvDiscountReview.setText("Rs. "+String.format("%.2f",Float.parseFloat(orderReviewBean.getDiscount_amount())));

			initHeader(findViewById(R.id.app_bar_header), true, "Payment Method");
			initFooter(findViewById(R.id.footer), 4, 3);
			icon_header_search.setVisibility(View.GONE);
			icon_header_cart.setVisibility(View.GONE);
			cart_count_txt.setVisibility(View.GONE);
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	String cardNumber;
	String expiryMonth;
	String expiryYear;
	String nameOnCard;
	String cvv;
	Payment payment;
	Payment.Builder builder = new Payment().new Builder();

	private void makePayment(final String orderid) {


		if(!bOnline && !bCash && !bPayTM){
			UtilityMethods.customToast(ToastConstant.SELECT_PAYMENT_MODE, mContext);
			return;
		}
		else if(bOnline){
			final HashMap<String, String> params = new HashMap<String, String>();
			double amount = total;

			params.put("amount",String.valueOf(total));
			params.put("surl",UrlsConstants.CHANGE_ORDER_STATUS+"success.php?orderid="+orderid);
			params.put("furl",UrlsConstants.CHANGE_ORDER_STATUS+"fail.php?orderid="+orderid);
			params.put("user_credentials","yPnUG6:test");
			params.put("key","yPnUG6");
			params.put("txnid",orderid);
			params.put("firstname",MySharedPrefs.INSTANCE.getFirstName()+" "+MySharedPrefs.INSTANCE.getLastName());
			params.put("email",MySharedPrefs.INSTANCE.getUserEmail());
//            params.put("phone", "9999999999");
			params.put("phone",MySharedPrefs.INSTANCE.getMobileNo());
			params.put("productinfo","GrocerMax Product Info");

			params.remove("amount");
			final double finalAmount = amount;

           /* String txnId = orderid;
			String merchant_key =  "yPnUG6";
			String amout = String.valueOf(total);

			String product_info = "GrocerMax shopping";
			String merchant_salt = "jJ0mWFKl";
			String u_fname = "USER_FNAME";
			String u_email = MySharedPrefs.INSTANCE.getUserEmail();



            final double finalAmount = amount;
            StringBuilder checkSumStr = new StringBuilder();
		    MessageDigest digest=null;

		    String hash = "";
		    try {
		        digest = MessageDigest.getInstance("SHA-512");
		        checkSumStr.append(merchant_key);
		        checkSumStr.append("|");

		        checkSumStr.append(txnId);
		        checkSumStr.append("|");
		        checkSumStr.append(amout);
		        checkSumStr.append("|");
		        checkSumStr.append(product_info);

		        checkSumStr.append("|");
		        checkSumStr.append(u_fname);
		        checkSumStr.append("|");
		        checkSumStr.append(u_email);

		        checkSumStr.append("|||||||||||"); // 11 times |
		        checkSumStr.append(merchant_salt);
		        digest.update(checkSumStr.toString().getBytes());
		        hash = bytesToHexString(digest.digest());
		    }

		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		    PayU.paymentHash = hash;

		    PayU.getInstance(ReviewOrderAndPay.this).startPaymentProcess(finalAmount, params);*/

			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... voids) {
					try {
                       /* HttpClient httpclient = new DefaultHttpClient();

                        HttpPost httppost = new HttpPost("http://uat.grocermax.com/webservice/new_services/getmobilehash");
                        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
                        postParams.add(new BasicNameValuePair("txnid", params.get("txnid")));
                        postParams.add(new BasicNameValuePair("amount", String.valueOf(finalAmount)));
                        postParams.add(new BasicNameValuePair("user_credentials",MySharedPrefs.INSTANCE.getUserEmail()));

                        httppost.setEntity(new UrlEncodedFormEntity(postParams));
                        JSONObject response = new JSONObject(EntityUtils.toString(httpclient.execute(httppost).getEntity()));*/

						HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
						HttpGet httpGet = new HttpGet(UrlsConstants.GET_MOBILE_HASH+"txnid="+orderid+"&amount="+String.valueOf(finalAmount)+"&email="+MySharedPrefs.INSTANCE.getUserEmail()+"&fname="+MySharedPrefs.INSTANCE.getFirstName());
						//System.out.println("genrate hash service = "+UrlsConstants.GET_MOBILE_HASH+"txnid="+orderid+"&amount="+String.valueOf(total)+"&email="+MySharedPrefs.INSTANCE.getUserEmail()+"&fname="+"ISHAN");
						httpGet.setHeader("Content-Type", "application/json");
						HttpResponse response1 = null;
            			/*try {*/
						response1 = client.execute(httpGet);
						HttpEntity resEntity = response1.getEntity();
						JSONObject response = new JSONObject(EntityUtils.toString(resEntity));
            			/*} catch (ClientProtocolException e) {
            				e.printStackTrace();
            			} catch (IOException e) {
            				e.printStackTrace();
            			}*/




						// set the hash values here.

						if (response.has("Result")) {
							PayU.merchantCodesHash = response.getJSONObject("Result").getString("merchantCodesHash");
							PayU.paymentHash = response.getJSONObject("Result").getString("paymentHash");
							PayU.vasHash = response.getJSONObject("Result").getString("mobileSdk");
							PayU.ibiboCodeHash = response.getJSONObject("Result").getString("detailsForMobileSdk");

							if (response.getJSONObject("Result").has("deleteHash")) {
								PayU.deleteCardHash = response.getJSONObject("Result").getString("deleteHash");
								PayU.getUserCardHash = response.getJSONObject("Result").getString("getUserCardHash");
								PayU.editUserCardHash = response.getJSONObject("Result").getString("editUserCardHash");
								PayU.saveUserCardHash = response.getJSONObject("Result").getString("saveUserCardHash");
							}

						}
//                        if(mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();

						PayU.getInstance(ReviewOrderAndPay.this).startPaymentProcess(finalAmount, params);
//                            PayU.getInstance(MainActivity.this).startPaymentProcess(finalAmount, params, new PayU.PaymentMode[]{PayU.PaymentMode.CC, PayU.PaymentMode.NB});

					} catch (UnsupportedEncodingException e) {
//                        if(mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
						new GrocermaxBaseException("ReviewOrderAndPay","doInBackground",e.getMessage(), GrocermaxBaseException.UnsupportedEncodingException,"nodetail");
//                        Toast.makeText(ReviewOrderAndPay.this, e.getMessage(), Toast.LENGTH_LONG).show();
					} catch (ClientProtocolException e) {
//                        if(mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
						new GrocermaxBaseException("ReviewOrderAndPay","doInBackground",e.getMessage(), GrocermaxBaseException.CLIENT_PROTOCOL_EXCEPTION,"nodetail");
//                        Toast.makeText(ReviewOrderAndPay.this, e.getMessage(), Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
//                        if(mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
						new GrocermaxBaseException("ReviewOrderAndPay","doInBackground",e.getMessage(), GrocermaxBaseException.JSON_EXCEPTION,"nodetail");
					} catch (IOException e) {
//                        if(mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
						new GrocermaxBaseException("ReviewOrderAndPay","doInBackground",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
					} /*catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                        if(mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }*/
					return null;
				}
			}.execute();


		}
		else
		{
			//new CODConfirm().execute();
		}

	}
	private String bytesToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
			if(requestCode==123)                  //uses when coming from OneTimePassword screen after to register successfully this will finish and go back to loginactivity and loginactivity also uses same funct
			{
				if(resultCode==RESULT_OK) {
					dismissDialog();
					MySharedPrefs.INSTANCE.putTotalItem("0");
					MySharedPrefs.INSTANCE.clearQuote();
					Intent intent = new Intent(ReviewOrderAndPay.this, CODConfirmation.class);
					Bundle call_bundle = new Bundle();
					call_bundle.putString("orderid", order_id);
					call_bundle.putString("status", "success");
					intent.putExtras(call_bundle);
					startActivity(intent);
					finish();
					setResult(RESULT_OK);
					finish();
				}
			}
			if (requestCode == PayU.RESULT) {
				if(resultCode == RESULT_OK) {
					//success
					if(data != null )                     //success
					{
						//  Toast.makeText(this, "Success" + data.getStringExtra("result"), Toast.LENGTH_LONG).show();
						dismissDialog();
						MySharedPrefs.INSTANCE.putTotalItem("0");
						MySharedPrefs.INSTANCE.clearQuote();
					/*Intent intent = new Intent(ReviewOrderAndPay.this, HomeScreen.class);

					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();*/
						Intent intent = new Intent(ReviewOrderAndPay.this, CODConfirmation.class);
						Bundle call_bundle = new Bundle();
						call_bundle.putString("orderid", order_id);
						call_bundle.putString("status", "success");
						intent.putExtras(call_bundle);
						startActivity(intent);
						finish();
					}
				}
				if (resultCode == RESULT_CANCELED) {          //unsuccess
					//failed
//                if(data != null)
//                {
					// Toast.makeText(this, "Failed-ishan" + data.getStringExtra("result"), Toast.LENGTH_LONG).show();
					showDialog();
					myApi.reqSetOrderStatus(UrlsConstants.SET_ORDER_STATUS+order_db_id);
//                }
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onActivityResult",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}


	@Override
	void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub
		try{
			if (bundle.getString("ACTION").equals(MyReceiverActions.FINAL_CHECKOUT)) {
				finalCheckoutBean= (FinalCheckoutBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (finalCheckoutBean.getFlag().equalsIgnoreCase("1")) {
					UtilityMethods.deleteCloneCart(this);
					if(payment_mode.equals("cashondelivery"))
					{
						MySharedPrefs.INSTANCE.putTotalItem("0");
						MySharedPrefs.INSTANCE.clearQuote();
						UtilityMethods.customToast(finalCheckoutBean.getResult(), ReviewOrderAndPay.this);
						Intent intent = new Intent(ReviewOrderAndPay.this, CODConfirmation.class);
						Bundle call_bundle = new Bundle();
						call_bundle.putString("orderid", finalCheckoutBean.getOrderId());
						call_bundle.putString("status", "success");
						intent.putExtras(call_bundle);
						startActivity(intent);
						finish();
					}else if(payment_mode.equalsIgnoreCase("payucheckout_shared")){
						order_id=finalCheckoutBean.getOrderId();
						order_db_id=finalCheckoutBean.getOrderDBID();
						makePayment(finalCheckoutBean.getOrderId());   //just call in case of payu.
					}else if(payment_mode.equalsIgnoreCase("paytm_cc")){
						order_id=finalCheckoutBean.getOrderId();
						order_db_id=finalCheckoutBean.getOrderDBID();
						payTM(order_id);
					}else if(payment_mode.equalsIgnoreCase("wallet")){     //mobikwik
						order_id=finalCheckoutBean.getOrderId();
						order_db_id=finalCheckoutBean.getOrderDBID();
						payMobiKwikWallet(order_id);
					}
				}
			}

//		if (bundle.getString("ACTION").equals(MyReceiverActions.GET_ORDER_STATUS)) {
//			String response= (String) bundle.getSerializable(ConnectionService.RESPONSE);
//			try {
//				JSONObject resJsonObject=new JSONObject(response);
//				if(resJsonObject.getString("flag").equals("1"))
//				{
//					if(resJsonObject.getString("Result").equals("payuprocess"))
//					{
//						showDialog();
//						myApi.reqSetOrderStatus(UrlsConstants.SET_ORDER_STATUS+order_db_id);
//					}
//					else
//					{
//						dismissDialog();
//						MySharedPrefs.INSTANCE.putTotalItem("0");
//						MySharedPrefs.INSTANCE.clearQuote();
//						Intent intent = new Intent(ReviewOrderAndPay.this, HomeScreen.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);
//						finish();
//					}
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
			if (bundle.getString("ACTION").equals(MyReceiverActions.SET_ORDER_STATUS)) {                     //FAILURE
				String response= (String) bundle.getSerializable(ConnectionService.RESPONSE);

				JSONObject resJsonObject=new JSONObject(response);
				if(resJsonObject.getInt("flag")==1)
				{
					dismissDialog();
					MySharedPrefs.INSTANCE.putTotalItem("0");
					MySharedPrefs.INSTANCE.clearQuote();
					Intent intent = new Intent(ReviewOrderAndPay.this, CODConfirmation.class);
					Bundle call_bundle = new Bundle();
					call_bundle.putString("orderid", order_id);
					call_bundle.putString("status", "fail");
//					call_bundle.putString("status", "success");
					intent.putExtras(call_bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}

	}



	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			initHeader(findViewById(R.id.app_bar_header), true, "Payment Method");
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try{
			EasyTracker.getInstance(this).activityStart(this);
//	    	tracker.activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
		}catch(Exception e){}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
			tracker.activityStop(this);
			FlurryAgent.onEndSession(this);
		}catch(Exception e){}
	}


	private void payTM(String orderid){
		Intent intent = new Intent(this,PayTMActivity.class);
		intent.putExtra("amount", String.valueOf(total));
		intent.putExtra("order_id", orderid);
		intent.putExtra("order_db_id",order_db_id);
		startActivity(intent);
	}

	private void payMobiKwikWallet(String orderid){
		Intent walletIntent = new Intent("MobikwikSDK");
		walletIntent.setPackage(getPackageName());
		walletIntent.setType(HTTP.PLAIN_TEXT_TYPE);
		walletIntent.putExtra("orderid", orderid);                           //
		walletIntent.putExtra("debitWallet", String.valueOf("true"));                           //string.valueOf("true")
		walletIntent.putExtra("amount", String.valueOf(total));                    //
		walletIntent.putExtra("cell", "9911500574");                                     //
		walletIntent.putExtra("email", "abhi0124abhi@gmail.com");                                //
		walletIntent.putExtra("paymentOption", "mw");                           //mobi kwik wallet

		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(walletIntent, 0);
		boolean isIntentSafe = activities.size() > 0;
		System.out.println("isIntentSafe " + activities.size());

		startActivity(walletIntent);
	}

}


class Coupon extends AsyncTask<String, String, String>
{
	Context context;
	String strApplyorRemove;
	String strGrandTotal,strShippingCharge;
	//	strCouponCode,strSubTotal,strSubTotalDiscount,strYouSave;
	public Coupon(Context mContext,String strapplyorremove){
		context = mContext;
		strApplyorRemove = strapplyorremove;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		((BaseActivity)context).showDialog();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
//		strApplyorRemove = params[1];
		try {
		HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
		params[0] = params[0].replace(" ", "%20");
		HttpGet httpGet = new HttpGet(params[0]);
		httpGet.setHeader("Content-Type", "application/json");
		HttpResponse response = null;

			response = client.execute(httpGet);
			HttpEntity resEntity = response.getEntity();
			return EntityUtils.toString(resEntity);
		} catch (ClientProtocolException e) {
			((BaseActivity)context).dismissDialog();
			new GrocermaxBaseException("ReviewOrderAndPay","doInBackground",e.getMessage(), GrocermaxBaseException.CLIENT_PROTOCOL_EXCEPTION,"nodetail");
		} catch (IOException e) {
			((BaseActivity)context).dismissDialog();
			new GrocermaxBaseException("ReviewOrderAndPay","doInBackground",e.getMessage(), GrocermaxBaseException.IO_EXCEPTION,"nodetail");
		} catch (Exception e){
			((BaseActivity)context).dismissDialog();
			new GrocermaxBaseException("ReviewOrderAndPay","doInBackground",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try{
			JSONObject jsonObject = new JSONObject(result);
			String strResult = jsonObject.getString("Result");
			String strFlag = jsonObject.getString("flag");
//			JSONArray jsonArray = jsonObject.getJSONArray("CartDetails");

			UtilityMethods.customToast(strResult, context);
			if(strApplyorRemove.equalsIgnoreCase("Apply"))
			{
				if(strFlag.equalsIgnoreCase("1")){  											//success

					JSONObject jsoncartObject = jsonObject.getJSONObject("CartDetails");
					if(jsoncartObject.length() > 0){
						float savee = 0;
						OrderReviewBean orderReviewBean1 = MySharedPrefs.INSTANCE.getOrderReviewBean();
						List<CartDetail> cartList = orderReviewBean1.getProduct();
						if(cartList!=null)
						{
							for(int i=0;i<cartList.size();i++)
							{
								savee = savee+(cartList.get(i).getQty()*(Float.parseFloat(cartList.get(i).getMrp())-Float.parseFloat(cartList.get(i).getPrice())));
							}
							savee = savee+(Float.parseFloat(jsoncartObject.getString("you_save")));
						}


//						Float.parseFloat(jsoncartObject.getString("you_save")
//						orderReviewBean1.setCouponDiscount(String.valueOf(Float.parseFloat(jsoncartObject.getString("you_save"))));
						orderReviewBean1.setCouponCode(jsoncartObject.getString("coupon_code"));
						orderReviewBean1.setCouponSubtotalWithDiscount(jsoncartObject.getString("subtotal_with_discount"));
						orderReviewBean1.setSubTotal(jsoncartObject.getString("subtotal"));
						orderReviewBean1.setShipping_ammount(jsoncartObject.getString("ShippingCharge"));
						orderReviewBean1.setSaving(String.valueOf(savee));
						orderReviewBean1.setDiscount_amount("-" + jsoncartObject.getString("you_save"));
						orderReviewBean1.setGrandTotal(jsoncartObject.getString("grand_total"));
						MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean1);
						MySharedPrefs.INSTANCE.putisCouponApply("true");
						MySharedPrefs.INSTANCE.putCouponAmount(jsoncartObject.getString("you_save"));
						((ReviewOrderAndPay)context).tvCouponDiscount.setText("Rs."+MySharedPrefs.INSTANCE.getCouponAmount());

						((ReviewOrderAndPay)context).tvSubTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean1.getSubTotal())));
						((ReviewOrderAndPay)context).tvShippingCharges.setText("Rs."+Float.parseFloat(orderReviewBean1.getShipping_ammount()));
						((ReviewOrderAndPay)context).tvYouSaved.setText("Rs."+String.format("%.2f",savee));
						((ReviewOrderAndPay)context).tvTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean1.getGrandTotal())));
						((ReviewOrderAndPay)context).tvItemCount.setText("Rs."+String.format("%.2f",Float.parseFloat(orderReviewBean1.getGrandTotal())));

						((ReviewOrderAndPay)context).llFirstPage.setVisibility(View.GONE);
						((ReviewOrderAndPay)context).llSecondPage.setVisibility(View.VISIBLE);
//						((ReviewOrderAndPay)context).tvEnterCode.setText("Applied Code");
						((ReviewOrderAndPay)context).etCouponCode.setEnabled(false);
//						((ReviewOrderAndPay)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.gray_1));

					}
				}else if(strFlag.equalsIgnoreCase("0")){  										//failure
					((ReviewOrderAndPay)context).llFirstPage.setVisibility(View.VISIBLE);
					((ReviewOrderAndPay)context).llSecondPage.setVisibility(View.GONE);
//					((ReviewOrderAndPay)context).tvEnterCode.setText("Enter Code");
					((ReviewOrderAndPay)context).etCouponCode.setEnabled(true);
//					((ReviewOrderAndPay)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.red));
				}
			}else if(strApplyorRemove.equalsIgnoreCase("Remove")){
				if(strFlag.equalsIgnoreCase("1")){  											//Remove success

					JSONObject jsoncartObject = jsonObject.getJSONObject("CartDetails");

					float savee2 = 0;
					OrderReviewBean orderReviewBean2 = MySharedPrefs.INSTANCE.getOrderReviewBean();
					List<CartDetail> cartList = orderReviewBean2.getProduct();
					if(cartList!=null)
					{
						for(int i=0;i<cartList.size();i++)
						{
							savee2 = savee2+(cartList.get(i).getQty()*(Float.parseFloat(cartList.get(i).getMrp())-Float.parseFloat(cartList.get(i).getPrice())));
						}
						savee2 = savee2-(Float.parseFloat(jsoncartObject.getString("you_save")));
					}

					Float totalremove = Float.parseFloat(orderReviewBean2.getGrandTotal()) +
							Float.parseFloat(MySharedPrefs.INSTANCE.getCouponAmount());
					Float couponwithdiscount = Float.parseFloat(orderReviewBean2.getCouponSubtotalWithDsicount()) +
							Float.parseFloat(MySharedPrefs.INSTANCE.getCouponAmount());

					orderReviewBean2.setSubTotal(String.valueOf(Float.parseFloat(orderReviewBean2.getSubTotal())));
					orderReviewBean2.setShipping_ammount(jsoncartObject.getString("ShippingCharge"));
					orderReviewBean2.setSaving(String.valueOf(savee2));
					orderReviewBean2.setDiscount_amount(jsoncartObject.getString("you_save"));
					orderReviewBean2.setGrandTotal(String.valueOf(totalremove));
					orderReviewBean2.setCouponCode(null);
					orderReviewBean2.setCouponSubtotalWithDiscount(String.valueOf(couponwithdiscount));
					MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean2);
					MySharedPrefs.INSTANCE.putisCouponApply("false");
					MySharedPrefs.INSTANCE.putCouponAmount("Rs. 0");
					((ReviewOrderAndPay)context).tvCouponDiscount.setText("Rs.0.00");

					((ReviewOrderAndPay)context).tvSubTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(String.valueOf(Float.parseFloat(orderReviewBean2.getSubTotal())))));
					((ReviewOrderAndPay)context).tvShippingCharges.setText("Rs."+Float.parseFloat(orderReviewBean2.getShipping_ammount()));
					((ReviewOrderAndPay)context).tvYouSaved.setText("Rs."+String.format("%.2f",savee2));
					((ReviewOrderAndPay)context).tvTotal.setText("Rs."+String.format("%.2f",Float.parseFloat(String.valueOf(totalremove))));
					((ReviewOrderAndPay)context).tvItemCount.setText("Rs."+String.format("%.2f",Float.parseFloat(String.valueOf(totalremove))));

					((ReviewOrderAndPay)context).llFirstPage.setVisibility(View.VISIBLE);
					((ReviewOrderAndPay)context).llSecondPage.setVisibility(View.GONE);
//					((ReviewOrderAndPay)context).tvEnterCode.setText("Enter Code");
					((ReviewOrderAndPay)context).etCouponCode.setEnabled(true);
//					((ReviewOrderAndPay)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.red));

				}else if(strFlag.equalsIgnoreCase("0")){  										//Remove failure
					((ReviewOrderAndPay)context).llFirstPage.setVisibility(View.GONE);
					((ReviewOrderAndPay)context).llSecondPage.setVisibility(View.VISIBLE);
//					((ReviewOrderAndPay)context).tvEnterCode.setText("Applied Code");
					((ReviewOrderAndPay)context).etCouponCode.setEnabled(false);
//					((ReviewOrderAndPay)context).tvMiddleLineCoupon.setBackgroundDrawable(((OffersPromoCode)context).getResources().getDrawable(R.color.gray_1));
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onPostExecute",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		((BaseActivity)context).dismissDialog();
	}

}









