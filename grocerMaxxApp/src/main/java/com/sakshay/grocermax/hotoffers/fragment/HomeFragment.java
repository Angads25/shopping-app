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
import com.sakshay.grocermax.bean.ShopByCategoryBean;
import com.sakshay.grocermax.hotoffers.adapter.ShopByCategoryListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.ShopByDealsListAdapter;
import com.sakshay.grocermax.utils.Constants;

import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/14/2015.
 */
public class HomeFragment extends Fragment {
    ViewPager scrollView;
    RecyclerView recyclerView1,recyclerView2;
    ArrayList<String> arrayList = new ArrayList<>();
    private static final int NUM_PAGES = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        addArrayData();
        Bundle bundle = this.getArguments();
        ShopByCategoryBean shopByCategoryBean = (ShopByCategoryBean) bundle.get(Constants.SHOP_BY_CATEGORY_MODEL);
        System.out.println("RESPONSE HOME"+shopByCategoryBean.getArrayList().size());
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        recyclerView1 = (RecyclerView)view.findViewById(R.id.recyclerView1);

        ShopByCategoryListAdapter shopByCategoryListAdapter1 = new ShopByCategoryListAdapter(getActivity(), this);
        shopByCategoryListAdapter1.setListData(shopByCategoryBean.getArrayList());
        recyclerView1.setAdapter(shopByCategoryListAdapter1);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(llm);

        recyclerView2 = (RecyclerView)view.findViewById(R.id.recyclerView2);
        ShopByDealsListAdapter shopByDealsListAdapter = new ShopByDealsListAdapter(getActivity(), this);
        shopByDealsListAdapter.setListData(arrayList);
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
            return new BannerFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void addArrayData() {

        arrayList.add("Brightness");
        arrayList.add("Contrast");
        arrayList.add("Autofix");
        arrayList.add("Black & White");
        arrayList.add("Flip Vertical");
        arrayList.add("Flip Horizontal");
        arrayList.add("Fish Eye");
        arrayList.add("Documentary");
        arrayList.add("Tint");
        arrayList.add("Vignette");
        arrayList.add("Sharpen");
        arrayList.add("Sepia");
        arrayList.add("Temperature");
        arrayList.add("Saturate");
        arrayList.add("Rotate");
        arrayList.add("Posterize");
        arrayList.add("Negative");
        arrayList.add("Lomoish");
        arrayList.add("Grayscale");
        arrayList.add("Grain");
        arrayList.add("Negative");
        arrayList.add("Fill Light");
        arrayList.add("Duotone");
        arrayList.add("Crossprocess");
    }
}
