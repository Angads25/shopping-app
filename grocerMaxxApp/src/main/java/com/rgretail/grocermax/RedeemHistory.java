package com.rgretail.grocermax;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.MaxCoin;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 29-Jun-16.
 */
public class RedeemHistory extends BaseActivity {

    TextView tv_coins;
    private ListView listview_RedeemTransaction;
    ImageView icon_header_back;
    ArrayList<com.rgretail.grocermax.bean.RedeemHistory> historiesList_used;
    ArrayList<com.rgretail.grocermax.bean.RedeemHistory> historiesList_un_used;
    ArrayList<MaxCoin> maxCoinList;
    MaxCoinFragment maxCoinFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_transaction);

        initView();
        addActionsInFilter(MyReceiverActions.REWARD_POINT);
        icon_header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            showDialog();
            /*myApi.reqRedeemPoint(UrlsConstants.REDEEM_POINT + MySharedPrefs.INSTANCE.getUserId());*/
            myApi.reqRedeemPoint(UrlsConstants.REDEEM_POINT + "3519");
        } catch (Exception e) {
            new GrocermaxBaseException("WalletActivity","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION, "error in getting wallet amount");
        }



    }

    public void initView(){
        tv_coins=(TextView)findViewById(R.id.tv_cooins);
        listview_RedeemTransaction=(ListView) findViewById(R.id.listview_redeemTran);
        icon_header_back=(ImageView)findViewById(R.id.icon_header_back);
    }

    @Override
    public void OnResponse(Bundle bundle) {
        if (bundle.getString("ACTION").equals(MyReceiverActions.REWARD_POINT)) {
            try
            {
                String rewardResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
                JSONObject reOrderJSON=new JSONObject(rewardResponse);

                if(reOrderJSON.getInt("flag")==1){
                    String point=reOrderJSON.getString("totalPoint");
                    tv_coins.setText(point);
                    JSONArray redeemArray=reOrderJSON.getJSONArray("redeemLog");
                    historiesList_used=new ArrayList<>();
                    historiesList_un_used=new ArrayList<>();
                    maxCoinList=new ArrayList<>();
                    for(int i=0;i<redeemArray.length();i++){
                        com.rgretail.grocermax.bean.RedeemHistory history=new com.rgretail.grocermax.bean.RedeemHistory();
                        history.setId(redeemArray.getJSONObject(i).getString("id"));
                        history.setCreated_date(redeemArray.getJSONObject(i).getString("created_date"));
                        history.setRedeem_point(redeemArray.getJSONObject(i).getString("redeem_point"));
                        history.setDesc(redeemArray.getJSONObject(i).getString("comment"));
                        history.setExp_date(redeemArray.getJSONObject(i).getString("coupon_exp_date"));
                        history.setUsed_coupon(redeemArray.getJSONObject(i).getString("coupon_code"));
                        history.setType_action(redeemArray.getJSONObject(i).getString("type_action"));
                        if(Integer.parseInt(redeemArray.getJSONObject(i).getString("type_action"))==8)
                            historiesList_used.add(history);
                        else
                            historiesList_un_used.add(history);
                    }

                    MaxCoin maxCoin_used=new MaxCoin("Used",historiesList_used);
                    MaxCoin maxCoin_Unused=new MaxCoin("Unused",historiesList_un_used);
                    maxCoinList.add(maxCoin_used);
                    maxCoinList.add(maxCoin_Unused);

                    FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
                   /* pager.setAdapter(adapter);
                    pager.setOffscreenPageLimit(0);
                    if (pager != null)
                        indicator.setViewPager(pager);*/
                    ViewPager pager = (ViewPager) findViewById(R.id.pager);
                    pager.setAdapter(adapter);
                    pager.setOffscreenPageLimit(0);

                    final TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
                    if (pager != null)
                        indicator.setViewPager(pager);



                   // listview_RedeemTransaction.setAdapter(new RedeemListAdapter());
                }else{
                    tv_coins.setText("0");
                }
            }catch(Exception e)
            {
                new GrocermaxBaseException("WalletActivity","OnResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"error in redeem point");
            }
        }
    }


    class GoogleMusicAdapter extends FragmentPagerAdapter {

        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                maxCoinFragment=MaxCoinFragment.newInstance(maxCoinList.get(position % maxCoinList.size()).getCouponList());
                return maxCoinFragment;
                //return ProductListFragments.newInstance(alCategory.get(position % alCategory.size()));
            } catch (Exception e) {
                new GrocermaxBaseException("Maxcoin", "GoogleMusicAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
            }
            return new Fragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
//            	return catObj.get(position % catObj.size()).getCategory().toUpperCase();
//				String str = alCategory.get(position % alCategory.size()).getCategory_name().toUpperCase();;
//				System.out.println("==Cat value is=="+str);

                return maxCoinList.get(position % maxCoinList.size()).getTitle().toUpperCase();
            } catch (Exception e) {
                new GrocermaxBaseException("CategoryTabs", "GoogleMusicAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
            }
            return "";
        }

        @Override
        public int getCount() {
            try {
//          		return catObj.size();
                return maxCoinList.size();
            } catch (Exception e) {
                new GrocermaxBaseException("CategoryTabs", "GoogleMusicAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
            }
            return 0;

        }

    }




       /* public class RedeemListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return historiesList.size();
        }

        @Override
        public Object getItem(int position) {
            return historiesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.wallet_transaction_list_item,null);
            }

            TextView tv_date=(TextView)convertView.findViewById(R.id.tv_date_time);
            TextView tv_description=(TextView)convertView.findViewById(R.id.tv_description);
            TextView tv_amount=(TextView)convertView.findViewById(R.id.tv_amount);
            TextView tv_coupon_used=(TextView)convertView.findViewById(R.id.tv_coupon_used);
            TextView tv_valid_date=(TextView)convertView.findViewById(R.id.tv_valid_date);
            LinearLayout ll=(LinearLayout)convertView.findViewById(R.id.ll);

            tv_date.setText(historiesList.get(position).getCreated_date().split(" ")[0]);
            tv_amount.setText(historiesList.get(position).getRedeem_point());
            tv_description.setText(historiesList.get(position).getDesc());
            if(!historiesList.get(position).getExp_date().equals("null") || !historiesList.get(position).getUsed_coupon().equals("null")){
               ll.setVisibility(View.VISIBLE);
                if(!historiesList.get(position).getExp_date().equals("null") && !historiesList.get(position).getExp_date().equals("0000-00-00")){
                    tv_valid_date.setText("Valid Till : "+historiesList.get(position).getExp_date());
                    tv_valid_date.setVisibility(View.VISIBLE);
                }else{
                    tv_valid_date.setText("Valid Till : N/A");
                    tv_valid_date.setVisibility(View.VISIBLE);
                }
                if(!historiesList.get(position).getUsed_coupon().equals("null")){
                    if(historiesList.get(position).getType_action().equals("1"))
                      tv_coupon_used.setText(Html.fromHtml("Coupon Used : <font color=black>"+historiesList.get(position).getUsed_coupon()+"</font>"));
                    else if(historiesList.get(position).getType_action().equals("2"))
                      tv_coupon_used.setText(Html.fromHtml("Coupon Refunded : <font color=black>"+historiesList.get(position).getUsed_coupon()+"</font>"));
                    else if(historiesList.get(position).getType_action().equals("4"))
                        tv_coupon_used.setText(Html.fromHtml("Coupon Canceled : <font color=black>"+historiesList.get(position).getUsed_coupon()+"</font>"));
                    else if(historiesList.get(position).getType_action().equals("8"))
                        tv_coupon_used.setText(Html.fromHtml("Coupon Imported : <font color=black>"+historiesList.get(position).getUsed_coupon()+"</font>"));
                    else
                        tv_coupon_used.setText(Html.fromHtml("Coupon : <font color=black>"+historiesList.get(position).getUsed_coupon()+"</font>"));
                    tv_coupon_used.setVisibility(View.VISIBLE);
                }else{
                    tv_coupon_used.setVisibility(View.GONE);
                }
            }
            else
                ll.setVisibility(View.GONE);




            return convertView;
        }
    }*/
}
