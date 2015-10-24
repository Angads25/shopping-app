package com.sakshay.grocermax.hotoffers.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.HomeBannerBean;
import com.sakshay.grocermax.bean.ShopByCategoryBean;
import com.sakshay.grocermax.bean.ShopByCategoryModel;
import com.sakshay.grocermax.bean.ShopByDealsBean;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.hotoffers.adapter.ShopByCategoryListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.ShopByDealsListAdapter;
import com.sakshay.grocermax.utils.Constants;

import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/14/2015.
 */
public class HomeFragment extends Fragment {
    RecyclerView recyclerView1, recyclerView2;
    TextView txtCategory, txtDeal;
    ArrayList<String> arrayList = new ArrayList<>();

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private HomeBannerBean homeBannerBean;
    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        progress = new ProgressDialog(getActivity());
        progress.setTitle(null);
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

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

        txtCategory = (TextView) view.findViewById(R.id.txt_category);
        txtDeal = (TextView) view.findViewById(R.id.txt_deal);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Reg.ttf");
        txtCategory.setTypeface(type);
        txtDeal.setTypeface(type);

        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);

        ArrayList<ShopByCategoryModel> alfinal = new ArrayList<ShopByCategoryModel>();
        ArrayList<ShopByCategoryModel> al = shopByCategoryBean.getArrayList();
        for(int i=0;i<((HotOffersActivity) getActivity()).catObj.size();i++){
            for(int j=0;j<al.size();j++) {
                if (((HotOffersActivity) getActivity()).catObj.get(i).getCategoryId().equalsIgnoreCase(al.get(j).getCategory_id())) {
                        alfinal.add(al.get(j));
                }
            }
        }

        ShopByCategoryListAdapter shopByCategoryListAdapter1 = new ShopByCategoryListAdapter(getActivity(), this);
//        shopByCategoryListAdapter1.setListData(shopByCategoryBean.getArrayList());
        shopByCategoryListAdapter1.setListData(alfinal);

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
        progress.dismiss();
        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            BannerFragment bannerFragment = new BannerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("imgUrl", homeBannerBean.getBanner().get(position).getImageurl());
            bundle.putString("linkUrl", homeBannerBean.getBanner().get(position).getLinkurl());
            bundle.putString("name", homeBannerBean.getBanner().get(position).getName());
//            bannerFragment.setData(homeBannerBean.getBanner().get(position).getImageurl());
            System.out.println("Position " + position + " ImageUrl " + homeBannerBean.getBanner().get(position).getImageurl());
//            bannerFragment.setLinkUrl(homeBannerBean.getBanner().get(position).getLinkurl());
//            bannerFragment.setName(homeBannerBean.getBanner().get(position).getName());

            System.out.println("====NAME====" + homeBannerBean.getBanner().get(position).getName());

            bannerFragment.setArguments(bundle);
//            return BannerFragment.newInstance(homeBannerBean.getBanner().get(position % homeBannerBean.getBanner().size()));
            return bannerFragment;
        }

        @Override
        public int getCount() {
            return homeBannerBean.getBanner().size();
        }


    }

}
