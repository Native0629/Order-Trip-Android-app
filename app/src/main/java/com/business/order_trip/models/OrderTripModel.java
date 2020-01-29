package com.business.order_trip.models;

public class OrderTripModel {
    public String id, order_id, trip_id;

    public OrderTripModel(String id, String order_id, String trip_id) {
        this.id = id;
        this.order_id = order_id;
        this.trip_id = trip_id;
    }

    public OrderTripModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }
}
