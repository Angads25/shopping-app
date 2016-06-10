package com.rgretail.grocermax.info;

/**
 * Created by anchit-pc on 10-Jun-16.
 */
public class AppsInfoModel {

    private String app_package_name;
    private String app_name;
    private String app_version_name;
    private String app_version_code;


    public AppsInfoModel(String app_package_name, String app_name, String app_version_name, String app_version_code) {
        this.app_package_name = app_package_name;
        this.app_name = app_name;
        this.app_version_name = app_version_name;
        this.app_version_code = app_version_code;
    }

    public String getApp_package_name() {
        return app_package_name;
    }

    public void setApp_package_name(String app_package_name) {
        this.app_package_name = app_package_name;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_version_name() {
        return app_version_name;
    }

    public void setApp_version_name(String app_version_name) {
        this.app_version_name = app_version_name;
    }

    public String getApp_version_code() {
        return app_version_code;
    }

    public void setApp_version_code(String app_version_code) {
        this.app_version_code = app_version_code;
    }
}
