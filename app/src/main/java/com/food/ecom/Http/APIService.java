package com.food.ecom.Http;

import com.food.ecom.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by SALAHUDIN` on 3/28/2018.
 */

public interface APIService{
    @Headers({"Authorization: key=AIzaSyCK-YrQA7YnRGnNuKFe9Xv6DPnSExIp2rU", "Content-Type:application/json"})
    @POST("/fcm/send")
    Call<Message> sendMessage(@Body Message message);

}
