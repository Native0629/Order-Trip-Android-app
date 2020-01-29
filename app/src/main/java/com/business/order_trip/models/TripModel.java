package com.business.order_trip.models;

import java.io.Serializable;

public class TripModel implements Serializable {
    public String id, from, to, date, timestamp, status, trip_user_id, order_id, order_count , reward ;

    public TripModel(String id, String from, String to, String date, String timestamp, String status, String trip_user_id, String order_id, String order_count, String reward) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.timestamp = timestamp;
        this.status = status;
        this.trip_user_id = trip_user_id;
        this.order_id = order_id;
        this.order_count = order_count;
        this.reward = reward;
    }

    public TripModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrip_user_id() {
        return trip_user_id;
    }

    public void setTrip_user_id(String trip_user_id) {
        this.trip_user_id = trip_user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}

