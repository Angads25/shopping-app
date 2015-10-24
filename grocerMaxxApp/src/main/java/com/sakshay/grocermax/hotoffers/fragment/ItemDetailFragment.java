package com.sakshay.grocermax.hotoffers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.DealByDealTypeBean;
import com.sakshay.grocermax.bean.OfferByDealTypeBean;
import com.sakshay.grocermax.bean.OfferByDealTypeModel;
import com.sakshay.grocermax.bean.OfferByDealTypeSubModel;
import com.sakshay.grocermax.hotoffers.MyPagerSlidingTabStrip;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.Worker;

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
//        itemDetailGrid = new ItemDetailGrid();
        try {
            is_shop_by_deal = data.getBoolean(Constants.SHOP_BY_DEAL);
            if (is_shop_by_deal) {
            } else {
                offerByDealTypeBean = (OfferByDealTypeBean) data.getSerializable(Constants.OFFER_BY_DEAL);
                if(offerByDealTypeBean.getDealcategorylisting().getAll().size()>0)
                {
                    dealcatListing = new HashMap<>();
                    keyList.add(" All ");
                    dealcatListing.put(" All ",offerByDealTypeBean.getDealcategorylisting().getAll());
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

        View view = inflater.inflate(R.layout.itemdetailfragment, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabs = (MyPagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
////        if(offerByDealTypeBean.getDealcategorylisting().size()>0)
////            itemDetailGrid.setData(offerByDealTypeBean.getDealcategorylisting().get(keyList.get(0)));
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
        tabs.setViewPager(viewPager);
        return view;
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
            return dealcatListing.size();
        }

    }


}
