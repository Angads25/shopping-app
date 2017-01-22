package com.rgretail.grocermax.bean;

/**
 * Created by anchit-pc on 22-Jan-17.
 */
public class Payments {

    private int icon;
    private String desc;
    private String payment_mode;
    private boolean checke_status;



    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public boolean isChecke_status() {
        return checke_status;
    }

    public void setChecke_status(boolean checke_status) {
        this.checke_status = checke_status;
    }
}
