package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.adapters.BillingAdapter;
import com.rgretail.grocermax.api.BillingStateCityLoader;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.Address;
import com.rgretail.grocermax.bean.CheckoutAddressBean;
import com.rgretail.grocermax.bean.OrderReviewBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

import java.util.ArrayList;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Abhishek on 8/22/2015.
 */
public class BillingAddress extends BaseActivity implements View.OnClickListener {
    private LayoutInflater inflater = null;
    private Spinner spinner_shipping;
    private LinearLayout layout_shipping = null;
    //	private CheckBox check_billing = null;
    LinearLayout llCheckBox;
    ImageView ivCheckBox;
    TextView tvCheckBox;
    boolean bCheck = false;

    private TextView text_billing;
    private Spinner spinner_billing;
    private ImageView iv_left_bottom_spinner;
    private LinearLayout layout_billing = null;
    private Button btnSaveAddress;
    private Button button_place_order;
    public static int requestNewAddress = 111;
    private ArrayList<Address> addressList;
    private ArrayList<String> profileNames;
    private CheckoutAddressBean address_obj;
    LinearLayout llTop;
//    EasyTracker tracker;
    ImageView imgDateSlot[];
    TextView tvDateSlot[];
    TextView tvHeaderMsg;
    private ArrayList<String> slots;
    LinearLayout ll_date;

    ArrayList<String> date_list;
    int dateSelectedposition=0;
    TextView ed[];
    RelativeLayout rlShippingAddr,rlBillingAddr;            //code

    RelativeLayout rlShippingAddrTop,rlBillingAddrTop;

    TextView tvShippingHeaderName,tvShippingAddrName,tvShippingAddr1,tvShippingAddr2,tvShippingAddr3,tvShippingAddr4,tvShippingAddr5,tvShippingAddr6;    //code
    TextView tvBillingHeaderName,tvBillingAddrName,tvBillingAddr1,tvBillingAddr2,tvBillingAddr3,tvBillingAddr4,tvBillingAddr5,tvBillingAddr6;          //code
    ListView lv_time_list;

    EditText edt_ship_fname,edt_ship_lname,edt_ship_number,edt_ship_address1,edt_ship_address2,edt_ship_city,edt_ship_state,edt_ship_country,edt_ship_pincode;
    EditText edt_bill_fname,edt_bill_lname,edt_bill_number,edt_bill_address1,edt_bill_address2,edt_bill_city,edt_bill_state,edt_bill_country,edt_bill_pincode;

    String date;
    String time="";
    private TextView tvSelectedTime;
    private RelativeLayout rlFirstTimeSlot,rlSecondTimeSlot,rlThirdTimeSlot,rlFourthTimeSlot;
    private TextView tvFirstSlotFull,tvSecondSlotFull,tvThirdSlorFull,tvFourthSlotFull;
    private TextView tvFirstTime,tvSecondTime,tvThirdTime,tvFourthTime;

    private ImageView ivLeft,ivRight;
    private TextView tvCurrentDate,tvSelectedDate;
    int selectedDatePosition = 0;

    ImageView billingDropDown;



    Button btn1TimeSlot,btn2TimeSlot,btn3TimeSlot,btn4TimeSlot;
    RelativeLayout rl1,rl2,rl3,rl4;

    TextView textDate,textTimeSlot,textShipping,textBilling;

    TextView tvPreviousSelected = null;              //click on date and holds for setting previous selected color to gray

    Button btnCheckoutBillingAddr;
    boolean bIsSelect[];
    public int selectedPosition = -1;
    String editIndex = "";                           //use when adding or editing the address on checkout screen will tell on which address user has edited.so that particular address being selected automatically and move on to next screen.

    public void goToAddress(Address address,int position)
    {
        try{
        if(BillingStateCityLoader.alState == null || BillingStateCityLoader.alState.size() == 0){                //first time call this service for getting states
            new BillingStateCityLoader(this,address,"billing",String.valueOf(position)).execute(UrlsConstants.GET_STATE);
        }else {
            Intent intent = new Intent(mContext, CreateNewAddress.class);
            intent.putExtra("address", address);
            intent.putExtra("shippingorbillingaddress", "billing");
            intent.putExtra("editindex", String.valueOf(position));                                    //means editing the address not adding.
            startActivityForResult(intent, requestNewAddress);
        }
        }catch(Exception e){
            new GrocermaxBaseException("BillingAddress","goToAddress",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    public void StateCityBilling(){
        try{
        showDialog();
        String url = UrlsConstants.GET_STATE;
        myApi.reqDeleteFromCart(url);
        }catch(Exception e){
            new GrocermaxBaseException("BillingAddress","StateCityBilling",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_billing_2);

        try{
            AppsFlyerLib.setCurrencyCode("INR");
            AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
            AppsFlyerLib.sendTracking(getApplicationContext());
        }catch(Exception e){}

        try {
            addActionsInFilter(MyReceiverActions.ADD_ADDRESS);
            addActionsInFilter(MyReceiverActions.ADD_BILL_ADDRESS);
            addActionsInFilter(MyReceiverActions.CHECKOUT_ADDRESS);

            if (getIntent().getSerializableExtra("addressBean") != null) {
                address_obj = (CheckoutAddressBean) getIntent().getSerializableExtra("addressBean");
//                addressList = address_obj.getAddress();
            }

            addressList = new ArrayList<Address>();
            for(int i=0;i<address_obj.getAddress().size();i++) {
                if(address_obj.getAddress().get(i).getDefaultBilling().equals("true")) {
                    addressList.add(address_obj.getAddress().get(i));
                }
            }



            bIsSelect = new boolean[addressList.size()];
            for(int i=0;i<bIsSelect.length;i++) {
                bIsSelect[i] = false;
            }

            ListView mList = (ListView) findViewById(R.id.lv_billing_addresses);
            BillingAdapter billingAdapter;
            billingAdapter = new BillingAdapter(BillingAddress.this, addressList,bIsSelect);
            mList.setAdapter(billingAdapter);

            btnCheckoutBillingAddr = (Button) findViewById(R.id.btn_checkout_billing_addr);
            btnCheckoutBillingAddr.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
            btnCheckoutBillingAddr.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {                                                  //calling for adding new address.
                    // TODO Auto-generated method stub
                    try {
                        if (selectedPosition == -1) {        //mean not pressed any of the address in the list
                            UtilityMethods.customToast(AppConstants.ToastConstant.BILLING_ADDRESS_EMPTY, mContext);
                            return;
                        }

                        try{
                            UtilityMethods.clickCapture(mContext,"Billing address","","","",MySharedPrefs.INSTANCE.getSelectedCity());
                            String data=MySharedPrefs.INSTANCE.getUserEmail()+"/"+MySharedPrefs.INSTANCE.getUserId();
                            UtilityMethods.sendGTMEvent(activity,"Billing",data,"Android Checkout Funnel");
                            RocqAnalytics.trackEvent("Billing address", new ActionProperties("Category", "Billing address", "Action", MySharedPrefs.INSTANCE.getSelectedCity()));
                        }catch(Exception e){}

                        OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
                        JSONObject billing_json_obj = new JSONObject();
                        Address billing_add = addressList.get(selectedPosition);
                        billing_json_obj.put("fname", billing_add.getFirstname());
                        billing_json_obj.put("lname", billing_add.getLastname());
                        billing_json_obj.put("city", billing_add.getCity());
//                        billing_json_obj.put("region", billing_add.getState());
                        billing_json_obj.put("region", billing_add.getRegion());
                        billing_json_obj.put("postcode", billing_add.getPostcode());
                        billing_json_obj.put("country_id", "IN");
                        billing_json_obj.put("telephone", billing_add.getTelephone());
                        billing_json_obj.put("addressline1", billing_add.getStreet());
                        billing_json_obj.put("addressline2", "");
                        billing_json_obj.put("default_billing", "0");
                        billing_json_obj.put("default_shipping", "0");
                        orderReviewBean.setBilling(billing_json_obj);
                        MySharedPrefs.INSTANCE.putOrderReviewBean(orderReviewBean);

                        Intent intent1 = new Intent(BillingAddress.this, DeliveryDetails.class);
                        intent1.putExtra("addressBean", address_obj);
                        startActivity(intent1);
                    } catch (Exception e) {
                        new GrocermaxBaseException("BillingAddress", "onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
                    }
                }
            });

            TextView tvAddNewAddress = (TextView) findViewById(R.id.add_new_address);
            tvAddNewAddress.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));

            RelativeLayout rlAddNewAddress = (RelativeLayout)findViewById(R.id.rl_add_new_address);

            rlAddNewAddress.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {                                                  //calling for adding new address.
                    // TODO Auto-generated method stub
                    try {
                        if(BillingStateCityLoader.alState == null || BillingStateCityLoader.alState.size() == 0){                //first time call this service for getting states
                          //  try{UtilityMethods.clickCapture(mContext,"","","","",AppConstants.GA_EVENT_NEW_BILLING_ADDRESS_SELECT);}catch(Exception e){}
                            Address addres = null;
                            new BillingStateCityLoader(BillingAddress.this,addres,"billing","-1").execute(UrlsConstants.GET_STATE);
                        }else {
                           // try{UtilityMethods.clickCapture(mContext,"","","","",AppConstants.GA_EVENT_NEW_BILLING_ADDRESS_SELECT);}catch(Exception e){}
                            Intent intent = new Intent(mContext, CreateNewAddress.class);
                            intent.putExtra("shippingorbillingaddress", "billing");
                            intent.putExtra("editindex", "-1");                                    //means adding the address not editing.
                            startActivityForResult(intent, requestNewAddress);
                        }
                    } catch (Exception e) {
                        new GrocermaxBaseException("AddressDetail", "goToAddress", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
                    }
                }
            });



            initHeader(findViewById(R.id.app_bar_header), true, "Select Billing Address");
            initFooter(findViewById(R.id.footer), 4, 3);
            icon_header_search.setVisibility(View.GONE);
            icon_header_cart.setVisibility(View.GONE);
            cart_count_txt.setVisibility(View.GONE);
            LinearLayout llIcon = (LinearLayout)findViewById(R.id.ll_placeholder_logoIcon_appBar);
            llIcon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 7f));
        }catch(Exception e){
            new GrocermaxBaseException("BillingAddress","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }



    @Override
    public void OnResponse(Bundle bundle) {
        String action = bundle.getString("ACTION");
        try {
            if (action.equals(MyReceiverActions.CHECKOUT_ADDRESS)) {

                CheckoutAddressBean bean = (CheckoutAddressBean) bundle.getSerializable(ConnectionService.RESPONSE);
			/*if(bean.getAddress().size()>0)
			{*/
//                Intent intent = new Intent(BillingAddress.this, BillingAddress.class);
//                intent.putExtra("addressBean", bean);
//                startActivity(intent);

                address_obj = bean;
//                addressList = address_obj.getAddress();

                OrderReviewBean orderReviewBean = MySharedPrefs.INSTANCE.getOrderReviewBean();
                JSONObject billing_json_obj = new JSONObject();

                int index;
                if(editIndex.equals("")){
                    addressList = address_obj.getAddress();
                    index = addressList.size()-1;            //doesn't come in this case
                }else if(editIndex.equals("-1")){
                    addressList = address_obj.getAddress();
                    index = addressList.size()-1;            //when adding address
                }else{
                    for(int i=0;i<address_obj.getAddress().size();i++) {
                        if(address_obj.getAddress().get(i).getDefaultBilling().equals("true")) {
                            addressList.add(address_obj.getAddress().get(i));
                        }
                    }
                    index = Integer.valueOf(editIndex);      //when editing address
                }

                Address billing_add = addressList.get(index);                    //getting the last address of new address list
                billing_json_obj.put("fname", billing_add.getFirstname());
                billing_json_obj.put("lname", billing_add.getLastname());
                billing_json_obj.put("city", billing_add.getCity());
//                billing_json_obj.put("region", billing_add.getState());
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

                Intent intent1 = new Intent(BillingAddress.this, BillingAddress.class);
                intent1.putExtra("addressBean", bean);
                startActivity(intent1);
//                Intent intent1 = new Intent(BillingAddress.this, DeliveryDetails.class);
//                intent1.putExtra("addressBean", bean);
//                startActivity(intent1);

			/*}else{
				Toast.makeText(CartProductList.this,ToastConstant.NO_ACCOUNT_ADDR,0).show();
			}*/
            }
        }catch (Exception e){
            new GrocermaxBaseException("BillingAddress","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
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
                myApi.reqCheckOutAddress(url);
//			finish();
            }else{
//                UtilityMethods.customToast(Constants.ToastConstant.ERROR_MSG, mContext);
            }
        }catch (Exception e){
            new GrocermaxBaseException("BillingAddress","onActivityResult",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}

        initHeader(findViewById(R.id.app_bar_header), true, "Select Billing Address");
        LinearLayout llIcon = (LinearLayout)findViewById(R.id.ll_placeholder_logoIcon_appBar);
        llIcon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 7f));
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

}


