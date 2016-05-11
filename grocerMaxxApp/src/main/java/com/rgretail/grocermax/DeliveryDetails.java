package com.rgretail.grocermax;

import android.content.Context;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.bean.CheckoutAddressBean;
import com.rgretail.grocermax.bean.DateObject;
import com.rgretail.grocermax.bean.OrderReviewBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Abhishek on 8/22/2015.
 */
public class DeliveryDetails extends BaseActivity implements View.OnClickListener {
    private LayoutInflater inflater = null;
    private Button button_place_order;
    private int requestNewAddress = 111;
    private ArrayList<Address> addressList;
    private CheckoutAddressBean address_obj;
//    EasyTracker tracker;

    ArrayList<String> date_list;
    String date;
    String time="";
    private TextView tvSelectedTime;
   // private RelativeLayout rlFirstTimeSlot,rlSecondTimeSlot,rlThirdTimeSlot,rlFourthTimeSlot;
    //private TextView tvFirstSlotFull,tvSecondSlotFull,tvThirdSlorFull,tvFourthSlotFull;
    //private TextView tvFirstTime,tvSecondTime,tvThirdTime,tvFourthTime;

    private ImageView ivLeft,ivRight;
    private TextView tvCurrentDate,tvSelectedDate;
    GridView grid_time_slot;
    int selectedDatePosition = 0;
    Button btnCheckoutDateTime;
    private String SCREENNAME = "DeliveryDetails-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_shipping_3);
        try{
            AppsFlyerLib.setCurrencyCode("INR");
            AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
            AppsFlyerLib.sendTracking(getApplicationContext());
        }catch(Exception e){}
        try {
            addActionsInFilter(MyReceiverActions.ADD_ADDRESS);
            addActionsInFilter(MyReceiverActions.ADD_BILL_ADDRESS);

            if (getIntent().getSerializableExtra("addressBean") != null) {
                address_obj = (CheckoutAddressBean) getIntent().getSerializableExtra("addressBean");
                addressList = address_obj.getAddress();
            }
            btnCheckoutDateTime = (Button) findViewById(R.id.btn_checkout_date_time);

            tvSelectedTime = (TextView) findViewById(R.id.tv_selected_time);
            grid_time_slot=(GridView)findViewById(R.id.grid_time_slot);

            /*Commented By Ishan*/
           /* rlFirstTimeSlot = (RelativeLayout) findViewById(R.id.rl_first_time_Slot);
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
            });*/

            ivLeft = (ImageView) findViewById(R.id.iv_left);
            ivRight = (ImageView) findViewById(R.id.iv_right);
            tvCurrentDate = (TextView) findViewById(R.id.tv_current_date);
            tvSelectedDate = (TextView) findViewById(R.id.tv_selected_date);

            btnCheckoutDateTime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {                                                  //calling for adding new address.
                    // TODO Auto-generated method stub
                    try{

                        try{
                            UtilityMethods.clickCapture(mContext,"Delivery details","","","",MySharedPrefs.INSTANCE.getSelectedCity());
                            RocqAnalytics.trackEvent("Delivery details", new ActionProperties("Category", "Delivery details", "Action", MySharedPrefs.INSTANCE.getSelectedCity()));
                        }catch(Exception e){}
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
                        OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
                        orderReviewBean.setDate(date);
                        orderReviewBean.setTimeSlot(time);
                        MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);
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
                        try {
                            selectedDatePosition--;
                            date = date_list.get(selectedDatePosition);

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat formatter3 = null;
                            Date convertedDate = null;

                            String yyyyMMdd = date;
                            formatter3 = new SimpleDateFormat("yyyy-MM-dd");
                            convertedDate = (Date) formatter.parse(yyyyMMdd);
                            System.out.println("Date from yyyyMMdd String in Java : " + convertedDate);    //Wed Sep 14 00:00:00 GMT+05:45 2011

                            String changedate = String.valueOf(convertedDate);
                            DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                            Date date1 = (Date) formatter1.parse(changedate);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date1);
                            String strDay="";
                            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                            if(dayOfWeek == 1){
                                strDay = "Sunday";
                            }else if(dayOfWeek == 2){
                                strDay = "Monday";
                            }else if(dayOfWeek == 3){
                                strDay = "Tuesday";
                            }else if(dayOfWeek == 4){
                                strDay = "Wednesday";
                            }else if(dayOfWeek == 5){
                                strDay = "Thursday";
                            }else if(dayOfWeek == 6){
                                strDay = "Friday";
                            }else if(dayOfWeek == 7){
                                strDay = "Saturday";
                            }


                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                            Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)),cal.get(Calendar.DATE));
                            String strFinalDate = strDay+", "+sdf.format(calendar.getTime());
                            tvCurrentDate.setText(strFinalDate);
                            tvSelectedDate.setText(strFinalDate);
                            time = "";
                            tvSelectedTime.setText("");
                            setTimeSlotting(date);
                        }catch(Exception e){

                        }
                    }
                }
            });
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedDatePosition == date_list.size() - 1) {
                        //no more date on right side
                    } else {
                        try{
                            selectedDatePosition++;
                            date = date_list.get(selectedDatePosition);


                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat formatter3 = null;
                            Date convertedDate = null;

                            String yyyyMMdd = date;
                            formatter3 = new SimpleDateFormat("yyyy-MM-dd");
                            convertedDate = (Date) formatter.parse(yyyyMMdd);
                            System.out.println("Date from yyyyMMdd String in Java : " + convertedDate);    //Wed Sep 14 00:00:00 GMT+05:45 2011

                            String changedate = String.valueOf(convertedDate);
                            DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                            Date date1 = (Date) formatter1.parse(changedate);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date1);
                            String strDay="";
                            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                            if(dayOfWeek == 1){
                                strDay = "Sunday";
                            }else if(dayOfWeek == 2){
                                strDay = "Monday";
                            }else if(dayOfWeek == 3){
                                strDay = "Tuesday";
                            }else if(dayOfWeek == 4){
                                strDay = "Wednesday";
                            }else if(dayOfWeek == 5){
                                strDay = "Thursday";
                            }else if(dayOfWeek == 6){
                                strDay = "Friday";
                            }else if(dayOfWeek == 7){
                                strDay = "Saturday";
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                            Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)),cal.get(Calendar.DATE));
                            String strFinalDate = strDay+", "+sdf.format(calendar.getTime());
                            tvCurrentDate.setText(strFinalDate);
                            tvSelectedDate.setText(strFinalDate);
                            time = "";
                            tvSelectedTime.setText("");
                            setTimeSlotting(date);
                        }catch(Exception e){

                        }

                    }
                }
            });

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


                //////////////// used for both cases 1)when any time slots available for starting date 2)when no time slots available for starting date /////////////
                ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
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
                if(alAvailable.get(0).equalsIgnoreCase("0") && alAvailable.get(0).equalsIgnoreCase("0") && alAvailable.get(0).equalsIgnoreCase("0") && alAvailable.get(0).equalsIgnoreCase("0")){
                    date = date_list.get(1);
                    setTimeSlotting(date);
                    if(date_list.size() > 1){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat formatter3 = null;
                        Date convertedDate = null;

                        // Creating SimpleDateFormat with yyyyMMdd format e.g."20110914"
//    String yyyyMMdd = "2015-08-30";
                        String yyyyMMdd = date;
                        formatter3 = new SimpleDateFormat("yyyy-MM-dd");
                        convertedDate = (Date) format.parse(yyyyMMdd);
                        System.out.println("Date from yyyyMMdd String in Java : " + convertedDate);    //Wed Sep 14 00:00:00 GMT+05:45 2011

                        String changedate = String.valueOf(convertedDate);
                        DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                        Date date1 = (Date) formatter1.parse(changedate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date1);
                        String strDay="";
                        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                        if(dayOfWeek == 1){
                            strDay = "Sunday";
                        }else if(dayOfWeek == 2){
                            strDay = "Monday";
                        }else if(dayOfWeek == 3){
                            strDay = "Tuesday";
                        }else if(dayOfWeek == 4){
                            strDay = "Wednesday";
                        }else if(dayOfWeek == 5){
                            strDay = "Thursday";
                        }else if(dayOfWeek == 6){
                            strDay = "Friday";
                        }else  if(dayOfWeek == 7){
                            strDay = "Saturday";
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                        Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)),cal.get(Calendar.DATE));
                        String strFinalDate = strDay+", "+sdf.format(calendar.getTime());
                        tvCurrentDate.setText(strFinalDate);
                        tvSelectedDate.setText(strFinalDate);


                        selectedDatePosition = 1;
                    }
                }else{
                    setTimeSlotting(date);
                    if(date_list.size() > 0){
                        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat formatter3 = null;
                        Date convertedDate = null;

                        // Creating SimpleDateFormat with yyyyMMdd format e.g."20110914"
//    String yyyyMMdd = "2015-08-30";
                        String yyyyMMdd = date;
                        formatter3 = new SimpleDateFormat("yyyy-MM-dd");
                        convertedDate = (Date) formatt.parse(yyyyMMdd);
                        System.out.println("Date from yyyyMMdd String in Java : " + convertedDate);    //Wed Sep 14 00:00:00 GMT+05:45 2011

                        String changedate = String.valueOf(convertedDate);
//                        String dateStr = "Mon Jun 18 00:00:00 IST 2012";
                        DateFormat formatter1 = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                        Date date1 = (Date) formatter1.parse(changedate);
//                        Date date1 = (Date)formatter1.parse(date_list.get(i));
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date1);
//    String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR)
//            + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) +
//            "/" + cal.get(Calendar.DAY_OF_WEEK);       //0-sunday ,1-monday,2-tuesday,3-wednesday,4-thursday,5-friday,6-saturday
//    System.out.println("formatedDate : " + formatedDate);
                        String strDay="";
                        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                        if(dayOfWeek == 1){
                            strDay = "Sunday";
                        }else if(dayOfWeek == 2){
                            strDay = "Monday";
                        }else if(dayOfWeek == 3){
                            strDay = "Tuesday";
                        }else if(dayOfWeek == 4){
                            strDay = "Wednesday";
                        }else if(dayOfWeek == 5){
                            strDay = "Thursday";
                        }else if(dayOfWeek == 6){
                            strDay = "Friday";
                        }else if(dayOfWeek == 7){
                            strDay = "Saturday";
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                        Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH)),cal.get(Calendar.DATE));
//    System.out.println("Date : " + sdf.format(calendar.getTime()));
                        String strFinalDate = strDay+", "+sdf.format(calendar.getTime());
//     cal.get(Calendar.DAY_OF_WEEK),     sunday
//cal.get(Calendar.DATE)             30
                        //cal.get(Calendar.YEAR)             2015
                        tvCurrentDate.setText(strFinalDate);
                        tvSelectedDate.setText(strFinalDate);

//                        tvSelectedDate.setText(date_list.get(0));
//                        tvCurrentDate.setText(date_list.get(0));
                        selectedDatePosition = 0;
                    }
                }
                //////////////// used for both cases 1)when any time slots available for starting date 2)when no time slots available for starting date /////////////

            }

            initHeader(findViewById(R.id.app_bar_header), true, "Delivery Details");
            initFooter(findViewById(R.id.footer), 4, 3);
            icon_header_search.setVisibility(View.GONE);
            icon_header_cart.setVisibility(View.GONE);
            cart_count_txt.setVisibility(View.GONE);
        }catch(Exception e){
            new GrocermaxBaseException("DeliveryDetails","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    private void setTimeSlotting(final String date)
    {
        try {
            ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
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

            grid_time_slot.setAdapter(new TimeSlotAdapter(alTime,alAvailable));
            grid_time_slot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
                        ArrayList<String> alTime = new ArrayList<String>();

                        for(int i=0;i<alTotal.size()/2;i++) {
                            alTime.add(i, alTotal.get(i));
                        }
                        time = alTime.get(position);
                        tvSelectedTime.setText(time);

                        for(int i=0;i<grid_time_slot.getAdapter().getCount();i++){
                            if(i==position){
                                ((RelativeLayout)parent.getChildAt(i).findViewById(R.id.rl_time_Slot)).setBackgroundColor(getResources().getColor(R.color.gray_1));
                            }else{
                                ((RelativeLayout)parent.getChildAt(i).findViewById(R.id.rl_time_Slot)).setBackgroundColor(getResources().getColor(R.color.white));
                            }
                        }

                    }catch(Exception e){
                        new GrocermaxBaseException("DeliveryDetails","Slot Select",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
                    }
                }
            });

              /*Commented By Ishan*/
            /*if (alTime.size() == 4) {
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
                    tvFirstSlotFull.setVisibility(View.VISIBLE);
                    tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    BlurMaskFilter.Blur style = BlurMaskFilter.Blur.NORMAL;
                    applyFilter(tvFirstTime, style);
                    rlFirstTimeSlot.setEnabled(false);
                }else{
                    tvFirstSlotFull.setText("");
                    tvFirstTime.setVisibility(View.VISIBLE);
                    tvFirstSlotFull.setVisibility(View.INVISIBLE);
                    tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
                    applyFilterVisible(tvFirstTime, new float[]{0f, -1f, 0.5f}, 0.8f, 15f, 1f);
                    rlFirstTimeSlot.setEnabled(true);
                }

                if(alAvailable.get(1).equalsIgnoreCase("0")){
                    tvSecondSlotFull.setText("SLOT FULL");
                    tvSecondSlotFull.setVisibility(View.VISIBLE);
                    tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    BlurMaskFilter.Blur style = BlurMaskFilter.Blur.NORMAL;
                    applyFilter(tvSecondTime, style);
                    rlSecondTimeSlot.setEnabled(false);
                }else{
                    tvSecondSlotFull.setText("");
                    tvSecondSlotFull.setVisibility(View.INVISIBLE);
                    tvSecondTime.setVisibility(View.VISIBLE);
                    tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
                    applyFilterVisible(tvSecondTime, new float[] { 0f, -1f, 0.5f }, 0.8f, 15f, 1f);
                    rlSecondTimeSlot.setEnabled(true);
                }

                if(alAvailable.get(2).equalsIgnoreCase("0")){
                    tvThirdSlorFull.setText("SLOT FULL");
                    tvThirdSlorFull.setVisibility(View.VISIBLE);
                    tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    BlurMaskFilter.Blur style = BlurMaskFilter.Blur.NORMAL;
                    applyFilter(tvThirdTime, style);
                    rlThirdTimeSlot.setEnabled(false);
                }else{
                    tvThirdSlorFull.setText("");
                    tvThirdSlorFull.setVisibility(View.INVISIBLE);
                    tvThirdTime.setVisibility(View.VISIBLE);
                    tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
                    applyFilterVisible(tvThirdTime, new float[] { 0f, -1f, 0.5f }, 0.8f, 15f, 1f);
                    rlThirdTimeSlot.setEnabled(true);
                }

                if(alAvailable.get(3).equalsIgnoreCase("0")){
                    tvFourthSlotFull.setText("SLOT FULL");
                    tvFourthSlotFull.setVisibility(View.VISIBLE);
                    tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                    BlurMaskFilter.Blur style = BlurMaskFilter.Blur.NORMAL;
                    applyFilter(tvFourthTime, style);
                    rlFourthTimeSlot.setEnabled(false);
                }else{
                    tvFourthSlotFull.setText("");
                    tvFourthSlotFull.setVisibility(View.INVISIBLE);
                    tvFourthTime.setVisibility(View.VISIBLE);
                    tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));
                    applyFilterVisible(tvFourthTime, new float[] { 0f, -1f, 0.5f }, 0.8f, 15f, 1f);
                    rlFourthTimeSlot.setEnabled(true);
                }
                rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                 }*/


        }catch(Exception e){
            new GrocermaxBaseException("DeliveryDetails","setTimeSlotting",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    private void applyFilter(TextView textView, BlurMaskFilter.Blur style) {
        if (Build.VERSION.SDK_INT >= 11) {
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
//        textView.setText(style.name());
//        textView.setText("SLOT FULL");
        float radius = textView.getTextSize() / 10;
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);
        textView.getPaint().setMaskFilter(filter);
    }

    private void applyFilterVisible(
            TextView textView, float[] direction, float ambient,
            float specular, float blurRadius) {
        if (Build.VERSION.SDK_INT >= 11) {
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        EmbossMaskFilter filter = new EmbossMaskFilter(
                direction, ambient, specular, blurRadius);
        textView.getPaint().setMaskFilter(filter);
    }



    @Override
    public void OnResponse(Bundle bundle) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode!=requestNewAddress && resultCode==RESULT_OK) {
            }else{
                UtilityMethods.customToast(Constants.ToastConstant.ERROR_MSG, mContext);
            }
        }catch (Exception e){
            new GrocermaxBaseException("DeliveryDetails","onActivityResult",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try{
            initHeader(findViewById(R.id.app_bar_header), true, "Delivery Details");
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
    }


    @Override
    public void onClick(View v) {

    }

    private void firstTimeSlot(){
        try{
        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
        ArrayList<String> alTime = new ArrayList<String>();

        for(int i=0;i<alTotal.size()/2;i++) {
            alTime.add(i, alTotal.get(i));
        }
        time = alTime.get(0);
        tvSelectedTime.setText(time);
/*Commented By Ishan*/
        /*rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));*/
        }catch(Exception e){
            new GrocermaxBaseException("DeliveryDetails","firstTimeSlot",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    private void secondTimeSlot(){
        try{
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
/*Commented By Ishan*/
       /* rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));*/
        }catch(Exception e){
            new GrocermaxBaseException("DeliveryDetails","secondTimeSlot",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    private void thirdTimeSlot()
    {
        try{

            ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
            ArrayList<String> alTime = new ArrayList<String>();

            for(int i=0;i<alTotal.size()/2;i++) {
                alTime.add(i, alTotal.get(i));
            }
            time = alTime.get(2);
            tvSelectedTime.setText(time);
/*Commented By Ishan*/
           /* rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
            rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
            rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
            rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));*/
        }catch(Exception e){
            new GrocermaxBaseException("DeliveryDetails","thirdTimeSlot",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }
    private void fourthTimeSlot(){
        try{

        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
//            ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
        ArrayList<String> alTime = new ArrayList<String>();

        for(int i=0;i<alTotal.size()/2;i++) {
            alTime.add(i, alTotal.get(i));
        }
        time = alTime.get(3);
        tvSelectedTime.setText(time);

            /*Commented By Ishan*/
        /*rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
        rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));*/
        }catch(Exception e){
            new GrocermaxBaseException("DeliveryDetails","fourthTimeSlot",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
        try{
//            EasyTracker.getInstance(this).activityStart(this);
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
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
        try{
//            EasyTracker.getInstance(this).activityStop(this);
            FlurryAgent.onEndSession(this);
        }catch(Exception e){}

        try {
            RocqAnalytics.stopScreen(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class TimeSlotAdapter extends BaseAdapter{

        ArrayList<String> alTimeForDate,alAvailableForDate;

        public TimeSlotAdapter(ArrayList<String> alTimeForDate,ArrayList<String> alAvailableForDate) {
            this.alTimeForDate = alTimeForDate;
            this.alAvailableForDate = alAvailableForDate;
        }

        @Override
        public int getCount() {
            return alTimeForDate.size();
        }

        @Override
        public Object getItem(int position) {
            return alTimeForDate.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.time_slot_grid_item,null);
            }

            final RelativeLayout rlTimeSlot = (RelativeLayout)convertView.findViewById(R.id.rl_time_Slot);
            TextView tvTime = (TextView)convertView.findViewById(R.id.tv_time_slot);
            TextView tvSlotFull = (TextView)convertView.findViewById(R.id.tv_slotfull_slot);

            tvTime.setText(alTimeForDate.get(position));
            tvTime.setTextColor(getResources().getColor(R.color.light_Gray));

            if(alAvailableForDate.get(position).equalsIgnoreCase("0")){
                tvSlotFull.setText("SLOT FULL");
                tvSlotFull.setVisibility(View.VISIBLE);
                tvSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                BlurMaskFilter.Blur style = BlurMaskFilter.Blur.NORMAL;
                applyFilter(tvTime, style);
                rlTimeSlot.setEnabled(false);
            }else{
                tvSlotFull.setText("");
                tvTime.setVisibility(View.VISIBLE);
                tvSlotFull.setVisibility(View.INVISIBLE);
                tvSlotFull.setTextColor(getResources().getColor(R.color.white));
                applyFilterVisible(tvTime, new float[]{0f, -1f, 0.5f}, 0.8f, 15f, 1f);
                rlTimeSlot.setEnabled(true);
            }
            rlTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));


            return convertView;
        }
    }





}


