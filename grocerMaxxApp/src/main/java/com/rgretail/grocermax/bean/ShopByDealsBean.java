package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nawab on 26-09-2015.
 */
public class ShopByDealsBean implements Serializable{

    public String Result, flag;
    public ArrayList<ShopByDealModel> deal_type = new ArrayList<>();

    public String name;
    public String linkurl;
    public String imageurl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

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
