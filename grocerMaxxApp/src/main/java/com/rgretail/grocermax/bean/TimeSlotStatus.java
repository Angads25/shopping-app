package com.rgretail.grocermax.bean;

/**
 * Created by anchit-pc on 25-Jan-17.
 */
public class TimeSlotStatus {

    String data;
    boolean status;

    public TimeSlotStatus(String data, boolean status) {
        this.data = data;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
