package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nawab.hussain on 9/24/2015.
 */
public class OfferByDealTypeModel implements Serializable{
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
    String image;
    ArrayList<OfferByDealTypeSubModel> deals;

    public ArrayList<OfferByDealTypeSubModel> getDeals() {
        return deals;
    }

    public void setDeals(ArrayList<OfferByDealTypeSubModel> deals) {
        this.deals = deals;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
