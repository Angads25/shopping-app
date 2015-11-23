package com.rgretail.grocermax.utils;

import com.rgretail.grocermax.bean.OfferByDealTypeSubModel;

import java.util.ArrayList;

/**
 * Created by Nawab on 25-09-2015.
 */
public class Worker {
//    onPageChange callback;
    ArrayList<OfferByDealTypeSubModel> arrayList = new ArrayList<>();

    onPageChange myHandler;
    public void setHandlerListener(onPageChange listener)
    {
        myHandler=listener;
    }
    public void myEventFired( ArrayList<OfferByDealTypeSubModel> arrayList)
    {
        if(myHandler!=null)
            myHandler.getonPageChanged(arrayList);
    }
}
