package com.example.recyclepro.models;

public class RecyclingSubmission {
    private String id;
    private String productName;
    private String time;
    private String state;

    public RecyclingSubmission(String id, String productName, String time, String state) {
        this.id = id;
        this.productName = productName;
        this.time = time;
        this.state = state;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RecyclingSubmission{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
