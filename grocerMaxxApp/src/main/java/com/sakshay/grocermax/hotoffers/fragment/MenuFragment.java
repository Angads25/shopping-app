package com.sakshay.grocermax.hotoffers.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sakshay.grocermax.CategoryTabs;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.bean.Category;
import com.sakshay.grocermax.bean.ShopByDealsBean;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.hotoffers.adapter.ExpandableMenuListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.MenuListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.ShopByDealsMenuListAdapter;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.UtilityMethods;

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
    private TextView txvTitle, txvShopByDeals, txvGetInTouch, txvShopByCategories;
    private ExpandableListView expandableListView;
    private ArrayList<String> header;
    private HashMap<String, ArrayList<CategorySubcategoryBean>> menuMap;
    private ExpandableMenuListAdapter expandableMenuListAdapter;
    private ShopByDealsMenuListAdapter shopByDealsListADapter;

    ShopByDealsBean shopByDealsBean;
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
//        }

        txvTitle = (TextView) view.findViewById(R.id.txvTitle);
        txvShopByCategories = (TextView) view.findViewById(R.id.txvShopByCategories);
        txvShopByDeals = (TextView) view.findViewById(R.id.txvShopByDeals);
        txvGetInTouch = (TextView) view.findViewById(R.id.txvGetInTouch);
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        lstMenu = (ListView) view.findViewById(R.id.lstMenu);
        lstShopByDealsMenu = (ListView) view.findViewById(R.id.lstShopByDealsMenu);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expLstMenu);

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
            txvShopByCategories.setVisibility(View.VISIBLE);

        } else {
            setExpandableListdata();
            lstMenu.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            expandableMenuListAdapter = new ExpandableMenuListAdapter(getActivity(), MenuFragment.this, header, menuMap);
            expandableListView.setAdapter(expandableMenuListAdapter);
            txvShopByDeals.setVisibility(View.GONE);
            txvGetInTouch.setVisibility(View.GONE);
            txvShopByCategories.setVisibility(View.GONE);
        }
        setListShopByCategoriesHeight(lstMenu);
        setListShopByCategoriesHeight(lstShopByDealsMenu);


        txvTitle.setText(title);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    ((HotOffersActivity) getActivity()).getDrawerLayout().closeDrawers();
                }
            }
        });
        txvShopByDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstShopByDealsMenu.setVisibility(View.VISIBLE);
                lstMenu.setVisibility(View.GONE);
            }
        });
        txvShopByCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstShopByDealsMenu.setVisibility(View.GONE);
                lstMenu.setVisibility(View.VISIBLE);
            }
        });
        txvGetInTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, "Check out the new awesome Grocermax! https://grocermax.com");
                startActivity(Intent.createChooser(intent,"How do you want to share ?"));
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
                        break;
                    }

                    if (!expand) {
                        //Toast.makeText(getActivity(), " Will not open "+catObj.get(groupPosition).getCategory(), Toast.LENGTH_SHORT).show();
                        expandableListView.collapseGroup(groupPosition);
                        startActivity(catObj.get(groupPosition));
                        return true;
                    }
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

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
