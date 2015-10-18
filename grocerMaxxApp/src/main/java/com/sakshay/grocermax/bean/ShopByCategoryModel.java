package com.sakshay.grocermax.bean;

import java.io.Serializable;

/**
 * Created by nawab.hussain on 9/24/2015.
 */
public class ShopByCategoryModel implements Serializable{
    String category_id;
    String images;
    String name;
    String offercount;
    String is_active;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffercount() {
        return offercount;
    }

    public void setOffercount(String offercount) {
        this.offercount = offercount;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }


}