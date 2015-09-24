package com.sakshay.grocermax.hotoffers;

import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.api.MyApi;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.OfferByDealTypeBean;
import com.sakshay.grocermax.bean.ShopByCategoryBean;
import com.sakshay.grocermax.hotoffers.fragment.HomeFragment;
import com.sakshay.grocermax.hotoffers.fragment.ItemDetailFragment;
import com.sakshay.grocermax.utils.Constants;

import org.json.JSONObject;


public class HotOffersActivity extends BaseActivity {


    public ShopByCategoryBean shopByCategoryBean;
    public OfferByDealTypeBean offerByDealTypeBean;
    private ImageView menuIcon;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot_offer);
        addActionsInFilter(MyReceiverActions.GET_SHOP_BY_CATEGORIES);
        menuIcon = (ImageView) findViewById(R.id.menuIcon);

        myApi.reqGetShopByCategories("http://dev.grocermax.com/webservice/new_services/shopbycategory");

    }


    @Override
    public void OnResponse(Bundle bundle) {
        try {


            String action = bundle.getString("ACTION");
            if (action.equals(MyReceiverActions.GET_SHOP_BY_CATEGORIES)) {

                System.out.println("RESPONSE" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
                shopByCategoryBean = gson.fromJson(jsonObject.toString(), ShopByCategoryBean.class);

                System.out.println("RESPONSE MODEL" + shopByCategoryBean.getResult());
                System.out.println("RESPONSE MODEL" + shopByCategoryBean.getArrayList().size());

                HomeFragment fragment = new HomeFragment();

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                Bundle data = new Bundle();
                data.putSerializable(Constants.SHOP_BY_CATEGORY_MODEL, shopByCategoryBean);
                fragment.setArguments(data);
                fragmentTransaction.commit();
            }else if(action.equals(MyReceiverActions.OFFER_BY_DEALTYPE)){


                System.out.println("RESPONSE OFFER" + bundle.getString("json"));
                JSONObject jsonObject = new JSONObject(bundle.getString("json"));
                Gson gson = new Gson();
                offerByDealTypeBean = gson.fromJson(jsonObject.toString(), OfferByDealTypeBean.class);
                System.out.println("RESPONSE OFFER" + offerByDealTypeBean.getDealcategorylisting().get("all").size());
                ItemDetailFragment fragment = new ItemDetailFragment();
                changeFragment(fragment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void hitForShopByCategory(String categoryId) {
        addActionsInFilter(MyReceiverActions.OFFER_BY_DEALTYPE);
        myApi.reqOfferByDealType("http://dev.grocermax.com/webservice/new_services/offerbydealtype?cat_id=?"+categoryId);
    }

    public void changeFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
//            fragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
        }
        fragmentTransaction.add(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
//        if(fragmentTransaction.getBackStackEntryCount())
        super.onBackPressed();
    }
}
