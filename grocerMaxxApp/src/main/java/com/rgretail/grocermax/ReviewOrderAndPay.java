package com.rgretail.grocermax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citrus.sdk.Constants;
import com.citrus.sdk.Environment;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.ui.fragments.ResultFragment;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.ResultModel;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.flurry.android.FlurryAgent;
import com.mobikwik.sdk.MobikwikSDK;
import com.mobikwik.sdk.lib.MKTransactionResponse;
import com.mobikwik.sdk.lib.Transaction;
import com.mobikwik.sdk.lib.TransactionConfiguration;
import com.mobikwik.sdk.lib.User;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.payu.sdk.ProcessPaymentActivity;
import com.payu.sdk.exceptions.HashException;
import com.payu.sdk.exceptions.MissingParameterException;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.FinalCheckoutBean;
import com.rgretail.grocermax.bean.OrderReviewBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.MyHttpUtils;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReviewOrderAndPay extends BaseActivity
{
	private Button button_pay;
	private FinalCheckoutBean finalCheckoutBean;
	TextView billing_amt, shipping_amt, tax, grand_total;

	public static String order_id;
	public static String order_db_id;
	private boolean bOnline,bCash,bPayTM,bMobiKwik,bCitrus;
	OrderReviewBean orderReviewBean;
	String payment_mode;
	float total;
	Intent intent;
	//	ProgressDialog mProgressDialog;
	String txnId;
	TextView txtItemCount,txtSubTotal,txtShippingCharges,txtYouSaved,txtTotal,txtCouponDiscount,txtWalletDiscount;
	TextView tvItemCount,tvSubTotal,tvShippingCharges,tvYouSaved,tvTotal,tvCouponDiscount;
	EditText etCouponCode;
	float saving=0;
	String strApplyCoupon;
	String strRemoveCoupon;
	Button llFirstPage;
	Button llSecondPage;
	public static List<CartDetail> cartListGA;
	public static String strTempAmount,strTempSelectedState,strTempTotal,strTempTaxAmount,strTempShippingAmount;
	private String SCREENNAME = "ReviewOrderAndPay-";
	Handler handler = new Handler();

    RelativeLayout llOnlinePayment,llCashOnDelivery,llPayTM,llMobiKwik,llCitrus;
    View view_online_payment,view_paytm,view_citrus,view_mobikwik;
    TextView tv_online_payment_offer,tv_paytm_offer,tv_citrus_offer,tv_my_wallet_offer,tv_mobikwik_offer;


    /*declearation for wallet*/
    TextView tv_my_wallet,tv_wallet_discount;
    ImageView iv_my_wallet;
    RelativeLayout llWallet,rl_wallet_discount;
    double wallet_amount=0.0;
    boolean wallet_checked_status=false;
    View v_my_wallet;
    double t_amount;
    public static final String TAG = "ReviewAndPay-Citrus";
    public static String pause_timeing;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			addActionsInFilter(MyReceiverActions.FINAL_CHECKOUT);
			addActionsInFilter(MyReceiverActions.GET_ORDER_STATUS);
			addActionsInFilter(MyReceiverActions.SET_ORDER_STATUS);
            addActionsInFilter(MyReceiverActions.WALLET_INFO);
			addActionsInFilter(MyReceiverActions.SET_PAYTM_ORDER_STATUS_SUCCESS);

			setContentView(R.layout.checkout_process_3);

			pause_timeing = dateFormat.format(new Date());

			orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();

			TextView tv = (TextView) findViewById(R.id.tv_choose_to_pay);
			tv.setTypeface(CustomFonts.getInstance().getRobotoBlack(this));

			 llOnlinePayment = (RelativeLayout) findViewById(R.id.rl_online_payment);
			 llCashOnDelivery = (RelativeLayout) findViewById(R.id.rl_cash_on_delivery);
			 llPayTM = (RelativeLayout) findViewById(R.id.rl_paytm);
			 llMobiKwik = (RelativeLayout) findViewById(R.id.rl_mobikwik);
             llCitrus = (RelativeLayout) findViewById(R.id.rl_citrus);

             view_online_payment=(View)findViewById(R.id.view_online_payment);
             view_paytm=(View)findViewById(R.id.view_paytm);
             view_citrus=(View)findViewById(R.id.view_citrus);
			 view_mobikwik=(View)findViewById(R.id.view_mobikwik);

            /*--------offer message on payment methods----------------*/
            tv_online_payment_offer=(TextView)findViewById(R.id.tv_online_Payment_offer);
            tv_paytm_offer=(TextView)findViewById(R.id.tv_paytm_offer);
            tv_citrus_offer=(TextView)findViewById(R.id.tv_citrus_offer);
            tv_my_wallet_offer=(TextView)findViewById(R.id.tv_my_wallet_offer);
			tv_mobikwik_offer=(TextView)findViewById(R.id.tv_mobikwik_offer);



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
               // setTotolAfterWalletSelectUnSelect(orderReviewBean.getGrandTotal());
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
				//	try{UtilityMethods.clickCapture(mContext,"","","","",SCREENNAME+AppConstants.GA_EVENT_CODE_APPLIED);}catch(Exception e){}
					if (etCouponCode.getText().toString().length() > 0) {
                        /*  capturing event when coupon is applied*/
                        try{
							UtilityMethods.clickCapture(mContext,"Coupon Apply","","Coupon-"+etCouponCode.getText().toString(),"",MySharedPrefs.INSTANCE.getSelectedCity());
							RocqAnalytics.trackEvent("Coupon Apply", new ActionProperties("Category", "Coupon Apply", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", "Coupon-"+etCouponCode.getText().toString()));
                        }catch(Exception e){}
                        /*----------------------------------------*/

						// need to change the keyboardVisibility here
						getKeyBoardVisibility();
						if (keyboardVisibility)
							UtilityMethods.hideKeyBoard(ReviewOrderAndPay.this);
						new Coupon(mContext, "Apply").execute(strApplyCoupon + etCouponCode.getText().toString());
					} else {
						UtilityMethods.customToast(AppConstants.ToastConstant.SELECT_COUPON_CODE, mContext);
					}
				}
			});

			llSecondPage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//	try{UtilityMethods.clickCapture(mContext,"","","","",SCREENNAME+AppConstants.GA_EVENT_REMOVE_CODE);}catch(Exception e){}
					if (etCouponCode.getText().toString().length() > 0) {

                        /*  capturing event when coupon is removed*/
                        try{
							UtilityMethods.clickCapture(mContext,"Coupon Remove","","Coupon-"+etCouponCode.getText().toString(),"",MySharedPrefs.INSTANCE.getSelectedCity());
							RocqAnalytics.trackEvent("Coupon Remove", new ActionProperties("Category", "Coupon Remove", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", "Coupon-"+etCouponCode.getText().toString()));
                        }catch(Exception e){}
                        /*----------------------------------------*/

						new Coupon(mContext, "Remove").execute(strRemoveCoupon + etCouponCode.getText().toString());
					} else {
						UtilityMethods.customToast(AppConstants.ToastConstant.SELECT_COUPON_CODE, mContext);
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
            final ImageView ivCitrus = (ImageView) findViewById(R.id.iv_citrus);

			final TextView btnOnlinePayment = (TextView) findViewById(R.id.btn_online_Payment);
			final TextView btnCashonDelivery = (TextView) findViewById(R.id.btn_cash_on_delivery);
			final ImageView btnPayTM = (ImageView) findViewById(R.id.btn_paytm);
			final ImageView btnMobiKwik = (ImageView) findViewById(R.id.btn_mobikwik);
            final ImageView btnCitrus = (ImageView) findViewById(R.id.btn_cirus);



			TextView tvCreditCard = (TextView) findViewById(R.id.CreditCard);

			ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
			ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
			ivPayTM.setImageResource(R.drawable.chkbox_unselected);         //unselect
			ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);         //unselect
            ivCitrus.setImageResource(R.drawable.chkbox_unselected);         //unselect




			btnOnlinePayment.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
			btnCashonDelivery.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

			tvCreditCard.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

			btnOnlinePayment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bCash = false;
					bPayTM = false;
					bMobiKwik = false;
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);                  //unselect

					} else {
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);           //unselect
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
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);         //unselect

					} else {
						ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);           //unselect
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
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);       //unselect

					} else {
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);           //unselect
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
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);       //unselect
					} else {
						ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);           //unselect
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
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);        //unselect
					} else {
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);           //unselect
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
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);        //unselect
					} else {
						ivPayTM.setImageResource(R.drawable.chkbox_unselected);           //unselect
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
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);        //unselect
					} else {
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);           //unselect
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
                    bCitrus = false;
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
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);        //unselect

					} else {
						ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);                                         //unselect
					}

				}
			});

            btnCitrus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    bCash = false;
                    bOnline = false;
                    bPayTM = false;
                    bMobiKwik = false;
                    if (bCitrus) {
                        bCitrus = false;
                    } else {
                        bCitrus = true;
                    }

                    if (bCitrus) {
                        ivCitrus.setImageResource(R.drawable.chkbox_selected);           //select
                        ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
                        ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
                        ivPayTM.setImageResource(R.drawable.chkbox_unselected);        //unselect
                        ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);        //unselect
                    } else {
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);           //unselect
                    }
                }
            });

            llCitrus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    bCash = false;
                    bOnline = false;
                    bPayTM = false;
                    bMobiKwik = false;
                    if (bCitrus) {
                        bCitrus = false;
                    } else {
                        bCitrus = true;
                    }

                    if (bCitrus) {
                        ivCitrus.setImageResource(R.drawable.chkbox_selected);                  //select
                        ivPayTM.setImageResource(R.drawable.chkbox_unselected);              //unselect
                        ivOnlinePayment.setImageResource(R.drawable.chkbox_unselected);         //unselect
                        ivCashonDelivery.setImageResource(R.drawable.chkbox_unselected);        //unselect
                        ivMobiKwik.setImageResource(R.drawable.chkbox_unselected);        //unselect

                    } else {
                        ivCitrus.setImageResource(R.drawable.chkbox_unselected);                                         //unselect
                    }

                }
            });



			button_pay = (Button) findViewById(R.id.btn_apply_coupon);

			button_pay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					try{
						UtilityMethods.clickCapture(mContext,"Review and Place order","","","",MySharedPrefs.INSTANCE.getSelectedCity());
						RocqAnalytics.trackEvent("Review and Place order", new ActionProperties("Category", "Review and Place order", "Action", MySharedPrefs.INSTANCE.getSelectedCity()));
                    }catch(Exception e){}

					orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
					total = Float.parseFloat(orderReviewBean.getGrandTotal());


                     /*setting the order detail to send the ecommerce Data On GA at CODConfirmation page on success payment*/
                    strTempAmount = orderReviewBean.getShipping_ammount();
                    strTempSelectedState = MySharedPrefs.INSTANCE.getSelectedState();
                    strTempTotal = orderReviewBean.getGrandTotal();
                    strTempTaxAmount = orderReviewBean.getTax_ammount();
                    strTempShippingAmount = orderReviewBean.getShipping_ammount();

                    cartListGA = orderReviewBean.getProduct();
                  /*---------------------------------------------------------------------------------------------*/


                     /*for checking that if payable amount =0 user should not select a payment method*/
                    if(Double.parseDouble(tvTotal.getText().toString().replace("Rs.","").trim())==0){
                        if(bCash || bOnline || bPayTM || bCitrus || bMobiKwik){             //!bMobiKwik
                            UtilityMethods.customToast(AppConstants.ToastConstant.NO_NEED_SELECT_PAYMENT_MODE, mContext);
                            return;
                        }else{
                            payment_mode = "cashondelivery";
                        }
                    }else{
                        if(!bCash && !bOnline && !bPayTM && !bCitrus && !bMobiKwik){             //!bMobiKwik
                            UtilityMethods.customToast(AppConstants.ToastConstant.SELECT_PAYMENT_MODE, mContext);
                            return;
                        }
                    }

                    /*end here*/


					//in case user click once but server timed out occured then btn should disable for next time.
					button_pay.setEnabled(false);
					button_pay.setVisibility(View.GONE);

						if(bPayTM){
							payment_mode="paytm_cc";
						}else if (bOnline) {
							payment_mode = "payucheckout_shared";
						} else if (bCash) {
							payment_mode = "cashondelivery";
						}else if(bCitrus){
                            payment_mode = "moto"; // for citrus pay
                        }else if (bMobiKwik) {
							payment_mode = "wallet";
						}


					showDialog();
					OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
					//String shipping = orderReviewBean.getShipping().toString();
					//String billing = orderReviewBean.getBilling().toString();

					try {
						////////////////POST/////////////
						String strurl = UrlsConstants.FINAL_CHECKOUT;
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("shipping", orderReviewBean.getShipping());
						jsonObject.put("billing", orderReviewBean.getBilling());
						jsonObject.put("userid", MySharedPrefs.INSTANCE.getUserId());
						jsonObject.put("quote_id", MySharedPrefs.INSTANCE.getQuoteId());
						jsonObject.put("timeslot", orderReviewBean.getTimeSlot());
						jsonObject.put("date", orderReviewBean.getDate());
						jsonObject.put("payment_method", payment_mode);
						jsonObject.put("shipping_method", "tablerate_bestway");
                        if(wallet_checked_status==true){
                            String total=tvItemCount.getText().toString().replace("Rs.","").trim();
                            jsonObject.put("payment_method_Wallet", "customercredit");
                            jsonObject.put("wallet", "1");
                            jsonObject.put("internalWalletAmount",String.valueOf(wallet_amount));
                            jsonObject.put("totalAmount", total);
                        }

						System.out.println("====payment json format====" + jsonObject);

                        if(payment_mode.equals("moto")) {
                            if (MySharedPrefs.INSTANCE.getMobileNo().equals("0000000000") || MySharedPrefs.INSTANCE.getMobileNo().equals("") || MySharedPrefs.INSTANCE.getMobileNo() == null || !UtilityMethods.isValidPhone(MySharedPrefs.INSTANCE.getMobileNo())){
                                dismissDialog();
                                button_pay.setEnabled(true);
                                button_pay.setVisibility(View.VISIBLE);
                                UtilityMethods.customToast("Your contact number is not valid.Please change your contact number", mContext);
                                Intent intent = new Intent(mContext, EditProfile.class);
                                startActivityForResult(intent,121);
                            }else{
                                myApi.reqFinalCheckout(strurl.replaceAll(" ", "%20"), jsonObject);
                            }
                        }else{
						myApi.reqFinalCheckout(strurl.replaceAll(" ", "%20"), jsonObject);
                        }


						///////code added by ishan///////
						/*if(payment_mode.equals("cashondelivery")) {
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									//Do something after 10 sec

									MySharedPrefs.INSTANCE.putTotalItem("0");
									MySharedPrefs.INSTANCE.clearQuote();
									Intent intent = new Intent(ReviewOrderAndPay.this, CODConfirmation.class);
									Bundle call_bundle = new Bundle();
									call_bundle.putString("orderid", "no_order_id");
									call_bundle.putString("status", "success");
									intent.putExtras(call_bundle);
									startActivity(intent);
									finish();
								}

							}, 10000);
						}*/
						///////////---------/////////////////

						////////////////POST/////////////
					}catch(Exception e){}


//					} catch (UnsupportedEncodingException e) {
//						 TODO Auto-generated catch block
//						e.printStackTrace();
//					}

				}
			});

			total = Float.parseFloat(orderReviewBean.getGrandTotal());

			initHeader(findViewById(R.id.app_bar_header), true, "Payment Method");
			initFooter(findViewById(R.id.footer), 4, 3);
			icon_header_search.setVisibility(View.GONE);
			icon_header_cart.setVisibility(View.GONE);
			cart_count_txt.setVisibility(View.GONE);


              /*initialization of view for wallet*/
            t_amount=Double.parseDouble(orderReviewBean.getGrandTotal());
            llWallet = (RelativeLayout) findViewById(R.id.rl_wallet);
            rl_wallet_discount=(RelativeLayout)findViewById(R.id.rl_wallet_discount);
            tv_wallet_discount=(TextView)findViewById(R.id.tv_wallet_discount);
            txtWalletDiscount=(TextView)findViewById(R.id.txt_wallet_discount);
            txtWalletDiscount.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
            tv_my_wallet=(TextView)findViewById(R.id.btn_my_wallet);
            tv_my_wallet.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
            iv_my_wallet=(ImageView)findViewById(R.id.iv_my_wallet);
            v_my_wallet=(View)findViewById(R.id.v_my_wallet);
            llWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(wallet_checked_status==false){
                        /*if wallet get selected*/
                        iv_my_wallet.setImageResource(R.drawable.chkbox_selected);
                        wallet_checked_status=true;
                        rl_wallet_discount.setVisibility(View.VISIBLE);
                        v_my_wallet.setVisibility(View.VISIBLE);
                        setTotolAfterWalletSelectUnSelect(t_amount);
                    }else{
                        /*if wallet get unselected*/
                        iv_my_wallet.setImageResource(R.drawable.chkbox_unselected);
                        wallet_checked_status=false;
                        rl_wallet_discount.setVisibility(View.GONE);
                        v_my_wallet.setVisibility(View.GONE);
                        //t_amount=Double.parseDouble(orderReviewBean.getGrandTotal());
                        setTotolAfterWalletSelectUnSelect(t_amount);
                    }
                }
            });

            try {
                showDialog();
                myApi.reqWallet(UrlsConstants.WALLET_INFO_URL + MySharedPrefs.INSTANCE.getUserId());
            } catch (Exception e) {
                new GrocermaxBaseException("ReviewOrderAndPay","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting wallet amount");
            }
            /*end*/

		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

    /*-----------Make payment when online payment/PayU selected-------------*/
	private void makePayment(final String orderid) {
		try {

			if (!bOnline && !bCash && !bPayTM && !bCitrus) {
				UtilityMethods.customToast(AppConstants.ToastConstant.SELECT_PAYMENT_MODE, mContext);
				return;
			} else if (bOnline) {
				final HashMap<String, String> params = new HashMap<String, String>();
				//double amount = total;
                double amount=Double.parseDouble(tvTotal.getText().toString().replace("Rs.",""));

				//params.put("amount", String.valueOf(total));
                params.put("amount", String.valueOf(amount));
				params.put("surl", UrlsConstants.CHANGE_ORDER_STATUS + "success.php?orderid=" + orderid);
				params.put("furl", UrlsConstants.CHANGE_ORDER_STATUS + "fail.php?orderid=" + orderid);
				//params.put("user_credentials", "yPnUG6:test");
                params.put("user_credentials", "yPnUG6:"+MySharedPrefs.INSTANCE.getUserEmail());
				params.put("key", "yPnUG6");
				params.put("txnid", orderid);
//				params.put("firstname", MySharedPrefs.INSTANCE.getFirstName() + " " + MySharedPrefs.INSTANCE.getLastName());
				params.put("firstname", MySharedPrefs.INSTANCE.getUserEmail());
				params.put("email", MySharedPrefs.INSTANCE.getUserEmail());
//            params.put("phone", "9999999999");
				params.put("phone", MySharedPrefs.INSTANCE.getMobileNo());
				params.put("productinfo", "GrocerMax Product Info");

				params.remove("amount");
				final double finalAmount = amount;

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... voids) {
						try {

							HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
							HttpGet httpGet = new HttpGet(UrlsConstants.GET_MOBILE_HASH + "txnid=" + orderid + "&amount=" + String.valueOf(finalAmount) + "&email=" + MySharedPrefs.INSTANCE.getUserEmail().trim() + "&fname=" + MySharedPrefs.INSTANCE.getFirstName().replaceAll(" ", "%20"));
//							System.out.println("genrate hash service = "+UrlsConstants.GET_MOBILE_HASH + "txnid=" + orderid + "&amount=" + String.valueOf(finalAmount) + "&email=" + MySharedPrefs.INSTANCE.getUserEmail() + "&fname=" + MySharedPrefs.INSTANCE.getFirstName().replace(" ", "%20"));
							httpGet.setHeader("Content-Type", "application/json");
                            httpGet.setHeader("device", getResources().getString(R.string.app_device));
                            httpGet.setHeader("version",getResources().getString(R.string.app_version));
                            if(MySharedPrefs.INSTANCE.getSelectedStateId() != null) {
                                httpGet.setHeader("storeid", MySharedPrefs.INSTANCE.getSelectedStoreId());
                            }
							HttpResponse response1 = null;
							response1 = client.execute(httpGet);
							HttpEntity resEntity = response1.getEntity();
							JSONObject response = new JSONObject(EntityUtils.toString(resEntity));

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

							PayU.getInstance(ReviewOrderAndPay.this).startPaymentProcess(finalAmount, params);

						} catch (UnsupportedEncodingException e) {
							new GrocermaxBaseException("ReviewOrderAndPay", "doInBackground", e.getMessage(), GrocermaxBaseException.UnsupportedEncodingException, "nodetail");
						} catch (ClientProtocolException e) {
							new GrocermaxBaseException("ReviewOrderAndPay", "doInBackground", e.getMessage(), GrocermaxBaseException.CLIENT_PROTOCOL_EXCEPTION, "nodetail");
						} catch (JSONException e) {
							new GrocermaxBaseException("ReviewOrderAndPay", "doInBackground", e.getMessage(), GrocermaxBaseException.JSON_EXCEPTION, "nodetail");
						} catch (IOException e) {
							new GrocermaxBaseException("ReviewOrderAndPay", "doInBackground", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
						}
						return null;
					}
				}.execute();


			} else {
				//new CODConfirm().execute();
			}

		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay", "makePayment", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}

	}

    /*-----------make payment when PayUMoney is selected--------------------------*/
    public void payumoneyMakePayment(String orderid) throws PackageManager.NameNotFoundException, MissingParameterException, HashException {
        // oops handle it here.


        double total=Double.parseDouble(tvTotal.getText().toString().replace("Rs.",""));
        Payment.Builder builder = new Payment().new Builder();
        Params requiredParams = new Params();
        builder.set(PayU.PRODUCT_INFO, "GrocerMax Product Info");
        builder.set(PayU.AMOUNT, String.valueOf(total));
        builder.set(PayU.TXNID, orderid);
        builder.set(PayU.EMAIL, MySharedPrefs.INSTANCE.getUserEmail());
        builder.set(PayU.FIRSTNAME, MySharedPrefs.INSTANCE.getFirstName() + " " + MySharedPrefs.INSTANCE.getLastName());
        builder.set("user_credentials", "yPnUG6:test");
        builder.set(PayU.SURL, "https://payu.herokuapp.com/success");
        builder.set(PayU.FURL, "https://payu.herokuapp.com/failure");
        builder.set(PayU.MODE, String.valueOf(PayU.PaymentMode.PAYU_MONEY));


        requiredParams.put(PayU.AMOUNT, builder.get(PayU.AMOUNT));
        requiredParams.put(PayU.PRODUCT_INFO, builder.get(PayU.PRODUCT_INFO));
        requiredParams.put(PayU.TXNID, builder.get(PayU.TXNID));
        requiredParams.put(PayU.SURL, builder.get(PayU.SURL));

        requiredParams.put(PayU.EMAIL, MySharedPrefs.INSTANCE.getUserEmail());
        requiredParams.put(PayU.FIRSTNAME, MySharedPrefs.INSTANCE.getFirstName() + " " + MySharedPrefs.INSTANCE.getLastName());
        requiredParams.put("user_credentials", "yPnUG6:test");
        requiredParams.put(PayU.SURL, builder.get(PayU.SURL));
        requiredParams.put(PayU.FURL, builder.get(PayU.FURL));
        requiredParams.put(PayU.MODE,builder.get(PayU.MODE));

        Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
        requiredParams.put(PayU.MERCHANT_KEY, bundle.getString("payu_merchant_id"));


        Payment payment = builder.create();

        String postData = PayU.getInstance(ReviewOrderAndPay.this).createPayment(payment, requiredParams);

        Intent intent = new Intent(this, ProcessPaymentActivity.class);
        intent.putExtra(com.payu.sdk.Constants.POST_DATA, postData);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivityForResult(intent, PayU.RESULT);

    }



    public void setTotolAfterWalletSelectUnSelect(double total_amount){
        t_amount=total_amount;
        if(wallet_checked_status==true){
                        /*if wallet is selected*/
            if(t_amount<=wallet_amount){
                tv_wallet_discount.setText("Rs."+String.format("%.2f",total_amount));
                //t_amount=0.0;
                tvTotal.setText("Rs.0.00");
            }else{
                //t_amount=t_amount-wallet_amount;
                tvTotal.setText("Rs."+String.format("%.2f",(t_amount-wallet_amount)));
                tv_wallet_discount.setText("Rs."+String.format("%.2f",wallet_amount));
            }
        }else{
                        /*if wallet is unselected*/
            tvTotal.setText("Rs."+String.format("%.2f",t_amount));
            tv_wallet_discount.setText("Rs.0.00");
        }

    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		System.out.println(requestCode+"======0000==========="+resultCode+"=========data=========="+data);
		try{

			if(requestCode==123)                  //uses when coming from OneTimePassword screen after to register successfully this will finish and go back to loginactivity and loginactivity also uses same funct
			{
				System.out.println(requestCode+"======0000==========="+data);
				if(resultCode==RESULT_OK) {
					dismissDialog();
					System.out.println(requestCode + "======0000===========" + data);
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
            /*handle response from PayUMoney payment*/
			if (requestCode == PayU.RESULT) {
				if(resultCode == RESULT_OK) {
					//success
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
//					}
				}
				if (resultCode == RESULT_CANCELED) {          //unsuccess payu

					showDialog();

					String url = UrlsConstants.SET_ORDER_STATUS;
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("status","canceled");
					jsonObject.put("orderid",order_id);
					jsonObject.put("orderdbid",order_db_id);
					myApi.reqSetOrderStatus(url, jsonObject);
				}
			}
            /*--------------End of PayUMoney payment handling-----------------------------------*/



            /*handle response from citrus payment*/
            if(requestCode == Constants.REQUEST_CODE_PAYMENT ) {
                int success_code=1; // 1 for payment fail
                if (resultCode == RESULT_OK && data != null) {

                    TransactionResponse transactionResponse=null;
                    ResultModel resultModel=null;

                    try {
                        // You will get data here if transaction flow is started through pay options other than wallet
                        transactionResponse = data.getParcelableExtra(Constants.INTENT_EXTRA_TRANSACTION_RESPONSE);
                        // You will get data here if transaction flow is started through wallet
                        resultModel = data.getParcelableExtra(ResultFragment.ARG_RESULT);
                    } catch (Exception e) {
						changeOrderStatusAndGotoConfirmationPage(1);

                    }

                    // Check which object is non-null
                    if (transactionResponse != null && transactionResponse.getJsonResponse() != null) {
                        // Decide what to do with this data
                        Log.d(TAG, "transaction response" + transactionResponse.getJsonResponse());
                        if(transactionResponse.getResponseCode().equals("0"))
                            success_code=0; //success
                        else
                            success_code=1; //fail

                    } else if (resultModel != null && resultModel.getTransactionResponse() != null) {
                        // Decide what to do with this data
                        Log.d(TAG, "result response" + resultModel.getTransactionResponse().getTransactionId());
                        if(resultModel.getTransactionResponse().getResponseCode().equals("0"))
                            success_code=0;  //success
                        else
                            success_code=1;  //fail
                    } else {
                        Log.d(TAG, "Both objects are null!");
                        success_code=1;  //fail
                    }
                }

                /* sending payment response to our server*/
				changeOrderStatusAndGotoConfirmationPage(success_code);
                /*----------------End of citrus payment handling --------------------------------------------*/
            }
			/*mobikwik payment handling*/
			if (requestCode == 1221 ) {
				if (data != null ) {
					MKTransactionResponse response = (MKTransactionResponse)data.getSerializableExtra(MobikwikSDK. EXTRA_TRANSACTION_RESPONSE );
					System.out.println("CheckoutActivity.onActivityResult() " + response. statusMessage );
					System.out.println("CheckoutActivity.onActivityResult() " + response. statusCode );
					if(response.statusCode.equals("0")){
						//success
						changeOrderStatusAndGotoConfirmationPage(0);
					}else{
						//failure
						changeOrderStatusAndGotoConfirmationPage(1);
					}

				}
			}
           /*-------------------------------------------------------*/
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onActivityResult",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

public void changeOrderStatusAndGotoConfirmationPage(int success_code){
	try {
		String url;
		if (payment_mode.equals("moto")) {
			url = UrlsConstants.SET_ORDER_STATUS;
			JSONObject jsonObject = new JSONObject();
			if(success_code==0)
				jsonObject.put("status","paymentsuccess");
			else
				jsonObject.put("status","canceled");

			jsonObject.put("orderid",order_id);
			jsonObject.put("orderdbid",order_db_id);
			jsonObject.put("payment_method",payment_mode);
			myApi.reqSetOrderStatusForCitrus(url, jsonObject);
		} else {
			url = UrlsConstants.SET_PAYTM_ORDER_STATUS_SUCCESS+"?orderid="+order_id+"&status="+"success";;
			myApi.reqSetOrderStatusPaytmSuccess(url);
		}


                /*sending on confirmation page*/
		MySharedPrefs.INSTANCE.putTotalItem("0");
		MySharedPrefs.INSTANCE.clearQuote();
		Intent intent = new Intent(ReviewOrderAndPay.this, CODConfirmation.class);
		Bundle call_bundle = new Bundle();
		call_bundle.putString("orderid", order_id);

		if(success_code==0)
            call_bundle.putString("status", "success");
        else
            call_bundle.putString("status", "fail");

		intent.putExtras(call_bundle);
		startActivity(intent);
		finish();
	} catch (Exception e) {
		e.printStackTrace();
	}
}


   public void citrusPayment(String citrus_order_id){


      /*This is the sand box credentials*/
       /*CitrusFlowManager.initCitrusConfig("q4qz4sa1wq-signup", "915112088057be17d992162f88eeb7f9",
               "q4qz4sa1wq-signin", "dca4f2179ade2454aaee0194be186774",
               getResources().getColor(R.color.citrus_white), ReviewOrderAndPay.this,
               Environment.SANDBOX, "q4qz4sa1wq",
               UrlsConstants.CITRUS_BILL_GENRATOR+""+citrus_order_id,
               UrlsConstants.CITRUS_RETURN_URL);*/

      /*This is the live credentilas*/
       CitrusFlowManager.initCitrusConfig("78s6jg7d4j-signup", "5e8816c36e20ee00fede9ac7b754b457",
               "78s6jg7d4j-signin", "2113fba06ef2fc41cab120983c80c10d",
               getResources().getColor(R.color.citrus_white), ReviewOrderAndPay.this,
               Environment.PRODUCTION, "prepaid",
               UrlsConstants.CITRUS_BILL_GENRATOR+""+citrus_order_id,
               UrlsConstants.CITRUS_RETURN_URL);


       /*This credential provided by citrus */
      /* CitrusFlowManager.initCitrusConfig("8x5hn2kbpc-signup","2b591f683aa3cf1426fd2a1103c5d845",
                                          "8x5hn2kbpc-signin","2996366165262aeb051533c6f7a78230",
                                           getResources().getColor(R.color.citrus_white), ReviewOrderAndPay.this,
                                           Environment.SANDBOX, "8x5hn2kbpc",
                                           "https://salty-plateau-1529.herokuapp.com/billGenerator.sandbox.php",
                                           "https://salty-plateau-1529.herokuapp.com/redirectURL.sandbox.php");*/


       //String amount=String.format("%.1f", Float.parseFloat(tvTotal.getText().toString().replace("Rs.","")));
       String amount=tvTotal.getText().toString().replace("Rs.","");
       if(amount.contains(".")){
           amount=amount.substring(0,amount.indexOf(".")+2);
       }



       CitrusFlowManager.startShoppingFlow(ReviewOrderAndPay.this,MySharedPrefs.INSTANCE.getUserEmail(),MySharedPrefs.INSTANCE.getMobileNo(),amount);
   }



	@Override
	public void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub
		try{
			if (bundle.getString("ACTION").equals(MyReceiverActions.FINAL_CHECKOUT)) {
				finalCheckoutBean= (FinalCheckoutBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (finalCheckoutBean.getFlag().equalsIgnoreCase("1")) {


					String amount=String.format("%.2f",Double.parseDouble(finalCheckoutBean.getSubTotal()));
					tvTotal.setText("Rs."+amount);

					UtilityMethods.deleteCloneCart(this);

					orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
					 strTempAmount = orderReviewBean.getShipping_ammount();
					 strTempSelectedState = MySharedPrefs.INSTANCE.getSelectedState();
					 strTempTotal = orderReviewBean.getGrandTotal();
					 strTempTaxAmount = orderReviewBean.getTax_ammount();
					strTempShippingAmount = orderReviewBean.getShipping_ammount();

					cartListGA = orderReviewBean.getProduct();           //make temporary copy of products,below step delete all infn.

					if(payment_mode.equals("cashondelivery"))
					{
						//////code added by Ishan//////////
						/*handler.removeCallbacksAndMessages(null);
						handler.getLooper().quit();*/
						//////////////////////////////////

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
					}else if(payment_mode.equalsIgnoreCase("moto")){
                        order_id=finalCheckoutBean.getOrderId();
                        order_db_id=finalCheckoutBean.getOrderDBID();
                        citrusPayment(order_id);
                    }
				}else if(finalCheckoutBean.getFlag().equalsIgnoreCase("2")){
                  showPopUpForExtraWork(finalCheckoutBean.getFlag(),finalCheckoutBean.getResult());
				}else if(finalCheckoutBean.getFlag().equalsIgnoreCase("3")){
					showPopUpForExtraWork(finalCheckoutBean.getFlag(),finalCheckoutBean.getResult());
				}else{
					UtilityMethods.customToast(finalCheckoutBean.getResult(), activity);
				}
			}

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
					intent.putExtras(call_bundle);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			}

             if (bundle.getString("ACTION").equals(MyReceiverActions.WALLET_INFO)) {
                 dismissDialog();
                try
                {
                    String Response = (String) bundle.getSerializable(ConnectionService.RESPONSE);
                    //System.out.println("reorder Response = "+quoteResponse);
                    JSONObject walletResponse=new JSONObject(Response);
                    if(walletResponse.getInt("flag")==1){
                        wallet_amount=walletResponse.getDouble("Balance");
                        if(wallet_amount<=0){
                            //tv_walletAmount.setText("0.00");
                            tv_my_wallet.setText("My Wallet ("+getResources().getString(R.string.Rs)+"0.00)");
                            iv_my_wallet.setVisibility(View.GONE);
                        }
                        else{
                            String w_amount=String.format("%.2f",wallet_amount);
                            tv_my_wallet.setText("My Wallet ("+getResources().getString(R.string.Rs)+" "+w_amount+")");
                            //tv_my_wallet.setText("My Wallet ("+getResources().getString(R.string.Rs)+String.format('"&.2f",wallet_amount)+")");
                            iv_my_wallet.setVisibility(View.VISIBLE);
                        }
                    }else{
                        tv_my_wallet.setText("My Wallet ("+getResources().getString(R.string.Rs)+"0.00)");
                        iv_my_wallet.setVisibility(View.GONE);
                    }

                    /*Displaying the payment option based on server response*/
                        try{
                         JSONObject payment=walletResponse.getJSONObject("payment");
                          /*to check if payment with internal wallet will be available or not*/
                            if(payment.has("customercredit")){
                                llWallet.setVisibility(View.VISIBLE);
                                v_my_wallet.setVisibility(View.VISIBLE);
                                if(payment.getJSONObject("customercredit").getString("mobile_label")!=null && !payment.getJSONObject("customercredit").getString("mobile_label").equals("null"))
                                tv_my_wallet_offer.setText(payment.getJSONObject("customercredit").getString("mobile_label"));
                                else
                                tv_my_wallet_offer.setText("");
                            }else{
                                llWallet.setVisibility(View.GONE);
                                v_my_wallet.setVisibility(View.GONE);
                            }

                          /*to check if payment with paytm will be available or not*/
                          if(payment.has("paytm_cc")){
                            llPayTM.setVisibility(View.VISIBLE);
                            view_paytm.setVisibility(View.VISIBLE);
                              if(payment.getJSONObject("paytm_cc").getString("mobile_label")!=null && !payment.getJSONObject("paytm_cc").getString("mobile_label").equals("null"))
                              tv_paytm_offer.setText(payment.getJSONObject("paytm_cc").getString("mobile_label"));
                              else
                              tv_paytm_offer.setText("");
                          }else{
                              llPayTM.setVisibility(View.GONE);
                              view_paytm.setVisibility(View.GONE);
                          }
                         /*to check if payment with payU will be available or not*/
                        if(payment.has("payucheckout_shared")){
                            llOnlinePayment.setVisibility(View.VISIBLE);
                            view_online_payment.setVisibility(View.VISIBLE);
                            if(payment.getJSONObject("payucheckout_shared").getString("mobile_label")!=null && !payment.getJSONObject("payucheckout_shared").getString("mobile_label").equals("null"))
                            tv_online_payment_offer.setText(payment.getJSONObject("payucheckout_shared").getString("mobile_label"));
                            else
                            tv_online_payment_offer.setText("");
                        }else{
                            llOnlinePayment.setVisibility(View.GONE);
                            view_online_payment.setVisibility(View.GONE);
                        }
                        /*to check if payment with Citrus will be available or not*/
                        if(payment.has("moto")){
                            llCitrus.setVisibility(View.VISIBLE);
                            view_citrus.setVisibility(View.VISIBLE);
                            if(payment.getJSONObject("moto").getString("mobile_label")!=null && !payment.getJSONObject("moto").getString("mobile_label").equals("null"))
                            tv_citrus_offer.setText(payment.getJSONObject("moto").getString("mobile_label"));
                            else
                            tv_citrus_offer.setText("");
                        }else{
                            llCitrus.setVisibility(View.GONE);
                            view_citrus.setVisibility(View.GONE);
                        }

						/*to check if payment with Mobikwik will be available or not*/
						if(true || payment.has("wallet")){
							llMobiKwik.setVisibility(View.VISIBLE);
							view_mobikwik.setVisibility(View.VISIBLE);
							if(payment.getJSONObject("wallet").getString("mobile_label")!=null && !payment.getJSONObject("wallet").getString("mobile_label").equals("null"))
								tv_mobikwik_offer.setText(payment.getJSONObject("wallet").getString("mobile_label"));
							else
								tv_mobikwik_offer.setText("");
						}else{
							llMobiKwik.setVisibility(View.GONE);
							view_mobikwik.setVisibility(View.GONE);
						}

                        }catch (Exception e){
                            new GrocermaxBaseException("ReviewOrderAndPay","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in getting payment option to be displayed");
                        }
                    /*-----------------------------------------------------*/

                }catch(Exception e)
                {
                    new GrocermaxBaseException("ReviewOrderAndPay","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in wallet");
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
			String resume_timing = dateFormat.format(new Date());
			System.out.println("resume timing=" + resume_timing);
			Date d1 = null;
			Date d2 = null;
			try {
				d1 = dateFormat.parse(pause_timeing);
				d2 = dateFormat.parse(resume_timing);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long diff = d2.getTime() - d1.getTime();
			long diffSecond = diff / (1000);
			System.out.println("diff timing=" + diffSecond);
			if(diffSecond>(60*Float.parseFloat(MySharedPrefs.INSTANCE.getResumeTime()))){
				Intent intent = new Intent(mContext, HomeScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}else{
				initHeader(findViewById(R.id.app_bar_header), true, "Payment Method");
			}
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	/*@Override
	public void onPause() {
		super.onPause();
		try {
			pause_timeing = dateFormat.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try{
//			EasyTracker.getInstance(this).activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
		}catch(Exception e){}
		 /*screen tracking using rocq*/
		try {
			RocqAnalytics.initialize(this);
			RocqAnalytics.startScreen(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
       /*------------------------------*/
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
			pause_timeing = dateFormat.format(new Date());
			System.out.println("pause timing="+pause_timeing);
			FlurryAgent.onEndSession(this);
		}catch(Exception e){}
		try {
			RocqAnalytics.stopScreen(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void payTM(String orderid){
		try {
			Intent intent = new Intent(this, PayTMActivity.class);
			//intent.putExtra("amount", String.valueOf(total));
            intent.putExtra("amount",tvTotal.getText().toString().replace("Rs.",""));
			intent.putExtra("order_id", orderid);
			intent.putExtra("order_db_id", order_db_id);
			startActivity(intent);
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","payTM",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private void payMobiKwikWallet(String orderid){
		try {

			TransactionConfiguration config = new TransactionConfiguration();
			config.setDebitWallet(true);
			config.setPgResponseUrl(UrlsConstants.NEW_BASE_URL+"mobikwik/sdkresponse.php");
			config.setChecksumUrl(UrlsConstants.NEW_BASE_URL+"mobikwik/sdkchecksum.php");
			config.setMerchantName("Grocermax");
			config.setAllowMixedContent(true);
			//config.setMbkId("MBK9002");
			config.setMbkId("MBK8170");
			config.setMode("1");

			User user=new User(MySharedPrefs.INSTANCE.getUserEmail(),MySharedPrefs.INSTANCE.getMobileNo());
			System.out.println("Amount="+tvTotal.getText().toString().replace("Rs.", "")+"---");
			Transaction newTransaction=Transaction.Factory.newTransaction(user,orderid,tvTotal.getText().toString().replace("Rs.", ""));

			Intent mobikwikIntent = new Intent( this , MobikwikSDK. class );
			mobikwikIntent.putExtra(MobikwikSDK. EXTRA_TRANSACTION_CONFIG , config);
			mobikwikIntent.putExtra(MobikwikSDK. EXTRA_TRANSACTION , newTransaction);
			startActivityForResult(mobikwikIntent, 1221);


		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay","payMobiKwikWallet",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	public void showPopUpForExtraWork(final String flag,String msg){
		Typeface typeface=Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
		Typeface typeface1=Typeface.createFromAsset(getAssets(),"Roboto-Light.ttf");
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReviewOrderAndPay.this);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
		alertDialog.setView(dialogView);
		alertDialog.setCancelable(false);
		final AlertDialog alert = alertDialog.create();
		TextView tv_msg=(TextView)dialogView.findViewById(R.id.tv_msg);
		tv_msg.setTypeface(typeface1);
		tv_msg.setText(msg);
		TextView tv_skip = (TextView) dialogView.findViewById(R.id.tv_skip);
		tv_skip.setTypeface(typeface);
		tv_skip.setVisibility(View.GONE);
		TextView tv_update=(TextView)dialogView.findViewById(R.id.tv_update);
		tv_update.setTypeface(typeface);
		tv_update.setText("OK");
		tv_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
				try {
					if (flag.equals("2")) {
						MySharedPrefs.INSTANCE.putTotalItem("0");
						MySharedPrefs.INSTANCE.clearQuote();
						MyApplication.isFromFinalCheckout=true;
						openOrderHistory();
					}else if(flag.equals("3")){
						AppConstants.strPopupData="";
						HomeScreen.isFromFragment=false;
						viewCart();
					}
				} catch (Exception e){

				}
			}
		});

		alert.show();
	}

}


class Coupon extends AsyncTask<String, String, String>
{
	Context context;
	String strApplyorRemove;
	String strGrandTotal,strShippingCharge;
	//	strCouponCode,strSubTotal,strSubTotalDiscount,strYouSave;
	public Coupon(Context mContext,String strapplyorremove){
		try {
			context = mContext;
			strApplyorRemove = strapplyorremove;
		}catch(Exception e){}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		try {
			((BaseActivity) context).showDialog();
		}catch(Exception e){
			new GrocermaxBaseException("ReviewOrderAndPay", "onPreExecute", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
//		strApplyorRemove = params[1];
		try {
			HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
			params[0] = params[0].replace(" ", "%20");
			HttpGet httpGet = new HttpGet(params[0]);
			httpGet.setHeader("device", context.getResources().getString(R.string.app_device));
			httpGet.setHeader("version", context.getResources().getString(R.string.app_version));
			if(MySharedPrefs.INSTANCE.getSelectedStateId() != null) {
				httpGet.setHeader("storeid", MySharedPrefs.INSTANCE.getSelectedStoreId());
			}
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

			//UtilityMethods.customToast(strResult, context);
            UtilityMethods.customToastLong(strResult,context);
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
                        ((ReviewOrderAndPay)context).setTotolAfterWalletSelectUnSelect(Double.parseDouble(orderReviewBean1.getGrandTotal()));
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
                    ((ReviewOrderAndPay)context).setTotolAfterWalletSelectUnSelect(Double.parseDouble(String.valueOf(totalremove)));
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

		try{
			((BaseActivity) context).dismissDialog();
		}catch(Exception e){}

	}






}









