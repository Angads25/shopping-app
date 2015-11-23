package com.rgretail.grocermax.bean;

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
    private List<ProductListBean> ProductList = new ArrayList<ProductListBean>();

    /**
     *
     * @return The Product
     */
    public List<ProductListBean> getProduct() {
        return ProductList;
    }

    /**
     *
     * @param Product
     *            The Product
     */
    public void setProduct(List<ProductListBean> ProductList) {
        this.ProductList = ProductList;
    }

}
