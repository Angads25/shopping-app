package com.rgretail.grocermax.bean;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 22-Jan-17.
 */
public class AllPayment {

    private String Payment_Type;
    private ArrayList<Payments> paymentsList;

    public AllPayment(String payment_Type, ArrayList<Payments> paymentsList) {
        Payment_Type = payment_Type;
        this.paymentsList = paymentsList;
    }

    public String getPayment_Type() {
        return Payment_Type;
    }

    public void setPayment_Type(String payment_Type) {
        Payment_Type = payment_Type;
    }

    public ArrayList<Payments> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(ArrayList<Payments> paymentsList) {
        this.paymentsList = paymentsList;
    }
}
