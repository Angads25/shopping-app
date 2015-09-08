package com.sakshay.grocermax.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 9/7/2015.
 */
public class LocationListBean extends BaseResponseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("location")
    @Expose
    private List<com.sakshay.grocermax.bean.LocationDetail> items = new ArrayList<com.sakshay.grocermax.bean.LocationDetail>();

    public List<com.sakshay.grocermax.bean.LocationDetail> getItems() {
        return items;
    }

    public void setItems(List<com.sakshay.grocermax.bean.LocationDetail> items) {
        this.items = items;
    }
}
