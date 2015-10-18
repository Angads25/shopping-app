package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/24/2015.
 */
public class ShopByCategoryBean implements Serializable{

    public String Result, flag;
    public ArrayList<ShopByCategoryModel> category = new ArrayList<>();

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

    public ArrayList<ShopByCategoryModel> getArrayList() {
        return category;
    }

    public void setArrayList(ArrayList<ShopByCategoryModel> category) {
        this.category = category;
    }


}
