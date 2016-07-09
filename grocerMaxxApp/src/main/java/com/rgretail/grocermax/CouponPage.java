package com.rgretail.grocermax;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.PramotionPageBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 16-Jun-16.
 */
public class CouponPage extends BaseActivity{

    ImageView icon_header_back;
    ListView lv;
    TextView tv_coupon_header;
    ArrayList<PramotionPageBean> pramotionList;
    String edit_text_value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_page);
        addActionsInFilter(MyReceiverActions.PRAMOTION_DATA);
        addActionsInFilter(MyReceiverActions.APPLY_REMOVE_COUPON);
        initViews();
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            showDialog();
            myApi.reqPramotion(UrlsConstants.PRAMOTION_URL);
        } catch (Exception e) {
            new GrocermaxBaseException("Pramotion page","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting pramotion list");
        }
    }
    public void initViews(){
        pramotionList=new ArrayList<>();
        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
        tv_coupon_header=(TextView)findViewById(R.id.tv_coupon_header);
        if(MySharedPrefs.INSTANCE.getCouponCode().equals("")){
            tv_coupon_header.setText("Apply Coupon");
            tv_coupon_header.setVisibility(View.GONE);
        }
        else{
            tv_coupon_header.setText(Html.fromHtml("Applied Coupon - <b>"+MySharedPrefs.INSTANCE.getCouponCode()+"</b>"));
            tv_coupon_header.setVisibility(View.VISIBLE);
        }
        lv=(ListView)findViewById(R.id.lv_coupon_list);
        lv.setItemsCanFocus(true);
    }

    @Override
    public void OnResponse(Bundle bundle) {
        dismissDialog();
        if (bundle.getString("ACTION").equals(MyReceiverActions.PRAMOTION_DATA)) {
            try
            {
                String pramotionResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
                JSONObject pramotionJSON=new JSONObject(pramotionResponse);
                if(pramotionJSON.getString("flag").equals("1")){
                    pramotionList.clear();
                    boolean isSet=false;
                    JSONArray couponArray=pramotionJSON.getJSONArray("coupon");
                    for(int i=0;i<couponArray.length();i++){
                        JSONObject couponObject=couponArray.getJSONObject(i);
                        PramotionPageBean p=new PramotionPageBean();
                        p.setName(couponObject.getString("name").equals("null")?"GMAX":couponObject.getString("name"));
                        p.setDesc(couponObject.getString("description").equals("null")?"":couponObject.getString("description"));
                        p.setValidDate(couponObject.getString("validDate").equals("null")?"":couponObject.getString("validDate"));
                        p.setCoupon_code(couponObject.getString("coupon_code").equals("null")?"":couponObject.getString("coupon_code"));
                        p.setPramotion_order(couponObject.getString("promo_order").equals("null")?"":couponObject.getString("promo_order"));
                        p.setIs_active(couponObject.getString("is_active").equals("null")?"0":couponObject.getString("is_active"));
                        if(MySharedPrefs.INSTANCE.getCouponCode().equals(couponObject.getString("coupon_code"))){
                            p.setIs_applied("true");
                            isSet=true;
                            pramotionList.add(0,p);
                        }
                        else{
                            p.setIs_applied("false");
                            pramotionList.add(p);
                        }
                    }

                    PramotionPageBean p1=new PramotionPageBean();
                    if(!MySharedPrefs.INSTANCE.getCouponCode().equals("") && !isSet)
                        p1.setIs_applied("true");
                    else
                        p1.setIs_applied("false");
                    pramotionList.add(0,p1);

                    lv.setAdapter(new PramotionAdapter());

                }else{
                    UtilityMethods.customToast("No Coupon available",CouponPage.this);
                }

            }catch(Exception e)
            {
                new GrocermaxBaseException("PramotionPageBean","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in pramotion");
            }
        }else if (bundle.getString("ACTION").equals(MyReceiverActions.APPLY_REMOVE_COUPON)) {
            try
            {
                String couponResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
                JSONObject couponApplyRemoveJSON=new JSONObject(couponResponse);
                if(couponApplyRemoveJSON.getInt("flag")==1){
                    UtilityMethods.customToast(couponApplyRemoveJSON.getString("Result"),CouponPage.this);
                    MySharedPrefs.INSTANCE.putCouponAmount(couponApplyRemoveJSON.getJSONObject("CartDetails").getString("you_save"));
                    viewCart();
                }else{
                    UtilityMethods.customToast(couponApplyRemoveJSON.getString("Result"),CouponPage.this);
                }
            }catch(Exception e)
            {
                new GrocermaxBaseException("Coupon Page","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in coupon page");
            }
        }
    }

    public class PramotionAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return pramotionList==null?0:pramotionList.size();
        }

        @Override
        public Object getItem(int position) {
            return pramotionList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.apply_coupon_listrow,null);
            }
            LinearLayout ll_list=(LinearLayout)convertView.findViewById(R.id.ll_list);
            LinearLayout ll_apply_coupon=(LinearLayout)convertView.findViewById(R.id.ll_apply_coupon);
            if(position==0){
             ll_apply_coupon.setVisibility(View.VISIBLE);
             ll_list.setVisibility(View.GONE);
                final EditText edt_coupon=(EditText)convertView.findViewById(R.id.editText);
                final TextView apply=(TextView)convertView.findViewById(R.id.tv_apply);

                edt_coupon.setText(edit_text_value);
                System.out.println("edit_text_value = " + edit_text_value);
                edt_coupon.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,int before, int count) {
                        edit_text_value = s.toString();
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,int count, int after) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                if(pramotionList.get(position).getIs_applied().equals("true")){
                     edt_coupon.setText(MySharedPrefs.INSTANCE.getCouponCode());
                     edt_coupon.setEnabled(false);
                     apply.setText("CANCEL");
                     apply.setBackground(getResources().getDrawable(R.drawable.cancel_coupon));
                }else{
                    edt_coupon.setText(edit_text_value);
                    edt_coupon.setSelection(edit_text_value.length());
                    edt_coupon.setEnabled(true);
                    apply.setText("APPLY");
                    apply.setBackground(getResources().getDrawable(R.drawable.apply_coupon));
                }

                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edt_coupon.getText().toString().length()>0)
                        applyCoupon(edt_coupon.getText().toString(),apply);
                        else
                            UtilityMethods.customToast("Please enter coupon code",CouponPage.this);
                    }
                });

            }else{
                ll_apply_coupon.setVisibility(View.GONE);
                ll_list.setVisibility(View.VISIBLE);
                TextView tv_pramotion_title=(TextView)convertView.findViewById(R.id.tv_coupon_title);
                TextView tv_pramotion_desc=(TextView)convertView.findViewById(R.id.tv_coupon_detail);
                final TextView tv_coupon_validity = (TextView)convertView.findViewById(R.id.tv_coupon_validity);
                final TextView tv_pramotion_code=(TextView)convertView.findViewById(R.id.tv_apply_coupon);
                final LinearLayout ll_desc=(LinearLayout)convertView.findViewById(R.id.ll_desc);
                final ImageView img_view_m=(ImageView)convertView.findViewById(R.id.img_view_m);
                final ImageView img_hide_m=(ImageView)convertView.findViewById(R.id.img_hide_m);

                tv_pramotion_title.setText(pramotionList.get(position).getName());
                tv_pramotion_title.setTypeface(CustomFonts.getInstance().getRobotoRegular(CouponPage.this));
                tv_pramotion_desc.setText(pramotionList.get(position).getDesc());
                tv_pramotion_desc.setTypeface(CustomFonts.getInstance().getRobotoRegular(CouponPage.this));
                if(pramotionList.get(position).getIs_applied().equals("true")){
                    tv_pramotion_code.setText(Html.fromHtml(pramotionList.get(position).getCoupon_code()+" - <b>CANCEL</b>"));
                    tv_pramotion_code.setBackground(getResources().getDrawable(R.drawable.cancel_coupon));
                }else{
                    tv_pramotion_code.setText(Html.fromHtml(pramotionList.get(position).getCoupon_code()+" - <b>APPLY</b>"));
                    tv_pramotion_code.setBackground(getResources().getDrawable(R.drawable.apply_coupon));
                }
                tv_pramotion_code.setTypeface(CustomFonts.getInstance().getRobotoRegular(CouponPage.this));
                tv_pramotion_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        applyCoupon(pramotionList.get(position).getCoupon_code(),tv_pramotion_code);
                    }
                });


                if(!pramotionList.get(position).getDesc().equals("")){
                    img_view_m.setVisibility(View.VISIBLE);
                    img_hide_m.setVisibility(View.GONE);
                }
                else{
                    img_view_m.setVisibility(View.GONE);
                    img_hide_m.setVisibility(View.GONE);
                }

                img_view_m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ll_desc.setVisibility(View.VISIBLE);
                        img_view_m.setVisibility(View.GONE);
                        img_hide_m.setVisibility(View.VISIBLE);

                        if(pramotionList.get(position).getValidDate().equals(""))
                            tv_coupon_validity.setVisibility(View.GONE);
                        else
                            tv_coupon_validity.setVisibility(View.VISIBLE);
                    }
                });
                img_hide_m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ll_desc.setVisibility(View.GONE);
                        img_view_m.setVisibility(View.VISIBLE);
                        img_hide_m.setVisibility(View.GONE);
                    }
                });
            }

            return convertView;
        }
    }


   public void applyCoupon(String coupon_code,TextView tv){
       String url;
       if(tv.getText().toString().contains("CANCEL")){
           url = UrlsConstants.REMOVE_COUPON + MySharedPrefs.INSTANCE.getUserId() + "&quote_id="
                   + MySharedPrefs.INSTANCE.getQuoteId() + "&couponcode="+coupon_code;
       }
       else{
           url = UrlsConstants.ADD_COUPON + MySharedPrefs.INSTANCE.getUserId() + "&quote_id="
                   + MySharedPrefs.INSTANCE.getQuoteId() + "&couponcode="+coupon_code;
       }
       showDialog();
       myApi.reqApplyRemoveCoupon(url,MyReceiverActions.APPLY_REMOVE_COUPON);
   }


}
