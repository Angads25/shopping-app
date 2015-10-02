package com.sakshay.grocermax.hotoffers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.api.SearchLoader;
import com.sakshay.grocermax.bean.HomeBannerBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.utils.UrlsConstants;

import org.json.JSONObject;


/**
 * Created by nawab.hussain on 9/14/2015.
 */
public class BannerFragment extends Fragment {

    private LinearLayout parentLayout;
    private CardView card_view;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.banner, container, false);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
        card_view = (CardView) view.findViewById(R.id.card_view);
        ImageView imageView  = (ImageView)view.findViewById(R.id.image);
        context = getActivity();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                ((HotOffersActivity) context).addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
//                String url = UrlsConstants.PRODUCTLISTING_BY_DEAL_TYPE;
//                ((HotOffersActivity)context).showDialog();
//                ((HotOffersActivity)context).myApi.reqProductListingByDealType(url + dealId);
//                System.out.println(dealId);

//                http://staging.grocermax.com/webservice/new_services/dealproductlisting?deal_id=270&version=1.0

                ////////////////////// 3. dealsbydealtype?deal_type_id=? ////////////////////////

//                String dealId = "1";
//                ((HotOffersActivity) context).addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
//                ((HotOffersActivity)context).showDialog();
//                String url = UrlsConstants.DEAL_BY_DEAL_TYPE;
//                System.out.print("==my work=="+url);
//                ((HotOffersActivity)context).myApi.reqDealByDealType(url+ dealId);

                ////////////////http://staging.grocermax.com/webservice/new_services/dealsbydealtype?deal_type_id=1&version=1.0////////////////////


//                shopbydealtype              -          open Hot Offers screen in gridview format.

//                productlist?pro_id=11498         -       will call Product_Detail (i.e. product description)

//                category?parentid=2402

                System.out.println("====values is===="+linkurl);
                int index = 0;
                String strType="";
                index = linkurl.indexOf("?");
                if(linkurl.length() >= index) {
                    strType = linkurl.substring(0, index);
                    System.out.println("====result is===="+strType);
                }

                if(strType.equalsIgnoreCase("dealproductlisting")){
                    String dealId = "270";
                    ((HotOffersActivity) context).addActionsInFilter(MyReceiverActions.PRODUCT_LISTING_BY_DEALTYPE);
                    String url = UrlsConstants.PRODUCTLISTING_BY_DEAL_TYPE;
                    ((HotOffersActivity)context).showDialog();
                    ((HotOffersActivity)context).myApi.reqProductListingByDealType(url + dealId);
                    System.out.println(dealId);
                }else if(strType.equalsIgnoreCase("dealsbydealtype")){
                    String dealId = "1";
                    ((HotOffersActivity) context).addActionsInFilter(MyReceiverActions.DEAL_BY_DEALTYPE);
                    ((HotOffersActivity)context).showDialog();
                    String url = UrlsConstants.DEAL_BY_DEAL_TYPE;
                    System.out.print("==my work=="+url);
                    ((HotOffersActivity)context).myApi.reqDealByDealType(url+ dealId);
                }else if(strType.equalsIgnoreCase("shopbydealtype")){

                }else if(strType.equalsIgnoreCase("search")){
//                    linkurl = "search?keyword=atta";
                    index = linkurl.indexOf("?");
                    int indexequal = linkurl.indexOf("=");
                    if(linkurl.length() >= index) {
                        String strEqual = linkurl.substring(indexequal, linkurl.length());
                        System.out.println("====indexequals is===="+strEqual);
                    }

                    String strAtta = "atta";
                    System.out.println("====values OF is====" + linkurl);
                    String url = UrlsConstants.BANNER_SEARCH_PRODUCT + linkurl;
                    url = url.replace(" ", "%20");
//                SearchLoader searchLoader  = new SearchLoader(this,search_key);
                    SearchLoader searchLoader  = new SearchLoader(context,strAtta);
                    searchLoader.execute(url);
                    Log.i("Banner Through Search", "URL::" + url);
                }


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


    public static void setData(String imageurl)
    {
        url = imageurl;
    }
    public static void setLinkUrl(String link){
        linkurl = link;
    }
    public static void setName(String strname){
        name = strname;
    }

}
