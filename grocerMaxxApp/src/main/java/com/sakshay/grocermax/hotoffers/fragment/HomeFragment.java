package com.sakshay.grocermax.hotoffers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.HomeBannerBean;
import com.sakshay.grocermax.bean.ShopByCategoryBean;
import com.sakshay.grocermax.bean.ShopByDealsBean;
import com.sakshay.grocermax.hotoffers.adapter.ShopByCategoryListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.ShopByDealsListAdapter;
import com.sakshay.grocermax.utils.Constants;

import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/14/2015.
 */
public class HomeFragment extends Fragment {
    ViewPager scrollView;
    RecyclerView recyclerView1, recyclerView2;
    ArrayList<String> arrayList = new ArrayList<>();
    private static final int NUM_PAGES = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private  HomeBannerBean homeBannerBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        ShopByCategoryBean shopByCategoryBean = (ShopByCategoryBean) bundle.get(Constants.SHOP_BY_CATEGORY_MODEL);
        ShopByDealsBean shopByDealsBean = (ShopByDealsBean) bundle.get(Constants.SHOP_BY_DEALS_MODEL);
        homeBannerBean = (HomeBannerBean) bundle.get(Constants.HOME_BANNER);

        try {
            System.out.println("RESPONSE HOME" + homeBannerBean.getBanner().size());
            System.out.println("RESPONSE HOME" + shopByCategoryBean.getArrayList().size());
            System.out.println("RESPONSE HOME" + shopByDealsBean.getArrayList().size());
        } catch (Exception e) {

        }
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);

        ShopByCategoryListAdapter shopByCategoryListAdapter1 = new ShopByCategoryListAdapter(getActivity(), this);
        shopByCategoryListAdapter1.setListData(shopByCategoryBean.getArrayList());
        recyclerView1.setAdapter(shopByCategoryListAdapter1);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(llm);

        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView2);
        ShopByDealsListAdapter shopByDealsListAdapter = new ShopByDealsListAdapter(getActivity(), this);
        shopByDealsListAdapter.setListData(shopByDealsBean.getArrayList());
        recyclerView2.setAdapter(shopByDealsListAdapter);

        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(llm1);

        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BannerFragment bannerFragment = new BannerFragment();
            bannerFragment.setData(homeBannerBean.getBanner().get(position).getImageurl());
            return bannerFragment;
        }

        @Override
        public int getCount() {
            return homeBannerBean.getBanner().size();
        }
    }

}
