package com.sakshay.grocermax.bean;

import android.os.Parcelable;

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
    private List<com.sakshay.grocermax.bean.Product> items = new ArrayList<com.sakshay.grocermax.bean.Product>();

    public List<com.sakshay.grocermax.bean.Product> getProduct() {
        return items;
    }

    public void setProduct(List<com.sakshay.grocermax.bean.Product> items) {
        this.items = items;
    }
}
