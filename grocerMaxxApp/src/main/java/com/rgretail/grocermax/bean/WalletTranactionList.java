package com.rgretail.grocermax.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anchit-pc on 17-Dec-15.
 */
public class WalletTranactionList implements Serializable {

    @SerializedName("Result")
    public String result;

    @SerializedName("log")
    public List<WalletTransaction> walletTransactionsList;

    public int flag;

}
