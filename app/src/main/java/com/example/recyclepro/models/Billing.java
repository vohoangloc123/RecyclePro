package com.example.recyclepro.models;

public class Billing {
    private String id;
    private String reviewer;
    private String fullName;
    private String detailBilling;
    private String customerName;
    private String email;
    private String customerAddress;
    private String time;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDetailBilling() {
        return detailBilling;
    }

    public void setDetailBilling(String detailBilling) {
        this.detailBilling = detailBilling;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Billing(String id, String reviewer, String fullName, String detailBilling, String customerName, String email, String customerAddress, String time, String status) {
        this.id = id;
        this.reviewer = reviewer;
        this.fullName = fullName;
        this.detailBilling = detailBilling;
        this.customerName = customerName;
        this.email = email;
        this.customerAddress = customerAddress;
        this.time = time;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Billing{" +
                "id='" + id + '\'' +
                ", reviewer='" + reviewer + '\'' +
                ", fullName='" + fullName + '\'' +
                ", detailBilling='" + detailBilling + '\'' +
                ", customerName='" + customerName + '\'' +
                ", email='" + email + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}
