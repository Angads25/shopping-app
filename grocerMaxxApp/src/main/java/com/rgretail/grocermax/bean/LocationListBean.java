package com.rgretail.grocermax.bean;

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
    private List<LocationDetail> items = new ArrayList<LocationDetail>();

    public List<LocationDetail> getItems() {
        return items;
    }

    public void setItems(List<LocationDetail> items) {
        this.items = items;
    }
}
