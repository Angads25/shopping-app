package com.rgretail.grocermax.hotoffers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.OfferByDealTypeSubModel;
import com.rgretail.grocermax.hotoffers.HotOffersActivity;
import com.rgretail.grocermax.hotoffers.adapter.ShopByDealDetailListAdapter;
import com.rgretail.grocermax.utils.Worker;

import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/15/2015.
 */
public class ShopByDealItemDetailGrid extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<OfferByDealTypeSubModel> offerList = new ArrayList<>();
    private static Worker workr;
    private static Fragment frag;

    public static ShopByDealItemDetailGrid newInstance(Worker worker,Fragment fragment) {
        //workr = worker;
        frag = fragment;
        return new ShopByDealItemDetailGrid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HotOffersActivity.bFromHome = false;
//        View view = inflater.inflate(R.layout.item_grid, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.gridView);
//        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 2);
//        ShopByDealDetailListAdapter optionsListAdapter = new ShopByDealDetailListAdapter(getActivity(), this);
//        recyclerView.setLayoutManager(gridLayout);
//        recyclerView.setAdapter(optionsListAdapter);
//        optionsListAdapter.setListData(offerList);

        View view = inflater.inflate(R.layout.item_grid, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.gridView);
//        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 2);
        ShopByDealDetailListAdapter optionsListAdapter = new ShopByDealDetailListAdapter(getActivity(), this);
//        recyclerView.setAdapter(optionsListAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(optionsListAdapter);
        optionsListAdapter.setListData(offerList);

        return view;
    }

    public void setData(ArrayList<OfferByDealTypeSubModel> arrayList) {
        this.offerList = arrayList;
    }

}
