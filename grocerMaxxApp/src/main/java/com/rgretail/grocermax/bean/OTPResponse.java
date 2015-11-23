package com.rgretail.grocermax.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Abhishek on 9/10/2015.
 */
public class OTPResponse implements Serializable {
    @Expose
    private String Result;
    @Expose
    private String otp;
    @Expose
    private String flag;

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        this.Result = result;
    }

    public String getOTP(){
        return otp;
    }

    public void setOTP(String otp){
        this.otp = otp;
    }

    public String getFlag(){
        return flag;
    }

    public void setFlag(String flag){
        this.flag = flag;
    }

}
