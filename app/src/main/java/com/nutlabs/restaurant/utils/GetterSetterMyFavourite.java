package com.nutlabs.restaurant.utils;

/**
 * Created by Shubham on 10/9/2016.
 */

public class GetterSetterMyFavourite {

    /**
     * businessId :
     * name : Kushal's fashion Jewellery
     * location : #12,27th Main,sector 2,Bengalore,Karnataka,560056,India
     * businessType : Jewellery
     * productCategory : Jewellery
     * sales : 12
     * image :
     * followers : 10
     */

    private String businessId;
    private String name;
    private String location;
    private String businessType;
    private String productCategory;
    private int sales;
    private String image;
    private int followers;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}