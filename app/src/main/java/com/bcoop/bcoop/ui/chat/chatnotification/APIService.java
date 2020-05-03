package com.bcoop.bcoop.ui.chat.chatnotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAACdM1mTE:APA91bGiFLw3uq78Ds4EooqW9uwOB0kcyjx9QQ9mI582Fmv5ZcjvhheGu10WOopxmMSutdKf_cJGiUpgty1y0ebccW25bVBnVkYST4eY7Ze-TydJFdAmT4L_MBxKogY6brPehcgc5ake"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
