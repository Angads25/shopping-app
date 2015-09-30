package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manish.verma on 30-09-2015.
 */
public class HomeBannerBean implements Serializable{
    public String Result, flag;

    public ArrayList<Banner> banner;

    public ArrayList<Banner> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<Banner> banner) {
        this.banner = banner;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public static class Banner {


        public String name;
        public String linkurl;
        public String imageurl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLinkurl() {
            return linkurl;
        }

        public void setLinkurl(String linkurl) {
            this.linkurl = linkurl;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

    }

}
