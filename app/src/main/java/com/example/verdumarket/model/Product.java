package com.example.verdumarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("stock")
    private int stock;

    @SerializedName("unitPrice")
    private double unitPrice;

    @SerializedName("unitMeasurement")
    private String unitMeasurement;

    @SerializedName("categoryId")
    private int categoryId;

    @SerializedName("category")
    private Category category;

    public Product (int id, String name, int stock, double unitPrice, String unitMeasurement, int categoryId) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.unitMeasurement = unitMeasurement;
        this.categoryId = categoryId;
    }

    public static Product createNewProduct(String name, int stock, double unitPrice, String unitMeasurement, int categoryId) {
        return new Product(-1, name, stock, unitPrice, unitMeasurement, categoryId); //-1 represents no ID
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // Parcelable implementation

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        stock = in.readInt();
        unitPrice = in.readDouble();
        unitMeasurement = in.readString();
        categoryId = in.readInt();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(stock);
        dest.writeDouble(unitPrice);
        dest.writeString(unitMeasurement);
        dest.writeInt(categoryId);
        dest.writeParcelable(category, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
