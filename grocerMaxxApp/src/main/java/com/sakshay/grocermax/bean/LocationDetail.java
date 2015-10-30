package com.sakshay.grocermax.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Abhishek on 9/7/2015.
 */
public class LocationDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    private String id;

    @Expose
    private String storeid;

    @Expose
    private String city_name;

    @Expose
    private String region_id;

    @Expose
    private String api_url;

    @Expose
    private String default_name;

    public void setStateName(String strState){this.default_name = strState; }

    public String getStateName(){return default_name; }

    public String getId() {return id;}

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return city_name;
    }

    public void setCityName(String city_name) {
        this.city_name = city_name;
    }

    public String getStateId() {
        return region_id;
    }

    public void setStateId(String price) {
        this.region_id = region_id;
    }

    public String getApiUrl() {
        return api_url;
    }

    public void setApiUrl(String row_total) {
        this.api_url = api_url;
    }

    public String getStoreId() {
        return storeid;
    }

    public void setStoreId(String store_id) {
        this.storeid = store_id;
    }

}
