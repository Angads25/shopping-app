package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nawab.hussain on 9/24/2015.
 */
public class DealByDealTypeBean implements Serializable {


    String Result,flag;
    HashMap<String,ArrayList<OfferByDealTypeSubModel>> dealcategory;

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

    public HashMap<String, ArrayList<OfferByDealTypeSubModel>> getDealcategorylisting() {
        return dealcategory;
    }

    public void setDealcategorylisting(HashMap<String, ArrayList<OfferByDealTypeSubModel>> dealcategory) {
        this.dealcategory = dealcategory;
    }
}
