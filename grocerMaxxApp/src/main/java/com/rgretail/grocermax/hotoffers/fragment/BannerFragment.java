package com.rgretail.grocermax.hotoffers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    /*-----------------------------------------------*/

                ////////////// 1.search ///////////////////
//                linkurl = "search?keyword=atta";
//                String strAtta = "atta";
//                System.out.println("====values OF is====" + linkurl);
//                String url = UrlsConstants.BANNER_SEARCH_PRODUCT + linkurl;
//                url = url.replace(" ", "%20");
////                SearchLoader searchLoader  = new SearchLoader(this,search_key);
//                SearchLoader searchLoader  = new SearchLoader(context,strAtta);
//                searchLoader.execute(url);
//                Log.i("Banner Through Search", "URL::" + url);
                /////////////// search ///////////////////

                ////////////////////// 2.dealproductlisting?deal_id=? ///////////////////

//                String dealId = "270";
//                ((HomeScreen) context).addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);home
//                String url = UrlsConstants.PRODUCTLISTING_BY_DEAL_TYPE;
//                ((HomeScreen)context).showDialog();
//                ((HomeScreen)context).myApi.reqProductListingByDealType(url + dealId);
//                System.out.println(dealId);

//                http://staging.grocermax.com/webservice/new_services/dealproductlisting?deal_id=270&version=1.0

                ////////////////////// 3. dealsbydealtype?deal_type_id=? ////////////////////////

//                String dealId = "1";
//                ((HomeScreen) context).addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
//                ((HomeScreen)context).showDialog();
//                String url = UrlsConstants.DEAL_BY_DEAL_TYPE;
//                System.out.print("==my work=="+url);
//                ((HomeScreen)context).myApi.reqDealByDealType(url+ dealId);

                ////////////////http://staging.grocermax.com/webservice/new_services/dealsbydealtype?deal_type_id=1&version=1.0////////////////////


//                shopbydealtype              -          open Hot Offers screen in gridview format.

//                productlist?pro_id=11498         -       will call Product_Detail (i.e. product description)

//                category?parentid=2402

//                System.out.println("====values is====" + linkurl);

//                types of banner in android
//1                "search?keyword=dairy"                             //
//2                "dealproductlisting?deal_id=11"                    //
//3                "dealsbydealtype?deal_type_id=1"                   //
//4                "shopbydealtype"                                   //not implemented in android
//5                "productlistall?cat_id=2402"                      //
//6                linkurl = "offerbydealtype?cat_id=2483";



                    int index = 0;
                    String strType = "";
                    if (linkurl.contains("?")) {
                        index = linkurl.indexOf("?");
                        if (linkurl.length() >= index) {
                            strType = linkurl.substring(0, index);
//                            System.out.println("====result is====" + strType);
                        }
                    } else {
                        strType = linkurl;
                    }

//                if (strType.equalsIgnoreCase("dealproductlisting")) {

                    AppConstants.strTitleHotDeal = name;
                    if (strType.equalsIgnoreCase("dealproductlisting")) {
//                    String dealId = "270";
//                    String dealId = linkurl.substring(index+1,linkurl.length()-1);
//                    System.out.println("===========dealproductlisting=============dealid===================="+dealId);
                        ((HomeScreen) context).addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
//                    String PRODUCTLISTING_BY_DEAL_TYPE = UrlsConstants.NEW_BASE_URL+"dealproductlisting?deal_id=";
//                    String url = UrlsConstants.PRODUCTLISTING_BY_DEAL_TYPE;


//                    AppConstants.strTitleHotDeal = "Offer Detail";
                        DealListScreen.strDealHeading = "Offer Detail";

                        String url = UrlsConstants.NEW_BASE_URL;
                        ((HomeScreen) context).showDialog();
                        ((HomeScreen) context).myApi.reqProductListingByDealType(url + linkurl);

                       // try{UtilityMethods.clickCapture(context,"","","","",HomeScreen.SCREENNAME+name+"-"+AppConstants.GA_EVENT_DEALS_PRODUCT_LISTING_THROUGH_HOME_BANNER);}catch(Exception e){}

//                    System.out.println(dealId);
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


                    } else if (strType.equalsIgnoreCase("search")) {
//                    linkurl = "search?keyword=atta";
                        String strSearch = "";
                        index = linkurl.indexOf("?");
                        int indexequal = linkurl.indexOf("=");
                        if (linkurl.length() >= index) {
                            strSearch = linkurl.substring(indexequal + 1, linkurl.length());
//                            System.out.println("====indexequals is====>>" + strSearch);
                        }

//                    String strAtta = "atta";
                        System.out.println("====values OF is====" + linkurl);
                        String url = UrlsConstants.BANNER_SEARCH_PRODUCT + linkurl;
                        url = url.replace(" ", "%20");
//                SearchLoader searchLoader  = new SearchLoader(this,search_key);
                        SearchLoader searchLoader = new SearchLoader(context, strSearch);
                        searchLoader.execute(url);
                        Log.i("Banner Through Search", "URL::" + url);
                        //try{UtilityMethods.clickCapture(context,"","","","",HomeScreen.SCREENNAME+name+"-"+AppConstants.GA_EVENT_SEARCH_LISTING_THROUGH_HOME_BANNER);}catch(Exception e){}
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
                    }
                }catch(Exception e){}



//                String str = linkurl.split("?")[0];
//                String[] parts = linkurl.split("/?");
//                if(parts.length>=1) {
//                    for(int i=0;i<parts.length;i++){
//                        if(parts[i] == "?"){
//                            index = i;
//                        }
//                        System.out.println("====values OF is====" + parts[i]);
//                    }
//                    String strdd = linkurl.substring(0, index);
//                    strdd = strdd.trim();
//                    System.out.println("====trim part is====" + strdd);
//                }
//                String strA = linkurl.split("?")[1];
            }
        });

        ImageLoader.getInstance().displayImage(url,
                imageView, ((BaseActivity) getActivity()).baseImageoptions);
        return view;
    }


//    public static void setData(String imageurl)
//    {
//        url = imageurl;
//    }
//    public static void setLinkUrl(String link){
//        linkurl = link;
//    }
//    public static void setName(String strname){
//        name = strname;
//    }

}
