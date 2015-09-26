package com.sakshay.grocermax.utils;

import com.sakshay.grocermax.bean.OfferByDealTypeModel;

import java.util.ArrayList;

/**
 * Created by Nawab on 25-09-2015.
 */
public class Worker {
//    onPageChange callback;
    ArrayList<OfferByDealTypeModel> arrayList = new ArrayList<>();

    onPageChange myHandler;
    public void setHandlerListener(onPageChange listener)
    {
        myHandler=listener;
    }
    public void myEventFired( ArrayList<OfferByDealTypeModel> arrayList)
    {
        if(myHandler!=null)
            myHandler.getonPageChanged(arrayList);
    }
}
