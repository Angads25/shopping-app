package com.rgretail.grocermax.hotoffers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.DealProductListingModel;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.hotoffers.adapter.ProductListingAdapter;

import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/15/2015.
 */
public class DealProductListingItemDetailGrid extends Fragment {
    private RecyclerView recyclerView;
    private ListView lv;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<DealProductListingModel> offerList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeScreen.bFromHome = false;

        Bundle bundle = getArguments();
        offerList = (ArrayList<DealProductListingModel>) bundle.get(Constants.PRODUCTLIST);
        View view = inflater.inflate(R.layout.item_grid, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.gridView1);
        lv=(ListView)view.findViewById(R.id.gridView);

        recyclerView.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
//        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 2);
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 1);
        ProductListingAdapter optionsListAdapter = new ProductListingAdapter(getActivity(), this);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setAdapter(optionsListAdapter);
        optionsListAdapter.setListData(offerList);
        return view;
    }

    public void setData(ArrayList<DealProductListingModel> arrayList) {
        this.offerList = arrayList;
    }


}
