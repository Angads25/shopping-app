package com.sakshay.grocermax.hotoffers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.bean.OfferByDealTypeSubModel;
import com.sakshay.grocermax.hotoffers.adapter.DetailListAdapter;
import com.sakshay.grocermax.hotoffers.adapter.ShopByDealDetailListAdapter;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Worker;
import com.sakshay.grocermax.utils.onPageChange;

import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/15/2015.
 */
public class ItemDetailGrid extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<OfferByDealTypeSubModel> offerList = new ArrayList<>();
    private static Worker workr;
    private static Fragment frag;

    public static ItemDetailGrid newInstance(Worker worker,Fragment fragment) {
        //workr = worker;
        frag = fragment;
        return new ItemDetailGrid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new Worker().setHandlerListener(new onPageChange() {
            @Override
            public void getonPageChanged(ArrayList<OfferByDealTypeSubModel> arrayList) {
                offerList = arrayList;
            }
        });
//        addArrayData();
//        offerList = ((ItemDetailFragment) frag).getOfferData(0);

//        View view = inflater.inflate(R.layout.item_grid, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.gridView);
//        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 2);
//        DetailListAdapter optionsListAdapter = new DetailListAdapter(getActivity(), this);
//        recyclerView.setLayoutManager(gridLayout);
//        recyclerView.setAdapter(optionsListAdapter);
//        optionsListAdapter.setListData(offerList);

        ((BaseActivity) getActivity()).initHeader(getActivity().findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
        ((BaseActivity) getActivity()).findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        ((BaseActivity) getActivity()).findViewById(R.id.header).setVisibility(View.GONE);

        View view = inflater.inflate(R.layout.item_grid, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.gridView);
        DetailListAdapter optionsListAdapter = new DetailListAdapter(getActivity(), this);
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
