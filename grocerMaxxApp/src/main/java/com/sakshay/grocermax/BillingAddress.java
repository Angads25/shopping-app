package com.sakshay.grocermax;

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

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.BillingAdapter;
import com.sakshay.grocermax.api.BillingStateCityLoader;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.bean.CheckoutAddressBean;
import com.sakshay.grocermax.bean.OrderReviewBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import org.json.JSONObject;

import java.util.ArrayList;

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
    EasyTracker tracker;
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

//			tvSelectedTime = (TextView) findViewById(R.id.tv_selected_time);
//
//			rlFirstTimeSlot = (RelativeLayout) findViewById(R.id.rl_first_time_Slot);
//			rlSecondTimeSlot = (RelativeLayout) findViewById(R.id.rl_second_time_Slot);
//			rlThirdTimeSlot = (RelativeLayout) findViewById(R.id.rl_third_time_Slot);
//			rlFourthTimeSlot = (RelativeLayout) findViewById(R.id.rl_fourth_time_Slot);
//
//			tvFirstTime = (TextView) findViewById(R.id.tv_time_1st_slot);
//			tvSecondTime = (TextView) findViewById(R.id.tv_time_2nd_slot);
//			tvThirdTime = (TextView) findViewById(R.id.tv_time_3rd_slot);
//			tvFourthTime = (TextView) findViewById(R.id.tv_time_4th_slot);
//
//			tvFirstSlotFull = (TextView) findViewById(R.id.tv_slotfull_1st_slot);
//			tvSecondSlotFull = (TextView) findViewById(R.id.tv_slotfull_2nd_slot);
//			tvThirdSlorFull = (TextView) findViewById(R.id.tv_slotfull_3rd_slot);
//			tvFourthSlotFull = (TextView) findViewById(R.id.tv_slotfull_4th_slot);
//
//			rlFirstTimeSlot.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					firstTimeSlot();
//				}
//			});
//			rlSecondTimeSlot.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					secondTimeSlot();
//				}
//			});
//			rlThirdTimeSlot.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					thirdTimeSlot();
//				}
//			});
//			rlFourthTimeSlot.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					fourthTimeSlot();
//				}
//			});
//
//			ivLeft = (ImageView) findViewById(R.id.iv_left);
//			ivRight = (ImageView) findViewById(R.id.iv_right);
//			tvCurrentDate = (TextView) findViewById(R.id.tv_current_date);
//			tvSelectedDate = (TextView) findViewById(R.id.tv_selected_date);
//
//			ivLeft.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if(selectedDatePosition == 0){
//
//					}else{
//						selectedDatePosition--;
//						tvCurrentDate.setText(date_list.get(selectedDatePosition));
//						tvSelectedDate.setText(date_list.get(selectedDatePosition));
//						date = date_list.get(selectedDatePosition);
//
//						time = "";
//						setTimeSlotting(date);
//
//						tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//						tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//						tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//						tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//						tvFirstSlotFull.setText("");
//						tvSecondSlotFull.setText("");
//						tvThirdSlorFull.setText("");
//						tvFourthSlotFull.setText("");
//
//						tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//						tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//						tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//						tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));
//
//						rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//						rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//						rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//						rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//
//					}
//				}
//			});
//			ivRight.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if(selectedDatePosition == date_list.size()-1){
//
//					}else{
//						selectedDatePosition++;
//						tvCurrentDate.setText(date_list.get(selectedDatePosition));
//						tvSelectedDate.setText(date_list.get(selectedDatePosition));
//						date = date_list.get(selectedDatePosition);
//
//						time = "";
//						setTimeSlotting(date);
//
//						tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//						tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//						tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//						tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//						tvFirstSlotFull.setText("");
//						tvSecondSlotFull.setText("");
//						tvThirdSlorFull.setText("");
//						tvFourthSlotFull.setText("");
//
//						tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//						tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//						tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//						tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));
//
//						rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//						rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//						rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//						rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//					}
//				}
//			});
//			btn1TimeSlot = (Button) findViewById(R.id.btn1_time_slot);
//			btn2TimeSlot = (Button) findViewById(R.id.btn2_time_slot);
//			btn3TimeSlot = (Button) findViewById(R.id.btn3_time_slot);
//			btn4TimeSlot = (Button) findViewById(R.id.btn4_time_slot);
//
//			rl1 = (RelativeLayout) findViewById(R.id.rl_1);
//			rl2 = (RelativeLayout) findViewById(R.id.rl_2);
//			rl3 = (RelativeLayout) findViewById(R.id.rl_3);
//			rl4 = (RelativeLayout) findViewById(R.id.rl_4);
//
//			textDate = (TextView) findViewById(R.id.text_date);
//			textTimeSlot = (TextView) findViewById(R.id.text_time_slot);
//			textShipping = (TextView) findViewById(R.id.text_shipping);
//			textBilling = (TextView) findViewById(R.id.text_billing);
//
//			textDate.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//			textTimeSlot.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//			textShipping.setTypeface(CustomFonts.getInstance().getRobotoBold(this));

//			textBilling.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//
//
//			button_place_order.setTypeface(CustomFonts.getInstance().getRobotoMedium(this));
//			tvHeaderMsg.setTypeface(CustomFonts.getInstance().getRobotoBold(this));
//
//			rlShippingAddr.setVisibility(View.GONE);
//			rlShippingAddrTop.setVisibility(View.GONE);
//			rlBillingAddr.setVisibility(View.GONE);
//			rlBillingAddrTop.setVisibility(View.GONE);
//
//			btn1TimeSlot.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					firstTimeSlot();
//				}
//			});
//
//			btn2TimeSlot.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					secondTimeSlot();
//				}
//			});
//
//			btn3TimeSlot.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					thirdTimeSlot();
//				}
//			});
//
//			btn4TimeSlot.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					fourthTimeSlot();
//				}
//			});
//
//			tvFirstTime.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					firstTimeSlot();
//				}
//			});
//
//			tvSecondTime.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					secondTimeSlot();
//				}
//			});
//
//			tvThirdTime.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					thirdTimeSlot();
//				}
//			});
//
//			tvFourthTime.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					fourthTimeSlot();
//				}
//			});
//
//                                                                           //to commented
//			if (addressList != null && addressList.size() > 0) {
//				inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//				date_list = new ArrayList<String>(address_obj.getDate_timeSlot().keySet());
//				ArrayList<DateObject> datArrayList = new ArrayList<DateObject>();
//				for (int i = 0; i < date_list.size(); i++) {
//					DateObject dateObject = new DateObject();
//					try {
//						dateObject.setDateTime(formatter.parse(date_list.get(i)));
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					datArrayList.add(dateObject);
//				}
//				Collections.sort(datArrayList);
//				date_list.clear();
//
//				for (int i = 0; i < datArrayList.size(); i++) {
//					date_list.add(formatter.format(datArrayList.get(i).getDateTime()));
//				}
//				date = date_list.get(0);
//				setTimeSlotting(date);
//				if(date_list.size() > 0){
//					tvSelectedDate.setText(date_list.get(0));
//					tvCurrentDate.setText(date_list.get(0));
//				}

//				imgDateSlot = new ImageView[date_list.size()];
//				tvDateSlot = new TextView[date_list.size()];
//
//				int tempInt = -1;                                          //once in below loop counting 2 layouts,so manage locally in below loop.
//
//				View view = inflater.inflate(R.layout.item_cat_main, null);
//
//				LinearLayout llTimeSlot = (LinearLayout) findViewById(R.id.ll_date_slot);
//				for (int i = 0; i < date_list.size() / 2; i++) {
//					tempInt++;
//					View oneLineView = inflater.inflate(R.layout.date_slot, null, false);
//
//					llTop = (LinearLayout) oneLineView.findViewById(R.id.one_line_top);
//
//					LinearLayout llLeft = (LinearLayout) oneLineView.findViewById(R.id.ll_left_1_line);
//					llLeft.setVisibility(View.VISIBLE);
//					imgDateSlot[tempInt] = (ImageView) oneLineView.findViewById(R.id.iv_left_1_one_line);
//					tvDateSlot[tempInt] = (TextView) oneLineView.findViewById(R.id.tv_right_1_one_line);
//					llLeft.setTag(tempInt);
//					llLeft.setOnClickListener(listener);
//
//					if (i == 0) {
//						imgDateSlot[tempInt].setImageResource(R.drawable.check_pay);
//						tvDateSlot[tempInt].setBackgroundResource(R.drawable.delivery_slot_selected_btn);
//						tvDateSlot[tempInt].setTextColor(getResources().getColor(R.color.delivery_slot_text_selected_color));
//						tvDateSlot[tempInt].setText(date_list.get(tempInt));
//					} else {
//						imgDateSlot[tempInt].setImageResource(R.drawable.uncheck_pay);
//						tvDateSlot[tempInt].setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//						tvDateSlot[tempInt].setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//						tvDateSlot[tempInt].setText(date_list.get(tempInt));
//					}
//
//					tempInt++;
//
//					LinearLayout llRight = (LinearLayout) oneLineView.findViewById(R.id.ll_right_1_line);
//					llRight.setVisibility(View.VISIBLE);
//					imgDateSlot[tempInt] = (ImageView) oneLineView.findViewById(R.id.iv_left_2_line);
//					tvDateSlot[tempInt] = (TextView) oneLineView.findViewById(R.id.tv_right_2_line);
//					llRight.setTag(tempInt);
//					llRight.setOnClickListener(listener);
//					imgDateSlot[tempInt].setImageResource(R.drawable.uncheck_pay);
//					tvDateSlot[tempInt].setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//					tvDateSlot[tempInt].setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//					tvDateSlot[tempInt].setText(date_list.get(tempInt));
//
//					llTimeSlot.addView(llTop);
//
//					if (date_list.size() % 2 != 0) {                     //if records are odd number then in last row only this view added on left side.
//						//odd timing records from server
//						if (i + 1 == date_list.size() / 2) {
//							tempInt++;
//
//							View oneLineViewLast = inflater.inflate(R.layout.date_slot, null, false);
//
//							llTop = (LinearLayout) oneLineViewLast.findViewById(R.id.one_line_top);
//
//							LinearLayout llLeftLast = (LinearLayout) oneLineViewLast.findViewById(R.id.ll_left_1_line);
//							llLeftLast.setVisibility(View.VISIBLE);
//							imgDateSlot[tempInt] = (ImageView) oneLineViewLast.findViewById(R.id.iv_left_1_one_line);
//							tvDateSlot[tempInt] = (TextView) oneLineViewLast.findViewById(R.id.tv_right_1_one_line);
//							llLeftLast.setTag(tempInt);
//							llLeftLast.setOnClickListener(listener);
//							imgDateSlot[tempInt].setImageResource(R.drawable.uncheck_pay);
//							tvDateSlot[tempInt].setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//							tvDateSlot[tempInt].setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//							tvDateSlot[tempInt].setText(date_list.get(tempInt));
//
//							LinearLayout llRightLast = (LinearLayout) oneLineViewLast.findViewById(R.id.ll_right_1_line);
//							llRightLast.setVisibility(View.INVISIBLE);
//							llTimeSlot.addView(llTop);
//						}
//					}
//
//				}
//
//				spinner_shipping = (Spinner) findViewById(R.id.spinner_shipping);
//				layout_shipping = (LinearLayout) findViewById(R.id.layout_shipping);
//
//				profileNames = new ArrayList<String>();
//				profileNames.add("Select an address");
//				profileNames.add("New address");
//				for (int i = 0; i < addressList.size(); i++) {
//					String profile_name = addressList.get(i).getFirstname() + " " + addressList.get(i).getLastname();
//
//					profileNames.add(profile_name);
//				}
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
                            Address addres = null;
                            new BillingStateCityLoader(BillingAddress.this,addres,"billing","-1").execute(UrlsConstants.GET_STATE);
                        }else {
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

//				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//						R.layout.spinner_textview, profileNames);
//				spinner_shipping.setAdapter(dataAdapter);
//
//				spinner_shipping.setOnItemSelectedListener(new OnItemSelectedListener() {
//					@Override
//					public void onItemSelected(AdapterView<?> parent, View view,
//											   int position, long id) {
//
//						//parent.getItemAtPosition(position);      //selected item
//
//						if (position > 1) {
//							shippingAddress(addressList.get(position - 2));           //code
//
//							if (layout_shipping != null) {                                      //from code
//								layout_shipping.setVisibility(View.GONE);
//							}                                                                 //to code
//						} else if (position == 1) {
//							showShipNewAddress(layout_shipping);
//							rlShippingAddr.setVisibility(View.GONE);
//							rlShippingAddrTop.setVisibility(View.GONE);
//						} else {
//							hideAddress(layout_shipping);
//							rlShippingAddr.setVisibility(View.GONE);
//							rlShippingAddrTop.setVisibility(View.GONE);
//						}
//						//TODO: For create new address
//						//Intent intent = new Intent(mContext, CreateNewAddress.class);
//						//startActivityForResult(intent, requestNewAddress);
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> arg0) {
//					}
//				});
//
//				text_billing = (TextView) findViewById(R.id.text_billing);
//				spinner_billing = (Spinner) findViewById(R.id.spinner_billing);
//				iv_left_bottom_spinner = (ImageView) findViewById(R.id.iv_left_bottom_spinner);
//				billingDropDown = (ImageView) findViewById(R.id.billing_drop_down);
//				layout_billing = (LinearLayout) findViewById(R.id.layout_billing);
//
//				spinner_billing.setAdapter(dataAdapter);
//
//				spinner_billing.setOnItemSelectedListener(new OnItemSelectedListener() {
//					@Override
//					public void onItemSelected(AdapterView<?> arg0, View arg1,
//											   int position, long arg3) {
//						if (position > 1) {
//							billingAddress(addressList.get(position - 2));            //code
////						showAddress(layout_billing, addressList.get(position-2));        //commented
//							if (layout_billing != null) {
//								layout_billing.setVisibility(View.GONE);
//							}
//						} else if (position == 1) {
//							showBillNewAddress(layout_billing);
//							rlBillingAddr.setVisibility(View.GONE);
//							rlBillingAddrTop.setVisibility(View.GONE);
//						} else {
//							hideAddress(layout_billing);
//							rlBillingAddr.setVisibility(View.GONE);
//							rlBillingAddrTop.setVisibility(View.GONE);
//						}
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> arg0) {
//					}
//				});
//				hideBillingAddress();
//
//
//				llCheckBox = (LinearLayout) findViewById(R.id.ll_checkbox);
//				ivCheckBox = (ImageView) findViewById(R.id.iv_checkbox);
//				tvCheckBox = (TextView) findViewById(R.id.tv_checkbox);
//
//				ivCheckBox.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						if (bCheck) {
//							bCheck = false;
//						} else {
//							bCheck = true;
//						}
//
//						if (bCheck) {
//							ivCheckBox.setBackgroundResource(R.drawable.cb_checked);
//							showBillingAddress();
//						} else {
//							ivCheckBox.setBackgroundResource(R.drawable.cb_unchecked);
//							hideBillingAddress();
//						}
//
//
//					}
//				});
//
//				tvCheckBox.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						if (bCheck) {
//							bCheck = false;
//						} else {
//							bCheck = true;
//						}
//
//						if (bCheck) {
//							ivCheckBox.setBackgroundResource(R.drawable.cb_checked);
//							showBillingAddress();
//						} else {
//							ivCheckBox.setBackgroundResource(R.drawable.cb_unchecked);
//							hideBillingAddress();
//						}
//					}
//				});
//
//				button_place_order.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						callreviewOrderApi();
//					}
//				});
//			} else {
//
//				findViewById(R.id.scroll_view).setVisibility(View.GONE);
//				TextView msg = (TextView) findViewById(R.id.msg);
//				msg.setVisibility(View.VISIBLE);
//				msg.setText("You don't have any address saved. Please go to address book and create a new address to continue");
//				button_place_order.setEnabled(false);
//
//
//				btnSaveAddress.setEnabled(true);
//				btnSaveAddress.setVisibility(View.VISIBLE);
//				btnSaveAddress.setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
////					Address address = null;
////					Intent intent = new Intent(mContext, CreateNewAddress.class);
////					intent.putExtra("address", address);
////					startActivity(intent);
////					startActivityForResult(intent, requestNewAddress);
//							}
//						});
//
//			}

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

//	private void setTimeSlotting(String date)
//	{
//		try {
//			ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//			if(alTime.size()>0){
//				tvSelectedTime.setText(alTime.get(0));
//				time = alTime.get(0);
//			}
//
//			if (alTime.size() == 4) {
//				tvFirstTime.setText(alTime.get(0));
//				tvSecondTime.setText(alTime.get(1));
//				tvThirdTime.setText(alTime.get(2));
//				tvFourthTime.setText(alTime.get(3));
//
//				tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//				tvFirstSlotFull.setText("");
//				tvSecondSlotFull.setText("");
//				tvThirdSlorFull.setText("");
//				tvFourthSlotFull.setText("");
//
//				tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//				tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//				tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//				tvFourthSlotFull.setTextColor(getResources().getColor(R.color.white));
//
//				rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//			} else if (alTime.size() == 3) {
//				tvFirstTime.setText(alTime.get(0));
//				tvSecondTime.setText(alTime.get(1));
//				tvThirdTime.setText(alTime.get(2));
//
//				tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvFourthTime.setTextColor(getResources().getColor(R.color.pallete_grey));
//
//				tvFirstSlotFull.setText("");
//				tvSecondSlotFull.setText("");
//				tvThirdSlorFull.setText("");
//				tvFourthSlotFull.setText("SLOT FULL");
//
//				tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//				tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//				tvThirdSlorFull.setTextColor(getResources().getColor(R.color.white));
//				tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//				rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlFourthTimeSlot.setEnabled(false);
//
//			} else if (alTime.size() == 2) {
//				tvFirstTime.setText(alTime.get(0));
//				tvSecondTime.setText(alTime.get(1));
//
//				tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvThirdTime.setTextColor(getResources().getColor(R.color.pallete_grey));
//				tvFourthTime.setTextColor(getResources().getColor(R.color.pallete_grey));
//
//				tvFirstSlotFull.setText("");
//				tvSecondSlotFull.setText("");
//				tvThirdSlorFull.setText("SLOT FULL");
//				tvFourthSlotFull.setText("SLOT FULL");
//
//				tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//				tvSecondSlotFull.setTextColor(getResources().getColor(R.color.white));
//				tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//				tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//				rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlThirdTimeSlot.setEnabled(false);
//				rlFourthTimeSlot.setEnabled(false);
//
//			} else if (alTime.size() == 1) {
//				tvFirstTime.setText(alTime.get(0));
//
//				tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//				tvSecondTime.setTextColor(getResources().getColor(R.color.primaryColor));
//				tvThirdTime.setTextColor(getResources().getColor(R.color.primaryColor));
//				tvFourthTime.setTextColor(getResources().getColor(R.color.primaryColor));
//
//				tvFirstSlotFull.setText("");
//				tvSecondSlotFull.setText("SLOT FULL");
//				tvThirdSlorFull.setText("SLOT FULL");
//				tvFourthSlotFull.setText("SLOT FULL");
//
//				tvFirstSlotFull.setTextColor(getResources().getColor(R.color.white));
//				tvSecondSlotFull.setTextColor(getResources().getColor(R.color.pallete_grey));
//				tvThirdSlorFull.setTextColor(getResources().getColor(R.color.pallete_grey));
//				tvFourthSlotFull.setTextColor(getResources().getColor(R.color.pallete_grey));
//
//				rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//				rlSecondTimeSlot.setEnabled(false);
//				rlThirdTimeSlot.setEnabled(false);
//				rlFourthTimeSlot.setEnabled(false);
//
//			}
//		}catch(Exception e){
//			new GrocermaxBaseException("ChooseAddress","setTimeSlotting",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
//	}


//	private void setTimeSlotting(String date)
//	{
//		try {
//			ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//			if (alTime.size() == 4) {
//				tvFirstTime.setText(alTime.get(0));
//				tvSecondTime.setText(alTime.get(1));
//				tvThirdTime.setText(alTime.get(2));
//				tvFourthTime.setText(alTime.get(3));
//
//				tvFirstTime.setVisibility(View.VISIBLE);
//				tvSecondTime.setVisibility(View.VISIBLE);
//				tvThirdTime.setVisibility(View.VISIBLE);
//				tvFourthTime.setVisibility(View.VISIBLE);
//
//				btn1TimeSlot.setVisibility(View.VISIBLE);
//				btn2TimeSlot.setVisibility(View.VISIBLE);
//				btn3TimeSlot.setVisibility(View.VISIBLE);
//				btn4TimeSlot.setVisibility(View.VISIBLE);
//
//				rl1.setVisibility(View.VISIBLE);
//				rl2.setVisibility(View.VISIBLE);
//				rl3.setVisibility(View.VISIBLE);
//				rl4.setVisibility(View.VISIBLE);
//			} else if (alTime.size() == 3) {
//				tvFirstTime.setText(alTime.get(0));
//				tvSecondTime.setText(alTime.get(1));
//				tvThirdTime.setText(alTime.get(2));
//
//				tvFirstTime.setVisibility(View.VISIBLE);
//				tvSecondTime.setVisibility(View.VISIBLE);
//				tvThirdTime.setVisibility(View.VISIBLE);
//				tvFourthTime.setVisibility(View.GONE);
//
//				btn1TimeSlot.setVisibility(View.VISIBLE);
//				btn2TimeSlot.setVisibility(View.VISIBLE);
//				btn3TimeSlot.setVisibility(View.VISIBLE);
//				btn4TimeSlot.setVisibility(View.GONE);
//
//				rl1.setVisibility(View.VISIBLE);
//				rl2.setVisibility(View.VISIBLE);
//				rl3.setVisibility(View.VISIBLE);
//				rl4.setVisibility(View.GONE);
//			} else if (alTime.size() == 2) {
//				tvFirstTime.setText(alTime.get(0));
//				tvSecondTime.setText(alTime.get(1));
//
//				tvFirstTime.setVisibility(View.VISIBLE);
//				tvSecondTime.setVisibility(View.VISIBLE);
//				tvThirdTime.setVisibility(View.GONE);
//				tvFourthTime.setVisibility(View.GONE);
//
//				btn1TimeSlot.setVisibility(View.VISIBLE);
//				btn2TimeSlot.setVisibility(View.VISIBLE);
//				btn3TimeSlot.setVisibility(View.GONE);
//				btn4TimeSlot.setVisibility(View.GONE);
//
//				rl1.setVisibility(View.VISIBLE);
//				rl2.setVisibility(View.VISIBLE);
//				rl3.setVisibility(View.GONE);
//				rl4.setVisibility(View.GONE);
//			} else if (alTime.size() == 1) {
//				tvFirstTime.setText(alTime.get(0));
//
//				tvFirstTime.setVisibility(View.VISIBLE);
//				tvSecondTime.setVisibility(View.GONE);
//				tvThirdTime.setVisibility(View.GONE);
//				tvFourthTime.setVisibility(View.GONE);
//
//				btn1TimeSlot.setVisibility(View.VISIBLE);
//				btn2TimeSlot.setVisibility(View.GONE);
//				btn3TimeSlot.setVisibility(View.GONE);
//				btn4TimeSlot.setVisibility(View.GONE);
//
//				rl1.setVisibility(View.VISIBLE);
//				rl2.setVisibility(View.GONE);
//				rl3.setVisibility(View.GONE);
//				rl4.setVisibility(View.GONE);
//			}
//		}catch(Exception e){
//			new GrocermaxBaseException("ChooseAddress","setTimeSlotting",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
//	}

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
        initHeader(findViewById(R.id.app_bar_header), true, "Select Billing Address");
        LinearLayout llIcon = (LinearLayout)findViewById(R.id.ll_placeholder_logoIcon_appBar);
        llIcon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 7f));
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

//	private void firstTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(0);
//		tvSelectedTime.setText(time);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//		tvFirstSlotFull.setText("");
//		tvSecondSlotFull.setText("");
//		tvThirdSlorFull.setText("");
//		tvFourthSlotFull.setText("");
//
//		tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//		rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
//		rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//	}

//	private void secondTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(1);
//		tvSelectedTime.setText(time);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//		tvFirstSlotFull.setText("");
//		tvSecondSlotFull.setText("");
//		tvThirdSlorFull.setText("");
//		tvFourthSlotFull.setText("");
//
//		tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//		rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
//		rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//	}

//	private void thirdTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(2);
//		tvSelectedTime.setText(time);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//		tvFirstSlotFull.setText("");
//		tvSecondSlotFull.setText("");
//		tvThirdSlorFull.setText("");
//		tvFourthSlotFull.setText("");
//
//		tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//		rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
//		rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//	}

//	private void fourthTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(3);
//		tvSelectedTime.setText(time);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.light_Gray));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.light_Gray));
//
//		tvFirstSlotFull.setText("");
//		tvSecondSlotFull.setText("");
//		tvThirdSlorFull.setText("");
//		tvFourthSlotFull.setText("");
//
//		tvFirstSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvSecondSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvThirdSlorFull.setTextColor(getResources().getColor(R.color.primaryColor));
//		tvFourthSlotFull.setTextColor(getResources().getColor(R.color.primaryColor));
//
//		rlFirstTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlSecondTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlThirdTimeSlot.setBackgroundColor(getResources().getColor(R.color.white));
//		rlFourthTimeSlot.setBackgroundColor(getResources().getColor(R.color.gray_1));
//
//	}


//	private void firstTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(0);
//
//		btn1TimeSlot.setBackgroundResource(R.drawable.check_pay);
//		btn2TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn3TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn4TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//
//		tvFirstTime.setBackgroundResource(R.drawable.delivery_slot_selected_btn);
//		tvSecondTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvThirdTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvFourthTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_selected_color));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//	}

//	private void secondTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(1);
//
//		btn1TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn2TimeSlot.setBackgroundResource(R.drawable.check_pay);
//		btn3TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn4TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//
//		tvFirstTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvSecondTime.setBackgroundResource(R.drawable.delivery_slot_selected_btn);
//		tvThirdTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvFourthTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_selected_color));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//	}

//	private void thirdTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(2);
//
//		btn1TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn2TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn3TimeSlot.setBackgroundResource(R.drawable.check_pay);
//		btn4TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//
//		tvFirstTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvSecondTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvThirdTime.setBackgroundResource(R.drawable.delivery_slot_selected_btn);
//		tvFourthTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_selected_color));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//	}

//	private void fourthTimeSlot(){
//		ArrayList<String> alTime = address_obj.getDate_timeSlot().get(date);
//		time = alTime.get(3);
//
//		btn1TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn2TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn3TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//		btn4TimeSlot.setBackgroundResource(R.drawable.check_pay);
//
//		tvFirstTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvSecondTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvThirdTime.setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//		tvFourthTime.setBackgroundResource(R.drawable.delivery_slot_selected_btn);
//
//		tvFirstTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvSecondTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvThirdTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//		tvFourthTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_selected_color));
//	}


//	OnClickListener listener = new OnClickListener() {
//		@Override
//		public void onClick(View view) {
//			try {
//				int pos = (Integer) view.getTag();
////			UtilityMethods.customToast(String.valueOf(pos)+"====", MyApplication.getInstance());
//
//				for (int i = 0; i < date_list.size(); i++) {
//					System.out.println("====text====" + i);
////				imgDateSlot[i].setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//					imgDateSlot[i].setImageResource(R.drawable.uncheck_pay);
//
//					tvDateSlot[i].setBackgroundResource(R.drawable.delivery_slot_date_unselected_btn);
//					tvDateSlot[i].setBackgroundColor(getResources().getColor(R.color.delivery_slot_unselected_color));
//					tvDateSlot[i].setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//				}
//
//				imgDateSlot[pos].setImageResource(R.drawable.check_pay);
//				tvDateSlot[pos].setTextColor(getResources().getColor(R.color.delivery_slot_text_selected_color));
//				tvDateSlot[pos].setBackgroundResource(R.drawable.delivery_slot_selected_btn);
////			((LinearLayout)view).setBackgroundColor(getResources().getColor(R.color.red));
//				date = date_list.get(pos);
//				time = "";
//				setTimeSlotting(date);
//				GridViewAdapter.selectedPosition = pos;
//
//				btn1TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//				btn2TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//				btn3TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//				btn4TimeSlot.setBackgroundResource(R.drawable.uncheck_pay);
//
//				tvFirstTime.setBackgroundResource(R.color.delivery_slot_unselected_color);
//				tvSecondTime.setBackgroundResource(R.color.delivery_slot_unselected_color);
//				tvThirdTime.setBackgroundResource(R.color.delivery_slot_unselected_color);
//				tvFourthTime.setBackgroundResource(R.color.delivery_slot_unselected_color);
//
//				tvFirstTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//				tvSecondTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//				tvThirdTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
//				tvFourthTime.setTextColor(getResources().getColor(R.color.delivery_slot_text_unselected_color));
////			//scroll_view.scrollTo(0, 0);
//			}catch(Exception e){
//				new GrocermaxBaseException("ChooseAddress","listener",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//			}
//		}
//	};


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            EasyTracker.getInstance(this).activityStart(this);
//            tracker.activityStart(this);
            FlurryAgent.onStartSession(this, getResources().getString(R.string.flurry_api_key));
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

}


