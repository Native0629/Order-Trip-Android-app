package com.business.order_trip.models;

import java.io.Serializable;

public class User implements Serializable {

    public User() {

    }
    public String id;
    public String status;
    public String username;
    public String first_name;
    public String last_name;
    public String email;
    public String password;
    public String country;
    public String phone_number;
    public String imageUri;
    public String date;
    public String imagePath;
    public int phone_status, email_status, shopper_count, trip_count, pre_shopper_count, pre_trip_count, upcoming_trip_count, social_type;

    public User(String id, String status, String username, String first_name, String last_name, String email, String password, String country, String phone_number, String imageUri, String date, String imagePath, int phone_status, int email_status, int shopper_count, int trip_count, int pre_shopper_count, int pre_trip_count, int upcoming_trip_count, int social_type) {
        this.id = id;
        this.status = status;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.country = country;
        this.phone_number = phone_number;
        this.imageUri = imageUri;
        this.date = date;
        this.imagePath = imagePath;
        this.phone_status = phone_status;
        this.email_status = email_status;
        this.shopper_count = shopper_count;
        this.trip_count = trip_count;
        this.pre_shopper_count = pre_shopper_count;
        this.pre_trip_count = pre_trip_count;
        this.upcoming_trip_count = upcoming_trip_count;
        this.social_type = social_type;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPhone_status() {
        return phone_status;
    }

    public void setPhone_status(int phone_status) {
        this.phone_status = phone_status;
    }

    public int getEmail_status() {
        return email_status;
    }

    public void setEmail_status(int email_status) {
        this.email_status = email_status;
    }

    public int getShopper_count() {
        return shopper_count;
    }

    public void setShopper_count(int shopper_count) {
        this.shopper_count = shopper_count;
    }

    public int getTrip_count() {
        return trip_count;
    }

    public void setTrip_count(int trip_count) {
        this.trip_count = trip_count;
    }

    public int getPre_shopper_count() {
        return pre_shopper_count;
    }

    public void setPre_shopper_count(int pre_shopper_count) {
        this.pre_shopper_count = pre_shopper_count;
    }

    public int getPre_trip_count() {
        return pre_trip_count;
    }

    public void setPre_trip_count(int pre_trip_count) {
        this.pre_trip_count = pre_trip_count;
    }

    public int getUpcoming_trip_count() {
        return upcoming_trip_count;
    }

    public void setUpcoming_trip_count(int upcoming_trip_count) {
        this.upcoming_trip_count = upcoming_trip_count;
    }

    public int getSocial_type() {
        return social_type;
    }

    public void setSocial_type(int social_type) {
        this.social_type = social_type;
    }
}

