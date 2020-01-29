package com.business.order_trip.models;

import java.io.Serializable;

public class OrderModel implements Serializable {
    int social_type;
    public String id, weight, category, country, city, destination, end_date, product_name, details, image_url, price, tax, total_price, quantity, sender_id, delive_id, trip_id, from, to, timestamp, imagePath, status;

    public OrderModel() {

    }

    public OrderModel(int social_type, String id, String weight, String category, String country, String city, String destination, String end_date, String product_name, String details, String image_url,
                      String price, String tax, String total_price, String quantity, String sender_id, String delive_id, String trip_id, String from, String to, String timestamp, String imagePath, String status) {
        this.social_type = social_type;
        this.id = id;
        this.weight = weight;
        this.category = category;
        this.country = country;
        this.city = city;
        this.destination = destination;
        this.end_date = end_date;
        this.product_name = product_name;
        this.details = details;
        this.image_url = image_url;
        this.price = price;
        this.tax = tax;
        this.total_price = total_price;
        this.quantity = quantity;
        this.sender_id = sender_id;
        this.delive_id = delive_id;
        this.trip_id = trip_id;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
        this.imagePath = imagePath;
        this.status= status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getDelive_id() {
        return delive_id;
    }

    public void setDelive_id(String delive_id) {
        this.delive_id = delive_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getSocial_type() {
        return social_type;
    }

    public void setSocial_type(int social_type) {
        this.social_type = social_type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
