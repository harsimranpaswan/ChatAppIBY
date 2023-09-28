package com.workshop.chatapp.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import com.workshop.chatapp.Notifications.Sender;
import com.workshop.chatapp.Notifications.MyResponse;
import retrofit2.http.POST;
import retrofit2.Call;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAApJr8BPc:APA91bGMFCSDDF-OaNR5lbV0gNOKHSbaduGa3UN4oOLvRQsWA-Dxpud3GezNPMN7RIZbqZ3m0U6z6miKJ7PTImei8jR45kaJNec0I_qdBwHEC-_dgTZaz3ENSKEX_ohkRMzARpt8XxmV"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}