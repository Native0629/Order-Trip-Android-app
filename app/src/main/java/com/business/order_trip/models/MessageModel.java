package com.business.order_trip.models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    public int _id;
    public String send_person;
    public String send_date;
    public String receive_date;
    public String send_time;
    public String receive_time;
    public String product_name;
    public String content;
    public String send_content;
    public String receive_content;
    public String user_id;
    public int type;// 0-chat, 1- notification, 2-


    public MessageModel(int _id, String send_person, String send_date, String receive_date, String send_time, String receive_time, String product_name, String send_content,
                        String receive_content, String user_id, int type) {
        this._id = _id;
        this.send_person = send_person;
        this.send_date = send_date;
        this.receive_date = receive_date;
        this.send_time = send_time;
        this.receive_time = receive_time;
        this.product_name = product_name;
        this.send_content = send_content;
        this.receive_content = receive_content;
        this.user_id = user_id;
        this.type = type;
    }
    public MessageModel() {
        this._id = 0;
        this.send_person = "";
        this.product_name = "";
        this.content = "";
    }
}
