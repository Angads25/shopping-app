package com.rgretail.grocermax.hotoffers.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.DealByDealTypeBean;
import com.rgretail.grocermax.bean.OfferByDealTypeSubModel;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.hotoffers.MyPagerSlidingTabStrip;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;
import com.rgretail.grocermax.utils.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nawab.hussain on 9/15/2015.
 */
public class ShopByDealItemDetailFragment extends Fragment {
    ArrayList<String> arrayList = new ArrayList<>();
    private static final int NUM_PAGES = 3;
    private String viewId;
    ViewPager viewPager;
    private List<String> keyList = new ArrayList<>();
    private ScreenSlidePagerAdapter mPagerAdapter;
    private MyPagerSlidingTabStrip tabs;
    // private OfferByDealTypeBean offerByDealTypeBean = new OfferByDealTypeBean();
    private DealByDealTypeBean dealByDealTypeBean = new DealByDealTypeBean();
    private boolean is_shop_by_deal = false;
    private String key;
    Worker worker;
    ArrayList<OfferByDealTypeSubModel> allData ;
    //HashMap<String,ArrayList<OfferByDealTypeSubModel>> dealcategory;
    HashMap<String,ArrayList<Product>> dealcategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle data = getArguments();
        //((HomeScreen) getActivity()).isFromFragment = true;
        if(getActivity() instanceof HomeScreen){
            ((HomeScreen) getActivity()).isFromFragment = true;
            HomeScreen.bFromHome = false;
        }
        else{
           HomeScreen.isFromFragment = false;
            HomeScreen.bFromHome = true;
        }


        try {

            ((BaseActivity) getActivity()).initHeader(getActivity().findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
            ((BaseActivity) getActivity()).findViewById(R.id.header_left).setVisibility(View.VISIBLE);
            ((BaseActivity) getActivity()).findViewById(R.id.header).setVisibility(View.GONE);

            is_shop_by_deal = data.getBoolean(Constants.SHOP_BY_DEAL);
            System.out.println("is_shop_by_deal = " + is_shop_by_deal);
            if (is_shop_by_deal) {
                dealByDealTypeBean = (DealByDealTypeBean) data.getSerializable(Constants.DEAL_BY_DEAL);
                dealcategory = new HashMap<>();

                if(dealByDealTypeBean.getDealcategory().getCategory().size()>0)
                {
                    for (OfferByDealTypeSubModel dataValue : dealByDealTypeBean.getDealcategory().getCategory()) {
                        if (dataValue.getName()!=null) {
                            keyList.add(dataValue.getName());
                            dealcategory.put(dataValue.getName(),dataValue.getDeals());
                        } else {
                            keyList.add("Deal");
                            dealcategory.put("Deal",dataValue.getDeals());
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().findViewById(R.id.header).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.itemdetailfragment, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabs = (MyPagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
//        if(offerByDealTypeBean.getDealcategorylisting().size()>0)
//            itemDetailGrid.setData(offerByDealTypeBean.getDealcategorylisting().get(keyList.get(0)));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
            //   System.out.println("print name="+AppConstants.strTitleHotDeal+"-"+keyList.get(i));
                senDataToGA(AppConstants.strTitleHotDeal+"-"+keyList.get(i));
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabs.setViewPager(viewPager);


        /*for landing on specific tab*/
        try {
            if(MySharedPrefs.INSTANCE.getTabIndex()!=null){

                viewPager.setCurrentItem(Integer.parseInt(MySharedPrefs.INSTANCE.getTabIndex()));
                MySharedPrefs.INSTANCE.putTabIndex("0");
            }
            else
                viewPager.setCurrentItem(0);
        } catch (Exception e) {
            viewPager.setCurrentItem(0);
        }


        if(keyList.size()>0)
        senDataToGA(AppConstants.strTitleHotDeal + "-" + keyList.get(0));
        return view;
    }

    public void senDataToGA(String label){
        try{
            if(MyApplication.isFromDrawer==true){
            UtilityMethods.clickCapture(getActivity(), "Drawer - Deal Category L2", "", label, "", MySharedPrefs.INSTANCE.getSelectedCity());
                UtilityMethods.sendGTMEvent(getActivity(),"Deal Page",label,"Android Deal Interaction");
            RocqAnalytics.trackEvent("Drawer - Deal Category L2", new ActionProperties("Category", "Drawer - Deal Category L2", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label",label));
            /*QGraph event*/
                /*JSONObject json=new JSONObject();
                json.put("Deal label",label);
                if(MySharedPrefs.INSTANCE.getUserId()!=null)
                    json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                UtilityMethods.setQGraphevent("Andriod Category Interaction - Deal Page",json);*/
                   /*--------------*/

            }
            else{
            UtilityMethods.clickCapture(getActivity(), "Deal Category L2", "", label, "", MySharedPrefs.INSTANCE.getSelectedCity());
               // UtilityMethods.sendGTMEvent(getActivity(),"deal page",label,"Android Category Interaction");
            RocqAnalytics.trackEvent("Deal Category L2", new ActionProperties("Category", "Deal Category L2", "Action", MySharedPrefs.INSTANCE.getSelectedCity(),"Label",label));
            /*QGraph event*/
                /*JSONObject json=new JSONObject();
                json.put("Deal label",label);
                if(MySharedPrefs.INSTANCE.getUserId()!=null)
                    json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
                UtilityMethods.setQGraphevent("Andriod Category Interaction - Deal Page",json);*/
                   /*--------------*/
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void setViewId(String id) {
        viewId = id;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return keyList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            ShopByDealItemDetailGrid fragment = new ShopByDealItemDetailGrid();
            fragment.setData(dealcategory.get(keyList.get(position)));
            return fragment;
        }

        @Override
        public int getCount() {
            return dealcategory.size();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        try {
            getActivity().findViewById(R.id.header).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        }catch(Exception e){}
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().findViewById(R.id.header).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.header).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        super.onStart();
    }
}
