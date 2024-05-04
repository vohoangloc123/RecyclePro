package com.example.recyclepro.models;

public class Product {
    private String productID;
    private String battery;
    private String caseDescribe;
    private String screen;
    private String phone;
    private String productName;
    private String purchasedDate;
    private String state;
    private String customerName;

    public Product(String productID, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen) {
        this.productID = productID;
        this.battery = battery;
        this.caseDescribe = caseDescribe;
        this.screen = screen;
        this.phone = phone;
        this.productName = productName;
        this.purchasedDate = purchasedDate;
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getCaseDescribe() {
        return caseDescribe;
    }

    public void setCaseDescribe(String caseDescribe) {
        this.caseDescribe = caseDescribe;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "Product{" +
                "productID='" + productID + '\'' +
                ", battery='" + battery + '\'' +
                ", caseDescribe='" + caseDescribe + '\'' +
                ", describe='" + screen + '\'' +
                ", phone='" + phone + '\'' +
                ", productName='" + productName + '\'' +
                ", purchasedDate='" + purchasedDate + '\'' +
                ", state='" + state + '\'' +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
