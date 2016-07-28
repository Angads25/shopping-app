package com.rgretail.grocermax.hotoffers.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.DealListScreen;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.api.SearchLoader;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONObject;


/**
 * Created by nawab.hussain on 9/14/2015.
 */
public class BannerFragment extends Fragment {

    private static Fragment frag;
    private LinearLayout parentLayout;
    private static String url = "";
    private static String linkurl = "";
    private static String name = "";
    Context context;


//    public static BannerFragment newInstance(HomeBannerBean homeBannerBean) {
//        try {
////            JSONObject jsonObject = new JSONObject(str);
//            BannerFragment fragment = new BannerFragment();
////	    	fragment.cat_id = categorySubcategoryBean.getCategoryId();
////	    	fragment.valuePairs = valuePairs;
////		    fragment.jsonArray = jsonArray;
////            fragment.jsonObject = jsonObject;
//            return fragment;
//        }catch(Exception e){
//            new GrocermaxBaseException("SearchProductFragments","newInstance",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
//        }
//
//
//        return null;
//    }

    public static BannerFragment newInstance(Fragment fragment) {

        frag = fragment;
        return new BannerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.banner, container, false);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        context = getActivity();

        HomeScreen.bFromHome = true;


        url = getArguments().getString("imgUrl");
//        linkurl = (((HomeFragment)frag).getHomeBannerBean()).getBanner().get(((HomeFragment)frag).getPosition()).getLinkurl();
//        name = (((HomeFragment)frag).getHomeBannerBean()).getBanner().get(((HomeFragment) frag).getPosition()).getName();
        System.out.println("====link values is====" + linkurl);
//        linkurl = getArguments().getString("linkUrl");
//        name = getArguments().getString("name");
        System.out.println(name+"====link values is====" + linkurl);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                linkurl = (((HomeFragment) frag).getHomeBannerBean()).getBanner().get(((HomeFragment) frag).getPosition()).getLinkurl();
                name = (((HomeFragment) frag).getHomeBannerBean()).getBanner().get(((HomeFragment) frag).getPosition()).getName();
                String imageUrl=(((HomeFragment) frag).getHomeBannerBean()).getBanner().get(((HomeFragment) frag).getPosition()).getImageurl();

                   /*tracking GA even when banner image is clicked*/
                    try{
                        UtilityMethods.clickCapture(getActivity(),"Banner Click","",imageUrl,"", MySharedPrefs.INSTANCE.getSelectedCity());
                        UtilityMethods.sendGTMEvent(context,"Home - Flagship",name,"Android Deal Interaction");
                    /*QGraph event*/
                        JSONObject json=new JSONObject();
                        json.put("Banner Name",name);
                        UtilityMethods.setQGraphevent("Andriod Banner Click - Home - Flagship",json);
                   /*--------------*/
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    /*-----------------------------------------------*/
                    /*to get index of tab*/
                    if(linkurl.contains("index")){
                        try {
                            int ind=linkurl.lastIndexOf("&");
                            String[] part=linkurl.split("&");
                            String[] part1=part[part.length-1].split("=");
                            MySharedPrefs.INSTANCE.putTabIndex(part1[1]);
                            linkurl=linkurl.substring(0,ind);
                        } catch (Exception e) {
                            MySharedPrefs.INSTANCE.putTabIndex("0");
                        }
                    }else{
                        MySharedPrefs.INSTANCE.putTabIndex("0");
                    }



                    int index = 0;
                    String strType = "";
                    if (linkurl.contains("?")) {
                        index = linkurl.indexOf("?");
                        if (linkurl.length() >= index) {
                            strType = linkurl.substring(0, index);
                        }
                    } else {
                        strType = linkurl;
                    }

                    AppConstants.strTitleHotDeal = name;
                    if (strType.equalsIgnoreCase("dealproductlisting")) {
                        ((HomeScreen) context).addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
                        DealListScreen.strDealHeading = "Offer Detail";

                        String url = UrlsConstants.NEW_BASE_URL;
                        ((HomeScreen) context).showDialog();
                        ((HomeScreen) context).myApi.reqProductListingByDealType(url + linkurl);

                    } else if (strType.equalsIgnoreCase("dealsbydealtype")) {
//                    String dealId = "1";
                        ((HomeScreen) context).addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
                        ((HomeScreen) context).showDialog();


//                    public final static String DEAL_BY_DEAL_TYPE = NEW_BASE_URL+"dealsbydealtype?deal_type_id=";
//                    String url = UrlsConstants.DEAL_BY_DEAL_TYPE;
                        String url = UrlsConstants.NEW_BASE_URL;
                        ((HomeScreen) context).myApi.reqDealByDealType(url + linkurl);
                        //try{UtilityMethods.clickCapture(context,"","","","",HomeScreen.SCREENNAME+name+"-"+AppConstants.GA_EVENT_DEALS_THROUGH_HOME_BANNER);}catch(Exception e){}
                    } else if (strType.equalsIgnoreCase("productlistall")) {
                        //                    public final static String GET_ALL_PRODUCTS_OF_CATEGORY = NEW_BASE_URL + "productlistall?cat_id=";
                        ((HomeScreen) context).addActionsInFilter(MyReceiverActions.ALL_PRODUCTS_CATEGORY);
                        ((HomeScreen) context).showDialog();
                        String strUrl = UrlsConstants.NEW_BASE_URL;
                        ((HomeScreen) context).myApi.reqAllProductsCategory(strUrl + linkurl);
                        //try{UtilityMethods.clickCapture(context,"",linkurl,"","",HomeScreen.SCREENNAME+name+"-"+AppConstants.GA_EVENT_PRODUCT_LISTING_THROUGH_HOME_BANNER);}catch(Exception e){}
//                        System.out.println("===complete url====" + strUrl + linkurl);
                    } else if (strType.equalsIgnoreCase("shopbydealtype")) {


                    }else if (strType.equalsIgnoreCase("specialdeal")) {
                        try {
                            String url = UrlsConstants.PRODUCTLISTING_BY_SPECIAL_DEAL_TYPE;
                            ((HomeScreen) context).showDialog();
                            ((HomeScreen) context).myApi.reqProductListingByDealType(url + linkurl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (strType.equalsIgnoreCase("search")) {

//                    linkurl = "search?keyword=atta";
                        String strSearch = "";
                        index = linkurl.indexOf("?");
                        int indexequal = linkurl.indexOf("=");
                        if (linkurl.length() >= index) {
                            strSearch = linkurl.substring(indexequal + 1, linkurl.length());
//                            System.out.println("====indexequals is====>>" + strSearch);
                        }

                        String url = UrlsConstants.BANNER_SEARCH_PRODUCT + linkurl;
                        url = url.replace(" ", "%20");
                        SearchLoader searchLoader = new SearchLoader(context, strSearch,"");
                        searchLoader.execute(url);
                    } else if (strType.equalsIgnoreCase("offerbydealtype")) {
//                    http://staging.grocermax.com/api/offerbydealtype?cat_id=2180&version=1.0
                        String strId = "";
                        index = linkurl.indexOf("?");
                        int indexequal = linkurl.indexOf("=");
                        if (linkurl.length() >= index) {
                            strId = linkurl.substring(indexequal + 1, linkurl.length());
//                            System.out.println("====indexequals is====>>" + strId);
                        }
                        ((HomeScreen) context).addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);
                        ((HomeScreen) context).hitForShopByCategory(strId);
                       // try{UtilityMethods.clickCapture(context,"","","","",HomeScreen.SCREENNAME+name+"-"+AppConstants.GA_EVENT_DEALS_OFFER_THROUGH_HOME_BANNER);}catch(Exception e){}
                    }else if(strType.equalsIgnoreCase("productdetail")){
                        AppConstants.strPopupData="";
                        ((HomeScreen) context).showDialog();
                        String url = UrlsConstants.NEW_BASE_URL + linkurl;
                        ((HomeScreen) context).myApi.reqProductDetailFromNotification(url);
                    }else if(strType.equalsIgnoreCase("singlepage")){
                        try {
                            index = linkurl.indexOf("?");
                            String id=linkurl.substring(index+1,linkurl.length());
                            ((HomeScreen) context).showDialog();
                            String url = UrlsConstants.PAGE_BANNER_MSG+"?"+id;
                            ((HomeScreen) context).myApi.reqSinglePageDate(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch(Exception e){}


            }
        });

        String image_name=url.substring(url.lastIndexOf("/")+1);
        if(UtilityMethods.isFilePresent(image_name,getActivity())){
            Bitmap img = UtilityMethods.loadImageFromStorage(getActivity(),image_name);
            imageView.setImageBitmap(img);
        }else{
        ImageLoader.getInstance().displayImage(url,imageView, ((BaseActivity) getActivity()).baseImageoptions);
        }
        return view;
    }

}
