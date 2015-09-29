package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nawab.hussain on 9/24/2015.
 */
public class OfferByDealTypeBean implements Serializable {


    String Result,flag;
//    HashMap<String,ArrayList<OfferByDealTypeModel>> dealcategorylisting;

    private DealCategory dealcategorylisting;

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public DealCategory getDealcategorylisting() {
        return dealcategorylisting;
    }

    public void setDealcategorylisting(DealCategory dealcategorylisting) {
        this.dealcategorylisting = dealcategorylisting;
    }

    //    public HashMap<String, ArrayList<OfferByDealTypeModel>> getDealcategorylisting() {
//        return dealcategorylisting;
//    }
//
//    public void setDealcategorylisting(HashMap<String, ArrayList<OfferByDealTypeModel>> dealcategorylisting) {
//        this.dealcategorylisting = dealcategorylisting;
//    }
}
