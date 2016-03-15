package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nawab on 26-09-2015.
 */
public class OfferByDealTypeSubModel implements Serializable{
    String id, category_id, dealName, img, images, name , image;
    //ArrayList<OfferByDealTypeSubModel> deals;
    ArrayList<Product> deals;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String promo_id, title, deal_image;


    public String getPromo_id() {
        return promo_id;
    }

    public void setPromo_id(String promo_id) {
        this.promo_id = promo_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeal_image() {
        return deal_image;
    }

    public void setDeal_image(String deal_image) {
        this.deal_image = deal_image;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

   /* public ArrayList<OfferByDealTypeSubModel> getDeals() {
        return deals;
    }

    public void setDeals(ArrayList<OfferByDealTypeSubModel> deals) {
        this.deals = deals;
    }*/
   public ArrayList<Product> getDeals() {
       return deals;
   }

    public void setDeals(ArrayList<Product> deals) {
        this.deals = deals;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
