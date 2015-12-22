package com.rgretail.grocermax.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anchit-pc on 17-Dec-15.
 */
public class WalletTransaction implements Serializable{

   @SerializedName("action_date")
    public String actionDate;

   @SerializedName("value_change")
   public String valueChange;

    public String comment;

    @SerializedName("order_id")
    public String orderId;

}
