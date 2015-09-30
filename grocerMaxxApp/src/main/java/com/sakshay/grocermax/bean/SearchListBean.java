package com.sakshay.grocermax.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 9/24/2015.
 */
public class SearchListBean extends  BaseResponseBean implements Serializable{

    private static final long serialVersionUID = 1L;

    @Expose
    private List<com.sakshay.grocermax.bean.ProductListBean> ProductList = new ArrayList<com.sakshay.grocermax.bean.ProductListBean>();

    /**
     *
     * @return The Product
     */
    public List<com.sakshay.grocermax.bean.ProductListBean> getProduct() {
        return ProductList;
    }

    /**
     *
     * @param Product
     *            The Product
     */
    public void setProduct(List<com.sakshay.grocermax.bean.ProductListBean> ProductList) {
        this.ProductList = ProductList;
    }

}
