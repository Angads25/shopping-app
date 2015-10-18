package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nawab on 26-09-2015.
 */
public class DealProductListingBean implements Serializable {


    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getTotalcount() {
        return Totalcount;
    }

    public void setTotalcount(String totalcount) {
        Totalcount = totalcount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ArrayList<DealProductListingModel> getProduct() {
        return Product;
    }

    public void setProduct(ArrayList<DealProductListingModel> product) {
        Product = product;
    }
//            8
//            9
//    Totalcount : 124
//    flag : 1

    String Result,Totalcount,flag;
    ArrayList<DealProductListingModel> Product;
}
