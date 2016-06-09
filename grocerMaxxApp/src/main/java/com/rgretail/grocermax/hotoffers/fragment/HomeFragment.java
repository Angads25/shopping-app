package com.rgretail.grocermax.hotoffers.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.HomeBannerBean;
import com.rgretail.grocermax.bean.ShopByCategoryBean;
import com.rgretail.grocermax.bean.ShopByCategoryModel;
import com.rgretail.grocermax.bean.ShopByDealsBean;
import com.rgretail.grocermax.hotoffers.AutoScrollViewPager;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.hotoffers.adapter.ShopByCategoryListAdapter;
import com.rgretail.grocermax.hotoffers.adapter.ShopBySpecialDealsListAdapter1;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/14/2015.
 */
public class HomeFragment extends Fragment {
    RecyclerView recyclerView1;
    GridView catg_grid;
    TextView txtCategory, txtDeal;
    ImageView img_deal;
    ArrayList<String> arrayList = new ArrayList<>();

    //private ViewPager mPager;
    private AutoScrollViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private HomeBannerBean homeBannerBean;
    private ShopByDealsBean shopBySpecialDealsBean;
    private ProgressDialog progress;
    private int pos;
    ImageView iv[];

    /*int count = 0;
    Timer timer;*/


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
        final ShopByDealsBean shopByDealsBean = (ShopByDealsBean) bundle.get(Constants.SHOP_BY_DEALS_MODEL);
        homeBannerBean = (HomeBannerBean) bundle.get(Constants.HOME_BANNER);
        shopBySpecialDealsBean = (ShopByDealsBean) bundle.get(Constants.SHOP_BY_SPECIAL_DEALS_MODEL);

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

        //mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager = (AutoScrollViewPager) view.findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);


       /* // Timer for auto sliding
        timer  = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count <= homeBannerBean.getBanner().size()) {
                            mPager.setCurrentItem(count,true);
                            count++;
                        } else {
                            count = 0;
                            mPager.setCurrentItem(count,true);
                        }
                    }
                });
            }
        }, 500, 3000);*/



        mPager.setInterval(4000);
        mPager.startAutoScroll();
        mPager.setAutoScrollDurationFactor(15);
        mPager.setSlideBorderMode(AutoScrollViewPager.LEFT);


        //BANNER LISTENER//
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //try{UtilityMethods.clickCapture(getActivity(), "", "", "", "", HomeScreen.SCREENNAME+homeBannerBean.getBanner().get(position).getName()+"-"+AppConstants.BANNER_SCROLLING);}catch(Exception e){}
            }

            @Override
            public void onPageSelected(int position) {

                pos = position;
                for (int i = 0; i < homeBannerBean.getBanner().size(); i++) {
                    if (i == position) {
                        iv[i].setImageResource(R.drawable.banner_carausal_selected);
                    } else {
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
                try {
                    UtilityMethods.clickCapture(getActivity(), "Banner Scroll", "", "", "", MySharedPrefs.INSTANCE.getSelectedCity());
                } catch (Exception e) {
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

        img_deal=(ImageView)view.findViewById(R.id.img_deal);

        txtCategory = (TextView) view.findViewById(R.id.txt_category);
        txtDeal = (TextView) view.findViewById(R.id.txt_deal);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Reg.ttf");
        txtCategory.setTypeface(type);
        txtDeal.setTypeface(type);

        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
        catg_grid=(GridView)view.findViewById(R.id.catg_grid);

        ArrayList<ShopByCategoryModel> alfinal = new ArrayList<ShopByCategoryModel>();
        ArrayList<ShopByCategoryModel> al = shopByCategoryBean.getArrayList();

        try {
            if (((HomeScreen) getActivity()).catObj!=null) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        ShopByCategoryListAdapter shopByCategoryListAdapter1 = new ShopByCategoryListAdapter(getActivity(), this);
//        shopByCategoryListAdapter1.setListData(shopByCategoryBean.getArrayList());
        shopByCategoryListAdapter1.setListData(alfinal);

       // recyclerView1.setAdapter(shopByCategoryListAdapter1);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(llm);
        catg_grid.setAdapter(shopByCategoryListAdapter1);
        setGridViewHeightBasedOnChildren(catg_grid, 3);

        ListView lv=(ListView)view.findViewById(R.id.lv);
        ShopBySpecialDealsListAdapter1 shopBySpecialDealsListAdapter1 = new ShopBySpecialDealsListAdapter1(getActivity(), this);
        shopBySpecialDealsListAdapter1.setListData(shopBySpecialDealsBean.getSpecial_deal_type());
        lv.setAdapter(shopBySpecialDealsListAdapter1);
        UtilityMethods.setListViewHeightBasedOnChildren(lv);



        /*for deal image on home page */
        if(shopByDealsBean.getArrayList().size()>0){
        ImageLoader.getInstance().displayImage(shopByDealsBean.getArrayList().get(0).getImg(),img_deal, ((BaseActivity) getActivity()).baseImageoptions);
            ((TextView)view.findViewById(R.id.tv_deal_name)).setText(shopByDealsBean.getArrayList().get(0).getDealType());
            ((TextView)view.findViewById(R.id.tv_deal_name)).setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"Gotham-Book.ttf"));
        }
        img_deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.strTitleHotDeal = "";
                AppConstants.strTitleHotDeal = shopByDealsBean.getArrayList().get(0).getDealType();
                ((HomeScreen) getActivity()).hitForShopByDeals(shopByDealsBean.getArrayList().get(0).getId());

                /*  tracking GA event for click on Shop By Deals from Home screen */
                try{
                    MyApplication.isFromDrawer=false;
                    UtilityMethods.clickCapture(getActivity(), "Deal Category L1", "", shopByDealsBean.getArrayList().get(0).getDealType(),"", MySharedPrefs.INSTANCE.getSelectedCity());
                    UtilityMethods.sendGTMEvent(getActivity(),"deal page",shopByDealsBean.getArrayList().get(0).getDealType(),"Android Category Interaction");
                    RocqAnalytics.trackEvent("Deal Category L1", new ActionProperties("Category", "Deal Category L1", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", shopByDealsBean.getArrayList().get(0).getDealType()));
                }catch(Exception e){}
                /*-----------------------------------------------------*/
            }
        });


        progress.dismiss();
        return view;
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();
        // Display display = getActivity().getWindowManager().getDefaultDisplay();
        //totalHeight = display.getWidth()/3;
        //width=width/3;

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

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
            System.out.println("Position " + position + " ImageUrl " + homeBannerBean.getBanner().get(position).getImageurl());
            System.out.println("====NAME====" + homeBannerBean.getBanner().get(position).getName());
            bannerFragment.setArguments(bundle);

            String image_url=homeBannerBean.getBanner().get(position).getImageurl();
            String image_name=image_url.substring(image_url.lastIndexOf("/")+1);
            if(!UtilityMethods.isFilePresent(image_name,getActivity())){
              new GetImage().execute(image_url,image_name);
            }
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


    @Override
    public void onPause() {
        super.onPause();
        try {
            if(mPager!=null)
            mPager.stopAutoScroll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if(mPager!=null)
            mPager.startAutoScroll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*downloading file for banner on home screen*/
    class GetImage extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            Bitmap image = null;
            try {
                url = new URL(params[0]);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                UtilityMethods.saveImageInInternalMemory(getActivity(),params[1],image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
    }



}
