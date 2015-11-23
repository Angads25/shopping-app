package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Abhishek on 10/5/2015.
 */
public class Simple implements Serializable{

    ArrayList<CategoriesProducts> hotproduct;
    ArrayList<CategoriesProducts> ProductList;
    String flag;


    public ArrayList<CategoriesProducts> getProductList() {
        return ProductList;
    }

    public void setProductList(ArrayList<CategoriesProducts> productList) {
        ProductList = productList;
    }

    public ArrayList<CategoriesProducts> getHotproduct() {
        return hotproduct;
    }

    public void setHotproduct(ArrayList<CategoriesProducts> hotproduct) {
        this.hotproduct = hotproduct;
    }

    public void setFlag(String flag){
        this.flag = flag;
    }

    public String getFlag(){
        return flag;
    }

}
