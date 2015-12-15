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

import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.DealByDealTypeBean;
import com.rgretail.grocermax.bean.OfferByDealTypeSubModel;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.hotoffers.MyPagerSlidingTabStrip;
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
    HashMap<String,ArrayList<OfferByDealTypeSubModel>> dealcategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle data = getArguments();
        ((HomeScreen) getActivity()).isFromFragment = true;
        HomeScreen.bFromHome = false;
//        ((HomeScreen)getActivity()).setHeader("activity start");
//        itemDetailGrid = new ItemDetailGrid();
        try {

            ((BaseActivity) getActivity()).initHeader(getActivity().findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
            ((BaseActivity) getActivity()).findViewById(R.id.header_left).setVisibility(View.VISIBLE);
            ((BaseActivity) getActivity()).findViewById(R.id.header).setVisibility(View.GONE);

            is_shop_by_deal = data.getBoolean(Constants.SHOP_BY_DEAL);
            if (is_shop_by_deal) {
                dealByDealTypeBean = (DealByDealTypeBean) data.getSerializable(Constants.DEAL_BY_DEAL);
                dealcategory = new HashMap<>();

///////////    responsible for All tab in starting   ////////////////////
//                if(dealByDealTypeBean.getDealcategory().getAll().size()>0)
//                {
//                    keyList.add(" All ");
//                    dealcategory.put(" All ",dealByDealTypeBean.getDealcategory().getAll());
//                }
///////////    responsible for All tab in starting   ////////////////////

                try{ UtilityMethods.clickCapture(getActivity(), "", "", "", "", AppConstants.GA_EVENT_DEAL_CATEGORY_OPENED);}catch(Exception e){}

                if(dealByDealTypeBean.getDealcategory().getCategory().size()>0)
                {
                    for (OfferByDealTypeSubModel dataValue : dealByDealTypeBean.getDealcategory().getCategory()) {
                                keyList.add(dataValue.getName());
                                dealcategory.put(dataValue.getName(),dataValue.getDeals());
                            }
                }

//                if (dealByDealTypeBean.getDealcategorylisting().size() > 0) {
//                    for (String key : dealByDealTypeBean.getDealcategorylisting().keySet()) {
//                        if(key.equalsIgnoreCase("all"))
//                        {
//                            keyList.add(key);
//                            dealcategory = new HashMap<>();
//                            dealcategory.put(key,dealByDealTypeBean.getDealcategorylisting().get(key));
//
//                        }else if(key.equalsIgnoreCase("category"))
//                        {
//                        ArrayList<OfferByDealTypeSubModel> catDeals = new ArrayList<>();
//
//                            catDeals = dealByDealTypeBean.getDealcategorylisting().get(key);
//                            for (OfferByDealTypeSubModel dataValue : catDeals) {
//                                keyList.add(dataValue.getName());
//                                dealcategory.put(dataValue.getName(),dataValue.getDeals());
//                            }
//                        }
//                    }
//
//                    //System.out.println("Response" + offerByDealTypeBean.getDealcategorylisting().keySet());
//                }
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

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//            }
//
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
        tabs.setViewPager(viewPager);
        return view;
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

//            ShopByCategoryListAdapter shopByCategoryListAdapter1 = new ShopByCategoryListAdapter(getActivity(), this);
//            shopByCategoryListAdapter1.setListData(shopByCategoryBean.getArrayList());
//            recyclerView1.setAdapter(shopByCategoryListAdapter1);
//            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
//            recyclerView1.setLayoutManager(llm);


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
