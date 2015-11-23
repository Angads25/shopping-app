package com.rgretail.grocermax.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 9/26/2015.
 */
public class DealListBean extends BaseResponseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("items")
    @Expose
    private List<Product> items = new ArrayList<Product>();

    public List<Product> getProduct() {
        return items;
    }

    public void setProduct(List<Product> items) {
        this.items = items;
    }
}
