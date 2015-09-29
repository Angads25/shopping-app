package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manish.verma on 29-09-2015.
 */
public class DealCategory implements Serializable{

    public ArrayList<OfferByDealTypeSubModel> all;
    public ArrayList<OfferByDealTypeSubModel> category;
    public ArrayList<OfferByDealTypeModel> dealsCategory;



    public ArrayList<OfferByDealTypeModel> getDealsCategory() {
        return dealsCategory;
    }

    public void setDealsCategory(ArrayList<OfferByDealTypeModel> dealsCategory) {
        this.dealsCategory = dealsCategory;
    }

    public ArrayList<OfferByDealTypeSubModel> getAll() {
        return all;
    }

    public void setAll(ArrayList<OfferByDealTypeSubModel> all) {
        this.all = all;
    }

    public ArrayList<OfferByDealTypeSubModel> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<OfferByDealTypeSubModel> category) {
        this.category = category;
    }
}
