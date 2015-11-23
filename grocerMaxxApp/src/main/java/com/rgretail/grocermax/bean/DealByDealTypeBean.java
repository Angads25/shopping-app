package com.rgretail.grocermax.bean;

import java.io.Serializable;

/**
 * Created by nawab.hussain on 9/24/2015.
 */
public class DealByDealTypeBean implements Serializable {


    String Result,flag;
//    HashMap<String,ArrayList<OfferByDealTypeSubModel>> dealcategory;

    public DealCategory dealcategory;

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

    public DealCategory getDealcategory() {
        return dealcategory;
    }

    public void setDealcategory(DealCategory dealcategory) {
        this.dealcategory = dealcategory;
    }
//    public HashMap<String, ArrayList<OfferByDealTypeSubModel>> getDealcategorylisting() {
//        return dealcategory;
//    }
//
//    public void setDealcategorylisting(HashMap<String, ArrayList<OfferByDealTypeSubModel>> dealcategory) {
//        this.dealcategory = dealcategory;
//    }
}
