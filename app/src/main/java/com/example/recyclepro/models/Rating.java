package com.example.recyclepro.models;

public class Rating {
    public int rating;
    public double ratePrice;

    public Rating(int rating, double ratePrice) {
        this.rating = rating;
        this.ratePrice = ratePrice;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getRatePrice() {
        return ratePrice;
    }

    public void setRatePrice(double ratePrice) {
        this.ratePrice = ratePrice;
    }
    //getter setter

    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                ", ratePrice=" + ratePrice +
                '}';
    }
}
