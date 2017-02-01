package com.rgretail.grocermax;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rgretail.grocermax.adapters.BillingAdapter;
import com.rgretail.grocermax.adapters.ShippingAdapter;
import com.rgretail.grocermax.api.BillingStateCityLoader;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.api.ShippingLocationLoader;
import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.bean.CheckoutAddressBean;
import com.rgretail.grocermax.bean.DateObject;
import com.rgretail.grocermax.bean.OrderReviewBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Abhishek on 8/22/2015.
 */
public class ShippingAddress extends BaseActivity implements View.OnClickListener {

    private int requestNewAddress = 111;
    private ArrayList<Address> addressList;
    private CheckoutAddressBean address_obj;
    String time="";
    private TextView btnSelectDeliveryDetails,tv_save_price,tv_shipping,tv_grandTotal;
    boolean bIsSelect[];
    public int selectedPosition = -1;            //used in adapter for selected position of address in listview
    String editIndex = "";
    ImageView ivShippingBilling;
    boolean bShippingAsBilling = true;
    ExpandableHeightListView mList;

    ScrollView scrollView;
    LinearLayout ll_place_order;

    TextView tv_date,tv_time;
    ImageView iv_edit_slot;
    public static int index;
    ArrayList<String> date_list;
    HashMap<String,ArrayList<String>> date_timeSlot_new;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd");
    ArrayList<String> dateStatus;
    ArrayList<String> timeStatus;
    String selected_date,selected_time;



    /*for billing */

    public static int requestNewAddress_billing = 222;
    private ArrayList<Address> addressList_billing;
    String time_billing="";
    private CheckoutAddressBean address_obj_billing;
    boolean bIsSelect_billing[];
    public int selectedPosition_billing = -1;
    String editIndex_billing = "";
    LinearLayout ll_billing;


    public static int request = 101;

    public void goToAddress(Address address,int position)
    {
        try{
            if(ShippingLocationLoader.alLocationShipping == null || ShippingLocationLoader.alLocationShipping.size() == 0){                //first time call this service for getting states
                new ShippingLocationLoader(this,address,"shipping",String.valueOf(position)).execute(UrlsConstants.GET_LOCATION_SHIPPING+MySharedPrefs.INSTANCE.getSelectedStateId());
            }else {
                Intent intent = new Intent(mContext, CreateNewAddress.class);
                intent.putExtra("address", address);
                intent.putExtra("shippingorbillingaddress","shipping");

                intent.putExtra("editindex",String.valueOf(position));                                    //means editing the address not adding.
                startActivityForResult(intent, requestNewAddress);
            }
        }catch(Exception e){
            new GrocermaxBaseException("ShippingAddress","goToAddress",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    public void goToBillingAddress(Address address,int position)
    {
        try{
            if(BillingStateCityLoader.alState == null || BillingStateCityLoader.alState.size() == 0){                //first time call this service for getting states
                new BillingStateCityLoader(this,address,"billing",String.valueOf(position)).execute(UrlsConstants.GET_STATE);
            }else {
                Intent intent = new Intent(mContext, CreateNewAddress.class);
                intent.putExtra("address", address);
                intent.putExtra("shippingorbillingaddress", "billing");
                intent.putExtra("editindex", String.valueOf(position));                                    //means editing the address not adding.
                startActivityForResult(intent, requestNewAddress_billing);
            }
        }catch(Exception e){
            new GrocermaxBaseException("BillingAddress","goToAddress",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_shipping_1);
        try {

            addActionsInFilter(MyReceiverActions.ADD_ADDRESS);
            addActionsInFilter(MyReceiverActions.ADD_BILL_ADDRESS);
            addActionsInFilter(MyReceiverActions.CHECKOUT_ADDRESS);
            addActionsInFilter(MyReceiverActions.CHECKOUT_ADDRESS_BILLING);

            if (getIntent().getSerializableExtra("addressBean") != null) {
                address_obj = (CheckoutAddressBean) getIntent().getSerializableExtra("addressBean");
            }
            btnSelectDeliveryDetails = (TextView) findViewById(R.id.btn_checkout_shipping_addr);
            tv_save_price = (TextView) findViewById(R.id.tv_save_price1);
            tv_shipping = (TextView) findViewById(R.id.tv_shipping1);
            tv_grandTotal = (TextView) findViewById(R.id.tv_grandTotal1);

            tv_date = (TextView) findViewById(R.id.tv_date);
            tv_time = (TextView) findViewById(R.id.tv_Time);
            index=-1;
            iv_edit_slot=(ImageView)findViewById(R.id.iv_edit_slot);
            iv_edit_slot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimeSlot();
                }
            });

            scrollView=(ScrollView)findViewById(R.id.scrollView);
            ll_place_order=(LinearLayout) findViewById(R.id.ll_place_order);

            tv_save_price.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(CartProductList.savingGlobal)));
            /*if(Float.parseFloat(CartProductList.shippingGlobal)==0)
                tv_shipping.setText("Free");
            else
                tv_shipping.setText(getResources().getString(R.string.rs)+""+String.format("%.2f", Float.parseFloat(CartProductList.shippingGlobal)));*/

            tv_shipping.setText(MySharedPrefs.INSTANCE.getTotalItem());

            tv_grandTotal.setText(getResources().getString(R.string.rs)+"" + String.format("%.2f", Float.parseFloat(CartProductList.totalGlobal)));


            ll_billing=(LinearLayout)findViewById(R.id.ll_billing);
            /*for shipping*/
            addressList = new ArrayList<Address>();
            for(int i=0;i<address_obj.getAddress().size();i++) {
                if(address_obj.getAddress().get(i).getCity() != null && address_obj.getAddress().get(i).getRegion() != null){
                    if(address_obj.getAddress().get(i).getCity().equalsIgnoreCase(MySharedPrefs.INSTANCE.getSelectedCity()) &&
                            address_obj.getAddress().get(i).getRegion().equalsIgnoreCase(MySharedPrefs.INSTANCE.getSelectedState())) {
                        if (addressList.size()==0) {
                            addressList.add(address_obj.getAddress().get(i));
                        }
                    }
                }
            }
            if(addressList.size()==0){
                Address add=new Address();
                add.setFirstname("Add");
                add.setLastname("Address");
                add.setStreetAddress("...<br>...<br>...");
                addressList.add(add);
                ll_place_order.setBackgroundColor(getResources().getColor(R.color.gray_1));
            }else{
                ll_place_order.setBackgroundColor(Color.parseColor("#0e69e5"));
            }


            /*for billing*/
            addressList_billing = new ArrayList<Address>();
            for(int i=0;i<address_obj.getAddress().size();i++) {
                if(address_obj.getAddress().get(i).getDefaultBilling().equals("true")) {
                    addressList_billing.add(address_obj.getAddress().get(i));
                }
            }



            /*for shipping*/
            if(addressList.size() > 0) {
                ivShippingBilling = (ImageView) findViewById(R.id.iv_same_shipping_billing);
                ivShippingBilling.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(bShippingAsBilling){
                            bShippingAsBilling = false;
                            ivShippingBilling.setImageResource(R.drawable.chkbox_unselected);
                            ll_billing.setVisibility(View.VISIBLE);
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }else{
                            bShippingAsBilling = true;
                            ivShippingBilling.setImageResource(R.drawable.chkbox_selected);
                            ll_billing.setVisibility(View.GONE);
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });
                        }
                    }
                });

                bIsSelect = new boolean[addressList.size()];
                for (int i = 0; i < bIsSelect.length; i++) {
                    bIsSelect[i] = false;
                }
                mList = (ExpandableHeightListView) findViewById(R.id.lv_shipping_addresses);
                ShippingAdapter shippingAdapter;
                shippingAdapter = new ShippingAdapter(ShippingAddress.this, addressList, bIsSelect);
                mList.setAdapter(shippingAdapter);
                mList.setExpanded(true);
                //UtilityMethods.setListViewHeightBasedOnChildren(mList);

            }

            /*for billing*/

            bIsSelect_billing = new boolean[addressList_billing.size()];
            for(int i=0;i<bIsSelect_billing.length;i++) {
                bIsSelect_billing[i] = false;
            }
            ExpandableHeightListView mList_billing = (ExpandableHeightListView) findViewById(R.id.lv_billing_addresses);
            BillingAdapter billingAdapter;
            billingAdapter = new BillingAdapter(ShippingAddress.this, addressList_billing,bIsSelect_billing);
            mList_billing.setAdapter(billingAdapter);
            mList_billing.setExpanded(true);

            TextView tvAddNewAddress = (TextView) findViewById(R.id.add_new_address);
           // tvAddNewAddress.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

            RelativeLayout rlAddNewAddress_billing = (RelativeLayout)findViewById(R.id.rl_add_new_address_billing);
            rlAddNewAddress_billing.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {                                                  //calling for adding new address.
                    // TODO Auto-generated method stub
                    try {
                        if(BillingStateCityLoader.alState == null || BillingStateCityLoader.alState.size() == 0){                //first time call this service for getting states
                            Address addres = null;
                            new BillingStateCityLoader(ShippingAddress.this,addres,"billing","-1").execute(UrlsConstants.GET_STATE);
                        }else {
                            Intent intent = new Intent(mContext, CreateNewAddress.class);
                            intent.putExtra("shippingorbillingaddress", "billing");
                            intent.putExtra("editindex", "-1");                                    //means adding the address not editing.
                            startActivityForResult(intent, requestNewAddress_billing);
                        }
                    } catch (Exception e) {
                        new GrocermaxBaseException("AddressDetail", "goToAddress", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
                    }
                }
            });


                RelativeLayout rlAddNewAddress = (RelativeLayout) findViewById(R.id.rl_add_new_address);
                rlAddNewAddress.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {                                                  //calling for adding new address.
                        // TODO Auto-generated method stub
                        try {
                            if (ShippingLocationLoader.alLocationShipping == null || ShippingLocationLoader.alLocationShipping.size() == 0) {                //first time call this service for getting states
                                Address addres = null;
                                new ShippingLocationLoader(ShippingAddress.this, addres, "shipping", "-1").execute(UrlsConstants.GET_LOCATION_SHIPPING + MySharedPrefs.INSTANCE.getSelectedStateId());
                            } else {
                                Intent intent = new Intent(mContext, CreateNewAddress.class);
                                intent.putExtra("shippingorbillingaddress", "shipping");
                                intent.putExtra("editindex", "-1");                                    //means adding the address not editing.
                                startActivityForResult(intent, requestNewAddress);
                            }
                        } catch (Exception e) {
                            new GrocermaxBaseException("AddressDetail", "goToAddress", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
                        }
                    }
                });




            btnSelectDeliveryDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {

                        if (selectedPosition == -1) {        //mean not pressed any of the address in the list
                            UtilityMethods.customToast(AppConstants.ToastConstant.SHIPPING_ADDRESS_EMPTY, mContext);
                            return;
                        }
                        if (!bShippingAsBilling && selectedPosition_billing == -1) {        //mean not pressed any of the address in the list
                            UtilityMethods.customToast(AppConstants.ToastConstant.BILLING_ADDRESS_EMPTY, mContext);
                            return;
                        }

                        try{
                            UtilityMethods.clickCapture(mContext,"Shipping address","","","",MySharedPrefs.INSTANCE.getSelectedCity());
                            String data=MySharedPrefs.INSTANCE.getUserEmail()+"/"+MySharedPrefs.INSTANCE.getUserId();
                            UtilityMethods.sendGTMEvent(activity,"Shipping",data,"Android Checkout Funnel");

                            /*QGraph event*/
                            JSONObject json=new JSONObject();
                            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                                json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                            UtilityMethods.setQGraphevent("Andriod Checkout Funnel - Shipping",json);
                   /*--------------*/
                        }catch(Exception e){}

                        Address ship_add = addressList.get(selectedPosition);

                        try{
                        if (ship_add.getRegionId() != null && MySharedPrefs.INSTANCE.getSelectedStateRegionId() != null) {
                            if (!ship_add.getRegionId().equals(MySharedPrefs.INSTANCE.getSelectedStateRegionId())) {
                                UtilityMethods.customToast(AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_FIRST + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_SECOND, mContext);
                                return;
                            }
                        }
                        }catch(Exception e){}



                        if (bShippingAsBilling) {
                            OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
                            JSONObject shipping_json_obj = new JSONObject();

                            shipping_json_obj.put("fname", ship_add.getFirstname());
                            shipping_json_obj.put("lname", ship_add.getLastname());
                            shipping_json_obj.put("city", ship_add.getCity());
                            shipping_json_obj.put("region", ship_add.getRegion());
                            shipping_json_obj.put("postcode", ship_add.getPostcode());
                            shipping_json_obj.put("country_id", "IN");
                            shipping_json_obj.put("telephone", ship_add.getTelephone());


                            shipping_json_obj.put("addressline1", ship_add.getStreet());
                            shipping_json_obj.put("addressline2", "");
                            shipping_json_obj.put("default_billing", "0");
                            shipping_json_obj.put("default_shipping", "0");
                            orderReviewBean.setShipping(shipping_json_obj);
                            MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);


                            OrderReviewBean orderReviewBean1 = MySharedPrefs.INSTANCE.getOrderReviewBean();
                            JSONObject billing_json_obj = new JSONObject();
                            billing_json_obj.put("fname", ship_add.getFirstname());
                            billing_json_obj.put("lname", ship_add.getLastname());
                            billing_json_obj.put("city", ship_add.getCity());
                            billing_json_obj.put("region", ship_add.getRegion());
                            billing_json_obj.put("postcode", ship_add.getPostcode());
                            billing_json_obj.put("country_id", "IN");
                            billing_json_obj.put("telephone", ship_add.getTelephone());

                            billing_json_obj.put("addressline1", ship_add.getStreet());
                            billing_json_obj.put("addressline2", "");
                            billing_json_obj.put("default_billing", "0");
                            billing_json_obj.put("default_shipping", "0");
                            orderReviewBean1.setBilling(billing_json_obj);
                            MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean1);

                            /*Intent intent1 = new Intent(ShippingAddress.this, DeliveryDetails.class);
                            intent1.putExtra("addressBean", address_obj);
                            startActivity(intent1);*/
                            OrderReviewBean orderReviewBean2 = MySharedPrefs.INSTANCE.getOrderReviewBean();
                            orderReviewBean2.setDate(selected_date);
                            orderReviewBean2.setTimeSlot(selected_time);
                            MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean2);
                            Intent intent1 = new Intent(ShippingAddress.this, ReviewOrderAndPay.class);
                            startActivity(intent1);
                        } else {
                            OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
                            JSONObject shipping_json_obj = new JSONObject();

                            shipping_json_obj.put("fname", ship_add.getFirstname());
                            shipping_json_obj.put("lname", ship_add.getLastname());
                            shipping_json_obj.put("city", ship_add.getCity());
                            shipping_json_obj.put("region", ship_add.getRegion());
                            shipping_json_obj.put("postcode", ship_add.getPostcode());
                            shipping_json_obj.put("country_id", "IN");
                            shipping_json_obj.put("telephone", ship_add.getTelephone());
                            shipping_json_obj.put("addressline1", ship_add.getStreet());
                            shipping_json_obj.put("addressline2", "");
                            shipping_json_obj.put("default_billing", "0");
                            shipping_json_obj.put("default_shipping", "0");
                            orderReviewBean.setShipping(shipping_json_obj);
                            MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);


                            OrderReviewBean orderReviewBean1 = MySharedPrefs.INSTANCE.getOrderReviewBean();
                            JSONObject billing_json_obj = new JSONObject();
                            Address billing_add = addressList_billing.get(selectedPosition_billing);
                            billing_json_obj.put("fname", billing_add.getFirstname());
                            billing_json_obj.put("lname", billing_add.getLastname());
                            billing_json_obj.put("city", billing_add.getCity());
                            billing_json_obj.put("region", billing_add.getRegion());
                            billing_json_obj.put("postcode", billing_add.getPostcode());
                            billing_json_obj.put("country_id", "IN");
                            billing_json_obj.put("telephone", billing_add.getTelephone());
                            billing_json_obj.put("addressline1", billing_add.getStreet());
                            billing_json_obj.put("addressline2", "");
                            billing_json_obj.put("default_billing", "0");
                            billing_json_obj.put("default_shipping", "0");
                            orderReviewBean1.setBilling(billing_json_obj);
                            MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean1);

                            /*Intent intent1 = new Intent(ShippingAddress.this, DeliveryDetails.class);
                            intent1.putExtra("addressBean", address_obj);
                            startActivity(intent1);*/

                            OrderReviewBean orderReviewBean2 = MySharedPrefs.INSTANCE.getOrderReviewBean();
                            orderReviewBean2.setDate(selected_date);
                            orderReviewBean2.setTimeSlot(selected_time);
                            MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean2);
                            Intent intent1 = new Intent(ShippingAddress.this, ReviewOrderAndPay.class);
                            startActivity(intent1);


                        }
                    } catch (Exception e) {
                        new GrocermaxBaseException("ShippingAddress", "onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
                    }
                }
            });

            /*------ for setting current available slot--------------------*/
            dateStatus=new ArrayList<>();
            timeStatus=new ArrayList<>();
            date_timeSlot_new=address_obj.getDate_timeSlot_new();
            date_list = new ArrayList<String>(address_obj.getDate_timeSlot_new().keySet());          //will contain all dates. in format 2015-08-30
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
                SimpleDateFormat formatter12 = new SimpleDateFormat("E, dd MMM");
                date_list.add(formatter.format(datArrayList.get(i).getDateTime()));                //will contain 25-08-2015 in sorted format
                dateStatus.add(formatter12.format(datArrayList.get(i).getDateTime()));
            }
            selected_date = dateStatus.get(0);
            selected_time=date_timeSlot_new.get(date_list.get(0)).get(0);
            tv_date.setText(selected_date);
            tv_time.setText(selected_time);
            /*----------------------------------------------------------------*/

            initHeader(findViewById(R.id.app_bar_header), true, "Address and Date & Time");
            initFooter(findViewById(R.id.footer), 4, 3);
            icon_header_search.setVisibility(View.GONE);
            icon_header_cart.setVisibility(View.GONE);
            cart_count_txt.setVisibility(View.GONE);
            LinearLayout llIcon = (LinearLayout)findViewById(R.id.ll_placeholder_logoIcon_appBar);
            llIcon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 7f));
        }catch(Exception e){
            new GrocermaxBaseException("ShippingAddress"," btnSelectDeliveryDetails.setOnClickListener",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }


    public void selectAddress(){
        try {
            String userId = MySharedPrefs.INSTANCE.getUserId();
            MyApplication.getAddressFrom="shipping_page";
            showDialog();
            String url = UrlsConstants.ADDRESS_BOOK + userId;
            myApi.reqAddressBook(url, MyReceiverActions.ADDRESS_BOOK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void showTimeSlot(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShippingAddress.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.time_slot_popup, null);
        dialogBuilder.setView(dialogView);

        final CustomNumberPicker np_date=(CustomNumberPicker) dialogView.findViewById(R.id.np_date);
        final CustomNumberPicker np_time=(CustomNumberPicker) dialogView.findViewById(R.id.np_time);
        
        setDividerColor(np_date,getResources().getColor(R.color.orange_text1));
        setDividerColor(np_time,getResources().getColor(R.color.orange_text1));
        TextView tv_done=(TextView)dialogView.findViewById(R.id.tv_done);
        tv_done.setTypeface(CustomFonts.getInstance().getRobotoMedium(ShippingAddress.this));
         timeStatus.clear();
        for(int i=0;i<date_timeSlot_new.get(date_list.get(0)).size();i++){
                timeStatus.add(date_timeSlot_new.get(date_list.get(0)).get(i));
        }
        try {
            String[] date_arryay = dateStatus.toArray(new String[dateStatus.size()]);
            np_date.setMinValue(0);
            np_date.setMaxValue(date_arryay.length-1);
            np_date.setDisplayedValues(date_arryay);
            np_date.setWrapSelectorWheel(false);

            String[] time_arryay = timeStatus.toArray(new String[timeStatus.size()]);
            np_time.setMinValue(0);
            np_time.setMaxValue(time_arryay.length-1);
            np_time.setDisplayedValues(time_arryay);
            np_time.setWrapSelectorWheel(false);
            selected_date=date_arryay[0];
            selected_time=time_arryay[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        np_date.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                timeStatus.clear();
                for(int i=0;i<date_timeSlot_new.get(date_list.get(newVal)).size();i++){
                        timeStatus.add(date_timeSlot_new.get(date_list.get(newVal)).get(i));
                }
                selected_date=dateStatus.get(newVal);
                String[] time_arryay = timeStatus.toArray(new String[timeStatus.size()]);
                try {
                    np_time.setMinValue(0);
                    np_time.setMaxValue(time_arryay.length-1);
                    np_time.setDisplayedValues(time_arryay);
                    np_time.setWrapSelectorWheel(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        np_time.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selected_time=timeStatus.get(newVal);
            }
        });



        final AlertDialog b = dialogBuilder.create();

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_date.setText(selected_date);
                tv_time.setText(selected_time);
                b.dismiss();
            }
        });
        b.show();
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }






    public void addAddress(){
        try {
            if (ShippingLocationLoader.alLocationShipping == null || ShippingLocationLoader.alLocationShipping.size() == 0) {                //first time call this service for getting states
                Address addres = null;
                new ShippingLocationLoader(ShippingAddress.this, addres, "shipping", "-1").execute(UrlsConstants.GET_LOCATION_SHIPPING + MySharedPrefs.INSTANCE.getSelectedStateId());
            } else {
                Intent intent = new Intent(mContext, CreateNewAddress.class);
                intent.putExtra("shippingorbillingaddress", "shipping");
                intent.putExtra("editindex", "-1");                                    //means adding the address not editing.
                startActivityForResult(intent, requestNewAddress);
            }
        } catch (Exception e) {
            new GrocermaxBaseException("AddressDetail", "goToAddress", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
        }
    }



    @Override
    public void OnResponse(Bundle bundle) {
        String action = bundle.getString("ACTION");
        try {
            if (action.equals(MyReceiverActions.CHECKOUT_ADDRESS)) {

                CheckoutAddressBean bean = (CheckoutAddressBean) bundle.getSerializable(ConnectionService.RESPONSE);
                address_obj = bean;
                OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
                JSONObject shipping_json_obj = new JSONObject();
                int index;
                if (editIndex.equals("")) {
                    addressList = address_obj.getAddress();
                    index = addressList.size() - 1;            //doesn't come in this case
                } else if (editIndex.equals("-1")) {
                    addressList = address_obj.getAddress();
                    index = addressList.size() - 1;            //when adding address
                } else {
                    for (int i = 0; i < address_obj.getAddress().size(); i++) {
                        if (address_obj.getAddress().get(i).getDefaultShipping().equals("true")) {
                            addressList.add(address_obj.getAddress().get(i));
                        }
                    }
                    index = Integer.valueOf(editIndex);      //when editing address
                }
                Address ship_add = addressList.get(index);                    //getting the last address of new address list
                shipping_json_obj.put("fname", ship_add.getFirstname());
                shipping_json_obj.put("lname", ship_add.getLastname());
                shipping_json_obj.put("city", ship_add.getCity());
                if (ship_add.getRegion() != null) {
                    if (!ship_add.getRegion().equals("")) {
                        shipping_json_obj.put("region", ship_add.getRegion());
                    }
                }
                shipping_json_obj.put("postcode", ship_add.getPostcode());
                shipping_json_obj.put("country_id", "IN");
                shipping_json_obj.put("telephone", ship_add.getTelephone());
                shipping_json_obj.put("addressline1", ship_add.getStreet());
                shipping_json_obj.put("addressline2", "");
                shipping_json_obj.put("default_billing", "0");
                shipping_json_obj.put("default_shipping", "0");
                orderReviewBean.setShipping(shipping_json_obj);
                MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);

                try {
                    if (ship_add.getRegionId() != null && MySharedPrefs.INSTANCE.getSelectedStateRegionId() != null) {
                        if (!ship_add.getRegionId().equals(MySharedPrefs.INSTANCE.getSelectedStateRegionId())) {
                            UtilityMethods.customToast(AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_FIRST + MySharedPrefs.INSTANCE.getSelectedCity() + "," + MySharedPrefs.INSTANCE.getSelectedState() + AppConstants.ToastConstant.EDIT_DIFFERENT_ADDRESS_SECOND, mContext);
                            return;
                        }
                    }
                }catch(Exception e){}


                finish();

                Intent intent = new Intent(ShippingAddress.this, ShippingAddress.class);
                intent.putExtra("addressBean", bean);
                startActivity(intent);

            }else if (action.equals(MyReceiverActions.CHECKOUT_ADDRESS_BILLING)) {

                CheckoutAddressBean bean = (CheckoutAddressBean) bundle.getSerializable(ConnectionService.RESPONSE);
                address_obj = bean;
                OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
                JSONObject billing_json_obj = new JSONObject();

                int index;
                if(editIndex_billing.equals("")){
                    addressList_billing = address_obj.getAddress();
                    index = addressList_billing.size()-1;            //doesn't come in this case
                }else if(editIndex_billing.equals("-1")){
                    addressList_billing = address_obj.getAddress();
                    index = addressList_billing.size()-1;            //when adding address
                }else{
                    for(int i=0;i<address_obj.getAddress().size();i++) {
                        if(address_obj.getAddress().get(i).getDefaultBilling().equals("true")) {
                            addressList_billing.add(address_obj.getAddress().get(i));
                        }
                    }
                    index = Integer.valueOf(editIndex_billing);      //when editing address
                }

                Address billing_add = addressList_billing.get(index);                    //getting the last address of new address list
                billing_json_obj.put("fname", billing_add.getFirstname());
                billing_json_obj.put("lname", billing_add.getLastname());
                billing_json_obj.put("city", billing_add.getCity());
                billing_json_obj.put("region", billing_add.getRegion());
                billing_json_obj.put("postcode", billing_add.getPostcode());
                billing_json_obj.put("country_id", "IN");
                billing_json_obj.put("telephone", billing_add.getTelephone());
                billing_json_obj.put("addressline1", billing_add.getStreet());
                billing_json_obj.put("addressline2","");
                billing_json_obj.put("default_billing","0");
                billing_json_obj.put("default_shipping", "0");
                orderReviewBean.setBilling(billing_json_obj);
                MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);

                finish();

                Intent intent1 = new Intent(ShippingAddress.this, ShippingAddress.class);
                intent1.putExtra("addressBean", bean);
                startActivity(intent1);
            }
        }catch (Exception e){
            new GrocermaxBaseException("ShippingAddress","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == requestNewAddress && resultCode==RESULT_OK) {
                //New address added refresh list
                editIndex = data.getStringExtra("editIndex");
                showDialog();
                String url = UrlsConstants.CHECKOUT_ADDRESS_BOOK+ MySharedPrefs.INSTANCE.getUserId();
                myApi.reqCheckOutAddress(url,MyReceiverActions.CHECKOUT_ADDRESS);
            }
            if (requestCode == requestNewAddress_billing && resultCode==RESULT_OK) {
                //New address added refresh list
                editIndex_billing = data.getStringExtra("editIndex");
                showDialog();
                String url = UrlsConstants.CHECKOUT_ADDRESS_BOOK+ MySharedPrefs.INSTANCE.getUserId();
                myApi.reqCheckOutAddress(url,MyReceiverActions.CHECKOUT_ADDRESS_BILLING);
            }
            if (requestCode == request && resultCode==RESULT_OK) {
                Bundle bundle = data.getExtras();
                Address a = (Address) bundle.getSerializable("AddressList");
                addressList.clear();
                addressList.add(a);
                bIsSelect = new boolean[addressList.size()];
                for (int i = 0; i < bIsSelect.length; i++) {
                    bIsSelect[i] = false;
                }
                ShippingAdapter shippingAdapter = new ShippingAdapter(ShippingAddress.this, addressList, bIsSelect);
                mList.setAdapter(shippingAdapter);
                mList.setExpanded(true);
            }
        }catch (Exception e){
            new GrocermaxBaseException("ShippingAddress","onActivityResult",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initHeader(findViewById(R.id.app_bar_header), true, "Address and Date & Time");
        LinearLayout llIcon = (LinearLayout)findViewById(R.id.ll_placeholder_logoIcon_appBar);
        llIcon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,7f));
    }


    @Override
    public void onClick(View v) {

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

}


