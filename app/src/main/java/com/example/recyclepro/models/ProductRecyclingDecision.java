package com.example.recyclepro.models;

public class ProductRecyclingDecision {
    private String id;
    private String customerName;
    private String productName;
    private String email;
    private String phone;
    private String customerAddress;
    private String branchAddress;
    private String description;
    private String time;

    public ProductRecyclingDecision(String id, String customerName, String productName, String email, String phone, String customerAddress, String branchAddres, String description, String time) {
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.email = email;
        this.phone = phone;
        this.customerAddress = customerAddress;
        this.branchAddress = branchAddres;
        this.description = description;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ProductRecyclingDecision{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", productName='" + productName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", branchAddres='" + branchAddress + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
