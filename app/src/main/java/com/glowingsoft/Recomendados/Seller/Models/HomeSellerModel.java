package com.glowingsoft.Recomendados.Seller.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeSellerModel implements Parcelable {
    String id, title, image, category_id, price, description, active, business_id, category_title, shop, owner_name, owner_id, owner_image;


    public static final Creator<HomeSellerModel> CREATOR = new Creator<HomeSellerModel>() {
        @Override
        public HomeSellerModel createFromParcel(Parcel in) {
            return new HomeSellerModel();
        }

        @Override
        public HomeSellerModel[] newArray(int size) {
            return new HomeSellerModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_image() {
        return owner_image;
    }

    public void setOwner_image(String owner_image) {
        this.owner_image = owner_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(active);
        dest.writeString(business_id);
        dest.writeString(image);
        dest.writeString(owner_id);
        dest.writeString(owner_image);
        dest.writeString(owner_name);
        dest.writeString(category_id);
        dest.writeString(category_title);
    }
}
