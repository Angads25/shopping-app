package com.rgretail.grocermax.bean;

import java.io.Serializable;

/**
 * Created by anchit-pc on 13-Jun-16.
 */
public class PramotionPageBean implements Serializable{

    private String name;
    private String desc;
    private String coupon_code;
    private String pramotion_order;
    private String is_active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getPramotion_order() {
        return pramotion_order;
    }

    public void setPramotion_order(String pramotion_order) {
        this.pramotion_order = pramotion_order;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
