package com.rgretail.grocermax.bean;

import java.io.Serializable;

/**
 * Created by Nawab on 26-09-2015.
 */
public class ShopByDealModel implements Serializable{
    String id,dealType,img;

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
}
