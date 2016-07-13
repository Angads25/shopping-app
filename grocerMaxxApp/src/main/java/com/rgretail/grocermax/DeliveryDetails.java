package com.rgretail.grocermax;

import android.content.Context;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
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

import org.json.JSONObject;

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

    public static int auto_selected=0;
    public static int selected_position=-1;


    private ImageView ivLeft,ivRight;
    private TextView tvCurrentDate,tvSelectedDate,tv_save_price,tv_shipping,tv_grandTotal;
    GridView grid_time_slot;
    int selectedDatePosition = 0;
    TextView btnCheckoutDateTime;
    private String SCREENNAME = "DeliveryDetails-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_shipping_3);

        auto_selected=0;
        selected_position=-1;
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
            tv_save_price = (TextView) findViewById(R.id.tv_save_price2);
            tv_shipping = (TextView) findViewById(R.id.tv_shipping2);
            tv_grandTotal = (TextView) findViewById(R.id.tv_grandTotal2);

            tv_save_price.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(CartProductList.savingGlobal)));
            if(Float.parseFloat(CartProductList.shippingGlobal)==0)
                tv_shipping.setText("Free");
            else
                tv_shipping.setText(getResources().getString(R.string.rs)+""+String.format("%.2f", Float.parseFloat(CartProductList.shippingGlobal)));

            tv_grandTotal.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(CartProductList.totalGlobal)));


            btnCheckoutDateTime = (TextView) findViewById(R.id.btn_checkout_date_time);

            tvSelectedTime = (TextView) findViewById(R.id.tv_selected_time);
            grid_time_slot=(GridView)findViewById(R.id.grid_time_slot);

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

                        try{
                            UtilityMethods.clickCapture(mContext,"Delivery details","","","",MySharedPrefs.INSTANCE.getSelectedCity());
                            String data=MySharedPrefs.INSTANCE.getUserEmail()+"/"+MySharedPrefs.INSTANCE.getUserId();
                            UtilityMethods.sendGTMEvent(activity,"Delivery Slot",data,"Android Checkout Funnel");
                            RocqAnalytics.trackEvent("Delivery details", new ActionProperties("Category", "Delivery details", "Action", MySharedPrefs.INSTANCE.getSelectedCity()));
                            /*QGraph event*/
                            JSONObject json=new JSONObject();
                            json.put("Delivery Slot",time);
                            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                                json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                            UtilityMethods.setQGraphevent("Andriod Checkout Funnel - Delivery Slot",json);
                   /*--------------*/

                        }catch(Exception e){}

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
                    auto_selected=0;
                    selected_position=-1;
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
                            tvSelectedTime.setVisibility(View.GONE);
                            setTimeSlotting(date);
                        }catch(Exception e){

                        }
                    }
                }
            });
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auto_selected=0;
                    selected_position=-1;
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
                            tvSelectedTime.setVisibility(View.GONE);
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
                //if(false)
                if(UtilityMethods.checkAvailableTimeSlot(alAvailable)){
                    date = date_list.get(1);
                    setTimeSlotting(date);
                    if(date_list.size() > 1){
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat formatter3 = null;
                        Date convertedDate = null;

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
            final ArrayList<String> alAvailable = new ArrayList<String>();
            //int selected_position=-1;


            for(int i=0;i<alTotal.size()/2;i++){
                if(alTotal.get(i)!=null && !alTotal.get(i).equals("null"))
                    alTime.add(alTotal.get(i));
            }
            int index=0;
            for(int i=alTotal.size()/2;i<alTotal.size();i++){
                if(alTotal.get(i)!=null && !alTotal.get(i).equals("null")){
                alAvailable.add(index,alTotal.get(i));
                index++;
                }
            }

            for(int i=0;i<alAvailable.size();i++)
            {
                if(alAvailable.get(i).equals("1"))
                {
                    selected_position=i;
                    break;
                }
            }


            grid_time_slot.setAdapter(new TimeSlotAdapter(alTime,alAvailable));
            UtilityMethods.setGridViewHeightBasedOnChildren(grid_time_slot, 2);
            grid_time_slot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                       selected_position=-1;
                        ArrayList<String> alTotal = address_obj.getDate_timeSlot().get(date);
                        ArrayList<String> alTime = new ArrayList<String>();

                        for(int i=0;i<alTotal.size()/2;i++) {
                            if(alTotal.get(i)!=null && !alTotal.get(i).equals("null"))
                                alTime.add(alTotal.get(i));
                            //alTime.add(i, alTotal.get(i));
                        }

                        if (alAvailable.get(position).equalsIgnoreCase("1")) {
                            for(int i=0;i<grid_time_slot.getAdapter().getCount();i++){
                                if(i==position){
                                    //((RelativeLayout)parent.getChildAt(i).findViewById(R.id.rl_time_Slot)).setBackgroundColor(getResources().getColor(R.color.gray_1));
                                    ((RelativeLayout)parent.getChildAt(i).findViewById(R.id.rl_time_Slot)).setBackgroundColor(getResources().getColor(R.color.bg_dark));
                                    ((TextView)parent.getChildAt(i).findViewById(R.id.tv_time_slot)).setTypeface(null, Typeface.BOLD);
                                      time = alTime.get(position);
                                      tvSelectedTime.setText(time);
                                      tvSelectedTime.setVisibility(View.VISIBLE);
                                }else{
                                    ((TextView)parent.getChildAt(i).findViewById(R.id.tv_time_slot)).setTypeface(null, Typeface.NORMAL);
                                    ((RelativeLayout)parent.getChildAt(i).findViewById(R.id.rl_time_Slot)).setBackgroundColor(getResources().getColor(R.color.white));
                                }
                            }
                        }

                    }catch(Exception e){
                        new GrocermaxBaseException("DeliveryDetails","Slot Select",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
                    }
                }
            });


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
            final ViewHolder holder;
            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.time_slot_grid_item,null);
                holder = new ViewHolder();
                holder.rlTimeSlot = (RelativeLayout)convertView.findViewById(R.id.rl_time_Slot);
                holder.tvTime = (TextView)convertView.findViewById(R.id.tv_time_slot);
                holder.tvSlotFull = (TextView)convertView.findViewById(R.id.tv_slotfull_slot);
                holder.view=(View)convertView.findViewById(R.id.view);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvTime.setText(alTimeForDate.get(position));
            //tvTime.setTextColor(getResources().getColor(R.color.light_Gray));
            holder.tvTime.setTextColor(Color.parseColor("#212121"));

            if(alAvailableForDate.get(position).equalsIgnoreCase("0")){
                holder.tvSlotFull.setText("SLOT FULL");
                holder.tvSlotFull.setVisibility(View.VISIBLE);
                holder.tvSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
                BlurMaskFilter.Blur style = BlurMaskFilter.Blur.NORMAL;
                //applyFilter(tvTime, style);
                holder.tvTime.setTextColor(Color.parseColor("#d6d6d6"));
                holder.rlTimeSlot.setEnabled(false);
                holder.rlTimeSlot.setClickable(false);
                holder.rlTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
            }else{
                holder.tvSlotFull.setText("");
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvSlotFull.setVisibility(View.INVISIBLE);
                holder.tvSlotFull.setTextColor(getResources().getColor(R.color.white));
               // applyFilterVisible(tvTime, new float[]{0f, -1f, 0.5f}, 0.8f, 15f, 1f);
                holder.rlTimeSlot.setEnabled(true);
                System.out.println("select time size = "+alTimeForDate.size());

                if(selected_position==position){
                    holder.rlTimeSlot.setBackgroundColor(getResources().getColor(R.color.bg_dark));
                    holder.tvTime.setTypeface(null, Typeface.BOLD);
                    time = alTimeForDate.get(position);
                    tvSelectedTime.setText(time);
                    tvSelectedTime.setVisibility(View.VISIBLE);
                }


               /* if(auto_selected==0){
                    auto_selected=1;
                    holder.rlTimeSlot.setBackgroundColor(getResources().getColor(R.color.bg_dark));
                    holder.tvTime.setTypeface(null, Typeface.BOLD);
                    time = alTimeForDate.get(position);
                    tvSelectedTime.setText(time);
                    tvSelectedTime.setVisibility(View.VISIBLE);
                }else{
                    //rlTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
                }*/
            }




            if(alTimeForDate.size()%2==0){
                if(position==alTimeForDate.size()-1 || position==alTimeForDate.size()-2)
                    holder.view.setVisibility(View.GONE);
                else
                    holder.view.setVisibility(View.VISIBLE);
            }else{
                if(position==alTimeForDate.size()-1)
                    holder.view.setVisibility(View.GONE);
               else
                    holder.view.setVisibility(View.VISIBLE);
            }



            return convertView;
        }

        public class ViewHolder {
            TextView tvTime,tvSlotFull;
            View view;
            RelativeLayout rlTimeSlot;

        }



    }





}


