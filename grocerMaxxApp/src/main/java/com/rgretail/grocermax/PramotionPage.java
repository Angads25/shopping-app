package com.rgretail.grocermax;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by anchit-pc on 13-Jun-16.
 */
public class PramotionPage extends BaseActivity{
    ImageView icon_header_back;
    ListView lv;
    ArrayList<PramotionPageBean> pramotionList;

    //TextView tv_pramotion_title,tv_pramotion_desc,tv_pramotion_code,tv_tap_to_copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pramotion_page);
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
        lv=(ListView)findViewById(R.id.lv_pramotion);

        /*tv_pramotion_title=(TextView)findViewById(R.id.tv_coupon_title);
        tv_pramotion_desc=(TextView)findViewById(R.id.tv_coupon_desc);
        tv_pramotion_code=(TextView)findViewById(R.id.tv_coupon_code);
        tv_tap_to_copy=(TextView)findViewById(R.id.tv_tap_to_copy);*/
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
                    UtilityMethods.customToast("No Pramotion available",PramotionPage.this);
                }

            }catch(Exception e)
            {
                new GrocermaxBaseException("PramotionPageBean","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in pramotion");
            }
        }
    }


    public class PramotionAdapter extends BaseAdapter{

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
                convertView=inflater.inflate(R.layout.coupon_listitem,null);
            }

            TextView tv_pramotion_title=(TextView)convertView.findViewById(R.id.tv_coupon_title);
            TextView tv_pramotion_desc=(TextView)convertView.findViewById(R.id.tv_coupon_desc);
            TextView tv_pramotion_code=(TextView)convertView.findViewById(R.id.tv_coupon_code);
            TextView tv_tap_to_copy=(TextView)convertView.findViewById(R.id.tv_tap_to_copy);
            final LinearLayout ll_code=(LinearLayout)convertView.findViewById(R.id.ll_code);
            final ImageView img=(ImageView)convertView.findViewById(R.id.imageView2);

            tv_pramotion_title.setText(pramotionList.get(position).getName());
            tv_pramotion_title.setTypeface(CustomFonts.getInstance().getRobotoRegular(PramotionPage.this));
            tv_pramotion_desc.setText(pramotionList.get(position).getDesc());
            tv_pramotion_desc.setTypeface(CustomFonts.getInstance().getRobotoRegular(PramotionPage.this));
            tv_pramotion_code.setText(Html.fromHtml("Code: <b>"+pramotionList.get(position).getCoupon_code()+"</b>"));
            tv_pramotion_code.setTypeface(CustomFonts.getInstance().getRobotoRegular(PramotionPage.this));
            if(!pramotionList.get(position).getCoupon_code().equals(""))
            ll_code.setVisibility(View.VISIBLE);
            else
            ll_code.setVisibility(View.GONE);

            tv_tap_to_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(pramotionList.get(position).getCoupon_code());
                    Toast.makeText(PramotionPage.this, "Coupon code copied", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }


}
