package com.rgretail.grocermax.hotoffers.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.HomeBannerBean;
import com.rgretail.grocermax.bean.ShopByCategoryBean;
import com.rgretail.grocermax.bean.ShopByCategoryModel;
import com.rgretail.grocermax.bean.ShopByDealsBean;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.hotoffers.adapter.ShopByCategoryListAdapter;
import com.rgretail.grocermax.hotoffers.adapter.ShopByDealsListAdapter;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;

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
    private int pos;
    ImageView iv[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeScreen.bFromHome = true;
        HomeScreen.isFromFragment = false;

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

        ((BaseActivity) getActivity()).initHeader(getActivity().findViewById(R.id.header), true, null);
        ((BaseActivity) getActivity()).findViewById(R.id.header).setVisibility(View.VISIBLE);
        ((BaseActivity) getActivity()).findViewById(R.id.header_left).setVisibility(View.GONE);
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);





        //BANNER LISTENER//
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //try{UtilityMethods.clickCapture(getActivity(), "", "", "", "", HomeScreen.SCREENNAME+homeBannerBean.getBanner().get(position).getName()+"-"+AppConstants.BANNER_SCROLLING);}catch(Exception e){}
            }

            @Override
            public void onPageSelected(int position) {

                pos = position;
                for(int i=0;i<homeBannerBean.getBanner().size();i++){
                    if(i == position){
                        iv[i].setImageResource(R.drawable.banner_carausal_selected);
                    }else{
                        iv[i].setImageResource(R.drawable.banner_carausal_unselected);
                    }


//                    if(i <= position) {
//                        iv[i].setImageResource(R.drawable.progress_green);
//                    }else{
//                        iv[i].setImageResource(R.drawable.progress_grey);
//                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                try{
                    UtilityMethods.clickCapture(getActivity(),"Banner Scroll","","","", MySharedPrefs.INSTANCE.getSelectedCity());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_dots);
        iv = new ImageView[homeBannerBean.getBanner().size()];
        for(int i=0;i<homeBannerBean.getBanner().size();i++){
            View view1 = inflater.inflate(R.layout.banner_single_image, null);
            iv[i] = (ImageView) view1.findViewById(R.id.banner_indicator);
            if(i==0) {
                iv[i].setImageResource(R.drawable.banner_carausal_selected);
            }else {
                iv[i].setImageResource(R.drawable.banner_carausal_unselected);
            }
            ll.addView(view1);
        }

        txtCategory = (TextView) view.findViewById(R.id.txt_category);
        txtDeal = (TextView) view.findViewById(R.id.txt_deal);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Reg.ttf");
        txtCategory.setTypeface(type);
        txtDeal.setTypeface(type);

        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);

        ArrayList<ShopByCategoryModel> alfinal = new ArrayList<ShopByCategoryModel>();
        ArrayList<ShopByCategoryModel> al = shopByCategoryBean.getArrayList();
        for (int i = 0; i < ((HomeScreen) getActivity()).catObj.size(); i++) {
            boolean b_id_found = false;
            for (int j = 0; j < al.size(); j++) {
                if (((HomeScreen) getActivity()).catObj.get(i).getCategoryId().equalsIgnoreCase(al.get(j).getCategory_id())) {
                    alfinal.add(al.get(j));
                    b_id_found = true;
                }
            }
            if(!b_id_found){
                ShopByCategoryModel shopByCategoryModel = new ShopByCategoryModel();
                    String strurlImage = Constants.base_url_category_image+((HomeScreen) getActivity()).catObj.get(i).getCategoryId()+".png";
                    shopByCategoryModel.setCategory_id(((HomeScreen) getActivity()).catObj.get(i).getCategoryId());
                    shopByCategoryModel.setName(((HomeScreen) getActivity()).catObj.get(i).getCategory());
                    shopByCategoryModel.setImages(strurlImage);
                    shopByCategoryModel.setIs_active(((HomeScreen) getActivity()).catObj.get(i).getIsActive());
                    shopByCategoryModel.setOffercount("0");
                alfinal.add(shopByCategoryModel);
            }
        }

//            for (int i = 0; i < al.size(); i++) {
//                boolean b_id_found = false;
//                for (int j = 0; j < ((HomeScreen) getActivity()).catObj.size(); j++) {
//                    if(al.get(i).getCategory_id().equalsIgnoreCase(((HomeScreen) getActivity()).catObj.get(j).getCategoryId())) {
//                        alfinal.add(al.get(i));
//                        b_id_found = true;
//                    }
//                }
//                if(!b_id_found){
//                    ShopByCategoryModel shopByCategoryModel = new ShopByCategoryModel();
////                    shopByCategoryModel.setCategory_id();
////                    shopByCategoryModel.setImages();
////                    shopByCategoryModel.setCategory_id();
////                    shopByCategoryModel.setName();
//                    shopByCategoryModel.setOffercount("0");
//                    alfinal.add(shopByCategoryModel);
//                }
//            }


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


    // responsible for Home screen Banner  //
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            BannerFragment bannerFragment = BannerFragment.newInstance(HomeFragment.this);
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

    public int getPosition() {
//        return pos;
        return mPager.getCurrentItem();
    }

    public HomeBannerBean getHomeBannerBean(){
        return homeBannerBean;
    }


    public HomeFragment() {
        super();
    }

}
