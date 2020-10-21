package com.edu.portal.dao;

import com.edu.bean.TbItem;

import java.util.List;

public class ItemCustomer extends TbItem {
    private String[] images;

    public String[] getImages() {
        String image = this.getImage();
        if (null != image&& image.length()>0){
            return image.split(",");
        }
        return null;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
