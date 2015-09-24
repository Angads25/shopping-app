package com.sakshay.grocermax.bean;

/**
 * Created by nawab.hussain on 9/24/2015.
 */
public class OfferByDealTypeModel {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    String dealType;
    String img;
}
