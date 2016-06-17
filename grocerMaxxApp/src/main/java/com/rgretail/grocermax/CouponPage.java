package com.rgretail.grocermax;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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
    ArrayList<PramotionPageBean> pramotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_page);
        addActionsInFilter(MyReceiverActions.PRAMOTION_DATA);
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
                    PramotionPageBean p1=new PramotionPageBean();
                    pramotionList.add(p1);
                    JSONArray couponArray=pramotionJSON.getJSONArray("coupon");
                    for(int i=0;i<couponArray.length();i++){
                        JSONObject couponObject=couponArray.getJSONObject(i);
                        PramotionPageBean p=new PramotionPageBean();
                        p.setName(couponObject.getString("name").equals("null")?"GMAX":couponObject.getString("name"));
                        p.setDesc(couponObject.getString("description").equals("null")?"":couponObject.getString("description"));
                        p.setCoupon_code(couponObject.getString("coupon_code").equals("null")?"":couponObject.getString("coupon_code"));
                        p.setPramotion_order(couponObject.getString("promo_order").equals("null")?"":couponObject.getString("promo_order"));
                        p.setIs_active(couponObject.getString("is_active").equals("null")?"0":couponObject.getString("is_active"));
                        pramotionList.add(p);
                    }

                    lv.setAdapter(new PramotionAdapter());

                }else{
                    UtilityMethods.customToast("No Coupon available",CouponPage.this);
                }

            }catch(Exception e)
            {
                new GrocermaxBaseException("PramotionPageBean","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in pramotion");
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
                EditText edt_coupon=(EditText)convertView.findViewById(R.id.editText);
                TextView apply=(TextView)convertView.findViewById(R.id.tv_apply);
                /*edt_coupon.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.requestFocus();
                        return false;
                    }
                });*/
                edt_coupon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(final View v, boolean hasFocus) {
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                v.requestFocus();
                                v.requestFocusFromTouch();
                            }
                        });
                    }
                });
            }else{
                ll_apply_coupon.setVisibility(View.GONE);
                ll_list.setVisibility(View.VISIBLE);
                TextView tv_pramotion_title=(TextView)convertView.findViewById(R.id.tv_coupon_title);
                TextView tv_pramotion_desc=(TextView)convertView.findViewById(R.id.tv_coupon_detail);
                TextView tv_pramotion_code=(TextView)convertView.findViewById(R.id.tv_apply_coupon);
                final LinearLayout ll_desc=(LinearLayout)convertView.findViewById(R.id.ll_desc);
                final ImageView img_view_m=(ImageView)convertView.findViewById(R.id.img_view_m);
                final ImageView img_hide_m=(ImageView)convertView.findViewById(R.id.img_hide_m);

                tv_pramotion_title.setText(pramotionList.get(position).getName());
                tv_pramotion_title.setTypeface(CustomFonts.getInstance().getRobotoRegular(CouponPage.this));
                tv_pramotion_desc.setText(pramotionList.get(position).getDesc());
                tv_pramotion_desc.setTypeface(CustomFonts.getInstance().getRobotoRegular(CouponPage.this));
                tv_pramotion_code.setText(Html.fromHtml(pramotionList.get(position).getCoupon_code()+" - <b>APPLY</b>"));
                tv_pramotion_code.setTypeface(CustomFonts.getInstance().getRobotoRegular(CouponPage.this));
                if(!pramotionList.get(position).getDesc().equals(""))
                    img_view_m.setVisibility(View.VISIBLE);
                else
                    img_view_m.setVisibility(View.GONE);

                img_view_m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ll_desc.setVisibility(View.VISIBLE);
                        img_view_m.setVisibility(View.GONE);
                        img_hide_m.setVisibility(View.VISIBLE);
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



}
