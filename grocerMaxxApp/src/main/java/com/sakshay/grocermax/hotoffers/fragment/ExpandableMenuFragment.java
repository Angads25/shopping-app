package com.sakshay.grocermax.hotoffers.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sakshay.grocermax.CategoryTabs;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.bean.ShopByDealsBean;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.hotoffers.adapter.ExpandableMenuListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.MenuListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.ShopByDealsMenuListAdapter;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nawab.hussain on 10/7/2015.
 */
public class ExpandableMenuFragment extends Fragment {
    private ArrayList<CategorySubcategoryBean> catObj;
    private ArrayList<String> menuArray = new ArrayList<>();
    private ListView lstMenu, lstShopByDealsMenu;
    private MenuListAdapter menuListAdapter;
    private ImageView imgBack;
    private LinearLayout llExpandableMenu;
    private String title;
    private Boolean isListView;
    private TextView txvTitle, txvShopByDeals, txvGetInTouch, txvShopByCategories;
    private ExpandableListView expandableListView;
    private ArrayList<String> header;
    private HashMap<String, ArrayList<CategorySubcategoryBean>> menuMap;
    private ExpandableMenuListAdapter expandableMenuListAdapter;
    private ShopByDealsMenuListAdapter shopByDealsListADapter;
    private boolean isFromDrawer = false;
    private int lastExpandedGroupPosition;

    ShopByDealsBean shopByDealsBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        menuListAdapter = new MenuListAdapter(getActivity(), ExpandableMenuFragment.this);
        shopByDealsListADapter = new ShopByDealsMenuListAdapter(getActivity(), ExpandableMenuFragment.this);

        if (bundle != null) {
            catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");
            shopByDealsBean = (ShopByDealsBean) bundle.get(Constants.SHOP_BY_DEALS_MODEL);
            title = bundle.getString("Title");
            isListView = bundle.getBoolean("isListView");
            isFromDrawer = bundle.getBoolean("isFromDrawer");
        } else {
            String response = UtilityMethods.readCategoryResponse(getActivity(), AppConstants.categoriesFile);
            catObj = UtilityMethods.getCategorySubCategory(response);
        }


        View view = inflater.inflate(R.layout.expandable_menu_fragment, container, false);
//        for (int i = 0; i < catObj.size(); i++) {
//            menuArray.add(catObj.get(i).getCategory());
//        }

        txvTitle = (TextView) view.findViewById(R.id.txvTitle);
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        llExpandableMenu = (LinearLayout) view.findViewById(R.id.ll_expandable_menu);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expLstMenu);

        setExpandableListdata();
        expandableListView.setVisibility(View.VISIBLE);
        expandableMenuListAdapter = new ExpandableMenuListAdapter(getActivity(), ExpandableMenuFragment.this, header, menuMap);
        expandableListView.setAdapter(expandableMenuListAdapter);


        txvTitle.setText(title);
        llExpandableMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    ((HotOffersActivity) getActivity()).getDrawerLayout().closeDrawers();
                }
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != lastExpandedGroupPosition) {
                    expandableListView.collapseGroup(lastExpandedGroupPosition);
                }
                lastExpandedGroupPosition = groupPosition;

            }
        });


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean expand = false;
//                expandableListView.c
                //Toast.makeText(getActivity(), "Size :" + catObj.get(groupPosition).getChildren().size(), Toast.LENGTH_SHORT).show();

                if(catObj.get(groupPosition).getChildren().size() > 0) {

                    for (int i = 0; i < catObj.get(groupPosition).getChildren().size(); i++) {
                        if (catObj.get(groupPosition).getChildren().size() > 0) {
//                       String asa =  catObj.get(groupPosition).getCategory();
//                        String asa1 =  catObj.get(groupPosition).getCategoryId();
                            if (catObj.get(groupPosition).getChildren().get(i).getChildren().size() > 0) {
                                //Toast.makeText(getActivity(), "Name : "+ catObj.get(groupPosition).getChildren().get(i).getCategory()+"Size :" + catObj.get(groupPosition).getChildren().get(i).getChildren().size(), Toast.LENGTH_SHORT).show();
                                expand = true;
                                break;
                            }
                        }


//                    for (int i = 0; i < catObj.get(mainCatPosition).getChildren().get(selectedIndex).getChildren().size(); i++) {
//                    if (catObj.get(mainCatPosition).getChildren().get(selectedIndex).getChildren().get(i).getChildren().size() > 0) {


                        if (!expand) {
                            //Toast.makeText(getActivity(), " Will not open "+catObj.get(groupPosition).getCategory(), Toast.LENGTH_SHORT).show();
                            expandableListView.collapseGroup(groupPosition);
//                            startActivity(catObj.get(groupPosition));
                            ((HotOffersActivity) getActivity()).isFromFragment = false;
                            ((HotOffersActivity) getActivity()).showDialog();
                            ((HotOffersActivity) getActivity()).getDrawerLayout().closeDrawers();
//                        String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(position).getChildren().get(groupPosition).getCategoryId();
                            String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(groupPosition).getCategoryId();
                            AppConstants.strTitleHotDeal = catObj.get(groupPosition).getCategory();
                            System.out.println(catObj.get(groupPosition).getCategory() + "==========id==========" + catObj.get(groupPosition).getCategoryId());
                            ((HotOffersActivity) getActivity()).myApi.reqAllProductsCategory(url);
                        }

                        return true;
                    }
                }else{
                    expandableListView.collapseGroup(groupPosition);
//                            startActivity(catObj.get(groupPosition));
                    ((HotOffersActivity) getActivity()).isFromFragment = false;
                    ((HotOffersActivity) getActivity()).showDialog();
                    ((HotOffersActivity) getActivity()).getDrawerLayout().closeDrawers();
//                        String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(position).getChildren().get(groupPosition).getCategoryId();
                    String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(groupPosition).getCategoryId();
                    AppConstants.strTitleHotDeal = catObj.get(groupPosition).getCategory();
                    System.out.println(catObj.get(groupPosition).getCategory() + "==========id==========" + catObj.get(groupPosition).getCategoryId());
                    ((HotOffersActivity) getActivity()).myApi.reqAllProductsCategory(url);
                    return true;
                }



                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

//                startActivity(catObj.get(groupPosition).getChildren().get(childPosition));
                ((HotOffersActivity) getActivity()).showDialog();
                ((HotOffersActivity) getActivity()).getDrawerLayout().closeDrawers();
                String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(groupPosition).getChildren().get(childPosition).getCategoryId();
                AppConstants.strTitleHotDeal = catObj.get(groupPosition).getChildren().get(childPosition).getCategory();
                System.out.println(catObj.get(groupPosition).getChildren().get(childPosition).getCategory() + "==========id111==========" + catObj.get(groupPosition).getChildren().get(childPosition).getCategoryId());
                ((HotOffersActivity) getActivity()).myApi.reqAllProductsCategory(url);
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
                System.out.println("==========>>>>>>>" + catObj.get(i).getCategory());
                if (catObj.get(i).getIsActive().equals("1")) {
                    header.add(catObj.get(i).getCategory());
                    menuMap.put(catObj.get(i).getCategory(), catObj.get(i).getChildren());
                }
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
        call_bundle.putBoolean("isFromDrawerMenu", true);
        call.putExtras(call_bundle);
        startActivity(call);
        ((HotOffersActivity) getActivity()).getDrawerLayout().closeDrawers();
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

    public boolean isExpanded(int position) {

        return expandableListView.isGroupExpanded(position);
    }
}
