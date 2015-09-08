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
    private String city_name;

    @Expose
    private String state_id;

    @Expose
    private String api_url;


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
        return state_id;
    }

    public void setStateId(String price) {
        this.state_id = state_id;
    }

    public String getApiUrl() {
        return api_url;
    }

    public void setApiUrl(String row_total) {
        this.api_url = api_url;
    }

}
