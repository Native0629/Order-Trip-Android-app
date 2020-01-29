package com.business.order_trip.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAZzLGnyQ:APA91bH1vECgKjlIEoMpgibgPwGzfJe7azQ-cCinTXn5R744OzLDSroNkvuJN-k6HfZg21rfumdcJ9DZqZese2NwTN7OihKfr9nOkexJKEKsACej0mLmA11RR6z57NowhFQkOokISt08"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
