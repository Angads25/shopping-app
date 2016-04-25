package com.rgretail.grocermax.hotoffers.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.hotoffers.adapter.ShopByDealDetailListAdapter;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.Worker;

import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/15/2015.
 */
public class ShopByDealItemDetailGrid extends Fragment {
   // private RecyclerView recyclerView;
   private RecyclerView recyclerView;
    private ListView lv;
    private ArrayList<String> arrayList = new ArrayList<>();
   // private ArrayList<OfferByDealTypeSubModel> offerList = new ArrayList<>();
   private ArrayList<Product> offerList = new ArrayList<>();
    private static Worker workr;
    private static Fragment frag;

    public static ShopByDealItemDetailGrid newInstance(Worker worker,Fragment fragment) {
        //workr = worker;
        frag = fragment;
        return new ShopByDealItemDetailGrid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getActivity() instanceof HomeScreen)
         HomeScreen.bFromHome = false;
        else
         HomeScreen.bFromHome = true;

        View view = inflater.inflate(R.layout.item_grid, container, false);
        //recyclerView = (RecyclerView) view.findViewById(R.id.gridView);
        recyclerView = (RecyclerView) view.findViewById(R.id.gridView1);
        lv = (ListView) view.findViewById(R.id.gridView);
        lv.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
//        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 2);
        ShopByDealDetailListAdapter optionsListAdapter = new ShopByDealDetailListAdapter(getActivity(), this);
//        recyclerView.setAdapter(optionsListAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        lv.setAdapter(optionsListAdapter);
        try {
            optionsListAdapter.setListData(offerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((BaseActivity) getActivity()).showDialog();
                    String url = UrlsConstants.PRODUCT_DETAIL_URL + offerList.get(position).getProductid();
                    ((BaseActivity) getActivity()).myApi.reqProductDetailFromNotification(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void setData(ArrayList<Product> arrayList) {
        this.offerList = arrayList;
    }

}
