package com.food.ecom.model;

import java.io.Serializable;

/**
 * Created by ThetaTeam2 on 01/06/2018.
 */

public class Product implements Serializable {
    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    String Key;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getCategoryType() {
        return CategoryType;
    }

    public void setCategoryType(int categoryType) {
        CategoryType = categoryType;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    String Name;
    String Description;
    String Image;
    Double Price;

    public Double getQty() {
        return Qty;
    }

    public void setQty(Double qty) {
        Qty = qty;
    }

    Double Qty;
    public String getPriceString() {
        return PriceString;
    }

    public void setPriceString(String priceString) {
        PriceString = priceString;
    }

    String PriceString;
    String Category;
    int CategoryType;
    int Status;

}
