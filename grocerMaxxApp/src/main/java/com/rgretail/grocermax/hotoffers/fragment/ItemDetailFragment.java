package com.rgretail.grocermax.hotoffers.fragment;

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
import com.rgretail.grocermax.bean.OfferByDealTypeBean;
import com.rgretail.grocermax.bean.OfferByDealTypeModel;
import com.rgretail.grocermax.bean.OfferByDealTypeSubModel;
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
public class ItemDetailFragment extends Fragment {
    ArrayList<String> arrayList = new ArrayList<>();
    private static final int NUM_PAGES = 3;
    private String viewId;
    ViewPager viewPager;
    private List<String> keyList = new ArrayList<>();
    private ScreenSlidePagerAdapter mPagerAdapter;
    private MyPagerSlidingTabStrip tabs;
    private OfferByDealTypeBean offerByDealTypeBean = new OfferByDealTypeBean();
    private DealByDealTypeBean dealByDealTypeBean = new DealByDealTypeBean();
    private boolean is_shop_by_deal = false;
    private String key;
    Worker worker;
    ArrayList<OfferByDealTypeModel> allData;

    HashMap<String,ArrayList<OfferByDealTypeSubModel>> dealcatListing;
//    private ItemDetailGrid itemDetailGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle data = getArguments();
        //((HomeScreen) getActivity()).isFromFragment = true;
        HomeScreen.isFromFragment = true;
        HomeScreen.bFromHome = false;
//        itemDetailGrid = new ItemDetailGrid();



        ((BaseActivity) getActivity()).initHeader(getActivity().findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
        ((BaseActivity) getActivity()).findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        ((BaseActivity) getActivity()).findViewById(R.id.header).setVisibility(View.GONE);

        try {
            is_shop_by_deal = data.getBoolean(Constants.SHOP_BY_DEAL);
            if (is_shop_by_deal) {
            } else {
                offerByDealTypeBean = (OfferByDealTypeBean) data.getSerializable(Constants.OFFER_BY_DEAL);

                if(offerByDealTypeBean.getDealcategorylisting().getAll().size()>0)
                {
                    dealcatListing = new HashMap<>();
                    //////// responsible for All tab in starting //////////
//                    keyList.add(" All ");
//                    dealcatListing.put(" All ",offerByDealTypeBean.getDealcategorylisting().getAll());
                    //////// responsible for All tab in starting //////////
                }

                if(offerByDealTypeBean.getDealcategorylisting().getDealsCategory().size()>0)
                {
                    for (OfferByDealTypeModel model : offerByDealTypeBean.getDealcategorylisting().getDealsCategory())
                    {
                        keyList.add(model.getDealType());
                        dealcatListing.put(model.getDealType(),model.getDeals());
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
////        if(offerByDealTypeBean.getDealcategorylisting().size()>0)
////            itemDetailGrid.setData(offerByDealTypeBean.getDealcategorylisting().get(keyList.get(0)));
//
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
               // System.out.println("Item Name="+AppConstants.strTitleHotDeal+" - "+keyList.get(i));

                sendDataToGA(AppConstants.strTitleHotDeal+" - "+keyList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabs.setViewPager(viewPager);

        try {
            sendDataToGA(AppConstants.strTitleHotDeal+" - "+keyList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    public void sendDataToGA(String lavel){
        try{
            UtilityMethods.clickCapture(getActivity(), "Category Deals", "", lavel, "", MySharedPrefs.INSTANCE.getSelectedCity());
           // UtilityMethods.sendGTMEvent(getActivity(),"deal page",lavel,"Android Category Interaction");
        /*QGraph event*/
           /* JSONObject json=new JSONObject();
            json.put("Deal label",lavel);
            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                json.put("User Id",MySharedPrefs.INSTANCE.getUserId());
            UtilityMethods.setQGraphevent("Andriod Category Interaction - Deal Page",json);*/
                   /*--------------*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
            ItemDetailGrid fragment = new ItemDetailGrid();
            fragment.setData(dealcatListing.get(keyList.get(position)));
            return fragment;
        }

        @Override
        public int getCount() {
            if(dealcatListing==null)
                return 0;
            return dealcatListing.size();
        }

    }


}
