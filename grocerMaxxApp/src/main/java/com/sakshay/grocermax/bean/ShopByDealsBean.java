package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nawab on 26-09-2015.
 */
public class ShopByDealsBean implements Serializable{

    public String Result, flag;
    public ArrayList<ShopByDealModel> deal_type = new ArrayList<>();

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

    public ArrayList<ShopByDealModel> getArrayList() {
        return deal_type;
    }

    public void setArrayList(ArrayList<ShopByDealModel> deal_type) {
        this.deal_type = deal_type;
    }
}
