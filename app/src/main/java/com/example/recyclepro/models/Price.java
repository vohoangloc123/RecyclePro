package com.example.recyclepro.models;

public class Price {
    private String id;
    private String productName;
    private Double price;
    private String branchName;

    public Price(String id, String productName, Double price, String branchName) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.branchName = branchName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", branchName='" + branchName + '\'' +
                '}';
    }
}
