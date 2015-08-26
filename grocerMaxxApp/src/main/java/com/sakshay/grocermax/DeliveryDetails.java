package com.sakshay.grocermax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.CheckoutAddressBean;
import com.sakshay.grocermax.bean.DateObject;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.UtilityMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Abhishek on 8/22/2015.
 */
public class DeliveryDetails extends BaseActivity implements View.OnClickListener {
    private LayoutInflater inflater = null;
    private Button button_place_order;
    private int requestNewAddress = 111;
    private ArrayList<Address> addressList;
    private CheckoutAddressBean address_obj;
    EasyTracker tracker;

    ArrayList<String> date_list;
    String date;
    String time="";
    private TextView tvSelectedTime;
    private RelativeLayout rlFirstTimeSlot,rlSecondTimeSlot,rlThirdTimeSlot,rlFourthTimeSlot;
    private TextView tvFirstSlotFull,tvSecondSlotFull,tvThirdSlorFull,tvFourthSlotFull;
    private TextView tvFirstTime,tvSecondTime,tvThirdTime,tvFourthTime;

    private ImageView ivLeft,ivRight;
    private TextView tvCurrentDate,tvSelectedDate;
    int selectedDatePosition = 0;
    Button btnCheckoutDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_shipping_3);
        try {
            addActionsInFilter(MyReceiverActions.ADD_ADDRESS);
            addActionsInFilter(MyReceiverActions.ADD_BILL_ADDRESS);

            if (getIntent().getSerializableExtra("addressBean") != null) {
                address_obj = (CheckoutAddressBean) getIntent().getSerializableExtra("addressBean");
                addressList = address_obj.getAddress();
            }
            btnCheckoutDateTime = (Button) findViewById(R.id.btn_checkout_date_time);

//		/*addActionsInFilter(MyReceiverActions.FINAL_CHECKOUT);*/
//			button_place_order = (Button) findViewById(R.id.button_place_order);
//
//			rlShippingAddr = (RelativeLayout) findViewById(R.id.rl_shipping_selected_addr);         //from code
//			rlShippingAddrTop = (RelativeLayout) findViewById(R.id.rl_addr_name_top_shipping);
//			tvShippingHeaderName = (TextView) findViewById(R.id.txt_shipping_header_name);
//			tvShippingAddrName = (TextView) findViewById(R.id.shipping_addr_name);
//			tvShippingAddr1 = (TextView) findViewById(R.id.shipping_addr_1);
//			tvShippingAddr2 = (TextView) findViewById(R.id.shipping_addr_2);
//			tvShippingAddr3 = (TextView) findViewById(R.id.shipping_addr_3);
//			tvShippingAddr4 = (TextView) findViewById(R.id.shipping_addr_4);
//			tvShippingAddr5 = (TextView) findViewById(R.id.shipping_addr_5);
//			tvShippingAddr6 = (TextView) findViewById(R.id.shipping_addr_6);
//			tvHeaderMsg = (TextView) findViewById(R.id.msg);
//
//
//			btnSaveAddress = (Button) findViewById(R.id.add_new_address);
//			btnSaveAddress.setVisibility(View.GONE);
//
//			rlBillingAddr = (RelativeLayout) findViewById(R.id.rl_billing_selected_addr);
//			rlBillingAddrTop = (RelativeLayout) findViewById(R.id.rl_billing_addr_name);
//			tvBillingHeaderName = (TextView) findViewById(R.id.txt_billing_header_name);
//			tvBillingAddrName = (TextView) findViewById(R.id.billing_addr_name);
//			tvBillingAddr1 = (TextView) findViewById(R.id.billing_addr_1);
//			tvBillingAddr2 = (TextView) findViewById(R.id.billing_addr_2);
//			tvBillingAddr3 = (TextView) findViewById(R.id.billing_addr_3);
//			tvBillingAddr4 = (TextView) findViewById(R.id.billing_addr_4);
//			tvBillingAddr5 = (TextView) findViewById(R.id.billing_addr_5);
//			tvBillingAddr6 = (TextView) findViewById(R.id.billing_addr_6);                         //to code
//
//			tvFirstTime = (TextView) findViewById(R.id.first_time);
//			tvSecondTime = (TextView) findViewById(R.id.second_time);
//			tvThirdTime = (TextView) findViewById(R.id.third_time);
//			tvFourthTime = (TextView) findViewById(R.id.fourth_time);

            tvSelectedTime = (TextView) findViewById(R.id.tv_selected_time);

            rlFirstTimeSlot = (RelativeLayout) findViewById(R.id.rl_first_time_Slot);
            rlSecondTimeSlot = (RelativeLayout) findViewById(R.id.rl_second_time_Slot);
            rlThirdTimeSlot = (RelativeLayout) findViewById(R.id.rl_third_time_Slot);
            rlFourthTimeSlot = (RelativeLayout) findViewById(R.id.rl_fourth_time_Slot);

            tvFirstTime = (TextView) findViewById(R.id.tv_time_1st_slot);
            tvSecondTime = (TextView) findViewById(R.id.tv_time_2nd_slot);
            tvThirdTime = (TextView) findViewById(R.id.tv_time_3rd_slot);
            tvFourthTime = (TextView) findViewById(R.id.tv_time_4th_slot);

            tvFirstSlotFull = (TextView) findViewById(R.id.tv_slotfull_1st_slot);
            tvSecondSlotFull = (TextView) findViewById(R.id.tv_slotfull_2nd_slot);
            tvThirdSlorFull = (TextView) findViewById(R.id.tv_slotfull_3rd_slot);
            tvFourthSlotFull = (TextView) findViewById(R.id.tv_slotfull_4th_slot);

            rlFirstTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firstTimeSlot();
                }
            });
            rlSecondTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    secondTimeSlot();
                }
            });
            rlThirdTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thirdTimeSlot();
                }
            });
            rlFourthTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fourthTimeSlot();
                }
            });

            ivLeft = (ImageView) findViewById(R.id.iv_left);
            ivRight = (ImageView) findViewById(R.id.iv_right);
            tvCurrentDate = (TextView) findViewById(R.id.tv_current_date);
            tvSelectedDate = (TextView) findViewById(R.id.tv_selected_date);

            btnCheckoutDateTime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {                                                  //calling for adding new address.
                    // TODO Auto-generated method stub
                    try{
                        if(time.equals(""))
                        {
                            UtilityMethods.customToast(AppConstants.ToastConstant.SELECT_TIME, mContext);
                            return;
                        }
                        if(date.equals(""))
            			{
            				UtilityMethods.customToast("Please select time slot", mContext);
            				return;
            			}
                        Intent intent1 = new Intent(DeliveryDetails.this, ReviewOrderAndPay.class);
                        startActivity(intent1);
                    }catch(Exception e){
                        new GrocermaxBaseException("DeliveryDetails","onCreate",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
                    }
                }
            });

            ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedDatePosition == 0){

                    }else{
                        selectedDatePosition--;
                        tvCurrentDate.setText(date_list.get(selectedDatePosition));
                        tvSelectedDate.setText(date_list.get(selectedDatePosition));
                        date = date_list.get(selectedDatePosition);

                        time = "";
                        tvSelectedTime.setText("");
                        setTimeSlotting(date);

//                        tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                        tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                        tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                        tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//                        tvFirstSlotFull.setText("");
//                        tvSecondSlotFull.setText("");
//                        tvThirdSlorFull.setText("");
//                        tvFourthSlotFull.setText("");
//
//                        tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//                        tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//                        tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//                        tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));
//
//                        rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));

                    }
                }
            });
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedDatePosition == date_list.size() - 1) {
                        //no more date on right side
                    } else {
                        selectedDatePosition++;
                        tvCurrentDate.setText(date_list.get(selectedDatePosition));
                        tvSelectedDate.setText(date_list.get(selectedDatePosition));
                        date = date_list.get(selectedDatePosition);

                        time = "";
                        tvSelectedTime.setText("");
                        setTimeSlotting(date);

//                        tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                        tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                        tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                        tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//                        tvFirstSlotFull.setText("");
//                        tvSecondSlotFull.setText("");
//                        tvThirdSlorFull.setText("");
//                        tvFourthSlotFull.setText("");
//
//                        tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//                        tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//                        tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//                        tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));
//
//                        rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            });



//            date_list = new ArrayList<String>(address_obj.getDate_timeAvailableSlot().keySet());
////            HashMap<String, ArrayList<String>> cehck = address_obj.getDate_timeAvailableSlot();
//            int len = date_list.size();
//            for(int i=0;i<len;i++){
////                ArrayList<String> record = date_list.get(i);
////                record.get(i);
//                date_list.get(i);
//            }

//            if (addressList != null && addressList.size() > 0) {
//                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                date_list = new ArrayList<String>(address_obj.getDate_timeAvailableSlot().keySet());
//                ArrayList<DateObject> datArrayList = new ArrayList<DateObject>();
//                for (int i = 0; i < date_list.size(); i++) {
//                    DateObject dateObject = new DateObject();
//                    try {
//                        dateObject.setDateTime(formatter.parse(date_list.get(i)));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    datArrayList.add(dateObject);
//                }
//                Collections.sort(datArrayList);
//                date_list.clear();
//
//                for (int i = 0; i < datArrayList.size(); i++) {
//                    date_list.add(formatter.format(datArrayList.get(i).getDateTime()));
//                }
//                date = date_list.get(0);
//                setTimeSlotting(date);
//                if(date_list.size() > 0){
//                    tvSelectedDate.setText(date_list.get(0));
//                    tvCurrentDate.setText(date_list.get(0));
//                }
//                ArrayList<String> alTime = address_obj.getDate_timeAvailableSlot().get(date);
//                for(int i=0;i<alTime.size();i++){
//                    String s111 = alTime.get(i);
//                    String s222 = alTime.get(i);
//                    String s333 = alTime.get(i);
//                    String s444 = alTime.get(i);
//                }
//            }


            if (addressList != null && addressList.size() > 0) {
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date_list = new ArrayList<String>(address_obj.getDate_timeSlot().keySet());          //will contain all dates. in format 2015-08-30
                ArrayList<DateObject> datArrayList = new ArrayList<DateObject>();
                for (int i = 0; i < date_list.size(); i++) {
                    DateObject dateObject = new DateObject();
                    try {
                        dateObject.setDateTime(formatter.parse(date_list.get(i)));   //will contain sun aug 30 00:00:00  GMT +15:30 2015
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    datArrayList.add(dateObject);
                }
                Collections.sort(datArrayList);                                //will contain Tue Aug 25 00:00:00 GMT+05:30 2015  in sorting format
                date_list.clear();

                for (int i = 0; i < datArrayList.size(); i++) {
                    date_list.add(formatter.format(datArrayList.get(i).getDateTime()));                //will contain 25-08-2015 in sorted format
                }
                date = date_list.get(0);                                          //will contain first starting date
                setTimeSlotting(date);
                if(date_list.size() > 0){
                    tvSelectedDate.setText(date_list.get(0));
                    tvCurrentDate.setText(date_list.get(0));
                }

            }

            initHeader(findViewById(R.id.app_bar_header), true, "Delivery Details");
            initFooter(findViewById(R.id.footer), 4, 3);
        }catch(Exception e){
            new GrocermaxBaseException("ChooseAddress","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    private void setTimeSlotting(String date)
    {
        try {
            ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
//            ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
            ArrayList<String> alTime = new ArrayList<String>();
            ArrayList<String> alAvailable = new ArrayList<String>();
            for(int i=0;i<alTotal.size()/2;i++){
                alTime.add(i,alTotal.get(i));
            }
            int index=0;
            for(int i=alTotal.size()/2;i<alTotal.size();i++){
                alAvailable.add(index,alTotal.get(i));
                index++;
            }


//            ArrayList<String> alTime1111 = address_obj.getDate_timeAvailableSlot().get(date);
//            if(alTime.size()>0){
//                tvSelectedTime.setText(alTime.get(0));
//                time = alTime.get(0);
//            }

            if (alTime.size() == 4) {
                tvFirstTime.setText(alTime.get(0));
                tvSecondTime.setText(alTime.get(1));
                tvThirdTime.setText(alTime.get(2));
                tvFourthTime.setText(alTime.get(3));

                tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
                tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
                tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
                tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));

                if(alAvailable.get(0).equalsIgnoreCase("0")){
                    tvFirstSlotFull.setText("SLOT FULL");
                    tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    rlFirstTimeSlot.setEnabled(false);
                }else{
                    tvFirstSlotFull.setText("");
                    tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
                    rlFirstTimeSlot.setEnabled(true);
                }

                if(alAvailable.get(1).equalsIgnoreCase("0")){
                    tvSecondSlotFull.setText("SLOT FULL");
                    tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    rlSecondTimeSlot.setEnabled(false);
                }else{
                    tvSecondSlotFull.setText("");
                    tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
                    rlSecondTimeSlot.setEnabled(true);
                }

                if(alAvailable.get(2).equalsIgnoreCase("0")){
                    tvThirdSlorFull.setText("SLOT FULL");
                    tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    rlThirdTimeSlot.setEnabled(false);
                }else{
                    tvThirdSlorFull.setText("");
                    tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
                    rlThirdTimeSlot.setEnabled(true);
                }

                if(alAvailable.get(3).equalsIgnoreCase("0")){
                    tvFourthSlotFull.setText("SLOT FULL");
                    tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    rlFourthTimeSlot.setEnabled(false);
                }else{
                    tvFourthSlotFull.setText("");
                    tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));
                    rlFourthTimeSlot.setEnabled(true);
                }

//                tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//                tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//                tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//                tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));

                rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
            }
//            else if (alTime.size() == 3) {
//                tvFirstTime.setText(alTime.get(0));
//                tvSecondTime.setText(alTime.get(1));
//                tvThirdTime.setText(alTime.get(2));
//                tvFourthTime.setText(alTime.get(3));
//
//                tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                tvFourthTime.setTextColor(getResources().getColor(R.color.pallete_grey));
//
//                tvFirstSlotFull.setText("");
//                tvSecondSlotFull.setText("");
//                tvThirdSlorFull.setText("");
//                tvFourthSlotFull.setText("SLOT FULL");
//
//                tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//                tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//                tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//                tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//                rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlFourthTimeSlot.setEnabled(false);
//
//            } else if (alTime.size() == 2) {
//                tvFirstTime.setText(alTime.get(0));
//                tvSecondTime.setText(alTime.get(1));
//                tvThirdTime.setText(alTime.get(2));
//                tvFourthTime.setText(alTime.get(3));
//
//                tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                tvThirdTime.setTextColor(getResources().getColor(R.color.pallete_grey));
//                tvFourthTime.setTextColor(getResources().getColor(R.color.pallete_grey));
//
//                tvFirstSlotFull.setText("");
//                tvSecondSlotFull.setText("");
//                tvThirdSlorFull.setText("SLOT FULL");
//                tvFourthSlotFull.setText("SLOT FULL");
//
//                tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//                tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//                tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//                tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//                rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlThirdTimeSlot.setEnabled(false);
//                rlFourthTimeSlot.setEnabled(false);
//
//            } else if (alTime.size() == 1) {
//                tvFirstTime.setText(alTime.get(0));
//                tvSecondTime.setText(alTime.get(1));
//                tvThirdTime.setText(alTime.get(2));
//                tvFourthTime.setText(alTime.get(3));
//
//                tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//                tvSecondTime.setTextColor(getResources().getColor(R.color.primaryColor));
//                tvThirdTime.setTextColor(getResources().getColor(R.color.primaryColor));
//                tvFourthTime.setTextColor(getResources().getColor(R.color.primaryColor));
//
//                tvFirstSlotFull.setText("");
//                tvSecondSlotFull.setText("SLOT FULL");
//                tvThirdSlorFull.setText("SLOT FULL");
//                tvFourthSlotFull.setText("SLOT FULL");
//
//                tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//                tvSecondSlotFull.setTextColor(getResources().getColor(R.color.pallete_grey));
//                tvThirdSlorFull.setTextColor(getResources().getColor(R.color.pallete_grey));
//                tvFourthSlotFull.setTextColor(getResources().getColor(R.color.pallete_grey));
//
//                rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//                rlSecondTimeSlot.setEnabled(false);
//                rlThirdTimeSlot.setEnabled(false);
//                rlFourthTimeSlot.setEnabled(false);
//            }
        }catch(Exception e){
            new GrocermaxBaseException("ChooseAddress","setTimeSlotting",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }


//	private void callreviewOrderApi()
//	{
//		try {
//			int shipping_position = spinner_shipping.getSelectedItemPosition();
//			if(shipping_position == 0)
//			{
//				UtilityMethods.customToast(ToastConstant.SELECT_SHIP_ADDR, mContext);
//				return;
//			}
////			if(check_billing.isChecked() && spinner_billing.getSelectedItemPosition() == 0)
//			if(bCheck && spinner_billing.getSelectedItemPosition() == 0)
//			{
//				UtilityMethods.customToast(ToastConstant.SELECT_BILL_ADDR, mContext);
//				return;
//			}
//			if(time.equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SELECT_TIME, mContext);
//				return;
//			}
//			if(shipping_position==1)
//			{
//			if(edt_ship_fname.getText().toString().equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_FNAME, mContext);
//				return;
//			}
//			if(edt_ship_lname.getText().toString().equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_LNAME, mContext);
//				return;
//			}
//			if(edt_ship_number.getText().toString().equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_PHONE, mContext);
//				return;
//			}
//			if(edt_ship_number.getText().toString().length()!=10)
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_PHONE_VALID, mContext);
//				return;
//			}
//			if(edt_ship_address1.getText().toString().equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_ADDR1, mContext);
//				return;
//			}
//			if(edt_ship_city.getText().toString().equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_CITY, mContext);
//				return;
//			}
//			if(edt_ship_state.getText().toString().equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_STATE, mContext);
//				return;
//			}
//			if(edt_ship_country.getText().toString().equals(""))
//			{
//				UtilityMethods.customToast(ToastConstant.SHIP_COUNTRY, mContext);
//				return;
//			}
//			if(edt_ship_pincode.getText().toString().length()!=6)
//			{
//
//				UtilityMethods.customToast(ToastConstant.SHIP_PINCODE, mContext);
//				return;
//			}
//		}
//
//
//
//
//
//			if(bCheck && spinner_billing.getSelectedItemPosition() == 1)
//			{
//				int billing_position = spinner_billing.getSelectedItemPosition();
//				if(billing_position==1)
//				{
//				if(edt_bill_fname.getText().toString().equals(""))
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_FNAME, mContext);
//
//					return;
//				}
//				if(edt_bill_lname.getText().toString().equals(""))
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_LNAME, mContext);
//					return;
//				}
//				if(edt_bill_number.getText().toString().equals(""))
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_PHONE, mContext);
//					return;
//				}
//				if(edt_bill_number.getText().toString().length()!=10)
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_PHONE_VALID, mContext);
//					return;
//				}
//				if(edt_bill_address1.getText().toString().equals(""))
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_ADDR1, mContext);
//					return;
//				}
//				if(edt_bill_city.getText().toString().equals(""))
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_CITY, mContext);
//					return;
//				}
//				if(edt_bill_state.getText().toString().equals(""))
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_STATE, mContext);
//					return;
//				}
//				if(edt_bill_country.getText().toString().equals(""))
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_COUNTRY, mContext);
//					return;
//				}
//				if(edt_bill_pincode.getText().toString().length()!=6)
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_PINCODE, mContext);
//					return;
//				}
//
//
//				if(edt_bill_pincode.getText().toString().length()!=6)
//				{
//					UtilityMethods.customToast(ToastConstant.BILL_PINCODE_VALID, mContext);
//					return;
//				}
//				}
//			}
//			JSONObject shipping_json_obj = new JSONObject();
//			if(shipping_position!=1)
//			{
//			Address ship_add = addressList.get(shipping_position-2);
//
//			shipping_json_obj.put("fname", ship_add.getFirstname());
//			shipping_json_obj.put("lname", ship_add.getLastname());
//			shipping_json_obj.put("city", ship_add.getCity());
//			shipping_json_obj.put("region", ship_add.getState());
//			shipping_json_obj.put("postcode", ship_add.getPostcode());
//			shipping_json_obj.put("country_id", "IN");
//			shipping_json_obj.put("telephone", ship_add.getTelephone());
//			shipping_json_obj.put("addressline1", ship_add.getStreet());
//			shipping_json_obj.put("addressline2","");
//			shipping_json_obj.put("default_billing","0");
//			shipping_json_obj.put("default_shipping","0");
//			}
//			else
//			{
//				shipping_json_obj.put("fname", edt_ship_fname.getText().toString());
//				shipping_json_obj.put("lname", edt_ship_lname.getText().toString());
//				shipping_json_obj.put("city", edt_ship_city.getText().toString());
//				shipping_json_obj.put("region", edt_ship_state.getText().toString());
//				shipping_json_obj.put("postcode", edt_ship_pincode.getText().toString());
//				shipping_json_obj.put("country_id", "IN");
//				shipping_json_obj.put("telephone", edt_ship_number.getText().toString());
//				shipping_json_obj.put("addressline1", edt_ship_address1.getText().toString());
//				shipping_json_obj.put("addressline2",edt_ship_address2.getText().toString());
//				shipping_json_obj.put("default_billing","0");
//				shipping_json_obj.put("default_shipping","0");
//			}
//
//			JSONObject billing_json_obj;
//
//			if(bCheck)
//			{
//				if(spinner_billing.getSelectedItemPosition() == spinner_shipping.getSelectedItemPosition())
//				{
//					billing_json_obj = shipping_json_obj;
//				}
//				else
//				{
//					int billing_position = spinner_billing.getSelectedItemPosition();
//					billing_json_obj = new JSONObject();
//					if(billing_position!=1)
//					{
//					Address billing_add = addressList.get(spinner_billing.getSelectedItemPosition()-2);
//					billing_json_obj.put("fname", billing_add.getFirstname());
//					billing_json_obj.put("lname", billing_add.getLastname());
//					billing_json_obj.put("city", billing_add.getCity());
//					billing_json_obj.put("region", billing_add.getState());
//					billing_json_obj.put("postcode", billing_add.getPostcode());
//					billing_json_obj.put("country_id", "IN");
//					billing_json_obj.put("telephone", billing_add.getTelephone());
//					billing_json_obj.put("addressline1", billing_add.getStreet());
//					billing_json_obj.put("addressline2","");
//					billing_json_obj.put("default_billing","0");
//					billing_json_obj.put("default_shipping","0");
//					}
//					else
//					{
//						billing_json_obj.put("fname", edt_bill_fname.getText().toString());
//						billing_json_obj.put("lname", edt_bill_lname.getText().toString());
//						billing_json_obj.put("city", edt_bill_city.getText().toString());
//						billing_json_obj.put("region", edt_bill_state.getText().toString());
//						billing_json_obj.put("postcode", edt_bill_pincode.getText().toString());
//						billing_json_obj.put("country_id", "IN");
//						billing_json_obj.put("telephone", edt_bill_number.getText().toString());
//						billing_json_obj.put("addressline1", edt_bill_address1.getText().toString());
//						billing_json_obj.put("addressline2",edt_bill_address2.getText().toString());
//						billing_json_obj.put("default_billing","0");
//						billing_json_obj.put("default_shipping","0");
//					}
//				}
//			}
//			else{
//				billing_json_obj = shipping_json_obj;
//			}
//
//			String params = shipping_json_obj.toString() + "&billing=" + billing_json_obj.toString();
//
//			OrderReviewBean orderReviewBean=MySharedPrefs.INSTANCE.getOrderReviewBean();
//			orderReviewBean.setBilling(billing_json_obj);
//			orderReviewBean.setShipping(shipping_json_obj);
//			orderReviewBean.setDate(date);
//			orderReviewBean.setTimeSlot(time);
//			MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);
//
//			if(shipping_position==1)
//			{
//				String fname=edt_ship_fname.getText().toString();
//				String lname=edt_ship_lname.getText().toString();
//				String addressline1=edt_ship_address1.getText().toString();
//				String addressline2=edt_ship_address2.getText().toString();
//				String city=edt_ship_city.getText().toString();
//				String state=edt_ship_state.getText().toString();
//				String pin=edt_ship_pincode.getText().toString();
//				String countrycode="IN";
//				String phone=edt_ship_number.getText().toString();
//				String url_param="fname="+fname+"&lname="+lname+"&addressline1="+addressline1+"&addressline2="+addressline2+"&city="+city+"&state="+state+"&pin="+pin+"&countrycode="+countrycode+"&phone="+phone;
//				url_param=url_param.replaceAll(" ","%20");
//				showDialog();
//				String url = UrlsConstants.ADD_ADDRESS + url_param + "&userid=" + MySharedPrefs.INSTANCE.getUserId() + "&default_billing=0&default_shipping=1";
//				myApi.reqAddAddress(url,MyReceiverActions.ADD_ADDRESS);
//
//			}else{
//
////				Intent intent = new Intent(this,OffersPromoCode.class);
////				startActivity(intent);
//				Intent call = new Intent(mContext, ReviewOrderAndPay.class);
//				startActivity(call);
//			}
//
//			/*Intent call = new Intent(mContext, ReviewOrderAndPay.class);
//			startActivity(call);*/
//		}catch(NullPointerException e){
//			new GrocermaxBaseException("ChooseAddress","callreviewOrderApi",e.getMessage(),GrocermaxBaseException.NULL_POINTER,"nodetail");
//		}catch (Exception e) {
//			new GrocermaxBaseException("ChooseAddress","callreviewOrderApi",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
//	}

//	public void addTextView()
//	{
//		try {
//			ed = new TextView[date_list.size()];
//			for (int i = 0; i < date_list.size(); i++) {
//
//				ed[i] = new TextView(ChooseAddress.this);
//				if (i == 0)
//					ed[i].setBackgroundColor(getResources().getColor(R.color.red));
//				else
//					ed[i].setBackgroundColor(getResources().getColor(R.color.orange_text));
//				ed[i].setId(i);
//				ed[i].setTextSize(20f);
//				ed[i].setText(date_list.get(i));
//				ed[i].setTextColor(Color.WHITE);
//				ed[i].setGravity(Gravity.CENTER);
//				LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
//				param.setMargins(5, 0, 5, 0);
//				ed[i].setPadding(10, 0, 10, 0);
//				ed[i].setLayoutParams(param);
//				ed[i].setOnClickListener(ChooseAddress.this);
//				ll_date.addView(ed[i]);
//			}
//		}catch(Exception e){
//			new GrocermaxBaseException("ChooseAddress","addTextView",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
//	}

//	private void showBillingAddress() {
//		text_billing.setVisibility(View.VISIBLE);
//		spinner_billing.setVisibility(View.VISIBLE);
//		iv_left_bottom_spinner.setVisibility(View.VISIBLE);
//		billingDropDown.setVisibility(View.VISIBLE);
//		layout_billing.setVisibility(View.VISIBLE);
//
//		if(spinner_billing.getSelectedItemPosition() == 0 || spinner_billing.getSelectedItemPosition() == 1){
//			rlBillingAddr.setVisibility(View.GONE);                            //latest
//			rlBillingAddrTop.setVisibility(View.GONE);
//		}else{
//			rlBillingAddr.setVisibility(View.VISIBLE);                                  //latest
//			rlBillingAddrTop.setVisibility(View.VISIBLE);
//		}
//	}

//	private void hideBillingAddress() {
//		text_billing.setVisibility(View.GONE);
//		spinner_billing.setVisibility(View.GONE);
//		iv_left_bottom_spinner.setVisibility(View.GONE);
//		billingDropDown.setVisibility(View.GONE);
//		layout_billing.setVisibility(View.GONE);
//
//		rlBillingAddr.setVisibility(View.GONE);                                //lastest
//		rlBillingAddrTop.setVisibility(View.GONE);
//	}

//	private void shippingAddress(Address obj){
//		rlShippingAddr.setVisibility(View.VISIBLE);
//		rlShippingAddrTop.setVisibility(View.VISIBLE);
//
//		tvShippingHeaderName.setText(obj.getFirstname() + " " + obj.getLastname());
//		tvShippingAddrName.setText(obj.getFirstname() + " " + obj.getLastname());
//		tvShippingAddr1.setText(obj.getRegion());         //street
//		tvShippingAddr2.setText(obj.getRegion());         //state
//		tvShippingAddr3.setText(obj.getTelephone());      //phone
//		tvShippingAddr4.setText(obj.getCity());           //city
//		tvShippingAddr5.setText(obj.getPostcode());       //post code
//		tvShippingAddr6.setText("India");                 //country
//	}

//	private void billingAddress(Address obj){
//		rlBillingAddr.setVisibility(View.VISIBLE);
//		rlBillingAddrTop.setVisibility(View.VISIBLE);
//
//		tvBillingHeaderName.setText(obj.getFirstname() + " " + obj.getLastname());
//		tvBillingAddrName.setText(obj.getFirstname() + " " + obj.getLastname());
//		tvBillingAddr1.setText(obj.getRegion());         //street
//		tvBillingAddr2.setText(obj.getRegion());         //state
//		tvBillingAddr3.setText(obj.getTelephone());      //phone
//		tvBillingAddr4.setText(obj.getCity());           //city
//		tvBillingAddr5.setText(obj.getPostcode());       //post code
//		tvBillingAddr6.setText("India");                 //country
//
//
//	}


//	private void showShipNewAddress(LinearLayout linearLayout){
//		linearLayout.removeAllViews();
//		linearLayout.setVisibility(View.VISIBLE);
//		View newaddressView = inflater.inflate(R.layout.new_address_row, null, false);
//		newaddressView.findViewById(R.id.layout_address_info).setBackgroundColor(mContext.getResources().getColor(R.color.grey_bg));
//
//		edt_ship_fname = (EditText) newaddressView.findViewById(R.id.fname);
//		edt_ship_lname = (EditText) newaddressView.findViewById(R.id.lname);
//		edt_ship_number = (EditText) newaddressView.findViewById(R.id.phone);
//		edt_ship_address1=(EditText)newaddressView.findViewById(R.id.address1);
//		edt_ship_address2=(EditText)newaddressView.findViewById(R.id.address2);
//		edt_ship_city=(EditText)newaddressView.findViewById(R.id.city);
//		edt_ship_state=(EditText)newaddressView.findViewById(R.id.state);
//		edt_ship_country=(EditText)newaddressView.findViewById(R.id.country);
//		edt_ship_pincode=(EditText)newaddressView.findViewById(R.id.pincode);
//		linearLayout.addView(newaddressView);
//	}
//	private void showBillNewAddress(LinearLayout linearLayout){
//		linearLayout.removeAllViews();
//		linearLayout.setVisibility(View.VISIBLE);
//		View newaddressView = inflater.inflate(R.layout.new_address_row, null, false);
//		newaddressView.findViewById(R.id.layout_address_info).setBackgroundColor(mContext.getResources().getColor(R.color.grey_bg));
//
//		edt_bill_fname = (EditText) newaddressView.findViewById(R.id.fname);
//		edt_bill_lname = (EditText) newaddressView.findViewById(R.id.lname);
//		edt_bill_number = (EditText) newaddressView.findViewById(R.id.phone);
//		edt_bill_address1=(EditText)newaddressView.findViewById(R.id.address1);
//		edt_bill_address2=(EditText)newaddressView.findViewById(R.id.address2);
//		edt_bill_city=(EditText)newaddressView.findViewById(R.id.city);
//		edt_bill_state=(EditText)newaddressView.findViewById(R.id.state);
//		edt_bill_country=(EditText)newaddressView.findViewById(R.id.country);
//		edt_bill_pincode=(EditText)newaddressView.findViewById(R.id.pincode);
//		linearLayout.addView(newaddressView);
//	}

//	private void hideAddress(LinearLayout linearLayout) {
//		linearLayout.removeAllViews();
//		linearLayout.setVisibility(View.GONE);
//	}

    @Override
    void OnResponse(Bundle bundle) {
//		try {
//			String action = bundle.getString("ACTION");
//			if (action.equals(MyReceiverActions.ADD_ADDRESS)) {
//				BaseResponseBean responseBean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
//				if (responseBean.getFlag().equalsIgnoreCase("1")) {
////				if(check_billing.isChecked()&&spinner_billing.getSelectedItemPosition()==1)
//					if (bCheck && spinner_billing.getSelectedItemPosition() == 1) {
//						String fname = edt_bill_fname.getText().toString();
//						String lname = edt_bill_lname.getText().toString();
//						String addressline1 = edt_bill_address1.getText().toString();
//						String addressline2 = edt_bill_address2.getText().toString();
//						String city = edt_bill_city.getText().toString();
//						String state = edt_bill_state.getText().toString();
//						String pin = edt_bill_pincode.getText().toString();
//						String countrycode = "IN";
//						String phone = edt_bill_number.getText().toString();
//						String url_param = "fname=" + fname + "&lname=" + lname + "&addressline1=" + addressline1 + "&addressline2=" + addressline2 + "&city=" + city + "&state=" + state + "&pin=" + pin + "&countrycode=" + countrycode + "&phone=" + phone;
//						url_param = url_param.replaceAll(" ", "%20");
//						showDialog();
//						String url = UrlsConstants.ADD_ADDRESS + url_param + "&userid=" + MySharedPrefs.INSTANCE.getUserId() + "&default_billing=1&default_shipping=0";
//						myApi.reqAddAddress(url, MyReceiverActions.ADD_BILL_ADDRESS);
//					} else {
//						Intent call = new Intent(mContext, ReviewOrderAndPay.class);
//						startActivity(call);
//					}
//				} else {
//					Toast.makeText(mContext, responseBean.getResult(), Toast.LENGTH_LONG).show();
//				}
//			} else if (action.equals(MyReceiverActions.ADD_BILL_ADDRESS)) {
//				BaseResponseBean responseBean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
//				if (responseBean.getFlag().equalsIgnoreCase("1")) {
//					Intent call = new Intent(mContext, ReviewOrderAndPay.class);
//					startActivity(call);
//				}
//			}
//		}catch (Exception e){
//			new GrocermaxBaseException("ChooseAddress","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		try{
		if (requestCode!=requestNewAddress && resultCode==RESULT_OK) {
			//New address added refresh list
//			showDialog();
//			String url = UrlsConstants.CHECKOUT_ADDRESS_BOOK+MySharedPrefs.INSTANCE.getUserId();
//			myApi.reqCheckOutAddress(url);
//			finish();
		}else{
			UtilityMethods.customToast(Constants.ToastConstant.ERROR_MSG, mContext);
		}
		}catch (Exception e){
			new GrocermaxBaseException("ChooseAddress","onActivityResult",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initHeader(findViewById(R.id.app_bar_header), true, "Delivery Details");
    }


    @Override
    public void onClick(View v) {
//		try {
//			ArrayAdapter<String> slotsAdapter = new ArrayAdapter<String>(ChooseAddress.this, R.layout.spinner_textview, address_obj.getDate_timeSlot().get(date_list.get(((TextView) v).getId())));
//			date = ((TextView) v).getText().toString();
//
//			time = "";
//			for (int i = 0; i < date_list.size(); i++) {
//				ed[i].setBackgroundColor(getResources().getColor(R.color.orange_text));
//			}
//			((TextView) v).setBackgroundColor(getResources().getColor(R.color.red));
//		}catch (Exception e){
//			new GrocermaxBaseException("ChooseAddress","onClick",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
    }

//	public static void setListViewHeightBasedOnChildren(ListView listView) {
//		try{
//		ListAdapter listAdapter = listView.getAdapter();
//		if (listAdapter == null)
//			return;
//
//		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
//		int totalHeight = 0;
//		View view = null;
//		for (int i = 0; i < listAdapter.getCount(); i++) {
//			view = listAdapter.getView(i, view, listView);
//			if (i == 0)
//				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
//
//			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//			totalHeight += view.getMeasuredHeight();
//		}
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//		listView.setLayoutParams(params);
//		listView.requestLayout();
//		}catch (Exception e){
//			new GrocermaxBaseException("ChooseAddress","setListViewHeightBasedOnChildren",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
//	}

//	public int getdp(int a){
//		int paddingPixel = a;
//		float density = this.getResources().getDisplayMetrics().density;
//		int paddingDp = (int)(paddingPixel * density);
//		return paddingDp;
//	}

    private void firstTimeSlot(){
//        ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//        time = alTime.get(0);
//        tvSelectedTime.setText(time);

        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
//            ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
        ArrayList<String> alTime = new ArrayList<String>();

        for(int i=0;i<alTotal.size()/2;i++) {
            alTime.add(i, alTotal.get(i));
        }
        time = alTime.get(0);
        tvSelectedTime.setText(time);

//        tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));

//        tvFirstSlotFull.setText("");
//        tvSecondSlotFull.setText("");
//        tvThirdSlorFull.setText("");
//        tvFourthSlotFull.setText("");

//        tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));

        rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void secondTimeSlot(){
//        ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//        time = alTime.get(1);
//        tvSelectedTime.setText(time);

        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
//            ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
        ArrayList<String> alTime = new ArrayList<String>();

        for(int i=0;i<alTotal.size()/2;i++) {
            alTime.add(i, alTotal.get(i));
        }
        time = alTime.get(1);
        tvSelectedTime.setText(time);


//        tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//        tvFirstSlotFull.setText("");
//        tvSecondSlotFull.setText("");
//        tvThirdSlorFull.setText("");
//        tvFourthSlotFull.setText("");
//
//        tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));

        rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void thirdTimeSlot(){
//        ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//        time = alTime.get(2);
//        tvSelectedTime.setText(time);

        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
//            ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
        ArrayList<String> alTime = new ArrayList<String>();

        for(int i=0;i<alTotal.size()/2;i++) {
            alTime.add(i, alTotal.get(i));
        }
        time = alTime.get(2);
        tvSelectedTime.setText(time);


//        tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//        tvFirstSlotFull.setText("");
//        tvSecondSlotFull.setText("");
//        tvThirdSlorFull.setText("");
//        tvFourthSlotFull.setText("");
//
//        tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));

        rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void fourthTimeSlot(){
//        ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//        time = alTime.get(3);
//        tvSelectedTime.setText(time);

        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
//            ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
        ArrayList<String> alTime = new ArrayList<String>();

        for(int i=0;i<alTotal.size()/2;i++) {
            alTime.add(i, alTotal.get(i));
        }
        time = alTime.get(3);
        tvSelectedTime.setText(time);


//        tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//        tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//        tvFirstSlotFull.setText("");
//        tvSecondSlotFull.setText("");
//        tvThirdSlorFull.setText("");
//        tvFourthSlotFull.setText("");
//
//        tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//        tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));

        rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            tracker.activityStart(this);
            FlurryAgent.onStartSession(this, getResources().getString(R.string.flurry_api_key));
            FlurryAgent.onPageView();         //Use onPageView to report page view count.
        }catch(Exception e){
            new GrocermaxBaseException("ChooseAddress","onStart",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try{
            tracker.activityStop(this);
            FlurryAgent.onEndSession(this);
        }catch(Exception e){
            new GrocermaxBaseException("ChooseAddress","onStop",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

}


