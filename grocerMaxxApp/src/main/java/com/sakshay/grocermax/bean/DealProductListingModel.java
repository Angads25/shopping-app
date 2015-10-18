package com.sakshay.grocermax.bean;

/**
 * Created by Nawab on 26-09-2015.
 */
public class DealProductListingModel {

    String productid,Name,p_brand,p_pack,promotion_level,Price,sale_price,Image,Status,currencycode;

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getP_brand() {
        return p_brand;
    }

    public void setP_brand(String p_brand) {
        this.p_brand = p_brand;
    }

    public String getP_pack() {
        return p_pack;
    }

    public void setP_pack(String p_pack) {
        this.p_pack = p_pack;
    }

    public String getPromotion_level() {
        return promotion_level;
    }

    public void setPromotion_level(String promotion_level) {
        this.promotion_level = promotion_level;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode;
    }
}
