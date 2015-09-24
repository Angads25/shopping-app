package com.sakshay.grocermax.hotoffers.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sakshay.grocermax.R;


/**
 * Created by nawab.hussain on 9/14/2015.
 */
public class BannerFragment extends Fragment {

    private LinearLayout parentLayout;
    private CardView card_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.banner, container, false);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
        card_view = (CardView) view.findViewById(R.id.card_view);
        return view;
    }
}
