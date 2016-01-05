package com.rgretail.grocermax.hotoffers.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rgretail.grocermax.CategoryTabs;
import com.rgretail.grocermax.LoginActivity;
import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.WalletActivity;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;
import com.rgretail.grocermax.CityActivity;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.bean.ShopByDealsBean;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.hotoffers.adapter.ExpandableMenuListAdapter;
import com.rgretail.grocermax.hotoffers.adapter.MenuListAdapter;
import com.rgretail.grocermax.hotoffers.adapter.ShopByDealsMenuListAdapter;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UrlsConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nawab.hussain on 10/7/2015.
 */
public class MenuFragment extends Fragment {
    private ArrayList<CategorySubcategoryBean> catObj;
    private ArrayList<String> menuArray = new ArrayList<>();
    private ListView lstMenu, lstShopByDealsMenu;
    private MenuListAdapter menuListAdapter;
    private ImageView imgBack;
    private String title;
    private Boolean isListView;
    private TextView txvTitle, txvShopByDeals, txvGetInTouch, txvShopByCategories,txvRateThisApp,txvYourWallet;
    public static TextView txvSelectLocation;
    private TextView txvLocation;
    private ImageView ivLocation;
    private ExpandableListView expandableListView;
    private ArrayList<String> header;
    private HashMap<String, ArrayList<CategorySubcategoryBean>> menuMap;
    private ExpandableMenuListAdapter expandableMenuListAdapter;
    private ShopByDealsMenuListAdapter shopByDealsListADapter;
    private View div1,div2,div3,div4;

    ShopByDealsBean shopByDealsBean;
    private String SCREENNAME = "Drawer-";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        menuListAdapter = new MenuListAdapter(getActivity(), MenuFragment.this);
        shopByDealsListADapter = new ShopByDealsMenuListAdapter(getActivity(), MenuFragment.this);

        if (bundle != null) {
            catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");
             shopByDealsBean = (ShopByDealsBean) bundle.get(Constants.SHOP_BY_DEALS_MODEL);
            title = bundle.getString("Title");
            isListView = bundle.getBoolean("isListView");
        } else {
            String response = UtilityMethods.readCategoryResponse(getActivity(), AppConstants.categoriesFile);
            catObj = UtilityMethods.getCategorySubCategory(response);
        }
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
//        for (int i = 0; i < catObj.size(); i++) {
//            menuArray.add(catObj.get(i).getCategory());
//        };

        div1 = (View)view.findViewById(R.id.div1);
        div2 = (View)view.findViewById(R.id.div2);
        div3 = (View)view.findViewById(R.id.div3);
        div4 = (View)view.findViewById(R.id.div4);
        txvTitle = (TextView) view.findViewById(R.id.txvTitle);
        txvShopByCategories = (TextView) view.findViewById(R.id.txvShopByCategories);
        txvShopByDeals = (TextView) view.findViewById(R.id.txvShopByDeals);
        txvGetInTouch = (TextView) view.findViewById(R.id.txvGetInTouch);
        txvRateThisApp = (TextView) view.findViewById(R.id.txv_rate_app);
        txvSelectLocation = (TextView) view.findViewById(R.id.txvSelectLocation);
        txvYourWallet=(TextView)view.findViewById(R.id.txvYourWallet);
        ivLocation = (ImageView) view.findViewById(R.id.ivLocation);
//        txvLocation = (TextView) view.findViewById(R.id.txvLocation);
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        lstMenu = (ListView) view.findViewById(R.id.lstMenu);
        lstShopByDealsMenu = (ListView) view.findViewById(R.id.lstShopByDealsMenu);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expLstMenu);

//        txvLocation.setText(AppConstants.strSelectedCity);
//        txvSelectLocation.setText(AppConstants.strSelectedCity);
        if(MySharedPrefs.INSTANCE.getSelectedCity() != null) {
            txvSelectLocation.setText(MySharedPrefs.INSTANCE.getSelectedCity());
//            txvLocation.setText(MySharedPrefs.INSTANCE.getSelectedCity());
        }

        if (isListView) {
            expandableListView.setVisibility(View.GONE);
            lstMenu.setVisibility(View.VISIBLE);
            menuListAdapter.setListData(catObj);
            lstMenu.setAdapter(menuListAdapter);

            lstShopByDealsMenu.setVisibility(View.GONE);
            shopByDealsListADapter.setListData(shopByDealsBean.getArrayList());

            lstShopByDealsMenu.setAdapter(shopByDealsListADapter);
            txvShopByDeals.setVisibility(View.VISIBLE);
            txvGetInTouch.setVisibility(View.VISIBLE);
            txvRateThisApp.setVisibility(View.VISIBLE);
            txvSelectLocation.setVisibility(View.VISIBLE);
//            txvLocation.setVisibility(View.VISIBLE);
            ivLocation.setVisibility(View.VISIBLE);
            txvShopByCategories.setVisibility(View.VISIBLE);

        } else {
            setExpandableListdata();
            lstMenu.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            expandableMenuListAdapter = new ExpandableMenuListAdapter(getActivity(), MenuFragment.this, header, menuMap);
            expandableListView.setAdapter(expandableMenuListAdapter);
            txvShopByDeals.setVisibility(View.GONE);
            txvGetInTouch.setVisibility(View.GONE);
            txvRateThisApp.setVisibility(View.GONE);
            txvSelectLocation.setVisibility(View.GONE);
//            txvLocation.setVisibility(View.GONE);
            ivLocation.setVisibility(View.GONE);
            txvShopByCategories.setVisibility(View.GONE);
        }
        setListShopByCategoriesHeight(lstMenu);
        setListShopByCategoriesHeight(lstShopByDealsMenu);

        div1.setVisibility(View.GONE);
        div2.setVisibility(View.VISIBLE);

        txvTitle.setText(title);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    ((HomeScreen) getActivity()).getDrawerLayout().closeDrawers();
                }
            }
        });
        txvShopByDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstShopByDealsMenu.setVisibility(View.VISIBLE);
                lstMenu.setVisibility(View.GONE);
                div3.setVisibility(View.GONE);
                div4.setVisibility(View.VISIBLE);
                //try{UtilityMethods.clickCapture(HomeScreen.mContext,"","","","",SCREENNAME+AppConstants.DRAWER_SHOP_BY_DEALS);}catch(Exception e){}

            }
        });
        txvShopByCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstShopByDealsMenu.setVisibility(View.GONE);
                lstMenu.setVisibility(View.VISIBLE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.VISIBLE);
                //try{UtilityMethods.clickCapture(HomeScreen.mContext,"","","","",SCREENNAME+AppConstants.DRAWER_SHOP_BY_CATEGORY);}catch(Exception e){}
            }
        });
        txvGetInTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, "Check out the new awesome Grocermax! https://grocermax.com");
                startActivity(Intent.createChooser(intent, "How do you want to share ?"));
                ((HomeScreen)getActivity()).isFromFragment=false;
               // try{UtilityMethods.clickCapture(HomeScreen.mContext,"","","","",SCREENNAME+AppConstants.DRAWER_GET_IN_TOUCH_WITH_US);}catch(Exception e){}
            }
        });

        txvRateThisApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityMethods.rateApp(getActivity());
               // try{UtilityMethods.clickCapture(HomeScreen.mContext,"","","","",SCREENNAME+AppConstants.DRAWER_RATE_US);}catch(Exception e){}
            }
        });

        /* click event on your wallet to reach on wallet screen */
        txvYourWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MySharedPrefs.INSTANCE.getLoginStatus()){
                  Intent i=new Intent(getActivity(), WalletActivity.class);
                    startActivity(i);

                }else{
                    //UtilityMethods.customToast("Not login",HomeScreen.mContext);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });



        /*///////////////////////////////////////////*/


        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstants.locationBean != null) {
                    if (AppConstants.locationBean.getFlag().equals("1")) {
                        MyApplication.isFromDrawer=true;
                        Intent call = new Intent(getActivity(), CityActivity.class);
                        Bundle call_bundle = new Bundle();
                        call_bundle.putSerializable("Location", AppConstants.locationBean);
                        call_bundle.putSerializable("FromDrawer", "fromdrawyer");
                        call.putExtras(call_bundle);
                        startActivity(call);
                    }
                }else{
                    ((HomeScreen)getActivity()).showDialog();
                    String url = UrlsConstants.GET_LOCATION;
                    ((HomeScreen)getActivity()).myApi.reqLocation(url);
                }
                //try{UtilityMethods.clickCapture(HomeScreen.mContext,"","","","",SCREENNAME+AppConstants.DRAWER_STORE);}catch(Exception e){}
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra(Intent.EXTRA_TEXT, "Check out the new awesome Grocermax! https://grocermax.com");
//                startActivity(Intent.createChooser(intent,"How do you want to share ?"));
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean expand = false;

                //Toast.makeText(getActivity(), "Size :" + catObj.get(groupPosition).getChildren().size(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < catObj.get(groupPosition).getChildren().size(); i++) {
                    if (catObj.get(groupPosition).getChildren().get(i).getChildren().size() > 0) {
                        //Toast.makeText(getActivity(), "Name : "+ catObj.get(groupPosition).getChildren().get(i).getCategory()+"Size :" + catObj.get(groupPosition).getChildren().get(i).getChildren().size(), Toast.LENGTH_SHORT).show();
                        expand = true;
                       // try{UtilityMethods.clickCapture(getActivity(),"","",catObj.get(groupPosition).getCategoryId(),"",SCREENNAME+catObj.get(groupPosition)+"-"+AppConstants.GA_EVENT_DRAWER_EXPANDABLE);}catch(Exception e){}
                        break;
                    }

                    System.out.println("====catobj parent1111111========"+catObj.get(groupPosition));

                    if (!expand) {
                        //Toast.makeText(getActivity(), " Will not open "+catObj.get(groupPosition).getCategory(), Toast.LENGTH_SHORT).show();
                        expandableListView.collapseGroup(groupPosition);
                        startActivity(catObj.get(groupPosition));
                        //try{UtilityMethods.clickCapture(getActivity(),"","",catObj.get(groupPosition).getCategoryId(),"",SCREENNAME+catObj.get(groupPosition)+"-"+AppConstants.GA_EVENT_DRAWER_SUB_CATEGORY_CLICK);}catch(Exception e){}
                        System.out.println("====catobj parent========"+catObj.get(groupPosition));
                        return true;
                    }
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                System.out.println("====catobj child========"+catObj.get(groupPosition));
                startActivity(catObj.get(groupPosition).getChildren().get(childPosition));
                return false;
            }
        });
        return view;
    }

    public void setData(ArrayList<CategorySubcategoryBean> array) {
        menuListAdapter.setListData(array);
        menuListAdapter.notifyDataSetChanged();
    }

    public void setListShopByCategoriesHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int itemHeight = 0;
            for (int i = 0; i < numberOfItems; i++) {
                View item = listAdapter.getView(i, null, listView);
                item.measure(0, 0);
                itemHeight += item.getMeasuredHeight();
            }
            int totalDividerHeight = listView.getDividerHeight() * (numberOfItems - 1);

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = itemHeight + totalDividerHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    public void setExpandableListdata() {
        if (!isListView) {
            header = new ArrayList<>();
            menuMap = new HashMap<>();

            for (int i = 0; i < catObj.size(); i++) {
                header.add(catObj.get(i).getCategory());
                menuMap.put(catObj.get(i).getCategory(), catObj.get(i).getChildren());
            }
        }
    }

    public void startActivity(CategorySubcategoryBean categorySubcategoryBean) {
        Intent call = new Intent(getActivity(), CategoryTabs.class);
        Bundle call_bundle = new Bundle();
        ArrayList<CategorySubcategoryBean> list = categorySubcategoryBean.getChildren();

//        if (list.size() > 0) {
//            if (!list.get(0).getCategory().equals("All")) {
//                CategorySubcategoryBean carBean = new CategorySubcategoryBean();
//                carBean.setCategory("All");
//                carBean.setCategoryId(catObj.get(position).getChildren().get(groupPosition).getCategoryId());
//                list.add(0, carBean);
//            }
//        } else {
        CategorySubcategoryBean carBean = new CategorySubcategoryBean();
        carBean.setCategory("All");
        carBean.setCategoryId(categorySubcategoryBean.getCategoryId());
        list.add(carBean);
//        }
        call_bundle.putSerializable("Categories", list);
        call_bundle.putSerializable("Header", categorySubcategoryBean.getCategory());
        call.putExtras(call_bundle);
        startActivity(call);
    }

    public boolean isExpandable(int position) {

        boolean expand = false;
        for (int i = 0; i < catObj.get(position).getChildren().size(); i++) {
            if (catObj.get(position).getChildren().get(i).getChildren().size() > 0) {

                //Toast.makeText(getActivity(), "Name : "+ catObj.get(groupPosition).getChildren().get(i).getCategory()+"Size :" + catObj.get(groupPosition).getChildren().get(i).getChildren().size(), Toast.LENGTH_SHORT).show();
                expand = true;
                break;
            }
        }

        return expand;
    }
}
