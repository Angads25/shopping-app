package com.rgretail.grocermax.bean;

import java.io.Serializable;

/**
 * Created by anchit-pc on 29-Jun-16.
 */
public class RedeemHistory implements Serializable{

    private String id;
    private String redeem_point;
    private String created_date;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRedeem_point() {
        return redeem_point;
    }

    public void setRedeem_point(String redeem_point) {
        this.redeem_point = redeem_point;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
