package com.rgretail.grocermax.bean;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 04-Sep-16.
 */
public class MaxCoin {

    String title;
    ArrayList<RedeemHistory> couponList;

    public MaxCoin(String title, ArrayList<RedeemHistory> couponList) {
        this.title = title;
        this.couponList = couponList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<RedeemHistory> getCouponList() {
        return couponList;
    }

    public void setCouponList(ArrayList<RedeemHistory> couponList) {
        this.couponList = couponList;
    }






}
