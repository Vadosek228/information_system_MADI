package ru.vladislav_akulinin.mychat_version_2.Fragments;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.vladislav_akulinin.mychat_version_2.Notifications.MyResponse;
import ru.vladislav_akulinin.mychat_version_2.Notifications.Sender;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAATIwKwOs:APA91bECMPI5rSTDziRsZzwZwlFerYjMRrOvEGcZWuiMTYVM1l59KpI02wARHHfM1pB9sqnpH6tWB2X39EJ1MPGaH0XFPO10Dg8-AxiskmXe4qSONMWisrWo3d0nzI5M-6Y52LbMlMiM"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
